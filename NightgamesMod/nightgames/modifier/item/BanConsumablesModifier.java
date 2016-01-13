package nightgames.modifier.item;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.json.simple.JSONObject;

import nightgames.items.Item;
import nightgames.modifier.ModifierComponent;

public class BanConsumablesModifier extends ItemModifier implements ModifierComponent<BanConsumablesModifier> {

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
        return "ban-consumables";
    }

    @Override
    public BanConsumablesModifier instance(JSONObject obj) {
        return new BanConsumablesModifier();
    }

    @Override
    public String toString() {
        return name();
    }
}
