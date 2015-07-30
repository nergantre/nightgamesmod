package nightgames.actions;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.global.Global;

public class Craft extends Action {

	public Craft() {
		super("Craft Potion");
	}

	@Override
	public boolean usable(Character user) {
		return user.location().potions()&&user.get(Attribute.Cunning)>5;
	}

	@Override
	public Movement execute(Character user) {
		user.state=State.crafting;
		return Movement.craft;
	}

	@Override
	public Movement consider() {
		return Movement.craft;
	}

}
