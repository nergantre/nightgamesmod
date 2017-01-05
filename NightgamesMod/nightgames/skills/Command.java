package nightgames.skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
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
import nightgames.items.clothing.ClothingSlot;
import nightgames.skills.damage.DamageType;
import nightgames.stance.Behind;
import nightgames.stance.FaceSitting;
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
        return !getSelf().human() && getSelf().canRespond() && target.is(Stsflag.enthralled)
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
        if (available.contains(CommandType.MASTER_INSERT) && Global.random(100) <= 75) {
            executeCommand(CommandType.MASTER_INSERT, c, target);
            return true;
        }

        // Then positioning
        Set<CommandType> positioning = new HashSet<>(available);
        positioning.retainAll(Arrays.asList(CommandType.MASTER_BEHIND, CommandType.MASTER_MOUNT,
                        CommandType.MASTER_REVERSE_MOUNT, CommandType.MASTER_FACESIT));
        if (!positioning.isEmpty() && Global.random(100) <= 75) {
            executeCommand(Global.pickRandom(positioning.toArray(new CommandType[] {})).get(), c, target);
            return true;
        }

        // Then stripping
        Set<CommandType> stripping = new HashSet<>(available);
        stripping.retainAll(Arrays.asList(CommandType.STRIP_MASTER, CommandType.STRIP_SLAVE));
        if (!stripping.isEmpty() && Global.random(100) <= 75) {
            executeCommand(Global.pickRandom(stripping.toArray(new CommandType[] {})).get(), c, target);
            return true;
        }

        // Then 'one-offs'
        Set<CommandType> oneoff = new HashSet<>(available);
        oneoff.retainAll(Arrays.asList(CommandType.MASTER_STRAPON, CommandType.SUBMIT));
        if (!oneoff.isEmpty() && Global.random(100) <= 75) {
            executeCommand(Global.pickRandom(oneoff.toArray(new CommandType[] {})).get(), c, target);
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
        oral.retainAll(Arrays.asList(CommandType.GIVE_ANNILINGUS, CommandType.GIVE_BLOWJOB,
                        CommandType.GIVE_CUNNILINGUS));
        if (!oral.isEmpty() && Global.random(100) <= 75) {
            executeCommand(Global.pickRandom(oral.toArray(new CommandType[] {})).get(), c, target);
            return true;
        }

        // If none chosen yet, just pick anything
        executeCommand(Global.pickRandom(available.toArray(new CommandType[] {})).get(), c, target);
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
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        // Not used - executeCommand prints messages
        if (modifier == null) {
            return getSelf().name() + "'s order confuses you for a moment, snapping her control over you.";
        }
        switch (modifier) {
            case critical:
                switch (damage) {
                    case 0:
                        return "While commanding you to be still, " + getSelf().name()
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
                return getSelf().name() + "'s eyes bid you to pleasure yourself on her behalf.";
            case special:
                return getSelf().name() + "'s voice pulls you in and you cannot resist fucking her";
            case weak:
                return "You are desperate to see more of " + getSelf().name() + "'s body";
            default:
                return null;
        }
    }

    private EnumSet<CommandType> availableCommands(Combat c, Character target) {
        EnumSet<CommandType> available = EnumSet.of(CommandType.HURT_SELF);

        if (strippable(getSelf()))
            available.add(CommandType.STRIP_MASTER);

        if (strippable(target))
            available.add(CommandType.STRIP_SLAVE);

        if (getSelf().crotchAvailable()) {

            if (target.body.getFetish("cock")
                           .isPresent() && getSelf().hasDick())
                available.add(CommandType.WORSHIP_COCK);

            if (target.body.getFetish("pussy")
                           .isPresent() && getSelf().hasPussy())
                available.add(CommandType.WORSHIP_PUSSY);

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

        if (c.getStance()
             .dom(getSelf()) && c.getStance().en == Stance.standingover) {
            available.add(CommandType.MASTER_MOUNT);
            available.add(CommandType.MASTER_REVERSE_MOUNT);
            if (getSelf().crotchAvailable())
                available.add(CommandType.MASTER_FACESIT);
        }

        if (!getSelf().hasDick() && !getSelf().has(Trait.strapped)
                        && (getSelf().has(Item.Strapon) || getSelf().has(Item.Strapon2)))
            available.add(CommandType.MASTER_STRAPON);

        if (target.crotchAvailable())
            available.add(CommandType.MASTURBATE);

        if (getSelf().getSkills()
                  .stream()
                  .filter(skill -> Tactics.fucking.equals(skill.type(c)))
                  .map(s -> s.copy(getSelf()))
                  .anyMatch(s -> s.requirements(c, getSelf(), target) && s.usable(c, target)))
            available.add(CommandType.MASTER_INSERT);

        return available;
    }

    private boolean strippable(Character ch) {
        if (ch.outfit.isNude() || (ch.outfit.slotOpen(ClothingSlot.top) && ch.outfit.slotOpen(ClothingSlot.bottom)))
            return false;
        if (!ch.outfit.slotOpen(ClothingSlot.top))
            return true;
        if (!ch.outfit.slotOpen(ClothingSlot.bottom) && !ch.outfit.getTopOfSlot(ClothingSlot.bottom)
                                                                  .getID()
                                                                  .equals("strapon"))
            return true;
        return false;
    }

    private Clothing getStripTarget(Character ch) {
        List<Clothing> strippable = ch.outfit.getAllStrippable();
        strippable.removeIf(c -> c.getID()
                                  .equals("strapon"));
        List<Clothing> highPriority = new ArrayList<>(strippable);
        highPriority.removeIf(c -> !c.getSlots()
                                   .contains(ClothingSlot.top)
                        && !c.getSlots()
                            .contains(ClothingSlot.bottom));
        assert !strippable.isEmpty();
        if (!highPriority.isEmpty())
            return (Clothing) highPriority.toArray()[Global.random(highPriority.size())];
        return (Clothing) strippable.toArray()[Global.random(strippable.size())];
    }

    private void executeCommand(CommandType chosen, Combat c, Character target) {
        getSelf().emote(Emotion.confident, 30);
        getSelf().emote(Emotion.dominant, 40);
        switch (chosen) {
            case GIVE_ANNILINGUS:
                c.write(getSelf(),
                                String.format("%s presents %s ass to %s, and %s"
                                                + " instantly %s towards it and %s it fervently.", getSelf().name(),
                                                getSelf().possessivePronoun(), target.nameDirectObject(),
                                                target.pronoun(), target.action("dive"), target.action("lick")));
                int m = target.has(Trait.silvertongue) ? 15 : 10;
                getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandomAss(),
                                7 + Global.random(m), c, this);
                if (Global.random(50) < getSelf().get(Attribute.Fetish) + 10) {
                    target.add(c, new BodyFetish(target, getSelf(), "ass", .1));
                }
                getSelf().buildMojo(c, 15);
                break;
            case GIVE_BLOWJOB:
                c.write(getSelf(),
                                String.format("%s holds up %s %s, and %s simply can't resist"
                                                + " the tantilizing appendage. %s %s head and %s and %s"
                                                + " it all over.", getSelf().name(), getSelf().possessivePronoun(),
                                                getSelf().body.getRandomCock()
                                                              .describe(getSelf()), target.subject(),
                                                              target.subjectAction("lower"),
                                                              target.possessivePronoun(), target.action("lick"),
                                                              target.action("suck")));
                m = target.has(Trait.silvertongue) ? 15 : 10;
                getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandomCock(),
                                7 + Global.random(m), c, this);
                if (Global.random(50) < getSelf().get(Attribute.Fetish) + 10) {
                    target.add(c, new BodyFetish(target, getSelf(), "cock", .1));
                }
                getSelf().buildMojo(c, 15);
                break;
            case GIVE_CUNNILINGUS:
                c.write(getSelf(), String.format(
                                "%s spreads %s labia and before %s can"
                                                + " even tell %s what to do, %s already between %s legs"
                                                + " slavering away at it.",
                                getSelf().name(), getSelf().possessivePronoun(), getSelf().pronoun(),
                                target.directObject(), target.subjectAction("are", "is"),
                                getSelf().possessivePronoun()));
                m = target.has(Trait.silvertongue) ? 15 : 10;
                getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandomPussy(),
                                7 + Global.random(m), c, this);
                if (Global.random(50) < getSelf().get(Attribute.Fetish) + 10) {
                    target.add(c, new BodyFetish(target, getSelf(), "pussy", .1));
                }
                getSelf().buildMojo(c, 15);
                break;
            case MASTER_BEHIND:
                c.write(getSelf(),
                                String.format("Freezing %s in place with a mere"
                                                + " glance, %s casually walks around %s and grabs %s from"
                                                + " behind.", target.directObject(), getSelf().name(),
                                                target.nameDirectObject(),
                                                target.directObject()));
                c.setStance(new Behind(getSelf(), target), target, false);
                getSelf().buildMojo(c, 5);
                break;
            case MASTER_MOUNT:
                c.write(getSelf(),
                                String.format("%s tells %s to remain still and"
                                                + " gracefully lies down on %s, %s face right above %ss.",
                                                getSelf().name(), target.subject(), 
                                                target.directObject(), getSelf().possessivePronoun(),
                                                target.possessivePronoun()));
                c.setStance(new Mount(getSelf(), target), target, false);
                getSelf().buildMojo(c, 5);
                break;
            case MASTER_REVERSE_MOUNT:
                c.write(getSelf(),
                                String.format("%s fixes %s with an intense glare, telling"
                                                + " %s to stay put. Moving a muscle does not even begin to enter"
                                                + " %s thoughts as %s turns away from %s and sits down on %s"
                                                + " belly.", getSelf().name(), target.subject(),
                                                target.directObject(), target.possessivePronoun(), getSelf().pronoun(),
                                                target.directObject(), target.possessivePronoun()));
                c.setStance(new ReverseMount(getSelf(), target), target, false);
                getSelf().buildMojo(c, 5);
                break;
            case MASTER_STRAPON:
                c.write(getSelf(),
                                String.format("%s affixes an impressive-looking strapon"
                                                + " to %s crotch. At first %s a bit intimidated, but once %s"
                                                + " tells %s that %s the look of it, %s %s practically"
                                                + " salivating.", getSelf().name(), getSelf().possessivePronoun(),
                                                target.directObject(),
                                                target.subjectAction("are", "is"), 
                                                getSelf().subject(), target.subjectAction("like"),
                                                target.pronoun(), target.action("are", "is")));
                if (getSelf().has(Item.Strapon2)) {
                    c.write(getSelf(), "The phallic toy vibrates softly but insistently, "
                                    + "obviously designed to make the recepient squeal.");
                }
                getSelf().getOutfit()
                         .equip(Clothing.getByID("strapon"));
                getSelf().buildMojo(c, 10);
                break;
            case MASTURBATE:
                BodyPart pleasured =
                                target.body.getRandom(target.hasDick() ? "cock" : target.hasPussy() ? "pussy" : "ass");
                c.write(getSelf(),
                                String.format("Feeling a bit uninspired, %s just tells %s"
                                                + " to play with %s %s for %s.", getSelf().name(),
                                                target.subject(), target.possessivePronoun(),
                                                pleasured.describe(target), getSelf().directObject()));
                target.body.pleasure(target, target.body.getRandom("hands"), pleasured, 10 + Global.random(20), c, this);
                break;
            case HURT_SELF:
                c.write(getSelf(),
                                String.format("Following a voiceless command,"
                                                + " %s %s elbow into %s gut as hard as %s can."
                                                + " It hurts, but the look of pure amusement on %s face"
                                                + " makes everything alright.", target.subjectAction("slam"),
                                                target.possessivePronoun(), target.possessivePronoun(),
                                                target.pronoun(), getSelf().nameOrPossessivePronoun()));
                target.pain(c, target, (int) target.modifyDamage(DamageType.physical, target, Global.random(30, 50)));
                break;
            case STRIP_MASTER:
                Clothing removed = getStripTarget(getSelf());
                if (removed == null)
                    return;
                getSelf().getOutfit()
                         .unequip(removed);
                c.write(getSelf(),
                                String.format("%s tells %s to remove %s %s for %s."
                                                + " %s gladly %s, eager to see more of %s perfect physique.",
                                                getSelf().subject(), target.subject(), 
                                                getSelf().possessivePronoun(), removed.getName(),
                                                getSelf().directObject(), 
                                                Global.capitalizeFirstLetter(target.pronoun()),
                                                target.action("comply", "complies"),
                                                getSelf().nameOrPossessivePronoun()));
                break;
            case STRIP_SLAVE:
                removed = getStripTarget(target);
                if (removed == null)
                    return;
                target.getOutfit()
                      .unequip(removed);
                c.write(getSelf(),
                                String.format("With a dismissive gesture, %s tells %s"
                                                + " that %s would feel far better without %s %s on. Of course!"
                                                + " That would make <i>everything</i> better! %s eagerly %s"
                                                + " the offending garment.", getSelf().name(), 
                                                target.subject(), target.pronoun(), 
                                                target.possessivePronoun(), removed.getName(),
                                                Global.capitalizeFirstLetter(target.pronoun()),
                                                target.action("remove")));
                break;
            case SUBMIT:
                c.write(getSelf(),
                                String.format("%s stares deeply into %s soul and tells"
                                                + " %s that %s should lie down on the ground. %s obey the order"
                                                + " without hesitation.", getSelf().name(),
                                                target.nameOrPossessivePronoun(), target.directObject(),
                                                target.pronoun(), 
                                                Global.capitalizeFirstLetter(target.subjectAction("obey"))));
                c.setStance(new StandingOver(getSelf(), target), target, false);
                break;
            case WORSHIP_COCK:
                c.write(getSelf(),
                                String.format("%s has a cock. %s that cock. %s humbly"
                                                + " %s for %s permission and %s is letting %s! %s enthusiastically"
                                                + " %s %s at %s feet and %s the beautiful %s with"
                                                + " almost religious zeal. At the same time, %s cannot contain %s lust"
                                                + " and simply must play with %s.", getSelf().name(),
                                                target.subjectAction("NEED", "NEEDS"),
                                                Global.capitalizeFirstLetter(target.pronoun()),
                                                target.action("beg"),
                                                getSelf().nameOrPossessivePronoun(), getSelf().pronoun(),
                                                target.directObject(),
                                                Global.capitalizeFirstLetter(target.pronoun()),
                                                target.action("throw"), target.reflectivePronoun(), getSelf().possessivePronoun(), 
                                                target.action("worship"),
                                                getSelf().body.getRandomCock().describe(target),
                                                target.pronoun(), target.possessivePronoun(),
                                                target.reflectivePronoun()));
                getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandomCock(),
                                10 + Global.random(8), c, this);
                if (target.hasDick())
                    target.body.pleasure(target, target.body.getRandom("hands"), target.body.getRandomCock(),
                                    10 + Global.random(8), c, this);
                else if (target.hasPussy())
                    target.body.pleasure(target, target.body.getRandom("hands"), target.body.getRandomPussy(),
                                    10 + Global.random(8), c, this);
                break;
            case WORSHIP_PUSSY:
                c.write(getSelf(),
                                String.format("%s has a pussy. %s that pussy. %s humbly"
                                                + " %s for %s permission and %s is letting %s! %s enthusiastically"
                                                + " %s %s at %s feet and %s the beautiful %s with"
                                                + " almost religious zeal. At the same time, %s cannot contain %s lust"
                                                + " and simply must play with %s.", getSelf().name(),
                                                target.subjectAction("NEED", "NEEDS"),
                                                Global.capitalizeFirstLetter(target.pronoun()),
                                                target.action("beg"),
                                                getSelf().nameOrPossessivePronoun(), getSelf().pronoun(),
                                                target.directObject(),
                                                Global.capitalizeFirstLetter(target.pronoun()),
                                                target.action("throw"), target.reflectivePronoun(), getSelf().possessivePronoun(), 
                                                target.action("worship"),
                                                getSelf().body.getRandomPussy().describe(target),
                                                target.pronoun(), target.possessivePronoun(),
                                                target.reflectivePronoun()));
                getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandomPussy(),
                                10 + Global.random(8), c, this);
                if (target.hasDick())
                    target.body.pleasure(target, target.body.getRandom("hands"), target.body.getRandomCock(),
                                    10 + Global.random(8), c, this);
                else if (target.hasPussy())
                    target.body.pleasure(target, target.body.getRandom("hands"), target.body.getRandomPussy(),
                                    10 + Global.random(8), c, this);
                break;
            case MASTER_INSERT:
                c.write(getSelf(),
                                String.format("With a mischevous smile, %s tells %s to be still,"
                                                + " and that %s has a special surprise for %s.", getSelf().name(),
                                                target.subject(), getSelf().pronoun(), target.directObject()));
                getSelf().getSkills()
                      .stream()
                      .filter(skill -> Tactics.fucking.equals(skill.type(c)))
                      .map(s -> s.copy(getSelf()))
                      .filter(s -> s.requirements(c, getSelf(), target) && s.usable(c, target))
                      .findAny()
                      .get()
                      .resolve(c, target);
                break;
            case MASTER_FACESIT:
                c.write(getSelf(), String.format("%s stands over %s face and slowly lowers %s down onto it.",
                                getSelf().name(), target.nameOrPossessivePronoun(), getSelf().reflectivePronoun()));
                c.setStance(new FaceSitting(getSelf(), target), target, false);
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
