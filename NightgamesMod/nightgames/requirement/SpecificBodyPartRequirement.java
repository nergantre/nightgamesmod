package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

/**
 * Returns true if character self has a particular body part.
 *
 * This is a more specific requirement than BodyPartRequirement.
 */
public class SpecificBodyPartRequirement extends BaseRequirement {
    private final BodyPart part;

    public SpecificBodyPartRequirement(BodyPart part) {
        this.part = part;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.body.contains(part);
    }
}
