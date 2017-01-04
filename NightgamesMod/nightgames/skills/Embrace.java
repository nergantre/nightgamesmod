package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Position;
import nightgames.stance.Stance;
import nightgames.stance.SuccubusEmbrace;

public class Embrace extends Skill {

    public Embrace(Character self) {
        super("Embrace", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.SuccubusWarmth);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && validPosition(c.getStance(), c, target)
                        && getSelf().body.has("wings");
    }

    private boolean validPosition(Position stance, Combat c, Character target) {
        if (!stance.vaginallyPenetrated(c) || !stance.dom(getSelf())) {
            return false;
        }
        if (stance.en == Stance.flying || stance.en == Stance.upsidedownmaledom
                        || stance.en == Stance.upsidedownfemdom) {
            return false;
        }
        if (stance.vaginallyPenetrated(c, target) && stance.en == Stance.doggy) {
            return true;
        }
        return stance.facing(target, getSelf());
    }

    @Override
    public String describe(Combat c) {
        return "Give your opponent a true demon's embrace";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        
        Position pos = c.getStance();
        Position next;
        boolean selfCatches = c.getStance().vaginallyPenetratedBy(c, getSelf(), target);
        
        String trans = transition(pos, target, selfCatches);
        
        if (selfCatches) {
            c.write(getSelf(), Global.format("%s Now properly seated, {self:subject-action:continue|continues}"
                            + " {self:possessive} bouncing movements while pressing {other:name-possessive}"
                            + " head to {self:possessive} chest. Meanwhile, {self:possessive}"
                            + " {self:body-part:wings} wrap around {other:direct-object}, holding"
                            + " {other:direct-object} firmly in place.", getSelf(), target, trans));
            next = new SuccubusEmbrace(getSelf(), target);
        } else {
            // TODO
            next = null;
            assert false : "Not implemented";
        }
        
        c.setStance(next, getSelf(), true);
        
        return false;
    }

    private String transition(Position pos, Character target, boolean selfCatches) {
        switch (pos.en) {
            case cowgirl:
                assert selfCatches;
                return "";
            case coiled:
                assert selfCatches;
                return "";
            case doggy:
                assert !selfCatches;
                return "";
            case missionary:
                assert !selfCatches;
                return "";
            default: 
                if (selfCatches) {
                    return "";
                } else {
                    return "";
                }
        }
    }
    
    @Override
    public Skill copy(Character user) {
        return new Embrace(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
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
