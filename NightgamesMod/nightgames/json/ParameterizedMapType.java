package nightgames.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * TODO: Write class-level documentation.
 */
class ParameterizedMapType<K, V> implements ParameterizedType {
    private Class<K> keyType;
    private Class<V> valueType;

    public ParameterizedMapType(Class<K> keyClazz, Class<V> valueClazz) {
        keyType = keyClazz;
        valueType = valueClazz;
    }

    @Override public Type[] getActualTypeArguments() {
        return new Type[] {keyType, valueType};
    }

    @Override public Type getRawType() {
        return Map.class;
    }

    @Override public Type getOwnerType() {
        return null;
    }
}
