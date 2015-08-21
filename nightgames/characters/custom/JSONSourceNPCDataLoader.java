package nightgames.characters.custom;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import nightgames.characters.Plan;
import nightgames.characters.Trait;
import nightgames.characters.Attribute;
import nightgames.characters.Growth;
import nightgames.items.Clothing;
import nightgames.items.Item;

public class JSONSourceNPCDataLoader {

	private static int readInteger(JSONObject struct, String key) {
		return ((Number)struct.get(key)).intValue();
	}

	private static float readFloat(JSONObject struct, String key) {
		return ((Number)struct.get(key)).floatValue();
	}

	private static void loadResources(JSONObject resources, Stats stats) {
		stats.stamina = readFloat(resources, "stamina");
		stats.arousal = readFloat(resources, "arousal");
		stats.mojo = readFloat(resources, "mojo");
		stats.willpower = readFloat(resources, "willpower");
	}

	public static NPCData load(String path) throws ParseException,IOException {
		Object value = JSONValue.parse(new FileReader(path));
		DataBackedNPCData data = new DataBackedNPCData();
		try {
			JSONObject object = (JSONObject)value;
			data.name = (String) object.get("name");
			data.trophy = Item.valueOf((String)object.get("trophy"));
			data.plan = Plan.valueOf((String)object.get("plan"));
			
			//load outfit
			JSONObject outfit = (JSONObject) object.get("outfit");
			JSONArray top = (JSONArray) outfit.get("top");
			for (Object clothing : top) {
				data.top.push(Clothing.valueOf((String)clothing));
			}
			JSONArray bottom = (JSONArray) outfit.get("bottom");
			for (Object clothing : bottom) {
				data.bottom.push(Clothing.valueOf((String)clothing));
			}
			
			// load stats
			JSONObject stats = (JSONObject)object.get("stats");
			// load base stats
			JSONObject baseStats = (JSONObject)stats.get("base");
			data.stats.level = ((Number)baseStats.get("level")).intValue();
			// load attributes
			JSONObject attributes = (JSONObject)baseStats.get("attributes");
			for (Object attributeString :attributes.keySet()) {
				Attribute att = Attribute.valueOf((String)attributeString);
				data.stats.attributes.put(att, ((Number)attributes.get(attributeString)).intValue());
			}
			loadResources((JSONObject)baseStats.get("resources"), data.stats);
			loadTraits((JSONArray)baseStats.get("traits"), data.stats.traits);
			loadGrowth((JSONObject)stats.get("growth"), data.growth);
			loadPreferredAttributes((JSONArray) ((JSONObject)stats.get("growth")).get("preferredAttributes"), data);
		} catch (ClassCastException e) {
			throw new ParseException("Badly formatted JSON character: " + e.getMessage(), 0);
		} catch (IllegalArgumentException e) {
			throw new ParseException("Nonexistent value: " + e.getMessage(), 0);
		}
		return data;
	}

	private static void loadGrowthResources(JSONObject obj, Growth growth) {
		growth.stamina = readFloat(obj, "stamina");
		growth.bonusStamina = readFloat(obj, "bonusStamina");
		growth.arousal = readFloat(obj, "arousal");
		growth.bonusArousal = readFloat(obj, "bonusArousal");
		growth.mojo = readFloat(obj, "mojo");
		growth.bonusMojo = readFloat(obj, "bonusMojo");
		growth.willpower = readFloat(obj, "willpower");
		growth.bonusWillpower = readFloat(obj, "bonusWillpower");
		growth.bonusAttributes = readInteger(obj, "bonusPoints");
		JSONArray points = (JSONArray) obj.get("points");
		int defaultPoints = 3;
		for (int i = 0; i < growth.attributes.length; i++) {
			if (i < points.size()) {
				growth.attributes[i] = ((Number)points.get(i)).intValue();
				defaultPoints = growth.attributes[i];
			} else {
				growth.attributes[i] = defaultPoints;
			}
		}
	}

	private static void loadGrowth(JSONObject obj, Growth growth) {
		loadGrowthResources((JSONObject)obj.get("resources"), growth);
		loadGrowthTraits((JSONArray)obj.get("traits"), growth);
	}

	private static void loadPreferredAttributes(JSONArray arr, DataBackedNPCData data) {
		for (Object mem : arr) {
			JSONObject obj = (JSONObject) mem;
			Attribute att = Attribute.valueOf((String)obj.get("attribute"));
			final int max;
			if (obj.containsKey("max")) {
				max = ((Number) obj.get("max")).intValue();
			} else {
				max = Integer.MAX_VALUE;
			}
			data.preferredAttributes.add(character -> {
				if (character.get(att) < max) { 
					return Optional.of(att);
				} else {
					return Optional.empty();
				}
			});
		}
	}

	private static void loadGrowthTraits(JSONArray arr, Growth growth) {
		for (Object mem : arr) {
			JSONObject obj = (JSONObject) mem;
			growth.addTrait(readInteger(obj, "level"), Trait.valueOf((String)obj.get("trait")));
		}
	}

	private static void loadTraits(JSONArray arr, List<Trait> traits) {
		for (Object traitString : arr) {
			Trait trait = Trait.valueOf((String)traitString);
			traits.add(trait);
		}
	}

}