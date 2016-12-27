package nightgames.creator.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import nightgames.characters.Attribute;
import nightgames.characters.Trait;

public class StatsBean {

	private SimpleIntegerProperty level;
	private SimpleDoubleProperty stamina;
	private SimpleDoubleProperty arousal;
	private SimpleDoubleProperty mojo;
	private SimpleDoubleProperty willpower;
	private SimpleListProperty<Trait> traits;
	private SimpleMapProperty<Attribute, Integer> attributes;
	
	public StatsBean() {
		level = new SimpleIntegerProperty(1);
		stamina = new SimpleDoubleProperty(80);
		arousal = new SimpleDoubleProperty(80);
		mojo = new SimpleDoubleProperty(80);
		willpower = new SimpleDoubleProperty(40);
		traits = new SimpleListProperty<>();
		attributes = new SimpleMapProperty<>();

		attributes.put(Attribute.Power, 3);
		attributes.put(Attribute.Seduction, 3);
		attributes.put(Attribute.Cunning, 3);
		attributes.put(Attribute.Perception, 5);
		attributes.put(Attribute.Speed, 5);
	}
	
	public final SimpleIntegerProperty levelProperty() {
		return this.level;
	}
	
	public final int getLevel() {
		return this.levelProperty().get();
	}
	
	public final void setLevel(final int level) {
		this.levelProperty().set(level);
	}
	
	public final SimpleDoubleProperty staminaProperty() {
		return this.stamina;
	}
	
	public final double getStamina() {
		return this.staminaProperty().get();
	}
	
	public final void setStamina(final double stamina) {
		this.staminaProperty().set(stamina);
	}
	
	public final SimpleDoubleProperty arousalProperty() {
		return this.arousal;
	}
	
	public final double getArousal() {
		return this.arousalProperty().get();
	}
	
	public final void setArousal(final double arousal) {
		this.arousalProperty().set(arousal);
	}
	
	public final SimpleDoubleProperty mojoProperty() {
		return this.mojo;
	}
	
	public final double getMojo() {
		return this.mojoProperty().get();
	}
	
	public final void setMojo(final double mojo) {
		this.mojoProperty().set(mojo);
	}
	
	public final SimpleDoubleProperty willpowerProperty() {
		return this.willpower;
	}
	
	public final double getWillpower() {
		return this.willpowerProperty().get();
	}
	
	public final void setWillpower(final double willpower) {
		this.willpowerProperty().set(willpower);
	}
	
	public final SimpleListProperty<Trait> traitsProperty() {
		return this.traits;
	}
	
	public final javafx.collections.ObservableList<nightgames.characters.Trait> getTraits() {
		return this.traitsProperty().get();
	}
	
	public final void setTraits(final javafx.collections.ObservableList<nightgames.characters.Trait> traits) {
		this.traitsProperty().set(traits);
	}
	
	public final SimpleMapProperty<Attribute, Integer> attributesProperty() {
		return this.attributes;
	}
	
	public final javafx.collections.ObservableMap<nightgames.characters.Attribute, java.lang.Integer> getAttributes() {
		return this.attributesProperty().get();
	}
	
	public final void setAttributes(
			final javafx.collections.ObservableMap<nightgames.characters.Attribute, java.lang.Integer> attributes) {
		this.attributesProperty().set(attributes);
	}
	
}
