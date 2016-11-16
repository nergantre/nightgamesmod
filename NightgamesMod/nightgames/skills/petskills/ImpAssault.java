package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingTrait;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.skills.damage.DamageType;
import nightgames.status.Stsflag;

public class ImpAssault extends SimpleEnemySkill {
    public ImpAssault(Character self) {
        super("Imp Assault", self);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.positioning);
        addTag(SkillTag.hurt);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && target.stunned() && target.is(Stsflag.braced);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            int m = 8 + getSelf().getLevel() + Global.random(5);
            if (target.hasBalls()) {
                if (target.has(Trait.achilles) && !target.has(ClothingTrait.armored)) {
                    m += 8;
                }
                c.write(getSelf(), Global.format("While {other:name-possessive} attention is focused on {self:possessive} master, "
                                + "{self:subject} creeps close to {other:direct-object} and uppercuts {other:direct-object} in the balls.", 
                                getSelf(), target));
                target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, m));
                target.emote(Emotion.nervous, 10);
                target.emote(Emotion.angry, 10);
            } else {
                c.write(getSelf(), Global.format("{self:SUBJECT} runs up to {other:name-do} and punches {other:direct-object} in the gut.", getSelf(), target));
                target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, m));
                target.emote(Emotion.nervous, 10);
                target.emote(Emotion.angry, 10);
            }
        } else {
            c.write(getSelf(), String.format("%s tries to kick %s but %s %s %s small legs before they reach %s.",
                            getSelf().subject(), target.nameDirectObject(),
                            target.pronoun(), target.action("catch", "catches"),
                            getSelf().possessivePronoun(),
                            target.directObject()));
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ImpAssault(user);
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
