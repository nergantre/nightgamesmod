package skills;

import characters.Attribute;
import characters.Character;
import characters.Trait;
import characters.body.BodyPart;
import combat.Combat;
import combat.Result;
import stance.Stance;
import status.CockBound;
import status.Stsflag;

public class PullOut extends Skill {

	public PullOut(Character self) {
		super("Pull Out", self);
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&(c.getStance().en == Stance.facesitting || c.getStance().penetration(getSelf()))&&c.getStance().dom(getSelf());
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(c.getStance().en ==Stance.anal){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.anal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.anal, target));
			}
		}
		else{
			if (getSelf().hasStatus(Stsflag.leglocked) || getSelf().hasStatus(Stsflag.armlocked) || (target.has(Trait.tight) && c.getStance().inserted(getSelf()))) {
				boolean escaped = getSelf().check(Attribute.Power, getSelf().escape() + target.get(Attribute.Power));
				if (escaped) {
					if(getSelf().human()) {
						c.write(getSelf(),deal(c,0,Result.normal, target));
					} else if(target.human()) {
						c.write(getSelf(),receive(c,0,Result.normal, target));
					}
				} else {
					if (getSelf().hasStatus(Stsflag.leglocked)) {
						BodyPart part = c.getStance().analinserted() ? target.body.getRandom("ass") : target.body.getRandomPussy();
						String partString = part.describe(target);
						if (getSelf().human())
							c.write(getSelf(),"You try to pull out of "+target.name()+"'s " + partString + ", but her legs immediately tighten against your waist, holding you inside her. The mere friction from her action sends a shiver down your spine.");
						else
							c.write(getSelf(),"She tries to pull out of "+target.nameOrPossessivePronoun()+" " + partString + ", but your legs immediately pull her back in, holding you inside her.");
					} else if (getSelf().hasStatus(Stsflag.armlocked)) {
						if (getSelf().human())
							c.write(getSelf(),"You try to pull yourself off of "+target.name() + ", but she merely pulls you back on top of her, surrounding you in her embrace.");
						else
							c.write(getSelf(),"She tries to pull herself off of "+target.name()+", but with a gentle pull of your hands, she collapses back on top of you.");
					} else if (target.has(Trait.tight) && c.getStance().inserted(getSelf())) {
						BodyPart part = c.getStance().analinserted() ? target.body.getRandom("ass") : target.body.getRandomPussy();
						String partString = part.describe(target);
						if (getSelf().human())
							c.write(getSelf(),"You try to pull yourself out of "+target.name()+"'s " +partString + ", but she clamps down hard on your cock while smiling at you. You almost cum from the sensation, and quickly abandon ideas about your escape.");
						else
							c.write(getSelf(),"She tries to pull herself out of "+target.nameOrPossessivePronoun()+" " + partString + ", but you clamp down hard on her cock, and prevent her from pulling out.");
					}
					int m = 8;
					if (c.getStance().inserted(getSelf())) {
						BodyPart part = c.getStance().analinserted() ? target.body.getRandom("ass") : target.body.getRandomPussy();
						String partString = part.describe(target);
						getSelf().body.pleasure(target, part, getSelf().body.getRandomInsertable(), m, c);
					}
					return false;
				}
			} else if (getSelf().hasStatus(Stsflag.cockbound)) {
				CockBound s = (CockBound)getSelf().getStatus(Stsflag.cockbound);
				c.write(getSelf(),"You try to pull out of "+target.name()+"'s " + target.body.getRandomPussy() + ", but her " + s.binding +" instantly react and pulls your dick back in.");
				int m = 8;
				getSelf().body.pleasure(target, target.body.getRandom("pussy"), getSelf().body.getRandom("cock"), m, c);					
				return false;
			} else if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			} else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
		}
		c.setStance(c.getStance().insert());
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new PullOut(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.anal){
			return "You pull your dick completely out of "+target.name()+"'s ass.";
		}
		return "You pull completely out of "+target.name()+"'s pussy, causing her to let out a disappointed little whimper.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.anal){
			return "You feel the pressure in your anus recede as "+getSelf().name()+" pulls out.";
		}
		else{
			return getSelf().name()+" lifts her hips more than normal, letting your dick slip completely out of her.";
		}
	}

	@Override
	public String describe() {
		return "Aborts penetration";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
