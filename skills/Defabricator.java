package skills;

import items.Item;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Defabricator extends Skill {

	public Defabricator(Character self) {
		super("Defabricator", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Science)>=18;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Science)>=18;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().mobile(self)&&!c.getStance().prone(self)&&!target.nude()&&self.has(Item.Battery,8);
	}

	@Override
	public String describe() {
		return "Does what it says on the tin.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		target.nudify();
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
			c.write(target,target.nakedLiner());
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		target.nudify();
	}

	@Override
	public Skill copy(Character user) {
		return new Defabricator(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You charge up your Defabricator and point it in "+target.name()+"'s general direction. A bright light engulfs her and her clothes are disintegrated in moment.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" points a device at you and light shines from it like it's a simple flashlight. The device's function is immediately revealed as your clothes just vanish " +
				"in the light. You're left naked in seconds.";
	}

}
