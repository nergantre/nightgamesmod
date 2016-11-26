package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;

public class ViceGrip extends Thrust {
    public ViceGrip(Character self) {
        super("Vice", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Ninjutsu) >= 24;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && c.getStance().vaginallyPenetratedBy(c, getSelf(), target)
                        && c.getStance().havingSexNoStrapped(c) && target.hasDick() && (target.stunned() || target.getStamina().percent() < 25) && target.getArousal().percent() >= 50;
    }

    @Override
    public int[] getDamage(Combat c, Character target) {
        int[] result = new int[2];

        int m = target.getArousal().max();
        result[0] = m;
        result[1] = 1;

        return result;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        BodyPart selfO = getSelfOrgan(c);
        BodyPart targetO = getTargetOrgan(c, target);
        Result result = Result.normal;

        writeOutput(c, result, target);

        int[] m = getDamage(c, target);
        assert (m.length >= 2);

        if (m[0] != 0)
            target.body.pleasure(getSelf(), selfO, targetO, m[0], c, this);
        if (m[1] != 0)
            getSelf().body.pleasure(target, targetO, selfO, m[1], -10000, c, false, this);
        return true;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 0;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 25;
    }

    @Override
    public Skill copy(Character user) {
        return new ViceGrip(user);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (c.getStance().en == Stance.anal) {
            return Global.format(
                            "{self:SUBJECT-ACTION:rhythmically squeeze|rhythmically squeezes} {self:possessive} {self:body-part:ass} around {other:possessive} dick, milking {other:direct-object} for all that {self:subject-action:are|is} worth.",
                            getSelf(), target);
        } else {
            return Global.format(
                            "{self:SUBJECT-ACTION:give|gives} {other:direct-object} a seductive wink and suddenly {self:possessive} {self:body-part:pussy} squeezes around {other:possessive} {other:body-part:cock} as though it's trying to milk {other:direct-object}.",
                            getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return deal(c, damage, modifier, target);
    }

    @Override
    public String describe(Combat c) {
        return "Ninjitsu technique: squeezes your opponent's dick like a vice, 100% chance to make him cum, but can only be used when the opponent is stunned or weak.";
    }

    @Override
    public String getName(Combat c) {
        return "Vice";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
