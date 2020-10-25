package seedu.lifeasier.storage;

import seedu.lifeasier.tasks.Deadline;
import seedu.lifeasier.tasks.Event;
import seedu.lifeasier.tasks.Lesson;
import seedu.lifeasier.tasks.Task;
import seedu.lifeasier.tasks.TaskList;
import seedu.lifeasier.ui.Ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The TaskStorage class handles the reading and writing of the save file for tasks.
 */
public class TaskStorage {

    public static final String BLANK_STRING = "";
    private static Logger logger = Logger.getLogger(TaskStorage.class.getName());
    private static final String SAVE_DELIMITER = "=-=";
    public static final String DEFAULT_DATA = "\n";

    private TaskList tasks;
    private String filePathTasks;
    private FileCommand fileCommand;
    private Ui ui;

    public TaskStorage(TaskList tasks, String filePathTasks) {
        this.tasks = tasks;
        this.filePathTasks = filePathTasks;
        this.fileCommand = new FileCommand();
        this.ui = new Ui();
    }

    /**
     * Handles saved task information.
     */
    protected void readTasksSave() {
        logger.log(Level.INFO, "Read Tasks save file start");
        try {
            File saveFile = new File(filePathTasks);

            assert saveFile.exists() : "Save file is supposed to exist";

            Scanner fileScanner = new Scanner(saveFile);
            createTaskList(fileScanner);

        } catch (IOException e) {
            ui.showFileReadError();
            logger.log(Level.WARNING, "Encountered error reading Tasks save file");
        }
        logger.log(Level.INFO, "Read Tasks save file end");
    }

    /**
     * Reads and adds all saved task information back into the program.
     *
     * @param fileScanner Scanner object to read file information.
     */
    protected void createTaskList(Scanner fileScanner) {
        logger.log(Level.INFO, "Rebuilding tasks list from save file");

        ArrayList<Task> taskList = tasks.getTaskList();

        try {
            while (fileScanner.hasNext()) {
                String taskInformation = fileScanner.nextLine();

                String[] taskComponents = taskInformation.split(SAVE_DELIMITER);
                checkForMissingDataInSave(taskComponents);
                String taskType = taskComponents[0];
                String taskDescription = taskComponents[2];
                Boolean taskStatus = fileCommand.convertToBoolean(taskComponents[1]);
                switch (taskType) {
                case "deadline":
                    rebuildDeadline(taskComponents, taskList, taskDescription, taskStatus);
                    break;
                case "event":
                    rebuildEvent(taskComponents, taskList, taskDescription, taskStatus);
                    break;
                case "lesson":
                    rebuildLesson(taskComponents, taskList, taskDescription, taskStatus);
                    break;
                default:
                    throw new StorageException();
                }
                logger.log(Level.INFO, "Rebuilt task: " + taskType);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            ui.showSaveDataMissingError();
            logger.log(Level.WARNING, "Missing data from save file");
        } catch (StorageException e) {
            ui.showUndeterminableTaskError();
            logger.log(Level.SEVERE, "Read task type failed");
        }
    }

    /**
     * Checks for missing information in each line of saved data read.
     *
     * @param taskComponents String array of read save data after separator has been removed.
     * @throws ArrayIndexOutOfBoundsException When data is missing.
     */
    private void checkForMissingDataInSave(String[] taskComponents) throws ArrayIndexOutOfBoundsException {
        for (String information : taskComponents) {
            if (information.equals(BLANK_STRING)) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
    }

    /**
     * Creates a new lesson object based on the save data read and adds it into the program.
     *
     * @param taskComponents String array of read save data after separator has been removed.
     * @param taskList List of tasks which the created object will be added into.
     * @param description The description of the task.
     * @param status The current status of the task.
     * @throws ArrayIndexOutOfBoundsException
     */
    protected void rebuildLesson(String[] taskComponents, ArrayList<Task> taskList, String description, Boolean status)
            throws ArrayIndexOutOfBoundsException {
        LocalDateTime lessonStartTime = fileCommand.convertToLocalDateTime(taskComponents[3]);
        LocalDateTime lessonEndTime = fileCommand.convertToLocalDateTime(taskComponents[4]);

        //Create new event in tasks
        taskList.add(new Lesson(description, lessonStartTime, lessonEndTime, status));
        tasks.increaseTaskCount();
    }

    /**
     * Creates a new lesson object based on the save data read and adds it into the program.
     *
     * @param taskComponents String array of read save data after separator has been removed.
     * @param taskList List of tasks which the created object will be added into.
     * @param description The description of the task.
     * @param status The current status of the task.
     * @throws ArrayIndexOutOfBoundsException
     */
    protected void rebuildEvent(String[] taskComponents, ArrayList<Task> taskList, String description, Boolean status)
            throws ArrayIndexOutOfBoundsException {
        LocalDateTime eventStartTime = fileCommand.convertToLocalDateTime(taskComponents[3]);
        LocalDateTime eventEndTime = fileCommand.convertToLocalDateTime(taskComponents[4]);

        //Create new event in tasks
        taskList.add(new Event(description, eventStartTime, eventEndTime, status));
        tasks.increaseTaskCount();
    }

    /**
     * Creates a new lesson object based on the save data read and adds it into the program.
     *
     * @param taskComponents String array of read save data after separator has been removed.
     * @param taskList List of tasks which the created object will be added into.
     * @param description The description of the task.
     * @param status The current status of the task.
     * @throws ArrayIndexOutOfBoundsException
     */
    protected void rebuildDeadline(String[] taskComponents, ArrayList<Task> taskList, String description,
                                   Boolean status) throws ArrayIndexOutOfBoundsException {
        LocalDateTime deadlineTimeInfo = fileCommand.convertToLocalDateTime(taskComponents[3]);

        //Create new deadline in tasks
        taskList.add(new Deadline(description, deadlineTimeInfo, status));
        tasks.increaseTaskCount();
    }

    /**
     * Writes information of TaskList onto the save file for storage whenever there is a change.
     */
    public void writeToTaskSaveFile() {
        try {
            FileWriter fileWriter = new FileWriter(filePathTasks, true);
            fileCommand.clearSaveFile(filePathTasks);

            String dataToSave;
            ArrayList<Task> taskList = tasks.getTaskList();

            //Append each tasks information into save file for tasks
            for (Task task : taskList) {
                String taskType = task.getType();
                switch (taskType) {
                case "deadline":
                    dataToSave = convertDeadlineToString(task, taskType);
                    break;
                case "event":
                    dataToSave = convertEventToString(task, taskType);
                    break;
                case "lesson":
                    dataToSave = convertLessonToString(task, taskType);
                    break;
                default:
                    dataToSave = DEFAULT_DATA;
                    fileWriter.write(dataToSave);
                    throw new StorageException();
                }
                fileWriter.write(dataToSave);
                logger.log(Level.INFO, "Write task to save: " + taskType);
            }
            fileWriter.close();
        } catch (IOException e) {
            ui.showFileWriteError();
            logger.log(Level.WARNING, "Unable to write to save file");
        } catch (ClassCastException e) {
            ui.showInvalidCastError();
            logger.log(Level.SEVERE, "Wrong class type passed, unable to cast");
        } catch (StorageException e) {
            ui.showUndeterminableTaskError();
            logger.log(Level.SEVERE, "Read task type failed");
        }
    }

    /**
     * Converts a lesson object into a save format.
     *
     * @param task Task object to be converted into a save format.
     * @param taskType The type of task being converted.
     * @return Formatted string in the save format.
     * @throws ClassCastException When the wrong class type is passed in and cannot be casted correctly.
     */
    protected String convertLessonToString(Task task, String taskType) throws ClassCastException {
        Lesson lesson = (Lesson) task;
        return taskType + SAVE_DELIMITER + task.getStatus() + SAVE_DELIMITER + task.getDescription() + SAVE_DELIMITER
                + lesson.getStart().format(FileCommand.DATE_TIME_FORMATTER) + SAVE_DELIMITER
                + lesson.getEnd().format(FileCommand.DATE_TIME_FORMATTER) + System.lineSeparator();
    }

    /**
     * Converts an event object into a save format.
     *
     * @param task Task object to be converted into a save format.
     * @param taskType The type of task being converted.
     * @return Formatted string in the save format.
     * @throws ClassCastException When the wrong class type is passed in and cannot be casted correctly.
     */
    protected String convertEventToString(Task task, String taskType) throws ClassCastException {
        Event event = (Event) task;
        return taskType + SAVE_DELIMITER + task.getStatus() + SAVE_DELIMITER + task.getDescription() + SAVE_DELIMITER
                + event.getStart().format(FileCommand.DATE_TIME_FORMATTER) + SAVE_DELIMITER
                + event.getEnd().format(FileCommand.DATE_TIME_FORMATTER) + System.lineSeparator();
    }

    /**
     * Converts a deadline object into a save format.
     *
     * @param task Task object to be converted into a save format.
     * @param taskType The type of task being converted.
     * @return Formatted string in the save format.
     * @throws ClassCastException When the wrong class type is passed in and cannot be casted correctly.
     */
    protected String convertDeadlineToString(Task task, String taskType) throws ClassCastException {
        Deadline deadline = (Deadline) task;
        return taskType + SAVE_DELIMITER + task.getStatus() + SAVE_DELIMITER + task.getDescription() + SAVE_DELIMITER
                + deadline.getBy().format(FileCommand.DATE_TIME_FORMATTER) + System.lineSeparator();
    }

}
