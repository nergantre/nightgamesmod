package skills;

import stance.Neutral;
import status.Braced;
import status.Stsflag;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Escape extends Skill {
	public Escape(Character self) {
		super("Escape", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		if (target.hasStatus(Stsflag.cockbound)) { return false; }
		return ((c.getStance().sub(getSelf())&&!c.getStance().mobile(getSelf()))||getSelf().bound())&&getSelf().canRespond();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(getSelf().bound()){
			if(getSelf().check(Attribute.Cunning, 5-getSelf().escape())){
				if(getSelf().human()){
					c.write(getSelf(),"You slip your hands out of your restraints.");
				} else if(target.human()) {
					c.write(getSelf(),getSelf().name()+" manages to free herself.");
				}
				getSelf().free();
			} else {
				if(getSelf().human()){
					c.write(getSelf(),"You try to slip your restraints, but can't get free.");
				}
				else if(target.human()) {
					c.write(getSelf(),getSelf().name()+" squirms against her restraints fruitlessly.");
				}
			}
		} else if(getSelf().check(Attribute.Cunning, 20+target.get(Attribute.Cunning) - (5*c.getStance().time+getSelf().escape()))) {
			if(getSelf().human()){
				if (getSelf().hasStatus(Stsflag.cockbound)) {
					c.write(getSelf(),"You some how managed to wiggle out of "+target.name()+"'s iron grip on your dick.");
					getSelf().removeStatus(Stsflag.cockbound);
					return;
				}
				c.write(getSelf(),"Your quick wits find a gap in "+target.name()+"'s hold and you slip away.");
			}
			else if(target.human()){
				if (getSelf().hasStatus(Stsflag.cockbound)) {
					c.write(getSelf(),"She some how managed to wiggle out ofyour iron grip on her dick.");
					getSelf().removeStatus(Stsflag.cockbound);
					return;
				}
				c.write(getSelf(),getSelf().name()+" goes limp and you take the opportunity to adjust your grip on her. As soon as you move, she bolts out of your weakened hold. " +
						"It was a trick!");
			}
			if (!getSelf().is(Stsflag.braced)) {
				getSelf().add(new Braced(getSelf()));
			}
			c.setStance(new Neutral(getSelf(),target));
		} else {
			if(getSelf().human()){
				if (getSelf().hasStatus(Stsflag.cockbound)) {
					c.write(getSelf(),"You try to escape "+target.name()+"'s iron grip on your dick. However, her pussy tongue has other ideas. She runs her tongue up and down your cock and leaves you gasping with pleasure.");
					int m = 8;
					getSelf().body.pleasure(target, target.body.getRandom("pussy"), getSelf().body.getRandom("cock"), m, c);					
				} else if (getSelf().nude()) {
					c.write(getSelf(),"You try to take advantage of an opening in "+target.name()+"'s stance to slip away, but she catches you by your protruding penis and reasserts her position.");
				}
				else{
					c.write(getSelf(),"You think you see an opening in "+target.name()+"'s stance, but she corrects it before you can take advantage.");
				}
			} else if(target.human()) {
				c.write(getSelf(),getSelf().name()+" manages to slip out of your grip for a moment, but you tickle her before she can get far and regain control.");
			}
		}
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Cunning)>=8;
	}

	@Override
	public Skill copy(Character user) {
		return new Escape(user);
	}
	public int speed(){
		return 1;
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character attacker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String describe() {
		return "Uses Cunning to try to escape a submissive position";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
