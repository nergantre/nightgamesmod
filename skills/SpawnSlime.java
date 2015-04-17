package skills;

import items.Item;
import pet.Slime;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class SpawnSlime extends Skill {

	public SpawnSlime(Character self) {
		super("Create Slime", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Science)>=3;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Science)>=3;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().mobile(self)&&!c.getStance().prone(self)&&self.pet==null&&self.has(Item.Battery)&&self.canSpend(5);
	}

	@Override
	public String describe() {
		return "Creates a mindless, but living slime to attack your opponent: 1 Battery";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.spendMojo(5);
		self.consume(Item.Battery, 1);
		int power = 3;
		int ac = 3;
		if(self.has(Trait.leadership)){
			power++;
		}
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.pet=new Slime(self,power,ac);
	}

	@Override
	public Skill copy(Character user) {
		return new SpawnSlime(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.summoning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You dispense blue slime on the floor and send a charge through it to animate it. The slime itself is not technically alive, but an extension of a larger " +
				"creature kept in Jett's lab.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" points a device at the floor and releases a blob of blue slime. The blob starts to move like a living thing and briefly takes on a vaguely humanoid shape " +
				"and smiles at you.";
	}

}
