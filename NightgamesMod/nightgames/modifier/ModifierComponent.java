package nightgames.modifier;

import org.json.simple.JSONObject;

public interface ModifierComponent<T extends ModifierComponent<T>> {

	String name();

	T instance(JSONObject obj);

}
