package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Stance;
import nightgames.stance.StandingOver;

public class Submit extends Skill {

    public Submit(Character self) {
        super("Submit", self);

    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().en == Stance.neutral && getSelf().canAct();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        c.setStance(new StandingOver(target, getSelf()), target, false);
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.submissive) || user.humanControlled(c);
    }

    @Override
    public Skill copy(Character user) {
        return new Submit(user);
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
        return "You nervously lie down on the floor.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().name() + " with a nervous glance, lies down on the floor.";
    }

    @Override
    public String describe(Combat c) {
        return "Submits to your opponent by lying down.";
    }
}
