package nightgames.actions;

import nightgames.characters.Character;
import nightgames.characters.State;

public class Scavenge extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6692555226745083699L;

	public Scavenge() {
		super("Scavenge Items");
	}

	@Override
	public boolean usable(Character user) {
		return user.location().materials();
	}

	@Override
	public Movement execute(Character user) {
		user.state = State.searching;
		return Movement.scavenge;
	}

	@Override
	public Movement consider() {
		return Movement.scavenge;
	}

}
