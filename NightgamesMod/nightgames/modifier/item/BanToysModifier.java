package nightgames.modifier.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonObject;

import nightgames.items.Item;
import nightgames.modifier.ModifierComponentLoader;

public class BanToysModifier extends ItemModifier implements ModifierComponentLoader<ItemModifier> {
    private static final String name = "ban-toys";

    static final Set<Item> TOYS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Item.Dildo, Item.Dildo2,
                    Item.Onahole, Item.Onahole2, Item.Tickler, Item.Tickler2, Item.Crop, Item.Crop2)));

    @Override
    public Set<Item> bannedItems() {
        return TOYS;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public BanToysModifier instance(JsonObject object) {
        return new BanToysModifier();
    }

    @Override
    public String toString() {
        return name;
    }

}
