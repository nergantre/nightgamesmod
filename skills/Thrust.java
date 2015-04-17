package skills;

import stance.Position;
import stance.Stance;
import global.Global;
import combat.Combat;
import combat.Result;

import characters.Attribute;
import characters.Character;
import characters.Trait;
import characters.body.BodyPart;

public class Thrust extends Skill {
	public Thrust(String name, Character self) {
		super(name, self);
	}

	public Thrust(Character self) {
		super("Thrust", self);
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().canthrust(self)&&c.getStance().penetration(self);
	}

	public BodyPart getSelfOrgan(Combat c) {
		if (c.getStance().inserted(self)) {
			return self.body.getRandomInsertable();
		} else if (c.getStance().en == Stance.anal) {
			return self.body.getRandom("ass");
		} else {
			return self.body.getRandomPussy();
		}
	}

	public BodyPart getTargetOrgan(Combat c, Character target) {
		if (c.getStance().inserted(target)) {
			return target.body.getRandomInsertable();
		} else if (c.getStance().en == Stance.anal) {
			return target.body.getRandom("ass");
		} else {
			return target.body.getRandomPussy();
		}
	}

	public int[] getDamage(Character target, Position stance) {
		int results[] = new int[2];

		int m = 5 + Global.random(14);
		int mt;
		if(self.has(Trait.experienced)){
			mt = Math.max(1, m/4);
		} else {
			mt = Math.max(1, m/3);
		}

		results[0] = m;
		results[1] = mt;

		return results;
	}
	
	@Override
	public void resolve(Combat c, Character target) {
		BodyPart selfO = getSelfOrgan(c);
		BodyPart targetO = getTargetOrgan(c, target);
		Result result;
		
		if(c.getStance().en==Stance.anal){
			result = Result.anal;
		} else if (selfO.isType("pussy")) {
			result = Result.reverse;
		} else {
			result = Result.normal;
		}

		if(self.human()){
			c.write(self,deal(c,0,result, target));
		} else if(target.human()) {
			c.write(self,receive(c,0,result, target));
		}

		int[] m = getDamage(target, c.getStance());
		assert(m.length >= 2);

		if (m[0] != 0)
			target.body.pleasure(self, selfO, targetO, m[0], c);
		if (m[1] != 0)
			self.body.pleasure(target, targetO, selfO, m[1], c);
		if (getMojoSpent() > 0) {
			self.spendMojo(getMojoSpent());
		}
		if (getMojoBuilt() > 0) {
			self.buildMojo(getMojoBuilt());
		}
	}

	public int getMojoBuilt() {
		return 10;
	}
	
	public int getMojoSpent() {
		return 0;
	}


	@Override
	public Skill copy(Character user) {
		return new Thrust(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.anal){
			return "You thrust steadily into " + target.name() + "'s ass, eliciting soft groans of pleasure.";
		} else if (modifier == Result.reverse) {
			return Global.format("You rock your hips against {other:direct-object}, riding her smoothly. "
								+ "Despite the slow place, {other:subject} soon starts gasping and mewing with pleasure.", self, target);
		} else {
			return "You thrust into "+target.name()+" in a slow, steady rhythm. She lets out soft breathy moans in time with your lovemaking. You can't deny you're feeling " +
					"it too, but by controlling the pace, you can hopefully last longer than she can.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.anal){
			if(self.has(Trait.strapped)){
				return self.name()+" thrusts her hips, pumping her artificial cock in and out of your ass and pushing on your prostate.";
			}
			else{
				return self.name()+"'s cock slowly pumps the inside of your rectum.";
			}
		} else if (modifier == Result.reverse ){ 
			return self.name()+" rocks her hips against you, riding you smoothly and deliberately. Despite the slow pace, the sensation of her hot, wet pussy surrounding " +
					"your dick is gradually driving you to your limit.";
		} else {
			return Global.format("{self:subject} thrusts into {other:name-possessive} {other:body-part:pussy} in a slow steady rhythm, leaving you gasping.", self, target);
		}
	}

	@Override
	public String describe() {
		return "Slow fuck, minimizes own pleasure";
	}
	
	@Override
	public String getName(Combat c) {
		if (c.getStance().inserted(self)) {
			return "Thrust";
		} else {
			return "Ride";
		}
	}
}
