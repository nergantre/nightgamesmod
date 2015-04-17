package actions;

import global.Global;

import characters.Attribute;
import characters.Character;
import characters.State;

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
