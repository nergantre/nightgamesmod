package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Blinded;
import nightgames.status.Stsflag;

public class RipBlindfold extends Skill {

    public RipBlindfold(Character self) {
        super("Rip Blindfold", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance()
                                      .reachTop(getSelf())
                        && target.is(Stsflag.blinded) && target.getStatus(Stsflag.blinded) instanceof Blinded;
    }

    @Override
    public float priorityMod(Combat c) {
        if (!getSelf().human() && getSelf().has(Trait.mindcontroller)) {
            return c.getStance().dom(getSelf()) ? 10.f : 2.f;
        }
        return -5.f;
    }
    
    @Override
    public String describe(Combat c) {
        return "Rip your opponent's blindfold off.";
    }

    @Override
    public int accuracy(Combat c, Character target) {
        if (!target.canAct() || !((Blinded) target.getStatus(Stsflag.blinded)).isVoluntary()) {
            return 200;
        }
        int base = 60;
        if (c.getStance().sub(target)) {
            base = 100 - (base / 2);
        }
        if (c.getStance().penetratedBy(c, target, getSelf())) {
            base = 100 - (base / 3);
        }
        return base;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean hit = target.roll(getSelf(), c, accuracy(c, target));

        if (hit) {
            c.write(getSelf(),
                            String.format("%s %s blindfold and %s it off with a strong yank.",
                                            getSelf().subjectAction("grab"), target.nameOrPossessivePronoun(),
                                            getSelf().action("pull")));
            target.removeStatus(Stsflag.blinded);
        } else {
            c.write(getSelf(), String.format("%s at %s blindfold, but %s %s away from %s fingers.",
                            getSelf().subjectAction("grasp"), target.nameOrPossessivePronoun(),
                            target.pronoun(), target.action("twist"), getSelf().possessiveAdjective()));
        }

        return hit;
    }

    @Override
    public Skill copy(Character user) {
        return new RipBlindfold(user);
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
