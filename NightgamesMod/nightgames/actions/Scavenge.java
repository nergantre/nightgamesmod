package nightgames.actions;

import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.global.Global;
import nightgames.items.Item;

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
