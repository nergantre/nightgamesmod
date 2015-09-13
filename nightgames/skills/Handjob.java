package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.stance.Stance;

public class Handjob extends Skill {

	public Handjob(Character self) {
		super("Handjob", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().reachBottom(getSelf())&&(target.crotchAvailable()||(getSelf().has(Trait.dexterous)&&target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getLayer()<=1))&&target.hasDick()&&getSelf().canAct()&&(!c.getStance().inserted(target)||c.getStance().en==Stance.anal);
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 7;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int m = 6 + Global.random(5);

		if(target.roll(this, c, accuracy(c))){
			if(getSelf().get(Attribute.Seduction)>=8){
				m += 6;
				if(getSelf().human()){
					c.write(getSelf(),deal(c,m,Result.normal, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,m,Result.normal, target));
				}
				target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("cock"), m, c);
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),deal(c,m,Result.normal, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,m,Result.weak, target));
				}
				target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("cock"), m, c);
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
		return user.get(Attribute.Seduction) >= 5;
	}

	@Override
	public Skill copy(Character user) {
		return new Handjob(user);
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You reach for "+target.name()+"'s dick but miss.";
		} else {
			return "You grab "+target.name()+"'s girl-cock and stroke it using the techniques you use when masturbating.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return getSelf().name()+" grabs for your dick and misses.";
		}
		int r;
		if(!target.crotchAvailable()){
			return getSelf().name()+" slips her hand into your "+target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()+" and strokes your dick.";
		}
		else if(modifier==Result.weak){
			return getSelf().name()+" clumsily fondles your crotch. It's not skillful by any means, but it's also not entirely ineffective.";
		}
		else{
			if(target.getArousal().get()<15){
				return getSelf().name()+" grabs your soft penis and plays with the sensitive organ until it springs into readiness.";
			}
			
			else if	((r = Global.random(3)) == 0){
				return getSelf().name()+" strokes and teases your dick, sending shivers of pleasure up your spine.";
			}
			else if(r==1){
				return getSelf().name()+" rubs the sensitive head of your penis and fondles your balls.";
			}
			else{
				return getSelf().name()+" jerks you off like she's trying to milk every drop of your cum.";
			}
		}
	}

	@Override
	public String describe(Combat c) {
		return "Rub your opponent's dick";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
