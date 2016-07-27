package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.items.ItemAmount;

/**
 * Returns true if character self has an item
 */
public class ItemRequirement extends BaseRequirement {
    private final ItemAmount item;

    public ItemRequirement(ItemAmount item) {
        this.item = item;
    }

    public ItemRequirement(String name, int amount) {
        this.item = new ItemAmount(name, amount);
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return self.has(item.item, item.amount);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        ItemRequirement that = (ItemRequirement) o;

        return item.equals(that.item);

    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + item.hashCode();
        return result;
    }
}
