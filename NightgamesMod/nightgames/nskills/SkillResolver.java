package nightgames.nskills;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class SkillResolver {
    public static SkillResult resolveSkill(SkillInterface skill, Combat c, Character user, Character target) {
        int roll = Global.random(1000);
        List<SkillResult> results = skill.getPossibleResults(c, user, target, roll);
        SkillResult result = results.stream().max((result1, result2) -> result1.getPriority() - result2.getPriority()).orElse(EmptySkillResult.getEmptyResult());
        return result;
    }
}
