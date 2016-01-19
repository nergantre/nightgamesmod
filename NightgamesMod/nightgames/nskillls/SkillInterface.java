package nightgames.nskillls;

import nightgames.combat.Combat;

import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.custom.requirement.AttributeRequirement;;

public interface SkillInterface {
    List<SkillResult> getPossibleResults(Combat c, Character user, Character target);

    boolean isUsable(Combat c, Character user, Character target);

    boolean meetsRequirements(Combat c, Character user, Character target);
}
