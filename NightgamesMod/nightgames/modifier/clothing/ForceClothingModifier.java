package nightgames.modifier.clothing;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import nightgames.global.JSONUtils;
import nightgames.modifier.ModifierComponent;

public class ForceClothingModifier extends ClothingModifier
		implements ModifierComponent<ForceClothingModifier> {

	private final Set<String> ids;

	public ForceClothingModifier(String... ids) {
		this.ids = Collections
				.unmodifiableSet(new HashSet<>(Arrays.asList(ids)));
	}

	@Override
	public Set<String> forcedItems() {
		return ids;
	}

	@Override
	public String name() {
		return "force-clothing";
	}

	@Override
	public String toString() {
		return "Forced:" + ids.toString();
	}

	@Override
	public ForceClothingModifier instance(JSONObject obj) {
		if (obj.containsKey("clothing")) {
			Object raw = obj.get("clothing");
			if (raw instanceof JSONArray) {
				return new ForceClothingModifier(
						JSONUtils.loadStringsFromArr(obj, "clothing")
								.toArray(new String[] {}));
			} else if (raw instanceof String) {
				return new ForceClothingModifier((String) raw);
			} else {
				throw new IllegalArgumentException(
						"'clothing' item of 'force-clothing' must be String or String Array.");
			}
		} else {
			throw new IllegalArgumentException(
					"'force-clothing' element must have 'clothing' item");
		}
	}
}
