package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.status.Oiled;
import nightgames.status.Stsflag;

public class Lubricate extends Skill {

    public Lubricate(Character self) {
        super("Lubricate", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf()) && getSelf().canAct() && getSelf().has(Item.Lubricant)
                        && target.mostlyNude() && !target.is(Stsflag.oiled) && !c.getStance().prone(getSelf());
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        target.add(c, new Oiled(target));
        getSelf().consume(Item.Lubricant, 1);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Lubricate(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You cover " + target.name() + " with an oily Lubricant.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s throws an oily liquid at %s. The liquid "
                        + "clings to %s and makes %s whole body slippery.",
                        getSelf().subject(), target.nameDirectObject(),
                        target.directObject(), target.possessiveAdjective());
    }

    @Override
    public String describe(Combat c) {
        return "Oil up your opponent, making her easier to pleasure";
    }

}
