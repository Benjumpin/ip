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
 
            switch (command) {
            case BYE:
                return userInterface.showBye();

            case LIST:
                return userInterface.showList(tasks);

            case MARK:
                int markIndex = Integer.parseInt(arguments.trim()) - 1;
                Task itemToMark = tasks.getTask(markIndex);
                
                itemToMark.markDone();
                saveTask(tasks);
                
                return userInterface.showMark(itemToMark);

            case UNMARK:
                int unmarkIndex = Integer.parseInt(arguments.trim()) - 1;
                Task itemToUnmark = tasks.getTask(unmarkIndex);

                itemToUnmark.markUndone();
                saveTask(tasks);
                
                return userInterface.showUnMark(itemToUnmark);

            case TODO:
                if (arguments.isEmpty()) {
                    throw new BenBotException("Empty description");
                }
                
                Task todoTask = new Todo(arguments);
                tasks.addTask(todoTask);
                saveTask(tasks);
                
                return userInterface.showAddTask(todoTask, tasks.getSize());

            case DEADLINE:
                String[] deadlineItems = arguments.split(" /by ");
                if (deadlineItems.length < 2) {
                    throw new BenBotException("not valid deadline, use /by to specify date.\n");
                }
                
                String deadlineDescription = deadlineItems[0].trim();
                if (deadlineDescription.isEmpty()) {
                    throw new BenBotException("Empty description");
                    
                } else {
                    String by = deadlineItems[1].trim();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime datetime = LocalDateTime.parse(by, formatter);

                    if (datetime.isBefore(LocalDateTime.now())) {
                        throw new BenBotException("You cannot set a deadline in the past!");
                    }

                    Task deadlineTask = new Deadline(deadlineDescription, by);
                    tasks.addTask(deadlineTask);
                    saveTask(tasks);
                    
                    return userInterface.showDeadline(deadlineTask, tasks.getSize());
                }

            case EVENT:
                String[] eventItems = arguments.split(" /from | /to ");
                if (eventItems.length < 3) {
                    throw new BenBotException("not valid event, use /from and /to to specify date\n");
                }
                
                String eventDescription = eventItems[0].trim();
                if (eventDescription.isEmpty()) {
                    throw new BenBotException("Empty description");
                    
                } else {
                    String from = eventItems[1].trim();
                    String to = eventItems[2].trim();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime fromDate = LocalDateTime.parse(from, formatter);
                    LocalDateTime toDate = LocalDateTime.parse(to, formatter);

                    if (fromDate.isBefore(LocalDateTime.now())) {
                        throw new BenBotException("You cannot set a date in the past!");
                    }
                    if (toDate.isBefore(fromDate)) {
                        throw new BenBotException("Can't set end date before start date!");
                    }

                    Task eventTask = new Event(eventDescription, from, to);
                    tasks.addTask(eventTask);
                    saveTask(tasks);
                    
                    return userInterface.showEvent(eventTask, tasks.getSize());
                }

            case DELETE:
                if (arguments.isEmpty()) {
                    throw new BenBotException("Please provide number");
                }
                
                try {
                    int deleteIndex = Integer.parseInt(arguments.trim()) - 1;
                    if (deleteIndex < 0 || deleteIndex >= tasks.getSize()) {
                        throw new BenBotException("No such description in list");
                    }
                    Task deleteTask = tasks.removeTask(deleteIndex);
                    saveTask(tasks);
                    
                    return userInterface.showDeleteTask(deleteTask, tasks.getSize());
                } catch (NumberFormatException e) {
                    throw new BenBotException("Please enter a number");
                }

            case FIND:
                if (arguments.isEmpty()) {
                    throw new BenBotException("Enter task to search using find ___");
                }
                
                return userInterface.showFoundTasks(tasks, arguments);

            default:
                throw new BenBotException("stop blabbering!");
            }
        } catch (Exception exception) {
            return userInterface.showError(exception.getMessage());
        }
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