package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Alert;
import nightgames.status.FireStance;
import nightgames.status.StoneStance;
import nightgames.status.Stsflag;
import nightgames.status.WaterStance;

public class FireForm extends Skill{

	public FireForm(Character self) {
		super("Fire Form", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Ki)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&!c.getStance().sub(getSelf())&&!getSelf().is(Stsflag.form);
	}

	@Override
	public String describe() {
		return "Boost Mojo gain at the expense of Stamina regeneration.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		getSelf().add(c, new FireStance(getSelf()));
		return true;
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
		return getSelf().name()+" powers up and you can almost feel the energy radiating from her.";
	}

}
