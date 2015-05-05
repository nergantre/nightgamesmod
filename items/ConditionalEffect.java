package items;

import global.Global;
import combat.Combat;
import characters.Character;
import characters.body.Body;
import characters.body.TentaclePart;
import status.Status;

public class ConditionalEffect extends ItemEffect {
	private ItemEffect effect;
	private EffectCondition condition;
	public interface EffectCondition {
		boolean operation(Combat c, Character user, Character opponent, Item item);
	}
	public ConditionalEffect(ItemEffect other, EffectCondition condition) {
		super("","",true, true);
		this.condition = condition;
		this.effect = other;
	}
	public boolean use(Combat c, Character user, Character opponent, Item item) {
		if (condition.operation(c, user, opponent, item)) {
			return effect.use(c, user, opponent, item);
		}
		return false;
	}
}