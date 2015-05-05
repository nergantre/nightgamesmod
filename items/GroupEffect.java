package items;

import java.util.Collection;

import global.Global;
import combat.Combat;
import characters.Character;
import characters.body.Body;
import characters.body.TentaclePart;
import status.Status;

public class GroupEffect extends ItemEffect {
	private Collection<ItemEffect> others;
	interface EffectCondition {
		boolean operation(Combat c, Character user, Character opponent, Item item);
	}

	public GroupEffect(Collection<ItemEffect> others) {
		super("","",true, true);
		this.others = others;
	}

	public boolean use(Combat c, Character user, Character opponent, Item item) {
		boolean changed = false;
		for (ItemEffect effect : others) {
			changed = effect.use(c, user, opponent, item) || changed;
		}
		return changed;
	}
}