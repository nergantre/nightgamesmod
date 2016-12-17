package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.stance.Mount;
import nightgames.stance.Stance;
import nightgames.status.MagLocked;
import nightgames.status.Stsflag;

public class MagLock extends Skill {

    public MagLock(Character self) {
        super("MagLock", self, 2);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.maglocks);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && getSelf().canAct() && c.getStance().reachTop(getSelf())
                        && !c.getStance().reachTop(target)
                        && c.getStance().dom(getSelf())
                        && getSelf().has(Item.Battery, 2) // consumed by MagLocked status
                        && (!target.is(Stsflag.maglocked) 
                                        || ((MagLocked) target.getStatus(Stsflag.maglocked)).getCount() < 3);
    }

    @Override
    public String describe(Combat c) {
        return "Place (another) MagLock on your opponent.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        MagLocked stat = (MagLocked) target.getStatus(Stsflag.maglocked);
        if (stat == null) {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:place|places} a metallic band"
                            + " around {other:name-possessive} arm. It doesn't do anything right now,"
                            + " but the powerful electromagnet inside will prove very hard to remove"
                            + " if it can lock on to another such band.", getSelf(), target));
            target.add(c, new MagLocked(target));
            return true;
        }
        if (stat.getCount() == 1) {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:force|forces} {other:name-possessive}"
                            + " wrists behind {other:possessive} back and {self:action:get|gets} a second MagLock around"
                            + " the wrist which was still free. {self:PRONOUN-ACTION:push|pushes} a little button"
                            + " on both bands, causing a loud <i>CLICK</i>."
                            + " {other:SUBJECT-ACTION:try|tries} to move {other:possessive} arms, but"
                            + " {other:action:find|finds} that the two MagLocks are almost inseperable."
                            + " Getting them off will be no mean feat; let alone what would happen if"
                            + " {self:SUBJECT} got another one in place...", getSelf(), target));
            stat.addLock();
            return true;
        } else {
            String end = "<b>{other:SUBJECT-ACTION:are|is} now completely immobilized by the MagLocks,"
                                + " incapable to struggle. {other:POSSESSIVE} only hope now is that"
                                + " {self:name-possessive} batteries run out soon.</b>";
            if (c.getStance().en == Stance.behind) {
                c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:push|pushes} {other:subject}"
                                + " forward, causing {other:direct-object} to fall flat on the ground."
                                + " Moving quickly, {self:subject-action:bend|bends} one of {other:possessive}"
                                + " legs up, so {other:possessive} ankle is right next to {other:possessive}"
                                + " locked wrists. Then {self:pronoun-action:place|places} a third MagLock"
                                + " around it, locking it with the two others and then rolling {other:direct-object}"
                                + " over onto {other:possessive} back. %s"
                                + " ", getSelf(), target, end));
            } else {
                c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:roll|rolls} {other:subject}"
                                + " onto {other:possessive} back and, before {other:pronoun} can"
                                + " respond, {self:action:pull|pulls} one of {other:possessive}"
                                + " ankles up to {other:possessive} bound wrists."
                                + " {self:SUBJECT-ACTION:pull|pulls} out a third MagLock and"
                                + " {self:action:place|places} it around the uncomfortably positioned"
                                + " ankle. The new MagLock joins with the two already in place with"
                                + " a <i>CLICK</i>. %s", getSelf(), target, end));
            }
            c.setStance(new Mount(getSelf(), target));
            stat.addLock();
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new MagLock(user);
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
