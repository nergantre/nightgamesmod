package nightgames.modifier.item;

import nightgames.items.Item;

public class FlagOnlyModifier extends ItemModifier {

	@Override
	public boolean itemIsBanned(Item i) {
		return i != Item.Flag;
	}

	@Override
	public String toString() {
		return "flag-only";
	}

}
