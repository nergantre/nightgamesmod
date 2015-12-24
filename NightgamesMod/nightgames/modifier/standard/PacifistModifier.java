package nightgames.modifier.standard;

import nightgames.global.Global;
import nightgames.modifier.BaseModifier;
import nightgames.modifier.skill.BanTacticsModifier;
import nightgames.skills.Tactics;

public class PacifistModifier extends BaseModifier {

	public PacifistModifier() {
		this.skills = new BanTacticsModifier(Tactics.damage);
	}

	@Override
	public int bonus() {
		return 100;
	}

	@Override
	public String name() {
		return "pacifist";
	}

	@Override
	public String intro() {
		return "Lilly gives you a long, appraising look. <i>\"I'm trying to decide what sort of man you are. You strike me as a good guy, probably not the type "
				+ "to hit a girl outside a match. I propose you try being a perfect gentleman by refusing to hit anyone during tonight's match too. So no slapping, "
				+ "kicking, anything intended to purely cause pain. If you agree, I'll add $"
				+ bonus()
				+ " to each point. What do you say?\"</i>";
	}

	@Override
	public String acceptance() {
		return "Lilly flashes you a broad grin and slaps you on the back uncomfortably hard. <i>\"Just so everyone's aware,\"</i> she calls out to your opponents, <i>\""
				+ Global.getPlayer().name()
				+ " has sworn that he won't hurt any girls tonight. So no matter how much anyone taunts him, whips him, or kicks him in the balls, he can't retaliate in "
				+ "any way.\"</i> As you try to ignore a growing sense of dread, she leans close to your ear and whispers, <i>\"Good luck.\"</i>";
	}

}
