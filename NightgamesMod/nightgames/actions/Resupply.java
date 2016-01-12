package nightgames.actions;

import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.global.Global;
import nightgames.global.Modifier;

public class Resupply extends Action {

	public Resupply() {
		super("Resupply");
	}

	@Override
	public boolean usable(Character user) {
		return user.location().resupply();
	}

	@Override
	public Movement execute(Character user) {
		if(user.human()){
			if(Global.getMatch().condition==Modifier.nudist){
				Global.gui().message("You check in so you're eligible to fight again, but you still don't get any clothes.");
			}
			else{
				Global.gui().message("You pick up a change of clothes and prepare to get back in the fray.");
			}
		}
		user.state=State.resupplying;
		return Movement.resupply;
	}

	@Override
	public Movement consider() {
		return Movement.resupply;
	}

}
