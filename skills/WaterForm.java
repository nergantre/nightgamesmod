package skills;

import status.Alert;
import status.Stsflag;
import status.WaterStance;
import combat.Combat;
import combat.Result;

import characters.Attribute;
import characters.Character;

public class WaterForm extends Skill{

	public WaterForm(Character self) {
		super("Water Form", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Ki)>=3;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Ki)>=3;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&!c.getStance().sub(self)&&!self.is(Stsflag.form);
	}

	@Override
	public String describe() {
		return "Improves evasion and counterattack rate at expense of Power";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.add(new WaterStance(self));
	}

	@Override
	public Skill copy(Character user) {
		return new WaterForm(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You relax your muscles, prepared to flow with and counter "+target.name()+"'s attacks.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" takes a deep breath and her movements become much more fluid.";
	}

}
