package nightgames.skills.strategy;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.skills.Skill;

public interface CombatStrategy {
    public static List<CombatStrategy> availableStrategies = Arrays.asList(new FuckStrategy());
    double weight(Combat c, Character self);
    int initialDuration(Combat c, Character self);
    Set<Skill> nextSkills(Combat c, Character self);
    CombatStrategy instance();
}
