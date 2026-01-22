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
                String[] num = niceInputString.split(" ");
                Task itemToMark = ls.get(Integer.parseInt(num[1]) - 1);
                itemToMark.markDone();
                reply("Nice! I've marked this task as done:\n " + itemToMark);

            } else if (niceInputString.startsWith("unmark")) {
                // mark item
                String[] num = niceInputString.split(" ");
                Task itemToMark = ls.get(Integer.parseInt(num[1]) - 1);
                itemToMark.markUndone();
                reply("Nice! I've unmarked this task as done:\n " + itemToMark);

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

