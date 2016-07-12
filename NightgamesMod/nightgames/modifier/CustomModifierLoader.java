package nightgames.modifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.*;

import nightgames.Resources.ResourceLoader;
import nightgames.characters.Player;
import nightgames.global.Global;
import nightgames.json.JsonUtils;
import nightgames.items.clothing.Clothing;
import nightgames.modifier.action.ActionModifier;
import nightgames.modifier.clothing.ClothingModifier;
import nightgames.modifier.item.ItemModifier;
import nightgames.modifier.skill.SkillModifier;
import nightgames.modifier.status.StatusModifier;

public final class CustomModifierLoader {
    private CustomModifierLoader() {
        // TODO Auto-generated constructor stub
    }

    public static Modifier readModifier(JsonObject object) {
        int bonus = object.get("bonus").getAsInt();
        String name = object.get("name").getAsString();
        String intro = object.get("intro").getAsString();
        String acceptance = object.get("acceptance").getAsString();

        ActionModifier action = JsonUtils.getOptionalArray(object, "action")
                        .map(array -> readModifiers(array, ActionModifier.combiner, ActionModifier.loader))
                        .orElse(ActionModifier.combiner.nullModifier());
        SkillModifier skill = JsonUtils.getOptionalArray(object, "skill")
                        .map(array -> readModifiers(array, SkillModifier.combiner, SkillModifier.loader))
                        .orElse(SkillModifier.combiner.nullModifier());
        ClothingModifier clothing = JsonUtils.getOptionalArray(object, "clothing")
                        .map(array -> readModifiers(array, ClothingModifier.combiner, ClothingModifier.loader))
                        .orElse(ClothingModifier.combiner.nullModifier());
        ItemModifier item = JsonUtils.getOptionalArray(object, "item")
                        .map(array -> readModifiers(array, ItemModifier.combiner, ItemModifier.loader))
                        .orElse(ItemModifier.combiner.nullModifier());

        return new BaseModifier(clothing, item, StatusModifier.combiner.nullModifier(), skill, action,
                        BaseModifier.EMPTY_CONSUMER) {

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

    private static <T extends ModifierCategory> T readModifierComponent(JsonObject object,
                    ModifierCategoryLoader<T> template) {

        return template.instance(object);
    }

    private static <T extends ModifierCategory> T readModifiers(JsonArray array, ModifierComponentCombiner<T> combiner, ModifierCategoryLoader<T> loader) {
        Collection<JsonObject> objects = new ArrayList<>();
        array.forEach(element -> objects.add(element.getAsJsonObject()));
        return objects.stream().map(obj -> CustomModifierLoader.readModifierComponent(obj, loader)).reduce(combiner::combine).orElse(combiner.nullModifier());
    }

    public static void main(String[] args) throws IOException, JsonParseException {
        InputStream in = ResourceLoader.getFileResourceAsStream("test_modifier.json");
        JsonObject obj = JsonUtils.rootJson(new InputStreamReader(in)).getAsJsonObject();
        Clothing.buildClothingTable();
        Global.buildParser();
        Global.buildModifierPool();
        Global.buildActionPool();
        Global.buildSkillPool(new Player("player"));
        Modifier mod = readModifier(obj);
        System.out.println(mod);
    }
}
