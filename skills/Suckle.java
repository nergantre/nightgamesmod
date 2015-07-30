package skills;

import stance.Stance;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

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
		int m = (getSelf().get(Attribute.Seduction) > 10 ? 5 : 0) + Global.random(6);
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
			getSelf().tempt(c, target, (3 + target.body.getRandomBreasts().size) * 2);
			target.buildMojo(c, 8);
		}
		return true;
	}

	@Override
	public boolean requirements(Character user) {
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
					"She drinks deeply of your milk, gurggling happily as more of the smooth liquid flows down her throat.";
		}
	}

	@Override
	public String describe() {
		return "Suck your opponent's nipples";
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
