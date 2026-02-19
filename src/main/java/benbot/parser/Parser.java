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
        String commandWord = getPart(fullCommand, 0);
        try{
            return Command.valueOf(commandWord.toUpperCase());
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
        return getPart(fullCommand, 1);
    }
    
    /**
     * Splits the input and returns the requested part (0 for command, 1 for args).
     * This is a private helper to maintain a Single Level of Abstraction.
     */
    private static String getPart(String input, int index) {
        if (input == null || input.isBlank()) {
            return "";
        }
        String[] parts = input.trim().split("\\s+", 2);
        if (index == 0) {
            return parts[0];
        }
        return parts.length > 1 ? parts[1] : "";
    }
}
