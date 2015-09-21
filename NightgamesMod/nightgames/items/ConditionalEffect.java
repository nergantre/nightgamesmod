package nightgames.items;

import nightgames.characters.Character;
import nightgames.characters.body.Body;
import nightgames.characters.body.TentaclePart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Status;

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