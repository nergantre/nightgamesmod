package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class SpecificBodyPartRequirement implements CustomRequirement {
    private BodyPart part;

    public SpecificBodyPartRequirement(BodyPart part) {
        this.part = part;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.body.contains(part);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SpecificBodyPartRequirement that = (SpecificBodyPartRequirement) o;

        return part.equals(that.part);

    }

    @Override public int hashCode() {
        return part.hashCode();
    }
}
