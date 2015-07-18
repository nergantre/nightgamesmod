package skills;

import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Bravado extends Skill {

	public Bravado(Character self) {
		super("Determination", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.has(Trait.fearless);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canRespond()&&c.getStance().mobile(getSelf())&&getSelf().canSpend(20);
	}

	@Override
	public void resolve(Combat c, Character target) {
		int x = getSelf().getMojo().get();
		getSelf().spendMojo(c, x);
		if(getSelf().human()){
			c.write(getSelf(),deal(c,x,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,x,Result.normal, target));
		}
		getSelf().calm(c, x/2);
		getSelf().heal(c, x);
		getSelf().emote(Emotion.confident, 30);
		getSelf().emote(Emotion.dominant, 20);
		getSelf().emote(Emotion.nervous,-20);
		getSelf().emote(Emotion.desperate, -30);
	}

	@Override
	public Skill copy(Character user) {
		return new Bravado(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.recovery;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You grit your teeth and put all your willpower into the fight.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character attacker) {
		return getSelf().name()+" gives you a determined glare as she seems to gain a second wind.";
	}

	@Override
	public String describe() {
		return "Consume mojo to restore stamina and reduce arousal";
	}

}
