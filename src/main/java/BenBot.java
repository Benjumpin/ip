import java.util.*;

public class BenBot {
    public static void main(String[] args) {
        System.out.println("=================================\n" +
                            "Hello! I'm BenBot!\n" +
                                "What can i do for you?\n\n");

        Scanner sc = new Scanner(System.in);
        List<String> ls = new ArrayList<>(100);

        while (true) {
            String input = sc.nextLine();
            if (input.trim().equalsIgnoreCase("bye")) break;
            else if(input.trim().equalsIgnoreCase("list")) {
                // print list
                StringBuilder inputls = new StringBuilder();
                int i = 1;
                for (String s : ls) {
                    inputls.append(i + ". " + ls.get(i-1) + "\n");
                    i++;
                }
                reply(inputls.toString());
            } else { // add input to list
                ls.add(input);
                reply("added: " + input);
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

