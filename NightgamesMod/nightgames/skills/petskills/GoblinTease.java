package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.characters.body.ToysPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.skills.damage.DamageType;

public class GoblinTease extends SimpleEnemySkill {
    public GoblinTease(Character self) {
        super("Goblin Tease", self);
        addTag(SkillTag.pleasure);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && c.getStance().prone(target);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            int m = Global.random(5, 11) + getSelf().getLevel() / 2;
            if (target.crotchAvailable() && !c.getStance().penisInserted(target) && target.hasDick()) {
                c.write(getSelf(), Global.format("{self:SUBJECT} steps over {other:name-possessive} dick and starts massaging it with "
                                + "{self:possessive} latex-covered foot.",
                                    getSelf(), target));
                target.body.pleasure(getSelf(), getSelf().body.getRandom("feet"), target.body.getRandomCock(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
            } else if (target.hasPussy() && target.crotchAvailable() && !c.getStance().vaginallyPenetrated(c, target)) {
                c.write(getSelf(), Global.format("{self:SUBJECT} pulls the humming vibrator our of {self:possessive} wet hole and "
                                + "thrusts it between {other:name-possessive} legs.",
                                getSelf(), target));
                target.body.pleasure(getSelf(), ToysPart.dildo, target.body.getRandomPussy(),
                                getSelf().modifyDamage(DamageType.pleasure, target, m), c);
            } else if (target.crotchAvailable() && !c.getStance().anallyPenetrated(c, target)) {
                if (Global.random(2) == 0) {
                    c.write(getSelf(), Global.format("{other:SUBJECT-ACTION:jump|jumps} in surprise as {other:pronoun} suddenly feel something solid penetrating {other:possessive} asshole. "
                                    + "{self:SUBJECT} got behind {other:direct-object} during the fight and delivered a sneak attack with an anal dildo. Before {other:pronoun} can retaliate "
                                    + "the goblin withdraws the toy and retreats to safety.",
                                    getSelf(), target));
                    target.body.pleasure(getSelf(), ToysPart.dildo, target.body.getRandomAss(),
                                    getSelf().modifyDamage(DamageType.pleasure, target, m), c);
                } else {
                    c.write(getSelf(), Global.format("{self:SUBJECT} takes advantage of {other:name-possessive} helplessness and positions {self:reflective} behind {other:direct-object}. "
                                    + "{self:PRONOUN} produces a string on anal beads and proceeds to insert them one bead at a time into {other:possessive} anus. "
                                    + "{self:PRONOUN} manages to get five beads in while {other:subject-action:are|is} unable to defend {other:reflective}. When {self:pronoun} "
                                    + "pulls them out, {other:subject-action:feel|feels} like they're turning {other:direct-object} inside out.",
                                    getSelf(), target));
                    target.body.pleasure(getSelf(), ToysPart.analbeads, target.body.getRandomAss(),
                                    getSelf().modifyDamage(DamageType.pleasure, target, m * 1.5), c);
                }
            }
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT} stays at the edge of battle and touches herself absentmindedly.", getSelf(), target));
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new GoblinTease(user);
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
