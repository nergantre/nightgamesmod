package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;

public class FairyEnergize extends SimpleMasterSkill {
    public FairyEnergize(Character self) {
        super("Fairy Energize", self, 5);
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
            int m = Global.random(17, 24) + getSelf().getLevel() / 2;
            c.write(getSelf(), Global.format("{self:SUBJECT} flies around {other:name-do}, channeling energy into {other:direct-object}.", getSelf(), target));
            target.buildMojo(c, m, getSelf().getName());
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT} flies around the edge of the fight looking for an opening.", getSelf(), target));
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new FairyEnergize(user);
    }
}
