package nightgames.modifier.standard;

import nightgames.global.Global;
import nightgames.modifier.BaseModifier;
import nightgames.modifier.clothing.NudeModifier;

public class NudistModifier extends BaseModifier {

	public NudistModifier() {
		this.clothing = new NudeModifier();
	}

	@Override
	public int bonus() {
		return 125;
	}

	@Override
	public String name() {
		return "nudist";
	}

	@Override
	public String intro() {
		return "<i>\"Funny thing " + Global.getPlayer().name()
				+ ", me and the other girls were just talking about you.\"</i> There's no way that's good. <i>\"I asked them all what their least "
				+ "favorite thing about you is.\"</i> Nope. Definitely not good. <i>\"After some discussion they all agreed that your worst quality is your insistence on "
				+ "so frequently wearing clothing. So, I think you should spend the match naked and see how well you do. I'm willing to offer a $"
				+ bonus() + " bonus to "
				+ "motivate you. What do you say?\"</i>";
	}

	@Override
	public String acceptance() {
		return "You agree to Lilly's rule and start to strip off your clothes. You try to appear nonchalant about it, but you can't help reddening a bit when your "
				+ "opponents start cheering you on. Lilly stiffles a laugh as you hand over your clothes. <i>\"You see? You're more popular already.\"</i>";
	}
	
	@Override
	public boolean isApplicable() {
		return !Global.getPlayer().outfitPlan.isEmpty();
	}

}
