package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.skills.damage.DamageType;
import nightgames.status.Abuff;
import nightgames.status.Stsflag;
import nightgames.status.TailSucked;

public class TailSuck extends Skill {

    public TailSuck(Character self) {
        super("Tail Suck", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 20 && user.get(Attribute.Dark) >= 15 && user.has(Trait.energydrain)
                        && user.body.get("tail").size() > 0;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && target.hasDick() && target.body.getRandomCock().isReady(target)
                        && target.crotchAvailable() && c.getStance().mobile(getSelf()) && !c.getStance().mobile(target)
                        && !c.getStance().inserted(target);
    }

    @Override
    public String describe(Combat c) {
        return "Use your tail to draw in your target's energies";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.is(Stsflag.tailsucked)) {
            writeOutput(c, Result.special, target);
            target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"), target.body.getRandomCock(),
                            Global.random(10) + 10, c, this);
            drain(c, target);
        } else if (getSelf().roll(getSelf(), c, accuracy(c))) {
            Result res = c.getStance().isBeingFaceSatBy(c, target, getSelf()) ? Result.critical
                            : Result.normal;
            writeOutput(c, res, target);
            target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"), target.body.getRandomCock(),
                            Global.random(10) + 10, c, this);
            drain(c, target);
            target.add(c, new TailSucked(target, getSelf(), power()));
        } else if (target.hasBalls()) {
            writeOutput(c, Result.weak, target);
            target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"), target.body.getRandom("balls"),
                            Global.random(5) + 5, c, this);
            return true;
        } else {
            writeOutput(c, Result.miss, target);
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
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return String.format(
                            "Flexing a few choice muscles, you provide extra stimulation"
                                            + " to %s trapped %s, drawing in further gouts of %s energy.",
                            target.nameOrPossessivePronoun(), target.body.getRandomCock().describe(target),
                            target.possessivePronoun());
        } else if (modifier == Result.normal) {
            return String.format(
                            "You open up the special mouth at the end of your"
                                            + " tail and aim it at %s %s. Flashing %s a confident smile, you launch"
                                            + " it forward, engulfing the shaft completely. You take a long, deep breath,"
                                            + " and you feel life flowing in from your tail as well as through"
                                            + " your nose.",
                            target.nameOrPossessivePronoun(), target.body.getRandomCock().describe(target),
                            target.directObject());
        } else if (modifier == Result.critical) {
            return String.format(
                            "Making sure %s view is blocked, you swing your tail out in front of you, hovering over"
                                            + " %s %s. Then, you open up the mouth at its tip and carefully lower it over the hard shaft."
                                            + " Amusingly, %s does not seem to understand %s predicament, but as soon as you <i>breathe</i>"
                                            + " in %s quickly catches on. The flow of energy through your tail makes you shudder atop"
                                            + " %s face.",
                            target.nameOrPossessivePronoun(), target.possessivePronoun(),
                            target.body.getRandomCock().describe(target), target.subject(), target.possessivePronoun(),
                            target.pronoun(), target.possessivePronoun());
        } else if (modifier == Result.weak) {
            return String.format(
                            "You shoot out your tail towards %s unprotected groin, but %s"
                                            + " twists away slightly causing you to just miss %s %s. Instead, your tail"
                                            + " latches onto %s balls. You can't do much with those in this way, so"
                                            + " after a little fondling you let go.",
                            target.nameOrPossessivePronoun(), target.pronoun(), target.possessivePronoun(),
                            target.body.getRandomCock().describe(target), target.possessivePronoun());
        } else {
            return String.format(
                            "You shoot out your tail towards %s unprotected groin, but %s"
                                            + " twists away slightly causing you to just miss %s %s.",
                            target.nameOrPossessivePronoun(), target.pronoun(), target.possessivePronoun(),
                            target.body.getRandomCock().describe(target));
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return String.format(
                            "%s twists and turns %s tail with renewed vigor,"
                                            + " stealing more of %s energy in the process.",
                            getSelf().name(), getSelf().possessivePronoun(), target.nameOrPossessivePronoun());
        } else if (modifier == Result.normal) {
            return String.format(
                            "%s grabs %s tail with both hands and aims it at"
                                            + " %s groin. The tip opens up like a flower, revealing a hollow"
                                            + " inside shaped suspiciously like a pussy. Leaving %s no chance"
                                            + " to ponder this curiosity, the tail suddenly flies at %s. The opening"
                                            + ", which does indeed <i>feel</i> like a pussy as well, engulfs %s %s"
                                            + " completely. %s as if %s %s slowly getting weaker the more it"
                                            + " sucks on %s. That is not good.",
                            getSelf().name(), getSelf().possessivePronoun(),
                            target.nameOrPossessivePronoun(), target.directObject(),target.directObject(),
                            target.possessivePronoun(), target.body.getRandomCock().describe(target),
                            Global.capitalizeFirstLetter(target.subjectAction("feel")),
                            target.pronoun(), target.action("are", "is"), target.directObject());
        } else if (modifier == Result.critical) {
            return String.format(
                            "With %s nose between %s asscheeks as it is, %s some muscles at the base "
                                            + "of %s spine tense up. %s %sn't sure what's going on, but not long after, %s"
                                            + " %s %s %s being swallowed up in a warm sheath. If %s %s weren't in %s face, you would"
                                            + " think %s were fucking %s. Suddenly, the slick canal contracts around %s dick, and"
                                            + " %s %s some of %s strength flowing out of %s and into it. That is not good.",
                            target.possessivePronoun(), getSelf().nameOrPossessivePronoun(), target.subjectAction("feel"),
                            getSelf().possessivePronoun(), Global.capitalizeFirstLetter(target.pronoun()),
                            target.action("are", "is"), target.pronoun(), target.action("feel"),
                            target.possessivePronoun(), target.body.getRandomCock().describe(target), 
                            getSelf().possessivePronoun(), user().body.getRandomPussy().describe(getSelf()),
                            target.possessivePronoun(),
                            getSelf().subject(), target.directObject(), target.nameOrPossessivePronoun(),
                            target.pronoun(), target.action("feel"), target.possessivePronoun(), target.directObject());
        } else if (modifier == Result.weak) {
            return String.format(
                            "%s grabs %s tail with both hands and aims it at"
                                            + " %s groin. The tip opens up like a flower, revealing a hollow"
                                            + " inside shaped suspiciously like a pussy. That cannot be good, so"
                                            + " %s %s hips just in time to evade the tail as it suddenly"
                                            + " launches forward. Evade may be too strong a term, though, as it"
                                            + " misses %s %s but finds %s balls instead. %s does not seem"
                                            + " too interested in them, though, and leaves them alone after"
                                            + " massaging them a bit.",
                            getSelf().name(), getSelf().possessivePronoun(), target.nameOrPossessivePronoun(),
                            target.subjectAction("twist"), target.possessivePronoun(), target.possessivePronoun(),
                            target.body.getRandomCock().describe(target), target.possessivePronoun(), getSelf().name());
        } else {
            return String.format("%s grabs %s tail with both hands and aims it at"
                            + " %s groin. The tip opens up like a flower, revealing a hollow"
                            + " inside shaped suspiciously like a pussy. That cannot be good, so"
                            + " %s %s hips just in time to evade the tail as it suddenly"
                            + " launches forward..", getSelf().name(), getSelf().possessivePronoun(), 
                            target.nameOrPossessivePronoun(),
                            target.subjectAction("twist"), target.possessivePronoun());
        }
    }

    private void drain(Combat c, Character target) {
        Attribute toDrain = Global.pickRandom(target.att.entrySet().stream().filter(e -> e.getValue() != 0)
                        .map(e -> e.getKey()).toArray(Attribute[]::new));
        target.add(c, new Abuff(target, toDrain, -power(), 20));
        getSelf().add(c, new Abuff(getSelf(), toDrain, power(), 20));
        target.drain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.drain, target, 10));
        target.drainMojo(c, getSelf(), 1 + Global.random(power() * 3));
        target.emote(Emotion.desperate, 5);
        getSelf().emote(Emotion.confident, 5);
    }

    private int power() {
        return (int) (1 + getSelf().get(Attribute.Dark) / 20.0);
    }

}
