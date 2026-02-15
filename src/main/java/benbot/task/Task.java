package benbot.task;

/**
 * Serves as a base class for more specific task types like Todo, Deadline, and Event.
 */
public class Task {
    protected boolean isComplete;
    protected String item;

    /**
     * Constructs a new Task with the specified description.
     *
     * @param item The description of the task.
     */
    public Task(String item) {
        assert item != null && !item.isBlank(): " Task description cannot be empty";
        this.item = item;
        this.isComplete = false;
    }

    /**
     * Returns a string symbol representing the completion status of the task.
     *
     * @return "X" if completed, " " otherwise.
     */
    public String getStatus() {
        return (isComplete ? "X" : " ");
    }

    /**
     *mark task as undone
     */
    public void markDone () {
        this.isComplete = true;
    }

    /**
     * mark task as done
     */
    public void markUndone () {
        this.isComplete = false;
    }

    /**
     * Formats the task into a string suitable for file storage.
     * This method is intended to be overridden by child classes.
     *
     * @return A formatted string representation of the task for storage.
     */
    public String changeFileFormat() {
        return String.format("? | %d | %s", this.isComplete ? 1 : 0, this.item);
    }

    /**
     * Returns a string representation of the task, showing its completion status and description.
     *
     * @return A formatted string like "[X] read book".
     */
    @Override
    public String toString() {
        return "[" + getStatus() + "] " + item;
    }
}
