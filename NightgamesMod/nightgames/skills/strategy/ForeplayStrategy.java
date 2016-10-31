package nightgames.skills.strategy;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class ForeplayStrategy extends KnockdownThenActionStrategy {
    @Override
    public double weight(Combat c, Character self) {
        double weight = 1;
        return weight;
    }

    @Override
    protected Set<Skill> getPreferredAfterKnockdownSkills(Combat c, Character self, Set<Skill> allowedSkills) {
        if (c.getStance().havingSex()) {
            return Collections.emptySet();
        }
        Set<Skill> foreplaySkills = allowedSkills.stream().filter(skill -> Tactics.pleasure.equals(skill.type(c)) || skill.getTags().contains(SkillTag.stripping)).collect(Collectors.toSet());
        return foreplaySkills;
    }

    @Override
    public CombatStrategy instance() {
        return new ForeplayStrategy();
    }
    
    @Override
    public int initialDuration(Combat c, Character self) {
        return Global.random(2, 6);
    }
}