package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Abuff;
import nightgames.status.Primed;

public class Haste extends Skill {

    public Haste(Character self) {
        super("Haste", self, 6);
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
                             .prone(getSelf())
                        && getSelf().canAct() && Primed.isPrimed(getSelf(), 1);
    }

    @Override
    public String describe(Combat c) {
        return "Temporarily buffs your speed: 1 charge";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().add(c, new Primed(getSelf(), -1));
        getSelf().add(c, new Abuff(getSelf(), Attribute.Speed, 10, 6));
        writeOutput(c, Result.normal, target);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Haste(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "You spend a stored time charge. The world around you appears to slow down as your personal time accelerates.");
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "%s hits a button on %s wristwatch and suddenly speeds up. %s is moving so fast that %s seems to blur.",
                        getSelf().getName(), getSelf().possessiveAdjective(), getSelf().pronoun(), getSelf().pronoun());
    }

}
