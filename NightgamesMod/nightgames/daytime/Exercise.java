package nightgames.daytime;

import java.util.ArrayList;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.global.Flag;
import nightgames.global.Global;

public class Exercise extends Activity {

	public Exercise(Character player) {
		super("Exercise", player);
	}

	@Override
	public boolean known() {
		return Global.checkFlag(Flag.metBroker);
	}

	@Override
	public void visit(String choice) {
		Global.gui().clearText();
		if (page == 0) {
			Global.gui().next(this);
			int gain = Global.random(3) + 3;
			if (player.has(Trait.fitnessNut)) {
				gain *= 2;
			}
			showScene(pickScene(gain));
			player.getStamina().gain(gain);
			Global.gui().message("<b>Your maximum stamina has increased by "
					+ gain + ".</b>");
		} else {
			done(true);
		}
	}

	@Override
	public void shop(Character npc, int budget) {
		npc.getStamina().gain(Global.random(3) + 1);
	}

	private void showScene(Scene chosen) {
		switch (chosen) {
			case basic1:
				Global.gui().message(
						"You're about halfway through your jog when a sudden downpour leaves you completely soaked. You squelch your way back to the dorm, looking like a drowned rat.");
				break;
			case basic2:
				Global.gui().message(
						"You head to the campus gym and spend some time in a variety of exercises.");
				break;
			case basic3:
				Global.gui().message(
						"You decide to take a brief jog around campus to improve your strength and stamina.");
				break;
			case fail1:
				Global.gui().message(
						"Maybe you didn't stretch well enough before you started, but a few minutes into your run you feel like you've pulled a muscle in your leg. Better take a break rather than injure yourself before the match.");
				break;
			case cassie1:
				Global.gui().message(
						"You head over to the campus gym and coincidentally run into Cassie there. <i>\"Hi. I'm not really much of a fitness enthusiast, but I need to get into better shape if I'm going to stay competitive.\"</i><br>The two of you spend some time doing light exercise and chatting.");

				Global.getNPC("Cassie").gainAffection(player, 1);
				player.gainAffection(Global.getNPC("Cassie"), 1);
				break;
			case jewel1:
				Global.gui().message(
						"You're going for a run around the campus and run into Jewel doing the same. She makes an immediate beeline towards you. <i>\"You're not getting out of running today, no matter how tempting the alternative is. We're going to get some real exercise.\"</i> She pushes you a lot harder than you had planned and you're exhausted by the end of it, but you did manage to keep up with her.");

				Global.getNPC("Jewel").gainAffection(player, 1);
				player.gainAffection(Global.getNPC("Jewel"), 1);
		}
	}

	private Scene pickScene(int gain) {
		ArrayList<Scene> available = new ArrayList<Scene>();
		if (gain == 1) {
			available.add(Scene.fail1);
		} else {
			available.add(Scene.basic1);
			available.add(Scene.basic2);
			available.add(Scene.basic3);
			if (Global.getNPC("Cassie").getAffection(player) >= 5) {
				available.add(Scene.cassie1);
			}
			if (Global.getNPC("Jewel").getAffection(player) >= 5
					&& player.getStamina().max() >= 35) {
				available.add(Scene.jewel1);
			}
		}
		return available.get(Global.random(available.size()));
	}

	private static enum Scene {
		basic1, basic2, basic3, cassie1, jewel1, fail1;
	}
}
