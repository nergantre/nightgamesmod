package nightgames.modifier;

import java.util.Collection;

import com.google.gson.JsonObject;

import nightgames.json.JsonUtils;

/**
 * TODO: Write class-level documentation.
 */
public interface ModifierCategoryLoader<T extends ModifierCategory<T>> {
    Collection<ModifierComponentLoader<T>> getTemplates();

    default T instance(JsonObject object) {
        String type = JsonUtils.getOptional(object, "type")
                        .orElseThrow(() -> new IllegalArgumentException("Modifier components must have a type"))
                        .getAsString();
        ModifierComponentLoader<T> loader = getTemplates().stream().filter(t -> t.isType(type)).findAny()
                        .orElseThrow(() -> new IllegalArgumentException("Unknown modifier component type: " + type));
        JsonObject value = JsonUtils.getOptionalObject(object, "value")
                        .orElseThrow(() -> new IllegalArgumentException("Modifier compoenents must have a value"));
        return loader.instance(value);
    }
}
