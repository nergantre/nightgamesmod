package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Cowgirl;
import nightgames.stance.Missionary;

public class CounterDrain extends CounterBase {
    public CounterDrain(Character self) {
        super("Counter Vortex", self, 6,
                        Global.format("{self:SUBJECT-ACTION:glow|glows} with a purple light.", self, self));
        addTag(SkillTag.drain);
        addTag(SkillTag.fucking);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.positioning);
        addTag(SkillTag.dark);
    }

    @Override
    public float priorityMod(Combat c) {
        return Global.randomfloat() * 3;
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
        } else {
            c.setStance(Missionary.similarInstance(getSelf(), target), getSelf(), true);
        }
        Drain drain = new Drain(getSelf());
        drain.resolve(c, target, true);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Dark) >= 25;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !c.getStance().dom(getSelf()) && !c.getStance().dom(target) && getSelf().canAct()
                        && getSelf().crotchAvailable() && target.crotchAvailable()
                        && (getSelf().hasDick() && target.hasPussy() || getSelf().hasPussy() && target.hasDick());
    }

    @Override
    public int getMojoCost(Combat c) {
        return 30;
    }

    @Override
    public String describe(Combat c) {
        return "Counter with Drain";
    }

    @Override
    public Skill copy(Character user) {
        return new CounterDrain(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.setup) {
            return Global.format(
                            "You drop your stance, take a deep breath and close your eyes. A purple glow starts radiating from your core.",
                            getSelf(), target);
        } else {
            return Global.format(
                            "You suddenly open your eyes as you sense {other:name} approaching. "
                                            + "The purple light that surrounds you suddenly flies into {other:direct-object}, "
                                            + "eliciting a cry out of her. She collapses like a puppet with her strings cut and falls to the ground. "
                                            + "Seeing the opportunity, you smirk and leisurely mount her.",
                            getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.setup) {
            return Global.format(
                            "She drops her stance, takes a deep breath and closes her eyes. {other:SUBJECT-ACTION:notice|notices}"
                            + " a purple glow begin to radiate from her core.",
                            getSelf(), target);
        } else {
            return Global.format(
                            "{self:SUBJECT} suddenly opens her eyes as {other:subject-action:approach|approaches}. "
                                            + "The purple light that was orbiting around {self:direct-object} suddenly reverses directions and flies into {other:direct-object}. "
                                            + "The purple energy seems to paralyze {other:possessive} muscles and {other:pronoun-action:collapse|collapses}"
                                            + " like a puppet with {other:possessive} strings cut. {other:PRONOUN} can't help but fall to the ground with a cry. "
                                            + "Seeing the opportunity, {self:pronoun} smirks and leisurely mounts {other:direct-object}.",
                            getSelf(), target);
        }
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
