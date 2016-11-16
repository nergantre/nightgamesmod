package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Engulfed;
import nightgames.stance.Stance;

public class Engulf extends CounterBase {
    public Engulf(Character self) {
        super("Engulf", self, 5, counterDesc(self), 2);
        addTag(SkillTag.fucking);
        addTag(SkillTag.positioning);
    }

    private static String counterDesc(Character self) {
        if (self.human()) {
            return "You have spread yourself out, ready to engulf your opponent.";
        } else {
            return String.format("%s spread %s thin, arms open invitingly.", self.name(), self.reflectivePronoun());
        }
    }

    @Override
    public float priorityMod(Combat c) {
        return 2;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().get(Attribute.Slime) > 19;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().en != Stance.engulfed && target.mostlyNude();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 30;
    }

    @Override
    public int speed() {
        return -20;
    }

    @Override
    public String getBlockedString(Combat c, Character target) {
        return Global.format(
                        "{self:SUBJECT-ACTION:move|moves} to engulf {other:subject} "
                                        + "in {self:possessive} slime, but {other:pronoun} stays out of {self:possessive} reach.",
                        getSelf(), target);
    }

    @Override
    public String describe(Combat c) {
        return "Set up a counter to engulf the opponent in your slime";
    }

    @Override
    public Skill copy(Character user) {
        return new Engulf(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return Global.format(
                        "You spread out your slime, getting ready to trap {other:name} in it.",
                        getSelf(), target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format(
                        "{self:NAME}'s body spreads out across the floor. From {self:possessive} lowered position, "
                        + "{self:pronoun} smiles deviously up at {other:name-do}, goading {other:direct-object} into an attack.",
                        getSelf(), target);
    }

    @Override
    public void resolveCounter(Combat c, Character target) {
        String msg = "As {other:subject-action:approach|approaches}, {self:subject} suddenly {self:action:rush|rushes}"
                        + " forward, folding {self:possessive} slime around {other:direct-object}. ";
        if (!target.outfit.isNude()) {
            target.nudify();
            msg += "{self:NAME-POSSESSIVE} slime vibrates wildly around {other:direct-object}, causing"
                            + " {other:possessive} clothes to dissolve without a trace. ";
        }
        msg += "As {self:pronoun} {self:action:reform|reforms} {self:possessive} body around {other:direct-object},"
                        + " {self:possessive} head appears besides {other:possessive}. {self:SUBJECT-ACTION:giggle|giggles}"
                        + " softly into {other:possessive} ear as {self:possessive} slime massages {other:possessive} ";
        if (target.hasDick())
            msg += "cock, ";
        if (target.hasBalls())
            msg += "balls, ";
        if (target.hasPussy())
            msg += "pussy, ";
        msg += "ass and every other inch of {other:possessive} skin. ";
        if (getSelf().getType()
                     .equals("Airi"))
            msg += "\n<i>\"It's done... over... stop struggling... cum.\"</i>";
        c.write(getSelf(), Global.format(msg, getSelf(), target));
        c.setStance(new Engulfed(getSelf(), target), getSelf(), true);
        getSelf().emote(Emotion.dominant, 50);
        getSelf().emote(Emotion.horny, 30);
        target.emote(Emotion.nervous, 50);
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
