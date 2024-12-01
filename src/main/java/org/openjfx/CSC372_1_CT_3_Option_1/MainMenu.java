package org.openjfx.CSC372_1_CT_3_Option_1;

// Imports
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.IllegalFormatException;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

// A class that extends Application and builds a main menu
public class MainMenu extends Application {
	// Class properties
	private Label label;
	private VBox vbox;
	private HBox hbox;
	private HBox hueBox;
	private MenuBar menuBar;
	private Menu mainMenu;
	private MenuItem dateTime;
	private MenuItem saveFile;
	private MenuItem changeHue;
	private MenuItem exit;
	
	@Override
	public void start(Stage applicationStage) {
		// Set up the current hue box
		hueBox = new HBox();
		hueBox.setPrefSize(20, 20);
		
		// Set up all other UI elements and set their titles
		label = new Label();
		vbox = new VBox();
		hbox = new HBox();
		menuBar = new MenuBar();
		mainMenu = new Menu("Main Menu");
		dateTime = new MenuItem("See Current Date and Time");
		saveFile = new MenuItem("Save To File"); 
		changeHue = new MenuItem("Change Hue", hueBox);
		exit = new MenuItem("Exit");
		
		// Set up the action handlers of each menu option
		dateTime.setOnAction(e -> {
			setMostRecentDateAndTime();
		});
		
		saveFile.setOnAction(e -> {
			saveDateTimeToFile();
		});
		
		changeHue.setOnAction(e -> {
			setBackgroundGreenHue(false);
		});

		exit.setOnAction(e -> {
			System.exit(0);
		});
		
		// Set font color to white
		label.setTextFill(Color.WHITE);
		
		// Add the menu items
		mainMenu.getItems().add(dateTime);
		mainMenu.getItems().add(saveFile);
		mainMenu.getItems().add(changeHue);
		mainMenu.getItems().add(exit);
		menuBar.getMenus().add(mainMenu);
		
		// Add the label and menu bar
		hbox.getChildren().add(label);
		vbox.getChildren().addAll(menuBar, hbox);
		
		// Set alignment and padding for label container
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(15, 15, 15, 15));
		
		// Set the initial background hue
		setBackgroundGreenHue(true);
		
		// Create, set and display the scene
		Scene scene = new Scene(vbox, 300, 200);
		applicationStage.setScene(scene);
		applicationStage.show();
	}
	
	// A method to generate a random green hue, set the background color to it, and update the menu current hue
	private void setBackgroundGreenHue(boolean isDefault) {
		int greenValue = 50;
		
		if (!isDefault) {
			try {
				// Create a random hue
				Random random = new Random();
				greenValue = random.nextInt(100);
			} catch(IllegalArgumentException e) {
				// Inform the user of an issue and output a debug statement.
				showAlertMessage("Something went wrong. Please try again.", AlertType.ERROR);
				System.out.println("Failed to generate new hue value.");
			}
		}
		
		// Generate the new green color, set the background, and the current hue in the menu
		Color newColor = new Color(0.0, greenValue/100.0, 0.0, 1.0);
		BackgroundFill fill = new BackgroundFill(newColor, CornerRadii.EMPTY, Insets.EMPTY);
		Background background = new Background(fill);
		vbox.setBackground(background);
		hueBox.setBackground(background);
	}
	
	// A method that sets the current date and time in the label
	private void setMostRecentDateAndTime() throws IllegalFormatException {
		LocalDateTime currentDate = LocalDateTime.now();
		
		// Format current date and time
		try {
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
			DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");
			String formattedDate = currentDate.format(dateFormat);
			String formattedTime = currentDate.format(timeFormat);
			
			// Set the label's text
			label.setText(String.format("Current date: %s\nCurrent Time: %s", formattedDate, formattedTime));
		} catch (IllegalArgumentException | DateTimeException e) {
			// Inform the user of an issue and output a debug statement.
			showAlertMessage("Something went wrong. Please try again.", AlertType.ERROR);
			System.out.println("Failed to get formatted date and time.");
		}
	}
	
	// A method to save the currently displayed date and time to a text file titled "log.txt"
	private void saveDateTimeToFile() {
		FileOutputStream outputStream = null;
		PrintWriter fileWriter = null;
		
		// Try to open and save the current label's contents to a text file
		try {
			outputStream = new FileOutputStream("log.txt");
			fileWriter = new PrintWriter(outputStream);
			String currentDateTimeString = label.getText();
			
			// If the user has not selected the date and time option, inform them and do not save to file.
			if (currentDateTimeString.equals("")) {
				throw new EmptyTextFieldException();
			}
			
			fileWriter.println(currentDateTimeString);
			
			// Inform the user of the success
			showAlertMessage("Successfully saved file.", AlertType.INFORMATION);
		} catch (EmptyTextFieldException e) {
			// The label was never set and so inform the user and output a debug message.
			showAlertMessage("No date and time displayed. File saved with empty text. Please user the menu to select See Current Date and Time.", AlertType.INFORMATION);
			System.out.println("Label is empty.");
		} catch (FileNotFoundException | SecurityException e) {
			// An error occurred so output a debug message and inform the user.
			showAlertMessage("Failed to save the file. Please try again.", AlertType.ERROR);
			System.out.println("Failed to save the file.");
		} finally {
			// Close the file
			fileWriter.close();
		}
	}
	
	// A method to show an alert to the user with the sent in message and type.
	private void showAlertMessage(String message, AlertType type) {
		try {
			Alert alert = new Alert(type, message, ButtonType.OK);
			alert.showAndWait();
		} catch (IllegalStateException e) {
			// An error occurred so output a debug message.
			System.out.println("Failed to show alert to the user.");
		}
	}

	public static void main(String [] args) {
		launch(args); // Launch application
	}
}