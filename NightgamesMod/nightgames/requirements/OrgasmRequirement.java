package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if character self has had at least a certain number of orgasms in the current combat.
 */
public class OrgasmRequirement extends BaseRequirement {
    private final int number;

    public OrgasmRequirement(int number) {
        this.number = number;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.orgasms >= number;
    }
    
    public int getCount() {
        return number;
    }
}
