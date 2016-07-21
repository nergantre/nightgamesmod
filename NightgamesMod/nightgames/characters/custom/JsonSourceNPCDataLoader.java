package nightgames.characters.custom;

import com.google.gson.*;
import nightgames.characters.*;
import nightgames.characters.body.Body;
import nightgames.characters.custom.effect.CustomEffect;
import nightgames.characters.custom.effect.MoneyModEffect;
import nightgames.json.JsonUtils;
import nightgames.items.Item;
import nightgames.items.ItemAmount;
import nightgames.items.clothing.Clothing;
import nightgames.requirements.*;
import nightgames.skills.Skill;
import nightgames.stance.Stance;
import nightgames.status.Stsflag;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonSourceNPCDataLoader {
    private static JsonRequirementLoader requirementLoader = new JsonRequirementLoader();

    protected static void loadResources(JsonObject resources, Stats stats) {
        stats.stamina = resources.get("stamina").getAsFloat();
        stats.arousal = resources.get("arousal").getAsFloat();
        stats.mojo = resources.get("mojo").getAsFloat();
        stats.willpower = resources.get("willpower").getAsFloat();
    }

    public static NPCData load(InputStream in) throws JsonParseException {
        JsonObject object = JsonUtils.rootJson(new InputStreamReader(in)).getAsJsonObject();
        return load(object);
    }

    public static NPCData load(JsonObject object) {
        DataBackedNPCData data = new DataBackedNPCData();
        data.name = object.get("name").getAsString();
        data.type = object.get("type").getAsString();
        data.trophy = Item.valueOf(object.get("trophy").getAsString());
        data.plan = Plan.valueOf(object.get("plan").getAsString());

        // load outfit
        JsonObject outfit = object.getAsJsonObject("outfit");
        JsonArray top = outfit.getAsJsonArray("top");
        for (JsonElement clothing : top) {
            data.top.push(Clothing.getByID(clothing.getAsString()));
        }
        JsonArray bottom = outfit.getAsJsonArray("bottom");
        for (JsonElement clothing : bottom) {
            data.bottom.push(Clothing.getByID(clothing.getAsString()));
        }

        // load stats
        JsonObject stats = object.getAsJsonObject("stats");
        // load base stats
        JsonObject baseStats = stats.getAsJsonObject("base");
        data.stats.level = baseStats.get("level").getAsInt();
        // load attributes
        data.stats.attributes.putAll(JsonUtils
                        .mapFromJson(baseStats.getAsJsonObject("attributes"), Attribute.class, Integer.class));

        loadResources(baseStats.getAsJsonObject("resources"), data.stats);
        loadTraits(baseStats.getAsJsonArray("traits"), data.stats.traits);
        loadGrowth(stats.getAsJsonObject("growth"), data.growth);
        loadPreferredAttributes(stats.getAsJsonObject("growth").getAsJsonArray("preferredAttributes"),
                        data.preferredAttributes);
        loadItems(object.getAsJsonObject("items"), data);
        loadAllLines(object.getAsJsonObject("lines"), data.characterLines);
        data.portraits = loadLines(object.getAsJsonArray("portraits"));
        loadRecruitment(object.getAsJsonObject("recruitment"), data.recruitment);
        data.body = Body.load(object.getAsJsonObject("body"), null);
        data.sex = object.get("sex").getAsString();

        JsonUtils.getOptionalArray(object, "ai-modifiers").ifPresent(arr -> loadAiModifiers(arr, data.aiModifiers));

        JsonUtils.getOptional(object, "start").map(JsonElement::getAsBoolean).ifPresent(b -> data.isStartCharacter = b);

        data.aiModifiers.setMalePref(JsonUtils.getOptional(object, "male-pref").map(JsonElement::getAsDouble));

        JsonUtils.getOptionalArray(object, "comments").ifPresent(arr -> loadComments(arr, data));

        return data;
    }

    protected static void loadRecruitment(JsonObject object, RecruitmentData recruitment) {
        recruitment.introduction = object.get("introduction").getAsString();
        recruitment.action = object.get("action").getAsString();
        recruitment.confirm = object.get("confirm").getAsString();
        recruitment.requirement = requirementLoader.loadRequirements(object.getAsJsonObject("requirements"));
        loadEffects(object.getAsJsonArray("cost"), recruitment.effects);
    }

    protected static void loadEffects(JsonArray jsonArray, List<CustomEffect> effects) {
        for (JsonElement element : jsonArray) {
            JsonObject obj = element.getAsJsonObject();
            JsonUtils.getOptional(obj, "modMoney").ifPresent(e -> effects.add(new MoneyModEffect(e.getAsInt())));
        }
    }

    private static void loadAllLines(JsonObject linesObj, Map<String, List<CustomStringEntry>> characterLines) {
        for (Map.Entry<String, JsonElement> e : linesObj.entrySet()) {
            String key = e.getKey();
            List<CustomStringEntry> lines = loadLines(linesObj.getAsJsonArray(key));
            characterLines.put(key, lines);
        }
    }

    private static List<CustomStringEntry> loadLines(JsonArray linesArr) {
        List<CustomStringEntry> entries = new ArrayList<>();
        for (JsonElement element : linesArr) {
            entries.add(readLine(element.getAsJsonObject()));
        }
        return entries;
    }

    protected static CustomStringEntry readLine(JsonObject object) {
        CustomStringEntry entry = new CustomStringEntry(object.get("text").getAsString());
        entry.requirements = JsonUtils.getOptionalObject(object, "requirements")
                        .map(obj -> requirementLoader.loadRequirements(obj)).orElse(new ArrayList<>());
        return entry;
    }

    private static void loadItems(JsonObject obj, DataBackedNPCData data) {
        loadItemsArray(obj.getAsJsonArray("initial"), data.startingItems);
        loadItemsArray(obj.getAsJsonArray("purchase"), data.purchasedItems);
    }

    private static void loadItemsArray(JsonArray arr, List<ItemAmount> items) {
        for (Object mem : arr) {
            JsonObject obj = (JsonObject) mem;
            items.add(readItem(obj));
        }
    }

    protected static ItemAmount readItem(JsonObject obj) {
        return JsonUtils.gson.fromJson(obj, ItemAmount.class);
    }

    protected static void loadGrowthResources(JsonObject object, Growth growth) {
        growth.stamina = object.get("stamina").getAsFloat();
        growth.bonusStamina = object.get("bonusStamina").getAsFloat();
        growth.arousal = object.get("arousal").getAsFloat();
        growth.bonusArousal = object.get("bonusArousal").getAsFloat();
        growth.mojo = object.get("mojo").getAsFloat();
        growth.bonusMojo = object.get("bonusMojo").getAsFloat();
        growth.willpower = object.get("willpower").getAsFloat();
        growth.bonusWillpower = object.get("bonusWillpower").getAsFloat();
        growth.bonusAttributes = object.get("bonusPoints").getAsInt();
        JsonArray points = object.getAsJsonArray("points");
        int defaultPoints = 3;
        for (int i = 0; i < growth.attributes.length; i++) {
            if (i < points.size()) {
                growth.attributes[i] = points.get(i).getAsInt();
                defaultPoints = growth.attributes[i];
            } else {
                growth.attributes[i] = defaultPoints;
            }
        }
    }

    private static void loadGrowth(JsonObject obj, Growth growth) {
        loadGrowthResources(obj.get("resources").getAsJsonObject(), growth);
        loadGrowthTraits(obj.get("traits").getAsJsonArray(), growth);
    }

    protected static void loadPreferredAttributes(JsonArray arr, List<PreferredAttribute> preferredAttributes) {
        for (JsonElement element : arr) {
            JsonObject obj = element.getAsJsonObject();
            Attribute att = JsonUtils.gson.fromJson(obj.get("attribute"), Attribute.class);
            final int max = JsonUtils.getOptional(obj, "max").map(JsonElement::getAsInt).orElse(Integer.MAX_VALUE);
            preferredAttributes.add(new MaxAttribute(att, max));
        }
    }

    private static void loadGrowthTraits(JsonArray arr, Growth growth) {
        for (JsonElement element : arr) {
            JsonObject obj = element.getAsJsonObject();
            growth.addTrait(obj.get("level").getAsInt(), JsonUtils.gson.fromJson(obj.get("trait"), Trait.class));
        }
    }

    private static void loadTraits(JsonArray array, List<Trait> traits) {
        traits.addAll(JsonUtils.collectionFromJson(array, Trait.class));
    }

    @SuppressWarnings("unchecked") protected static void loadAiModifiers(JsonArray arr, AiModifiers mods) {
        for (Object aiMod : arr) {
            JsonObject obj = (JsonObject) aiMod;
            String value = obj.get("value").getAsString();
            double weight = obj.get("weight").getAsFloat();
            String type = obj.get("type").getAsString();
            switch (type) {
                case "skill":
                    try {
                        mods.getAttackMods().put((Class<? extends Skill>) Class.forName(value), weight);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException("Skill not found: " + value);
                    }
                    break;
                case "position":
                    mods.getPositionMods().put(Stance.valueOf(value), weight);
                    break;
                case "self-status":
                    mods.getSelfStatusMods().put(Stsflag.valueOf(value), weight);
                    break;
                case "opponent-status":
                    mods.getOppStatusMods().put(Stsflag.valueOf(value), weight);
                    break;
                default:
                    throw new IllegalArgumentException("Type of AiModifier must be one of \"skill\", "
                                    + "\"position\", \"self-status\", or \"opponent-status\", " + "but was \"" + type
                                    + "\".");
            }
        }
    }

    @SuppressWarnings("unchecked") private static void loadComments(JsonArray arr, DataBackedNPCData data) {
        arr.forEach(e -> CommentSituation.parseComment(e.getAsJsonObject(), data.comments));
    }
}
