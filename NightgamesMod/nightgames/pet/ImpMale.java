package nightgames.pet;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.status.Horny;

public class ImpMale extends Pet {

	public ImpMale(Character owner) {
		super("Imp", owner, Ptype.impmale, 3, 2);
	}

	public ImpMale(Character owner, int power, int ac) {
		super("Imp", owner, Ptype.impmale, power, ac);
	}

	@Override
	public String describe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void act(Combat c, Character target) {
		if (target.human()) {

		} else {
			switch (Global.random(4)) {
			case 3:
				c.write(owner(),
						"Your imp masturbates frantically until he cums intensely. He aims his spurting cock at "
								+ target.name() + ", hitting her in the face with a thick load "
								+ "of semen. The imp blinks out of existence, but the damage is done. " + target.name()
								+ " flushes bright red and looks stunned as the aphrodisiac laden fluid "
								+ "overwhelms her senses.");
				target.arouse(4 + 4 * Global.random(power), c);
				target.add(c, new Horny(target, 5, 5, "imp cum"));
				remove();
				break;
			case 2:
				if (target.crotchAvailable() && !c.getStance().vaginallyPenetrated(target)) {
					c.write(owner(), "Your imp latches onto " + target.name()
							+ " and shoves his thick cock into her pussy. As the demon humps her, she shrieks and punches him away.");
					target.body.pleasure(null, null, target.body.getRandom("pussy"), 2 + 3 * Global.random(power), c);
				} else {
					c.write(owner(), own() + "imps strokes himself while watching the fight.");
				}
				break;
			case 1:
				if (target.breastsAvailable()) {
					c.write(owner(), "Your imp jumps onto " + target.name()
							+ " and grabs her nipples. Unable to fight the law of gravity, the imp falls back to the floor, pulling painfully on her nipples.");
					target.pain(c, 3 + 2 * Global.random(power), false);
				} else {
					c.write(owner(), own() + "imps strokes himself while watching the fight.");
				}
				break;
			default:
				if (!target.breastsAvailable()) {
					if (Global.random(25) > target.getOutfit().getTopOfSlot(ClothingSlot.top).dc()
							+ (target.getStamina().percent() - target.getArousal().percent()) / 4 || !target.canAct()) {
						c.write(owner(),
								own() + "imp steals " + target.name() + "'s "
										+ target.getOutfit().getTopOfSlot(ClothingSlot.top).getName()
										+ " and runs off with it.");
						target.strip(ClothingSlot.top, c);
					} else {
						c.write(owner(), own() + "imp yanks on " + target.name() + "'s "
								+ target.getOutfit().getTopOfSlot(ClothingSlot.top).getName() + " ineffectually.");
					}
				} else if (!target.crotchAvailable()) {
					if (Global.random(25) > target.getOutfit().getTopOfSlot(ClothingSlot.bottom).dc()
							+ (target.getStamina().percent() - target.getArousal().percent()) / 4 || !target.canAct()) {
						c.write(owner(),
								own() + "imp steals " + target.name() + "'s "
										+ target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()
										+ " and runs off with it.");
						target.strip(ClothingSlot.bottom, c);
					} else {
						c.write(owner(), own() + "imp yanks on " + target.name() + "'s "
								+ target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName() + " ineffectually.");
					}
				} else {
					c.write(owner(), own() + "imps strokes himself while watching the fight.");
				}
			}
		}
	}

	@Override
	public void vanquish(Combat c, Pet opponent) {
		switch (opponent.type()) {
		case fairyfem:
			c.write(owner(),
					own() + "imp manages to catch " + opponent.own()
							+ "faerie as the tiny fae flies around his head. He shoves his cock into the faerie's face, smearing his pre-cum over her. "
							+ "He presses her against his shaft, stroking himself with her entire body. The faerie, intoxicated by the potent fluid is unable to last long and cums with a high "
							+ "pitched moan.");
			break;
		case fairymale:
			c.write(owner(), own() + "imp swats " + opponent.own() + "faerie out of the air.");
			break;
		case impfem:
			c.write(owner(),
					own() + "imp grabs " + opponent.own()
							+ "female imp and bends her over. He rams his cock into her wet box without any foreplay. The female groans in protest and flails about "
							+ "in an attempt to take control, but she's held fast. The male shows impressive stamina, fucking the female until she orgasms and vanishes right off his dick. He then seeks a "
							+ "new target for his unsatisfied cock.");
			break;
		case impmale:
			c.write("");
			break;
		case slime:
			c.write(owner(),
					own() + "imp pins " + opponent.own()
							+ "slime under foot and lets his leaking cock drip onto the amorphous mass. As the slime absorbs the first drops of pre-cum, it starts to "
							+ "frantically attempt to reach the imp's penis. It seems unable to change shape though and the demon keeps it pinned while letting more pre-cum drip down. After absorbing enough "
							+ "fluid, the slime's color darkens and it gradually solidifies, unable to move.");
			break;
		default:
			break;
		}
		opponent.remove();
	}

	@Override
	public void caught(Combat c, Character captor) {
		if (owner().human()) {
			c.write(captor, captor.name()
					+ " shoves your imp to the floor and pins its cock under her foot. She grinds her foot, lubricated with the pre-cum that's streaming from "
					+ "your minion's erection. The imp jabbers incoherently as it shoots its load and disappears, leaving only a puddle of cum.");
		} else if (captor.human()) {
			c.write("");
		}
		remove();
	}

	@Override
	public boolean hasDick() {
		return true;
	}
}
