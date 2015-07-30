package skills;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class MagicMissile extends Skill {

	public MagicMissile(Character self) {
		super("Magic Missile", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Arcane)>=1;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf());
	}

	@Override
	public int getMojoCost(Combat c) {
		return 5;
	}

	@Override
	public String describe() {
		return "Fires a small magic projectile: 5 Mojo";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy())){
			if(target.nude()&&Global.random(3)==2){
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.critical, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.critical, target));
				}
				if(target.has(Trait.achilles)){
					target.pain(c, Global.random(6));
				}
				target.pain(c, 9+Global.random(2*getSelf().get(Attribute.Arcane)+1));
				target.emote(Emotion.angry,10);
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.normal, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.normal, target));
				}
				target.pain(c, 6+Global.random(getSelf().get(Attribute.Arcane)+2));
				target.emote(Emotion.angry,5);
			}
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
		return new MagicMissile(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.damage;
	}

	public int accuracy(){
		return 7;
	}
	public int speed(){
		return 8;
	}
	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You fire a bolt of magical energy, but "+target.name()+" narrowly dodges out of the way.";
		}
		else if(modifier == Result.critical){
			if(target.hasBalls()){
				return "You cast and fire a magic missile at "+target.name()+". Just by luck, it hits her directly in the jewels. She cringes in pain, cradling her bruised parts.";
			}
			else{
				return "You cast and fire a magic missile at "+target.name()+". By chance, it flies under her guard and hits her solidly in the pussy. She doubles over " +
					"with a whimper, holding her bruised parts.";
			}
		}
		else{
			return "You hurl a magic missile at "+target.name()+", hitting and staggering her a step.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You see "+getSelf().name()+" start to cast a spell and you dive to the left, just in time to avoid the missile.";
		}
		else if(modifier == Result.critical){
			return getSelf().name()+" casts a quick spell and fires a bolt of magic into your vulnerable groin. You cradle your injured plums as pain saps the strength from your " +
					"legs.";
		}
		else{
			return getSelf().name()+"'s hand glows as she casts a spell. Before you can react, you're struck with an impact like a punch in the gut.";
		}
	}

}
