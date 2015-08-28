package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.IceStance;
import nightgames.status.Stsflag;
import nightgames.status.WaterStance;

public class IceForm extends Skill {

	public IceForm(Character self) {
		super("Ice Form", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Ki)>=12;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&!c.getStance().sub(getSelf())&&!getSelf().is(Stsflag.form);
	}

	@Override
	public String describe() {
		return "Improves resistance to pleasure, reduces mojo gain to zero.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		getSelf().add(c, new IceStance(getSelf()));
		return true;
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
		return getSelf().name()+" takes a deep breath and her expression turns so frosty that you're not sure you can ever thaw her out.";
	}

}
