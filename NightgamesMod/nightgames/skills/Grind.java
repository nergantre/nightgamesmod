package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;

public class Grind extends Thrust {
    private static final String divineName = "Sacrament";

    public Grind(Character self) {
        super("Grind", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 14;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().canthrust(c, getSelf())
                        && c.getStance().havingSexOtherNoStrapped(c, getSelf()) && c.getStance().en != Stance.anal;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 10;
    }

    @Override
    public int[] getDamage(Combat c, Character target) {
        int results[] = new int[2];

        int ms = 8;
        int mt = 5;
        if (getLabel(c).equals(divineName)) {
            ms = 16;
            mt = 10;
        }

        if (getSelf().has(Trait.experienced)) {
            mt = mt * 2 / 3;
        }
        mt = Math.max(1, mt);
        results[0] = ms;
        results[1] = mt;

        return results;
    }

    @Override
    public Skill copy(Character user) {
        return new Grind(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean res = super.resolve(c, target);
        if (getLabel(c).equals(divineName)) {
            target.heal(c, 20);
            target.buildMojo(c, 5);
            target.loseWillpower(c, Global.random(3) + 2, false);
            getSelf().usedAttribute(Attribute.Divinity, c, .5);
        }
        return res;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.reverse) {
            if (getLabel(c).equals(divineName)) {
                return Global.format(
                                "{self:SUBJECT-ACTION:fill|fills} {self:possessive} pussy with divine power until it's positively dripping with glowing golden mists. {self:PRONOUN} {self:action:then grind|grinds} against {other:direct-object} with {self:possessive} "
                                                + getSelfOrgan(c).fullDescribe(getSelf())
                                                + ", stimulating {other:possessive} entire manhood, completely obliterating any resistance from {other:possessive} mind.",
                                getSelf(), target);
            }
            return Global.format(
                            "{self:SUBJECT-ACTION:grind|grinds} against {other:direct-object} with {self:possessive} "
                                            + getSelfOrgan(c).fullDescribe(getSelf())
                                            + ", stimulating {other:possessive} entire manhood and bringing {other:direct-object} closer to climax.",
                            getSelf(), target);
        } else {
            if (getLabel(c).equals(divineName)) {
                // TODO divine for fucking someone
                return Global.format(
                                "{self:SUBJECT} grind {self:possessive} hips against {other:direct-object} without thrusting. {other:SUBJECT} trembles and gasps as the movement stimulates {other:possessive} clit and the walls of {other:possessive} {other:body-part:pussy}.",
                                getSelf(), target);
            }
            return Global.format(
                            "{self:SUBJECT} grind {self:possessive} hips against {other:direct-object} without thrusting. {other:SUBJECT} trembles and gasps as the movement stimulates {other:possessive} clit and the walls of {other:possessive} {other:body-part:pussy}.",
                            getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character attacker) {
        return deal(c, damage, modifier, attacker);
    }

    @Override
    public String describe(Combat c) {
        if (getLabel(c).equals(divineName)) {
            return "Grind against your opponent with minimal thrusting. Extremely consistent pleasure and builds some mojo";
        } else {
            return "Grind against your opponent with minimal thrusting. Extremely consistent pleasure and builds some mojo for both player";
        }
    }

    @Override
    public String getLabel(Combat c) {
        if (getSelf().get(Attribute.Divinity) >= 10) {
            return divineName;
        } else {
            return "Grind";
        }
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
