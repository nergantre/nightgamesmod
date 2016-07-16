package nightgames.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Provides a Type for collections that lets Gson.fromJson() get around type erasure.
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
