package skills;

import stance.Stance;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Handjob extends Skill {

	public Handjob(Character self) {
		super("Handjob", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().reachBottom(self)&&(target.pantsless()||(self.has(Trait.dexterous)&&target.bottom.size()<=1))&&target.hasDick()&&self.canAct()&&(!c.getStance().penetration(target)||c.getStance().en==Stance.anal);
	}

	@Override
	public void resolve(Combat c, Character target) {
		int m = 6 + Global.random(5);

		if(target.roll(this, c, accuracy()+self.tohit())){
			if(self.get(Attribute.Seduction)>=8){
				if(self.human()){
					c.write(self,deal(c,m,Result.normal, target));
				}
				else if(target.human()){
					c.write(self,receive(c,m,Result.normal, target));
				}
				target.body.pleasure(self, self.body.getRandom("hands"), target.body.getRandom("cock"), m, c);
				self.buildMojo(c, 10);
			}
			else{
				if(self.human()){
					c.write(self,deal(c,m,Result.normal, target));
				}
				else if(target.human()){
					c.write(self,receive(c,m,Result.weak, target));
				}
				target.body.pleasure(self, self.body.getRandom("hands"), target.body.getRandom("cock"), m, c);
			}
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.miss, target));
			}
		}
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return true;
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
			return self.name()+" grabs for your dick and misses.";
		}
		int r;
		if(!target.bottom.isEmpty()){
			return self.name()+" slips her hand into your "+target.bottom.peek().getName()+" and strokes your dick.";
		}
		else if(modifier==Result.weak){
			return self.name()+" clumsily fondles your crotch. It's not skillful by any means, but it's also not entirely ineffective.";
		}
		else{
			if(target.getArousal().get()<15){
				return self.name()+" grabs your soft penis and plays with the sensitive organ until it springs into readiness.";
			}
			
			else if	((r = Global.random(3)) == 0){
				return self.name()+" strokes and teases your dick, sending shivers of pleasure up your spine.";
			}
			else if(r==1){
				return self.name()+" rubs the sensitive head of your penis and fondles your balls.";
			}
			else{
				return self.name()+" jerks you off like she's trying to milk every drop of your cum.";
			}
		}
	}

	@Override
	public String describe() {
		return "Rub your opponent's dick";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
