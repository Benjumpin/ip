package benbot.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    protected LocalDateTime by;

    public Deadline(String description, String by) {
        super(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.by = LocalDateTime.parse(by, formatter);
    }

    @Override
    public String changeFileFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "D | " + (isComplete ? "1" : "0") + " | " + item + " | " + this.by.format(formatter);
    }

    @Override
    public String toString() {
        String formattedDate = by.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a"));
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }
}
