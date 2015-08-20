package nightgames.characters.custom;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import nightgames.characters.Plan;
import nightgames.characters.Trait;
import nightgames.characters.Attribute;
import nightgames.items.Clothing;
import nightgames.items.Item;

public class JSONSourceNPCDataLoader {
	
	private static int readNumber(JSONObject struct, String key) {
		return ((Number)struct.get(key)).intValue();
	}
	
	private static void loadResources(JSONObject resources, Stats stats) {
		stats.stamina = readNumber(resources, "stamina");
		stats.arousal = readNumber(resources, "arousal");
		stats.mojo = readNumber(resources, "mojo");
		stats.willpower = readNumber(resources, "willpower");
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
		} catch (ClassCastException e) {
			throw new ParseException("Badly formatted JSON character: " + e.getMessage(), 0);
		} catch (IllegalArgumentException e) {
			throw new ParseException("Nonexistent value: " + e.getMessage(), 0);
		}
		return data;
	}

	private static void loadTraits(JSONArray arr, List<Trait> traits) {
		for (Object traitString : arr) {
			Trait trait = Trait.valueOf((String)traitString);
			traits.add(trait);
		}
	}

}