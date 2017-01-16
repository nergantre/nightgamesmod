package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Charmed;
import nightgames.status.Enthralled;
import nightgames.status.Lovestruck;
import nightgames.status.Stsflag;
import nightgames.status.Trance;

public class Deepen extends Skill {

    public Deepen(Character self) {
        super("Deepen", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Hypnosis) >= 1;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().behind(getSelf())
                        && !c.getStance().behind(target) && !c.getStance().sub(getSelf())
                        && !target.is(Stsflag.enthralled)
                        && (target.is(Stsflag.trance) || target.is(Stsflag.lovestruck) || target.is(Stsflag.charmed));
    }
    
    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    @Override
    public String describe(Combat c) {
        return "Deepen your opponent's trance";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.is(Stsflag.trance)) {
            if (target.human()) {
                c.write(getSelf(), Global.format("{self:NAME-POSSESSIVE} all-encompassing eyes completely fills your field of vision now as she destroys any last trace of independent thought inside your mind.", getSelf(), target));
            } else {
                c.write(getSelf(), Global.format("Since {other:NAME-DO} has alreay been heavy hypnotized, you take the chance to erode the last bits of {other:possessive} resistance. There's no way {other:pronoun} can disobey you now.", getSelf(), target));
            }
            target.add(c, new Enthralled(target, getSelf(), 3));
            target.add(c, new Trance(target, 5));
            target.add(c, new Lovestruck(target, getSelf(), 7));
            target.add(c, new Charmed(target, 10));
        } else if (target.is(Stsflag.lovestruck)) {
            if (target.human()) {
                c.write(getSelf(), Global.format("{self:SUBJECT} holds your face in her hands and forces you to look into her eyes. You don't even think about resisting as her words become truth inside your brain.", getSelf(), target));
            } else {
                c.write(getSelf(), Global.format("Since {other:NAME-DO} has alreay been hypnotized, you take the chance to bring {other:direct-object} even deeper.", getSelf(), target));
            }
            target.add(c, new Trance(target, 5));
            target.add(c, new Lovestruck(target, getSelf(), 7));
            target.add(c, new Charmed(target, 10));
        } else if (target.is(Stsflag.charmed)) {
            if (target.human()) {
                c.write(getSelf(), Global.format("{self:SUBJECT} leans close and brings you deeper under her control with her hypnotic voice.", getSelf(), target));
            } else {
                c.write(getSelf(), Global.format("Since {other:NAME-DO} has alreay been lightly hypnotized, you take the chance to bring {other:direct-object} deeper.", getSelf(), target));
            }
            target.add(c, new Lovestruck(target, getSelf(), 7));
            target.add(c, new Charmed(target, 10));
        }
        return false;
    }

    @Override
    public Skill copy(Character user) {
        return new Deepen(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "NA";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return "NA";
    }
}
