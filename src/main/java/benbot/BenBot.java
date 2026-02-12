package benbot;

import benbot.task.Task;
import benbot.task.Todo;
import benbot.task.Deadline;
import benbot.task.Event;
import benbot.storage.Storage;
import benbot.exception.BenBotExceptions;
import benbot.command.Command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class BenBot {

    private static final String FILE_PATH = "./data/benbot.txt";
    private Storage storage;
    private List<Task> ls;

    /**
     * Constructor for BenBot.
     * Initializes the storage and loads existing tasks.
     */
    public BenBot() {
        storage = new Storage(FILE_PATH);
        try {
            storage.createDataFileIfNeeded();
            ls = storage.loadFile();
        } catch (IOException | BenBotExceptions error) {
            ls = new ArrayList<>();
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        assert input != null: "Input string to getResponse cannot be null";
        String niceInputString = input.trim();
        String[] inputStringArray = niceInputString.split(" ", 2);
        assert inputStringArray.length > 0: "input parsing failed to produce any tokens";
        String command = inputStringArray[0];
        Command com = getCommand(command);
        assert com != null: "getCommand should return a Command enum";
        try {
            switch (com) {
                case BYE:
                    return "Bye. Hope to see you again soon!";

                case LIST:
                    StringBuilder inputList = new StringBuilder();
                    for (int i = 0; i < ls.size(); i++) {
                        inputList.append(i + 1 + ". " + ls.get(i) + "\n");
                    }
                    return inputList.toString();

                case MARK:
                    String[] strings = niceInputString.split(" ");
                    Task itemToMark = ls.get(Integer.parseInt(strings[1]) - 1);
                    itemToMark.markDone();
                    saveTask(ls);
                    return "Nice! I've marked this task as done:\n " + itemToMark;

                case UNMARK:
                    String[] strings2 = niceInputString.split(" ");
                    Task itemToMark2 = ls.get(Integer.parseInt(strings2[1]) - 1);
                    itemToMark2.markUndone();
                    saveTask(ls);
                    return "OK, I've marked this task as not done yet:\n " + itemToMark2;

                case TODO:
                    if (niceInputString.length() <= 5) {
                        throw new BenBotExceptions("Empty description");
                    }
                    String todoItem = input.substring(5).trim();
                    Task t = new Todo(todoItem);
                    ls.add(t);
                    saveTask(ls);
                    return "Got it. I've added this task:\n  " + t +
                            "\nNow you have " + ls.size() + " tasks in the list.";

                case DEADLINE:
                    String[] deadlineItem = input.split(" /by ");
                    if (deadlineItem.length < 2) {
                        throw new BenBotExceptions("not valid deadline, use /by to specify date.\n");
                    }
                    if (deadlineItem[0].length() <= 9) {
                        throw new BenBotExceptions("Empty description");
                    } else {
                        String desc = deadlineItem[0].substring(9).trim();
                        String by = deadlineItem[1].trim();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime datetime = LocalDateTime.parse(by, formatter);

                        if (datetime.isBefore(LocalDateTime.now())) {
                            throw new BenBotExceptions("You cannot set a deadline in the past!");
                        }

                        Task td = new Deadline(desc, by);
                        ls.add(td);
                        saveTask(ls);
                        return "Got it. I've added this task:\n  " + td +
                                "\nNow you have " + ls.size() + " tasks in the list.";
                    }

                case EVENT:
                    String[] eventItem = input.split(" /from | /to ");
                    if (eventItem.length < 3) {
                        throw new BenBotExceptions("not valid event, use /from and /to to specify date\n");
                    }
                    if (eventItem[0].length() <= 6) {
                        throw new BenBotExceptions("Empty description");
                    } else {
                        String desc = eventItem[0].substring(6).trim();
                        String from = eventItem[1].trim();
                        String to = eventItem[2].trim();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime fromDate = LocalDateTime.parse(from, formatter);
                        LocalDateTime toDate = LocalDateTime.parse(to, formatter);

                        if (fromDate.isBefore(LocalDateTime.now())) {
                            throw new BenBotExceptions("You cannot set a date in the past!");
                        }

                        if (toDate.isBefore(fromDate)) {
                            throw new BenBotExceptions("Can't set end date before start date!");
                        }

                        Task te = new Event(desc, from, to);
                        ls.add(te);
                        saveTask(ls);
                        return "Got it. I've added this task:\n  " + te +
                                "\nNow you have " + ls.size() + " tasks in the list.";
                    }

                case DELETE:
                    String[] deleteItem = input.split(" ");
                    if (deleteItem.length < 2) {
                        throw new BenBotExceptions("Please provide number");
                    }
                    try {
                        int index = Integer.parseInt(deleteItem[1]) - 1;

                        if (index < 0 || index >= ls.size()) {
                            throw new BenBotExceptions("No such item in list");
                        }
                        Task tdel = ls.remove(index);
                        saveTask(ls);
                        return "Noted. I've removed this task:\n  " + tdel +
                                "\nNow you have " + ls.size() + " tasks in the list.";
                    } catch (NumberFormatException e) {
                        throw new BenBotExceptions("Please enter a number");
                    }

                case FIND:
                    if (niceInputString.length() <= 5) {
                        throw new BenBotExceptions("Enter task to search using find ___");
                    }

                    String keyword = niceInputString.substring(5).trim();
                    StringBuilder foundTasks = new StringBuilder("Here are the matching tasks:\n");
                    int matchCount = 0;

                    for (int i = 0; i < ls.size(); i++) {
                        Task task = ls.get(i);
                        if (task.toString().contains(keyword)) {
                            matchCount++;
                            foundTasks.append(matchCount + "." + task + "\n");
                        }
                    }

                    if (matchCount == 0) {
                        return "No task found";
                    } else {
                        return foundTasks.toString();
                    }

                default:
                    throw new BenBotExceptions("stop blabbering!");
            }

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private Command getCommand(String commandWord) {
        try {
            return Command.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }
    private void saveTask(List<Task> tasks) {
        assert tasks != null: "Attempted to save a null task list to storage";
        try {
            storage.saveFile(tasks);
        } catch (IOException error) {
            System.out.println("Error saving tasks to files: " + error.getMessage());
        }
    }
}