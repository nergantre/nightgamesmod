package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Abuff;

public class Drain extends Skill {

	public Drain(Character self) {
		super("Drain", self, 5);
	}

	public Drain(String name, Character self) {
		super(name, self, 5);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Dark)>=15 || user.has(Trait.energydrain);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (this.getSelf().canAct())
				&& (c.getStance().penetration(this.getSelf()));
	}
	@Override
	public int getMojoCost(Combat c) {
		return 30;
	}
	@Override
	public float priorityMod(Combat c) {
		return 2.0f;
	}
	
	@Override
	public String describe() {
		return "Drain your opponent of their energy";
	}

	private void steal(Combat c, Character target, Attribute att, int amount) {
		amount = Math.min(target.get(att), amount);
		if (amount <= 0) { return; }
		target.add(c, new Abuff(target,att, -amount,20));
		getSelf().add(c, new Abuff(getSelf(),att, amount,20));
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		return resolve(c, target, false);
	}

	public boolean resolve(Combat c, Character target, boolean nocost) {
		int strength = Math.max(10, 1 + (getSelf().get(Attribute.Dark) / 4));
		int type = Math.max(1, Global.centeredrandom(6, getSelf().get(Attribute.Dark) / 3.0 ,3));

		if (this.getSelf().human()) {
			c.write(getSelf(),deal(c, type, Result.normal, target));
		} else if (target.human()) {
			c.write(getSelf(),receive(c, type, Result.normal, target));
		}
		switch (type) {
		case 0:
			getSelf().arouse(getSelf().getArousal().max(), c);
		case 1:
			target.drain(c, getSelf(), 50);
			break;
		case 2:
			target.loseMojo(c, 20);
			this.getSelf().buildMojo(c, 20);
			break;
		case 3:
			steal(c, target, Attribute.Cunning, strength);
			target.loseMojo(c, target.getMojo().get());
			break;
		case 4:
			steal(c, target, Attribute.Power, strength);
			target.drain(c, getSelf(), 50);
			break;
		case 5:
			steal(c, target, Attribute.Seduction, strength);
			target.tempt(c, getSelf(), 10);
			break;
		case 6:
			steal(c, target, Attribute.Power, strength);
			steal(c, target, Attribute.Seduction, strength);
			steal(c, target, Attribute.Cunning, strength);
			target.mod(Attribute.Perception, 1);
			target.drain(c, getSelf(), 50);
			target.loseMojo(c, 10);
			target.tempt(c, getSelf(), 10);
			break;
		default:
			break;
		}
		return type != 0;
	}

	@Override
	public Skill copy(Character target) {
		return new Drain(target);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(c.getStance().inserted(target)){
			String muscDesc = c.getStance().analinserted() ? "anal" : "vaginal";
			String partDesc = c.getStance().analinserted() ? getSelf().body.getRandom("ass").describe(getSelf()) : getSelf().body.getRandomPussy().describe(getSelf());
			String base = "You put your powerful " + muscDesc + " muscles to work whilst"
					+ " transfixing " + target.name()
					+ "'s gaze with your own, goading " + target.possessivePronoun() + " energy into "+ target.possessivePronoun() + " cock."
					+ " Soon it erupts from her into your " + partDesc + ", ";
			switch (damage) {
			case 4:
				return base + "and you can feel " + target.possessivePronoun() + " strength pumping into you.";
			case 3:
				return base + "and you can feel " + target.possessivePronoun() + " memories and excperiences flow"
						+ " into you, adding to your skill.";
			case 5:
				return base + "taking " + target.possessivePronoun() + " raw sexual energy and"
						+ " adding it to your own";
			case 1:
				return base + "but unfortunately you made a mistake, and only feel a small"
						+ " bit of " + target.possessivePronoun() + " energy traversing the space between you.";
			case 2:
				return base + "but unfortunately you made a mistake, and only feel a small"
						+ " bit of " + target.possessivePronoun() + " energy traversing the space between you.";
			case 6:
				return base
						+ "far more powerfully than you even thought possible."
						+ " You feel a fragment of " + target.possessivePronoun() + " soul break away from him and"
						+ " spew into you, taking with it a portion of " + target.possessivePronoun() + " strength,"
						+ " skill and wits and merging with your own. You have clearly"
						+ " won this fight, and a lot more than that.";
			default:
				// Should never happen
				return " but nothing happens, you feel strangely impotent.";
			}
		}
		else{
			String base = "With your cock deep inside "+target.name()+", you can feel the heat from her core. You draw the energy from her, mining her depths. ";
			switch (damage) {
			case 4:
				return base + "You feel yourself grow stronger as you steal her physical power.";
			case 5:
				return base + "You manage to steal some of her sexual experience and skill at seduction.";
			case 3:
				return base + "You draw some of her wit and cunning into yourself.";
			case 1:
				return "You attempt to drain "+target.name()+"'s energy through your intimate connection, taking a bit of her energy.";
			case 2:
				return "You attempt to drain "+target.name()+"'s energy through your intimate connection, stealing some of her restraint.";
			case 0:
				return "You attempt to drain "+target.name()+"'s energy through your intimate connection, but it goes wrong. You feel intense pleasure feeding " +
						"back into you and threatening to overwhelm you. You brink the spiritual link as fast as you can, but you're still left on the brink of " +
						"climax.";
			case 6:
				return base
						+ "You succeed in siphoning off a portion of her soul, stealing both her physical and mental strength. This energy will eventually " +
						"return to its owner, but for now, you're very powerful!";
			default:
				// Should never happen
				return " but nothing happens, you feel strangely impotent.";
			}
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		String muscDesc = c.getStance().analinserted() ? "anal" : "vaginal";
		String partDesc = c.getStance().analinserted() ? getSelf().body.getRandom("ass").describe(getSelf()) : getSelf().body.getRandomPussy().describe(getSelf());
		
		String base = "You feel " + getSelf().nameOrPossessivePronoun() + " powerful" + muscDesc+ " muscles suddenly tighten around you. "
				+ "She starts kneading your dick bringing you immense pleasure and soon"
				+ " you feel yourself erupt into her, but you realize your are shooting"
				+ " something far more precious than semen into her " + partDesc + "; as more of the ethereal"
				+ " fluid leaves you, you feel ";
		switch (damage) {
		case 4:
			return base + "your strength leaving you with it, "
					+ "making you more tired than you have ever felt.";
		case 5:
			return base
					+ "memories of previous sexual experiences escape your mind,"
					+ " numbing your skills, rendering you more sensitive and"
					+ " perilously close to the edge of climax.";
		case 3:
			return base + "your mind go numb, causing your confidence and"
					+ "cunning to flow into her.";
		case 1:
			return "Clearly the succubus is trying to do something really special to you, "
					+ "as you can feel the walls of her " + partDesc + " squirm against you in a way "
					+ "no human could manage, but all you feel is some drowsiness";
		case 2:
			return "Clearly the succubus is trying to do something really special to you, "
					+ "as you can feel the walls of her " + partDesc + " squirm against you in a way "
					+ "no human could manage, but all you feel is your focus waning some";
		case 0:
			return getSelf().name()+" squeezes you with her " + partDesc + " and starts to milk you, but you suddenly feel her shudder and moan loudly. Looks like her plan backfired.";
		case 6:
			return base
					+ "something snap loose inside of you and it seems to flow right "
					+ "through your dick and into her. When it is over you feel... empty "
					+ "somehow. At the same time, "+getSelf().name()+" seems radiant, looking more powerful,"
					+ " smarter and even more seductive than before. Through all of this,"
					+ " she has kept on thrusting and you are right on the edge of climax."
					+ " Your defeat appears imminent, but you have already lost something"
					+ " far more valuable than a simple sex fight...";
		default:
			// Should never happen
			return " nothing. You should be feeling something, but you're not.";
		}
	}

	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		if (c.getStance().inserted(getSelf())) {
			return "pussy";
		} else {
			return "cock";
		}
	}
	public String getWithOrganType(Combat c, Character target) {
		if (c.getStance().inserted(getSelf())) {
			return "cock";
		} else {
			return "pussy";
		}
	}
}
