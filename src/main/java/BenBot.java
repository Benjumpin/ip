import java.util.*;

public class BenBot {
    public static void main(String[] args) {
        System.out.println("=================================\n" +
                            "Hello! I'm BenBot!\n" +
                                "What can i do for you?\n\n");
        Scanner sc = new Scanner(System.in);
        while (true) {
            String input = sc.nextLine();
            if (input.trim().equalsIgnoreCase("bye")) break;
            replyBox(input);

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


    public static void replyBox(String message) {
        System.out.println("=================================\n");
        System.out.println(message);
        System.out.println("=================================\n");
    }
}

