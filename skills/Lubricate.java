package skills;

import items.Item;
import global.Global;
import global.Modifier;
import status.Oiled;
import status.Stsflag;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Lubricate extends Skill {

	public Lubricate(Character self) {
		super("Lubricate", self);
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().mobile(self)&&self.canAct()&&self.has(Item.Lubricant)&&target.nude()&&!target.is(Stsflag.oiled)&&!c.getStance().prone(self)
				&&(!self.human()||Global.getMatch().condition!=Modifier.noitems);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		target.add(new Oiled(target));
		self.consume(Item.Lubricant, 1);
	}

	@Override
	public Skill copy(Character user) {
		return new Lubricate(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You cover "+target.name()+" with an oily Lubricant.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" throws an oily liquid at you. The liquid clings to you and makes your whole body slippery.";
	}

	@Override
	public String describe() {
		return "Oil up your opponent, making her easier to pleasure";
	}

}
