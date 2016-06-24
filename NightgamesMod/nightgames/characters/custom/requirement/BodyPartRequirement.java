package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class BodyPartRequirement implements CustomRequirement {
    private String type;

    public BodyPartRequirement(String type) {
        this.type = type;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
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
        return type.hashCode();
    }
}
