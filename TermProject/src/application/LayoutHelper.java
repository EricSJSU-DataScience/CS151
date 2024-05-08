package application;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public interface LayoutHelper {
	/**
	 * Program interface are VBox objects. Each line on page are HBox object.
	 * 
	 * @param page: VBox object
	 * @param line: HBox object
	 */
	public default void initialize(VBox page, ArrayList<HBox> line) {
		page.setPrefSize(560, 300);
		page.setAlignment(Pos.TOP_CENTER);
		// Add contents to the VBox
		for (HBox itr : line) {
			page.getChildren().add(itr);
		}
	}
	/**
	 * Program interface
	 * @param page
	 * @param line
	 */
	public default void initialize(VBox page, HBox...line) {
		page.setPrefSize(560, 300);
		page.setAlignment(Pos.TOP_CENTER);
		// Add contents to the VBox
		for (HBox itr : line) {
			page.getChildren().add(itr);
		}
	}
	/**
	 * Create HBox as line on the page. This method is for Title on the page.
	 * @param arg
	 * @return
	 */
	public default HBox createTitle(String arg) {
		HBox title = new HBox();
		Text titleText = new Text(arg);
		titleText.setFont(Font.font("Helvetica", 30));

		title.getChildren().add(titleText);
		title.setAlignment(Pos.BASELINE_CENTER);

		return title;
	}
	/**
	 * custom label with same format
	 * @param arg
	 * @return
	 */
	public default Label createLabel(String arg) {
		Label label = new Label(arg);
		label.setFont(Font.font("Helvetica", 20));
		label.setPrefWidth(190);
		
		return label;
	}
	/**
	 * Create line with TextField with redAsterisk.
	 * @param arg
	 * @param must
	 * @return
	 */
	public default HBox createTextFieldLine(String arg, boolean must) {
		// redAsterisk
		HBox res = new HBox();
		if (must) {
			res.getChildren().add(redAsterisk());
		}else {
			res.getChildren().add(emptySpace());
		}
		// label will use createLabel() function
		
		// text field
		TextField textField = new TextField();
		textField.setId("text");//set id for textField lookup
		textField.setPrefWidth(180);

		res.getChildren().addAll(createLabel(arg), textField);
		res.setAlignment(Pos.BASELINE_CENTER);
		return res;
	}
	/**
	 * Create line with TextField without redAsterisk. 
	 * @param arg
	 * @return
	 */
	public default HBox createTextFieldLine(String arg) {
		return createTextFieldLine(arg, false);
	}
	public default HBox createTextAreaLine(String arg) {
		// redAsterisk
		HBox res = new HBox();
		
		res.getChildren().add(emptySpace());
		
		// label will use createLabel() function
		
		// text field
		TextArea textField = new TextArea();
		textField.setId("text");//set id for textField lookup
		
		textField.setPrefSize(180, 50);

		res.getChildren().addAll(createLabel(arg), textField);
		res.setAlignment(Pos.BASELINE_CENTER);
		return res;
	}
	/**
	 * Create line with drop down list
	 * @param <E>
	 * @param <T>
	 * @param arg
	 * @param list
	 * @return
	 */
	public default <E> HBox createDropdownList(String arg, HashMap<String, E> list) {
		// redAsterisk
		HBox res = new HBox();

		// label
		
		// temporary comboBox
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.setPromptText("Select a " + arg);
		comboBox.setId("choice");
		comboBox.setPrefWidth(180);
		
		for(String itr: list.keySet()) {
			comboBox.getItems().add(itr);
		}
		
		res.getChildren().addAll(redAsterisk(), createLabel(arg), comboBox);
		res.setAlignment(Pos.BASELINE_CENTER);
		return res;
	}
	/**
	 * Create line with date picker.
	 * @param arg
	 * @return
	 */
	public default HBox createDatePicker(String arg) {
		HBox res = new HBox();
		res.getChildren().add(emptySpace());
		// label

		// date picker
		DatePicker date = new DatePicker();
		date.setId("date");
		date.setPrefWidth(180);
		
		res.getChildren().addAll(createLabel(arg), date);
		res.setAlignment(Pos.BASELINE_CENTER);

		return res;
	}
	/**
	 * Create Button (Confirm & Clear)
	 * @param arg: text on Button
	 * @return
	 */
	public default Button createButton(String arg) {
		Button button = new Button(arg);

		button.setStyle("-fx-text-fill: Black; " + "-fx-font-size: 16px; ");
		button.setPrefSize(120, 30);

		return button;
	}
	/**
	 * Create last line on the page which is Confirm & Clear Button.
	 * @return
	 */
	public default HBox lastLine() {
		HBox res = new HBox(50, createButton("Confirm"), createButton("Clear"));
		res.setAlignment(Pos.BASELINE_CENTER);
		return res;
	}
	/**
	 * This method help clear Button clear TextField
	 * @param arg: multiple TextField
	 */
	public default void clearTextField(TextField...arg) {
		for (TextField itr : arg) {
			itr.clear();
		}
	}
	
	public default void clearTextArea(TextArea...arg) {
		for (TextArea itr : arg) {
			itr.clear();
		}
	}
	/**
	 * Set action for clear Button
	 * @param arg
	 * @param itr: represent which line is TextField
	 */
	public default void clearButtonAction(ArrayList<HBox> arg, int...itr) {
		((Button)arg.get(arg.size() - 1).getChildren().get(1)).setOnAction(e -> {
			for(int i : itr) {
				clearTextField((TextField)arg.get(i).lookup("#text"));
			}
		});
	}
	/**
	 * This is Label to represent that line can not be empty.
	 * @return
	 */
	public default Label redAsterisk() {
		Label redAsterisk = new Label("*");
		redAsterisk.setFont(Font.font("Arial", 20));
		redAsterisk.setTextFill(Color.RED);
		redAsterisk.setPrefWidth(10);
		
		return redAsterisk;
	}
	public default Label emptySpace() {
		Label empty = new Label();
		empty.setPrefWidth(10);
		
		return empty;
	}
}
