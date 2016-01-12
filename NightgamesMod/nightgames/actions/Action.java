package nightgames.actions;

import java.io.Serializable;

import nightgames.characters.Character;

public abstract class Action implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4981682001213276175L;
	protected String name;

	public Action(String name) {
		this.name = name;
	}

	public abstract boolean usable(Character user);

	public abstract Movement execute(Character user);

	@Override
	public String toString() {
		return name;
	}

	public abstract Movement consider();

	public boolean freeAction() {
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Action other = (Action) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
