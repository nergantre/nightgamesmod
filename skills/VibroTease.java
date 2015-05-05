package skills;

import global.Global;
import items.Item;
import stance.Stance;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class VibroTease extends Skill {

	public VibroTease(Character self) {
		super("Vibro-Tease", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.has(Item.Strapon2);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().dom(self)&&(c.getStance().en==Stance.anal)&&self.has(Trait.strapped)&&c.getStance().inserted(self)&&self.has(Item.Strapon2);
	}

	@Override
	public String describe() {
		return "Turn up the strapon vibration";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else{
			if(target.human()){
				c.write(self,receive(c,0,Result.normal, target));
			}
		}
		int m = 10 + Global.random(5);
		target.body.pleasure(self, null, target.body.getRandom("ass"), m, c);
		self.arouse(2, c);
		self.buildMojo(c, 20);
	}

	@Override
	public Skill copy(Character user) {
		return new VibroTease(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" cranks up the vibration to maximum level which stirs up your insides. " +
				"She teasingly pokes the tip against your prostate which causes your limbs to get shaky from the pleasure.";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
