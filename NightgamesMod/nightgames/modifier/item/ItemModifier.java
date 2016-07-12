package nightgames.modifier.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nightgames.characters.Character;
import nightgames.items.Item;
import nightgames.modifier.ModifierCategory;
import nightgames.modifier.ModifierComponent;
import nightgames.modifier.ModifierComponentLoader;

public abstract class ItemModifier implements ModifierCategory<ItemModifier>, ModifierComponent {
    public static final ItemModifierLoader loader = new ItemModifierLoader();
    public static final ItemModifierCombiner combiner = new ItemModifierCombiner();


    public Set<Item> bannedItems() {
        return Collections.emptySet();
    }

    public Map<Item, Integer> ensuredItems() {
        return Collections.emptyMap();
    }

    public boolean itemIsBanned(Character c, Item i) {
        return !playerOnly() || c.human() && bannedItems().contains(i);
    }

    public void giveRequiredItems(Character c) {
        ensuredItems().forEach((item, count) -> {
            while (!c.has(item, count)) {
                c.gain(item);
            }
        });
    }

    public boolean playerOnly() {
        return true;
    }

    @Override public ItemModifier combine(ItemModifier next) {
        ItemModifier first = this;
        return new ItemModifier() {
            @Override
            public void giveRequiredItems(Character c) {
                first.giveRequiredItems(c);
                next.giveRequiredItems(c);
            }

            @Override
            public boolean itemIsBanned(Character c, Item i) {
                return first.itemIsBanned(c, i) || next.itemIsBanned(c, i);
            }

            @Override
            public String toString() {
                return first.toString() + next.toString();
            }

            public String name() {
                return first.name() + " + " + next.name();
            }
        };
    }

    @Override
    public abstract String toString();
}
