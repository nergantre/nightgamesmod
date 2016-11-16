package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.status.Blinded;
import nightgames.status.Stsflag;

public class PlaceBlindfold extends Skill {

    public PlaceBlindfold(Character self) {
        super("Place Blindfold", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && getSelf().has(Item.Blindfold) && !target.is(Stsflag.blinded) && !c.getStance()
                                                                                 .mobile(target);
    }

    @Override
    public float priorityMod(Combat c) {
        if (!getSelf().human() && getSelf().has(Trait.mindcontroller)) {
            return -3.f;
        }
        return 2.f;
    }

    @Override
    public String describe(Combat c) {
        return "Place a blindfold over your opponent's eyes";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (!target.canAct() || target.roll(getSelf(), c, 60)) {
            c.write(getSelf(),
                            String.format("%s a blindfold around %s head, covering %s eyes.",
                                            getSelf().subjectAction("snap"), target.nameOrPossessivePronoun(),
                                            target.possessivePronoun()));
            getSelf().remove(Item.Blindfold);
            target.add(new Blinded(target, "a blindfold", false));
        } else {
            c.write(getSelf(),
                            String.format("%s out a blindfold and %s to place it around %s "
                                            + "head, but %s it away and throws it clear.",
                                            getSelf().subjectAction("take"), getSelf().action("try", "tries"),
                                            target.possessivePronoun(),
                                            target.subjectAction("rip")));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new PlaceBlindfold(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

}
