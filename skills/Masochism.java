package skills;

import status.Masochistic;
import status.Stsflag;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Masochism extends Skill {

	public Masochism(Character self) {
		super("Masochism", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Fetish)>=1;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().mobile(self)&&self.getArousal().get()>=15&&!self.is(Stsflag.masochism);
	}

	@Override
	public String describe() {
		return "You and your opponent become aroused by pain: Arousal at least 15";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.add(new Masochistic(self));
		target.add(new Masochistic(target));
	}

	@Override
	public Skill copy(Character user) {
		return new Masochism(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You fantasize about the pleasure that exquisite pain can bring. You share this pleasure with "+target.name()+".";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" shivers in arousal. You're suddenly bombarded with thoughts of letting her hurt you in wonderful ways.";
	}

}
