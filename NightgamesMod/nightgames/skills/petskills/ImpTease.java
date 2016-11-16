package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.skills.damage.DamageType;

public class ImpTease extends SimpleEnemySkill {
    public ImpTease(Character self) {
        super("Imp Tease", self);
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
                c.write(getSelf(), Global.format("{self:SUBJECT} jumps onto {other:name-do} and humps at {other:possessive} dick before "
                                + "{other:subject-action:can pull|can pull} {self:direct-object} off.",
                                    getSelf(), target));
                target.body.pleasure(getSelf(), getSelf().body.getRandomPussy(), target.body.getRandomCock(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
                return true;
            } else if (target.hasPussy() && !c.getStance().vaginallyPenetrated(c, target) && target.crotchAvailable() && getSelf().hasDick()) {
                c.write(getSelf(), Global.format("{self:SUBJECT-ACTION} latches onto {other:name-do} and shoves {self:possessive} thick cock into {other:possessive} pussy. As the demon humps {other:direct-object}, {other:SUBJECT-ACTION:yell|shrieks} and punches {self:direct-object} away.",
                                getSelf(), target));
                target.body.pleasure(getSelf(), getSelf().body.getRandomCock(), target.body.getRandomPussy(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
                return true;
            } else if (target.breastsAvailable()) {
                c.write(getSelf(), Global.format("{self:SUBJECT} jumps up and hugs {other:name-possessive} chest and licks {other:possessive} nipples with "
                                + "{self:possessive} longer than average tongue until {other:pronoun-action:pull|pulls} {self:direct-object} off.",
                                getSelf(), target));
                m += 5;
                target.body.pleasure(getSelf(), getSelf().body.getRandom("mouth"), target.body.getRandomBreasts(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
                return true;
            }
        }
        c.write(getSelf(), Global.format("{self:SUBJECT} stands at the periphery of the fight, touching {self:reflective} idly.", getSelf(), target));
        return false;
    }

    @Override
    public Skill copy(Character user) {
        return new ImpTease(user);
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
