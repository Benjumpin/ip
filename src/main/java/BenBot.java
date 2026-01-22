import java.util.*;

public class BenBot {
    public static void main(String[] args) {
        System.out.println("=================================\n" +
                            "Hello! I'm BenBot!\n" +
                                "What can i do for you?\n\n");

        Scanner sc = new Scanner(System.in);
        List<Task> ls = new ArrayList<>();

        while (true) {
            String inputString = sc.nextLine();
            String niceInputString = inputString.trim().toLowerCase();
            try {
                if (niceInputString.equals("bye")) break;

                else if (niceInputString.equals("list")) {
                    // print list
                    StringBuilder inputList = new StringBuilder();

                    for (int i = 0; i < ls.size(); i++) {
                        inputList.append(i + 1 + ". " + ls.get(i) + "\n");
                    }
                    reply(inputList.toString());

                } else if (niceInputString.startsWith("mark")) {
                    // mark item
                    String[] strings = niceInputString.split(" ");
                    Task itemToMark = ls.get(Integer.parseInt(strings[1]) - 1);
                    itemToMark.markDone();
                    reply("Nice! I've marked this task as done:\n " + itemToMark);

                } else if (niceInputString.startsWith("unmark")) {
                    // unmark item
                    String[] strings = niceInputString.split(" ");
                    Task itemToMark = ls.get(Integer.parseInt(strings[1]) - 1);
                    itemToMark.markUndone();
                    reply("OK, I've marked this task as not done yet:\n " + itemToMark);

                } else if (niceInputString.startsWith("todo")) {
                    if (niceInputString.length() <= 5) {
                        // STOP here and throw the error. The catch block above will handle it.
                        throw new BenBotExceptions("Empty description");
                    }
                    // todo item
                    String todoItem = inputString.substring(5).trim();
                    Task t = new Todo(todoItem);
                    ls.add(t);
                    reply("Got it. I've added this task:\n  " + t +
                            "\nNow you have " + ls.size() + " tasks in the list.");

                } else if (niceInputString.startsWith("deadline")) {
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
                        Task t = new Deadline(desc, by);
                        ls.add(t);
                        reply("Got it. I've added this task:\n  " + t +
                                "\nNow you have " + ls.size() + " tasks in the list.");
                    }

                } else if (niceInputString.startsWith("event")) {
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
                        Task t = new Event(desc, from, to);
                        ls.add(t);
                        reply("Got it. I've added this task:\n  " + t +
                                "\nNow you have " + ls.size() + " tasks in the list.");
                    }
                } else if (niceInputString.startsWith("delete")) {
                    String[] deleteItem = inputString.split(" ");
                    if (deleteItem.length < 2) {
                        throw new BenBotExceptions("Please provide number");
                    }
                    try {
                        int index = Integer.parseInt(deleteItem[1]);

                        if (index < 0 || index >= ls.size()) {
                            throw new BenBotExceptions("No such item in list");
                        }
                        Task t = ls.remove(index); // save removed item to print latre
                        reply("Noted. I've removed this task:\n  " + t +
                                "\nNow you have " + ls.size() + " tasks in the list.");
                    } catch (NumberFormatException e) {
                        throw new BenBotExceptions("Please enter a number");
                    }
                } else {
                    throw new BenBotExceptions("stop blabbering!");
                }
            } catch (BenBotExceptions e) {
                reply(e.getMessage());
            }
        }

        System.out.println("=================================\n" +
                             "Bye. Hope to see you again soon!");
    }


    public static void reply(String message) {
        System.out.println("=================================\n");
        System.out.println(message);
        System.out.println("=================================\n");
    }
}

