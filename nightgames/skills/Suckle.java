package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;

public class Suckle extends Skill {
	public Suckle(Character self) {
		super("Suckle", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.topless()&&c.getStance().reachTop(getSelf())&&c.getStance().front(getSelf())&&(getSelf().canAct()||(c.getStance().enumerate()==Stance.nursing&&getSelf().canRespond()))&&c.getStance().facing();
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		Result results = target.has(Trait.lactating) ? Result.special : Result.normal;
		int m = (getSelf().get(Attribute.Seduction) > 10 ? 8 : 4) + Global.random(6);
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,results, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,results, target));
		}
		if (getSelf().has(Trait.silvertongue)) {
			m += 4;
		}
		target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandom("breasts"), m, c);
		if (results == Result.special) {
			getSelf().tempt(c, target, target.body.getRandomBreasts(), (3 + target.body.getRandomBreasts().size) * 2);
			target.buildMojo(c, 10);
		} else {
			target.buildMojo(c, 5);
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Suckle(user);
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.normal){
			return "You slowly circle your tongue around each of "+target.name()+"'s nipples, and start sucking like a newborn.";
		} else {
			return "You slowly circle your tongue around each of "+target.name()+"'s nipples, and start sucking like a newborn. " +
					"Her milk slides smoothly down your throat, and you're left with a warm comfortable feeling.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.normal){
			return getSelf().name()+" licks and sucks your nipples, sending a surge of excitement straight to your groin.";
		} else {
			return getSelf().name()+" licks and sucks your nipples, drawing forth a gust of breast milk from your teats. " +
					"She drinks deeply of your milk, gurgling happily as more of the smooth liquid flows down her throat.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Suck your opponent's nipples. Builds mojo for the opponent.";
	}
	@Override
	public boolean makesContact() {
		return true;
	}

	public String getTargetOrganType(Combat c, Character target) {
		return "breasts";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "mouth";
	}
}
