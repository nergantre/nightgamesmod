package nightgames.global;

import org.json.simple.JSONObject;

public class JSONUtils {
	public static int readInteger(JSONObject struct, String key) {
		return ((Number)struct.get(key)).intValue();
	}

	public static float readFloat(JSONObject struct, String key) {
		return ((Number)struct.get(key)).floatValue();
	}
	public static String readString(JSONObject struct, String key) {
		return (String)struct.get(key);
	}
}
