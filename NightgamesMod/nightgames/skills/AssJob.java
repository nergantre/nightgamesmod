package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;

public class AssJob extends Skill {

	public AssJob(Character self) {
		super("Assjob", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction) >= 25;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && target.hasDick() && selfNakedOrUnderwear()
				&& (c.getStance().behind(target)
						|| c.getStance().en == Stance.reversemount
						|| c.getStance().mobile(getSelf())
								&& !c.getStance().prone(getSelf())
								&& !c.getStance().behind(getSelf()));
	}

	@Override
	public String describe(Combat c) {
		return "Hump your opponent's cock with your ass";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (c.getStance().behind(target)) {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.special, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, Result.special, target));
			}
			int m = 5 + Global.random(4);
			int fetishChance = 40 + getSelf().get(Attribute.Fetish) / 2;
			if (target.crotchAvailable()) {
				if (getSelf().crotchAvailable()) {
					m += 8;
					fetishChance += 30;
				} else {
					m += 4;
					fetishChance += 15;
				}
			}
			target.body.pleasure(getSelf(), getSelf().body.getRandomAss(),
					target.body.getRandomCock(), m, c);

			if (Global.random(100) < fetishChance) {
				target.add(new BodyFetish(target, getSelf(), "ass",
						.1 + getSelf().get(Attribute.Fetish) * .05));
			}
		} else if (target.roll(this, c, accuracy(c))) {
			if (c.getStance().en == Stance.reversemount) {
				if (getSelf().human()) {
					c.write(getSelf(), deal(c, 0, Result.strong, target));
				} else if (target.human()) {
					c.write(getSelf(), receive(c, 0, Result.strong, target));
				}
				int m = 5 + Global.random(4);
				int fetishChance = 40 + getSelf().get(Attribute.Fetish) / 2;
				if (target.crotchAvailable()) {
					if (getSelf().crotchAvailable()) {
						m += 8;
						fetishChance += 30;
					} else {
						m += 4;
						fetishChance += 15;
					}
				}
				if (target.body.getRandomCock().isReady(target)) {
					target.body.pleasure(getSelf(),
							getSelf().body.getRandomAss(),
							target.body.getRandomCock(), m, c);
				} else {
					target.tempt(c, getSelf(), getSelf().body.getRandomAss(),
							m);
				}

				if (Global.random(100) < fetishChance) {
					target.add(new BodyFetish(target, getSelf(), "ass",
							.1 + getSelf().get(Attribute.Fetish) * .05));
				}
			} else {
				if (getSelf().human()) {
					c.write(getSelf(), deal(c, 0, Result.normal, target));
				} else if (target.human()) {
					c.write(getSelf(), receive(c, 0, Result.normal, target));
				}
				int m = 4 + Global.random(3);
				if (target.crotchAvailable()) {
					if (getSelf().crotchAvailable()) {
						m += 6;
					} else {
						m += 3;
					}
				}
				target.body.pleasure(getSelf(), getSelf().body.getRandomAss(),
						target.body.getRandomCock(), m, c);
			}
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
	public Skill copy(Character user) {
		return new AssJob(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	private boolean selfNakedOrUnderwear() {
		return getSelf().getOutfit().slotEmptyOrMeetsCondition(
				ClothingSlot.bottom, c -> c.getLayer() == 0);
	}

	private boolean selfWearingUnderwear() {
		return getSelf().getOutfit().getSlotAt(ClothingSlot.bottom, 0) != null;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		switch (modifier) {
			case special:
				if (getSelf().crotchAvailable() && target.crotchAvailable()) {
					return String.format(
							"You push your naked ass back against"
									+ " %s %s, rubbing it with vigor.",
							target.nameOrPossessivePronoun(),
							target.body.getRandomCock().describe(target));
				} else {
					return String.format(
							"You relax slightly in %s arms and rub your ass"
									+ " into %s crotch.",
							target.nameOrPossessivePronoun(), target.name());
				}
			case strong:
				if (!target.crotchAvailable()) {
					return String.format(
							"You hump your ass against %s covered groin.",
							target.nameOrPossessivePronoun());
				} else if (target.body.getRandomCock().isReady(getSelf())) {
					return String.format(
							"You wedge %s %s in your soft crack and"
									+ " firmly rub it up against you, eliciting a quiet moan from"
									+ " %s.",
							target.nameOrPossessivePronoun(),
							target.body.getRandomCock().describe(target),
							target.directObject());
				} else {
					return String.format(
							"You lean back and rub your ass against %s, but"
									+ " %s %s is still too soft to really get into it.",
							target.name(), target.possessivePronoun(),
							target.body.getRandomCock().describe(target));
				}
			case normal:
				return String.format(
						"You back up against %s and grab %s by the waist."
								+ " Before %s has a chance to push you away, you rub your ass against"
								+ " %s crotch.",
						target.name(), target.directObject(), target.pronoun(),
						target.possessivePronoun());
			case miss:
			default:
				return String.format(
						"You try to mash your ass against %s crotch, but %s"
								+ " pushes you away.",
						target.nameOrPossessivePronoun(), target.pronoun());
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		switch (modifier) {
			case special:
				String res = String.format(
						"You hold %s tight, thinking %s intends to break "
								+ "free your hold. But instead %s pushes %s firm asscheeks"
								+ " against your cock and grinds them against you. ",
						getSelf().name(), getSelf().pronoun(),
						getSelf().pronoun(), getSelf().possessivePronoun());
				if (getSelf().crotchAvailable() && target.crotchAvailable()) {
					res += String
							.format("Your %s slides between %s mounds as if it belongs there.",
									target.body.getRandomCock()
											.describe(target),
									getSelf().possessivePronoun());
				} else {
					res += String.format(
							"The swells of %s ass feel great on your cock even through the clothing between you.",
							getSelf().possessivePronoun());
				}
				return res;
			case strong:
				if (!target.crotchAvailable()) {
					return String.format(
							"%s sits firmly on your crotch and starts "
									+ "dryhumping you with an impish grin. As %s grinds against your "
									+ "%s restlessly, you're definitely feeling it much more than she is.",
							getSelf().name(), getSelf().pronoun(),
							target.outfit.getTopOfSlot(ClothingSlot.bottom)
									.getName());
				} else if (target.body.getRandomCock().isReady(getSelf())) {
					return String.format(
							"%s lays back on you, squeezing your %s between %s soft asscheeks. You try to "
									+ "crawl away, but %s grinds %s perky butt against you, massaging your hard-on %s.",
							getSelf().name(),
							target.body.getRandomCock().describe(getSelf()),
							getSelf().possessivePronoun(), getSelf().pronoun(),
							getSelf().possessivePronoun(),
							selfWearingUnderwear()
									? "with her soft " + getSelf().getOutfit()
											.getBottomOfSlot(
													ClothingSlot.bottom)
											.getName()
									: "in her luscious crack");
				} else {
					return String.format(
							"You try to slide from under %s, but %s leans "
									+ "forward, holding down your legs. You feel %s round ass press"
									+ " against your groin as %s sits back on you. <i>\"Like what "
									+ "you see?\"</i> - %s taunts you, shaking %s hips invitingly.",
							getSelf().name(), getSelf().pronoun(),
							getSelf().possessivePronoun(), getSelf().pronoun(),
							getSelf().pronoun(), getSelf().possessivePronoun());
				}
			case normal:
				return String.format(
						"Unexpectedly, %s turns around and rams %s waist against "
								+ "your groin, taking hold of your arms before you can recover your balance."
								+ " %s takes the opportunity to tease you, rubbing %s bubble butt against "
								+ "your sensitive %s.",
						getSelf().name(), getSelf().possessivePronoun(),
						Global.capitalizeFirstLetter(getSelf().pronoun()),
						getSelf().possessivePronoun(),
						target.body.getRandomCock().describe(target));
			case miss:
			default:
				return String.format(
						"%s moves %s ass towards your crotch, but you push her away.",
						getSelf().name(), getSelf().possessivePronoun());
		}
	}

}
