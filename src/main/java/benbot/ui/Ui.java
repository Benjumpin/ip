package benbot.ui;

import benbot.task.Task;
import benbot.task.TaskList;

/**
 * Handles the user interface messages for the BenBot application.
 */
public class Ui {
    public String showBye() {
        return "Bye. Hope to see you again soon!";
    }

    public String showAddTask(Task task, int totalTasks) {
        return "Got it. I've added this task:\n  " + task +
                "\nNow you have " + totalTasks + " tasks in the list.";
    }

    public String showDeleteTask(Task task, int totalTasks) {
        return "Noted. I've removed this task:\n  " + task +
                "\nNow you have " + totalTasks + " tasks in the list.";
    }

    public String showMark(Task itemToMark) {
        return "Nice! I've marked this task as done:\n " + itemToMark;
    }

    public String showUnMark(Task itemToMark) {
        return "OK, I've marked this task as not done yet:\n " + itemToMark;
    }

    public String showEvent(Task item, int size) {
        return "Got it. I've added this task:\n  " + item +
                "\nNow you have " + size + " tasks in the list.";
    }

    public String showDeadline(Task item, int size) {
        return "Got it. I've added this task:\n  " + item +
                "\nNow you have " + size + " tasks in the list.";
    }

    public String showError(String message) {
        return "Error " + message;
    }

    public String showList(TaskList taskList) {
        if (taskList.getSize() == 0) {
            return "Your list is currently empty.";
        }
        
        StringBuilder stringBuilder = new StringBuilder("Here are the tasks in your list:\n");
        
        for (int i = 0; i < taskList.getSize(); i++) {
            stringBuilder.append((i + 1)).append(". ").append(taskList.getTask(i)).append("\n");
        }
        
        return stringBuilder.toString().trim();
    }

    public String showFoundTasks(TaskList taskList, String keyword) {
        StringBuilder stringBuilder = new StringBuilder("Here are the matching tasks in your list:\n");
        int matchCount = 0;
        
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
            
            if (task.toString().contains(keyword)) {
                matchCount++;
                stringBuilder.append(matchCount).append(".").append(task).append("\n");
            }
        }
        
        if (matchCount == 0) {
            return "No tasks found matching: " + keyword;
        }
        
        return stringBuilder.toString().trim();
    }
    
    public String showHelp() {
        return "Here are the available commands:\n"
                + "<time> format: <yyyy-MM-dd HH:mm>\n"
                + "1. todo <desc> - Adds a todo task\n"
                + "2. deadline <desc> /by <time> - Adds a deadline\n"
                + "3. event <desc> /from <time> /to <time> - Adds an event\n"
                + "4. list - Shows all tasks\n"
                + "5. mark <index> - Marks task as done\n"
                + "6. unmark <index> - Marks task as undone\n"
                + "7. delete <index> - Removes a task\n"
                + "8. find <keyword> - Searches for tasks\n"
                + "9. bye - Exits the app";
    }
}