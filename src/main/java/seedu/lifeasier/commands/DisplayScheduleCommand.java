package seedu.lifeasier.commands;

import seedu.lifeasier.storage.FileStorage;
import seedu.lifeasier.tasks.TaskList;
import seedu.lifeasier.parser.Parser;
import seedu.lifeasier.ui.Ui;
import seedu.lifeasier.ui.ScheduleUi;
import seedu.lifeasier.notes.NoteList;

import java.time.LocalDate;

public class DisplayScheduleCommand extends Command {

    private boolean isDisplayWeek;
    private final ScheduleUi scheduleUi = new ScheduleUi();

    private static final String NO_TASKS_TODAY_MESSAGE = "You have nothing on for today!";

    public DisplayScheduleCommand(String toDisplay) {
        this.isDisplayWeek = toDisplay.equals("week");
    }

    @Override
    public void execute(Ui ui, NoteList notes, TaskList tasks, FileStorage storage, Parser parser) {
        LocalDate currDate = LocalDate.now();
        if (isDisplayWeek) {
            scheduleUi.displayWeekSchedule(tasks);
        } else {
            int TaskCountToday = scheduleUi.getTaskCountForToday(tasks, currDate);

            if (TaskCountToday > 0) {
                System.out.println("Here is your schedule for today:");
                scheduleUi.displayDaySchedule(currDate, tasks);
            } else {
                System.out.println(NO_TASKS_TODAY_MESSAGE);
            }
        }
    }
}
