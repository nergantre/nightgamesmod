package nightgames.characters;

import java.util.Optional;

/**
 * Attribute levelup constraint for capping attributes. If no maximum value is provided, attribute growth is effectively unlimited.
 */
public class MaxAttribute implements PreferredAttribute {
    private final Attribute att;
    private final int max;

    public MaxAttribute(Attribute att) {
        this(att, Integer.MAX_VALUE);
    }

    public MaxAttribute(Attribute att, int max) {
        this.att = att;
        this.max = max;
    }

    @Override public Optional<Attribute> getPreferred(Character c) {
        if (c.get(att) < max) {
            return Optional.of(att);
        } else {
            return Optional.empty();
        }
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MaxAttribute that = (MaxAttribute) o;

        return max == that.max && att == that.att;

    }

    @Override public int hashCode() {
        int result = att.hashCode();
        result = 31 * result + max;
        return result;
    }
}
