package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.stance.Mount;
import nightgames.status.Stsflag;
import nightgames.status.WingWrapped;

public class ComeHither extends Skill {

    public ComeHither(Character self) {
        super("Come Hither", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.ComeHither);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && target.is(Stsflag.charmed)
                        && c.getStance().distance() > 1
                        && c.getStance().mobile(getSelf()) && c.getStance().mobile(target)
                        && target.canAct();
    }

    @Override
    public String describe(Combat c) {
        return "Invite your opponent to get closer";
    }

    @Override
    public boolean resolve(Combat c, Character target) {

        boolean selfMounts = (!getSelf().hasPussy() && getSelf().hasDick())
                             || target.get(Attribute.Submissive) > Global.random(30);
        
        writeOutput(c, selfMounts ? Result.special : Result.normal, target);                   
                            
        if (selfMounts) {
            c.setStance(new Mount(getSelf(), target));
        } else {
            c.setStance(new Mount(target, getSelf()));
            if (getSelf().has(Trait.DemonsEmbrace) && getSelf().body.has("wings")) {
                target.add(c, new WingWrapped(target, getSelf()));
            }
        }
        new Kiss(getSelf()).resolve(c, target);
        
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ComeHither(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        String msg = "You smoothly lay down on your back and shoot {other:name-do} your"
                        + " most winning smile. You open your legs slowly, almost tortously so,"
                        + " keeping {other:possessive} gaze fixed between them. ";
        if (!getSelf().outfit.slotOpen(ClothingSlot.bottom)) {
            msg += "Even though your {self:main-genitals} are still hidden, the mere promise"
                            + " of what might be found between your legs ";
        } else {
            msg += "The sight of {self:main-genitals} ";
        }
        msg += "completely overwhelms any resistance {other:pronoun} had left. {other:PRONOUN}"
                        + " almost falls down on top of you, and you ";
        if (modifier == Result.normal) {
            msg += "embrace {other:direct-object} with a warm kiss.";
            if (getSelf().has(Trait.DemonsEmbrace) && getSelf().body.has("wings")) {
                msg += " You also take the opportunity to wrap your wings around {other:direct-object},"
                                + " holding onto {other:direct-object} tightly.";
            }
        } else {
            msg += "roll over on top of {other:direct-object}, holding {other:direct-object} down while"
                            + " kissing {other:direct-object} tenderly.";
        }
        return Global.format(msg, getSelf(), target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String msg = "{self:SUBJECT} lays down in front of {other:name-do}. In {other:possessive}"
                        + " current state, the fact that this is a very strange tactic in a fight doesn't"
                        + " even register. {self:PRONOUN} gives {other:direct-object} a smoky, lust-filled"
                        + " look and beckons {other:direct-object} forward with a finger while spreading"
                        + " {self:possessive} legs, ";
        if (!getSelf().outfit.slotOpen(ClothingSlot.bottom)) {
            msg += "promising a world of pleasure hidden beneath {self:possessive} clothing.";
        } else {
            msg += "{self:possessive} bare {self:main-genitals} a beacon of unimaginable passion.";
        }
        msg += "{other:SUBJECT-ACTION:collapse|collapses} onto {other:possessive} knees and"
                        + " {other:action:crawl|crawls} forward, ever closer to {self:name-possessive}"
                        + " warm embrace. Upon {other:possessive} arrival, {self:subject} ";
        if (modifier == Result.normal) {
            msg += "wraps {self:possessive} arms around {other:possessive torso, pulling {other:direct-object}"
                            + " down into a passion-filled kiss.";
            if (getSelf().has(Trait.DemonsEmbrace) && getSelf().body.has("wings")) {
                msg += " {self:PRONOUN} also takes the opportunity to wrap {self:possessive}"
                                + " wings around {other:direct-object},"
                                + " holding onto {other:direct-object} tightly.";
            }
        } else {
            msg += "flips the both of " + c.bothDirectObject(target) + " over so {other:subject-action:are|is}"
                            + " on {other:possessive} back, and leans in for a lustful kiss.";
        }
        return Global.format(msg, getSelf(), target);
    }

}
