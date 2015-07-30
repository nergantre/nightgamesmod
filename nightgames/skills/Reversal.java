package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Pin;

public class Reversal extends Skill {

	public Reversal(Character self) {
		super("Reversal", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && !c.getStance().mobile(getSelf())&&c.getStance().sub(getSelf())&&getSelf().canAct();
	}

	@Override
	public int getMojoCost(Combat c) {
		return 20;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy())){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			
			c.setStance(new Pin(getSelf(),target));
			target.emote(Emotion.nervous, 10);
			getSelf().emote(Emotion.dominant, 10);
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
	public boolean requirements(Character user) {
		return user.get(Attribute.Cunning)>=24;
	}

	@Override
	public Skill copy(Character user) {
		return new Reversal(user);
	}
	public int speed(){
		return 4;
	}
	public int accuracy(){
		return 4;
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You try to get on top of "+target.name()+", but she's apparently more ready for it than you realized.";
		}
		else{
			return "You take advantage of "+target.name()+"'s distraction and put her in a pin.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return getSelf().name()+" tries to reverse your hold, but you stop her.";
		}
		else{
			return getSelf().name()+" rolls you over and ends up on top.";
		}
	}

	@Override
	public String describe() {
		return "Take dominant position: 10 Mojo";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
