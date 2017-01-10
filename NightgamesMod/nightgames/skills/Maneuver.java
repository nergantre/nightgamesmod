package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Behind;

public class Maneuver extends Skill {
    public Maneuver(Character self) {
        super("Manuever", self, 2);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && !c.getStance().prone(target) && !c.getStance().behind(getSelf()) && getSelf().canAct()
                        && !getSelf().has(Trait.undisciplined) && !c.getStance().connected(c);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 15;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (isFlashStep(c)) {
            if (target.roll(getSelf(), c, accuracy(c, target))) {
                writeOutput(c, Result.special, target);
                c.setStance(new Behind(getSelf(), target), getSelf(), true);
                getSelf().weaken(c, getSelf().getStamina().get() / 10);
                getSelf().emote(Emotion.confident, 15);
                getSelf().emote(Emotion.dominant, 15);
                target.emote(Emotion.nervous, 10);
                return true;
            } else {
                writeOutput(c, Result.miss, target);
                return false;
            }
        } else {
            if (target.roll(getSelf(), c, accuracy(c, target))) {
                writeOutput(c, Result.normal, target);
                c.setStance(new Behind(getSelf(), target), getSelf(), true);
                getSelf().emote(Emotion.confident, 15);
                getSelf().emote(Emotion.dominant, 15);
                target.emote(Emotion.nervous, 10);
                return true;
            } else {
                writeOutput(c, Result.miss, target);
                return false;
            }
        }
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 20 || isFlashStep(c);
    }

    @Override
    public Skill copy(Character user) {
        return new Maneuver(user);
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public int accuracy(Combat c, Character target) {
        return isFlashStep(c) ? 200 : 75;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return "You channel your ki into your feet and dash behind " + target.getName()
            + " faster than her eyes can follow.";
        } else if (modifier == Result.miss) {
            return "You try to get behind " + target.getName() + " but are unable to.";
        } else {
            return "You dodge past " + target.getName() + "'s guard and grab her from behind.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to slip behind %s, but %s %s able to keep %s in sight.",
                            getSelf().subject(), target.nameDirectObject(), target.pronoun(),
                            target.action("are", "is"), getSelf().directObject());
        } else if (modifier == Result.special) {
            return String.format("%s starts to move and suddenly vanishes. %s for a"
                            + " second and feel %s grab %s from behind.",
                            getSelf().subject(), target.subjectAction("hesitate"),
                            getSelf().subject(), target.directObject());
        } else {
            return String.format("%s lunges at %s, but when %s %s to grab %s, %s ducks out of sight. "
                            + "Suddenly %s arms are wrapped around %ss. How did %s get behind %s?",
                            getSelf().subject(), target.nameDirectObject(), target.pronoun(),
                            target.action("try", "tries"), target.directObject(), getSelf().pronoun(),
                            getSelf().nameOrPossessivePronoun(), target.nameOrPossessivePronoun(),
                            getSelf().pronoun(), target.directObject());
        }
    }

    private boolean isFlashStep(Combat c) {
        return getSelf().getStamina().percent() > 15 && getSelf().get(Attribute.Ki) >= 6;
    }

    @Override
    public String describe(Combat c) {
        if (isFlashStep(c)) {
            return "Use lightning speed to get behind your opponent before she can react: 10% stamina";
        } else {
            return "Get behind opponent";
        }
    }

    @Override
    public boolean makesContact() {
        return true;
    }

    @Override
    public String getLabel(Combat c) {
        if (isFlashStep(c)) {
            return "Flash Step";
        }
        return getName(c);
    }
}
