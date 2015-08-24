package nightgames.cmatch.prepare;

import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.cmatch.CustomMatch;

/**
 * Change to ensure a character has a specific trait.
 */
public class GiveTraitChange implements PrematchChange {

	private Trait trait;
	private boolean hadTrait;
	
	public GiveTraitChange(Trait trait) {
		this.trait = trait;
	}

	@Override
	public void apply(CustomMatch match, Character ch, List<PrematchChange> changeList) {
		hadTrait = ch.has(trait);
		if (!hadTrait) {
			ch.add(trait);
			changeList.add(this);
		}
	}

	@Override
	public void revert(CustomMatch match, Character ch) {
		if (!hadTrait)
			ch.remove(trait);
	}

	@Override
	public PrematchChange copy() {
		return new GiveTraitChange(trait);
	}

}
