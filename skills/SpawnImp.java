package skills;

import pet.ImpFem;
import pet.ImpMale;
import pet.Ptype;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class SpawnImp extends Skill {
	private Ptype gender;
	
	public SpawnImp(Character self,Ptype gender) {
		super("Summon Imp", self);
		this.gender=gender;
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Dark)>=6;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf())&&getSelf().pet==null&&getSelf().canSpend(10);
	}

	@Override
	public String describe() {
		return "Summon a demonic Imp: 5 arousal, 10 mojo";
	}

	@Override
	public void resolve(Combat c, Character target) {
		getSelf().arouse(5, c);
		getSelf().spendMojo(c, 10);
		int power = 3;
		int ac = 2;
		if(getSelf().has(Trait.leadership)){
			power++;
		}
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
			if(gender==Ptype.impfem){
				getSelf().pet=new ImpFem(getSelf(),power,ac);
			}
			else{
				getSelf().pet=new ImpMale(getSelf(),power,ac);
			}
		}
		else{
			if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			getSelf().pet=new ImpFem(getSelf(),power,ac);
		}
	}

	@Override
	public Skill copy(Character user) {
		return new SpawnImp(user,gender);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.summoning;
	}
	public String getLabel(Combat c){
		if(gender==Ptype.impfem){
			return "Imp (female)";
		}
		else{
			return "Imp (male)";
		}
	}
	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(gender==Ptype.impfem){
			return "You focus your dark energy and summon a minion to fight for you. A naked, waist high, female imp steps out of a small burst of flame. She stirs up her honey " +
					"pot and despite yourself, you're slightly affected by the pheromones she's releasing.";
		}
		else{
			return "You focus your dark energy and summon a minion to fight for you. A brief burst of flame reveals a naked imp. He looks at "+target.name()+" with hungry eyes " +
					"and a constant stream of pre-cum leaks from his large, obscene cock.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" spreads out her dark aura and a demonic imp appears next to her in a burst of flame. The imp stands about waist height, with bright red hair, " +
				"silver skin and a long flexible tail. It's naked, clearly female, and surprisingly attractive given its inhuman features.";
	}

}
