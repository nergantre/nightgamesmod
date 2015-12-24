package nightgames.modifier.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

import nightgames.items.Item;
import nightgames.modifier.ModifierComponent;

public class BanToysModifier extends ItemModifier implements ModifierComponent<BanToysModifier> {

	static final Set<Item> TOYS = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList(Item.Dildo,
					Item.Dildo2, Item.Onahole, Item.Onahole2, Item.Tickler,
					Item.Tickler2, Item.Crop, Item.Crop2)));

	@Override
	public Set<Item> bannedItems() {
		return TOYS;
	}

	@Override
	public String name() {
		return "ban-toys";
	}

	@Override
	public BanToysModifier instance(JSONObject obj) {
		return new BanToysModifier();
	}
	
	@Override
	public String toString() {
		return name();
	}

}
