package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Collared;
import nightgames.status.Stsflag;

public class Collar extends Skill {

    public Collar(Character self) {
        super("Collar", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.trainingcollar);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().reachTop(getSelf()) && !target.canRespond()
                        && (!target.is(Stsflag.collared) || getSelf().has(Item.Battery, 5));
    }

    @Override
    public String getLabel(Combat c) {
        return c.getOpponent(getSelf()).is(Stsflag.collared) ? "Recharge Collar" : "Place Collar";
    }
    
    @Override
    public String describe(Combat c) {
        return c.getOpponent(getSelf()).is(Stsflag.collared) 
                        ? "Spend 5 batteries to recharge the collar around your opponent's neck." 
                        : "Place a Training Collar around your opponent's neck";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.is(Stsflag.collared)) {
            getSelf().consume(Item.Battery, 5);
            ((Collared) target.getStatus(Stsflag.collared)).recharge();
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:replace|replaces} the batteries"
                            + " of {other:name-possessive} collar, so it can keep going for longer.",
                            getSelf(), target));
        } else {
            c.write(getSelf(), Global.format("Able to take {self:possessive} time - given"
                            + " {other:name-possessive} current situation - {self:subject-action:pull|pulls}"
                            + " out a metal collar and {self:action:lock|locks} it in place around"
                            + " {other:name-possessive} neck. <i>\"Is that comfortable, {other:name}?\"</i>"
                            + " {self:pronoun-action:ask|asks} {other:direct-object}, <i>\"That little"
                            + " collar is going to make sure you behave. You can be a good %s, right {other:name}?\"<i>"
                            , getSelf(), target, target.boyOrGirl()));
            target.add(c, new Collared(target));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Collar(user);
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
