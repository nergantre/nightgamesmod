package actions;

import global.Global;
import characters.Character;
import characters.State;

public class Bathe extends Action {

	public Bathe() {
		super("Clean Up");
	}

	@Override
	public boolean usable(Character user) {
		return user.location().bath();
	}

	@Override
	public Movement execute(Character user) {
		if(user.human()){
			if(user.location().name=="Showers"){
				Global.gui().message("It's a bit dangerous, but a shower sounds especially inviting right now.");
			}
			else if(user.location().name=="Pool"){
				Global.gui().message("There's a jacuzzi in the pool area and you decide to risk a quick soak.");
			}
		}
		user.state=State.shower;
		user.delay(1);
		return Movement.bathe;
	}

	@Override
	public Movement consider() {
		return Movement.bathe;
	}

}
