package benbot.task;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String changeFileFormat() {
        return "T | " + (isComplete ? "1" : "0") + " | " + item;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
