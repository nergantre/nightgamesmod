package nightgames.start;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 *
 */
public class ConfigurationUtils {
    protected static <T> Optional<T> mergeOptionals(Optional<T> primary, Optional<T> secondary) {
        if (primary.isPresent()) {
            return primary;
        } else {
            return secondary;
        }
    }

    protected static <T> Collection<T> mergeCollections(Collection<T> primary, Collection<T> secondary) {
        Collection<T> list = new ArrayList<>(primary);
        list.addAll(secondary);
        return list;
    }

    protected static <T> Optional<Collection<T>> mergeCollections(Optional<Collection<T>> primary,
                    Optional<Collection<T>> secondary) {
        if (!primary.isPresent() && !secondary.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(mergeCollections(primary.orElse(Collections.emptySet()),
                        secondary.orElse(Collections.emptySet())));
    }
}
