package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;

public class OrgasmicThrust extends Thrust {
    public OrgasmicThrust(String name, Character self) {
        super(name, self);
    }

    public OrgasmicThrust(Character self) {
        super("Orgasmic Thrust", self);
        addTag(SkillTag.pleasureSelf);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return false;
    }

    public BodyPart getSelfOrgan(Combat c, Character target) {
        BodyPart part = super.getSelfOrgan(c, target);
        if (part != null && part.isType("cock")) {
            return part;
        }
        return null;
    }

    public int[] getDamage(Combat c, Character target) {
        int results[] = new int[2];

        int m = Global.random(25, 40);
        if (c.getStance().anallyPenetrated(c, target) && getSelf().has(Trait.assmaster)) {
            m *= 1.5;
        }

        results[0] = m;
        results[1] = 0;

        return results;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 0;
    }

    @Override
    public Skill copy(Character user) {
        return new OrgasmicThrust(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.anal) {
            return Global.format("As {self:pronoun-action:are|is} about to cum, {self:subject} rapidly and almost involuntarily "
                            + "{self:action:pump|pumps} {other:name-possessive} ass with {self:possessive} rock hard cock. "
                            + "The only thing {other:pronoun} can manage to do is try and hold on.", getSelf(), target);
        } else {
            return Global.format("As {self:pronoun-action:are|is} about to cum, {self:subject} rapidly and almost involuntarily "
                            + "{self:action:pump|pumps} {other:name-possessive} hot sex with {self:possessive} rock hard cock. "
                            + "The only thing {other:pronoun} can manage to do is try and hold on.", getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return deal(c, damage, modifier, target);
    }

    @Override
    public String describe(Combat c) {
        return "Involuntary skill";
    }

    @Override
    public Character getDefaultTarget(Combat c) {
        return c.getStance().getPartner(c, getSelf());
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
