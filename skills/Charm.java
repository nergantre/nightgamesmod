package skills;

import status.Charmed;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Charm extends Skill {
	public Charm(Character self) {
		super("Charm", self, 4);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canSpend(20)&&getSelf().canRespond()&&c.getStance().facing()&&!target.wary();
	}

	@Override
	public void resolve(Combat c, Character target) {
		getSelf().spendMojo(c, 20);
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		int m = (int) (Math.round(2+Global.random(4) + getSelf().body.getCharismaBonus(target)));
		if(target.has(Trait.imagination)){
			m += 4;
			target.tempt(c, getSelf(), m);
			if(Global.random(4)>=1){
				c.write(target.subjectAction("were", "was") + " charmed.");
				target.add(new Charmed(target));
			}
		} else {
			target.tempt(c, getSelf(), m);
			if(Global.random(4)>=2){
				c.write(target.subjectAction("were", "was") + " charmed.");
				target.add(new Charmed(target));
			}
		}
		target.emote(Emotion.horny,10);
		getSelf().emote(Emotion.confident, 20);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Cunning)>=8 && user.get(Attribute.Seduction) > 16;
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
		return getSelf().getName() + " flashes a dazzling smile at you.";
	}

	@Override
	public String describe() {
		return "Charms your opponent into not hurting you.";
	}
}
