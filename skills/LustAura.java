package skills;

import global.Global;
import status.Horny;
import status.Stsflag;
import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class LustAura extends Skill {

	public LustAura(Character self) {
		super("Lust Aura", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Dark)>=3;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Dark)>=3;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canRespond()&&c.getStance().mobile(self)&&self.canSpend(10)&&!target.is(Stsflag.horny, self.nameOrPossessivePronoun() + " aura of lust");
	}

	@Override
	public String describe() {
		return "Inflicts arousal over time: 5 Arousal, 5 Mojo";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.spendMojo(10);
		self.arouse(10, c);
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		target.add(new Horny(target,3+2*self.getSkimpiness(),3+Global.random(3), self.nameOrPossessivePronoun() + " aura of lust"));
		target.emote(Emotion.horny, 10);
	}

	@Override
	public Skill copy(Character user) {
		return new LustAura(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You allow the corruption in your libido to spread out from your body. "+target.name()+" flushes with arousal and presses her thighs together as the aura taints her.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" releases an aura of pure sex. You feel your body becoming hot just being near her.";
	}

}
