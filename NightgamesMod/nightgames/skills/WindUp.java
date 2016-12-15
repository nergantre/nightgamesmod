package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Primed;

public class WindUp extends Skill {

    public WindUp(Character self) {
        super("Wind Up", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.getPure(Attribute.Temporal) >= 1;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance()
                .mobile(getSelf())
                        && !c.getStance()
                             .prone(getSelf());
    }
    
    @Override
    public float priorityMod(Combat c) {
        int temp = getSelf().getPure(Attribute.Temporal);
        float base;
        if (temp < 6)
            base = 1.f;
        else if (temp < 8)
            base = 2.f;
        else if (temp < 10)
            base = 2.5f;
        else
            base = 3.f;
        return Primed.isPrimed(getSelf(), 6) ? base / 2.f : base;
    }

    @Override
    public String describe(Combat c) {
        return "Primes time charges: first charge free, 2 Mojo for each additional charge";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int charges = Math.min(4, getSelf().getMojo()
                                           .get()
                        / 5);
        getSelf().add(c, new Primed(getSelf(), charges + 1));
        getSelf().spendMojo(c, charges * 5);
        writeOutput(c, Result.normal, target);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new WindUp(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.recovery;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "You take advantage of a brief lull in the fight to wind up your Procrastinator, priming time charges for later use.");
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s fiddles with a small device on %s wrist.", getSelf().name(),
                        getSelf().possessivePronoun());
    }

}
