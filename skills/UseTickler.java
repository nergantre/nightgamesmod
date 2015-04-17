package skills;

import items.Item;
import status.Hypersensitive;
import global.Global;
import global.Modifier;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class UseTickler extends Skill {

	public UseTickler(Character self) {
		super(Item.Tickler.getName(), self);
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (self.has(Item.Tickler)||self.has(Item.Tickler2))&&self.canAct()&&(c.getStance().mobile(self)||c.getStance().dom(self))&&(c.getStance().reachTop(self)||c.getStance().reachBottom(self))
				&&(!self.human()||Global.getMatch().condition!=Modifier.notoys);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy()+self.tohit())){
		if(target.pantsless()&&c.getStance().reachBottom(self)&&!c.getStance().penetration(self)){
			if(self.has(Item.Tickler2)&&Global.random(2)==1&&self.canSpend(10)){
				if(self.human()){
					c.write(self,deal(c,0,Result.critical, target));
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.critical, target));
				}
				self.spendMojo(10);
				target.add(new Hypersensitive(target));
			}
			else{
				if(self.human()){
					c.write(self,deal(c,0,Result.strong, target));
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.strong, target));
				}
			}
			target.tempt(c, self, Global.random(target.get(Attribute.Perception)*3));
			target.weaken(c, 2+target.get(Attribute.Perception)+Global.random(6));
		}
		else if(target.topless()&&c.getStance().reachTop(self)){
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
			target.tempt(c, self, 4+Global.random(target.get(Attribute.Perception)*2));
			target.weaken(c, 2+target.get(Attribute.Perception));
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.weak, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.weak, target));
			}
			target.tempt(c, self, Global.random(target.get(Attribute.Perception)));
			target.weaken(c, target.get(Attribute.Perception));
		}
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
		return new UseTickler(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You brandish your feather tickler, but "+target.name()+" manages to avoid it.";
		}
		else if(modifier == Result.critical){
			return "You brush your tickler over "+target.name()+"'s body, causing her to shiver and retreat. When you tickle her again, she yelps and almost falls down. " +
					"It seems like your special feathers made her more sensitive than usual.";
		}
		else if(modifier == Result.strong){
			return "You run your tickler across "+target.name()+"'s sensitive thighs and pussy. She can't help but let out a quiet whimper of pleasure.";
		}
		else if(modifier == Result.weak){
			return "You catch "+target.name()+" off guard by tickling her neck and ears.";
		}
		else{
			return "You tease "+target.name()+"'s naked upper body with your feather tickler, paying close attention to her nipples.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return self.name()+" comes after you with her feather tickler, but you catch it and push it away.";
		}
		else if(modifier == Result.critical){
			return self.name()+" teases your dick and balls with her feather tickler. After she stops, you feel an unnatural sensitivity where the feathers touched you.";
		}
		else if(modifier == Result.strong){
			return self.name()+" brushes her tickler over your balls and teases the sensitive head of your penis.";
		}
		else if(modifier == Result.weak){
			return self.name()+" pulls out a feather tickler and teases any exposed skin she can reach.";
		}
		else{
			return self.name()+" runs her feather tickler across your nipples and abs.";
		}
	}

	@Override
	public String describe() {
		return "Use your tickler on opponet, weakening and arousing her. More effective if nude";
	}

}
