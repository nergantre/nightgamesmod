package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.stance.Stance;
import nightgames.status.Stsflag;
import nightgames.status.WingWrapped;

public class WingWrap extends Skill {

    public WingWrap(Character self) {
        super("Wing Wrap", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().has(Trait.DemonsEmbrace);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && getSelf().body.has("wings") && (c.getStance()
                                                                      .havingSex(c)
                        || c.getStance()
                            .distance() < 2)
                        && !target.is(Stsflag.wrapped)
                        && c.getStance().facing(getSelf(), target) || c.getStance().en == Stance.behind
                            || c.getStance().en == Stance.behindfootjob;
    }

    @Override
    public String describe(Combat c) {
        return "Wrap your opponent up in your wings";
    }

    @Override
    public float priorityMod(Combat c) {
        return getSelf().has(Trait.VampireWings) ? 4.f : 1.f;
    }

    @Override
    public boolean resolve(Combat c, Character target) {

        c.write(getSelf(), describeWrap(c, target));
        target.add(c, new WingWrapped(target, getSelf()));

        if (c.getStance()
             .sub(getSelf())
                        && c.getStance()
                            .havingSex(c)) {
            SubmissiveHold hold = new SubmissiveHold(getSelf());
            if (Skill.skillIsUsable(c, hold, target)) {
                c.write(getSelf(),
                                Global.format("Taking full advantage of {other:name-possessive}"
                                                + " surprise, {self:subject-action} uses more conventional means"
                                                + " to secure an even better hold on {other:direct-object}!", getSelf(),
                                                target));
                hold.resolve(c, target);
            }
        }

        return true;
    }

    private String describeWrap(Combat c, Character target) {
        String desc;
        switch (c.getStance().en) {
            case missionary:
                desc = "{self:SUBJECT-ACTION:pull|pulls} {other:name-possessive} upper"
                                + " body down on top of {self:poss-pronoun} with a wide grin. Then,"
                                + " {self:possessive} {self:body-part:wings} snake out"
                                + " from under {self:possessive} torso and wrap themselves" + " around "
                                + c.bothDirectObject(target) + ".";
                break;
            case cowgirl:
                desc = "{self:SUBJECT-ACTION:lean|leans} down over {other:name-do}, "
                                + "{self:possessive} {self:body-part:breasts}"
                                + (getSelf().body.getLargestBreasts().size > BreastsPart.c.size
                                                ? "rubbing delightfully into {other:possessive}"
                                                                + " {other:body-part:breasts}."
                                                : "hanging enticingly above {other:direct-object}")
                                + ". {self:POSSESSIVE} {self:body-part:wings} curl down beside "
                                + c.bothDirectObject(target) + " and worm their way underneath "
                                + "{other:possessive} back.";
                break;
            default:
                desc = "{self:SUBJECT-ACTION:pull|pulls} {other:name-do} close to "
                                + " {self:reflective} and then {self:action:wrap|wraps}"
                                + " {self:possessive} {self:body-part:wings} around {other:direct-object}."
                                + " They pull tight around {other:direct-object}, holding"
                                + " {other:direct-object} close to {self:direct-object}.";
        }
        if (c.getStance()
             .facing(getSelf(), target)) {
            desc += " In what little light penetrates the coccoon {self:name-possessive} wings" + " have created, ";
            if (target.human()) {
                desc += "{self:possessive} face, coupled with {self:possessive} confident expression,"
                                + " are at once both terrifying and irresistably attractive.";
            } else {
                desc += "{self:pronoun-action:look|looks} down on {other:direct-object} with amusement.";
            }
        }
        if (getSelf().has(Trait.VampireWings) && target.outfit.slotEmpty(ClothingSlot.top)) {
            desc += "As the material of the wings settle on {other:name-possessive} skin,"
                            + " they begin to drain {other:direct-object} of {other:possessive}" + " Power!";
        }
        return Global.format(desc, getSelf(), target);
    }

    @Override
    public Skill copy(Character user) {
        return new WingWrap(user);
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
