package nightgames.items.clothing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Modifier;

public class Outfit {
	private Map<ClothingSlot, List<Clothing>> outfit;
	private Set<Clothing> equipped;
	public Outfit() {
		outfit = new EnumMap<>(ClothingSlot.class);
		equipped = new HashSet<>();
		Arrays.stream(ClothingSlot.values()).forEach(slot -> {
			List<Clothing> list = new ArrayList<Clothing>(Clothing.N_LAYERS);
			outfit.put(slot, list);
			IntStream.range(0, Clothing.N_LAYERS).forEach(i -> list.set(i, null));
		});
	}

	public Outfit(Outfit other) {
		outfit = new EnumMap<>(ClothingSlot.class);
		Arrays.stream(ClothingSlot.values()).forEach(slot -> outfit.put(slot, new ArrayList<Clothing>(other.outfit.get(slot))));
		equipped = new HashSet<>(other.equipped);
	}

	/* public information api */
	public boolean slotNaked(ClothingSlot slot) {
		return (outfit.get(slot).isEmpty()
				|| !outfit.get(slot).stream().noneMatch(article -> article != null && !article.is(ClothingTrait.open)));
	}

	public boolean slotEmpty(ClothingSlot slot) {
		return (outfit.get(slot).isEmpty()
				|| !outfit.get(slot).stream().noneMatch(article -> article != null));
	}

	public boolean slotUnshreddable(ClothingSlot slot) {
		return slotEmpty(slot) || getTopOfSlot(slot).is(ClothingTrait.indestructible);
	}

	public boolean slotEmptyOrMeetsCondition(ClothingSlot slot, Predicate<? super Clothing> condition) {
		return (outfit.get(slot).isEmpty()
				|| !outfit.get(slot).stream().filter(article -> article != null).allMatch(condition));
	}

	public Clothing getTopOfSlot(ClothingSlot slot) {
		List<Clothing> list = outfit.get(slot);
		for (int i = Clothing.N_LAYERS; i >= 0; i--) {
			if (list.get(i) != null) {
				return list.get(i);
			}
		}
		return null;
	}

	public Clothing getBottomOfSlot(ClothingSlot slot) {
		List<Clothing> list = outfit.get(slot);
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
		List<ClothingSlot> slotsAvailable = 
				Arrays.stream(ClothingSlot.values()).filter(slot -> !slotNaked(slot)).collect(Collectors.toList());
		if (slotsAvailable.isEmpty()) { return null; }
		Collections.shuffle(slotsAvailable);
		return slotsAvailable.get(0);
	}

	public ClothingSlot getRandomShreddableSlot() {
		List<ClothingSlot> slotsAvailable = 
				Arrays.stream(ClothingSlot.values()).filter(slot -> !slotUnshreddable(slot)).collect(Collectors.toList());
		if (slotsAvailable.isEmpty()) { return null; }
		Collections.shuffle(slotsAvailable);
		return slotsAvailable.get(0);
	}

	public ClothingSlot getRandomEquippedSlot() {
		List<ClothingSlot> slotsAvailable = 
				Arrays.stream(ClothingSlot.values()).filter(slot -> !slotEmpty(slot)).collect(Collectors.toList());
		if (slotsAvailable.isEmpty()) { return null; }
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
	public void change(Modifier rule, List<Clothing> plan) {
		undress();
		plan.forEach(article -> equip(article));
	}

	public List<Clothing> undress() {
		return equipped.stream().map(article -> unequip(article)).collect(Collectors.toList());
	}

	public List<Clothing> undressOnly(Predicate<? super Clothing> requirement) {
		return equipped.stream().filter(requirement).map(article -> unequip(article)).collect(Collectors.toList());
	}

	public Clothing unequip(Clothing article) {
		if (article == null) { return null; }
		equipped.remove(article);
		article.getSlots().forEach(slot -> outfit.get(slot).set(article.getLayer(), null));
		return article;
	}

	public List<Clothing> equip(Clothing article) {
		// get a list of things that will be unequipped by this
		List<Clothing> unequipped = article.getSlots().stream()
				.map(slot -> outfit.get(slot).get(article.getLayer()))
				.filter(c -> c != null)
				.map(c -> unequip(c)).collect(Collectors.toList());
		article.getSlots().forEach(slot -> outfit.get(slot).set(article.getLayer(), article));
		equipped.add(article);
		return unequipped;
	}

	public void dress(List<Clothing> pile){
		pile.forEach(article -> equip(article));
	}

	public Collection<Clothing> getEquipped() {
		return Collections.unmodifiableCollection(equipped);
	}

	public double getExposure(ClothingSlot slot) {
		double exposure = 1;
		for (int i = Clothing.N_LAYERS; i >= 0; i++) {
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
		fitness += outfit.get(ClothingSlot.top).stream().filter(article ->
				!article.is(ClothingTrait.skimpy)
				&& !article.is(ClothingTrait.flexible)
				&& !article.is(ClothingTrait.open)).mapToDouble(article -> topFitness * (1 + article.dc() * .1)).sum();
		fitness += outfit.get(ClothingSlot.bottom).stream().filter(article ->
				!article.is(ClothingTrait.skimpy)
				&& !article.is(ClothingTrait.flexible)
				&& !article.is(ClothingTrait.open)).mapToDouble(article -> bottomFitness * (1 + article.dc() * .1)).sum();
		return fitness;
	}

	public String describe(Character c) {
		/*if(top.empty()&&bottom.empty()){
			description = description+"She is completely naked.<br>";
		}
		else{
			if(top.empty()){
				description = description+"She is topless and ";
				if(!bottom.empty()){
					description=description+"wearing ";
				}
			}
			else{
				description = description+"She is wearing "+top.peek().pre()+top.peek().getName()+" and ";
			}
			if(bottom.empty()){
				description = description+"is naked from the waist down.<br>";
			}
			else{
				description = description+bottom.peek().pre()+bottom.peek().getName()+".<br>";
			}
		}*/
		return "";
	}

}
