package benbot;

import javafx.application.Application;

/**
 * A helper class to bypass classpath issues when launching the JavaFX application.
 */
public class Launcher {
    /**
     * Entry point for the application.
     *
     * @param arguments Command line arguments.
     */
    public static void main(String[] arguments) {
        Application.launch(Main.class, arguments);
    }
}