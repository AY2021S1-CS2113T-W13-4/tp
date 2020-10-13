package seedu.lifeasier.ui;

import seedu.lifeasier.tasks.Deadline;
import seedu.lifeasier.tasks.Event;
import seedu.lifeasier.tasks.Lesson;
import seedu.lifeasier.tasks.Task;
import seedu.lifeasier.tasks.TaskList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ScheduleUi {
    public void displayWeekSchedule(LocalDate startOfWeek, TaskList tasks) {
        for (int i = 0; i < 7; i++) {
            displayDayOfWeek(i);
            displayDaySchedule(startOfWeek.plus(i, ChronoUnit.DAYS), tasks);
            System.out.println();
        }
    }

    public void displayDayOfWeek(int i) {
        LocalDateTime datePointer = LocalDateTime.now().plus(i, ChronoUnit.DAYS);
        System.out.println(datePointer.getDayOfWeek());
    }

    public void displayDaySchedule(LocalDate date, TaskList tasks) {
        for (int i = 0; i < tasks.getTaskCount(); i++) {
            Task t = tasks.getTask(i);
            LocalDateTime startDateTime = getStart(t);
            LocalDateTime endDateTime = getEnd(t);
            if (startDateTime.toLocalDate().equals(date)) {
                printWithScheduleFormat(t, startDateTime, endDateTime);
            }
        }
    }

    public void printWithScheduleFormat(Task t, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String startDateTimeString = getTimeStamp(startDateTime);
        String endDateTimeString = (endDateTime == null) ? "      " : ("-" + getTimeStamp(endDateTime));
        System.out.println(startDateTimeString + endDateTimeString + "  " + t.getDescription());
    }

    public LocalDateTime getStart(Task t) {
        if (t instanceof Lesson) {
            return ((Lesson) t).getStart();
        } else if (t instanceof Event) {
            return ((Event) t).getStart();
        }
        return ((Deadline) t).getBy();
    }

    public LocalDateTime getEnd(Task t) {
        if (t instanceof Lesson) {
            return ((Lesson) t).getEnd();
        } else if (t instanceof Event) {
            return ((Event) t).getEnd();
        }
        return null;
    }

    public String getTimeStamp(LocalDateTime timedItem) {
        return timedItem.toLocalTime().toString();
    }

    public int getTaskCountForToday(TaskList tasks, LocalDate date) {
        int count = 0;
        for (int i = 0; i < tasks.getTaskCount(); i++) {
            Task t = tasks.getTask(i);
            if (getStart(t).toLocalDate().equals(date)) {
                count++;
            }
        }
        return count;
    }
}
