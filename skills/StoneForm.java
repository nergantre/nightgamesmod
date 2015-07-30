package skills;

import status.Alert;
import status.StoneStance;
import status.Stsflag;
import status.WaterStance;
import combat.Combat;
import combat.Result;

import characters.Attribute;
import characters.Character;

public class StoneForm extends Skill{

	public StoneForm(Character self) {
		super("Stone Form", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Ki)>=12;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&!c.getStance().sub(getSelf())&&!getSelf().is(Stsflag.form);
	}

	@Override
	public String describe() {
		return "Improves Pain Resistance rate at expense of Speed";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		getSelf().add(c, new StoneStance(getSelf()));
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new StoneForm(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You tense your body to absorb and shrug off attacks.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" braces herself to resist your attacks.";
	}

}
