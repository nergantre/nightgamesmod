package nightgames.creator.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import nightgames.characters.Trait;

public class TraitBean {

	private SimpleObjectProperty<Trait> trait;
	private SimpleIntegerProperty level;

	public TraitBean() {
		trait = new SimpleObjectProperty<>(Trait.none);
		level = new SimpleIntegerProperty(-1);
	}

	public SimpleObjectProperty<Trait> traitProperty() {
		return this.trait;
	}

	public nightgames.characters.Trait getTrait() {
		return this.traitProperty().get();
	}

	public void setTrait(final nightgames.characters.Trait trait) {
		this.traitProperty().set(trait);
	}

	public SimpleIntegerProperty levelProperty() {
		return this.level;
	}

	public int getLevel() {
		return this.levelProperty().get();
	}

	public void setLevel(final int level) {
		this.levelProperty().set(level);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((trait == null) ? 0 : trait.hashCode());
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
		TraitBean other = (TraitBean) obj;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (trait == null) {
			if (other.trait != null)
				return false;
		} else if (!trait.equals(other.trait))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TraitBean [trait=" + trait + ", level=" + level + "]";
	}

}
