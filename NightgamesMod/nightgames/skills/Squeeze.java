package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;

public class Squeeze extends Skill {

	public Squeeze(Character self) {
		super("Squeeze Balls", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.hasBalls() && c.getStance().reachBottom(getSelf()) && getSelf().canAct()
				&& !getSelf().has(Trait.shy);
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 5;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (target.roll(this, c, accuracy(c))) {
			if (target.has(Trait.brassballs)) {
				if (target.human()) {
					c.write(getSelf(), receive(c, 0, Result.weak2, target));
				} else if (getSelf().human()) {
					c.write(getSelf(), deal(c, 0, Result.weak2, target));
				}
			} else if (target.crotchAvailable()) {
				if (getSelf().has(Item.ShockGlove) && getSelf().has(Item.Battery, 2)) {
					getSelf().consume(Item.Battery, 2);
					if (target.human()) {
						c.write(getSelf(), receive(c, 0, Result.special, target));
					} else if (getSelf().human()) {
						c.write(getSelf(), deal(c, 0, Result.special, target));
					}
					target.pain(c, Global.random(8 + target.get(Attribute.Perception)) + 16);
					if (target.has(Trait.achilles)) {
						target.pain(c, 6);
					}
				} else if (target.has(ClothingTrait.armored)) {
					if (target.human()) {
						c.write(getSelf(), receive(c, 0, Result.item, target));
					} else if (getSelf().human()) {
						c.write(getSelf(), deal(c, 0, Result.item, target));
					}
					target.pain(c, Global.random(3));
				} else {
					if (target.human()) {
						c.write(getSelf(), receive(c, 0, Result.normal, target));
					} else if (getSelf().human()) {
						c.write(getSelf(), deal(c, 0, Result.normal, target));
					}
					if (target.has(Trait.achilles)) {
						target.pain(c, 4);
					}
					target.pain(c, Global.random(7) + 5);
				}
			} else {
				if (target.human()) {
					c.write(getSelf(), receive(c, 0, Result.weak, target));
				} else if (getSelf().human()) {
					c.write(getSelf(), deal(c, 0, Result.weak, target));
				}
				target.pain(c, (int) Math.round(Global.random(7) + 5 - 5 * target.getExposure(ClothingSlot.bottom)));
			}

			target.emote(Emotion.angry, 15);
		} else {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.miss, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Power) >= 9;
	}

	@Override
	public Skill copy(Character user) {
		return new Squeeze(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.damage;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.miss) {
			return "You try to grab " + target.name() + "'s balls, but she avoids it.";
		} else if (modifier == Result.special) {
			return "You use your shock glove to deliver a painful jolt directly into " + target.name()
					+ "'s testicles.";
		} else if (modifier == Result.weak) {
			return "You grab the bulge in " + target.name() + "'s "
					+ target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName() + " and squeeze.";
		} else if (modifier == Result.weak2) {
			return "You grab " + target.name() + "by the balls and squeeze hard, but" + target.pronoun()
					+ " does not flinch at all.";
		} else if (modifier == Result.item) {
			return "You grab the bulge in " + target.name() + "'s "
					+ target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()
					+ ", but find it solidly protected.";
		} else {
			return "You manage to grab " + target.name()
					+ "'s balls and squeeze them hard. You feel a twinge of empathy when she cries out in pain, but you maintain your grip.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.miss) {
			return getSelf().name() + " grabs at your balls, but misses.";
		} else if (modifier == Result.special) {
			return getSelf().name()
					+ " grabs your naked balls roughly in her gloved hand. A painful jolt of electricity shoots through your groin, sapping your will to fight.";
		} else if (modifier == Result.weak) {
			return getSelf().name() + " grabs your balls through your "
					+ target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName() + " and squeezes hard.";
		} else if (modifier == Result.weak2) {
			return getSelf().name() + " grins menacingly and firmly grabs your nuts. "
					+ Global.capitalizeFirstLetter(getSelf().pronoun()) + " squeezes as hard as " + getSelf().pronoun()
					+ "can, but you hardly feel it.";
		} else if (modifier == Result.item) {
			return getSelf().name() + " grabs your crotch through your "
					+ target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName() + ", but you can barely feel it.";
		} else {
			return getSelf().name()
					+ " reaches between your legs and grabs your exposed balls. You writhe in pain as she pulls and squeezes them.";
		}
	}

	@Override
	public String getLabel(Combat c) {
		if (getSelf().has(Item.ShockGlove)) {
			return "Shock Balls";
		} else {
			return getName(c);
		}
	}

	@Override
	public String describe(Combat c) {
		return "Grab opponent's groin; deals more damage if she's naked";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
