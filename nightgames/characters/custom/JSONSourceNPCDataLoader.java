package nightgames.characters.custom;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import nightgames.characters.Plan;
import nightgames.characters.Trait;
import nightgames.characters.custom.requirement.AndRequirement;
import nightgames.characters.custom.requirement.BodyPartRequirement;
import nightgames.characters.custom.requirement.CustomRequirement;
import nightgames.characters.custom.requirement.DomRequirement;
import nightgames.characters.custom.requirement.InsertedRequirement;
import nightgames.characters.custom.requirement.ItemRequirement;
import nightgames.characters.custom.requirement.LevelRequirement;
import nightgames.characters.custom.requirement.MoodRequirement;
import nightgames.characters.custom.requirement.NotRequirement;
import nightgames.characters.custom.requirement.OrRequirement;
import nightgames.characters.custom.requirement.ResultRequirement;
import nightgames.characters.custom.requirement.ReverseRequirement;
import nightgames.characters.custom.requirement.StanceRequirement;
import nightgames.characters.custom.requirement.SubRequirement;
import nightgames.characters.custom.requirement.TraitRequirement;
import nightgames.combat.Result;
import nightgames.global.JSONUtils;
import nightgames.characters.Attribute;
import nightgames.characters.Emotion;
import nightgames.characters.Growth;
import nightgames.items.Clothing;
import nightgames.items.Item;
import nightgames.items.ItemAmount;

public class JSONSourceNPCDataLoader {
	private static ItemAmount readItem(JSONObject obj) {
		Item item = Item.valueOf((String)obj.get("item"));
		int amount = JSONUtils.readInteger(obj, "amount");
		return new ItemAmount(item, amount);
	}

	private static CustomStringEntry readLine(JSONObject obj) {
		CustomStringEntry entry = new CustomStringEntry((String)obj.get("text"));
		if (obj.containsKey("requirements")) {
			loadRequirement((JSONObject) obj.get("requirements"), entry.requirements);
		}
		return entry;
	}

	private static void loadResources(JSONObject resources, Stats stats) {
		stats.stamina = JSONUtils.readFloat(resources, "stamina");
		stats.arousal = JSONUtils.readFloat(resources, "arousal");
		stats.mojo = JSONUtils.readFloat(resources, "mojo");
		stats.willpower = JSONUtils.readFloat(resources, "willpower");
	}

	public static NPCData load(InputStream in) throws ParseException,IOException {
		Object value = JSONValue.parse(new InputStreamReader(in));
		DataBackedNPCData data = new DataBackedNPCData();
		try {
			JSONObject object = (JSONObject)value;
			data.name = (String) object.get("name");
			data.type = (String) object.get("type");
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
			loadItems((JSONObject)object.get("items"), data);
			loadAllLines((JSONObject)object.get("lines"), data.characterLines);
			loadLines((JSONArray)object.get("portraits"), data.portraits);
		} catch (ClassCastException e) {
			throw new ParseException("Badly formatted JSON character: " + e.getMessage(), 0);
		} catch (IllegalArgumentException e) {
			throw new ParseException("Nonexistent value: " + e.getMessage(), 0);
		}
		return data;
	}

	private static void loadLines(JSONArray linesArr, List<CustomStringEntry> entries) {
		for (Object obj : linesArr) {
			entries.add(readLine((JSONObject)obj));
		}
	}

	private static void loadRequirement(JSONObject obj, List<CustomRequirement> reqs) {
		// reverse reverses the self/other, so you can apply the requirement to the opponent
		if (obj.containsKey("reverse")) {
			List<CustomRequirement> subReqs = new ArrayList<>();
			loadRequirement((JSONObject) obj.get("reverse"), subReqs);
			reqs.add(new ReverseRequirement(subReqs));
		}
		// and requires that both of the sub requirements are true
		if (obj.containsKey("and")) {
			JSONArray reqsArr = ((JSONArray)obj.get("and"));
			List<List<CustomRequirement>> allReqs = new ArrayList<>();
			for (Object reqMem : reqsArr) {
				JSONObject reqsObj = (JSONObject) reqMem;
				List<CustomRequirement> subReqs = new ArrayList<>();
				loadRequirement(reqsObj, subReqs);
				allReqs.add(subReqs);
			}
			reqs.add(new AndRequirement(allReqs));
		}
		// or requires that one of the sub requirements are true
		if (obj.containsKey("or")) {
			JSONArray reqsArr = ((JSONArray)obj.get("or"));
			List<List<CustomRequirement>> allReqs = new ArrayList<>();
			for (Object reqMem : reqsArr) {
				JSONObject reqsObj = (JSONObject) reqMem;
				List<CustomRequirement> subReqs = new ArrayList<>();
				loadRequirement(reqsObj, subReqs);
				allReqs.add(subReqs);
			}
			reqs.add(new OrRequirement(allReqs));
		}
		// not requires that the sub requirement be not true
		if (obj.containsKey("not")) {
			List<CustomRequirement> subReqs = new ArrayList<>();
			loadRequirement((JSONObject) obj.get("not"), subReqs);
			reqs.add(new NotRequirement(subReqs));
		}
		// trait requires the character to have the trait
		if (obj.containsKey("trait")) {
			reqs.add(new TraitRequirement(Trait.valueOf(JSONUtils.readString(obj, "trait"))));
		}
		// dom requires the character to be in a dominant stance. Invalid out of combat
		if (obj.containsKey("dom")) {
			reqs.add(new DomRequirement());
		}
		// sub requires the character to be in a submissive stance. Invalid out of combat
		if (obj.containsKey("sub")) {
			reqs.add(new SubRequirement());
		}
		// inserted requires the character to be inserted. Invalid out of combat
		if (obj.containsKey("inserted")) {
			reqs.add(new InsertedRequirement());
		}
		// body part requires the character to have at least one of the type of body part specified
		if (obj.containsKey("bodypart")) {
			reqs.add(new BodyPartRequirement(JSONUtils.readString(obj, "bodypart")));
		}
		// level requires the character to be at least that level
		if (obj.containsKey("level")) {
			reqs.add(new LevelRequirement(JSONUtils.readInteger(obj, "level")));
		}
		// item requires the character to have at least that many items
		if (obj.containsKey("item")) {
			reqs.add(new ItemRequirement(readItem((JSONObject)obj.get("item"))));
		}
		// mood requires the character to be in that mood. Does not work on the player
		if (obj.containsKey("mood")) {
			reqs.add(new MoodRequirement(Emotion.valueOf(JSONUtils.readString(obj, "mood"))));
		}
		// stance requires the character to be in that stance. Invalid out of combat
		if (obj.containsKey("stance")) {
			reqs.add(new StanceRequirement(JSONUtils.readString(obj, "stance")));
		}
		// result requires the battle to be in that result state. Invalid out of combat
		if (obj.containsKey("result")) {
			reqs.add(new ResultRequirement(Result.valueOf(JSONUtils.readString(obj, "result"))));
		}
	}

	private static void loadAllLines(JSONObject linesObj, Map<String, List<CustomStringEntry>> characterLines) {
		for (Object key : linesObj.keySet()) {
			String keyString = (String)key;
			characterLines.put(keyString, new ArrayList<>());
			loadLines((JSONArray) linesObj.get(keyString), characterLines.get(keyString));
		}
	}

	private static void loadItems(JSONObject obj, DataBackedNPCData data) {
		loadItemsArray((JSONArray) obj.get("initial"), data.startingItems);
		loadItemsArray((JSONArray) obj.get("purchase"), data.purchasedItems);
	}

	private static void loadItemsArray(JSONArray arr, List<ItemAmount> items) {
		for (Object mem : arr) {
			JSONObject obj = (JSONObject) mem;
			items.add(readItem(obj));
		}
	}

	private static void loadGrowthResources(JSONObject obj, Growth growth) {
		growth.stamina = JSONUtils.readFloat(obj, "stamina");
		growth.bonusStamina = JSONUtils.readFloat(obj, "bonusStamina");
		growth.arousal = JSONUtils.readFloat(obj, "arousal");
		growth.bonusArousal = JSONUtils.readFloat(obj, "bonusArousal");
		growth.mojo = JSONUtils.readFloat(obj, "mojo");
		growth.bonusMojo = JSONUtils.readFloat(obj, "bonusMojo");
		growth.willpower = JSONUtils.readFloat(obj, "willpower");
		growth.bonusWillpower = JSONUtils.readFloat(obj, "bonusWillpower");
		growth.bonusAttributes = JSONUtils.readInteger(obj, "bonusPoints");
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
			growth.addTrait(JSONUtils.readInteger(obj, "level"), Trait.valueOf((String)obj.get("trait")));
		}
	}

	private static void loadTraits(JSONArray arr, List<Trait> traits) {
		for (Object traitString : arr) {
			Trait trait = Trait.valueOf((String)traitString);
			traits.add(trait);
		}
	}

}