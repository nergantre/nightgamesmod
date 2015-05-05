package actions;

import global.Global;
import status.Energized;
import status.Stsflag;
import characters.Attribute;
import characters.Character;

public class Energize extends Action {

	public Energize() {
		super("Absorb Mana");
	}

	@Override
	public boolean usable(Character user) {
		return user.get(Attribute.Arcane)>=1&&user.location().mana()&&!user.is(Stsflag.energized);
	}

	@Override
	public Movement execute(Character user) {
		if(user.human()){
			Global.gui().message("You duck into the creative writing room and find a spellbook sitting out in the open. Aisha must have left it for you. The spellbook builds mana " +
				"continuously and the first lesson you learned was how to siphon off the excess. You absorb as much as you can hold, until you're overflowing with mana.");
		}
		user.getMojo().fill();
		user.add(new Energized(user,20));
		return Movement.mana;
	}

	@Override
	public Movement consider() {
		return Movement.mana;
	}

}
