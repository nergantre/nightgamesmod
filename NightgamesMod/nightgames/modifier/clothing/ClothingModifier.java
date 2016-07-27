package nightgames.modifier.clothing;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.items.clothing.Outfit;
import nightgames.modifier.ModifierCategory;
import nightgames.modifier.ModifierComponent;

public abstract class ClothingModifier implements ModifierCategory<ClothingModifier>, ModifierComponent {
    protected static final Set<Integer> ALL_LAYERS = Collections
                    .unmodifiableSet(IntStream.range(0, Clothing.N_LAYERS).boxed().collect(Collectors.toSet()));
    protected static final Set<ClothingSlot> ALL_SLOTS = Collections.unmodifiableSet(EnumSet.allOf(ClothingSlot.class));
    protected static final Map<ClothingSlot, Set<Integer>> ALL_SLOT_LAYER_COMBOS = Collections
                    .unmodifiableMap(EnumSet.allOf(ClothingSlot.class).stream().collect(Collectors.toMap(t -> t,
                                    t -> IntStream.range(0, Clothing.N_LAYERS).boxed().collect(Collectors.toSet()))));

    public static final ClothingModifierLoader loader = new ClothingModifierLoader();
    public static final ClothingModifierCombiner combiner = new ClothingModifierCombiner();

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

    public Set<ClothingTrait> forbiddenClothingTraits() {
        return Collections.emptySet();
    }

    public boolean playerOnly() {
        return true;
    }

    public void apply(Outfit outfit) {
        Set<Clothing> equipped = new HashSet<>(outfit.getEquipped());
        equipped.forEach(outfit::unequip);

        // remove disallowed articles
        equipped.removeIf(c -> forbiddenItems().contains(c.getName()));

        // remove disallowed layers
        equipped.removeIf(c -> !allowedLayers().contains(c.getLayer()));

        // remove disallowed slots
        equipped.removeIf(c -> c.getSlots().stream().anyMatch(s -> !allowedSlots().contains(s)));

        // remove disallowed combinations
        equipped.removeIf(c -> !allowedSlotLayerCombos().entrySet().stream()
                        .allMatch(e -> !c.getSlots().contains(e.getKey()) || e.getValue().contains(c.getLayer())));

        // remove disallowed attributes
        equipped.removeIf(c -> forbiddenClothingTraits().stream().anyMatch(t -> c.attributes().contains(t)));

        // add forced items, first remove same slots
        equipped.removeIf(c -> forcedItems().stream().map(Clothing::getByID)
                        .anyMatch(c2 -> c2.getSlots().stream().anyMatch(s -> c.getSlots().contains(s))
                                        && c2.getLayer() == c.getLayer()));
        forcedItems().stream().map(Clothing::getByID).forEach(equipped::add);

        equipped.forEach(outfit::equip);
    }

    @Override public ClothingModifier combine(ClothingModifier next) {
        ClothingModifier first = this;
        return new ClothingModifier() {
            @Override public Set<Integer> allowedLayers() {
                // allows only layers allowed by both modifiers
                Set<Integer> layers = new HashSet<>(first.allowedLayers());
                layers.retainAll(next.allowedLayers());
                return layers;
            }

            @Override public Set<ClothingSlot> allowedSlots() {
                // allows only slots allowed by both modifiers
                Set<ClothingSlot> slots = new HashSet<>(first.allowedSlots());
                slots.retainAll(next.allowedSlots());
                return slots;
            }

            @Override public Map<ClothingSlot, Set<Integer>> allowedSlotLayerCombos() {
                // merges allowed combos on a layer-by-layer basis
                // {ClothingSlot.bottom: [0, 1], ClothingSlot.top: [0,1,2]}
                // combined with
                // {ClothingSlot.top: [0, 2], ClothingSlot.feet: [0,1]}
                // becomes
                // {ClothingSlot.top: [0, 2]
                Map<ClothingSlot, Set<Integer>> allowedCombos = new HashMap<>();
                Set<ClothingSlot> slots = first.allowedSlotLayerCombos().keySet();
                slots.retainAll(next.allowedSlotLayerCombos().keySet());
                for (ClothingSlot slot : slots) {
                    Set<Integer> layers = first.allowedSlotLayerCombos().get(slot);
                    layers.retainAll(next.allowedSlotLayerCombos().get(slot));
                    allowedCombos.put(slot, layers);
                }
                return allowedCombos;
            }

            @Override public Set<ClothingTrait> forbiddenClothingTraits() {
                // forbids clothing traits forbidden by either modifier
                Set<ClothingTrait> forbiddenTraits = new HashSet<>(first.forbiddenClothingTraits());
                forbiddenTraits.addAll(next.forbiddenClothingTraits());
                return forbiddenTraits;
            }

            @Override public Set<String> forbiddenItems() {
                // forbids items forbidden by either modifier
                Set<String> forbiddenItems = new HashSet<>(first.forbiddenItems());
                forbiddenItems.addAll(next.forbiddenItems());
                return forbiddenItems;
            }

            @Override public Set<String> forcedItems() {
                // forces items forced by either modifier
                // For items worn in the same clothing slot, which item will apply is arbitrary.
                Set<String> forcedItems = new HashSet<>(first.forcedItems());
                forcedItems.addAll(next.forcedItems());
                return forcedItems;
            }

            @Override public void apply(Outfit outfit) {
                first.apply(outfit);
                next.apply(outfit);
            }

            @Override public String toString() {
                return first.toString() + ", " + next.toString();
            }

            @Override public String name() {
                return first.name() + " with " + next.name();
            }
        };
    }

    @Override
    public abstract String toString();

    public static void main(String[] args) {
        Clothing.buildClothingTable();
        ClothingModifierCombiner combiner = new ClothingModifierCombiner();

        Outfit test1 = new Outfit();
        test1.equip(Clothing.getByID("bra"));
        test1.equip(Clothing.getByID("panties"));
        test1.equip(Clothing.getByID("jeans"));
        test1.equip(Clothing.getByID("shirt"));

        Outfit test2 = new Outfit(test1);
        Outfit test3 = new Outfit(test1);
        Outfit test4 = new Outfit(test1);
        Outfit test5 = new Outfit(test1);

        combiner.nullModifier().apply(test1);
        System.out.println(test1);

        new UnderwearOnlyModifier().apply(test2);
        System.out.println(test2);

        new NoPantiesModifier().apply(test3);
        System.out.println(test3);

        new NoPantiesModifier().combine(new ForceClothingModifier("blouse", "thong")).apply(test4);
        System.out.println(test4);

        Stream.of((ClothingModifier) new UnderwearOnlyModifier(), new ForceClothingModifier("blouse", "thong"),
                        new NoPantiesModifier()).reduce(combiner.template(), combiner::combine).apply(test5);
        System.out.println(test5);
    }
}
