package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.skills.damage.DamageType;

public class SlimeJob extends SimpleEnemySkill {
    public SlimeJob(Character self) {
        super("Slime Job", self);
        addTag(SkillTag.pleasure);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return super.requirements(c, user, target);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            int m = Global.random(5, 11) + getSelf().getLevel() / 2;
            if (target.crotchAvailable() && !c.getStance().penisInserted(target) && target.hasDick()) {
                c.write(getSelf(), Global.format("{self:SUBJECT} forms into a humanoid shape and grabs {other:name-possessive} dick. "
                                + "A slimy vagina forms around {other:possessive} penis and rubs {other:direct-object} with a slippery pleasure.",
                                    getSelf(), target));
                target.body.pleasure(getSelf(), getSelf().body.getRandomPussy(), target.body.getRandomCock(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
                return true;
            } else if (target.hasPussy() && !c.getStance().vaginallyPenetrated(c, target) && target.crotchAvailable() && getSelf().hasDick()) {
                c.write(getSelf(), Global.format("Two long appendages extend from {self:name-do} and wrap around {other:name-possessive} legs. "
                                + "A third, phallic shaped appendage forms and penetrates {other:possessive} "
                                + "pussy. {self:PRONOUN} stifles a moan as the slimy phallus thrusts in and out of {other:direct-object}.",
                                getSelf(), target));
                target.body.pleasure(getSelf(), getSelf().body.getRandomCock(), target.body.getRandomPussy(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
                return true;
            } else if (target.breastsAvailable()) {
                c.write(getSelf(), Global.format("{self:SUBJECT} grows two long slimy appendages which rises up and tweaks {other:name-possessive} "
                                + "sensitive nipples.",
                                getSelf(), target));
                target.body.pleasure(getSelf(), getSelf().body.getRandom("tentacles"), target.body.getRandomBreasts(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
                return true;
            }
        }
        c.write(getSelf(), Global.format("You see eyes form in {self:name-do} as it watches the fight curiously.", getSelf(), target));
        return false;
    }

    @Override
    public Skill copy(Character user) {
        return new SlimeJob(user);
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
