package nightgames.modifier.item;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import nightgames.items.Item;

public class BanConsumablesModifier extends ItemModifier {

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
}
