package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;

public class ConcedePosition extends Skill {

    public ConcedePosition(Character self) {
        super("Concede Position", self);
        addTag(SkillTag.worship);
        addTag(SkillTag.petDisallowed);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return false;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().dom(getSelf()) && c.getStance().reverse(c, false) != c.getStance() && c.getStance().havingSex(c, getSelf());
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        c.setStance(c.getStance().reverse(c, false));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ConcedePosition(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
            return Global.format("{other:NAME-POSSESSIVE} divine majesty is too much for {self:name-do}. "
                            + "With a docile smile, {self:pronoun-action:concede|concedes} {self:possessive} dominant position to {other:direct-object}.", getSelf(), target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.anal) {
            return String.format("%s the pressure in %s anus recede as %s pulls out.",
                            target.subjectAction("feel"), target.possessiveAdjective(),
                            getSelf().subject());
        } else if (modifier == Result.reverse) {
            return String.format("%s lifts %s hips more than normal, letting %s dick slip completely out of %s.",
                            getSelf().subject(), getSelf().possessiveAdjective(),
                            target.nameOrPossessivePronoun(), getSelf().directObject());
        } else if (modifier == Result.normal) {
            return String.format("%s pulls %s dick completely out of %s pussy, leaving %s feeling empty.",
                            getSelf().subject(), getSelf().possessiveAdjective(),
                            target.nameOrPossessivePronoun(), target.directObject());
        } else {
            return String.format("%s lifts herself off %s face, giving %s a brief respite.",
                            getSelf().subject(), target.nameOrPossessivePronoun(), target.directObject());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Concede your dominant position to your opponent";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
