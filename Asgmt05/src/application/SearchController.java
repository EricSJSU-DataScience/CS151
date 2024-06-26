package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controller class for the NewCategory.fxml file. This class handles the logic
 * and functionality of the UI components defined in the FXML file.
 */
public class SearchController {
	final String filePath_assets = "assets.csv";
	private List<AssetInfo> assets = new ArrayList<AssetInfo>();
	private AssetInfo selectedAsset;
	@FXML
	TextField assetName; // Text field for entering category name
	@FXML
	TableView<AssetInfo> tableView = new TableView<>();

	@FXML
	TableColumn<AssetInfo, String> name;

	@FXML
	TableColumn<AssetInfo, String> category;

	@FXML TableColumn<AssetInfo, String> locationName;

	@FXML
	TableColumn<AssetInfo, String> purchaseDate;

	@FXML
	TableColumn<AssetInfo, String> description;

	@FXML 
	TableColumn<AssetInfo, String> purchaseValue;

	@FXML
	TableColumn<AssetInfo, String> warrantyExpirationDate;

	ObservableList<AssetInfo> list = FXCollections.observableArrayList(assets);

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the FXML file has been loaded.
	 */
	public void initialize() {
		loadAssetsFromCSV();

		name.setCellValueFactory(new PropertyValueFactory<AssetInfo, String>("name"));
		category.setCellValueFactory(new PropertyValueFactory<AssetInfo, String>("category"));
		locationName.setCellValueFactory(new PropertyValueFactory<AssetInfo, String>("location"));
		purchaseDate.setCellValueFactory(new PropertyValueFactory<AssetInfo, String>("purchaseDate"));
		description.setCellValueFactory(new PropertyValueFactory<AssetInfo, String>("description"));
		purchaseValue.setCellValueFactory(new PropertyValueFactory<AssetInfo, String>("purchasedValue"));
		warrantyExpirationDate.setCellValueFactory(new PropertyValueFactory<AssetInfo, String>("warrantyExpirationDate"));
		

		tableView.setItems(list);
	}

	public void loadAssetsFromCSV() {
		String filePath = "assets.csv"; // Path to the CSV file

		// Check if the file exists
		if (Files.exists(Paths.get(filePath))) {
			try {
				// Read all lines from the file and process each line to create AssetInfo
				// objects

				assets = Files.readAllLines(Paths.get(filePath)).stream().map(line -> line.split(","))
						.map(assetDetails -> parseAsset(assetDetails)).collect(Collectors.toList());

			} catch (IOException e) {
				System.err.println("Error reading CSV file: " + e.getMessage());
			}
		} else {
			System.err.println("CSV file does not exist");
		}
	}

	// Helper method to parse a single asset entry from the CSV
	private AssetInfo parseAsset(String[] assetDetails) {
		AssetInfo asset = new AssetInfo();
		asset.setName(assetDetails[0].trim());
		asset.setCategory(assetDetails[1].trim());
		asset.setLocation(assetDetails[2].trim());
		asset.setPurchaseDate(parseDate(assetDetails[3].trim()));
		asset.setDescription(assetDetails[4].trim());
		asset.setPurchasedValue(assetDetails[5].trim());
		asset.setWarrantyExpirationDate(parseDate(assetDetails[6].trim()));
		return asset;
	}

	// Helper method to parse dates, handling "No date provided"
	private LocalDate parseDate(String date) {
		if (date.equals("No date provided")) {
			return null;
		} else {
			try {
				return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
			} catch (DateTimeParseException e) {
				System.err.println("Error parsing date: " + e.getMessage());
				return null;
			}
		}
	}

	@FXML
	public void searchAssetsByName() {
		String searchText = assetName.getText().toLowerCase(); // Get the search text and convert it to lower case

		System.out.println("Starting search...");
		ObservableList<AssetInfo> filteredAssets = FXCollections.observableArrayList();
		for (AssetInfo asset : assets) {
			if (asset.getName().toLowerCase().contains(searchText.toLowerCase())) {
				System.out.println("Match found: " + asset.getName());
				filteredAssets.add(asset);
			}
		}
		System.out.println("Number of matches: " + filteredAssets.size());
		tableView.setItems(filteredAssets);
	}

	@FXML
	public void deleteSelectedAsset() {
		// Get the selected item from the TableView
		selectedAsset = tableView.getSelectionModel().getSelectedItem();

		if (selectedAsset != null) {

			tableView.getItems().remove(selectedAsset);
			assets.remove(selectedAsset);

			overWriteCSV();
			// debug messages
			System.out.println("Deleted: " + selectedAsset.getName());
		} else {
			System.out.println("No item selected to delete.");
		}
	}

	// overwrite deleted result to csv file
	private void overWriteCSV() {
		clearCSV();
		for (AssetInfo itr : assets) {
			saveAssetToCSV(itr);
		}
	}

	public void clearCSV() {
		// file path for the CSV file at instance variable

		try {
			// Write an empty list to the file, truncating it to zero size
			Files.write(Paths.get(filePath_assets), Collections.emptyList(), StandardOpenOption.TRUNCATE_EXISTING);
			// testing message
			System.out.println("CSV file has been cleared.");
		} catch (IOException e) {
			// testing message
			System.err.println("Error clearing the CSV file: " + e.getMessage());
		}
	}

	private void saveAssetToCSV(AssetInfo asset) {
		// file path for the CSV file at instance variable

		try {
			// Check if the file does not exist and create it if necessary
			if (!Files.exists(Paths.get(filePath_assets))) {
				Files.createFile(Paths.get(filePath_assets));
			}
			// Write the assets details to the CSV file
			try (FileWriter fw = new FileWriter(filePath_assets, true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
				out.print(String.join(", ", asset.getName(), asset.getCategory(), asset.getLocation(),
						asset.getPurchaseDate(), // != null ?
													// asset.getPurchaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
													// : "",
						asset.getDescription(), asset.getPurchasedValue(), asset.getWarrantyExpirationDate()// != null ?
																											// asset.getWarrantyExpirationDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
																											// : ""
				));
				out.println();// Move to a new line in the file
			} catch (IOException e) {
				System.err.println("Error writing to CSV file: " + e.getMessage());// Handle possible I/O errors
			}
		} catch (IOException ex) {
			System.err.println("An error occurred initializing the CSV file: " + ex.getMessage());// Handle file
																									// creation errors
		}
	}

	//

	@FXML
	public void editSelectedAsset() {
		// Get the selected item from the TableView
		selectedAsset = tableView.getSelectionModel().getSelectedItem();

		if (selectedAsset != null) {
			// edit selected asset
			
			
			editAssetPage();
			

		} else {
			System.out.println("No item selected to edit.");
		}
		
	}

	// test if List correct loaded
	public void displayAll() {
		for (AssetInfo itr : assets) {
			itr.display();
			System.out.println();
		}
	}

	/**
	 * 
	 * Handles the action event when the "Back To Home" button is clicked. Loads the
	 * homepage FXML file and sets it as the scene for the stage.
	 */
	@FXML
	public void goHome() {
		try {// Load the FXML file for the welcome page
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Welcome.fxml"));
			Parent root = loader.load();

			// Get the stage from the current scene
			Stage stage = (Stage) assetName.getScene().getWindow();

			// Set the new scene for the stage
			Scene scene = new Scene(root);

			// Set the title of the stage
			stage.setTitle("Welcome to TrackWise");

			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception appropriately
		}
	}

	@FXML
	private void editAssetPage() {
			int index = assets.indexOf(selectedAsset);
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditAsset.fxml")); 
			Parent root = loader.load(); // Loads the root element of the FXML file
			
			EditAssetController editAssetController = loader.getController();
			editAssetController.initialize(assets, index);
			
			Stage stage = (Stage) assetName.getScene().getWindow();
			Scene scene = new Scene(root); // Creates a new scene with the loaded root element

			stage.setScene(scene); // Sets the scene to the stage
			stage.setTitle("Edit Asset"); // Sets the title of the stage
			stage.show(); // Shows the stage
		} catch (Exception e) {
			e.printStackTrace();// Prints the stack trace if an exception occurs
		}

	}

}
