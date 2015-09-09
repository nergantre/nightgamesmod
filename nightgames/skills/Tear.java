package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;

public class Tear extends Skill {

	public Tear(Character self) {
		super("Tear Clothes", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Power) >= 32 || user.get(Attribute.Animism) >= 12;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return ((c.getStance().reachTop(getSelf()) && !target.breastsAvailable())
				|| (((c.getStance().oral(getSelf()) || c.getStance().reachBottom(getSelf()))
						&& !target.crotchAvailable())))
				&& getSelf().canAct();
	}

	@Override
	public String describe(Combat c) {
		return "Rip off your opponent's clothes";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (c.getStance().reachTop(getSelf()) && !target.getOutfit().slotEmpty(ClothingSlot.top)) {
			Clothing article = target.getOutfit().getTopOfSlot(ClothingSlot.top);
			if (!article.is(ClothingTrait.indestructible) && getSelf().get(Attribute.Animism) >= 12
					&& (getSelf().check(Attribute.Power,
							article.dc()
									+ (target.getStamina().percent() - (target.getArousal().percent()) / 4)
									+ getSelf().get(Attribute.Animism) * getSelf().getArousal().percent() / 100)
							|| !target.canAct())) {
				if (getSelf().human()) {
					c.write(getSelf(), "You channel your animal spirit and shred " + target.name() + "'s "
							+ article.getName() + " with claws you don't actually have.");
				} else if (target.human()) {
					c.write(getSelf(),
							getSelf().name() + " lunges towards you and rakes her nails across your " 
									+ article.getName() + ", shredding the garment. That shouldn't be possible. Her "
							+ "nails are not that sharp, and if they were, you surely wouldn't have gotten away unscathed.");
				}
				target.shred(ClothingSlot.top);
				if (getSelf().human() && target.mostlyNude()) {
					c.write(target.nakedLiner(c));
				}
			} else if (!article.is(ClothingTrait.indestructible)
					&& getSelf().check(Attribute.Power,
							article.dc()
									+ (target.getStamina().percent() - target.getArousal().percent()) / 4)
					|| !target.canAct()) {
				if (getSelf().human()) {
					c.write(getSelf(), target.name() + " yelps in surprise as you rip her "
							+ article.getName() + " apart.");
				} else if (target.human()) {
					c.write(getSelf(),
							getSelf().name() + " violently rips your " + article.getName() + " off.");
				}
				target.shred(ClothingSlot.top);
				if (getSelf().human() && target.mostlyNude()) {
					c.write(target, target.nakedLiner(c));
				}
			} else {
				if (getSelf().human()) {
					c.write(getSelf(), "You try to tear apart " + target.name() + "'s " + article.getName()
							+ ", but the material is more durable than you expected.");
				} else if (target.human()) {
					c.write(getSelf(), getSelf().name() + " yanks on your " + article.getName()
							+ ", but fails to remove it.");
				}
			}
		} else if (!target.getOutfit().slotEmpty(ClothingSlot.top)) {
			Clothing article = target.getOutfit().getTopOfSlot(ClothingSlot.bottom);
			if (!article.is(ClothingTrait.indestructible) && getSelf().get(Attribute.Animism) >= 12
					&& getSelf().check(Attribute.Power,
							article.dc()
									+ (target.getStamina().percent() - (target.getArousal().percent()) / 4)
									+ getSelf().get(Attribute.Animism) * getSelf().getArousal().percent() / 100)
					|| !target.canAct()) {
				if (getSelf().human()) {
					c.write(getSelf(), "You channel your animal spirit and shred " + target.name() + "'s "
							+ article.getName() + " with claws you don't actually have.");
				} else if (target.human()) {
					c.write(getSelf(),
							getSelf().name() + " lunges towards you and rakes her nails across your "
									+ article.getName() + ", shredding the garment. That shouldn't be possible. Her "
							+ "nails are not that sharp, and if they were, you surely wouldn't have gotten away unscathed.");
				}
				target.shred(ClothingSlot.bottom);
				if (getSelf().human() && target.mostlyNude()) {
					c.write(target, target.nakedLiner(c));
				}
				if (target.human() && target.crotchAvailable()) {
					if (target.getArousal().get() >= 15) {
						c.write("Your boner springs out, no longer restrained by your pants.");
					} else {
						c.write(getSelf().name() + " giggles as your flaccid dick is exposed");
					}
				}
				target.emote(Emotion.nervous, 10);
			}
			if (!article.is(ClothingTrait.indestructible)
					&& getSelf().check(Attribute.Power,
							article.dc()
									+ (target.getStamina().percent() - target.getArousal().percent()) / 4)
					|| !target.canAct()) {
				if (getSelf().human()) {
					c.write(getSelf(), target.name() + " yelps in surprise as you rip her "
							+ article.getName() + " apart.");
				} else if (target.human()) {
					c.write(getSelf(),
							getSelf().name() + " violently rips your " + article.getName() + " off.");
				}
				target.shred(ClothingSlot.bottom);
				if (getSelf().human() && target.mostlyNude()) {
					c.write(target, target.nakedLiner(c));
				}
				if (target.human() && target.crotchAvailable()) {
					if (target.getArousal().get() >= 15) {
						c.write("Your boner springs out, no longer restrained by your pants.");
					} else {
						c.write(getSelf().name() + " giggles as your flaccid dick is exposed");
					}
				}
				target.emote(Emotion.nervous, 10);
			} else {
				if (getSelf().human()) {
					c.write(getSelf(), "You try to tear apart " + target.name() + "'s " + article.getName()
							+ ", but the material is more durable than you expected.");
				} else if (target.human()) {
					c.write(getSelf(), getSelf().name() + " yanks on your " + article.getName()
							+ ", but fails to remove them.");
				}
				return false;
			}
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Tear(user);
	}

	public String getLabel(Combat c) {
		if (getSelf().get(Attribute.Animism) >= 12) {
			return "Shred Clothes";
		} else {
			return "Tear Clothes";
		}
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
