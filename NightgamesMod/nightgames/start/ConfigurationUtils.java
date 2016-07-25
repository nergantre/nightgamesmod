package nightgames.start;

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
}
