package nightgames.creator.gui;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nightgames.creator.model.TraitBean;

public class TraitCell extends ListCell<TraitBean> {

	private HBox box;
	private Label label;
	private TextField field;
	private ChangeListener<String> listener;
	
	public TraitCell() {
		box = new HBox(20);
		label = new Label();
		label.setFont(Font.font("segoeUI", FontWeight.BOLD, 14));
		field = new TextField("5");
		field.setMaxWidth(40);
		field.setMinWidth(40);
		CreatorGui.setFieldNumericOnly(field);
	}
	
	@Override
	public void updateItem(TraitBean trait, boolean empty) {
		super.updateItem(trait, empty);
		box.getChildren().clear();
		if (empty || trait == null) {
			setText(null);
			setGraphic(null);
		} else {
			box.getChildren().addAll(label, field);
			label.setText(trait.getTrait().toString());
			field.setText(trait.getLevel() + "");
			if (listener != null) {
				field.textProperty().removeListener(listener);
			}
			listener = (obs, old, nw) -> {
				try {
					trait.setLevel(Integer.parseInt(nw));
				} catch (NumberFormatException e) {
					// NOP
				}
			};
		}
		setGraphic(box);
	}
	
}
