package skills;

import global.Global;
import items.Item;
import status.Hypersensitive;
import status.Shamed;
import characters.Attribute;
import characters.Character;
import characters.body.BreastsPart;
import characters.body.CockPart;

import combat.Combat;
import combat.Result;

public class CockGrowth extends Skill {
	public CockGrowth(Character self) {
		super("Cock Growth", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Arcane)>=12;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf());
	}
	
	@Override
	public float priorityMod(Combat c) {
		return .5f;
	}

	@Override
	public String describe() {
		return "Grows or enlarges your opponent's cock.";
	}
	
	@Override
	public int getMojoCost(Combat c) {
		return 25;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		Result res = target.roll(this, c, 0) ? Result.normal : Result.miss;
		if (res == Result.normal && !target.hasDick()) {
			res = Result.special;
		}

		boolean permanent = Global.random(20) == 0 && (getSelf().human() || target.human());
		if(getSelf().human()){
			c.write(getSelf(),deal(c,permanent ? 1 : 0, res, target));
		} else if(target.human()) {		
			c.write(getSelf(),receive(c,permanent ? 1 : 0, res, target));
		}
		if ( res != Result.miss) {
			target.add(c, new Hypersensitive(target));
			CockPart part = target.body.getCockBelow(CockPart.massive.size);
			if (permanent) {
				if (part != null) {
					target.body.remove(part);
					target.body.add(part.upgrade());
				} else {
					target.body.add(CockPart.small);
				}
			} else {
				if (part != null) {
					target.body.temporaryAddOrReplacePartWithType(part.upgrade(), 10);
				} else {
					target.body.temporaryAddPart(CockPart.small, 10);
				}
			}
		}
		return res != Result.miss;
	}

	@Override
	public Skill copy(Character user) {
		return new CockGrowth(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		String message;
		if (modifier == Result.miss) {
			message = "You attempt to channel your arcane energies into "+target.name()+"'s crotch, but she dodges out of the way, causing your spell to fail.";			
		} else {
			if (modifier == Result.special) {
				message = "You channel your arcane energies into "+target.name()+"'s groin. A moment later, " + target.name() + " yelps as her clitoris rapidly enlarges into a small girl-cock!";
			} else {
				message = "You channel your arcane energies into "+target.name()+"'s dick. A moment later, " + target.name() + " yelps as her cock rapidly enlarges!";
			}
			if (damage > 0)
				message += " You realize the effects are permanent!";
		}
		return message;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		String message;
		if (modifier == Result.miss) {
			message = getSelf().name() + " stops moving and begins chanting. You start feeling some tingling in your groin, but it quickly subsides as you dodge out of the way.";
		} else {
			if (modifier == Result.special) {
				message = getSelf().name() + " stops moving and begins chanting. You feel your clit grow hot, and start expanding! "
						+ "You try to hold it back with your hands, but the growth continues until you're the proud owner of a new small girl-dick. "
						+ "The new sensations from your new maleness makes you tremble.";
			} else {
				message = getSelf().name() + " stops moving and begins chanting. You feel your cock grow hot, and start expanding! "
						+ "You try to hold it back with your hands, but the growth continues until it's much larger than before. "
						+ "The new sensations from your new larger cock makes you tremble.";
			}
			if (damage > 0)
				message += " You realize the effects are permanent!";
		}
		return message;
	}

}
