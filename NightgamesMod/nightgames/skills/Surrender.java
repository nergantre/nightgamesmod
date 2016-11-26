package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;

public class Surrender extends Skill {
    public Surrender(Character self) {
        super("Surrender", self);
        addTag(SkillTag.suicidal);
    }

    @Override
    public float priorityMod(Combat c) {
        return -100000000;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        //getSelf().tempt(c, getSelf().getArousal().max());
        getSelf().loseWillpower(c, getSelf().getWillpower().max());
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Surrender(user);
    }

    @Override
    public int speed() {
        return 6;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "After giving up on the fight, %s start fantasizing about %s body. %s quickly find %s at the edge.",
                        getSelf().subject(), target.possessivePronoun(),
                        Global.capitalizeFirstLetter(getSelf().pronoun()), getSelf().reflectivePronoun());
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "After giving up on the fight, %s start fantasizing about %s body. %s quickly find %s at the edge.",
                        getSelf().subject(), target.possessivePronoun(),
                        Global.capitalizeFirstLetter(getSelf().pronoun()), getSelf().reflectivePronoun());
    }

    @Override
    public String describe(Combat c) {
        return "Give up";
    }
}
