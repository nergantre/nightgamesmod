package skills;

import stance.ReverseMount;
import stance.SixNine;
import stance.Stance;
import status.Trance;
import status.CockBound;
import status.Stsflag;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Blowjob extends Skill {

	public Blowjob(Character self) {
		super("Blow", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (target.pantsless()&&target.hasDick()&&c.getStance().oral(self)&&!c.getStance().behind(self)&&self.canAct()&&!c.getStance().penetration(self))
				|| ((c.getStance().inserted(target) && self.has(Trait.vaginaltongue) && c.getStance().en != Stance.anal));
	}

	@Override
	public float priorityMod(Combat c) {
		float priority = 0;
		if (c.getStance().penetration(c.p2) || c.getStance().penetration(c.p1)) {
			priority += 1.0f;
		}
		if (self.has(Trait.silvertongue)) {
			priority += 1;
		}if (self.has(Trait.greatkiss)) {
			priority += 1;
		}
		return priority;
	}

	@Override
	public void resolve(Combat c, Character target) {
		int m = 2 + Global.random(6);
		if (c.getStance().inserted(target) && self.has(Trait.vaginaltongue)) {
			m += 4;
			if(target.human()){
				c.write(self,receive(c,m,Result.intercourse, target));
			} else if(self.human()){
				c.write(self,deal(c,m,Result.intercourse, target));
			}
			target.body.pleasure(self, self.body.getRandom("pussy"), target.body.getRandom("cock"), m, c);					
			self.buildMojo(5);
		} else if(target.roll(this, c, accuracy()+self.tohit())){
			if(self.has(Trait.silvertongue)){
				m += 4;
				if(target.human()){
					c.write(self,receive(c,m,Result.special, target));
				}
				else if(self.human()){
					c.write(self,deal(c,m,Result.special, target));
				}
			}
			else{
				if(target.human()){
					c.write(self,receive(c,m,Result.normal, target));
				}
				else if(self.human()){
					c.write(self,deal(c,m,Result.normal, target));
				}
			}

			target.body.pleasure(self, self.body.getRandom("mouth"), target.body.getRandom("cock"), m, c);					

			self.buildMojo(5);
			if(ReverseMount.class.isInstance(c.getStance())){
				c.setStance(new SixNine(self,target));
			}
		}
		else{
			if(self.human()){
				c.write(deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(receive(c,0,Result.miss, target));
			}
		}
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Seduction)>=10;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Seduction)>=10;
	}
	public int accuracy(){
		return 6;
	}
	@Override
	public Skill copy(Character user) {
		return new Blowjob(user);
	}
	public int speed(){
		return 2;
	}

	@Override
	public Tactics type(Combat c) {
		if ((c.getStance().penetration(c.p2) || c.getStance().penetration(c.p1)) && self.has(Trait.vaginaltongue)) {
			return Tactics.fucking;
		} else {
			return Tactics.pleasure;
		}
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You try to take "+target.name()+"'s penis into your mouth, but she manages to pull away.";
		}
		if(target.getArousal().get()<15){
			return "You suck on "+target.name()+" flaccid little penis until it grows into an intimidating large erection.";
		}
		else if(target.getArousal().percent()>=90){
			return target.name()+"'s girl-cock seems ready to burst, so you suck on it strongly and attack the glans with your tongue fiercely.";
		}
		else if(modifier==Result.special){
			return "You put your skilled tongue to good use tormenting and teasing her unnatural member.";
		} else {
			return "You feel a bit odd, faced with "+target.name()+"'s rigid cock, but as you lick and suck on it, you discover the taste is quite palatable. Besides, " +
					"making "+target.name()+" squirm and moan in pleasure is well worth it.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return self.name()+" tries to suck your cock, but you pull your hips back to avoid her.";
		} else if(modifier==Result.special){
			return self.name()+"'s soft lips and talented tongue work over your dick, drawing out dangerously irresistible pleasure with each touch.";
		} else if(modifier==Result.intercourse){
			return self.name()+"'s pussy lips suddenly quiver and you feel a long sinuous object wrap around your cock. You realize she's controlling her vaginal tongue to blow you with her pussy! "
					+"Her lower tongue runs up and down your shaft causing you to shudder with arousal.";
		} else if(target.getArousal().get()<15){
			return self.name()+" takes your soft penis into her mouth and sucks on it until it hardens";
		}
		else if(target.getArousal().percent()>=90){
			return self.name()+" laps up the precum leaking from your cock and takes the entire length into her mouth, sucking relentlessly";
		} else{
			int r = Global.random(4);
			if(r==0){
				return self.name()+" runs her tongue up the length of your dick, sending a jolt of pleasure up your spine. She slowly wraps her lips around your dick and sucks.";
			}
			else if(r==1){
				return self.name()+" sucks on the head of your cock while her hand strokes the shaft.";
			}
			else if(r==2){
				return self.name()+" licks her way down to the base of your cock and gently sucks on your balls.";
			}
			else{
				return self.name()+" runs her tongue around the glans of your penis and teases your urethra.";
			}
		}
	}

	@Override
	public String describe() {
		return "Lick and suck your opponent's dick";
	}
}
