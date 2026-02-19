package benbot;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import benbot.command.Command;
import benbot.exception.BenBotException;
import benbot.parser.Parser;
import benbot.storage.Storage;
import benbot.task.Deadline;
import benbot.task.Event;
import benbot.task.Task;
import benbot.task.TaskList;
import benbot.task.Todo;
import benbot.ui.Ui;


/**
 * The main logic for the BenBot chatbot.
 */
public class BenBot {

    private static final String FILE_PATH = "./data/benbot.txt";
    private Storage storage;
    private TaskList tasks;
    private Ui userInterface;

    /**
     * Constructor for BenBot.
     * Initializes the storage and loads existing tasks.
     */
    public BenBot() {
        userInterface = new Ui();
        storage = new Storage(FILE_PATH);
        try {
            storage.createDataFileIfNeeded();
            tasks = new TaskList(storage.loadFile());
            
        } catch (IOException | BenBotException error) {
            tasks = new TaskList();
        }
    }

    /**
     * Returns a welcome message and the current task list.
     */
    public String getWelcome() {
        StringBuilder sb = new StringBuilder("Hello! I'm BenBot.\n");
        sb.append(userInterface.showList(tasks));
        sb.append("\n\nTip: Type 'help' if you're not sure how to start!");
        
        return sb.toString();
    }
    
    /**
     * Generates a response based on user input.
     *
     * @param input The raw string input from the user.
     * @return The formatted response string from BenBot.
     */
    public String getResponse(String input) {
        assert input != null: "Input string to getResponse cannot be null";
        
        Command command = Parser.parseCommand(input);
        String arguments = Parser.parseArguments(input);
        
        assert command != null: "getCommand should return a Command enum";
        
        try {
            return executeCommand(command, arguments);
        } catch (BenBotException exception) {
            return userInterface.showError(exception.getMessage());
        } catch (Exception exception) {
            return userInterface.showError("Sorry! Unexpected error occured: " + exception.getMessage());
        }
    }
    
    private String executeCommand(Command command, String arguments) throws BenBotException {
        switch (command) {
        case BYE:
            return userInterface.showBye();
        case LIST:
            return userInterface.showList(tasks);
        case MARK:
            return handleMark(arguments);
        case UNMARK:
            return handleUnmark(arguments);
        case TODO:
            return handleTodo(arguments);
        case DEADLINE:
            return handleDeadline(arguments);
        case EVENT:
            return handleEvent(arguments);
        case DELETE:
            return handleDelete(arguments);
        case FIND:
            return handleFind(arguments);
        case HELP:
            return userInterface.showHelp();
        default:
            throw new BenBotException("stop blabbering!");
        }
    }
    
    private String handleMark(String arguments) {
        int index = Integer.parseInt(arguments.trim()) - 1;
        Task task = tasks.getTask(index);

        task.markDone();
        saveTask(tasks);

        return userInterface.showMark(task);
    }
    
    private String handleUnmark(String arguments) {
        int index = Integer.parseInt(arguments.trim()) - 1;
        Task task = tasks.getTask(index);

        task.markUndone();
        saveTask(tasks);

        return userInterface.showUnMark(task);
    }
    
    private String handleTodo(String arguments) throws BenBotException {
        if (arguments.isEmpty()) {
            throw new BenBotException("Empty description");
        }

        Task todoTask = new Todo(arguments);
        tasks.addTask(todoTask);
        saveTask(tasks);

        return userInterface.showAddTask(todoTask, tasks.getSize());
    }

    private String handleDeadline(String arguments) throws BenBotException {
        String[] deadlineItems = arguments.split(" /by ");
        if (deadlineItems.length < 2) {
            throw new BenBotException("not valid deadline, use /by to specify date.\n");
        }

        String description = deadlineItems[0].trim();
        if (description.isEmpty()) {
            throw new BenBotException("Empty description");
        }

        String by = deadlineItems[1].trim();
        Task deadlineTask = new Deadline(description, by);
        tasks.addTask(deadlineTask);
        saveTask(tasks);

        return userInterface.showDeadline(deadlineTask, tasks.getSize());
    }

    private String handleEvent(String arguments) throws BenBotException {
        String[] parts = arguments.split(" /from | /to ");
        if (parts.length < 3) {
            throw new BenBotException("not valid event, use /from and /to to specify dates\n");
        }

        String description = parts[0].trim();
        if (description.isEmpty()) {
            throw new BenBotException("Empty description");
        }

        String from = parts[1].trim();
        String to = parts[2].trim();

        // Add date validation logic here if needed...

        Task eventTask = new Event(description, from, to);
        tasks.addTask(eventTask);
        saveTask(tasks);

        return userInterface.showEvent(eventTask, tasks.getSize());
    }

    private String handleDelete(String arguments) throws BenBotException {
        if (arguments.isEmpty()) {
            throw new BenBotException("Please provide a task number");
        }
        
        int index;
        try {
            index = Integer.parseInt(arguments.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new BenBotException("Please enter a valid number");
        }
        
        if (index < 0 || index >= tasks.getSize()) {
            throw new BenBotException("No such task in list");
        }

        Task task = tasks.removeTask(index);
        saveTask(tasks);

        return userInterface.showDeleteTask(task, tasks.getSize());
    }

    private String handleFind(String arguments) throws BenBotException {
        if (arguments.isEmpty()) {
            throw new BenBotException("Enter task to search using find <keyword>");
        }

        return userInterface.showFoundTasks(tasks, arguments);
    }
    
    private void saveTask(TaskList tasks) {
        assert tasks != null: "Attempted to save a null TaskList to storage";
        
        try {
            storage.saveFile(tasks.getAllTasks());
        } catch (IOException exception) {
            System.out.println("Error saving tasks to files: " + exception.getMessage());
        }
    }
}