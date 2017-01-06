package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;
import nightgames.status.Stsflag;

public class Undress extends Skill {

    public Undress(Character self) {
        super("Undress", self);
        addTag(SkillTag.undressing);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 5;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && !c.getStance()
                                       .sub(getSelf())
                        && (!getSelf().mostlyNude() || !getSelf().reallyNude() && getSelf().stripDifficulty(target) > 0)
                        && !c.getStance()
                             .prone(getSelf());
    }

    @Override
    public String describe(Combat c) {
        return "Remove your own clothes";
    }

    @Override
    public float priorityMod(Combat c) {
        return -10.0f;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Result res = Result.normal;
        int difficulty = getSelf().stripDifficulty(target);
        if (difficulty > 0) {
            res = Global.random(50) > difficulty ? Result.weak : Result.miss;
        }

        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, res, target));
        } else if (c.shouldPrintReceive(target, c)) {
            if (target.human() && target.is(Stsflag.blinded))
                printBlinded(c);
            else
                c.write(getSelf(), receive(c, 0, res, target));
        }
        if (res == Result.normal) {
            getSelf().undress(c);
        } else if (res == Result.weak) {
            getSelf().stripRandom(c, true);
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Undress(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to struggle out of your clothing, but it stubbornly clings onto you.";
        } else if (modifier == Result.weak) {
            return "You manage to struggle out of some of your clothing.";
        }
        if (c.getStance().en != Stance.neutral) {
            return "You wiggle out of your clothes and toss them aside.";
        }
        return "You quickly strip off your clothes and toss them aside.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to struggle out of %s clothing, but it stubbornly clings onto %s.",
                            getSelf().subject(), getSelf().possessiveAdjective(), getSelf().directObject());
        } else if (modifier == Result.weak) {
            return String.format("%s manages to struggle out of some of %s clothing.", getSelf().subject(),
                            getSelf().possessiveAdjective());
        }
        if (c.getStance().en != Stance.neutral) {
            return String.format("%s wiggles out of %s clothes and tosses them aside.", getSelf().subject(),
                            getSelf().possessiveAdjective());
        }
        return String.format("%s puts some space between %s and strips naked.", getSelf().subject(), c.isBeingObserved()
                        ? getSelf().reflectivePronoun() + " and " + target.nameDirectObject() : "you");
    }
}
