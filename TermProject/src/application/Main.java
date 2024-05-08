package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	final private String appName = "QuickSort";

	private StackPane rightSection = new StackPane();

	private WelcomePage welcomePage = new WelcomePage();
	private NewCategory newCategory = new NewCategory();
	private NewLocation newLocation = new NewLocation();
	private AssetManagement manageAsset = new AssetManagement();
	private NewAsset newAsset = new NewAsset();
	
	//Testing Page for single element
	//private TestPage test = new TestPage();

	private HomeNavigator homeNavigator = new HomeNavigator(choice -> {
		welcomePage.setVisible("Welcome Page".equals(choice));
		newCategory.setVisible("New Category Page".equals(choice));
		newLocation.setVisible("New Location Page".equals(choice));
		newAsset.setVisible("New Asset".equals(choice));
		manageAsset.setVisible("Asset Management".equals(choice));
		//test page
		//test.setVisible("Reports Page".equals(choice));
	});

	private NavigationMenu navigationMenu = new NavigationMenu(choice -> {
		welcomePage.setVisible("Welcome Page".equals(choice));
		newCategory.setVisible("New Category Page".equals(choice));
		newLocation.setVisible("New Location Page".equals(choice));
		newAsset.setVisible("New Asset Page".equals(choice));
		manageAsset.setVisible("Asset Management".equals(choice));
		homeNavigator.setVisible(!welcomePage.isVisible());
		//test page
		//test.setVisible("Reports Page".equals(choice));
	});

	public void start(Stage primaryStage) throws Exception {
		
		rightSection.getChildren().addAll(welcomePage, newCategory, newLocation, newAsset, manageAsset , homeNavigator);
		initializePage();

		//rightSection.getChildren().add(test);
		//test.setVisible(false);
		
		HBox mainBkgd = new HBox();
		mainBkgd.getChildren().addAll(rightSection, navigationMenu);

		Scene scene = new Scene(mainBkgd, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle(appName);
		primaryStage.show();

	}

	public void initializePage() {
		rightSection.setAlignment(Pos.TOP_RIGHT);
		// initialize with welcome page
		rightSection.getChildren().get(0).setVisible(true);
		// other page not visible
		rightSection.getChildren().get(1).setVisible(false);
		rightSection.getChildren().get(2).setVisible(false);
		rightSection.getChildren().get(3).setVisible(false);
		rightSection.getChildren().get(4).setVisible(false);
		
		// home navigatior
		rightSection.getChildren().get(5).setVisible(false);
		
		//Test page visible
		
	}

	public static void main(String[] args) {

		launch(args);
	}
}
