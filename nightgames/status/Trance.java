package nightgames.status;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.skills.Masturbate;
import nightgames.skills.Piston;
import nightgames.skills.Skill;
import nightgames.skills.Suckle;
import nightgames.skills.Thrust;

public class Trance extends Status {
	private int duration;
	public Trance(Character affected) {
		super("Trance", affected);
		if(affected.has(Trait.PersonalInertia)){
			duration = 3;
		}else{
			duration = 2;
		}
		flag(Stsflag.trance);
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You know that you should be fighting back, but it's so much easier to just surrender.";
		}
		else{
			return affected.name()+" is flush with desire and doesn't seem interested in fighting back.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s now entranced.\n", affected.subjectAction("are", "is"));
	}

	@Override
	public boolean mindgames(){
		return true;
	}
	
	@Override
	public float fitnessModifier () {
		return - (2 + duration / 2.0f);
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		duration--;
		if(duration<=0){
			affected.removelist.add(this);
			affected.addlist.add(new Cynical(affected));
		}
		affected.loseWillpower(c, 1);
		affected.emote(Emotion.horny,15);
		return 0;
	}


	@Override
	public Collection<Skill> allowedSkills(){
		return Arrays.asList((Skill)new Masturbate(affected),
				new Thrust(affected),
				new Piston(affected));
	}

	@Override
	public int damage(Combat c, int x) {
		affected.removelist.add(this);
		return 0;
	}

	@Override
	public double pleasure(Combat c, double x) {
		return 0;
	}

	@Override
	public int weakened(int x) {
		return 0;
	}

	@Override
	public int tempted(int x) {
		return 3;
	}

	@Override
	public int evade() {
		return 0;
	}

	@Override
	public int escape() {
		return -10;
	}

	@Override
	public int gainmojo(int x) {
		return 0;
	}

	@Override
	public int spendmojo(int x) {
		return 0;
	}
	@Override
	public int counter() {
		return -10;
	}

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Trance(newAffected);
	}
}
