package nightgames.items.clothing;

import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nightgames.characters.CharacterSex;
import nightgames.characters.Trait;
import nightgames.json.JsonUtils;

public class JsonClothingLoader {
    /*
     * Example: { "displayName":"shirt", "buffs":[ ], "price":275, "prefix":"a ", "attributes":[ ], "layer":1, "slots" : ["top"], "sex" : ["all"], "hotness":0.1, "name":"shirt", "toughness":5, "exposure":0.4 },
     */
    protected static Clothing loadClothingFromJson(JsonObject clothingObj) {
        Clothing clothing = new Clothing();
        clothing.id = clothingObj.get("name").getAsString();
        clothing.name = clothingObj.get("displayName").getAsString();
        clothing.price = clothingObj.get("price").getAsInt();
        clothing.setLayer(clothingObj.get("layer").getAsInt());
        clothing.prefix = clothingObj.get("prefix").getAsString();
        clothing.hotness = clothingObj.get("hotness").getAsFloat();
        clothing.exposure = clothingObj.get("exposure").getAsFloat();
        clothing.dc = clothingObj.get("toughness").getAsInt();
        clothing.buffs = new HashSet<>(JsonUtils.collectionFromJson(clothingObj.getAsJsonArray("buffs"), Trait.class));
        clothing.attributes = new HashSet<>(JsonUtils.collectionFromJson(clothingObj.getAsJsonArray("attributes"), ClothingTrait.class));
        clothing.sex = new HashSet<>(loadGenderWhitelist(clothingObj.getAsJsonArray("sex")));
        clothing.slots = new HashSet<>(JsonUtils.collectionFromJson(clothingObj.getAsJsonArray("slots"), ClothingSlot.class));
        clothing.stores = new HashSet<>(JsonUtils.stringsFromJson(clothingObj.getAsJsonArray("shops")));
        return clothing;
    }

    private static Set<CharacterSex> loadGenderWhitelist(JsonArray arr) {
        Collection<String> genderStrings = JsonUtils.stringsFromJson(arr);
        if (genderStrings.contains("all")) {
            return new HashSet<>(Arrays.asList(CharacterSex.values()));
        } else {
            return genderStrings.stream().map(CharacterSex::valueOf).collect(Collectors.toSet());
        }
    }

    public static List<Clothing> loadClothingListFromJson(JsonArray clothingArr) {
        List<Clothing> results = new ArrayList<>();
        for (JsonElement element: clothingArr) {
            results.add(loadClothingFromJson(element.getAsJsonObject()));
        }
        return results;
    }
}
