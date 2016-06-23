package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if character self has at least one of the bodypart.
 */
public class BodyPartRequirement implements Requirement {
    private final String type;

    public BodyPartRequirement(String type) {
        this.type = type;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.body.has(type);
    }
}
