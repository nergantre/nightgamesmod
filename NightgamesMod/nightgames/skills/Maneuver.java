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
        super("Manuever", self);
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
        return 8;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            writeOutput(c, Result.normal, target);
            c.setStance(new Behind(getSelf(), target));
            getSelf().emote(Emotion.confident, 15);
            getSelf().emote(Emotion.dominant, 15);
            target.emote(Emotion.nervous, 10);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 20;
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
    public int accuracy(Combat c) {
        return 75;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to get behind " + target.name() + " but are unable to.";
        } else {
            return "You dodge past " + target.name() + "'s guard and grab her from behind.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to slip behind %s, but %s %s able to keep %s in sight.",
                            getSelf().subject(), target.nameDirectObject(), target.pronoun(),
                            target.action("are", "is"), getSelf().directObject());
        } else {
            return String.format("%s lunges at %s, but when %s %s to grab %s, %s ducks out of sight. "
                            + "Suddenly %s arms are wrapped around %s. How did %s get behind %s?",
                            getSelf().subject(), target.nameDirectObject(), target.pronoun(),
                            target.action("try", "tries"), target.directObject(), getSelf().pronoun(),
                            getSelf().nameOrPossessivePronoun(), target.nameOrPossessivePronoun(),
                            getSelf().pronoun(), target.directObject());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Get behind opponent: 8 Mojo";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
