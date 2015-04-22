package skills;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.body.Body;
import characters.body.BodyPart;

import combat.Combat;
import combat.Result;

public class Footjob extends Skill {

	public Footjob(Character self) {
		super("Footjob", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Seduction)>=22;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Seduction)>=22;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().feet(self)&&target.pantsless()&&(c.getStance().prone(self)!=c.getStance().prone(target))&&self.canAct()&&!c.getStance().penetration(target);
	}

	@Override
	public float priorityMod(Combat c) {
		BodyPart feet = self.body.getRandom("feet");
		Character other = c.p1 == self ? c.p2 : c.p1;
		BodyPart otherpart = other.hasDick() ? other.body.getRandomCock() : other.body.getRandomPussy();
		if (feet != null) {
			return (float) Math.max(0, (feet.getPleasure(otherpart) - 1));
		}
		return 0;
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy()+self.tohit())){
			int m = 8 + Global.random(6);
			if(self.human()){
				c.write(self,deal(c,m,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,m,Result.normal, target));
			}
			if (target.hasDick())
				target.body.pleasure(self, self.body.getRandom("feet"), target.body.getRandom("cock"), m, c);
			else
				target.body.pleasure(self, self.body.getRandom("feet"), target.body.getRandom("pussy"), m, c);
			if(c.getStance().dom(self)){
				self.buildMojo(c, 20);
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
		return new Footjob(user);
	}
	public int speed(){
		return 4;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You aim your foot between "+target.name()+"'s legs, but miss.";
		}
		else if(target.hasDick()){
			return "You press your foot against "+target.name()+"'s girl-cock and stimulate it by grinding it with the sole.";
		}
		else{
			return "You rub your foot against "+target.name()+"'s pussy lips, using her own wetness as lubricant, and stimulate her love button with your toe.";
		}
		
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character attacker) {
		if(modifier==Result.miss){
			return self.name()+" swings her foot at your groin, but misses.";
		}
		else{
			return self.name()+" rubs you dick with the sole of her soft foot. From time to time, she teases you by pinching the glans between her toes and jostling your balls.";
		}
	}

	@Override
	public String describe() {
		return "Pleasure your opponent with your feet";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
