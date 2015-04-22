package skills;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Kick extends Skill {

	public Kick(Character self) {
		super("Kick", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Power)>=17;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Power)>=17;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().feet(self)&&self.canAct()&&(!c.getStance().prone(self)||self.has(Trait.dirtyfighter)&&!c.getStance().penetration(self));
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(!target.bottom.isEmpty()&&self.getPure(Attribute.Ki)>=14&&Global.random(3)==2){
			if(self.human()){
				c.write(self,deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.special, target));
			}
			target.shred(1);
		}
		if(target.roll(this, c, accuracy()+self.tohit())){
			int m = Global.random(12)+self.get(Attribute.Power);
			if(self.human()){
				if(c.getStance().prone(self)){
					c.write(self,deal(c,m,Result.strong, target));
				}
				else{
					c.write(self,deal(c,m,Result.normal, target));
	
				}
			}
			else if(target.human()){
				if(c.getStance().prone(self)){
					c.write(self,receive(c,m,Result.strong, target));
				}
				else{
					c.write(self,receive(c,m,Result.normal, target));
				}
			}
			if(target.has(Trait.achilles)){
				m+=4+Global.random(4);
			}
			if(target.has(Trait.armored)){
				m = m/2;
			}
			target.pain(c, m);
			if(self.has(Trait.wrassler)){
				target.calm(c, Global.random(5));
			}
			else{
				target.calm(c, Global.random(8));
			}
			self.buildMojo(c, 10);
			target.emote(Emotion.angry,20);
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
		return new Kick(user);
	}
	public int speed(){
		return 8;
	}
	public Tactics type(Combat c) {
		return Tactics.damage;
	}
	public String toString(){
		if(self.getPure(Attribute.Ki)>=14){
			return "Shatter Kick";
		}
		else{
			return "Kick";
		}
	}
	
	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "Your kick hits nothing but air.";
		}
		if(modifier==Result.special){
			return "You focus your ki into a single kick, targeting not "+target.name()+"'s body, but her "+target.bottom.peek()+". The garment is completely destroyed, but " +
					"she is safely left completely unharmed. Wait, you are actually fighting right now, aren't you?";
		}
		if(modifier==Result.strong){
			return "Lying on the floor, you feign exhaustion, hoping "+target.name()+" will lower her guard. As she approaches unwarily, you suddenly kick up between " +
					"her legs, delivering a painful hit to her sensitive vulva.";
		}
		else{
			return "You deliver a swift kick between "+target.name()+"'s legs, hitting her squarely on the baby maker.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return self.name()+"'s kick hits nothing but air.";
		}
		if(modifier==Result.special){
			return self.name()+" launches a powerful kick straight at your groin, but pulls it back just before impact. You feel a chill run down your spine and your testicles " +
					"are grateful for the last second reprieve. Your "+target.bottom.peek()+" crumble off your body, practically disintegrating.... Still somewhat grateful.";
		}
		if(modifier==Result.strong){
			return "With "+self.name()+" flat on her back, you quickly move in to press your advantage. Faster than you can react, her foot shoots up between " +
					"your legs, dealing a critical hit on your unprotected balls.";
		}
		else{
			return self.name()+"'s foot lashes out into your delicate testicles with devastating force. ";
		}
	}

	@Override
	public String describe() {
		return "Kick your opponent in the groin";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
