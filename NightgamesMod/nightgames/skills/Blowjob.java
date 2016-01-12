package nightgames.skills;

import javax.print.attribute.standard.MediaSize.Other;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.StraponPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.ReverseMount;
import nightgames.stance.SixNine;
import nightgames.stance.Stance;
import nightgames.status.CockBound;
import nightgames.status.Stsflag;
import nightgames.status.Trance;

public class Blowjob extends Skill {

	public Blowjob(Character self) {
		super("Blow", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		boolean canUse = (c.getStance().enumerate() == Stance.facesitting && getSelf().canRespond()) || getSelf().canAct();
		return (target.crotchAvailable()&&target.hasDick()&&c.getStance().oral(getSelf())&&c.getStance().front(getSelf())&&canUse&&!c.getStance().inserted(target))
				|| (getSelf().canRespond() && isVaginal(c));
	}

	@Override
	public float priorityMod(Combat c) {
		float priority = 0;
		if (c.getStance().penetratedBy(getSelf(), c.getOther(getSelf()))) {
			priority += 1.0f;
		}
		if (getSelf().has(Trait.silvertongue)) {
			priority += 1;
		}if (getSelf().has(Trait.experttongue)) {
			priority += 1;
		}
		return priority;
	}

	public boolean isVaginal (Combat c) {
		return c.getStance().vaginallyPenetratedBy(getSelf(), c.getOther(getSelf())) && !c.getOther(getSelf()).has(Trait.strapped) && getSelf().has(Trait.vaginaltongue);
	}
	@Override
	public int getMojoBuilt(Combat c) {
		if (isVaginal(c)) {
			return 10;
		} else if (c.getStance().enumerate() == Stance.facesitting) {
			return 0;
		} else {
			return 5;
		}
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int m = 10 + Global.random(8);
		boolean facesitting = c.getStance().enumerate() == Stance.facesitting;
		if(getSelf().has(Trait.silvertongue)){
			m += 4;
		}
		if (isVaginal(c)) {
			m += 4;
			if(target.human()){
				c.write(getSelf(),receive(c,m,Result.intercourse, target));
			} else if(getSelf().human()){
				c.write(getSelf(),deal(c,m,Result.intercourse, target));
			}
			target.body.pleasure(getSelf(), getSelf().body.getRandom("pussy"), target.body.getRandom("cock"), m, c);					
		} else if(facesitting){
			if(target.human()){
				c.write(getSelf(),receive(c,m,Result.reverse, target));
			} else if(getSelf().human()){
				c.write(getSelf(),deal(c,m,Result.reverse, target));
			}
			target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandom("cock"), m, c);					
			target.buildMojo(c, 10);
		} else if(c.getStance().mobile(target)&&target.roll(this, c, accuracy(c))){
			if(getSelf().has(Trait.silvertongue)){
				if(target.human()){
					c.write(getSelf(),receive(c,m,Result.special, target));
				}
				else if(getSelf().human()){
					c.write(getSelf(),deal(c,m,Result.special, target));
				}
			}
			else{
				if(target.human()){
					c.write(getSelf(),receive(c,m,Result.normal, target));
				}
				else if(getSelf().human()){
					c.write(getSelf(),deal(c,m,Result.normal, target));
				}
			}

			BodyPart mouth = getSelf().body.getRandom("mouth");
			BodyPart cock = target.body.getRandom("cock");
			target.body.pleasure(getSelf(), mouth, cock, m, c);
			if (mouth.isErogenous()) {
				getSelf().body.pleasure(getSelf(), cock, mouth, m, c);
			}

			if(ReverseMount.class.isInstance(c.getStance())){
				c.setStance(new SixNine(getSelf(),target));
			}
		}
		else{
			if(getSelf().human()){
				c.write(deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(receive(c,0,Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction)>=10 && !user.has(Trait.temptress);
	}
	public int accuracy(Combat c){
		return 75;
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
		if (c.getStance().vaginallyPenetrated(getSelf()) && getSelf().has(Trait.vaginaltongue)) {
			return Tactics.fucking;
		} else {
			return Tactics.pleasure;
		}
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		String m = "";
		if(modifier==Result.miss){
			m = "You try to take "+target.name()+"'s penis into your mouth, but she manages to pull away.";
		}
		if(target.getArousal().get()<15){
			m = "You suck on "+target.name()+" flaccid little penis until it grows into an intimidating large erection.";
		}
		else if(target.getArousal().percent()>=90){
			m = target.name()+"'s girl-cock seems ready to burst, so you suck on it strongly and attack the glans with your tongue fiercely.";
		}
		else if(modifier==Result.special){
			m = "You put your skilled tongue to good use tormenting and teasing her unnatural member.";
		} else if (modifier==Result.reverse) {
			m = "With " +target.name() + " sitting over your face, you have no choice but to try to suck her off.";
		} else {
			m = "You feel a bit odd, faced with "+target.name()+"'s rigid cock, but as you lick and suck on it, you discover the taste is quite palatable. Besides, " +
					"making "+target.name()+" squirm and moan in pleasure is well worth it.";
		}
		if (modifier != Result.miss && getSelf().body.getRandom("mouth").isErogenous()) {
			m += "<br>Unfortunately for you, your sensitive modified mouth pussy sends spasms of pleasure into you too as you mouth fuck " + target.possessivePronoun() + " cock.";
		}
		return m;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		String m = "";
		if(modifier==Result.miss){
			m += getSelf().name()+" tries to suck your cock, but you pull your hips back to avoid her.";
		} else if(modifier==Result.special){
			m += getSelf().name()+"'s soft lips and talented tongue work over your dick, drawing out dangerously irresistible pleasure with each touch.";
		} else if(modifier==Result.intercourse){
			m += getSelf().name()+"'s pussy lips suddenly quiver and you feel a long sinuous object wrap around your cock. You realize she's controlling her vaginal tongue to blow you with her pussy! "
					+"Her lower tongue runs up and down your shaft causing you to shudder with arousal.";
		} else if(modifier == Result.reverse){
			m += "Faced with your dick sitting squarely in front of her face, " + getSelf().name() + " obediently tongues your cock in defeat.";
		} else if(target.getArousal().get()<15){
			m += getSelf().name()+" takes your soft penis into her mouth and sucks on it until it hardens";
		}
		else if(target.getArousal().percent()>=90){
			m += getSelf().name()+" laps up the precum leaking from your cock and takes the entire length into her mouth, sucking relentlessly";
		} else{
			int r = Global.random(4);
			if(r==0){
				m += getSelf().name()+" runs her tongue up the length of your dick, sending a jolt of pleasure up your spine. She slowly wraps her lips around your dick and sucks.";
			}
			else if(r==1){
				m += getSelf().name()+" sucks on the head of your cock while her hand strokes the shaft.";
			}
			else if(r==2){
				m += getSelf().name()+" licks her way down to the base of your cock and gently sucks on your balls.";
			}
			else{
				m += getSelf().name()+" runs her tongue around the glans of your penis and teases your urethra.";
			}
		}

		if (modifier != Result.miss && getSelf().body.getRandom("mouth").isErogenous()) {
			m += "<br>Unfortunately for her, as " + getSelf().subject() + " mouth fucks " + target.possessivePronoun() + " cock " + getSelf().nameOrPossessivePronoun() + " sensitive modified mouth pussy sends spasms of pleasure into " + getSelf().reflectivePronoun() + " too.";
		}
		return m;
	}

	@Override
	public String describe(Combat c) {
		return "Lick and suck your opponent's dick";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
