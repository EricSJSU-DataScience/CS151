package application;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;
import model.Asset;
import model.Category;
import model.Location;

public class NewAsset extends VBox implements LayoutHelper{
	/**
	 * This page is VBox object. All page like this will put in StackPane.
	 * Navigation page on the left could change visibility of page on the right.
	 */
	private Asset asset = new Asset();
	
	private HashMap<String, Category> category = new HashMap<String, Category>();
	private HashMap<String, Location> location = new HashMap<String, Location>();
	private final String file = "asset.csv";
	
	private ArrayList<HBox> layout;
	private final String title = "Create New Asset";
	private final String line1 = "Asset's Name: ";
	private final String line2 = "Category: ";
	private final String line3 = "Location: ";
	private final String line4 = "Purchase date: ";
	private final String line5 = "Description: ";
	private final String line6 = "Purchased Value: ";
	private final String line7 = "Warranty\nExpiration Date";
	

	public NewAsset() {
		super(30); // spacing parameter 30
		super.setPadding(new Insets(40, 40, 40, 40));
		
		layout = new ArrayList<HBox>();
		setExample();
		
        layout.add(createTitle(title));
        layout.add(createTextFieldLine(line1, true));
        layout.add(createDropdownList(line2, category));
        layout.add(createDropdownList(line3, location));
        layout.add(createDatePicker(line4));
        layout.add(createTextAreaLine(line5));
        layout.add(createTextFieldLine(line6));
        layout.add(createDatePicker(line7));
        layout.add(lastLine());
        
        //Purchased Value
        ((TextField)layout.get(6).lookup("#text")).setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        
		initialize(this, layout);
		clearButtonAction(layout, 1, 5, 6);
		buttonAction(layout);
	}
	
	// getInfo() is in working progress.
	public void getInfo() {
		asset.setName(((TextField) layout.get(1).lookup("#text")).getText());
		
		asset.setCategory(category.get(((ComboBox<String>) layout.get(2).lookup("#choice")).getValue()));
		
		asset.setLocation(location.get(((ComboBox<String>) layout.get(3).lookup("#choice")).getValue()));
		
		//asset.setPurchaseDate();
		//purchase date as LocalDate
		asset.setPurchaseDate(((DatePicker)getInput(layout.get(4), "date")).getValue());
		
		//description
		asset.setDesciption(((TextArea) layout.get(5).lookup("#text")).getText());
		//purchase value as Double
		asset.setPurchaseValue(Double.parseDouble(((TextField) layout.get(6).lookup("#text")).getText()));
		//Warranty expiration date
		asset.setWarrantyExpDate(((DatePicker)getInput(layout.get(7), "date")).getValue());
		
	}
	@SuppressWarnings("unchecked")
	public <E> E getInput(HBox line, String keyword) {
		return (E)line.lookup("#" + keyword);
	}
	private void buttonAction(ArrayList<HBox> arg) {
		((Button)arg.get(arg.size() - 1).getChildren().get(0)).setOnAction(e -> {

			String name = ((TextField)layout.get(1).lookup("#text")).getText();
			if (name.isEmpty()) {
				// Show an error message if the name is empty
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Asset name can not be empty!");
				alert.showAndWait();
			} else {
				// Save the category name to a .csv file
				this.getInfo();
				saveAssetToCsv();
				System.out.println(asset.saveToCsv());
				//
			}
		});

	}
	
	private void setExample() {
		//later will change to read category and location from csv
		Category ex1 = new Category("example category");
		category.put(ex1.getName(), ex1);
		
		Location ex2 = new Location("example location", "example description");
		location.put(ex2.getName(), ex2);
		
		//ex1.display();
		//ex2.display();
	}
	
	private void saveAssetToCsv() {
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
					writer.append("Asset Name,Category,Location,Purchase Date,Description,Purchase Value,Warranty Exp Date");
				}

				// Then append the new category to the file
				saveAssetToCsv();
				// prevent second msg
				return;
			}

			// Show a success message
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Success");
			alert.setContentText("Asset created successfully.");
			alert.showAndWait();
		} catch (IOException ex) {
			// Show an error message if there was a problem saving the category
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("There was a problem saving the asset.");
			alert.showAndWait();
		}
	}
	

}
