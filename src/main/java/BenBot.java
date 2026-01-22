import java.util.*;

public class BenBot {
    public static void main(String[] args) {
        System.out.println("=================================\n" +
                            "Hello! I'm BenBot!\n" +
                                "What can i do for you?\n\n");

        Scanner sc = new Scanner(System.in);
        List<Task> ls = new ArrayList<>(100);

        while (true) {
            String inputString = sc.nextLine();
            String niceInputString = inputString.trim().toLowerCase();
            if (niceInputString.equals("bye")) break;

            else if(niceInputString.equals("list")) {
                // print list
                StringBuilder inputList = new StringBuilder();

                for (int i = 0; i< ls.size(); i++) {
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

            } else if (niceInputString.startsWith("todo")) {
                // todo item
                String todoItem = inputString.substring(5).trim();
                Task t = new Todo(todoItem);
                ls.add(t);
                reply("Got it. I've added this task:\n  " + t +
                        "\nNow you have " + ls.size() + " tasks in the list.");

            } else if (niceInputString.startsWith("deadline")) {
                // deadline item
                String[] deadlineItem = inputString.split(" /by ");
                if (deadlineItem.length < 2) reply("not valid deadline, use /by to specify date.\n");
                else {
                    String desc = deadlineItem[0].substring(9).trim();
                    String from = deadlineItem[1].trim();
                    Task t = new Deadline(desc, from);
                    ls.add(t);
                    reply("Got it. I've added this task:\n  " + t +
                            "\nNow you have " + ls.size() + " tasks in the list.");
                }

            } else if (niceInputString.startsWith("event")) {
                // event item
                String[] eventItem = inputString.split(" /from | /to ");
                if (eventItem.length < 3) reply("not valid event, use /from and /to to specify date\n");
                else {
                    String desc = eventItem[0].substring(6).trim();
                    String from = eventItem[1].trim();
                    String to = eventItem[2].trim();
                    Task t = new Event(desc, from, to);
                    ls.add(t);
                    reply("Got it. I've added this task:\n  " + t +
                            "\nNow you have " + ls.size() + " tasks in the list.");
                }
            } else {
                // add input to list
                Task inputTask = new Task(inputString);
                ls.add(inputTask);
                reply("added: " + inputString);
            }

        }

        System.out.println("=================================\n" +
                             "Bye. Hope to see you again soon!\n" +
                                "╥━━━━━━━━╭━━╮━━┳\n" +
                                "╢╭╮╭━━━━━┫┃▋▋━▅┣\n" +
                                "╢┃╰┫┈┈┈┈┈┃┃┈┈╰┫┣\n" +
                                "╢╰━┫┈┈┈┈┈╰╯╰┳━╯┣\n" +
                                "╢┊┊┃┏┳┳━━┓┏┳┫┊┊┣\n" +
                                "╨━━┗┛┗┛━━┗┛┗┛━━┻\n");
    }


    public static void reply(String message) {
        System.out.println("=================================\n");
        System.out.println(message);
        System.out.println("=================================\n");
    }
}

