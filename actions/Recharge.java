package actions;

import items.Item;
import global.Global;

import characters.Attribute;
import characters.Character;

public class Recharge extends Action {

	public Recharge() {
		super("Recharge");
	}

	@Override
	public boolean usable(Character user) {
		return user.location().recharge()&&user.get(Attribute.Science)>0&&user.count(Item.Battery)<20;
	}

	@Override
	public Movement execute(Character user) {
		if(user.human()){
			Global.gui().message("You find a power supply and restore your batteries to full.");
		}
		user.chargeBattery();
		return Movement.recharge;
	}

	@Override
	public Movement consider() {
		return Movement.recharge;
	}

}
