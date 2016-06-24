package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.items.ItemAmount;

public class ItemRequirement implements CustomRequirement {
    private ItemAmount type;

    public ItemRequirement(ItemAmount type) {
        this.type = type;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.has(type.item, type.amount);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ItemRequirement that = (ItemRequirement) o;

        return type.equals(that.type);

    }

    @Override public int hashCode() {
        return type.hashCode();
    }
}
