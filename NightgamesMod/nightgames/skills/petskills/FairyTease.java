package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.skills.damage.DamageType;

public class FairyTease extends SimpleEnemySkill {
    public FairyTease(Character self) {
        super("Fairy Tease", self);
        addTag(SkillTag.pleasure);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return super.requirements(c, user, target) && gendersMatch(target);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            int m = Global.random(5, 11) + getSelf().getLevel() / 2;
            if (target.crotchAvailable() && !c.getStance().penisInserted(target) && target.hasDick()) {
                c.write(getSelf(), Global.format("{self:SUBJECT} hugs {other:name-possessive} dick and rubs it with "
                                    + "{self:possessive} entire body until {other:pronoun-action:pull|pulls} {self:direct-object} off.",
                                    getSelf(), target));
                m += 5;
                target.body.pleasure(getSelf(), getSelf().body.getRandom("skin"), target.body.getRandomCock(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
            } else if (target.hasDick() && !c.getStance().penisInserted(target) ) {
                c.write(getSelf(), Global.format("{self:SUBJECT} slips into {other:name-possessive} %s and plays with "
                                + "{other:possessive} penis until {other:pronoun-action:manage|manages} to remove {self:direct-object}.",
                                getSelf(), target, target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()));
                target.body.pleasure(getSelf(), getSelf().body.getRandom("skin"), target.body.getRandomCock(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
            } else if (target.breastsAvailable()) {
                c.write(getSelf(), Global.format("{self:SUBJECT} hugs {other:name-possessive} chest and rubs {other:possessive} nipples with "
                                + "{self:possessive} entire body until {other:pronoun-action:pull|pulls} {self:direct-object} off.",
                                getSelf(), target));
                m += 5;
                target.body.pleasure(getSelf(), getSelf().body.getRandom("skin"), target.body.getRandomBreasts(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
            } else {
                c.write(getSelf(), Global.format("{self:SUBJECT} slips into {other:name-possessive} %s and plays with "
                                + "{other:possessive} sensitive nipples until {other:pronoun-action:manage|manages} to remove {self:direct-object}.",
                                getSelf(), target, target.getOutfit().getTopOfSlot(ClothingSlot.top).getName()));
                target.body.pleasure(getSelf(), getSelf().body.getRandom("skin"), target.body.getRandomBreasts(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
            }
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT} flies around the edge of the fight looking for an opening.", getSelf(), target));
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new FairyTease(user);
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
