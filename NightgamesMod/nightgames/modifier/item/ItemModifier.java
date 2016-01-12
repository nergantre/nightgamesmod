package nightgames.modifier.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nightgames.characters.Character;
import nightgames.items.Item;

public abstract class ItemModifier {

	public static final List<ItemModifier> TYPES = Collections
			.unmodifiableList(Arrays.asList(new BanToysModifier(), new BanConsumablesModifier()));

	public static final ItemModifier NULL_MODIFIER = new ItemModifier() {
		@Override
		public String toString() {
			return "null-item-modifier";
		}
	};

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

	public static ItemModifier forAll(ItemModifier mod) {
		return new ItemModifier() {
			@Override
			public boolean playerOnly() {
				return false;
			}

			@Override
			public void giveRequiredItems(Character c) {
				mod.giveRequiredItems(c);
			}

			@Override
			public boolean itemIsBanned(Character c, Item i) {
				return mod.itemIsBanned(c, i);
			}

			@Override
			public String toString() {
				return mod.toString();
			}
		};
	}

	public ItemModifier combineWith(ItemModifier other) {
		ItemModifier me = this;
		return new ItemModifier() {
			@Override
			public void giveRequiredItems(Character c) {
				me.giveRequiredItems(c);
				other.giveRequiredItems(c);
			}

			@Override
			public boolean itemIsBanned(Character c, Item i) {
				return me.itemIsBanned(c, i) || other.itemIsBanned(c, i);
			}

			@Override
			public String toString() {
				return me.toString() + other.toString();
			}
		};
	}

	public static ItemModifier allOf(ItemModifier... mods) {
		if (mods.length == 0) {
			return NULL_MODIFIER;
		}
		ItemModifier result = mods[0];
		for (int i = 1; i < mods.length; i++) {
			result = result.combineWith(mods[i]);
		}
		return result;
	}

	@Override
	public abstract String toString();
}
