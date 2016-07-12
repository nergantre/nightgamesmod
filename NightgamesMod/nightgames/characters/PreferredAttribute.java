package nightgames.characters;

import java.util.Optional;

/**
 * Interface for constraints on attribute growth during character levelup.
 */
public interface PreferredAttribute {
    Optional<Attribute> getPreferred(Character c);
}
