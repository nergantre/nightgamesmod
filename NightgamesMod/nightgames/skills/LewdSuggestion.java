package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Horny;
import nightgames.status.Stsflag;

public class LewdSuggestion extends Skill {

    public LewdSuggestion(Character self) {
        super("Lewd Suggestion", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Hypnosis) >= 3;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().behind(getSelf())
                        && !c.getStance().behind(target) && !c.getStance().sub(getSelf());
    }

    @Override
    public String describe(Combat c) {
        return "Plant an erotic suggestion in your hypnotized target.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean alreadyTranced =
                        target.is(Stsflag.charmed) || target.is(Stsflag.enthralled) || target.is(Stsflag.trance);
        if (!alreadyTranced && Global.random(3) == 0) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.miss, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.miss, target));
            }
            return false;
        } else if (target.is(Stsflag.horny)) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.strong, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.strong, target));
            }
        } else if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }

        target.add(c, Horny.getWithPsycologicalType(getSelf(), target, 10, 4, "Hypnosis"));
        target.emote(Emotion.horny, 30);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new LewdSuggestion(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.strong) {
            return String.format(
                            "You take advantage of the erotic fantasies already swirling through %s's head, whispering ideas that fan the flame of %s lust.",
                            new Object[] {target.name(), target.possessiveAdjective()});
        }
        if (modifier == Result.miss) {
            return String.format(
                            "You whisper ideas that attempt to fan the flame of %s lust, but it doesn't seem to do much",
                            new Object[] {target.nameOrPossessivePronoun()});
        }
        return String.format("You plant an erotic suggestion in %s's mind, distracting %s with lewd fantasies.",
                        new Object[] {target.name(), target.directObject()});
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.strong) {
            return String.format(
                            "%s whispers a lewd suggestion to %s, intensifying the fantasies %s %s trying to ignore and enflaming %s arousal.",
                            getSelf().name(), target.nameDirectObject(), target.pronoun(), target.action("were", "was"),
                            target.possessiveAdjective());
        }
        if (modifier == Result.miss) {
            return String.format(
                            "%s whispers a lewd suggestion to %s, but %s just %s it, and %s to concentrate on the fight.",
                            getSelf().name(), target.nameDirectObject(), target.pronoun(),
                            target.action("ignore"), target.action("try", "tries"));
        }
        return String.format(
                        "%s gives %s a hypnotic suggestion and %s head is immediately filled with erotic possibilities.",
                        getSelf().name(), target.nameDirectObject(), target.possessiveAdjective());
    }

}
