package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;

/**
 * Returns true if the combat is in a certain state.
 */
public class ResultRequirement extends BaseRequirement {
    private final Result result;

    public ResultRequirement(Result result) {
        this.result = result;
    }

    public ResultRequirement(String result) {
        this(Result.valueOf(result));
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return c != null && c.state == result;
    }
}
