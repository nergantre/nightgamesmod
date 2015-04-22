package skills;

import status.Alluring;
import status.Distorted;
import status.Nimble;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Illusions extends Skill {

	public Illusions(Character self) {
		super("Illusions", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Arcane)>=12;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Arcane)>=12;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().mobile(self)&&!c.getStance().prone(self)&&self.canSpend(10);
	}

	@Override
	public String describe() {
		return "Create illusions to act as cover: 10 Mojo";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.spendMojo(c, 10);
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.add(new Distorted(self));
		self.add(new Alluring(self, 5));
	}

	@Override
	public Skill copy(Character user) {
		return new Illusions(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You cast an illusion spell to create seveal images of yourself. At the same time, you add a charm to make yourself irresistable.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" casts a brief spell and your vision is filled with naked copies of her. You can still tell which "+self.name()+" is real, but it's still a distraction. At the same time, she suddnely looks irresistable.";
	}
}
