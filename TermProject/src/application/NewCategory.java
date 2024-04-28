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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Category;

public class NewCategory extends VBox implements LayoutHelper {
	private Category category = new Category();
	private final String file = "category.csv";

	private ArrayList<HBox> layout = new ArrayList<HBox>();
	private final String title = "Create New TestPage";
	private final String line1 = "Category's name: ";


	public NewCategory() {
		super(30); // spacing parameter 30
		super.setPadding(new Insets(40, 40, 40, 40));
 
		layout.add(createTitle(title));
		layout.add(createTextLine(line1, true));
		layout.add(lastLine());

		initialize(this, layout);
		
		clearButtonAction(layout, 1);
		buttonAction(layout);
		
	}
	
	//
	private void buttonAction(ArrayList<HBox> arg) {
		((Button)arg.get(arg.size() - 1).getChildren().get(0)).setOnAction(e -> {

			String name = ((TextField)layout.get(1).lookup("#text")).getText();
			if (name.isEmpty()) {
				// Show an error message if the name is empty
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Category name can not be empty!");
				alert.showAndWait();
			} else {
				// Save the category name to a .csv file
				category.setName(name);
				saveCategoryToCsv();
				//clear TextField
				clearTextField((TextField)arg.get(1).lookup("#text"));
			}
			
		});

	}
	
	private void saveCategoryToCsv() {
		try {
			// Check if the file exists
			if (Files.exists(Paths.get(file))) {
				// If it exists, append the new category to the file
				try (FileWriter writer = new FileWriter(file, true)) {
					writer.append("\n");
					writer.append(category.saveToCsv());
				}
			} else {
				// If the file doesn't exist, create it and write the header row
				try (FileWriter writer = new FileWriter(file)) {
					writer.append("Category Name");
				}

				// Then append the new category to the file
				saveCategoryToCsv();
				return;
			}

			// Show a success message
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Success");
			alert.setContentText("Category created successfully.");
			alert.showAndWait();
		} catch (IOException ex) {
			// Show an error message if there was a problem saving the category
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("There was a problem saving the category.");
			alert.showAndWait();
		}
	}

}