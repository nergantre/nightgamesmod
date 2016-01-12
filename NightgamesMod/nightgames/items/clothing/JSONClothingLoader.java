package nightgames.items.clothing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import nightgames.characters.CharacterSex;
import nightgames.characters.Trait;
import nightgames.global.JSONUtils;

public class JSONClothingLoader {
	/*
	 * Example: { "displayName":"shirt", "buffs":[ ], "price":275, "prefix":"a "
	 * , "attributes":[ ], "layer":1, "slots" : ["top"], "sex" : ["all"],
	 * "hotness":0.1, "name":"shirt", "toughness":5, "exposure":0.4 },
	 */
	private static Clothing loadClothingFromJSON(JSONObject clothingObj) {
		String displayName = JSONUtils.readString(clothingObj, "displayName");
		int price = JSONUtils.readInteger(clothingObj, "price");
		int toughness = JSONUtils.readInteger(clothingObj, "toughness");
		int layer = JSONUtils.readInteger(clothingObj, "layer");
		String name = JSONUtils.readString(clothingObj, "name");
		String prefix = JSONUtils.readString(clothingObj, "prefix");
		float hotness = JSONUtils.readFloat(clothingObj, "hotness");
		float exposure = JSONUtils.readFloat(clothingObj, "exposure");
		Set<Trait> traits = JSONUtils.loadEnumsFromArr(clothingObj, "buffs", Trait.class);
		Set<ClothingTrait> attributes = JSONUtils.loadEnumsFromArr(clothingObj, "attributes", ClothingTrait.class);
		Set<ClothingSlot> slots = JSONUtils.loadEnumsFromArr(clothingObj, "slots", ClothingSlot.class);
		Set<CharacterSex> sex = JSONUtils.loadEnumsFromArrWithExtras(clothingObj, "sex",
				Collections.singletonMap("all", Arrays.asList(CharacterSex.values())), CharacterSex.class);
		List<String> stores = JSONUtils.loadStringsFromArr(clothingObj, "shops");
		Clothing res = new Clothing();
		res.id = name;
		res.price = price;
		res.name = displayName;
		res.setLayer(layer);
		res.prefix = prefix;
		res.hotness = hotness;
		res.exposure = exposure;
		res.dc = toughness;
		res.buffs = new ArrayList<>(traits);
		res.attributes = new ArrayList<>(attributes);
		res.sex = new ArrayList<>(sex);
		res.setSlots(new ArrayList<>(slots));
		res.stores = new ArrayList<>(stores);
		return res;
	}

	public static List<Clothing> loadClothingListFromJSON(JSONArray clothingArr) {
		List<Clothing> results = new ArrayList<Clothing>();
		for (Object obj : clothingArr) {
			results.add(loadClothingFromJSON((JSONObject) obj));
		}
		return results;
	}
}
