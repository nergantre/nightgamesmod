package nightgames.creator.req;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import nightgames.characters.Attribute;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.stance.Stance;
import nightgames.status.Stsflag;

public class CreatorRequirement extends ReadOnlyObjectPropertyBase<RequirementType> {

	public static final CreatorRequirement ROOT = new CreatorRequirement(RequirementType.AND, false);

	protected RequirementType type;
	private ChoiceBox<RequirementType> typeBox;
	private ChoiceBox<String> charBox;
	private HBox box;
	private boolean dirty;

	public CreatorRequirement() {
		this(RequirementType.DOM, true);
	}

	private CreatorRequirement(RequirementType type, boolean editable) {
		this.type = type;
		typeBox = typeChoiceBox();
		if (!editable) {
			typeBox.getItems().retainAll(type);
		}
		typeBox.getSelectionModel().selectedItemProperty().addListener(this::update);
		charBox = new ChoiceBox<>(FXCollections.observableArrayList("This character", "Other character"));
		charBox.getSelectionModel().select(0);
		dirty = false;
		buildBox();
	}

	private void update(ObservableValue<? extends RequirementType> obs, RequirementType old, RequirementType nw) {
		type = nw;
		if (old != nw) {
			buildBox();
			fireValueChangedEvent();
		}
	}

	private static ChoiceBox<RequirementType> typeChoiceBox() {
		ChoiceBox<RequirementType> cb = new ChoiceBox<>();
		cb.setItems(FXCollections.observableArrayList(EnumSet.allOf(RequirementType.class)));
		return cb;
	}

	private void buildBox() {
		box = new HBox();
		if (type.isCharacterSpecific()) {
			box.getChildren().add(charBox);
		}
		box.getChildren().add(typeBox);
		typeBox.getSelectionModel().select(type);
		type.getArguments().stream().filter(RequirementArgument::hasControl)
		.map(RequirementArgument::buildControl).forEach(box.getChildren()::add);
	}

	public HBox getBox() {
		return box;
	}

	public Map<RequirementArgument, Node> getMapping() {
		Map<RequirementArgument, Node> mapping = new HashMap<>();
		for (int i = 2, argIdx = 0; i < box.getChildren().size(); i++, argIdx++) {
			mapping.put(type.getArguments().get(argIdx), box.getChildren().get(i));
		}
		return mapping;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		fireValueChangedEvent();
	}

	@Override
	public Object getBean() {
		return null;
	}

	@Override
	public String getName() {
		return type.name().toLowerCase();
	}

	@Override
	public RequirementType get() {
		return type;
	}

	private JsonPrimitive getChosenItem(int idx) {
		return new JsonPrimitive(((ChoiceBox<?>) getBox().getChildren().get(idx)).getSelectionModel()
				.getSelectedItem().toString());
	}

	@SuppressWarnings("unchecked")
	private JsonPrimitive getString(int idx) {
		return new JsonPrimitive(
				((ChoiceBox<String>) getBox().getChildren().get(idx)).getSelectionModel().getSelectedItem());
	}

	private JsonPrimitive getNumber(int idx) {
		return new JsonPrimitive(Double.parseDouble(((TextField) getBox().getChildren().get(idx)).getText()));
	}

	@SuppressWarnings("unchecked")
	private <T> void setChosenItem(int idx, T val) {
		ChoiceBox<T> box = (ChoiceBox<T>) getBox().getChildren().get(idx);
		box.getSelectionModel().select(val);
	}
	
	private void setString(int idx, String str) {
		setChosenItem(idx, str);
	}
	
	private void setNumber(int idx, double nr) {
		((TextField) getBox().getChildren().get(idx)).setText("" + nr);
	}
	
	public void toJson(JsonObject toFill) {
		switch (type) {
		case ANAL:
			toFill.add("anal", new JsonPrimitive(""));
			break;
			
		case RESULT:
		case POSITION:
			toFill.add(type.name().toLowerCase(), getChosenItem(1));
			break;
			
		case RANDOM:
			toFill.add("random", getNumber(1));
			break;

		case AND:
		case OR:
			JsonObject arr = new JsonObject();
			toFill.add(type.name().toLowerCase(), arr);
			break;

		case NOT:
			toFill.add("not", new JsonObject());
			break;

		case DOM:
		case SUB:
		case WINNING:
		case PRONE:
		case INSERTED:
			wrapReverse(toFill, new JsonPrimitive(true));
			break;

		case LEVEL:
		case ORGASMS:
			wrapReverse(toFill, getNumber(2));
			break;

		case ATTRIBUTE:
			JsonObject obj = new JsonObject();
			obj.add("att", getString(2));
			obj.add("amount", getNumber(3));
			wrapReverse(toFill, obj);
			break;
			
		case BODY:
			wrapReverse(toFill, getString(2));
			break;
			
		case ITEM:
		case MOOD:
		case STATUS:
		case TRAIT:
			wrapReverse(toFill, getChosenItem(2));
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void wrapReverse(JsonObject toFill, JsonElement element) {
		if (type.isCharacterSpecific() && ((ChoiceBox<String>) getBox().getChildren().get(0)).getSelectionModel()
				.getSelectedItem().startsWith("Other")) {
			JsonObject reverse = new JsonObject();
			reverse.add(type.name().toLowerCase(), element);
			toFill.add("reverse", reverse);
		} else {
			toFill.add(type.name().toLowerCase(), element);
		}
	}

	private static CreatorRequirement unwrapReverse(JsonObject json) {
		CreatorRequirement req = fromJson(json.entrySet().iterator().next().getValue().getAsJsonObject());
		req.charBox.getSelectionModel().select(1);
		return req;
	}
	
	public static CreatorRequirement fromJson(JsonObject json) {
		Map.Entry<String, JsonElement> entry = json.entrySet().iterator().next();
		String key = entry.getKey();
		if (key.equals("reverse")) {
			return unwrapReverse(json);
		}
		RequirementType type = RequirementType.parse(key);
		CreatorRequirement req = new CreatorRequirement(type, true);
		
		switch (type) {
		case RESULT:
			req.setChosenItem(1, Result.valueOf(entry.getValue().getAsString()));
			break;
		case POSITION:
			req.setChosenItem(1, Stance.valueOf(entry.getValue().getAsString()));
			break;
		case RANDOM:
			req.setNumber(1, entry.getValue().getAsDouble());
			break;
		case AND:
		case OR:
		case NOT:
			//TODO
			break;
		case LEVEL:
		case ORGASMS:
			req.setNumber(2, entry.getValue().getAsDouble());
			break;
		case ATTRIBUTE:
			req.setChosenItem(2, Attribute.valueOf(entry.getValue().getAsJsonObject().get("att").getAsString()));
			req.setNumber(3, entry.getValue().getAsJsonObject().get("value").getAsDouble());
			break;
		case BODY:
			req.setString(2, entry.getValue().getAsString());
			break;
		case ITEM:
			req.setChosenItem(2, Item.valueOf(entry.getValue().getAsJsonObject().get("item").getAsString()));
			break;
		case MOOD:
			req.setChosenItem(1, Emotion.valueOf(entry.getValue().getAsString()));
			break;
		case STATUS:
			req.setChosenItem(1, Stsflag.valueOf(entry.getValue().getAsString()));
			break;
		case TRAIT:
			req.setChosenItem(1, Trait.valueOf(entry.getValue().getAsString()));
			break;
		default:
			//NOP
		}
		
		return req;
	}
}
