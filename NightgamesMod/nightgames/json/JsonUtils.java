package nightgames.json;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import nightgames.characters.body.BodyPart;
import nightgames.characters.body.mods.PartMod;
import nightgames.items.clothing.Clothing;

public class JsonUtils {
    private static Gson gson = null;

    public static <T> Collection<T> collectionFromJson(JsonArray array, Class<T> clazz) {
        Type type = new ParameterizedCollectionType<>(clazz);
        return getGson().fromJson(array, type);
    }

    public static JsonArray jsonFromCollection(Collection<?> collection) {
        return getGson().toJsonTree(collection).getAsJsonArray();
    }

    /**
     * Convenience method for turning JsonObjects into maps
     *
     * @param object A JsonObject with some number of properties and values.
     * @return A map as constructed by Gson
     */
    public static <K, V> Map<K, V> mapFromJson(JsonObject object, Class<K> keyClazz, Class<V> valueClazz) {
        Type type = new ParameterizedMapType<>(keyClazz, valueClazz);
        return getGson().fromJson(object, type);
    }

    public static JsonObject JsonFromMap(Map<?, ?> map) {
        return getGson().toJsonTree(map).getAsJsonObject();
    }

    public static Optional<JsonElement> getOptional(JsonObject object, String key) {
        return Optional.ofNullable(object.get(key));
    }

    public static Optional<JsonArray> getOptionalArray(JsonObject object, String key) {
        return getOptional(object, key).map(JsonElement::getAsJsonArray);
    }

    public static Optional<JsonObject> getOptionalObject(JsonObject object, String key) {
        return getOptional(object, key).map(JsonElement::getAsJsonObject);
    }

    public static Collection<String> stringsFromJson(JsonArray array) {
        return JsonUtils.collectionFromJson(array, String.class);
    }

    public static JsonElement rootJson(Path path) throws JsonParseException, IOException {
        Reader reader = Files.newBufferedReader(path);
        return rootJson(reader);
    }

    public static JsonElement rootJson(Reader reader) throws JsonParseException {
        JsonParser parser = new JsonParser();
        return parser.parse(reader);
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting()
                            .registerTypeAdapter(Clothing.class, new ClothingAdaptor())
                            .registerTypeAdapter(BodyPart.class, new BodyPartAdapter())
                            .registerTypeAdapter(PartMod.class, new PartModAdapter())
                            .create();
        }
        return gson;
    }
}
