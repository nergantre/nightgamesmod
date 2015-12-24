package nightgames.modifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import nightgames.actions.Action;
import nightgames.characters.Character;
import nightgames.global.Match;
import nightgames.items.Item;
import nightgames.modifier.action.ActionModifier;
import nightgames.modifier.clothing.ClothingModifier;
import nightgames.modifier.item.ItemModifier;
import nightgames.modifier.skill.SkillModifier;

public abstract class BaseModifier implements Modifier {

	protected static final BiConsumer<Character, Match> EMPTY_CONSUMER = (c,
			m) -> {
	};

	protected ClothingModifier				clothing;
	protected ItemModifier					items;
	protected StatusModifier				status;
	protected SkillModifier					skills;
	protected ActionModifier				actions;
	protected BiConsumer<Character, Match>	custom;

	protected Map<Character, Map<Item, Integer>> moddedItems;

	protected BaseModifier(ClothingModifier clothing, ItemModifier items,
			StatusModifier status, SkillModifier skills, ActionModifier actions,
			BiConsumer<Character, Match> custom) {
		this.clothing = clothing;
		this.items = items;
		this.status = status;
		this.skills = skills;
		this.actions = actions;
		this.custom = custom;
		this.moddedItems = new HashMap<>();
	}

	protected BaseModifier() {
		this(ClothingModifier.NULL_MODIFIER, ItemModifier.NULL_MODIFIER,
				StatusModifier.NULL_MODIFIER, SkillModifier.NULL_MODIFIER,
				ActionModifier.NULL_MODIFIER, EMPTY_CONSUMER);
	}

	@Override
	public void handleOutfit(Character c) {
		if (c.human() || !clothing.playerOnly())
			clothing.apply(c.outfit);
	}

	@Override
	public void handleItems(Character c) {
		moddedItems.putIfAbsent(c, new HashMap<>());
		Map<Item, Integer> inventory = new HashMap<>(c.getInventory());
		inventory.forEach((item, count) -> {
			if (items.itemIsBanned(item)) {
				c.getInventory().remove(item);
				moddedItems.get(c).putIfAbsent(item, 0);
				moddedItems.get(c).compute(item, (i, cnt) -> cnt - count);
			}
		});
		items.ensuredItems().forEach((item, count) -> {
			while (!c.has(item, count)) {
				c.gain(item);
				moddedItems.get(c).putIfAbsent(item, 0);
				moddedItems.get(c).compute(item, (i, cnt) -> cnt + 1);
			}
		});
	}

	@Override
	public void handleStatus(Character c) {
		status.apply(c);
	}

	public SkillModifier getSkillModifier() {
		return skills;
	}

	public void handleTurn(Character c, Match m) {
		custom.accept(c, m);
	}

	@Override
	public boolean allowAction(Action act, Character c, Match m) {
		return !c.human() || !actions.actionIsBanned(act, c, m);
	}
	
	@Override
	public void undoItems(Character c) {
		if (moddedItems.containsKey(c))
			moddedItems.get(c).forEach((item, count) -> c.gain(item, -count));
	}

	@Override
	public boolean isApplicable() {
		return true;
	}

	@Override
	public String toString() {
		return String.format(
				"Modifier\n\tClothing: %s\n\tItems: %s\n\t"
				+ "Status: %s\n\tSkills: %s\n\tActions: %s\n\tCustom: %s\n",
				clothing.toString(), items.toString(), status.toString(),
				skills.toString(), actions.toString(),
				custom == EMPTY_CONSUMER ? "no" : "yes");
	}
}
