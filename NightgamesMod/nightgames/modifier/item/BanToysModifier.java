package nightgames.modifier.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nightgames.items.Item;

public class BanToysModifier extends ItemModifier {

	static final Set<Item> TOYS = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList(Item.Dildo,
					Item.Dildo2, Item.Onahole, Item.Onahole2, Item.Tickler,
					Item.Tickler2, Item.Crop, Item.Crop2)));

	@Override
	public Set<Item> bannedItems() {
		return TOYS;
	}

}
