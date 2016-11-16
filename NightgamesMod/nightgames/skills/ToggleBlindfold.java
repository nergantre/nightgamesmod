package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Blinded;
import nightgames.status.Stsflag;

public class ToggleBlindfold extends Skill {

    public ToggleBlindfold(Character self) {
        super("Toggle Blindfold", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public String getLabel(Combat c) {
        return getSelf().is(Stsflag.blinded) ? "Remove Blindfold" : "Wear Blindfold";
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return ((!getSelf().is(Stsflag.blinded) && getSelf().has(Item.Blindfold))
                        || (getSelf().is(Stsflag.blinded)) && canRemove()) && getSelf().canAct();
    }

    private boolean canRemove() {
        Blinded status = (Blinded) getSelf().getStatus(Stsflag.blinded);
        assert status != null;
        return status.getCause()
                     .equals("a blindfold") && status.isVoluntary();
    }

    public float priorityMod(Combat c) {
        return getSelf().is(Stsflag.blinded) ? 4.f : -4.f;
    }

    @Override
    public String describe(Combat c) {
        return getSelf().is(Stsflag.blinded) ? "Remove your blindfold" : "Put on a blindfold to shield your eyes.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (!getSelf().is(Stsflag.blinded)) {
            getSelf().remove(Item.Blindfold);
            if (!c.getStance()
                  .sub(getSelf()) || target.roll(getSelf(), c, 80)) {
                getSelf().add(new Blinded(getSelf(), "a blindfold", true));
                c.write(getSelf(), String.format("%s a blindfold around %s eyes.",
                                getSelf().subjectAction("tie"), getSelf().possessivePronoun()));
            } else {
                c.write(getSelf(), String.format("%s out a blindfold, but %s it from %s hands and %s it away.",
                                getSelf().subjectAction("take"), target.subjectAction("snatch", "snatches"),
                                getSelf().possessivePronoun(), getSelf().action("throw")));
            }
        } else if (c.getStance()
                    .sub(getSelf()) && target.canAct() && Global.random(2) == 0) {
            c.write(getSelf(),
                            String.format("%s to take off %s blindfold, but %s %s hands away.",
                                            getSelf().subjectAction("try", "tries"), getSelf().possessivePronoun(),
                                            target.subjectAction("keep"), getSelf().possessivePronoun()));
        } else {
            getSelf().gain(Item.Blindfold);
            c.write(getSelf(),
                            String.format("%s off %s blindfold and %s a few times to clear %s eyes.",
                                            getSelf().subjectAction("take"), getSelf().possessivePronoun(),
                                            getSelf().action("blink"), getSelf().possessivePronoun()));
            getSelf().removeStatus(Stsflag.blinded);
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ToggleBlindfold(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
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
