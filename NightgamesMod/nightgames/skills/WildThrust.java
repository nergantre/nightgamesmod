package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class WildThrust extends Thrust {
    public WildThrust(Character self) {
        super("Wild Thrust", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Animism) > 1;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().havingSex() && c.getStance().inserted();
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public int[] getDamage(Combat c, Character target) {
        int results[] = new int[2];

        int m = 15 + Global.random(20) + Math.min(getSelf().get(Attribute.Animism), getSelf().getArousal().getReal() / 30);
        int mt = 15 + Global.random(20);
        mt = Math.max(1, mt);
        results[0] = m;
        results[1] = mt;

        return results;
    }

    @Override
    public Skill copy(Character user) {
        return new WildThrust(user);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.anal || modifier == Result.upgrade) {
            return "You wildly pound " + target.name()
                            + " in the ass with no regard to technique. She whimpers in pleasure and can barely summon the strength to hold herself off the floor.";
        } else if (modifier == Result.reverse) {
            return Global.format(
                            "{self:SUBJECT-ACTION:bounce|bounces} wildly on {other:name-possessive} cock with no regard to technique, relentlessly driving you both towards orgasm.",
                            getSelf(), target);
        } else {
            return "You wildly pound your dick into " + target.name()
                            + "'s pussy with no regard to technique. Her pleasure filled cries are proof that you're having an effect, but you're feeling it "
                            + "as much as she is.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.anal) {
            return getSelf().name() + " passionately pegs you in the ass as you groan and try to endure the sensation.";
        } else if (modifier == Result.upgrade) {
            return getSelf().name() + " pistons wildly into you while pushing your shoulders on the ground; "
                            + getSelf().name()
                            + "'s tits are shaking above your head while her strapon stimulates your prostate.";
        } else if (modifier == Result.reverse) {
            return getSelf().name() + " frenziedly bounces on your cock, relentlessly driving you both toward orgasm.";
        } else {
            return Global.format(
                            "{self:SUBJECT-ACTION:rapidly pound|rapidly pounds} {self:possessive} {self:body-part:cock} into {other:possessive} {other:body-part:pussy}, "
                                            + "relentlessly driving you both toward orgasm",
                            getSelf(), target);
        }
    }

    @Override
    public String describe(Combat c) {
        return "Fucks opponent without holding back. Extremely random large damage.";
    }

    @Override
    public String getName(Combat c) {
        if (c.getStance().inserted(getSelf())) {
            return "Wild Thrust";
        } else {
            return "Wild Ride";
        }
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
