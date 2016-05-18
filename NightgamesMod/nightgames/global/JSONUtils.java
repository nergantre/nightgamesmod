package nightgames.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
}
