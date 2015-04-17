package actions;

import items.Item;

import global.Global;
import characters.Character;
import characters.State;

public class Scavenge extends Action {

	public Scavenge() {
		super("Scavenge Items");
	}

	@Override
	public boolean usable(Character user) {
		return user.location().materials();
	}

	@Override
	public Movement execute(Character user) {
		user.state=State.searching;
		return Movement.scavenge;
	}

	@Override
	public Movement consider() {
		return Movement.scavenge;
	}

}
