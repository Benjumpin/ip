package benbot;

import benbot.parser.Parser;
import benbot.task.*;
import benbot.storage.Storage;
import benbot.exception.BenBotExceptions;
import benbot.command.Command;
import benbot.ui.Ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

public class BenBot {

    private static final String FILE_PATH = "./data/benbot.txt";
    private Storage storage;
    private TaskList ls;
    private Ui ui;

    /**
     * Constructor for BenBot.
     * Initializes the storage and loads existing tasks.
     */
    public BenBot() {
        ui = new Ui();
        storage = new Storage(FILE_PATH);
        try {
            storage.createDataFileIfNeeded();
            ls = new TaskList(storage.loadFile());
        } catch (IOException | BenBotExceptions error) {
            ls = new TaskList();
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        assert input != null: "Input string to getResponse cannot be null";
        Command com = Parser.parseCommand(input);
        String arguments = Parser.parseArguments(input);
//        String niceInputString = input.trim();
//        String[] inputStringArray = niceInputString.split(" ", 2);
//        assert inputStringArray.length > 0: "input parsing failed to produce any tokens";
//        String command = inputStringArray[0];
//        Command com = getCommand(command);
        assert com != null: "getCommand should return a Command enum";
        try {
            int index;
            Task itemToMark;
            String desc;
            switch (com) {
                case BYE:
                    return ui.showBye();

                case LIST:
                    return ui.showList(ls);

                case MARK:
                    index = Integer.parseInt(arguments.trim()) - 1;
                    itemToMark = ls.getTask(index);
                    itemToMark.markDone();
                    saveTask(ls);
                    return ui.showMark(itemToMark);

                case UNMARK:
                    index = Integer.parseInt(arguments.trim()) - 1;
                    itemToMark = ls.getTask(index);
                    itemToMark.markUndone();
                    saveTask(ls);
                    return ui.showUnMark(itemToMark);

                case TODO:
                    if (arguments.isEmpty()) {
                        throw new BenBotExceptions("Empty description");
                    }
                    Task t = new Todo(arguments);
                    ls.addTask(t);
                    saveTask(ls);
                    return ui.showAddTask(t, ls.getSize());

                case DEADLINE:
                    String[] deadlineItem = arguments.split(" /by ");
                    if (deadlineItem.length < 2) {
                        throw new BenBotExceptions("not valid deadline, use /by to specify date.\n");
                    }
                    desc = deadlineItem[0].trim();
                    if (desc.isEmpty()) {
                        throw new BenBotExceptions("Empty description");
                    } else {
                        String by = deadlineItem[1].trim();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime datetime = LocalDateTime.parse(by, formatter);

                        if (datetime.isBefore(LocalDateTime.now())) {
                            throw new BenBotExceptions("You cannot set a deadline in the past!");
                        }

                        Task td = new Deadline(desc, by);
                        ls.addTask(td);
                        saveTask(ls);
                        return ui.showDeadline(td, ls.getSize());
                    }

                case EVENT:
                    String[] eventItem = arguments.split(" /from | /to ");
                    if (eventItem.length < 3) {
                        throw new BenBotExceptions("not valid event, use /from and /to to specify date\n");
                    }
                    desc = eventItem[0].trim();

                    if (desc.isEmpty()) {
                        throw new BenBotExceptions("Empty description");
                    } else {
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
                        ls.addTask(te);
                        saveTask(ls);
                        return ui.showEvent(te ,ls.getSize());
                    }

                case DELETE:
                    if (arguments.isEmpty()) {
                        throw new BenBotExceptions("Please provide number");
                    }
                    try {
                        index = Integer.parseInt(arguments.trim()) - 1;

                        if (index < 0 || index >= ls.getSize()) {
                            throw new BenBotExceptions("No such item in list");
                        }
                        Task taskDel = ls.removeTask(index);
                        saveTask(ls);
                        return ui.showDeleteTask(taskDel, ls.getSize());
                    } catch (NumberFormatException e) {
                        throw new BenBotExceptions("Please enter a number");
                    }

                case FIND:
                    if (arguments.isEmpty()) {
                        throw new BenBotExceptions("Enter task to search using find ___");
                    }
                   return ui.showFoundTasks(ls, arguments);

                default:
                    throw new BenBotExceptions("stop blabbering!");
            }

        } catch (Exception e) {
            return ui.showError(e.getMessage());
        }
    }

    private void saveTask(TaskList tasks) {
        assert tasks != null: "Attempted to save a null TaskList to storage";
        try {
            storage.saveFile(tasks.getAllTasks());
        } catch (IOException error) {
            System.out.println("Error saving tasks to files: " + error.getMessage());
        }
    }
}