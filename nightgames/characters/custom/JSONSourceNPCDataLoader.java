package nightgames.characters.custom;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import nightgames.characters.Plan;
import nightgames.items.Clothing;
import nightgames.items.Item;

public class JSONSourceNPCDataLoader {
	public static NPCData load(String path) throws ParseException,IOException {
		Object value = JSONValue.parse(new FileReader(path));
		DataBackedNPCData data = new DataBackedNPCData();
		try {
			JSONObject object = (JSONObject)value;
			data.name = (String) object.get("name");
			data.trophy = Item.valueOf((String)object.get("trophy"));
			data.plan = Plan.valueOf((String)object.get("plan"));
			JSONObject outfit = (JSONObject) object.get("outfit");
			JSONArray top = (JSONArray) outfit.get("top");
			for (Object clothing : top) {
				data.top.push(Clothing.valueOf((String)clothing));
			}
			JSONArray bottom = (JSONArray) outfit.get("bottom");
			for (Object clothing : bottom) {
				data.bottom.push(Clothing.valueOf((String)clothing));
			}
		} catch (ClassCastException e) {
			throw new ParseException("Badly formatted JSON character: " + e.getMessage(), 0);
		} catch (IllegalArgumentException e) {
			throw new ParseException("Nonexistent value: " + e.getMessage(), 0);
		}
		return data;
	}
}