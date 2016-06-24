package nightgames.characters.custom.requirement;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public class AttributeRequirement implements CustomRequirement {
    Attribute att;
    int amount;

    public AttributeRequirement(Attribute att, int amount) {
        this.att = att;
        this.amount = amount;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.get(att) >= amount;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AttributeRequirement that = (AttributeRequirement) o;

        if (amount != that.amount)
            return false;
        return att == that.att;

    }

    @Override public int hashCode() {
        int result = att.hashCode();
        result = 31 * result + amount;
        return result;
    }
}
