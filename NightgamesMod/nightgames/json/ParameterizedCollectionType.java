package nightgames.json;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO: Write class-level documentation.
 */
class ParameterizedCollectionType<T> implements ParameterizedType {
    private Class<T> wrapped;

    public ParameterizedCollectionType(Class<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override public Type[] getActualTypeArguments() {
        return new Type[] {wrapped};
    }

    @Override public Type getRawType() {
        return Collection.class;
    }

    @Override public Type getOwnerType() {
        return null;
    }
}
