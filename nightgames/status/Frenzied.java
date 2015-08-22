package nightgames.status;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.AssFuck;
import nightgames.skills.Carry;
import nightgames.skills.CounterDrain;
import nightgames.skills.CounterRide;
import nightgames.skills.Drain;
import nightgames.skills.Engulf;
import nightgames.skills.Fly;
import nightgames.skills.Fuck;
import nightgames.skills.Grind;
import nightgames.skills.Invitation;
import nightgames.skills.LegLock;
import nightgames.skills.LevelDrain;
import nightgames.skills.Piston;
import nightgames.skills.ReverseAssFuck;
import nightgames.skills.ReverseCarry;
import nightgames.skills.ReverseFly;
import nightgames.skills.ReverseFuck;
import nightgames.skills.Skill;
import nightgames.skills.SpiralThrust;
import nightgames.skills.SubmissiveHold;
import nightgames.skills.Thrust;
import nightgames.skills.Tighten;
import nightgames.skills.ToggleKnot;

public class Frenzied extends Status {

	private static final Collection<Skill> FUCK_SKILLS = new HashSet<>();

	static {
		// Skills that either lead to penetration, or can be used during it.
		Character p = Global.getPlayer();
		FUCK_SKILLS.add(new AssFuck(p));
		FUCK_SKILLS.add(new Carry(p));
		FUCK_SKILLS.add(new CounterDrain(p));
		FUCK_SKILLS.add(new CounterRide(p));
		FUCK_SKILLS.add(new Drain(p));
		FUCK_SKILLS.add(new Engulf(p)); // Probably?
		FUCK_SKILLS.add(new Fly(p));
		FUCK_SKILLS.add(new Fuck(p));
		FUCK_SKILLS.add(new Grind(p));
		FUCK_SKILLS.add(new Invitation(p));
		FUCK_SKILLS.add(new LegLock(p));
		FUCK_SKILLS.add(new LevelDrain(p));
		FUCK_SKILLS.add(new Piston(p));
		FUCK_SKILLS.add(new ReverseAssFuck(p));
		FUCK_SKILLS.add(new ReverseCarry(p));
		FUCK_SKILLS.add(new ReverseFly(p));
		FUCK_SKILLS.add(new ReverseFuck(p));
		FUCK_SKILLS.add(new SpiralThrust(p));
		FUCK_SKILLS.add(new SubmissiveHold(p));
		FUCK_SKILLS.add(new Thrust(p));
		FUCK_SKILLS.add(new Tighten(p));
		FUCK_SKILLS.add(new ToggleKnot(p));
	}

	private int duration;
	private final int startDuration;
	private final Combat c;

	public Frenzied(Character affected, Combat c, int duration) {
		super("Frenzied", affected);
		this.c = c;
		this.duration = duration;
		this.startDuration = duration;
		flag(Stsflag.frenzied);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s mind blanks, leaving only the bestial need to breed.",
				affected.nameOrPossessivePronoun());
	}

	@Override
	public String describe() {
		if (affected.human()) {
			return "You cannot think about anything other than fucking all those around.";
		} else {
			return String.format("%s has a frenzied look in %s eyes, interested in nothing but raw, hard sex.",
					affected.name(), affected.possessivePronoun());
		}
	}

	@Override
	public int mod(Attribute a) {
		if (a == Attribute.Cunning)
			return -5;
		if (a == Attribute.Power)
			return 3;
		if (a == Attribute.Animism)
			return 3;
		return 0;
	}

	@Override
	public int regen(Combat c) {
		if (--duration == 0 || (!c.getStance().inserted() && !c.getStance().analinserted())) {
			affected.removelist.add(this);
			affected.addlist.add(new Cynical(affected));
		}
		affected.emote(Emotion.horny, 15);
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
		return (int) (-x * 0.2);
	}

	@Override
	public int tempted(int x) {
		return (int) (x * 0.2);
	}

	@Override
	public int evade() {
		return -10;
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
		return -20;
	}

	@Override
	public int value() {
		return 0;
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Frenzied(newAffected, c, startDuration);
	}

	@Override
	public Collection<Skill> allowedSkills() {
		// Gather the preferred skills for which the character meets the
		// requirements
		return FUCK_SKILLS.stream().filter(s -> s.requirements(affected)).map(s -> s.copy(affected))
				.collect(Collectors.toSet());
	}
}
