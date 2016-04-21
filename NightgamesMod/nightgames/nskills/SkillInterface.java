package nightgames.nskills;

import nightgames.combat.Combat;

import java.util.List;

import nightgames.characters.Character;;

public interface SkillInterface {
    List<SkillResult> getPossibleResults(Combat c, Character user, Character target, int roll);

    boolean isSelectable(Combat c, Character user, Character target);

    String getName();
}
