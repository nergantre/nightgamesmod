package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Pin;

public class Restrain extends Skill {

    public Restrain(Character self) {
        super("Pin", self);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && c.getStance().mobile(getSelf()) && c.getStance().prone(target)
                        && c.getStance().reachTop(getSelf()) && getSelf().canAct() && c.getStance().reachTop(target)
                        && !c.getStance().connected(c);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        return resolve(c, target, false);
    }

    public boolean resolve(Combat c, Character target, boolean nofail) {
        if (nofail || target.roll(getSelf(), c, accuracy(c))) {
            writeOutput(c, Result.normal, target);
            c.setStance(new Pin(getSelf(), target));
            target.emote(Emotion.nervous, 10);
            target.emote(Emotion.desperate, 10);
            getSelf().emote(Emotion.dominant, 20);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 8;
    }

    @Override
    public Skill copy(Character user) {
        return new Restrain(user);
    }

    @Override
    public int speed() {
        return 2;
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
            return "You try to catch " + target.name() + "'s hands, but she squirms too much to keep your grip on her.";
        } else {
            return "You manage to restrain " + target.name() + ", leaving her helpless and vulnerable beneath you.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to pin %s down, but %s %s %s arms free.",
                            getSelf().subject(), target.nameDirectObject(),
                            target.pronoun(), target.action("keep"), target.possessivePronoun());
        } else {
            return String.format("%s pounces on %s and pins %s arms in place, leaving %s at %s mercy.",
                            getSelf().subject(), target.nameDirectObject(), target.possessivePronoun(),
                            target.directObject(), getSelf().directObject());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Restrain opponent until she struggles free";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
