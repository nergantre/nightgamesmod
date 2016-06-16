package nightgames.start;

import java.util.Optional;

/**
 * Created by Ryplinn on 6/14/2016.
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
