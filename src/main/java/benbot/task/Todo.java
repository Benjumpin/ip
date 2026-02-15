package benbot.task;

/**
 * Todo class for benbot
 * Used by "Todo 'item'"
 */
public class Todo extends Task {
    /**
     * Constructs a Todo task with the specified description.
     *
     * @param description The description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the task formatted for saving to a file.
     *
     * @return A formatted string representing the todo task for storage.
     */
    @Override
    public String changeFileFormat() {
        return "T | " + (isComplete ? "1" : "0") + " | " + item;
    }

    /**
     * Returns a string representation of the todo task, including its type and status.
     *
     * @return A string in the format [T][status] description.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
