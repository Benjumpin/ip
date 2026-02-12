package benbot.task;

public class Task {
    protected boolean isComplete;
    protected String item;

    public Task(String item) {
        assert item != null && !item.isBlank(): " Task description cannot be empty";
        this.item = item;
        this.isComplete = false;
    }

    public String getStatus() {
        return (isComplete ? "X" : " ");
    }

    public void markDone () {
        this.isComplete = true;
    }

    public void markUndone () {
        this.isComplete = false;
    }

    /*
    Returns a task in string format for file storage
    To be overridden in child class
     */
    public String changeFileFormat() {
        return String.format("? | %d | %s", this.isComplete ? 1 : 0, this.item);
    }

    @Override
    public String toString() {
        return "[" + getStatus() + "] " + item;
    }
}
