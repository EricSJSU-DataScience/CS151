package application;

import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class HomeNavigator extends VBox {
	
	private Button homeNavigator;

	public HomeNavigator(Consumer<String> choice) {
		super(30); // spacing parameter 30
		super.setPadding(new Insets(10, 10, 10, 10));

		Rectangle rect = new Rectangle(48, 5);
		Group arrow = arrowShape();

		homeNavigator = createButton(rect, arrow);

		initialize();
		buttonAction(choice);
	}

	private void initialize() {
		this.setMaxSize(40, 40);
		
		this.setAlignment(Pos.TOP_LEFT);
		// Add contents to the VBox
		this.getChildren().addAll(homeNavigator);
	}

	public Group arrowShape() {
		// 4 coordinate of arrow
		double x1 = 50.0, y1 = 200.0;
		double x2 = 80.0, y2 = 200.0;

		double x3 = 64.0, y3 = 182.0;
		double x4 = 64.0, y4 = 218.0;

		// horizontal line
		Line line1 = new Line();
		line1.setStartX(x1);
		line1.setStartY(y1);
		line1.setEndX(x2);
		line1.setEndY(y2);
		line1.setStrokeWidth(2);

		// upper line
		Line line2 = new Line();
		line2.setStartX(x1);
		line2.setStartY(y1);
		line2.setEndX(x3);
		line2.setEndY(y3);
		line2.setStrokeWidth(2);

		// lower line
		Line line3 = new Line();
		line3.setStartX(x1);
		line3.setStartY(y1);
		line3.setEndX(x4);
		line3.setEndY(y4);
		line3.setStrokeWidth(2);

		// group drawing together
		Group arrow = new Group(line1, line2, line3);

		return arrow;
	}

	public Button createButton(Shape shape, Group graphic) {
		Button button = new Button();
		// button.setPrefSize(30, 30);

		button.setShape(shape);
		button.setGraphic(graphic);

		return button;
	}

	private void buttonAction(Consumer<String> choice) {
		homeNavigator.setOnAction(e -> {
			choice.accept("Welcome Page");
			this.setVisible(false);
		});

	}

}
