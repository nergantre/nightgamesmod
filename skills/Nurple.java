package skills;

import items.Item;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class Nurple extends Skill {

	public Nurple(Character self) {
		super("Twist Nipples", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Power)>=13;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Power)>=13;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.topless()&&c.getStance().reachTop(self)&&self.canAct();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy()+self.tohit())){
			if(self.has(Item.ShockGlove)&&self.has(Item.Battery,2)){
				if(self.human()){
					c.write(self,deal(c,0,Result.special, target));
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.special, target));
				}
				target.pain(c, Global.random(9)+target.get(Attribute.Perception));
			}
			else{
				if(self.human()){
					c.write(self,deal(c,0,Result.normal, target));
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.normal, target));
				}
				target.pain(c, Global.random(9)+target.get(Attribute.Perception)/2);
			}
			target.calm(c, Global.random(5));
			self.buildMojo(c, 10);
			target.emote(Emotion.angry,15);
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.miss, target));
			}
		}
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
	public String toString(){
		if(self.has(Item.ShockGlove)){
			return "Shock breasts";
		}
		else{
			return name;
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
			return self.name()+" tries to grab your nipples, but misses.";
		}
		else if(modifier == Result.special){
			return self.name()+" touches your nipple with her glove and a jolt of electricity hits you.";
		}
		else{
			return self.name()+" twists your sensitive nipples, giving you a jolt of pain.";
		}
	}

	@Override
	public String describe() {
		return "Twist opponent's nipples painfully";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
