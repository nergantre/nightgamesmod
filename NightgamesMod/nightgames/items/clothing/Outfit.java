package nightgames.items.clothing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.DebugFlags;
import nightgames.global.Global;

public class Outfit {
    private Map<ClothingSlot, List<Clothing>> outfit;
    private Set<Clothing> equipped;

    public Outfit() {
        outfit = new EnumMap<>(ClothingSlot.class);
        equipped = new HashSet<>();
        Arrays.stream(ClothingSlot.values()).forEach(slot -> {
            List<Clothing> list = new ArrayList<>(Clothing.N_LAYERS);
            outfit.put(slot, list);
            IntStream.range(0, Clothing.N_LAYERS).forEach(i -> list.add(null));
        });
    }

    public Outfit(Outfit other) {
        outfit = new EnumMap<>(ClothingSlot.class);
        Arrays.stream(ClothingSlot.values()).forEach(slot -> outfit.put(slot, new ArrayList<>(other.outfit.get(slot))));
        equipped = new HashSet<>(other.equipped);
    }

    /* public information api */
    public Collection<Clothing> getAll() {
        return outfit.values().stream().flatMap(List::stream).filter(c -> c != null).collect(Collectors.toSet());
    }
    
    public boolean slotOpen(ClothingSlot slot) {
        return outfit.get(slot).isEmpty() || !outfit.get(slot).stream()
                        .anyMatch(article -> article != null && !article.is(ClothingTrait.open));
    }

    public boolean slotEmpty(ClothingSlot slot) {
        return outfit.get(slot).isEmpty() || !outfit.get(slot).stream().anyMatch(article -> article != null);
    }

    public boolean slotUnshreddable(ClothingSlot slot) {
        return slotEmpty(slot) || getTopOfSlot(slot).is(ClothingTrait.indestructible);
    }

    public boolean slotEmptyOrMeetsCondition(ClothingSlot slot, Predicate<? super Clothing> condition) {
        Stream<Clothing> clothes = outfit.get(slot).stream().filter(article -> article != null);
        List<Clothing> clothesList = clothes.collect(Collectors.toList());
        return clothesList.stream().allMatch(condition);
    }

    public Clothing getTopOfSlot(ClothingSlot slot) {
        return getTopOfSlot(slot, Clothing.N_LAYERS - 1);
    }

    public Clothing getTopOfSlot(ClothingSlot slot, int highestLayer) {
        List<Clothing> list = outfit.get(slot);
        if (list == null) {
            return null;
        }
        for (int i = highestLayer; i >= 0; i--) {
            if (list.get(i) != null) {
                return list.get(i);
            }
        }
        return null;
    }

    public List<Clothing> getAllStrippable() {
        return Arrays.stream(ClothingSlot.values()).map(this::getTopOfSlot).filter(article -> article != null)
                        .distinct().collect(Collectors.toList());
    }

    public Clothing getBottomOfSlot(ClothingSlot slot) {
        List<Clothing> list = outfit.get(slot);
        if (list == null) {
            return null;
        }
        for (int i = 0; i < Clothing.N_LAYERS; i++) {
            if (list.get(i) != null) {
                return list.get(i);
            }
        }
        return null;
    }

    public Clothing getSlotAt(ClothingSlot slot, int layer) {
        return outfit.get(slot).get(layer);
    }

    public ClothingSlot getRandomNakedSlot() {
        List<ClothingSlot> slotsAvailable = Arrays.stream(ClothingSlot.values()).filter(slot -> !slotOpen(slot))
                        .collect(Collectors.toList());
        if (slotsAvailable.isEmpty()) {
            return null;
        }
        Collections.shuffle(slotsAvailable);
        return slotsAvailable.get(0);
    }

    public ClothingSlot getRandomShreddableSlot() {
        List<ClothingSlot> slotsAvailable = Arrays.stream(ClothingSlot.values()).filter(slot -> !slotUnshreddable(slot))
                        .collect(Collectors.toList());
        if (slotsAvailable.isEmpty()) {
            return null;
        }
        Collections.shuffle(slotsAvailable);
        return slotsAvailable.get(0);
    }

    public ClothingSlot getRandomEquippedSlot() {
        List<ClothingSlot> slotsAvailable = Arrays.stream(ClothingSlot.values()).filter(slot -> !slotEmpty(slot))
                        .collect(Collectors.toList());
        if (slotsAvailable.isEmpty()) {
            return null;
        }
        Collections.shuffle(slotsAvailable);
        return slotsAvailable.get(0);
    }

    public boolean has(Trait t) {
        return equipped.stream().anyMatch(article -> article.buffs(t));
    }

    public boolean has(ClothingTrait attribute) {
        return equipped.stream().anyMatch(article -> article.is(attribute));
    }

    // mutator apis below
    public void change(List<Clothing> plan) {
        undress();
        plan.forEach(this::equip);
    }

    public List<Clothing> undress() {
        List<Clothing> copy = new ArrayList<>(equipped);
        return copy.stream().map(this::unequip).collect(Collectors.toList());
    }

    public List<Clothing> undressOnly(Predicate<? super Clothing> requirement) {
        List<Clothing> copy = new ArrayList<>(equipped);
        return copy.stream().filter(requirement).map(this::unequip).collect(Collectors.toList());
    }

    public Clothing unequip(Clothing article) {
        if (article == null) {
            return null;
        }
        equipped.remove(article);
        article.getSlots().forEach(slot -> outfit.get(slot).set(article.getLayer(), null));
        return article;
    }

    public List<Clothing> equip(Clothing article) {
        // get a list of things that will be unequipped by this
        List<Clothing> unequipped = article.getSlots().stream().map(slot -> outfit.get(slot).get(article.getLayer()))
                        .filter(c -> c != null).map(this::unequip).collect(Collectors.toList());
        article.getSlots().forEach(slot -> outfit.get(slot).set(article.getLayer(), article));
        equipped.add(article);
        return unequipped;
    }

    public void dress(List<Clothing> pile) {
        pile.forEach(this::equip);
    }

    public Collection<Clothing> getEquipped() {
        return Collections.unmodifiableCollection(equipped);
    }

    public double getExposure(ClothingSlot slot) {
        double exposure = 1;
        for (int i = Clothing.N_LAYERS - 1; i >= 0; i--) {
            Clothing article = outfit.get(slot).get(i);
            if (article == null) {
                continue;
            }
            exposure *= article.getExposure();
            if (article.is(ClothingTrait.skimpy) || article.is(ClothingTrait.open)) {
                continue;
            } else {
                break;
            }
        }
        return exposure;
    }

    public double getExposure() {
        double exposure = 0;
        double totalWeight = 0;
        for (ClothingSlot slot : ClothingSlot.values()) {
            double weight = slot.getExposureWeight();
            exposure += weight * getExposure(slot);
            totalWeight += weight;
        }
        return exposure / totalWeight;
    }

    public double getFitness(Combat c, double bottomFitness, double topFitness) {
        double fitness = 0;
        fitness += outfit.get(ClothingSlot.top).stream().filter(article -> article != null)
                        .filter(article -> !article.is(ClothingTrait.skimpy) && !article.is(ClothingTrait.flexible)
                                        && !article.is(ClothingTrait.open))
                        .mapToDouble(article -> topFitness * (1 + article.dc() * .1)).sum();
        fitness += outfit.get(ClothingSlot.bottom).stream().filter(article -> article != null)
                        .filter(article -> !article.is(ClothingTrait.skimpy) && !article.is(ClothingTrait.flexible)
                                        && !article.is(ClothingTrait.open))
                        .mapToDouble(article -> bottomFitness * (1 + article.dc() * .1)).sum();
        return fitness;
    }

    public String describe(Character c) {
        StringBuilder sb = new StringBuilder();
        Set<Clothing> described = new HashSet<>();
        Clothing over = outfit.get(ClothingSlot.top).get(4);
        Clothing top = getTopOfSlot(ClothingSlot.top, 3);
        Clothing bottom = getTopOfSlot(ClothingSlot.bottom, 3);
        Clothing arms = getTopOfSlot(ClothingSlot.arms, 3);
        Clothing legs = getTopOfSlot(ClothingSlot.legs, 3);
        Clothing feet = getTopOfSlot(ClothingSlot.feet, 3);

        Set<Clothing> others = new LinkedHashSet<>();
        if (arms != null) {
            others.add(arms);
        }
        if (legs != null) {
            others.add(legs);
        }
        if (feet != null) {
            others.add(feet);
        }
        if (over != null) {
            sb.append("Under {self:possessive} " + over.getName() + ", ");
        }
        if (top == null && bottom == null && others.isEmpty()) {
            sb.append("{self:subject-action:are|is} completely naked.<br/>");
        } else {
            boolean addedTop = false;
            if (top == null && bottom == null) {
            } else {
                if (top == null) {
                    sb.append("{self:subject-action:are|is} topless");
                } else {
                    sb.append("{self:subject-action:are|is} wearing " + top.pre() + top.getName() + "");
                    addedTop = true;
                    described.add(top);
                }
                if (bottom == null) {
                    sb.append(" but {self:possessive} crotch is clearly visible.<br/>");
                } else {
                    if (bottom != top) {
                        sb.append(" and ");
                        if (!addedTop) {
                            sb.append("wearing ");
                        }
                        sb.append(bottom.pre() + bottom.getName() + ".<br/>");
                        described.add(bottom);
                    } else {
                        sb.append(".<br/>");
                    }
                }
            }
            others.removeAll(described);
            if (!others.isEmpty()) {
                if (top == null && bottom == null) {
                    sb.append("{self:SUBJECT-ACTION:are|is} only wearing");
                } else {
                    sb.append("{self:PRONOUN-ACTION:are|is} also wearing");
                }
                int index = 0;
                for (Clothing article : others) {
                    if (index == others.size() - 1) {
                        if (others.size() > 1) {
                            sb.append(" and");
                        }
                    }
                    sb.append(" " + article.pre() + article.getName());
                    if (index < others.size() - 2 && others.size() > 2) {
                        sb.append(',');
                    }
                    index += 1;
                }
                if (top == null && bottom == null) {
                    sb.append(" while {self:possessive} chest and crotch are clearly visible.");
                } else {
                    sb.append('.');
                }
            }
        }
        sb.append("<br/>");
        if (Global.isDebugOn(DebugFlags.DEBUG_CLOTHING)) {
            for (Clothing article : equipped) {
                sb.append(article);
                sb.append("<br/>");
            }
        }
        return Global.capitalizeFirstLetter(Global.format(sb.toString(), c, c));
    }

    public boolean isNude() {
        return equipped.isEmpty();
    }

    // strips to fuck
    public List<Clothing> strip() {
        return undressOnly(article -> !article.is(ClothingTrait.open) && (article.getSlots().contains(ClothingSlot.top)
                        || article.getSlots().contains(ClothingSlot.bottom)));
    }

    // strips to fuck, but can't get rid of indestructables
    public List<Clothing> forcedstrip() {
        return undressOnly(article -> (!article.is(ClothingTrait.indestructible) || !article.is(ClothingTrait.open))
                        && (article.getSlots().contains(ClothingSlot.top)
                                        || article.getSlots().contains(ClothingSlot.bottom)));
    }

    private void getHotness(ClothingSlot slot, Map<Clothing, Double> seen) {
        List<Clothing> list = outfit.get(slot);
        double exposure = 1;
        for (int i = Clothing.N_LAYERS - 1; i >= 0; i--) {
            Clothing article = list.get(i);
            if (article != null) {
                if (seen.containsKey(article) && seen.get(article) < exposure) {
                    seen.put(article, exposure);
                } else if (!seen.containsKey(article)) {
                    seen.put(article, exposure);
                }
                if (!article.is(ClothingTrait.open) || !article.is(ClothingTrait.skimpy)) {
                    return;
                }
                exposure *= article.getExposure();
            }
        }
    }

    public double getHotness() {
        Map<Clothing, Double> maximumExposure = new HashMap<>();
        Arrays.stream(ClothingSlot.values()).forEach(slot -> getHotness(slot, maximumExposure));
        return maximumExposure.keySet().stream()
                        .mapToDouble(article -> maximumExposure.get(article) * article.getHotness()).sum();
    }

    @Override
    public String toString() {
        if (equipped == null) {
            return super.toString();
        }
        String sb = "[" + equipped.stream().map(Clothing::getName).collect(Collectors.joining(", ")) + ']';
        return String.format("%s@%s", sb, Integer.toHexString(hashCode()));
    }

    public boolean hasNoShoes() {
        Clothing feetSlot = getTopOfSlot(ClothingSlot.feet);
        return feetSlot == null || feetSlot.getLayer() < 2;
    }

    public boolean canEquip(Clothing c) {
        return c.getSlots()
                    .stream()
                    .map(this::getTopOfSlot)
                    .filter(Objects::nonNull)
                    .mapToInt(Clothing::getLayer)
                    .allMatch(i -> c.getLayer() < i);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Outfit outfit = (Outfit) o;

        return equipped.equals(outfit.equipped);

    }

    @Override public int hashCode() {
        return equipped.hashCode();
    }
}
