package application;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WelcomePage extends VBox {
	private Text welcomeMessage;
	private Text appName;
	private ImageView logo;

	public WelcomePage() {
		super(30); // spacing parameter 30

		// Welcome message + app name
		welcomeMessage = textMessage("Welcome to", 40);
		appName = textMessage("QuickSort", 48);

		// App Logo
		logo = new ImageView(new Image(getClass().getResourceAsStream("/application/resources/logo.png")));

		initialize();

	}

	private void initialize() {
		this.setPrefSize(560, 300);
		this.setAlignment(Pos.CENTER);
		// Add contents to the VBox
		this.getChildren().addAll(welcomeMessage, appName, logo);
	}

	public Text textMessage(String arg, int size) {
		Text text = new Text(arg);
		text.setFont(Font.font("Helvetica", size));

		return text;
	}

}
