package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.Stsflag;
import nightgames.status.TailSucked;

public class TailSuck extends Skill {

	public TailSuck(Character self) {
		super("Tail Suck", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction) >= 20
				&& user.get(Attribute.Dark) >= 15 && user.has(Trait.succubus)
				&& user.body.get("tail").size() > 0;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct() && target.hasDick()
				&& target.body.getRandomCock().isReady(target)
				&& target.crotchAvailable() && c.getStance().mobile(getSelf())
				&& !c.getStance().mobile(target)
				&& !c.getStance().inserted(target);
	}

	@Override
	public String describe(Combat c) {
		return "Use your tail to draw in your target's energies";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if (target.is(Stsflag.tailsucked)) {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.special, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, Result.special, target));
			}
			target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"),
					target.body.getRandomCock(), Global.random(10) + 10, c);
			drain(c, target);
		} else if (getSelf().roll(this, c, accuracy(c))) {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.normal, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, Result.normal, target));
			}
			target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"),
					target.body.getRandomCock(), Global.random(10) + 10, c);
			drain(c, target);
			target.add(c, new TailSucked(target, getSelf(), power()));
		} else {
			if (getSelf().human()) {
				c.write(getSelf(), deal(c, 0, Result.miss, target));
			} else if (target.human()) {
				c.write(getSelf(), receive(c, 0, Result.miss, target));
			}
			if (target.hasBalls())
				target.body.pleasure(getSelf(),
						getSelf().body.getRandom("tail"),
						target.body.getRandom("balls"), Global.random(5) + 5,
						c);
			else
				target.pleasure(Global.random(5) + 5, c);
			return false;
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new TailSuck(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.special) {
			return String.format(
					"Flexing a few choice muscles, you provide extra stimulation"
							+ " to %s trapped %s, drawing in further gouts of %s energy.",
					target.nameOrPossessivePronoun(),
					target.body.getRandomCock().describe(target),
					target.possessivePronoun());
		} else if (modifier == Result.normal) {
			return String.format(
					"You open up the special mouth at the end of your"
							+ " tail and aim it at %s %s. Flashing %s a confident smile, you launch"
							+ " it forward, engulfing the shaft completely. You take a long, deep breath,"
							+ " and you feel life flowing in from your tail as well as through"
							+ " your nose.",
					target.nameOrPossessivePronoun(),
					target.body.getRandomCock().describe(target),
					target.directObject());
		}
		return String.format(
				"You shoot out your tail towards %s unprotected groin, but %s"
						+ " twists away slightly causing you to just miss %s %s. Instead, your tail"
						+ " latches onto %s balls. You can't do much with those in this way, so"
						+ " after a little fondling you let go.",
				target.nameOrPossessivePronoun(), target.pronoun(),
				target.possessivePronoun(),
				target.body.getRandomCock().describe(target),
				target.possessivePronoun());
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == Result.special) {
			return String.format(
					"%s twists and turns %s tail with renewed vigor,"
							+ " stealing more of your energy in the process.",
					getSelf().name(), getSelf().possessivePronoun());
		} else if (modifier == Result.normal) {
			return String.format(
					"%s grabs %s tail with both hands and aims it at"
							+ " your groin. The tip opens up like a flower, revealing a hollow"
							+ " inside shaped suspiciously like a pussy. Leaving you no chance"
							+ " to ponder this curiosity, the tail suddenly flies at you. The opening"
							+ ", which does indeed <i>feel</i> like a pussy as well, engulfs your %s"
							+ " completely. You feel as if you are slowly getting weaker the more it"
							+ " sucks on you. That is not good.",
					getSelf().name(), getSelf().possessivePronoun(),
					target.body.getRandomCock().describe(target));
		}
		return String.format(
				"%s grabs %s tail with both hands and aims it at"
						+ " your groin. The tip opens up like a flower, revealing a hollow"
						+ " inside shaped suspiciously like a pussy. That cannot be good, so"
						+ " you twist your hips just in time to evade the tail as it suddenly"
						+ " launches forward. Evade may be too strong a term, though, as it"
						+ " misses your %s but finds your balls instead. %s does not seem"
						+ " to interested in them, though, and leaves them alone after"
						+ " massaging them a bit.",
				getSelf().name(), getSelf().possessivePronoun(),
				target.body.getRandomCock().describe(target), getSelf().name());
	}

	private void drain(Combat c, Character target) {
		Attribute toDrain = Global.pickRandom(
				target.att.entrySet().stream().filter(e -> e.getValue() != 0)
						.map(e -> e.getKey()).toArray(Attribute[]::new));
		target.add(c, new Abuff(target, toDrain, -power(), 20));
		getSelf().add(c, new Abuff(getSelf(), toDrain, power(), 20));
		target.drain(c, getSelf(), 1 + Global.random(power() * 3));
		target.drainMojo(c, getSelf(), 1 + Global.random(power() * 3));
		target.emote(Emotion.desperate, 5);
		getSelf().emote(Emotion.confident, 5);
	}

	private int power() {
		return (int) (1 + (double) getSelf().get(Attribute.Dark) / 20.0);
	}

}
