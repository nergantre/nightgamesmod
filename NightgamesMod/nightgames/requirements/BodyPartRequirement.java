package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Returns true if character self has at least one of the bodypart.
 */
public class BodyPartRequirement extends BaseRequirement {
    private final String type;

    public BodyPartRequirement(String type) {
        this.type = type;
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return self.body.has(type);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BodyPartRequirement that = (BodyPartRequirement) o;

        return type.equals(that.type);

    }

    @Override public int hashCode() {
        return super.hashCode() * 31 + type.hashCode();
    }
}
