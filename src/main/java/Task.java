import javax.swing.*;
import java.util.*;
public class Task {
    protected boolean isComplete;
    protected String item;

    public Task(String item) {
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
    @Override
    public String toString() {
        return "[" + getStatus() + "] " + item;
    }
}
