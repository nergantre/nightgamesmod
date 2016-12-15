package nightgames.creator.req;

import java.util.EnumSet;
import java.util.function.Supplier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import nightgames.characters.Attribute;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.WingsPart;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.stance.Stance;
import nightgames.status.Stsflag;

public enum RequirementArgument {
	NUMBER(TextField::new),
	TEXT(TextField::new),
	REQUIREMENT,
	REQUIREMENTS,
	TRAIT(() -> enumChoice(Trait.class)),
	POSITION(() -> enumChoice(Stance.class)),
	ITEM(() -> enumChoice(Item.class)),
	MOOD(() -> enumChoice(Emotion.class)),
	STATUS(() -> enumChoice(Stsflag.class)),
	RESULT(() -> enumChoice(Result.class)),
	ATTRIBUTE(() -> enumChoice(Attribute.class)),
	BODY(RequirementArgument::bodyPartChoice);

	private final Supplier<? extends Control> builder;
	private final boolean hasControl;

	private RequirementArgument(Supplier<? extends Control> builder) {
		this.builder = builder;
		hasControl = true;
	}

	private RequirementArgument() {
		builder = () -> null;
		hasControl = false;
	}

	public Control buildControl() {
		return builder.get();
	}

	public boolean hasControl() {
		return hasControl;
	}

	private static <E extends Enum<E>> ObservableList<String> enumNames(Class<E> clazz) {
		return FXCollections.observableArrayList(EnumSet.allOf(clazz).stream().map(Enum::name).toArray(String[]::new));
	}

	private static <E extends Enum<E>> ChoiceBox<String> enumChoice(Class<E> clazz) {
		ChoiceBox<String> cb = new ChoiceBox<>();
		cb.setItems(enumNames(clazz));
		return cb;
	}

	private static ChoiceBox<String> bodyPartChoice() {
		ChoiceBox<String> cb = new ChoiceBox<>();
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll(enumNames(BasicCockPart.class).stream().map(s -> "Cock: " + s).toArray(String[]::new));
		items.addAll(enumNames(CockMod.class).stream().map(s -> "Cock: " + s).toArray(String[]::new));
		items.addAll(enumNames(PussyPart.class).stream().map(s -> "Pussy: " + s).toArray(String[]::new));
		items.addAll(enumNames(BreastsPart.class).stream().map(s -> "Breasts: " + s).toArray(String[]::new));
		items.addAll(enumNames(EarPart.class).stream().map(s -> "Ears: " + s).toArray(String[]::new));
		items.addAll(enumNames(TailPart.class).stream().map(s -> "Tail: " + s).toArray(String[]::new));
		items.addAll(enumNames(WingsPart.class).stream().map(s -> "Wings: " + s).toArray(String[]::new));
		cb.setItems(items);
		return cb;
	}
}
