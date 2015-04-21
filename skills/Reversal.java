package skills;

import stance.Pin;
import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class Reversal extends Skill {

	public Reversal(Character self) {
		super("Reversal", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Cunning)>=24;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && !c.getStance().mobile(self)&&c.getStance().sub(self)&&self.canSpend(10)&&self.canAct();
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.spendMojo(10);
		if(target.roll(this, c, accuracy()+self.tohit())){
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
			
			c.setStance(new Pin(self,target));
			target.emote(Emotion.nervous, 10);
			self.emote(Emotion.dominant, 10);
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
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Cunning)>=24;
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
			return self.name()+" tries to reverse your hold, but you stop her.";
		}
		else{
			return self.name()+" rolls you over and ends up on top.";
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
