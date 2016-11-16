package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.status.Shield;

public class FairyShield extends SimpleMasterSkill {
    public FairyShield(Character self) {
        super("Fairy Shield", self, 10);
        addTag(SkillTag.buff);
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
            int duration = 3 + getSelf().getLevel() / 10;
            c.write(getSelf(), Global.format("{self:SUBJECT} raises a shield around {other:name-do}, preventing attacks!", getSelf(), target));
            target.add(new Shield(target, .5, duration));
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT} flies around the edge of the fight looking for an opening.", getSelf(), target));
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new FairyShield(user);
    }
}
