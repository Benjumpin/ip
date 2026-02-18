package benbot.parser;

import benbot.command.Command;

/**
 * Helper class to splits the input string
 */
public class Parser {

    /**
     * Identifies the command type from the user's input string.
     * The first word of the input is converted to uppercase and matched against the
     * Command enum values.
     *
     * @param fullCommand The raw input string provided by the user.
     * @return The matching Command enum; returns Command.UNKNOWN
     * if no match is found.
     */
    public static Command parseCommand(String fullCommand) {
        String[] commandParts = fullCommand.trim().split(" ", 2);
        try{
            return Command.valueOf(commandParts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }

    /**
     * Extracts the arguments part of the user's input string, excluding the command word.
     *
     * @param fullCommand The raw input string provided by the user.
     * @return The argument string if it exists, otherwise an empty string.
     */
    public static String parseArguments(String fullCommand) {
        String[] commandParts = fullCommand.trim().split(" ", 2);
        
        return commandParts.length > 1 ? commandParts[1] : "";

    }
}
