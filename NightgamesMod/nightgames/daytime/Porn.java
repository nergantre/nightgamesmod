package nightgames.daytime;

import java.util.ArrayList;

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

	private void showScene(Scene chosen) { switch (chosen) {
	     case basic3: 
	       Global.gui().message("You watch a nude 'audition' by a self-proclaimed aspiring actress. If she can't fake a better orgasm than that, you can see why her career isn't going anywhere.");
	       break;
	     case basic1: 
	       Global.gui().message("You spend about an hour browsing fetish porn websites. Some things do not need to be inserted into the human body.");
	       break;
	     case basic2: 
	       Global.gui().message("You spend about an hour browsing fetish porn websites. You feel a bit more desensitized to normal sex and a little bit dead inside.");
	       break;
	     case fail1: 
	       Global.gui().message("It feels like the internet has run out of sexy. There's nothing new worth fapping to. Maybe there's something decent behind this paywall? No, don't do it. It's a trap.");
	       break;
	     case mara1: 
	       Global.gui().message("You were planning to browse some porn and probably rub one out, but why is Mara in your room? <i>\"Don't sweat the details. I brought you this new porn game so we could play it together. I even saved you some time by making a custom girl who looks like me.\"</i>");
	       
	       break;
	     case angel1: 
	       Global.gui().message("When Angel invited you to watch a movie with her friends, you did not expect it to be porn. In retrospect, you probably should have. Caroline and Sarah have claimed comfortable looking arm chairs, while you, Angel and Mei are packed together on a small sofa. Mei grins at you suggestively. <i>\"If you need to whip it out and jerk off, we'll pretend not to notice.\"</i> That kinda considerate of her, but it's probably not going to be an option. Angel already has her hand down your pants.");
	       
	 
	 
	       break;
	     case reyka1: 
	       Global.gui().message("You stumble onto a webcam of a girl who specializes in fantasy roleplay. Wait... is that Reyka? That's definitely Reyka. Can she absorb libido over the internet?");
	     }
	   }
	   
	   private Scene pickScene(int gain)
	   {
	     ArrayList<Scene> available = new ArrayList();
	     if (gain == 1) {
	       available.add(Scene.fail1);
	     } else {
	       available.add(Scene.basic1);
	       available.add(Scene.basic2);
	       available.add(Scene.basic3);
	       if (Global.getNPC("Mara").getAffection(this.player) >= 5) {
	         available.add(Scene.mara1);
	       }
	       if (Global.getNPC("Angel").getAffection(this.player) >= 10) {
	         available.add(Scene.angel1);
	       }
	       if ((Global.checkFlag(Flag.Reyka)) && (Global.getNPC("Reyka").getAffection(this.player) >= 1)) {
	         available.add(Scene.reyka1);
	       }
	     }
	     return (Scene)available.get(Global.random(available.size()));
	   }
	   
	   private static enum Scene { basic1, 
	     basic2, 
	     basic3, 
	     fail1, 
	     mara1, 
	     angel1, 
	     reyka1;
	   }
}
