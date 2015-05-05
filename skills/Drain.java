package skills;

import status.Abuff;
import global.Global;
import characters.Character;
import characters.Trait;
import characters.Attribute;
import combat.Combat;
import combat.Result;

public class Drain extends Skill {

	public Drain(Character self) {
		super("Drain", self, 5);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Dark)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (this.self.canAct()) && (c.getStance().canthrust(this.self))
				&& (c.getStance().penetration(this.self))
				&& (this.self.canSpend(20));
	}
	
	@Override
	public float priorityMod(Combat c) {
		return 2.0f;
	}
	
	@Override
	public String describe() {
		return "Drain your opponent of their energy";
	}

	private void steal(Character target, Attribute att, int amount) {
		amount = Math.min(target.get(att), amount);
		if (amount <= 0) { return; }
		target.add(new Abuff(target,att, -amount,20));
		self.add(new Abuff(self,att, amount,20));
	}

	@Override
	public void resolve(Combat c, Character target) {
		resolve(c, target, false);
	}

	public void resolve(Combat c, Character target, boolean nocost) {
		int strength = Math.max(10, 1 + (self.get(Attribute.Dark) / 4));
		if (!nocost) {
			self.spendMojo(c, 20);
		}
		int type = Global.centeredrandom(6, self.get(Attribute.Dark) / 3.0 ,3);

		if (this.self.human()) {
			c.write(self,deal(c, type, Result.normal, target));
		} else if (target.human()) {
			c.write(self,receive(c, type, Result.normal, target));
		}
		switch (type) {
		case 0:
			self.arouse(self.getArousal().max(), c);
		case 1:
			target.weaken(c, 50);
			this.self.heal(c, 50);
			break;
		case 2:
			target.loseMojo(c, 20);
			this.self.buildMojo(c, 20);
			break;
		case 3:
			steal(target, Attribute.Cunning, strength);
			target.loseMojo(c, target.getMojo().get());
			break;
		case 4:
			steal(target, Attribute.Power, strength);
			target.weaken(c, 10);
			break;
		case 5:
			steal(target, Attribute.Seduction, strength);
			target.tempt(c, self, 10);
			break;
		case 6:
			steal(target, Attribute.Power, strength);
			steal(target, Attribute.Seduction, strength);
			steal(target, Attribute.Cunning, strength);
			target.mod(Attribute.Perception, 1);
			target.weaken(c, 10);
			target.spendMojo(c, 10);
			target.tempt(c, self, 10);
			break;
		default:
			break;
		}
		self.getMojo().gain(1);
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
			String base = "You put your powerful vaginal muscles to work whilst"
					+ " transfixing " + target.name()
					+ "'s gaze with your own, goading " + target.possessivePronoun() + " energy into "+ target.possessivePronoun() + " cock."
					+ " Soon it erupts from her into your " + self.body.getRandomPussy() + ", ";
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
						+ "You succeed in siphoning off a portion of her soul, stealing both her physical and mental strength. This energy will eventaully " +
						"return to its owner, but for now, you're very powerful!";
			default:
				// Should never happen
				return " but nothing happens, you feel strangely impotent.";
			}
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		String base = "You feel the succubus' pussy suddenly tighten around you. "
				+ "She starts kneading your dick bringing you immense pleasure and soon"
				+ " you feel yourself erupt into her, but you realize your are shooting"
				+ " something far more precious than semen into her; as more of the ethereal"
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
					+ "as you can feel the walls of her vagina squirm against you in a way "
					+ "no human could manage, but all you feel is some drowsiness";
		case 2:
			return "Clearly the succubus is trying to do something really special to you, "
					+ "as you can feel the walls of her vagina squirm against you in a way "
					+ "no human could manage, but all you feel is your focus waning some";
		case 0:
			return self.name()+" squeezes you with her pussy and starts to milk you, but you suddenly feel her shudder and moan loudly. Looks like her plan backfired.";
		case 6:
			return base
					+ "something snap loose inside of you and it seems to flow right "
					+ "through your dick and into her. When it is over you feel... empty "
					+ "somehow. At the same time, "+self.name()+" seems radiant, looking more powerful,"
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
}
