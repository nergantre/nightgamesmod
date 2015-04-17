package skills;

import status.Shamed;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Taunt extends Skill {

	public Taunt(Character self) {
		super("Taunt", self);
	}

	@Override
	public boolean requirements() {
		
		return self.getPure(Attribute.Cunning)>=8;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.nude()&&!c.getStance().sub(self)&&self.canSpend(5)&&self.canAct()&&!self.has(Trait.shy);
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.spendMojo(5);
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		int m = (6+Global.random(4)) * Math.min(2, (1 + self.getSkimpiness()));
		if(target.has(Trait.imagination)){
			m += 4;
			target.tempt(c, self, m);
			if(Global.random(4)>=1){
				target.add(new Shamed(target));
			}
		} else {
			target.tempt(c, self, m);
			if(Global.random(4)>=2){
				target.add(new Shamed(target));
			}
		}
		target.emote(Emotion.angry,30);
		target.emote(Emotion.nervous,15);
		self.emote(Emotion.dominant, 20);
		target.modMojo(-10);
		
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Cunning)>=8;
	}

	@Override
	public Skill copy(Character user) {
		return new Taunt(user);
	}
	public int speed(){
		return 9;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You tell "+target.name()+" that if she's so eager to be fucked senseless, you're available during off hours.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.taunt();
	}

	@Override
	public String describe() {
		return "Embarrass your opponent, may inflict Shamed";
	}
}
