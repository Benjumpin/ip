package benbot.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Event class for benbot
 * Used by "Event 'item' /from 'yyyy-mm-dd hh-mm' /to 'yyyy-mm-dd hh-mm'"
 */
public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructs an Event task with the specified description, start time, and end time.
     *
     * @param description The description of the event.
     * @param from The start time string in "yyyy-MM-dd HH:mm" format.
     * @param to The end time string in "yyyy-MM-dd HH:mm" format.
     */
    public Event(String description, String from, String to) {
        super(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.from = LocalDateTime.parse(from, formatter);
        this.to = LocalDateTime.parse(to, formatter);
    }

    /**
     * Returns the event formatted for saving to a file.
     *
     * @return A formatted string representing the event task for storage.
     */
    @Override
    public String changeFileFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "E | " + (isComplete ? "1" : "0") + " | " + item + " | "
                + this.from + " | " + this.to.format(formatter);
    }

    /**
     * Returns a string representation of the event task, including its type, status, and duration.
     *
     * @return A string in the format [E][status] description (from: MMM d yyyy, h:mm a to: MMM d yyyy, h:mm a).
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
        return "[E]" + super.toString() + " (from: " + from.format(formatter)
                + " to: " + to.format(formatter) + ")";
    }
}
