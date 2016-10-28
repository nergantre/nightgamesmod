package nightgames.skills.strategy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;

public class DefaultStrategy implements CombatStrategy {
    @Override
    public double weight(Combat c, Character self) {
        double weight = 5;
        return weight;
    }

    @Override
    public Set<Skill> nextSkills(Combat c, Character self) {
        Character other = c.getOther(self);
        Set<Skill> availableSkills = new HashSet<>(self.getSkills());
        Skill.filterAllowedSkills(c, availableSkills, self, other);
        Set<Skill> allowedSkills = availableSkills.stream().filter(skill -> Skill.skillIsUsable(c, skill, other)).collect(Collectors.toSet());
        return allowedSkills;
    }
    
    @Override
    public CombatStrategy instance() {
        return new DefaultStrategy();
    }

    @Override
    public int initialDuration(Combat c, Character self) {
        return Global.random(1, 3);
    }
}