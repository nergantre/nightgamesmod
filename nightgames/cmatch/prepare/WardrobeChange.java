package nightgames.cmatch.prepare;

import java.util.List;
import java.util.Stack;

import nightgames.characters.Character;
import nightgames.cmatch.CustomMatch;
import nightgames.items.Clothing;


/**
 * Change to alter a character's wardrobe.
 * It is recommended you apply at most one of these, as multiple will override each other.
 */
public class WardrobeChange implements PrematchChange {

	private Stack<Clothing>[] replacement, original;
	
	
	public WardrobeChange(Stack<Clothing>[] replacement) {
		this.replacement = replacement;
	}

	@Override
	public void apply(CustomMatch match, Character ch, List<PrematchChange> changeList) {
		this.original = ch.outfit;
		ch.outfit = replacement;
		changeList.add(this);
	}

	@Override
	public void revert(CustomMatch match, Character ch) {
		ch.outfit = original;
	}

	@Override
	public PrematchChange copy() {
		return new WardrobeChange(replacement);
	}

}
