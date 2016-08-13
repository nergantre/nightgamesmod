package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.StandingOver;

public class StandUp extends Skill {

    public StandUp(Character self) {
        super("Stand Up", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().getUp(getSelf()) && !c.getStance().mobile(target)
                        && !c.getStance().inserted();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        c.setStance(new StandingOver(getSelf(), target));
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new StandUp(user);
    }

    @Override
    public int speed() {
        return 0;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public float priorityMod(Combat c) {
        return -2;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You relinquish your hold on " + target.name() + " and stand back up.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s relinquishes %s hold on %s and stands back up.",
                        getSelf().subject(), getSelf().possessivePronoun(),
                        target.nameDirectObject());
    }

    @Override
    public String describe(Combat c) {
        return "Stand up";
    }
}
