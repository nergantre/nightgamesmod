package nightgames.nskills;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;;

public interface SkillInterface {
    List<SkillResult> getPossibleResults(Combat c, Character user, Character target, int roll);

    boolean isSelectable(Combat c, Character user, Character target);

    String getName();
}
