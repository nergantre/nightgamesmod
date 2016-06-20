package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.items.Item;
import nightgames.items.ItemAmount;

/**
 * Returns true if character self has an item
 */
public class ItemRequirement implements Requirement {
    private final ItemAmount item;

    public ItemRequirement(ItemAmount item) {
        this.item = item;
    }

    public ItemRequirement(String itemType, int amount) {
        item = new ItemAmount(Item.valueOf(itemType), amount);
    }

    @Override public String getKey() {
        return "item";
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.has(item.item, item.amount);
    }
}
