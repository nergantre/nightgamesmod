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
	public boolean requirements() {
		return self.has(Trait.fearless);
	}

	@Override
	public boolean requirements(Character user) {
		return user.has(Trait.fearless);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canRespond()&&c.getStance().mobile(self)&&self.canSpend(20);
	}

	@Override
	public void resolve(Combat c, Character target) {
		int x = self.getMojo().get();
		self.spendMojo(c, x);
		if(self.human()){
			c.write(self,deal(c,x,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,x,Result.normal, target));
		}
		self.calm(c, x/2);
		self.heal(c, x);
		self.emote(Emotion.confident, 30);
		self.emote(Emotion.dominant, 20);
		self.emote(Emotion.nervous,-20);
		self.emote(Emotion.desperate, -30);
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
		return self.name()+" gives you a determined glare as she seems to gain a second wind.";
	}

	@Override
	public String describe() {
		return "Consume mojo to restore stamina and reduce arousal";
	}

}
