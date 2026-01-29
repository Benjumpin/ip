import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class BenBot {

    private static final String FILE_PATH = "./data/benbot.txt";
    private static Storage storage;

    public static void main(String[] args) {

        storage = new Storage(FILE_PATH);

        try {
            storage.createDataFileIfNeeded();
        } catch (IOException error) {
            System.out.println("Error initializing storage: " + error.getMessage());
            return;
        }

        System.out.println("=================================\n" +
                            "Hello! I'm BenBot!\n" +
                                "What can i do for you?\n" +
                                  "=================================");

        Scanner sc = new Scanner(System.in);
        List<Task> ls;

        try {
            ls = storage.loadFile();
        } catch (BenBotExceptions error) {
            System.out.println("Warning: " + error.getMessage());
            ls = new ArrayList<>();
        }

        while (true) {
            String inputString = sc.nextLine();
            String niceInputString = inputString.trim();
            String[] inputStringArray = niceInputString.split(" ", 2);
            String command = inputStringArray[0];
            Command com = getCommand(command);

            try {
                switch(com) {
                    case BYE:
                        break;

                    case LIST:
                        // print list
                        StringBuilder inputList = new StringBuilder();

                        for (int i = 0; i < ls.size(); i++) {
                            inputList.append(i + 1 + ". " + ls.get(i) + "\n");
                        }
                        reply(inputList.toString());
                        break;

                    case MARK:
                        // mark item
                        String[] strings = niceInputString.split(" ");
                        Task itemToMark = ls.get(Integer.parseInt(strings[1]) - 1);
                        itemToMark.markDone();
                        saveTask(ls);
                        reply("Nice! I've marked this task as done:\n " + itemToMark);
                        break;

                    case UNMARK:
                        // unmark item
                        String[] strings2 = niceInputString.split(" ");
                        Task itemToMark2 = ls.get(Integer.parseInt(strings2[1]) - 1);
                        itemToMark2.markUndone();
                        saveTask(ls);
                        reply("OK, I've marked this task as not done yet:\n " + itemToMark2);
                        break;

                    case TODO:
                        // todo item
                        if (niceInputString.length() <= 5) {
                            throw new BenBotExceptions("Empty description");
                        }
                        String todoItem = inputString.substring(5).trim();
                        Task t = new Todo(todoItem);
                        ls.add(t);
                        saveTask(ls);
                        reply("Got it. I've added this task:\n  " + t +
                                "\nNow you have " + ls.size() + " tasks in the list.");
                        break;

                    case DEADLINE:
                        // deadline item
                        String[] deadlineItem = inputString.split(" /by ");
                        if (deadlineItem.length < 2) {
                            throw new BenBotExceptions("not valid deadline, use /by to specify date.\n");
                        }
                        if (deadlineItem[0].length() <= 9) {
                            throw new BenBotExceptions("Empty description");
                        } else {
                            String desc = deadlineItem[0].substring(9).trim();
                            String by = deadlineItem[1].trim();
                            Task td = new Deadline(desc, by);
                            ls.add(td);
                            saveTask(ls);
                            reply("Got it. I've added this task:\n  " + td +
                                    "\nNow you have " + ls.size() + " tasks in the list.");
                        }
                        break;

                    case EVENT:
                        // event item
                        String[] eventItem = inputString.split(" /from | /to ");
                        if (eventItem.length < 3) {
                            throw new BenBotExceptions("not valid event, use /from and /to to specify date\n");
                        }
                        if (eventItem[0].length() <= 6) {
                            throw new BenBotExceptions("Empty description");
                        } else {
                            String desc = eventItem[0].substring(6).trim();
                            String from = eventItem[1].trim();
                            String to = eventItem[2].trim();
                            Task te = new Event(desc, from, to);
                            ls.add(te);
                            saveTask(ls);
                            reply("Got it. I've added this task:\n  " + te +
                                    "\nNow you have " + ls.size() + " tasks in the list.");
                        }
                        break;

                    case DELETE:
                        // delete item
                        String[] deleteItem = inputString.split(" ");
                        if (deleteItem.length < 2) {
                            throw new BenBotExceptions("Please provide number");
                        }
                        try {
                            int index = Integer.parseInt(deleteItem[1]) - 1;

                            if (index < 0 || index >= ls.size()) {
                                throw new BenBotExceptions("No such item in list");
                            }
                            Task tdel = ls.remove(index); // save removed item to print later
                            saveTask(ls);
                            reply("Noted. I've removed this task:\n  " + tdel +
                                    "\nNow you have " + ls.size() + " tasks in the list.");
                        } catch (NumberFormatException e) {
                            throw new BenBotExceptions("Please enter a number");
                        }
                        break;
                    default:
                        throw new BenBotExceptions("stop blabbering!");
                }
            } catch (BenBotExceptions e) {
                reply(e.getMessage());
            }
            if (com == Command.BYE) break;

        }

        System.out.println("=================================\n" +
                             "Bye. Hope to see you again soon!");
    }


    public static void reply(String message) {
        System.out.println("=================================");
        System.out.println(message);
        System.out.println("=================================\n");
    }

    // Parsing command
    private static Command getCommand(String commandWord) {
        try {
            return Command.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }

    /*
    Saving the current list of tasks into the storage txt file
     */
    private static void saveTask(List<Task> tasks) {
        try {
            storage.saveFile(tasks);
        } catch (IOException error) {
            System.out.println(":C Error saving tasks to file: " + error.getMessage());
        }
    }

}

