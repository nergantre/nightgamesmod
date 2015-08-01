package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.skills.Suckle;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class Suckling extends Status {
	private int duration;
	private Suckle skill;

	public Suckling(Character affected, Character opponent, int duration) {
		super("Suckling", affected);
		skill = new Suckle(opponent);
		if(affected.has(Trait.PersonalInertia)){
			this.duration = duration * 3 / 2;
		}else{
			this.duration = duration;
		}
		flag(Stsflag.suckling);
	}

	@Override
	public Collection<Skill> allowedSkills(){
		return Collections.singleton((Skill)new Suckle(affected));
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You feel an irresistable urge to suck on her nipples.";
		}
		else{
			return affected.name()+" is looking intently at your breasts.";
		}
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
		affected.emote(Emotion.horny,15);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
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
		return 0;
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
		return new Suckling(newAffected, skill.user(), duration);
	}
}
