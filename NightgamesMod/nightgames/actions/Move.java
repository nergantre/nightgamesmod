package nightgames.actions;

import nightgames.areas.Area;
import nightgames.characters.Character;

public class Move extends Action {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6111866290941387475L;
	private Area destination;

	public Move(Area destination) {
		super("Move(" + destination.name + ")");
		this.destination = destination;
	}

	@Override
	public boolean usable(Character user) {
		return true;
	}

	@Override
	public Movement execute(Character user) {
		user.travel(destination);
		return destination.id();
	}

	public Area getDestination() {
		return destination;
	}

	@Override
	public Movement consider() {
		return destination.id();
	}
}
