package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.stance.Stance;

public class Nurple extends Skill {

	public Nurple(Character self) {
		super("Twist Nipples", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Power)>=13;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.breastsAvailable()&&c.getStance().reachTop(getSelf())&&getSelf().canAct();
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 10;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy(c))){
			if(getSelf().has(Item.ShockGlove)&&getSelf().has(Item.Battery,2)){
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.special, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.special, target));
				}
				target.pain(c, Global.random(9)+target.get(Attribute.Perception));
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.normal, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.normal, target));
				}
				target.pain(c, Global.random(9)+target.get(Attribute.Perception)/2);
			}
			target.loseMojo(c, 5);
			target.emote(Emotion.angry,15);
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Nurple(user);
	}
	public int speed(){
		return 7;
	}
	public Tactics type(Combat c) {
		return Tactics.damage;
	}
	@Override
	public String getLabel(Combat c){
		if(getSelf().has(Item.ShockGlove)){
			return "Shock breasts";
		}
		else{
			return getName(c);
		}
	}
	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You grope at "+target.name()+"'s breasts, but miss.";
		}
		else if(modifier == Result.special){
			return "You grab "+target.name()+"'s boob with your shock-gloved hand, painfully shocking her.";
		}
		else{
			return "You pinch and twist "+target.name()+"'s nipples, causing her to yelp in surprise.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return getSelf().name()+" tries to grab your nipples, but misses.";
		}
		else if(modifier == Result.special){
			return getSelf().name()+" touches your nipple with her glove and a jolt of electricity hits you.";
		}
		else{
			return getSelf().name()+" twists your sensitive nipples, giving you a jolt of pain.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Twist opponent's nipples painfully";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
