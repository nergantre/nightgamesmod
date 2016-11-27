package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Collared;
import nightgames.status.Stsflag;

public class RemoveBomb extends Skill {

    public RemoveBomb(Character self) {
        super("Remove Bomb", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && getSelf().is(Stsflag.bombed);
    }

    @Override
    public String describe(Combat c) {
        return "Try to remove the device on your chest.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().is(Stsflag.collared) && Global.random(100) < 40) {
            Collared stat = (Collared) getSelf().getStatus(Stsflag.collared);
            stat.spendCharges(c, 1);
            writeOutput(c, Result.special, target);
            getSelf().pain(c, null, 20 + Global.random(40));
            return false;
        }
        if (c.getStance().dom(target)) {
            if (Global.random(100) < 75) {
                writeOutput(c, Result.miss, target);
                return false;
            } else {
                writeOutput(c, Result.normal, target);
                getSelf().removeStatus(Stsflag.bombed);
            }
        } else if (Global.random(100) < getSelf().getStamina().percent()) {
            writeOutput(c, Result.normal, target);
            getSelf().removeStatus(Stsflag.bombed);
        } else {
            writeOutput(c, Result.miss, target);
        }
        getSelf().pain(c, null, 10 + Global.random(40));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new RemoveBomb(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.recovery;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.normal) {
            return "You grab the beeping device on your chest and rip it off. It gives off"
                            + " a powerful shock, but you ignore it long enough to throw"
                            + " it away.";
        }
        if (modifier == Result.special) {
            return "You reach up to grab the sphere on your chest, but the collar around your neck"
                            + " does not appreciate the sentiment and shocks you to keep your arms down.";
        }
        if (!c.getStance().dom(target)) {
            return "You reach for the device on your chest, but the moment you touch it, it sends"
                            + " a powerful shock up your arm. You draw your hand back in pain.";
        }
        switch (c.getStance().en) {
            case behind:
            case pin:
                return Global.format("You reach towards your chest, aiming to get the beeping sphere off and away,"
                            + " but {other:subject} catches your wrists and pulls your hands back down."
                                , getSelf(), target);    
            case missionary:
            case cowgirl:
            case mount:
                return Global.format("You try to get the metallic sphere off your chest, but {other:subject} catches"
                                + " your hands and pulls them up over your head, well away from the"
                                + " intimidating device.", getSelf(), target);
            default:
                return Global.format("You try to remove the metallic sphere from your chest, but {other:subject}"
                                + " keeps your hands away from it.", getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "{self:SUBJECT} tries to remove your bomb from {self:possessive} chest, but fails.";
        }
        return "{self:SUBJECT} removes your bomb from {self:possessive} chest.";
    }

}
