package application;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Location;

public class NewLocation extends VBox implements LayoutHelper{
	private Location location = new Location();
	private final String file = "location.csv";
	
	private ArrayList<HBox> layout;
	private final String title = "Create New Location";
	private final String line1 = "Location's name: ";
	private final String line2 = "Description: ";

	public NewLocation() {
		super(30); // spacing parameter 30
		super.setPadding(new Insets(40, 40, 40, 40));

		layout = new ArrayList<HBox>();

		layout.add(createTitle(title));
		layout.add(createTextFieldLine(line1, true));
		layout.add(createTextAreaLine(line2));
		layout.add(lastLine());

		
		initialize(this, layout);
		
		clearButtonAction(layout, 1, 2);
		buttonAction(layout);
	}
	
	public void getInfo() {
		location.setName(((TextField) layout.get(1).lookup("#text")).getText());
		location.setDesciption(((TextArea) layout.get(2).lookup("#text")).getText());
	}
	
	private void buttonAction(ArrayList<HBox> arg) {
		((Button)arg.get(arg.size() - 1).getChildren().get(0)).setOnAction(e -> {

			String name = ((TextField)layout.get(1).lookup("#text")).getText();
			if (name.isEmpty()) {
				// Show an error message if the name is empty
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Location name can not be empty!");
				alert.showAndWait();
			} else {
				// Save the category name to a .csv file
				this.getInfo();
				saveToCsv();
				//clear TextField
				clearTextField((TextField)arg.get(1).lookup("#text"));
				clearTextArea((TextArea)arg.get(2).lookup("#text"));
			}
			
		});

	}
	
	private void saveToCsv() {
		try {
			// Check if the file exists
			if (Files.exists(Paths.get(file))) {
				// If it exists, append the new category to the file
				try (FileWriter writer = new FileWriter(file, true)) {
					writer.append("\n");
					writer.append(location.saveToCsv());
				}
			} else {
				// If the file doesn't exist, create it and write the header row
				try (FileWriter writer = new FileWriter(file)) {
					writer.append("Location Name,Description");
				}

				// Then append the new category to the file
				saveToCsv();
				// prevent second msg
				return;
			}

			// Show a success message
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Success");
			alert.setContentText("Location created successfully.");
			alert.showAndWait();
		} catch (IOException ex) {
			// Show an error message if there was a problem saving the category
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("There was a problem saving the location.");
			alert.showAndWait();
		}
	}

}
