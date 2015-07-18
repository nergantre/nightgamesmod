package skills;

import stance.Stance;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Finger extends Skill {

	public Finger(Character self) {
		super("Finger", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().reachBottom(getSelf())&&(target.pantsless()||(getSelf().has(Trait.dexterous)&&target.bottom.size()<=1))&&target.hasPussy()&&getSelf().canAct()&&(!c.getStance().penetration(target)||c.getStance().en==Stance.anal);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy())){
			int m = 4 + Global.random(4);
			if(getSelf().get(Attribute.Seduction)>=8){
				m += 3;
				if(getSelf().human()){
					c.write(getSelf(),deal(c,m,Result.normal, target));
				} else {
					c.write(getSelf(),receive(c,0,Result.normal, target));
				}
				target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("pussy"), m, c);					
				getSelf().buildMojo(c, 10);
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),deal(c,m,Result.weak, target));
				}else {
					c.write(getSelf(),receive(c,0,Result.weak, target));
				}
				target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("pussy"), m, c);					
			}
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
		}
	}

	public int accuracy(){
		return 7;
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Finger(user);
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You grope at "+target.name()+"'s pussy, but miss.";
		}
		if(modifier == Result.weak){
			return "You grope between "+target.name()+"'s legs, not really knowing what you're doing. You don't know where she's the most sensitive, so you rub and " +
					"stroke every bit of moist flesh under your fingers.";
		}
		else{
			if(target.getArousal().get()<=15){
				return "You softly rub the petals of "+target.name()+"'s closed flower.";
			}
			else if(target.getArousal().percent()<50){
				return target.name()+"'s sensitive lower lips start to open up under your skilled touch and you can feel her becoming wet.";
			}
			else if(target.getArousal().percent()<80){
				return "You locate "+target.name()+"'s clitoris and caress it directly, causing her to tremble from the powerful stimulation.";
			}
			else{
				return "You stir "+target.name()+"'s increasingly soaked pussy with your fingers and rub her clit with your thumb.";
			}
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "She gropes at your pussy, but miss.";
		}
		if(modifier == Result.weak){
			return "She gropes between your's legs, not really knowing what she's doing. She doesn't know where you're the most sensitive, so she rubs and " +
					"strokes every bit of your moist flesh under her fingers.";
		}
		else{
			if(target.getArousal().get()<=15){
				return "She softly rubs the petals of your closed flower.";
			}
			else if(target.getArousal().percent()<50){
				return "Your sensitive lower lips start to open up under her skilled touch and you can feel yourself becoming wet.";
			}
			else if(target.getArousal().percent()<80){
				return "She locates your clitoris and caress it directly, causing you to tremble from the powerful stimulation.";
			}
			else{
				return "She stirs your increasingly soaked pussy with her fingers and rub your clit directly with her thumb.";
			}
		}
	}

	@Override
	public String describe() {
		return "Digitally stimulate opponent's pussy";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		return "pussy";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "hands";
	}
}
