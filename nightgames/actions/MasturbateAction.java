package nightgames.actions;

import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.global.Global;
import nightgames.global.Modifier;

public class MasturbateAction extends Action {
	public MasturbateAction() {
		super("Masturbate");
	}

	@Override
	public boolean usable(Character user) {
		return user.getArousal().get()>=15&&!(user.human()&&Global.getMatch().condition==Modifier.norecovery);
	}

	@Override
	public Movement execute(Character user) {
		if(user.human()){
			if (user.hasDick())
				Global.gui().message("You desperately need to deal with your erection before you run into an opponent. You find an isolated corner and quickly jerk off.");
			else if (user.hasPussy())
				Global.gui().message("You desperately need to deal with your throbbing pussy before you run into an opponent. You find an isolated corner and quickly finger yourself to a quick orgasm.");
			else
				Global.gui().message("You desperately need to deal with your throbbing body before you run into an opponent. You find an isolated corner and quickly finger your ass to a quick orgasm.");
		}
		user.state=State.masturbating;
		user.delay(1);
		return Movement.masturbate;
	}

	@Override
	public Movement consider() {
		return Movement.masturbate;
	}

}
