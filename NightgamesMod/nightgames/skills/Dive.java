package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.stance.Stance;
import nightgames.stance.StandingOver;

public class Dive extends Skill {

    public Dive(Character self) {
        super("Dive", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Submissive) >= 1;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().en == Stance.neutral;
    }

    @Override
    public String describe(Combat c) {
        return "Hit the deck! Avoids some attacks.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        c.setStance(new StandingOver(target, getSelf()));
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Dive(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.negative;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You take evasive action and dive to the floor. Ok, you're on the floor now. That's as far as you planned.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().name() + " dives dramatically away and lands flat on the floor.";
    }

}
