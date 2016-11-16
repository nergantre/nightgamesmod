package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;

public class GoblinDenial extends SimpleMasterSkill {
    public GoblinDenial(Character self) {
        super("GoblinDenial", self, 5);
        addTag(SkillTag.calm);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    @Override
    public int accuracy(Combat c) {
        return 80;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            int m = Global.random(17, 24) + getSelf().getLevel() / 2;
            c.write(getSelf(), Global.format("{self:SUBJECT} suddenly appears to turn against {other:name-do} and slaps {other:direct-object} sensitive testicles. "
                            + "You're momentarily confused, but you realize the shock probably lessened some of {other:possessive} pent up desires.", getSelf(), target));
            target.pain(c, getSelf(), Global.random(15, 25));
            target.calm(c, m * 2);
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT} stays at the edge of battle and touches herself absentmindedly.", getSelf(), target));
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new GoblinDenial(user);
    }
}
