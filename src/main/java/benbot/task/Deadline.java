package benbot.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Deadline class for benbot
 * Used by "Deadline 'description' /by 'yyyy-mm-dd hh-mm'"
 */
public class Deadline extends Task {

    protected LocalDateTime by;

    /**
     * Constructs a Deadline task with the specified description and deadline string.
     *
     * @param description The description of the task.
     * @param by The deadline date string in "yyyy-MM-dd HH:mm" format.
     */
    public Deadline(String description, String by) {
        super(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.by = LocalDateTime.parse(by, formatter);
    }

    /**
     * Returns the task formatted for saving to a file.
     *
     * @return A formatted string representing the deadline task for storage.
     */
    @Override
    public String changeFileFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "D | " + (isComplete ? "1" : "0") + " | " + description + " | " + this.by.format(formatter);
    }

    /**
     * Returns a string representation of the deadline task, including its type, status, and due date.
     *
     * @return A string in the format [D][status] description (by: MMM d yyyy, h:mm a).
     */
    @Override
    public String toString() {
        String formattedDate = by.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a"));
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }
}
