package nightgames.skills;

import java.util.List;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BreastsPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;

public class Paizuri extends Skill {

	public Paizuri(Character self) {
		super("Use Breasts", self);
	}

	static int MIN_REQUIRED_BREAST_SIZE = 3;

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().hasBreasts()
				&& getSelf().body.getLargestBreasts().size >= MIN_REQUIRED_BREAST_SIZE
				&& target.hasDick() && getSelf().topless()
				&& target.pantsless() && c.getStance().paizuri(getSelf())
				&& c.getStance().front(getSelf())
				&& getSelf().canAct() && !c.getStance().penetration(getSelf());
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 15;
	}
	@Override
	public boolean resolve(Combat c, Character target) {
		BreastsPart breasts = getSelf().body.getLargestBreasts();
		//try to find a set of breasts large enough, if none, default to largest.
		for (int i = 0 ; i < 3; i++) {
			BreastsPart otherbreasts = getSelf().body.getRandomBreasts();
			if (otherbreasts.size > MIN_REQUIRED_BREAST_SIZE) {
				breasts = otherbreasts;
				break;
			}
		}

		int m = (4 + Global.random(3));
		if (target.human()) {
			c.write(getSelf(), receive(0, Result.normal, target, breasts));
		}
		target.body.pleasure(getSelf(), getSelf().body.getRandom("breasts"), target.body.getRandom("cock"), m, c);					
		if (Global.random(100) < 2 + getSelf().get(Attribute.Fetish)) {
			target.add(c, new BodyFetish(target, getSelf(), BreastsPart.a.getType(), .25));
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction) >= 28 && user.hasBreasts();
	}

	@Override
	public Skill copy(Character user) {
		return new Paizuri(user);
	}

	public int speed() {
		return 4;
	}

	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}

	public String receive(int damage, Result modifier, Character target, BreastsPart breasts) {
		StringBuilder b = new StringBuilder();
		b.append(getSelf().name() + " squeezes your dick between her ");
		b.append(breasts.describe(getSelf()));
		b.append(". She rubs them up and down your shaft and teasingly licks your tip.");
		return b.toString();
	}

	@Override
	public String describe(Combat c) {
		return "Rub your opponent's dick between your boobs";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean makesContact() {
		return true;
	}

	public String getTargetOrganType(Combat c, Character target) {
		return "cock";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "breasts";
	}
}
