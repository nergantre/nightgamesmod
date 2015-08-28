package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Enthralled;

public class Whisper extends Skill {

	public Whisper(Character self) {
		super("Whisper", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().kiss(getSelf())&&getSelf().canAct()&&!getSelf().has(Trait.direct);
	}
	
	@Override
	public float priorityMod(Combat c) {
		return getSelf().has(Trait.darkpromises) ? .2f : 0;
	}
	@Override
	public int getMojoBuilt(Combat c) {
		return 10;
	}
	@Override
	public boolean resolve(Combat c, Character target) {
		int roll = Global.centeredrandom(4, getSelf().get(Attribute.Dark) / 5.0, 2);
		int m = 4 + Global.random(6);

		if(target.has(Trait.imagination)) {
			m += 4;
		}
		if(getSelf().has(Trait.darkpromises)) {
			m += 3;
		}
		if(getSelf().has(Trait.darkpromises)&& roll == 4 &&getSelf().canSpend(15) && !target.wary()){
			getSelf().spendMojo(c, 15);
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.special, target));
			}
			target.add(c, new Enthralled(target,getSelf(), 4));
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
		}
		target.tempt(c, getSelf(), m);
		target.emote(Emotion.horny, 30);
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction)>=32 && !user.has(Trait.direct);
	}

	@Override
	public Skill copy(Character user) {
		return new Whisper(user);
	}
	public int speed(){
		return 9;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.special){
			return "You whisper words of domination in "+target.name()+"'s ear, filling her with your darkness. The spirit in her eyes seems to dim as she submits to your will.";
		}
		else {
			return "You whisper sweet nothings in "+target.name()+"'s ear. Judging by her blush, it was fairly effective.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.special){
			return this.getSelf().name() + " whispers in your ear in some eldritch language."
					+ " Her words echo through your head and you feel a"
					+ " strong compulsion to do what she tells you";
		}
		else{
			return getSelf().name()+" whispers some deliciously seductive suggestions in your ear.";
		}
	}

	@Override
	public String describe() {
		return "Arouse opponent by whispering in her ear";
	}
}
