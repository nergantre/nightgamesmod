package skills;

import global.Global;
import stance.Position;
import stance.Stance;
import characters.Attribute;
import characters.Character;
import characters.Trait;
import characters.body.BodyPart;

import combat.Combat;
import combat.Result;

public class SpiralThrust extends Thrust {

	public SpiralThrust(Character self) {
		super("Spiral Thrust", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.has(Trait.spiral);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().canthrust(self)&&(c.getStance().penetration(self)||c.getStance().penetration(target))&&self.canSpend(10);
	}

	@Override
	public int[] getDamage(Character target, Position stance) {
		int[] result = new int[2];

		int x = self.getMojo().get();
		int mt = x / 2;
		if(self.has(Trait.experienced)){
			mt = mt * 3 / 4;
		}
		result[0] = x;
		result[1] = mt;

		return result;
	}

	@Override
	public int getMojoBuilt() {
		return 0;
	}

	@Override
	public int getMojoSpent() {
		return self.getMojo().get();
	}

	@Override
	public Skill copy(Character user) {
		return new SpiralThrust(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.anal){
			return "You unleash your strongest technique into "+target.name()+"'s ass, spiraling your hips and stretching her tight sphincter.";
		} else if (modifier == Result.reverse) {
			return Global.format("As you bounce on "+target.name()+"'s steaming pole, you feel a power welling up inside you. You put everything you have into moving your hips circularly, " +
					"rubbing every inch of her cock with your hot slippery " + getSelfOrgan(c).fullDescribe(self) +  ".", self, target);
		} else {
			return "As you thrust into "+target.name()+"'s hot pussy, you feel a power welling up inside you. You put everything you have into moving your hips circularly " +
					"while you continue to drill into her.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		BodyPart selfO = getSelfOrgan(c);
		if(modifier == Result.anal){
			return self.name()+" drills into your ass with extraordinary power. Your head seems to go blank and you fall face down to the ground as your arms turn to jelly and give out.";
		} else if (modifier != Result.reverse) {
			return Global.format("The movements of {self:name-possessive} cock suddenly change. She suddenly begins drilling to your poor pussy with an unprecedently passion. "
					+ "The only thing you can do is bite your lips and try to not instantly cum.", self, target);
		} else {
			return self.name()+" begins to move her hips wildly in circles, rubbing every inch of your cock with her hot, " + (selfO.isType("pussy") ? "slippery pussy walls" : " steaming asshole") + ", bringing you more pleasure " +
					"than you thought possible.";
		}
	}

	@Override
	public String describe() {
			return "Converts your mojo into fucking: All Mojo";
	}

	@Override
	public String getName(Combat c) {
		if (c.getStance().inserted(self)) {
			return "Spiral Thrust";
		} else {
			return "Spiral";
		}
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
