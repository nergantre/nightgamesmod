package nightgames.modifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import nightgames.Resources.ResourceLoader;
import nightgames.characters.CharacterSex;
import nightgames.characters.Player;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.items.clothing.Clothing;
import nightgames.modifier.action.ActionModifier;
import nightgames.modifier.clothing.ClothingModifier;
import nightgames.modifier.item.ItemModifier;
import nightgames.modifier.skill.SkillModifier;

public final class CustomModifierLoader {

	private CustomModifierLoader() {
		// TODO Auto-generated constructor stub
	}

	public static Modifier readModifier(JSONObject root) {
		int bonus = JSONUtils.readInteger(root, "bonus");
		String name = JSONUtils.readString(root, "name");
		String intro = JSONUtils.readString(root, "intro");
		String acceptance = JSONUtils.readString(root, "acceptance");
		ActionModifier action;
		if (root.containsKey("action")) {
			action = readActionModifiers((JSONArray) root.get("action"));
		} else {
			action = ActionModifier.NULL_MODIFIER;
		}
		SkillModifier skill;
		if (root.containsKey("skill")) {
			skill = readSkillModifiers((JSONArray) root.get("skill"));
		} else {
			skill = SkillModifier.NULL_MODIFIER;
		}
		ClothingModifier clothing;
		if (root.containsKey("clothing")) {
			clothing = readClothingModifiers((JSONArray) root.get("clothing"));
		} else {
			clothing = ClothingModifier.NULL_MODIFIER;
		}
		ItemModifier item;
		if (root.containsKey("item")) {
			item = readItemModifiers((JSONArray) root.get("item"));
		} else {
			item = ItemModifier.NULL_MODIFIER;
		}
		return new BaseModifier(clothing, item, StatusModifier.NULL_MODIFIER,
				skill, action, BaseModifier.EMPTY_CONSUMER) {

			@Override
			public int bonus() {
				return bonus;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public String intro() {
				return intro;
			}

			@Override
			public String acceptance() {
				return acceptance;
			}
		};
	}

	@SuppressWarnings("unchecked")
	private static ActionModifier readActionModifier(JSONObject obj) {
		String type = JSONUtils.readString(obj, "type");
		ModifierComponent<? extends ActionModifier> template = (ModifierComponent<? extends ActionModifier>) ActionModifier.TYPES
				.stream()
				.filter(t -> ((ModifierComponent<? extends ActionModifier>) t)
						.name().equals(type))
				.findAny().orElseThrow(() -> new IllegalArgumentException(
						"Unkown modifier type " + type));
		if (!obj.containsKey("value")
				|| !(obj.get("value") instanceof JSONObject)) {
			throw new IllegalArgumentException(
					"All modifiers must have an object 'value'");
		}
		JSONObject value = (JSONObject) obj.get("value");
		return template.instance(value);
	}

	@SuppressWarnings("unchecked")
	private static SkillModifier readSkillModifier(JSONObject obj) {
		String type = JSONUtils.readString(obj, "type");
		ModifierComponent<? extends SkillModifier> template = (ModifierComponent<? extends SkillModifier>) SkillModifier.TYPES
				.stream()
				.filter(t -> ((ModifierComponent<? extends SkillModifier>) t)
						.name().equals(type))
				.findAny().orElseThrow(() -> new IllegalArgumentException(
						"Unkown modifier type " + type));
		if (!obj.containsKey("value")
				|| !(obj.get("value") instanceof JSONObject)) {
			throw new IllegalArgumentException(
					"All modifiers must have an object 'value'");
		}
		JSONObject value = (JSONObject) obj.get("value");
		return template.instance(value);
	}

	@SuppressWarnings("unchecked")
	private static ClothingModifier readClothingModifier(JSONObject obj) {
		String type = JSONUtils.readString(obj, "type");
		ModifierComponent<? extends ClothingModifier> template = (ModifierComponent<? extends ClothingModifier>) ClothingModifier.TYPES
				.stream()
				.filter(t -> ((ModifierComponent<? extends ClothingModifier>) t)
						.name().equals(type))
				.findAny().orElseThrow(() -> new IllegalArgumentException(
						"Unkown modifier type " + type));
		if (!obj.containsKey("value")
				|| !(obj.get("value") instanceof JSONObject)) {
			throw new IllegalArgumentException(
					"All modifiers must have an object 'value'");
		}
		JSONObject value = (JSONObject) obj.get("value");
		return template.instance(value);
	}

	@SuppressWarnings("unchecked")
	private static ItemModifier readItemModifier(JSONObject obj) {
		String type = JSONUtils.readString(obj, "type");
		ModifierComponent<? extends ItemModifier> template = (ModifierComponent<? extends ItemModifier>) ItemModifier.TYPES
				.stream()
				.filter(t -> ((ModifierComponent<? extends ItemModifier>) t)
						.name().equals(type))
				.findAny().orElseThrow(() -> new IllegalArgumentException(
						"Unkown modifier type " + type));
		if (!obj.containsKey("value")
				|| !(obj.get("value") instanceof JSONObject)) {
			throw new IllegalArgumentException(
					"All modifiers must have an object 'value'");
		}
		JSONObject value = (JSONObject) obj.get("value");
		return template.instance(value);
	}

	private static ActionModifier readActionModifiers(JSONArray arr) {
		List<ActionModifier> mods = new ArrayList<>();
		for (Object obj : arr) {
			JSONObject jobj = (JSONObject) obj;
			mods.add(readActionModifier(jobj));
		}
		return ActionModifier.allOf(mods.toArray(new ActionModifier[] {}));
	}

	private static SkillModifier readSkillModifiers(JSONArray arr) {
		List<SkillModifier> mods = new ArrayList<>();
		for (Object obj : arr) {
			JSONObject jobj = (JSONObject) obj;
			mods.add(readSkillModifier(jobj));
		}
		return SkillModifier.allOf(mods.toArray(new SkillModifier[] {}));
	}

	private static ClothingModifier readClothingModifiers(JSONArray arr) {
		List<ClothingModifier> mods = new ArrayList<>();
		for (Object obj : arr) {
			JSONObject jobj = (JSONObject) obj;
			mods.add(readClothingModifier(jobj));
		}
		return ClothingModifier.allOf(mods.toArray(new ClothingModifier[] {}));
	}

	private static ItemModifier readItemModifiers(JSONArray arr) {
		List<ItemModifier> mods = new ArrayList<>();
		for (Object obj : arr) {
			JSONObject jobj = (JSONObject) obj;
			mods.add(readItemModifier(jobj));
		}
		return ItemModifier.allOf(mods.toArray(new ItemModifier[] {}));
	}

	public static void main(String[] args) throws IOException, ParseException {
		InputStream in = ResourceLoader
				.getFileResourceAsStream("test_modifier.json");
		JSONObject obj = (JSONObject) JSONValue
				.parseWithException(new InputStreamReader(in));
		Clothing.buildClothingTable();
		Global.buildParser();
		Global.buildModifierPool();
		Global.buildActionPool();
		Global.buildSkillPool(new Player("player", CharacterSex.male));
		Modifier mod = readModifier(obj);
		System.out.println(mod);
	}
}
