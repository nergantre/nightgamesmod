package nightgames.global;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class JSONUtils {
    public static int readInteger(JSONObject struct, String key) {
        return struct.get(key) == null ? 0 : ((Number) struct.get(key)).intValue();
    }

    public static float readFloat(JSONObject struct, String key) {
        return ((Number) struct.get(key)).floatValue();
    }

    public static String readString(JSONObject struct, String key) {
        return (String) struct.get(key);
    }

    public static boolean readBoolean(JSONObject struct, String key) {
        return ((Boolean) struct.get(key)).booleanValue();
    }
    
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> readOptional(JSONObject struct, String key) {
        return Optional.ofNullable((T) struct.get(key));
    }
    
    public static List<String> loadStringsFromArr(JSONObject obj, String name) {
        List<String> arr = new ArrayList<>();
        JSONArray savedArr = (JSONArray) obj.get(name);
        for (Object elem : savedArr) {
            String val = (String) elem;
            arr.add(val);
        }
        return arr;
    }

    public static <T extends Enum<T>> Set<T> loadEnumsFromArr(JSONObject obj, String name, Class<T> enumClass) {
        Set<T> arr = new HashSet<>();
        JSONArray savedArr = (JSONArray) obj.get(name);
        for (Object elem : savedArr) {
            String key = (String) elem;
            arr.add(Enum.valueOf(enumClass, key));
        }
        return arr;
    }

    public static <T extends Enum<T>> Set<T> loadEnumsFromArrWithExtras(JSONObject obj, String name,
                    Map<String, List<T>> extras, Class<T> enumClass) {
        Set<T> res = new HashSet<>();
        JSONArray savedArr = (JSONArray) obj.get(name);
        for (Object elem : savedArr) {
            String key = (String) elem;
            if (extras.containsKey(key)) {
                res.addAll(extras.get(key));
            } else {
                res.add(Enum.valueOf(enumClass, key));
            }
        }
        return res;
    }

    public static JSONObject rootFromFile(Path path) {
        JSONObject root;
        try (Reader read = Files.newBufferedReader(path)) {
            root = (JSONObject) JSONValue.parseWithException(read);
        } catch (IOException|ParseException e) {
            throw new RuntimeException("Error reading JSON file: " + e.getMessage());
        }

        return root;
    }

    public static <T> Optional<T> getIfExists(JSONObject obj, String key, Function<Object, T> f) {
        if (!obj.containsKey(key))
            return Optional.empty();
        return Optional.ofNullable(f.apply(obj.get(key)));
    }
}
