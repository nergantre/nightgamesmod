package nightgames.nskillls;

import java.util.ArrayList;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class SkillResolver {
    public static SkillResult resolveSkill(SkillInterface skill, Combat c, Character user, Character target) {
        List<SkillResult> results = skill.getPossibleResults(c, user, target);
        if (results.isEmpty()) {
            return EmptySkillResult.getEmptyResult();
        }
        List<Double> weights = new ArrayList<>(results.size());
        weights.add(results.get(0).getWeight(c, user, target));
        for (int i = 1; i < results.size(); i++) {
            weights.add(results.get(i).getWeight(c, user, target) + weights.get(i - 1));
        }
        double totalWeight = weights.get(weights.size() - 1);
        double roll = Global.randomdouble() * totalWeight;
        for (int i = 0; i < weights.size(); i++) {
            if (roll >= weights.get(i)) {
                return results.get(i);
            }
        }
        return results.get(results.size());
    }
}
