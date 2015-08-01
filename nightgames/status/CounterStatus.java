package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.skills.CounterBase;
import nightgames.skills.Skill;

public class CounterStatus extends Status {
	private int duration;
	private CounterBase skill;
	private String desc;

	public CounterStatus(Character affected, CounterBase skill, String description) {
		this(affected, skill, description, 0);
	}

	public CounterStatus(Character affected, CounterBase skill, String description, int duration) {
		super("Counter", affected);
		this.duration=duration;
		this.skill = skill;
		this.desc = description;
		flag(Stsflag.counter);
	}

	@Override
	public String describe() {
		return desc;
	}

	@Override
	public float fitnessModifier () {
		return .5f;
	}
	
	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public void eot(Combat c) {
		if (duration <= 0) {
			affected.removelist.add(this);
		}
		duration--;
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
		return 0;
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
		return -100;
	}

	@Override
	public int value() {
		return 0;
	}
	public void resolveSkill(Combat c, Character target) {
		affected.removelist.add(this);
		skill.resolveCounter(c, target);
	}

	@Override
	public int regen(Combat c) {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new CounterStatus(newAffected, skill, desc, duration);
	}
}
