package nightgames.daytime;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.global.Flag;
import nightgames.global.Global;

public class Porn extends nightgames.daytime.Activity {
	public Porn(Character player) {
		super("Browse Porn Sites", player);
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
				Global.gui().message("You watch a nude 'audition' by a self-proclaimed aspiring actress. If she can't fake a better orgasm than that, you can see why her career isn't going anywhere.");
				break;
			case 1:
				Global.gui().message("You spend about an hour browsing fetish porn websites. Some things do not need to be inserted into the human body.");
				break;
			default:
				Global.gui().message("You spend about an hour browsing fetish porn websites. You feel a bit more desensitized to normal sex and a little bit dead inside.");
			}
			
			Global.gui().next(this);
			int gain = Global.random(5)+4;
			if(player.has(Trait.expertGoogler)){
				gain*=2;
			}
			player.getArousal().gain(gain);
			Global.gui().message("<b>Your maximum arousal has increased by "+gain+".</b>");
		}
		else{
			done(true);
		}
	}

	@Override
	public void shop(Character npc, int budget) {
		npc.getArousal().gain(8);
	}

}
