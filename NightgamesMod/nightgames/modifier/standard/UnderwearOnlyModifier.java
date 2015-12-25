package nightgames.modifier.standard;

import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.modifier.BaseModifier;

public class UnderwearOnlyModifier extends BaseModifier {

	public UnderwearOnlyModifier() {
		clothing = new nightgames.modifier.clothing.UnderwearOnlyModifier();
	}

	@Override
	public int bonus() {
		return 100;
	}

	@Override
	public String name() {
		return "underwearonly";
	}

	@Override
	public String intro() {
		return "<i>\"So, " + Global.getPlayer().name()
				+ ", what would you say to another match in your underwear? For some reason, that just amuses the hell out of me. "
				+ "The bonus is still $" + bonus() + " per point.\"</i> ";
	}

	@Override
	public String acceptance() {
		return "Lilly smiles with her hands on her hips. <i>\"Glad to hear it. We'll hang on to the rest of your clothes until the match is over. Underwear-only starts "
				+ "now.\"</i> She wants you to undress here before the match even starts? You hesitate as you realize your opponents are all watching you curiously. Some "
				+ "of Lilly's assistants are still around too. She laughs when she notices your reluctance. <i>\"Are you seriously getting embarrassed about this? As if "
				+ "anyone in this room hasn't seen you naked on a regular basis.\"</i> She does have a point. You quickly strip off your shirt and pants and prepare for "
				+ "the match.";
	}

	@Override
	public boolean isApplicable() {
		return Global.getPlayer().outfitPlan.stream()
				.anyMatch(article -> article.getLayer() == 0 && (article
						.getSlots().contains(ClothingSlot.top)
						|| article.getSlots().contains(ClothingSlot.bottom)));
	}

}
