package nightgames.skills;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.stance.Behind;
import nightgames.stance.Mount;
import nightgames.stance.ReverseMount;
import nightgames.stance.Stance;
import nightgames.stance.StandingOver;
import nightgames.status.BodyFetish;
import nightgames.status.Stsflag;

public class Command extends Skill {

	public Command(Character self) {
		super("Command", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return !user.human();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !getSelf().human() && getSelf().canRespond()
				&& target.is(Stsflag.enthralled)
				&& !availableCommands(c, target).isEmpty();
	}

	@Override
	public float priorityMod(Combat c) {
		return 10.0f;
	}

	@Override
	public String describe(Combat c) {
		return "Order your thrall around";
	}

	@Override
	public boolean resolve(Combat c, Character target) {

		EnumSet<CommandType> available = availableCommands(c, target);
		assert !available.isEmpty();

		// Fucking takes priority
		if (available.contains(CommandType.MASTER_INSERT)
				&& Global.random(100) <= 75) {
			executeCommand(CommandType.MASTER_INSERT, c, target);
			return true;
		}

		// Then positioning
		Set<CommandType> positioning = new HashSet<>(available);
		positioning.retainAll(Arrays.asList(CommandType.MASTER_BEHIND,
				CommandType.MASTER_MOUNT, CommandType.MASTER_REVERSE_MOUNT,
				CommandType.MASTER_FACESIT));
		if (!positioning.isEmpty() && Global.random(100) <= 75) {
			executeCommand(
					Global.pickRandom(
							positioning.toArray(new CommandType[] {})),
					c, target);
			return true;
		}

		// Then stripping
		Set<CommandType> stripping = new HashSet<>(available);
		stripping.retainAll(Arrays.asList(CommandType.STRIP_MASTER,
				CommandType.STRIP_SLAVE));
		if (!stripping.isEmpty() && Global.random(100) <= 75) {
			executeCommand(
					Global.pickRandom(stripping.toArray(new CommandType[] {})),
					c, target);
			return true;
		}

		// Then 'one-offs'
		Set<CommandType> oneoff = new HashSet<>(available);
		oneoff.retainAll(
				Arrays.asList(CommandType.MASTER_STRAPON, CommandType.SUBMIT));
		if (!oneoff.isEmpty() && Global.random(100) <= 75) {
			executeCommand(
					Global.pickRandom(oneoff.toArray(new CommandType[] {})), c,
					target);
			return true;
		}

		// Then oral
		if (available.contains(CommandType.WORSHIP_PUSSY)) {
			executeCommand(CommandType.WORSHIP_PUSSY, c, target);
			return true;
		}
		if (available.contains(CommandType.WORSHIP_COCK)) {
			executeCommand(CommandType.WORSHIP_COCK, c, target);
			return true;
		}
		Set<CommandType> oral = new HashSet<>(available);
		oral.retainAll(Arrays.asList(CommandType.GIVE_ANNILINGUS,
				CommandType.GIVE_BLOWJOB, CommandType.GIVE_CUNNILINGUS));
		if (!oral.isEmpty() && Global.random(100) <= 75) {
			executeCommand(
					Global.pickRandom(oral.toArray(new CommandType[] {})), c,
					target);
			return true;
		}

		// If none chosen yet, just pick anything
		executeCommand(
				Global.pickRandom(available.toArray(new CommandType[] {})), c,
				target);
		return true;
	}

	@Override
	public Skill copy(Character target) {
		return new Command(target);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier,
			Character target) {
		return null;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier,
			Character target) {
		if (modifier == null) {
			return getSelf().name()
					+ "'s order confuses you for a moment, snapping her control over you.";
		}
		switch (modifier) {
			case critical:
				switch (damage) {
					case 0:
						return "While commanding you to be still, "
								+ getSelf().name()
								+ " starts bouncing wildly on your dick.";
					case 1:
						return "Her scent overwhelms you and you feel a compulsion to pleasure her.";
					case 2:
						return "You feel an irresistible compulsion to lie down on your back";
					default:
						break;
				}
			case miss:
				return "You feel an uncontrollable desire to undress yourself";
			case normal:
				return getSelf().name()
						+ "'s eyes bid you to pleasure yourself on her behalf.";
			case special:
				return getSelf().name()
						+ "'s voice pulls you in and you cannot resist fucking her";
			case weak:
				return "You are desperate to see more of " + getSelf().name()
						+ "'s body";
			default:
				return null;
		}
	}

	private EnumSet<CommandType> availableCommands(Combat c, Character target) {
		EnumSet<CommandType> available = EnumSet.of(CommandType.HURT_SELF);

		if (getSelf().getRandomStrippable() != null)
			available.add(CommandType.STRIP_MASTER);

		if (target.getRandomStrippable() != null)
			available.add(CommandType.STRIP_SLAVE);

		if (getSelf().crotchAvailable()) {

			if (target.body.getFetish("cock").isPresent()
					&& getSelf().hasDick())
				available.add(CommandType.WORSHIP_COCK);

			if (target.body.getFetish("pussy").isPresent()
					&& getSelf().hasPussy())
				available.add(CommandType.WORSHIP_COCK);

			if (getSelf().hasDick())
				available.add(CommandType.GIVE_BLOWJOB);

			if (getSelf().hasPussy())
				available.add(CommandType.GIVE_CUNNILINGUS);

			available.add(CommandType.GIVE_ANNILINGUS);
		}

		if (c.getStance().en == Stance.neutral) {
			available.add(CommandType.SUBMIT);
			available.add(CommandType.MASTER_BEHIND);
		}

		if (c.getStance().dom(getSelf())
				&& c.getStance().en == Stance.standingover) {
			available.add(CommandType.MASTER_MOUNT);
			available.add(CommandType.MASTER_REVERSE_MOUNT);
			if (getSelf().crotchAvailable())
				available.add(CommandType.MASTER_FACESIT);
		}

		if (!getSelf().hasDick() && !getSelf().has(Trait.strapped)
				&& (getSelf().has(Item.Strapon)
						|| getSelf().has(Item.Strapon2)))
			available.add(CommandType.MASTER_STRAPON);

		if (target.crotchAvailable())
			available.add(CommandType.MASTURBATE);

		if (Global.getByTactics(c, Tactics.fucking).stream()
				.map(s -> s.copy(getSelf()))
				.anyMatch(s -> s.requirements(c, getSelf(), target)
						&& s.usable(c, target)))
			available.add(CommandType.MASTER_INSERT);

		return available;
	}

	private void executeCommand(CommandType chosen, Combat c,
			Character target) {
		getSelf().emote(Emotion.confident, 30);
		getSelf().emote(Emotion.dominant, 40);
		switch (chosen) {
			case GIVE_ANNILINGUS:
				c.write(getSelf(), String.format(
						"%s presents %s ass to you, and you"
								+ " instantly dive towards it and lick it fervently.",
						getSelf().name(), getSelf().possessivePronoun()));
				int m = target.has(Trait.silvertongue) ? 15 : 10;
				getSelf().body.pleasure(target, target.body.getRandom("mouth"),
						getSelf().body.getRandomAss(), 7 + Global.random(m), c);
				if (Global.random(50) < getSelf().get(Attribute.Fetish) + 10) {
					target.add(new BodyFetish(target, getSelf(), "ass", .1));
				}
				getSelf().buildMojo(c, 15);
				break;
			case GIVE_BLOWJOB:
				c.write(getSelf(), String.format(
						"%s holds up %s %s, and you simply can't resist"
								+ " the tantilizing appendage. You lower your head and lick and suck"
								+ " it all over.",
						getSelf().name(), getSelf().possessivePronoun(),
						getSelf().body.getRandomCock().describe(getSelf())));
				m = target.has(Trait.silvertongue) ? 15 : 10;
				getSelf().body.pleasure(target, target.body.getRandom("mouth"),
						getSelf().body.getRandomCock(), 7 + Global.random(m),
						c);
				if (Global.random(50) < getSelf().get(Attribute.Fetish) + 10) {
					target.add(new BodyFetish(target, getSelf(), "cock", .1));
				}
				getSelf().buildMojo(c, 15);
				break;
			case GIVE_CUNNILINGUS:
				c.write(getSelf(), String.format(
						"%s spreads %s labia and before %s can"
								+ " even tell you what to do, you are already between %s legs"
								+ " slavering away at it.",
						getSelf().name(), getSelf().possessivePronoun(),
						getSelf().pronoun(), getSelf().possessivePronoun()));
				m = target.has(Trait.silvertongue) ? 15 : 10;
				getSelf().body.pleasure(target, target.body.getRandom("mouth"),
						getSelf().body.getRandomPussy(), 7 + Global.random(m),
						c);
				if (Global.random(50) < getSelf().get(Attribute.Fetish) + 10) {
					target.add(new BodyFetish(target, getSelf(), "pussy", .1));
				}
				getSelf().buildMojo(c, 15);
				break;
			case MASTER_BEHIND:
				c.write(getSelf(),
						String.format("Freezing you in place with a mere"
								+ " glance, %s casually walks around you and grabs you from"
								+ " behind.", getSelf().name()));
				c.setStance(new Behind(getSelf(), target));
				getSelf().buildMojo(c, 5);
				break;
			case MASTER_MOUNT:
				c.write(getSelf(), String.format(
						"%s tells you to remain still and"
								+ " gracefully lays down on you, %s face right above yours.",
						getSelf().name(), getSelf().possessivePronoun()));
				c.setStance(new Mount(getSelf(), target));
				getSelf().buildMojo(c, 5);
				break;
			case MASTER_REVERSE_MOUNT:
				c.write(getSelf(),
						String.format(
								"%s fixes you with an intense glare, telling"
										+ " you to stay put. Moving a muscle does not even begin to enter"
										+ " your thoughts as %s turns away from you and sits down on your"
										+ " belly.",
								getSelf().name(), getSelf().pronoun()));
				c.setStance(new ReverseMount(getSelf(), target));
				getSelf().buildMojo(c, 5);
				break;
			case MASTER_STRAPON:
				c.write(getSelf(), String.format(
						"%s affixes an impressive-looking strapon"
								+ " to %s crotch. At first you are a bit intimidated, but once %s"
								+ " tells you that you like the look of it, you are practically"
								+ " salivating.",
						getSelf().name(), getSelf().possessivePronoun(),
						getSelf().pronoun()));
				if (getSelf().has(Item.Strapon2)) {
					c.write(getSelf(),
							"The phallic toy vibrates softly but insistently, "
									+ "obviously designed to make the recepient squeal.");
				}
				getSelf().getOutfit().equip(Clothing.getByID("strapon"));
				getSelf().buildMojo(c, 10);
				break;
			case MASTURBATE:
				BodyPart pleasured = target.body.getRandom(target.hasDick()
						? "cock" : target.hasPussy() ? "pussy" : "ass");
				c.write(getSelf(),
						String.format(
								"Feeling a bit uninspired, %s just tells you"
										+ " to play with your %s for %s.",
								getSelf().name(), pleasured.describe(target),
								getSelf().directObject()));
				target.body.pleasure(target, target.body.getRandom("hands"),
						pleasured, 10 + Global.random(20), c);
				break;
			case HURT_SELF:
				c.write(getSelf(),
						String.format(
								"Following a voiceless command,"
										+ " you slam your elbow into you gut as hard as you can."
										+ " It hurts, but the look of pure amusement on %s face"
										+ " makes everything alright.",
								getSelf().nameOrPossessivePronoun()));
				target.pain(c, 10 + Global.random(20));
			case STRIP_MASTER:
				Clothing removed = getSelf().getRandomStrippable();
				if (removed == null) return;
				getSelf().getOutfit().unequip(removed);
				c.write(getSelf(),
						String.format(
								"%s tells you to remove %s %s for %s."
										+ " You gladly comply, eager to see more of %s perfect physique.",
								getSelf().name(), getSelf().possessivePronoun(),
								removed.getName(), getSelf().directObject(),
								getSelf().possessivePronoun()));
				break;
			case STRIP_SLAVE:
				removed = target.getRandomStrippable();
				if (removed == null) return;
				target.getOutfit().unequip(removed);
				c.write(getSelf(),
						String.format(
								"With a dismissive gesture, %s tells you"
										+ " that you would feel far better without your %s on. Of course!"
										+ " That would make <i>everything</i> better! You eagerly remove"
										+ " the offending garment.",
								getSelf().name(), removed.getName()));
				break;
			case SUBMIT:
				c.write(getSelf(),
						String.format(
								"%s stares deeply into your soul and tells"
										+ " you that you should lay down on the ground. You obey the order"
										+ " without hesitation.", getSelf().name()));
				c.setStance(new StandingOver(getSelf(), target));
				break;
			case WORSHIP_COCK:
				c.write(getSelf(),
						String.format(
								"%s has a cock. You NEED that cock. You humbly"
										+ " beg for %s permission and %s is letting you! You enthusiastically"
										+ " throw yourself at %s feet and worship the beautiful %s with"
										+ " almost religious zeal. At the same time, you cannot contain your lust"
										+ " and simply must play with yourself.",
								getSelf().name(), getSelf().possessivePronoun(),
								getSelf().pronoun(), getSelf()
										.possessivePronoun(),
						getSelf().body.getRandomCock().describe(getSelf())));
				getSelf().body.pleasure(target, target.body.getRandom("mouth"),
						getSelf().body.getRandomCock(), 10 + Global.random(8),
						c);
				if (target.hasDick())
					target.body.pleasure(target, target.body.getRandom("hands"),
							target.body.getRandomCock(), 10 + Global.random(8),
							c);
				else if (target.hasPussy())
					target.body.pleasure(target, target.body.getRandom("hands"),
							target.body.getRandomPussy(), 10 + Global.random(8),
							c);
				break;
			case WORSHIP_PUSSY:
				c.write(getSelf(),
						String.format(
								"%s has a pussy. You NEED that pussy. You humbly"
										+ " beg for %s permission and %s is letting you! You enthusiastically"
										+ " throw yourself at %s feet and worship the beautiful %s with"
										+ " almost religious zeal. At the same time, you cannot contain your lust"
										+ " and simply must play with yourself.",
								getSelf().name(), getSelf().possessivePronoun(),
								getSelf().pronoun(), getSelf()
										.possessivePronoun(),
						getSelf().body.getRandomPussy().describe(getSelf())));
				getSelf().body.pleasure(target, target.body.getRandom("mouth"),
						getSelf().body.getRandomPussy(), 10 + Global.random(8),
						c);
				if (target.hasDick())
					target.body.pleasure(target, target.body.getRandom("hands"),
							target.body.getRandomCock(), 10 + Global.random(8),
							c);
				else if (target.hasPussy())
					target.body.pleasure(target, target.body.getRandom("hands"),
							target.body.getRandomPussy(), 10 + Global.random(8),
							c);
				break;
			case MASTER_INSERT:
				c.write(getSelf(),
						String.format(
								"With a mischevous smile, %s tells you to be still,"
										+ " and that %s has a special surprise for you.",
								getSelf().name(), getSelf().pronoun()));
				Global.getByTactics(c, Tactics.fucking).stream()
						.map(s -> s.copy(getSelf()))
						.filter(s -> s.requirements(c, getSelf(), target)
								&& s.usable(c, target)).findAny().get()
						.resolve(c, target);
				break;
			case MASTER_FACESIT:
				c.write(getSelf(), String.format(
						"%s stands over your face and slowly"
								+ " lowers %s down onto it.",
						getSelf().name(), getSelf().reflectivePronoun()));
				break;
		}
	}

	private enum CommandType {
		STRIP_MASTER,
		STRIP_SLAVE,
		WORSHIP_COCK,
		WORSHIP_PUSSY,
		GIVE_BLOWJOB,
		GIVE_CUNNILINGUS,
		GIVE_ANNILINGUS,
		MASTURBATE,
		HURT_SELF,
		SUBMIT,
		MASTER_MOUNT,
		MASTER_FACESIT,
		MASTER_REVERSE_MOUNT,
		MASTER_BEHIND,
		MASTER_STRAPON,
		MASTER_INSERT
	}
}
