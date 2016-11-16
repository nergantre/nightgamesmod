package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Cowgirl;
import nightgames.stance.Missionary;

public class CounterRide extends CounterBase {
    public CounterRide(Character self) {
        super("Sex Counter", self, 5, Global.format(
                        "{self:SUBJECT-ACTION:invite|invites} the opponent with {self:possessive} body.", self, self));
        addTag(SkillTag.fucking);
        addTag(SkillTag.positioning);
    }

    @Override
    public float priorityMod(Combat c) {
        return Global.randomfloat() * 2;
    }

    @Override
    public void resolveCounter(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        if (target.hasDick() && getSelf().hasPussy()) {
            c.setStance(Cowgirl.similarInstance(getSelf(), target), getSelf(), true);
            new Thrust(getSelf()).resolve(c, target);
        } else {
            c.setStance(Missionary.similarInstance(getSelf(), target), getSelf(), true);
            new Thrust(getSelf()).resolve(c, target);
        }
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 25;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !c.getStance().dom(getSelf()) && !c.getStance().dom(target) && getSelf().canAct()
                        && getSelf().crotchAvailable() && target.crotchAvailable()
                        && (getSelf().hasDick() && target.hasPussy() || getSelf().hasPussy() && target.hasDick());
    }

    @Override
    public int getMojoCost(Combat c) {
        return 40;
    }

    @Override
    public String describe(Combat c) {
        return "Invites opponent into your embrace";
    }

    @Override
    public Skill copy(Character user) {
        return new CounterRide(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.setup && getSelf().hasPussy()) {
            return Global.format(
                            "You turn around and bend over with your ass seductively waving in the air. You slowly "
                                            + "tease your glistening lower lips and spread them apart, inviting {other:name} to take {other:possessive} pleasure.",
                            getSelf(), target);
        } else if (modifier == Result.setup && getSelf().hasDick()) {
            return Global.format(
                            "You grab your cock and quickly stroke it to full mast. You let your dick go and it swings back and forth, catching {other:name-possessive} gaze.",
                            getSelf(), target);
        } else if (getSelf().hasPussy() && target.hasDick()) {
            return Global.format(
                            "As {other:subject} approaches you, you suddenly lower your center of balance and sweep {other:possessive} legs out from under her. "
                                            + "With one smooth motion, you drop your hips and lodge {other:possessive} dick firmly inside yourself.",
                            getSelf(), target);
        } else {
            return Global.format(
                            "As {other:subject} approaches you, you suddenly lower your center of balance and sweep {other:possessive} legs out from under her. "
                                            + "With one smooth motion, you spread her legs apart and plunge into her depths.",
                            getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.setup && getSelf().hasPussy()) {
            return Global.format(
                            "{self:SUBJECT} turns around and bends over her ass seductively waving in the air. She slowly "
                                            + "teases her glistening lower lips and spread them apart, inviting {other:name-do} in to her embrace.",
                            getSelf(), target);
        } else if (modifier == Result.setup && getSelf().hasDick()) {
            return Global.format(
                            "{self:SUBJECT} takes out her cock and strokes it to full mast. She then lets her dick go and it swings back and forth, catching {other:name-possessive} gaze.",
                            getSelf(), target);
        } else if (getSelf().hasPussy() && target.hasDick()) {
            return Global.format(
                            "As {other:subject-action:approach|approaches} {self:name}, {self:pronoun} suddenly disappears from "
                            + "{other:possessive} view; half a second later, {other:possessive} legs are swept out from under {other:direct-object}. "
                                            + "With a soft giggle, {self:name} swiftly mounts {other:name-do} and starts riding {other:possessive} cock.",
                            getSelf(), target);
        } else {
            return Global.format(
                            "As {other:subject} approaches {self:name}, she suddenly disappears from {other:name-possessive} view; half a second "
                            + "later, {other:possessive} legs are swept out from under {other:direct-object}. "
                                            + "With a sexy grin, {self:name} wrenches {other:name-possessive}"
                                            + " legs apart and plunges into {other:possessive} slobbering vagina.",
                            getSelf(), target);
        }
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
