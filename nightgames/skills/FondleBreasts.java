package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;

public class FondleBreasts extends Skill {

	public FondleBreasts(Character self) {
		super("Fondle Breasts", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().reachTop(getSelf())&&target.hasBreasts()&&getSelf().canAct();
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 7;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int m = 1 + Global.random(4);
		if(target.roll(this, c, accuracy(c))){
			if(target.breastsAvailable()){
				m += 4;
				if(getSelf().human()){
					c.write(getSelf(),deal(c,m,Result.normal, target));
				} else {
					c.write(getSelf(),receive(c,m,Result.normal, target));
				}
				target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("breasts"), m, c);					
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),deal(c,m,Result.normal, target));
				} else {
					c.write(getSelf(),receive(c,m,Result.normal, target));
				}
				target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("breasts"), m, c);					
			}
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new FondleBreasts(user);
	}
	public int speed(){
		return 6;
	}
	public int accuracy(Combat c){
		return 95;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You grope at "+target.name()+"'s breasts, but miss.";
		}
		else if(target.breastsAvailable()){
			return "You massage "+target.name()+"'s soft breasts and pinch her nipples, causing her to moan with desire.";
		}
		else{
			return "You massage "+target.name()+"'s breasts over her "+target.getOutfit().getTopOfSlot(ClothingSlot.top).getName()+".";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return getSelf().name() + " gropes at your " + target.body.getRandomBreasts().describe(target) + ", but misses the mark.";
		}
		else if(target.breastsAvailable()){
			return getSelf().name() + " massages your " + target.body.getRandomBreasts().describe(target) + ", and pinch your nipples, causing you to moan with desire.";
		}
		else{
			return getSelf().name() + " massages your " + target.body.getRandomBreasts().describe(target) + " over your "+target.getOutfit().getTopOfSlot(ClothingSlot.top).getName()+".";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Grope your opponents breasts. More effective if she's topless";
	}

	@Override
	public String getLabel(Combat c){
		return c.getOther(getSelf()).body.getBreastsAbove(BreastsPart.flat.size) != null ? "Fondle Breasts" : "Tease Chest";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		return "breasts";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "hands";
	}
}
