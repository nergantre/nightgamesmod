package nightgames.actions;

import nightgames.characters.Character;
import nightgames.global.Global;

public class Wait extends Action {

	public Wait() {
		super("Wait");
	}

	@Override
	public boolean usable(Character user) {
		return true;
	}

	@Override
	public Movement execute(Character user) {
		return Movement.wait;
	}

	@Override
	public Movement consider() {
		return Movement.wait;
	}

}
