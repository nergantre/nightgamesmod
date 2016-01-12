package nightgames.daytime;

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
		if(page==0){
			switch(Global.random(3)){
			case 2:
				Global.gui().message("You're about halfway through your jog when a sudden downpour leaves you completely soaked. You squelch your way back to the dorm, looking like a drowned rat.");
				break;
			case 1:
				Global.gui().message("You head to the campus gym and spend some time in a variety of exercises.");
				break;
			default:
				Global.gui().message("You decide to take a brief jog around campus to improve your strength and stamina.");
			}
			Global.gui().next(this);
			int gain = Global.random(3)+3;
			if(player.has(Trait.fitnessNut)){
				gain*=2;
			}
			player.getStamina().gain(gain);
			Global.gui().message("<b>Your maximum stamina has increased by "+gain+".</b>");
		}
		else{
			done(true);
		}
	}

	@Override
	public void shop(Character npc, int budget) {
		npc.getStamina().gain(Global.random(3)+1);
	}

}
