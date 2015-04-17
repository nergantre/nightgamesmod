package skills;

import status.Alert;
import status.FireStance;
import status.StoneStance;
import status.Stsflag;
import status.WaterStance;
import combat.Combat;
import combat.Result;

import characters.Attribute;
import characters.Character;

public class FireForm extends Skill{

	public FireForm(Character self) {
		super("Fire Form", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Ki)>=15;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Ki)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&!c.getStance().sub(self)&&!self.is(Stsflag.form);
	}

	@Override
	public String describe() {
		return "Boost Mojo gain at the expense of Stamina regeneration.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.add(new FireStance(self));
	}

	@Override
	public Skill copy(Character user) {
		return new FireForm(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You let your ki burn, wearing down your body, but enhancing your spirit.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" powers up and you can almost feel the energy radiating from her.";
	}

}
