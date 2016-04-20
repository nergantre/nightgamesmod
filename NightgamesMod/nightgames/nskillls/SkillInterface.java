package nightgames.nskillls;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;;

public interface SkillInterface {
    List<SkillResult> getPossibleResults(Combat c, Character user, Character target);

    boolean isUsable(Combat c, Character user, Character target);

    boolean meetsRequirements(Combat c, Character user, Character target);
}
