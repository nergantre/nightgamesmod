package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.status.Falling;

public class SlimeTrip extends SimpleEnemySkill {
    public SlimeTrip(Character self) {
        super("Slime Trip", self);
        addTag(SkillTag.positioning);
        addTag(SkillTag.knockdown);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && !c.getStance().prone(target);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    @Override
    public int accuracy(Combat c) {
        return 50;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            c.write(getSelf(), Global.format("{other:SUBJECT-ACTION:slip|slips} on {self:name-do} as it clings to {other:possessive} feet, losing {other:possessive} balance.", 
                            getSelf(), target));
            target.add(new Falling(target));
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:stumble|stumbles} as {self:subject} clings to {other:possessive} leg. "
                            + "{other:SUBJECT-ACTION:manage|manages} to catch {other:reflective} and {other:action:scrape|scrapes} off the clingly blob.",
                            getSelf(), target));
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new SlimeTrip(user);
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
