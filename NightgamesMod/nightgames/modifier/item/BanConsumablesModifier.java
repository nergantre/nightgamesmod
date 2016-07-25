package nightgames.modifier.item;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.google.gson.JsonObject;

import nightgames.items.Item;
import nightgames.modifier.ModifierComponentLoader;

public class BanConsumablesModifier extends ItemModifier implements ModifierComponentLoader<ItemModifier> {
    private static final String name = "ban-consumables";

    static final Set<Item> CONSUMABLES;

    static {
        Set<Item> banned = EnumSet.allOf(Item.class);
        banned.removeAll(BanToysModifier.TOYS);
        banned.removeIf(i -> i.getName().contains("Trophy"));
        CONSUMABLES = Collections.unmodifiableSet(banned);
    }

    @Override
    public Set<Item> bannedItems() {
        return CONSUMABLES;
    }

    @Override
    public String name() {
        return name;
    }

    @Override public BanConsumablesModifier instance(JsonObject object) {
        return new BanConsumablesModifier();
    }

    @Override
    public String toString() {
        return name;
    }
}
