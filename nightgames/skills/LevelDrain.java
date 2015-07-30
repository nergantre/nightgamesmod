package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Satiated;

public class LevelDrain extends Drain {

	public LevelDrain(Character self) {
		super("Level Drain", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Dark)>=30;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (this.getSelf().canAct()) && (c.getStance().canthrust(getSelf()))
				&& (c.getStance().penetration(this.getSelf()));
	}

	@Override
	public String describe() {
		return "Drain your opponent of their levels";
	}

	@Override
	public int getMojoCost(Combat c) {
		return 60;
	}

	private int stealXP(Character target) {
		int xpStolen = target.getXP();
		if (xpStolen <= 0) { return 0; }
		target.loseXP(xpStolen);
		getSelf().gainXP(xpStolen);
		return xpStolen;
	}

	@Override
	public float priorityMod(Combat c) {
		return 5.0f;
	}
	
	@Override
	public boolean resolve(Combat c, Character target) {
		int strength = Math.max(1, 1 + ((getSelf().get(Attribute.Dark)) / 30));
		
		int type = Global.centeredrandom(2, getSelf().get(Attribute.Dark) / 20.0f, 2);
		if (this.getSelf().human()) {
			c.write(getSelf(),deal(c, type, Result.normal, target));
		} else if (target.human()) {
			c.write(getSelf(),receive(c, type, Result.normal, target));
		}
		switch (type) {
		case 0:
			getSelf().arouse(getSelf().getArousal().max(), c);
			break;
		case 1:
			int stolen = stealXP(target);
			if (stolen > 0) {
				getSelf().add(c, new Satiated(target, stolen, 0));
				if (getSelf().human())
					c.write("You have absorbed " + stolen + " XP from " + target.name() + "!\n");
				else
					c.write(getSelf().name() + " has absorbed " + stolen + " XP from you!\n");
			}
			break;
		case 2:
			getSelf().add(c, new Satiated(target, 0, strength));
			int xpStolen = 0;
			for(int i = 0; i < strength; i++) {
				xpStolen += 95 + (5 * (target.getLevel()));
				c.write(target.dong());
			}
			if (getSelf().human())
				c.write("You have stolen " + strength + " of " + target.name() + "'s levels and absorbed it as " + xpStolen + " XP!\n");
			else
				c.write(getSelf().name() + " has stolen " + strength + " of your levels and absorbed it as " + xpStolen + " XP!\n");
			getSelf().gainXP(xpStolen);
			target.tempt(c, getSelf(), target.getArousal().max());
			break;
		default:
			break;
		}
		return type != 0;
	}

	@Override
	public Skill copy(Character target) {
		return new LevelDrain(target);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(getSelf().hasPussy()){
			String base = "You put your powerful vaginal muscles to work whilst"
					+ " transfixing " + target.name()
					+ "'s gaze with your own, goading his energy into his cock."
					+ " Soon it erupts from him, ";
			switch (damage) {
			case 0:
				return base + "but unfortunately you made a mistake, and the feedback leaves"
						+ " you on the edge of climax!";
			case 1:
				return base + "and you can feel his memories and experiences flow"
						+ " into you, adding to your skill.";
			case 2:
				return base
						+ "far more powerfully than you even thought possible."
						+ " You feel a fragment of his soul break away from him and"
						+ " spew into you, taking with it a portion of his very being"
						+ "and merging with your own. You have clearly"
						+ " won this fight, and a lot more than that.";
			default:
				// Should never happen
				return " but nothing happens, you feel strangely impotent.";
			}
		}
		else{
			String base = "With your cock deep inside "+target.name()+", you can feel the heat from her core. You draw the energy from her, mining her depths. ";
			switch (damage) {
			case 0:
				return "You attempt to drain "+target.name()+"'s energy through your intimate connection, but it goes wrong. You feel intense pleasure feeding " +
						"back into you and threatening to overwhelm you. You brink the spiritual link as fast as you can, but you're still left on the brink of " +
						"climax.";
			case 1:
				return "You attempt to drain "+target.name()+"'s energy through your intimate connection, taking a bit of her experience.";
			case 2:
				return base
						+ "You succeed in siphoning off a portion of her soul, stealing a portion of her very being. This energy permanently " +
						"settles within you!";
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
		case 0:
			return getSelf().name()+" squeezes you with her pussy and starts to milk you, but you suddenly feel her shudder and moan loudly. Looks like her plan backfired.";
		case 1:
			return base
					+ "your experiences and memories escape your mind and flowing into her.";
		case 2:
			return base
					+ "your very being snap loose inside of you and it seems to flow right "
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
		return "cock";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "pussy";
	}
}
