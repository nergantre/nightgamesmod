package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;
import nightgames.stance.TribadismStance;

public class Tribadism extends Skill {

    public Tribadism(String name, Character self, int cooldown) {
        super(name, self, cooldown);
    }

    public Tribadism(Character self) {
        super("Tribadism", self);
    }

    public BodyPart getSelfOrgan() {
        BodyPart res = getSelf().body.getRandomPussy();
        return res;
    }

    public BodyPart getTargetOrgan(Character target) {
        return target.body.getRandomPussy();
    }

    public boolean fuckable(Combat c, Character target) {
        BodyPart selfO = getSelfOrgan();
        BodyPart targetO = getTargetOrgan(target);
        boolean possible = selfO != null && targetO != null;
        boolean stancePossible = false;
        if (possible) {
            stancePossible = true;
            if (selfO.isType("pussy")) {
                stancePossible &= !c.getStance().vaginallyPenetrated(c, getSelf());
            }
            if (targetO.isType("pussy")) {
                stancePossible &= !c.getStance().vaginallyPenetrated(c, target);
            }
        }
        stancePossible &= !c.getStance().havingSex(c);
        return possible && stancePossible && getSelf().clothingFuckable(selfO) && target.crotchAvailable();
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return fuckable(c, target) && c.getStance().mobile(getSelf()) && !c.getStance().mobile(target)
                        && getSelf().canAct();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        BodyPart selfO = getSelfOrgan();
        BodyPart targetO = getTargetOrgan(target);
        writeOutput(c, Result.normal, target);
        if (c.getStance().en != Stance.trib) {
            c.setStance(new TribadismStance(getSelf(), target));
        }
        int otherm = 10;
        int m = 10;
        target.body.pleasure(getSelf(), selfO, targetO, m, c, this);
        getSelf().body.pleasure(target, targetO, selfO, otherm, c, this);
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Tribadism(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.normal) {
            return Global.format(
                            "You grab {other:name-possessive} legs and push them apart. You then push your hot snatch across her pussy lips and grind down on it.",
                            getSelf(), target);
        }
        return "Bad stuff happened";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        BodyPart selfO = getSelfOrgan();
        BodyPart targetO = getTargetOrgan(target);
        if (modifier == Result.normal) {
            String message = String.format("%s grabs %s leg and slides her crotch against %s."
                            + " She then grinds her %s against %s wet %s.", getSelf().subject(),
                            target.nameOrPossessivePronoun(), target.possessivePronoun(),
                            selfO.describe(getSelf()), target.possessivePronoun(),
                            targetO.describe(getSelf()));
            return message;
        }
        return "Bad stuff happened";
    }

    @Override
    public String describe(Combat c) {
        return "Grinds your pussy against your opponent's.";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
