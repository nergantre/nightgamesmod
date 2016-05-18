package nightgames.nskills;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.combat.Combat;;

public interface SkillInterface {
    Optional<SkillResult> getHighestPriorityUsableResult(Combat c, Character user, Character target);

    String getName();
    
    boolean resolve(Combat c, Character user, Character target);
}
