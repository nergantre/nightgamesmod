package nightgames.items;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class MaybeEffect extends ItemEffect {
	private ItemEffect effect;
	private double probability;

	public MaybeEffect(ItemEffect other, double probability) {
		super("", "", true, true);
		this.probability = probability;
		effect = other;
	}

	@Override
	public boolean use(Combat c, Character user, Character opponent, Item item) {
		if (Global.randomdouble() < probability) {
			return effect.use(c, user, opponent, item);
		}
		return false;
	}
}