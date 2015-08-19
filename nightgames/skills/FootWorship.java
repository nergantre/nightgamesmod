package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.AssPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;
import nightgames.stance.StandingOver;
import nightgames.status.BodyFetish;

public class FootWorship extends Skill {
	public FootWorship(Character self) {
		super("Foot Worship", self);
	}

	@Override
	public boolean requirements(Character user) {
		Optional<BodyFetish> fetish = getSelf().body.getFetish("feet");
		return fetish.isPresent() && fetish.get().magnitude >= .5;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.body.has("feet")&&c.getStance().reachBottom(getSelf())&&getSelf().canAct()&&!c.getStance().behind(getSelf());
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		Result result = Result.normal;
		int m = 0; int n = 0;
		m = 8 + Global.random(6);
		n = 20;
		if (m > 0) {
			target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandom("feet"), m, c);
		}
		if (n > 0) {
			target.buildMojo(c, n);
		}
		if (c.getStance().en == Stance.neutral) {
			c.setStance(new StandingOver(target, getSelf()));
		}
		return result != Result.miss;
	}

	@Override
	public Skill copy(Character user) {
		return new FootWorship(user);
	}
	public int speed(){
		return 2;
	}
	public int accuracy(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You throw yourself at " + target.nameOrPossessivePronoun()
			+ " dainty feet and start sucking on her toes. " + target.subject() + " seems surprised at first, "
					+ "but then grins and rubs her soles against your face, eliciting a moan from you.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name() + " throws herself at your feet. She worshipfully grasps your feet "
				+ "and start licking between your toes, all while her face displays a mask of ecstasy.";
	}

	@Override
	public String describe() {
		return "Worship opponent's feet: builds mojo for opponent";
	}
	public String getTargetOrganType(Combat c, Character target) {
		return "feet";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "mouth";
	}
}
