package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Hypersensitive;
import nightgames.status.Stsflag;

public class HeightenSenses extends Skill {

    public HeightenSenses(Character self) {
        super("Heighten Senses", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Hypnosis) >= 5;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().behind(getSelf())
                        && !c.getStance().behind(target) && !c.getStance().sub(getSelf())
                        && (!target.is(Stsflag.hypersensitive) || target.getPure(Attribute.Perception) < 9);
    }

    @Override
    public String describe(Combat c) {
        return "Hypnotize the target to temporarily become more sensitive";
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
        } else if (target.is(Stsflag.hypersensitive) && Global.random(2) == 0) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.strong, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.strong, target));
            }
            target.mod(Attribute.Perception, 1);
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            }
            target.add(c, new Hypersensitive(target));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new HeightenSenses(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.strong) {
            return String.format(
                            "You plant a suggestion in %s's head to increase %s sensitivity. %s accepts the suggestion so easily and strongly that you suspect it may have had a permanent effect.",
                            new Object[] {target.name(), target.possessivePronoun(), target.pronoun()});
        }
        if (modifier == Result.miss) {
            return String.format(
                            "You plant a suggestion in %s's head to increase %s sensitivity. Unfortunately, it didn't seem to affect %s much.",
                            new Object[] {target.name(), target.possessivePronoun(), target.directObject()});
        }
        return String.format(
                        "You plant a suggestion in %s's head to increase %s sensitivity. %s shivers as %s sense of touch is amplified",
                        new Object[] {getSelf().name(), target.possessivePronoun(), target.pronoun(),
                                        target.possessivePronoun()});
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.strong) {
            return String.format(
                            "%s explains to %s that %s body, especially %s erogenous zones, have become more"
                            + " sensitive. %s's right. All %s senses feel heightened. %s almost like "
                            + "a superhero. It's ok if this is permanent, right?",
                            getSelf().name(), target.subject(), target.possessivePronoun(), 
                            target.possessivePronoun(), getSelf().subject(), target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.subjectAction("feel")));
        }
        if (modifier == Result.miss) {
            return String.format(
                            "%s explains to %s that %s body, especially %s erogenous zones, have become more"
                            + " sensitive. %s aren't really feeling it though.",
                            getSelf().name(), target.subject(), target.possessivePronoun(),
                            target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.subjectAction("aren't", "isn't")));
        }
        return String.format(
                        "%s explains to %s that %s body, especially %s erogenous zones, have become more "
                        + "sensitive. %s goosebumps cover %s skin as if %s %s been hit by a "
                        + "Sensitivity Flask. Maybe %s %s and just didn't notice",
                        getSelf().name(), target.subject(), target.possessivePronoun(), 
                        target.possessivePronoun(),
                        Global.capitalizeFirstLetter(target.subjectAction("feel")),
                        target.possessivePronoun(), target.pronoun(), target.action("have", "has"),
                        target.pronoun(), target.action("were", "was"));
    }

}
