package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;

public class FairyHeal extends SimpleMasterSkill {
    public FairyHeal(Character self) {
        super("Fairy Heal", self);
        addTag(SkillTag.heal);
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
            int m = Global.random(7, 14) + getSelf().getLevel();
            c.write(getSelf(), Global.format("{self:SUBJECT} flies around {other:name-do}, rains magic energy on {other:direct-object}, restoring {other:possessive} strength.", getSelf(), target));
            target.heal(c, m);
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT} flies around the edge of the fight looking for an opening to heal {self:possessive} master.", getSelf(), target));
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new FairyHeal(user);
    }
}
