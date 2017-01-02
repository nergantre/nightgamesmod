package nightgames.creator.req;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import nightgames.requirements.*;

public class CreatorRequirement extends ReadOnlyObjectPropertyBase<RequirementType> {

	public static final CreatorRequirement ROOT = new CreatorRequirement(RequirementType.AND, false);

	protected RequirementType type;
	private ChoiceBox<RequirementType> typeBox;
	private ChoiceBox<String> charBox;
	private HBox box;
	private boolean dirty;
	private boolean built = false;

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
		if (old != nw && built) {
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
		built = true;
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
	
	public static JsonObject allToJson(TreeItem<CreatorRequirement> treeItem) {
		JsonObject obj = new JsonObject();
		if (treeItem.getValue() == ROOT) {
			treeItem.getChildren().forEach(i -> i.getValue().toJson(obj));
		}
		return obj;
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
		case ORGASM:
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
			req.setString(1,entry.getValue().getAsString());
			break;
		case POSITION:
			req.setString(1,entry.getValue().getAsString());
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
		case ORGASM:
			req.setNumber(2, entry.getValue().getAsDouble());
			break;
		case ATTRIBUTE:
			req.setString(2, entry.getValue().getAsJsonObject().get("att").getAsString());
			req.setNumber(3, entry.getValue().getAsJsonObject().get("value").getAsDouble());
			break;
		case BODY:
			req.setString(2, entry.getValue().getAsString());
			break;
		case ITEM:
			req.setString(2, entry.getValue().getAsJsonObject().get("item").getAsString());
			break;
		case MOOD:
			req.setString(1, entry.getValue().getAsString());
			break;
		case STATUS:
			req.setString(1, entry.getValue().getAsString());
			break;
		case TRAIT:
			req.setString(1, entry.getValue().getAsString());
			break;
		default:
			//NOP
		}
		
		return req;
	}
	
	public static CreatorRequirement fromReqs(Requirement req, boolean reversed) {
		CreatorRequirement creq = new CreatorRequirement(RequirementType.typeOf(req), true);
		if (creq.type.isCharacterSpecific()) {
			creq.charBox.getSelectionModel().select(reversed ? 1 : 0);
		}
		switch (creq.type) {
		case ATTRIBUTE:
			creq.setChosenItem(2, ((AttributeRequirement) req).getAtt().toString());
			creq.setNumber(3, ((AttributeRequirement) req).getAmount());
			break;
		case BODY:
			creq.setString(2, ((BodyPartRequirement) req).getType());
			break;
		case ITEM:
			creq.setChosenItem(2, ((ItemRequirement) req).getItemAmount().item.toString());
			break;
		case LEVEL:
			creq.setNumber(2, ((LevelRequirement) req).getLevel());
			break;
		case MOOD:
			creq.setChosenItem(2, ((MoodRequirement) req).getMood().toString());
			break;
		case ORGASM:
			creq.setNumber(2, ((OrgasmRequirement) req).getCount());
			break;
		case POSITION:
			creq.setChosenItem(1, ((PositionRequirement) req).getPosition().toString());
			break;
		case RANDOM:
			creq.setNumber(1, ((RandomRequirement) req).getThreshold());
			break;
		case RESULT:
			creq.setChosenItem(1, ((ResultRequirement) req).getResult().toString());
			break;
		case STATUS:
			creq.setChosenItem(2, ((StatusRequirement) req).getFlag().toString());
			break;
		case TRAIT:
			creq.setChosenItem(2, ((TraitRequirement) req).getTrait().toString());
			break;
		default:
			break;
		}
		return creq;
	}
}
