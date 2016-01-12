package nightgames.modifier.clothing;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.items.clothing.Outfit;

public abstract class ClothingModifier {

	public static final List<ClothingModifier> TYPES = Collections.unmodifiableList(Arrays.asList(
			new ForceClothingModifier(), new NoPantiesModifier(), new NudeModifier(), new UnderwearOnlyModifier()));
	public static final ClothingModifier NULL_MODIFIER = new ClothingModifier() {
		@Override
		public String toString() {
			return "null-clothing-modifier";
		}

	};

	protected static final Set<Integer> ALL_LAYERS = Collections
			.unmodifiableSet(IntStream.range(0, Clothing.N_LAYERS).boxed().collect(Collectors.toSet()));
	protected static final Set<ClothingSlot> ALL_SLOTS = Collections.unmodifiableSet(EnumSet.allOf(ClothingSlot.class));
	protected static final Map<ClothingSlot, Set<Integer>> ALL_SLOT_LAYER_COMBOS = Collections
			.unmodifiableMap(EnumSet.allOf(ClothingSlot.class).stream().collect(Collectors.toMap(t -> t,
					t -> IntStream.range(0, Clothing.N_LAYERS).boxed().collect(Collectors.toSet()))));

	public Set<Integer> allowedLayers() {
		return ALL_LAYERS;
	}

	public Set<ClothingSlot> allowedSlots() {
		return ALL_SLOTS;
	}

	public Map<ClothingSlot, Set<Integer>> allowedSlotLayerCombos() {
		return ALL_SLOT_LAYER_COMBOS;
	}

	public Set<String> forbiddenItems() {
		return Collections.emptySet();
	}

	public Set<String> forcedItems() {
		return Collections.emptySet();
	}

	public Set<ClothingTrait> forbiddenAttributes() {
		return Collections.emptySet();
	}

	public boolean playerOnly() {
		return true;
	}

	public void apply(Outfit outfit) {
		Set<Clothing> equipped = new HashSet<>(outfit.getEquipped());
		equipped.forEach(outfit::unequip);

		// remove disalowed articles
		equipped.removeIf(c -> forbiddenItems().contains(c.getName()));

		// remove disallowed layers
		equipped.removeIf(c -> !allowedLayers().contains(c.getLayer()));

		// remove disallowed slots
		equipped.removeIf(c -> c.getSlots().stream().anyMatch(s -> !allowedSlots().contains(s)));

		// remove disallowed combinations
		equipped.removeIf(c -> !allowedSlotLayerCombos().entrySet().stream()
				.allMatch(e -> !c.getSlots().contains(e.getKey()) || e.getValue().contains(c.getLayer())));

		// remove disallowed attributes
		equipped.removeIf(c -> forbiddenAttributes().stream().anyMatch(t -> c.attributes().contains(t)));

		// add forced items, first remove same slots
		equipped.removeIf(c -> forcedItems().stream().map(Clothing::getByID).anyMatch(
				c2 -> c2.getSlots().stream().anyMatch(s -> c.getSlots().contains(s)) && c2.getLayer() == c.getLayer()));
		forcedItems().stream().map(Clothing::getByID).forEach(equipped::add);

		equipped.forEach(outfit::equip);
	}

	public ClothingModifier andThen(ClothingModifier next) {
		ClothingModifier me = this;
		return new ClothingModifier() {
			@Override
			public void apply(Outfit outfit) {
				me.apply(outfit);
				next.apply(outfit);
			}

			@Override
			public String toString() {
				return me.toString() + ", " + next.toString();
			}
		};
	}

	public static ClothingModifier forAll(ClothingModifier playerOnly) {
		return new ClothingModifier() {

			@Override
			public boolean playerOnly() {
				return false;
			}

			@Override
			public void apply(Outfit o) {
				playerOnly.apply(o);
			}

			@Override
			public String toString() {
				return playerOnly.toString();
			}
		};
	}

	public static ClothingModifier allOf(ClothingModifier... modifiers) {
		if (modifiers.length == 0) {
			return NULL_MODIFIER;
		}
		ClothingModifier result = modifiers[0];
		for (int i = 1; i < modifiers.length; i++) {
			result = result.andThen(modifiers[i]);
		}
		return result;
	}

	@Override
	public abstract String toString();

	public static void main(String[] args) {
		Clothing.buildClothingTable();
		Outfit test1 = new Outfit();
		test1.equip(Clothing.getByID("bra"));
		test1.equip(Clothing.getByID("panties"));
		test1.equip(Clothing.getByID("jeans"));
		test1.equip(Clothing.getByID("shirt"));

		Outfit test2 = new Outfit(test1);
		Outfit test3 = new Outfit(test1);
		Outfit test4 = new Outfit(test1);
		Outfit test5 = new Outfit(test1);

		NULL_MODIFIER.apply(test1);
		System.out.println(test1);

		new UnderwearOnlyModifier().apply(test2);
		System.out.println(test2);

		new NoPantiesModifier().apply(test3);
		System.out.println(test3);

		new NoPantiesModifier().andThen(new ForceClothingModifier("blouse", "thong")).apply(test4);
		System.out.println(test4);

		allOf(forAll(new UnderwearOnlyModifier()), new ForceClothingModifier("blouse", "thong"),
				new NoPantiesModifier()).apply(test5);
		System.out.println(test5);
	}
}
