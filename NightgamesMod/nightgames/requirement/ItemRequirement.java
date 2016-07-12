package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.items.Item;
import nightgames.items.ItemAmount;

/**
 * Returns true if character self has an item
 */
public class ItemRequirement extends BaseRequirement {
    private final ItemAmount item;

    public ItemRequirement(ItemAmount item) {
        this.item = item;
    }

    public ItemRequirement(String item, int amount) {
        this.item = new ItemAmount(Item.valueOf(item), amount);
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.has(item.item, item.amount);
    }
}
