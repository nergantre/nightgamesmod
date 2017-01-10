package nightgames.creator.gui;

import java.util.Comparator;
import java.util.List;

import com.google.gson.JsonObject;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import nightgames.creator.verify.VerificationResult;
import nightgames.creator.verify.Verifier;

public class VerificationList extends ListView<VerificationResult> {

	public VerificationList() {
		setCellFactory(l -> new Cell());
		setEditable(false);
		setPrefSize(1000, 600);
	}

	public void update(JsonObject obj) {
		getItems().clear();
		List<VerificationResult> results = Verifier.verify(obj);
		getItems().addAll(results);
		getItems().sort(Comparator.naturalOrder());
	}
	
	private class Cell extends ListCell<VerificationResult> {

		@Override
		public void updateItem(VerificationResult item, boolean empty) {
			super.updateItem(item, empty);
			if (empty || item == null) {
				setText(null);
				setGraphic(null);
			} else {
				setText(item.getLevel().name() + ": " + item.getMessage());
			}
		}

	}
}
