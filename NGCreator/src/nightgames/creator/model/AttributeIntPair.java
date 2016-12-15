package nightgames.creator.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import nightgames.characters.Attribute;

public class AttributeIntPair {

	private ObjectProperty<Attribute> attribute;
	private IntegerProperty value;
	
	public AttributeIntPair(Attribute attr, int value) {
		this.attribute = new SimpleObjectProperty<>(attr);
		this.value = new SimpleIntegerProperty(value);
	}

	public final ObjectProperty<Attribute> attributeProperty() {
		return this.attribute;
	}
	

	public final nightgames.characters.Attribute getAttribute() {
		return this.attributeProperty().get();
	}
	

	public final void setAttribute(final nightgames.characters.Attribute attribute) {
		this.attributeProperty().set(attribute);
	}
	

	public final IntegerProperty valueProperty() {
		return this.value;
	}
	

	public final int getValue() {
		return this.valueProperty().get();
	}
	

	public final void setValue(final int value) {
		this.valueProperty().set(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributeIntPair other = (AttributeIntPair) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AttributeIntPair [attribute=" + attribute + ", value=" + value + "]";
	}
	
}
