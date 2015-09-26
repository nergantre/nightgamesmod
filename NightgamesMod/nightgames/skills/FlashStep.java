package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Behind;

public class FlashStep extends Skill {

	public FlashStep(Character self) {
		super("Flash Step", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Ki)>=6;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf())&&!c.getStance().prone(target)&&!c.getStance().behind(getSelf())&&getSelf().canAct()&&!c.getStance().inserted();
	}
	@Override
	public int getMojoCost(Combat c) {
		return 15;
	}
	@Override
	public String describe(Combat c) {
		return "Use lightning speed to get behind your opponent before she can react: 10 stamina";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		c.setStance(new Behind(getSelf(),target));
		getSelf().weaken(c, 10);

		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new FlashStep(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You channel your ki into your feet and dash behind "+target.name()+" faster than her eyes can follow.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" starts to move and suddenly vanishes. You hesitate for a second and feel her grab you from behind.";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
