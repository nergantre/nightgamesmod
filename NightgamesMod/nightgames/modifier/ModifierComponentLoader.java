package nightgames.modifier;

import com.google.gson.JsonObject;

/**
 * TODO: Write class-level documentation.
 */
public interface ModifierComponentLoader<T extends ModifierCategory> {
    String name();

    default boolean isType(String type) {
        return type.equals(name());
    }

    T instance(JsonObject object);
}
