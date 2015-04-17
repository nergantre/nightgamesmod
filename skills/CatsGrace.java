package skills;

import status.Nimble;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class CatsGrace extends Skill {

	public CatsGrace(Character self) {
		super("Cat's Grace", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Animism)>=3;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Animism)>=3;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().mobile(self)&&self.getArousal().percent()>=20;
	}
	
	@Override
	public String describe() {
		return "Use your instinct to nimbly avoid attacks";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.add(new Nimble(self,4));
	}

	@Override
	public Skill copy(Character user) {
		return new CatsGrace(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You rely on your animal instincts to quicken your movements and avoid attacks.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" focuses for a moment and her movements start to speed up and become more animalistic.";
	}

}
