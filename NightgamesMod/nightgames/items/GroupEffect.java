package nightgames.items;

import java.util.Collection;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class GroupEffect extends ItemEffect {
	private Collection<ItemEffect> others;

	interface EffectCondition {
		boolean operation(Combat c, Character user, Character opponent,
				Item item);
	}

	public GroupEffect(Collection<ItemEffect> others) {
		super("", "", true, true);
		this.others = others;
	}

	@Override
	public boolean use(Combat c, Character user, Character opponent,
			Item item) {
		boolean changed = false;
		for (ItemEffect effect : others) {
			changed = effect.use(c, user, opponent, item) || changed;
		}
		return changed;
	}
}