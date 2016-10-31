package nightgames.skills.strategy;

import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;

public class UseToyStrategy extends KnockdownThenActionStrategy {
    @Override
    public double weight(Combat c, Character self) {
        double weight = 1;
        return weight;
    }

    @Override
    protected Set<Skill> getPreferredSkills(Combat c, Character self, Set<Skill> allowedSkills) {
        Set<Skill> gadgetSkills = allowedSkills.stream()
                        .filter(skill -> (skill.getTags().contains(SkillTag.usesToy)
                                        || skill.getTags().contains(SkillTag.stripping))
                                        && !skill.getTags().contains(SkillTag.suicidal))
                        .collect(Collectors.toSet());

        return gadgetSkills;
    }
    
    @Override
    public CombatStrategy instance() {
        return new UseToyStrategy();
    }

    @Override
    public int initialDuration(Combat c, Character self) {
        return Global.random(2, 6);
    }
}