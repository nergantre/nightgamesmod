package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class Kick extends Skill {

	public Kick(Character self) {
		super("Kick", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Power)>=17;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().feet(getSelf())&&getSelf().canAct()&&(!c.getStance().prone(getSelf())||getSelf().has(Trait.dirtyfighter)&&!c.getStance().penetration(getSelf()));
	}
	
	@Override
	public int getMojoCost(Combat c) {
		return 5;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(!target.bottom.isEmpty()&&getSelf().get(Attribute.Ki)>=14&&Global.random(3)==2){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.special, target));
			}
			target.shred(1);
		}
		if(target.roll(this, c, accuracy())){
			int m = Global.random(12)+getSelf().get(Attribute.Power);
			if(getSelf().human()){
				if(c.getStance().prone(getSelf())){
					c.write(getSelf(),deal(c,m,Result.strong, target));
				}
				else{
					c.write(getSelf(),deal(c,m,Result.normal, target));
	
				}
			}
			else if(target.human()){
				if(c.getStance().prone(getSelf())){
					c.write(getSelf(),receive(c,m,Result.strong, target));
				}
				else{
					c.write(getSelf(),receive(c,m,Result.normal, target));
				}
			}
			if(target.has(Trait.achilles)){
				m+=4+Global.random(4);
			}
			if(target.has(Trait.armored)){
				m = m/2;
			}
			target.pain(c, m);
			target.emote(Emotion.angry,20);
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
		return new Kick(user);
	}
	public int speed(){
		return 8;
	}
	public Tactics type(Combat c) {
		return Tactics.damage;
	}
	public String getLabel(Combat c){
		if(getSelf().get(Attribute.Ki)>=14){
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
			return getSelf().name()+"'s kick hits nothing but air.";
		}
		if(modifier==Result.special){
			return getSelf().name()+" launches a powerful kick straight at your groin, but pulls it back just before impact. You feel a chill run down your spine and your testicles " +
					"are grateful for the last second reprieve. Your "+target.bottom.peek()+" crumble off your body, practically disintegrating.... Still somewhat grateful.";
		}
		if(modifier==Result.strong){
			return "With "+getSelf().name()+" flat on her back, you quickly move in to press your advantage. Faster than you can react, her foot shoots up between " +
					"your legs, dealing a critical hit on your unprotected balls.";
		}
		else{
			return getSelf().name()+"'s foot lashes out into your delicate testicles with devastating force. ";
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
