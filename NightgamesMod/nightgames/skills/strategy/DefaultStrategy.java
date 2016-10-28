package nightgames.skills.strategy;

import java.util.Set;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;

public class DefaultStrategy extends AbstractStrategy {
    @Override
    public double weight(Combat c, Character self) {
        double weight = 5;
        return weight;
    }

    @Override
    protected Set<Skill> filterSkills(Combat c, Character self, Set<Skill> allowedSkills) {
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