package benbot.parser;

import benbot.command.Command;

public class Parser {
    public static Command parseCommand(String fullCommand) {
        String[] split = fullCommand.trim().split(" ", 2);
        try{
            return Command.valueOf(split[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }

    public static String parseArguments(String fullCommand) {
        String[] split = fullCommand.trim().split(" ", 2);
        return split.length > 1 ? split[1] : "";

    }
}
