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
        super("Embrace", self, 6);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.SuccubusWarmth);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && validPosition(c.getStance(), c, target) && getSelf().body.has("wings");
    }

    @Override
    public float priorityMod(Combat c) {
        return 10.f; // objectively better than the positions it's available from
    }

    private boolean validPosition(Position stance, Combat c, Character target) {
        if (!stance.vaginallyPenetrated(c) || !stance.dom(getSelf())) {
            return false;
        }
        if (stance.en == Stance.succubusembrace || stance.en == Stance.upsidedownmaledom
                        || stance.en == Stance.upsidedownfemdom
                        || (stance.en == Stance.flying && !stance.penetrated(c, getSelf()))) {
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
        boolean selfCatches = c.getStance()
                               .vaginallyPenetratedBy(c, getSelf(), target);

        String trans = transition(c, pos, target, selfCatches);

        if (selfCatches) {
            c.write(getSelf(),
                            Global.format("%s Now properly seated, {self:subject-action:continue|continues}"
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

    private String transition(Combat c, Position pos, Character target, boolean selfCatches) {
        switch (pos.en) {
            case cowgirl:
                assert selfCatches;
                return Global.format(
                                "{self:SUBJECT-ACTION:pull|pulls} {other:name-possessive}"
                                                + " upper body up to {self:possessive} own by {other:possessive}"
                                                + " shoulders. One hand slips behind {other:name-possessive} head and"
                                                + " pulls it towards {self:name-possessive} {self:body-part:breasts},"
                                                + " which are leaking a small bit of milk."
                                                + " <i>\"There, there, {other:name}. Just relax. Have a drink, if"
                                                + " you like.\"</i> All the while, {self:pronoun-action:keep|keeps}"
                                                + " rocking {self:possessive} hips against {other:direct-object}.",
                                getSelf(), target);
            case coiled:
                assert selfCatches;
                return Global.format(
                                "{self:NAME-POSSESSIVE} arms and legs pull even tighter around"
                                                + " {other:name-do}. Then, quickly and efficiently,"
                                                + " {self:pronoun-action:roll|rolls} %s over, seating"
                                                + " {self:reflective} in {other:possessive} lap. Before"
                                                + " {other:subject-action:have|has} a chance to respond,"
                                                + " {self:subject-action:shove|shoves} {other:possessive} head"
                                                + " into {self:possessive} milky cleavage.",
                                getSelf(), target, c.bothDirectObject(target));
            case flying:
                if (pos.penetrated(c, getSelf())) {
                    return Global.format(
                                    "A powerful fear seizes {other:name-do} as"
                                                    + " {self:subject} suddenly {self:action:swoop|swoops} down to"
                                                    + " the ground. {self:PRONOUN-ACTION:are|is} skilled enough to"
                                                    + " keep %s from crashing, and instead {self:pronoun-action:deposit|deposits}"
                                                    + " {other:direct-object} safely on the ground, seating {self:reflective}"
                                                    + " in {other:possessive} lap. {self:PRONOUN} then hugs {other:direct-object}"
                                                    + " close, pushing {other:possessive} face into {self:possessive}"
                                                    + " lactating {self:body-part:breasts}.",
                                    getSelf(), target, c.bothDirectObject(target));
                } else {
                    return "";
                }
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
        return Tactics.fucking;
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
