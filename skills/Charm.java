package skills;

import status.Charmed;
import status.Shamed;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Charm extends Skill {

	public Charm(Character self) {
		super("Charm", self);
	}

	@Override
	public boolean requirements() {
		return requirements(self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !c.getStance().sub(self)&&self.canSpend(20)&&self.canRespond()&&c.getStance().behind(target);
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.spendMojo(20);
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		int m = (int) (Math.round(2+Global.random(4) + self.body.getCharismaBonus(target)));
		if(target.has(Trait.imagination)){
			m += 4;
			target.tempt(c, self, m);
			if(Global.random(4)>=1){
				c.write(target.subjectAction("were", "was") + " charmed.");
				target.add(new Charmed(target));
			}
		} else {
			target.tempt(c, self, m);
			if(Global.random(4)>=2){
				c.write(target.subjectAction("were", "was") + " charmed.");
				target.add(new Charmed(target));
			}
		}
		target.emote(Emotion.horny,10);
		self.emote(Emotion.confident, 20);
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Cunning)>=8 && user.getPure(Attribute.Seduction) > 16;
	}

	@Override
	public Skill copy(Character user) {
		return new Charm(user);
	}
	public int speed(){
		return 9;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You flash a dazzling smile at " + target.getName() + ".";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return target.getName() + "flashes a dazzling smile at you.";
	}

	@Override
	public String describe() {
		return "Embarrass your opponent, may inflict Shamed";
	}
}
