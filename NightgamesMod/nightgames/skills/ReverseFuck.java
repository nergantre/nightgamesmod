package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;

public class ReverseFuck extends Fuck {
    public ReverseFuck(String name, Character self, int cooldown) {
        super(name, self, cooldown);
        addTag(SkillTag.positioning);
    }

    public ReverseFuck(Character self) {
        super("Reverse Fuck", self, 0);
    }

    @Override
    public BodyPart getSelfOrgan() {
        return getSelf().body.getRandomPussy();
    }

    @Override
    public BodyPart getTargetOrgan(Character target) {
        return target.body.getRandomCock();
    }

    @Override
    public Skill copy(Character user) {
        return new ReverseFuck(user);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        BodyPart selfO = getSelfOrgan();
        BodyPart targetO = getTargetOrgan(target);
        if (modifier == Result.normal) {
            return Global.format(
                            "{self:subject-action:rub|rubs} {self:possessive} {self:body-part:pussy} against {other:possessive} {other:body-part:cock}, "
                                            + "causing {other:direct-object} to shiver with anticipation. In one swift motion, {self:subject-action:plunge|plunges} {other:possessive} {other:body-part:cock} "
                                            + "into {self:possessive} depths.",
                            getSelf(), target);
        } else if (modifier == Result.miss) {
            if (!selfO.isReady(getSelf()) && !targetO.isReady(target)) {
                return Global.format(
                                "{self:subject-action:are|is} in a good position to fuck {other:direct-object}, but neither of %s are aroused enough to follow through.",
                                getSelf(), target, c.bothDirectObject(target));
            } else if (!getTargetOrgan(target).isReady(target)) {
                return Global.format(
                                "{self:subject-action:position|positions} {self:possessive} {self:body-part:pussy} on top of {other:possessive} {other:body-part:cock}, "
                                                + "but {self:subject-action:find|finds} that {other:possessive} {other:body-part:cock} is still limp.",
                                getSelf(), target);
            } else if (!selfO.isReady(getSelf())) {
                return Global.format(
                                "{self:subject-action:position|positions} {self:possessive} {self:body-part:pussy} on top of {other:possessive} {other:body-part:cock}, "
                                                + "but {self:subject-action|find:finds} that {self:subject-action:are:is} not nearly wet enough to allow a comfortable insertion.",
                                getSelf(), target);
            }
            return Global.format("{self:subject-action:manage|manages} to miss the mark.", getSelf(), target);
        }
        return "Bad stuff happened";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return deal(c, damage, modifier, target);
    }

    @Override
    public String describe(Combat c) {
        return "Straddle your opponent and ride " + c.getOpponent(getSelf()).possessivePronoun() + " cock";
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
