package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;

public class FairyFem extends Pet {

	public FairyFem(Character owner) {
		super("faerie", owner, Ptype.fairyfem, 2, 4);
	}

	public FairyFem(Character owner, int power, int ac) {
		super("faerie", owner, Ptype.fairyfem, power, ac);
	}

	@Override
	public String describe() {
		return null;
	}

	@Override
	public void act(Combat c, Character target) {
		if (target.human()) {
			switch (Global.random(4)) {
			case 3:
				if (target.crotchAvailable()) {
					c.write(owner(), own()
							+ "faerie flies at you and kicks you in the balls. She doesn't have a lot of weight to put behind it, but it still hurts like hell.");
					if (target.has(Trait.achilles)) {
						target.pain(c, 4 + Global.random(4), false);
					}
					target.pain(c, 3 + 2 * Global.random(power), false);
				} else {
					c.write(owner(), own() + "faerie flies around the edge of the fight looking for an opening.");
				}
				break;
			case 2:
				if (!c.getStance().inserted(target)) {
					if (target.crotchAvailable()) {
						c.write(owner(), own()
								+ "faerie hugs your dick and rubs it with her entire body until you pull her off.");
						target.body.pleasure(null, null, target.body.getRandom("cock"), 2 + 3 * Global.random(power),
								c);
					} else {
						c.write(owner(),
								own() + "faerie slips into your "
										+ target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()
										+ " and plays with your penis until you manage to remove her.");
						target.body.pleasure(null, null, target.body.getRandom("cock"), 2 + 3 * Global.random(power),
								c);
					}
				} else {
					c.write(owner(), own() + "faerie flies around the edge of the fight looking for an opening.");
				}
				break;
			case 1:
				c.write(owner(), own() + "faerie flies around " + owner().name() + ", channelling energy into her.");
				owner().buildMojo(c, 20 + power);
				break;
			default:
				c.write(owner(),
						own() + "faerie rains magic energy on " + owner().name() + ", restoring her strength.");
				owner().heal(c, power + Global.random(10));
			}
		} else {
			switch (Global.random(4)) {
			case 3:
				if (target.breastsAvailable()) {
					c.write(owner(),
							"Your faerie lands on " + target.name() + "'s tit and plays with her sensitive nipple.");
					target.body.pleasure(null, null, target.body.getRandom("breasts"), 3 + 2 * Global.random(power), c);
					target.pain(c, 3 + 2 * Global.random(power), false, true);
				} else {
					c.write(owner(), own() + "faerie flies around the edge of the fight looking for an opening.");
				}
				break;
			case 2:
				c.write(owner(), own() + "faerie flies around the edge of the fight looking for an opening.");
				break;
			case 1:
				c.write(owner(),
						"Your faerie circles around your with a faint glow and kisses you on the cheek. You feel energy building inside you.");
				owner().buildMojo(c, 20);
				break;
			default:
				c.write(owner(),
						"Your faerie flies next to your ear in speaks to you. The words aren't in english and you have no idea what she said, but somehow you feel your fatigue drain away.");
				owner().heal(c, power + Global.random(10));
			}
		}
	}

	@Override
	public void vanquish(Combat c, Pet opponent) {
		switch (opponent.type()) {
		case fairyfem:
			c.write(owner(),
					"The two faeries circle around each other vying for the upper hand. " + own() + "faerie catches "
							+ opponent.own() + "faerie by the hips and starts to eat her out. "
							+ opponent
									.own()
					+ " fae struggles to break free, but can barely keep flying as she rapidly reaches orgasm and vanishes.");
			break;
		case fairymale:
			c.write(owner(), "The faeries zip through the air like a couple dogfighting planes. " + opponent.own()
					+ "male manages to catch the female's hands, but you see her foot shoot up decisively "
					+ "between his legs. The stricken male tumbles lazily to the floor and vanishes in midair.");
			break;
		case impfem:
			c.write(owner(),
					own() + " faerie flies between the legs of " + opponent.own()
							+ "imp, slipping both arms into the larger pussy. The imp trembles as falls to the floor as the faerie puts her entire "
							+ "upper body into pleasuring her. " + own()
							+ "faerie is forcefully expelled by the imp's orgasm just before the imp vanishes.");
			break;
		case impmale:
			c.write(owner(),
					opponent.own() + "imp grabs at " + own()
							+ "faerie, but the nimble sprite changes direct in midair and darts between the imp's legs. You can't see exactly what happens next, "
							+ "but the imp clutches his groin in pain and disappears.");
			break;
		case slime:
			c.write(owner(), own() + "fae glows with magic as it circles " + opponent.own()
					+ "slime rapidly. The slime begins to tremble and slowly elongates into the shape of a crude phallis. It shudders "
					+ "violently and sprays liquid from the tip until the entire creature is a puddle on the floor.");
			break;
		default:
			break;
		}
		opponent.remove();
	}

	@Override
	public void caught(Combat c, Character captor) {
		if (captor.human()) {
			c.write(captor, "You snag " + own()
					+ "faerie out of the air. She squirms in your hand, but has no chance of breaking free. You lick the fae from pussy to breasts and the little thing squeals "
					+ "in pleasure. The taste is surprisingly sweet and makes your tongue tingle. You continue lapping up the flavor until she climaxes and disappears.");
		} else {
			c.write(captor, captor.name()
					+ " manages to catch your faerie and starts pleasuring her with the tip of her finger. The sensitive fae clings to the probing finger desperately as she thrashes "
					+ "in ecstasy. Before you can do anything to help, your faerie vanishes in a burst of orgasmic magic.");
		}
		remove();
	}

	@Override
	public boolean hasPussy() {
		return true;
	}
}
