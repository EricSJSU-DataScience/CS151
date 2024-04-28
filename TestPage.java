package application;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import model.Asset;
import model.Category;
import model.Location;

public class TestPage extends VBox implements LayoutHelper {
	/**
	 * This page is VBox object. All page like this will put in StackPane.
	 * Navigation page on the left could change visibility of page on the right.
	 */
	private Asset asset = new Asset();
	
	private HashMap<String, Category> category = new HashMap<String, Category>();;
	private HashMap<String, Location> location = new HashMap<String, Location>();
	private final String file = "asset.csv";
	
	private ArrayList<HBox> layout;
	private final String title = "Test Page";
	private final String line1 = "Asset's Name: ";
	private final String line2 = "Category: ";
	private final String line3 = "Location: ";
	private final String line4 = "Purchase date: ";
	private final String line5 = "Description: ";
	private final String line6 = "Purchased Value: ";
	private final String line7 = "Warranty\nExpiration Date";
	
	private DatePicker date;
	private String dateFormat = "yyyy-MM-dd";

	public TestPage() {
		super(30); // spacing parameter 30
		super.setPadding(new Insets(40, 40, 40, 40));
		
		layout = new ArrayList<HBox>();
		setExample();
		
		layout.add(createTitle(title));
        layout.add(createTextFieldLine(line1, true));
        layout.add(createDropdownList(line2, category));
        layout.add(createDropdownList(line3, location));
		
        //Test how to use DatePicker
		HBox target = new HBox();
		target.getChildren().add(emptySpace());

		// date picker
		date = new DatePicker();
		date.setId("date");
		date.setPrefWidth(180);
		
		target.getChildren().addAll(createLabel(line4), date);
		target.setAlignment(Pos.BASELINE_CENTER);

		
		date.setOnAction(e -> {
            
            System.out.println("Selected Date: " + date.getValue());
            System.out.println(((DatePicker)layout.get(4).lookup("#date")).getValue());
            System.out.println(((DatePicker)layout.get(4).lookup("#date")).getValue().getClass());

        });
		
		
		layout.add(target);
		
		layout.add(lastLine());

		initialize(this, layout);
		buttonAction(layout);
		clearButtonAction(layout, 1);
		
		
		
		
	}

	//
	public void getInfo() {
		asset.setName(((TextField) layout.get(1).lookup("#text")).getText());
		//asset.setDesciption(((TextField) layout.get(5).lookup("#text")).getText());
		asset.setCategory(category.get(((ComboBox<String>) layout.get(2).lookup("#choice")).getValue()));
		asset.setLocation(location.get(((ComboBox<String>) layout.get(3).lookup("#choice")).getValue()));
		asset.getCategory().display();
		asset.getLocation().display();
		//asset.setPurchaseDate();
		//test purchase date: get error message when try to access date value
		System.out.println(date.getEditor());
	}
	
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
				this.getInfo();
				//saveCategoryToCsv();
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
					writer.append(asset.saveToCsv());
				}
			} else {
				// If the file doesn't exist, create it and write the header row
				try (FileWriter writer = new FileWriter(file)) {
					writer.append("Category Name");
				}

				// Then append the new category to the file
				saveCategoryToCsv();
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
	//temporary use
	private void setExample() {
		//later will change to read category and location from csv
		Category ex1 = new Category("example category");
		category.put(ex1.getName(), ex1);
		
		Location ex2 = new Location("example location", "example description");
		location.put(ex2.getName(), ex2);
		
		//ex1.display();
		//ex2.display();
	}

}