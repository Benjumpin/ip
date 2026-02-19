package benbot;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private BenBot benbot;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image benbotImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));
    /**
     * Initializes the scroll pane to automatically scroll to the bottom.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }
    
    /**
     * Sets the BenBot instance for this window and Displays the opening message.
     *
     * @param benbot The BenBot logic instance to be injected.
     */
    public void setBenBot(BenBot benbot) {
        this.benbot = benbot;
        String welcomeMessage = benbot.getWelcome();
        dialogContainer.getChildren().add(
                DialogBox.getBenBotDialog(welcomeMessage, benbotImage)
        );
    }
    
    /**
     * Creates two dialog boxes, one echoing user input and the other containing BenBot's reply.
     * Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = benbot.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBenBotDialog(response, benbotImage)
        );

        userInput.clear();
    }
}