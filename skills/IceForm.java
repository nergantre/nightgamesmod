package skills;

import status.IceStance;
import status.Stsflag;
import status.WaterStance;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class IceForm extends Skill {

	public IceForm(Character self) {
		super("Ice Form", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Ki)>=12;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Ki)>=12;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&!c.getStance().sub(self)&&!self.is(Stsflag.form);
	}

	@Override
	public String describe() {
		return "Improves resistance to pleasure, reduces mojo gain to zero.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.add(new IceStance(self));
	}

	@Override
	public Skill copy(Character user) {
		return new IceForm(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You visualize yourself at the center of a raging snow storm. You can already feel yourself start to go numb.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" takes a deep breath and her expression turns so frosty that you're not sure you can ever thaw her out.";
	}

}
