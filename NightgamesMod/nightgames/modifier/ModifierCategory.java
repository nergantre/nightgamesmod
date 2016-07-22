package nightgames.modifier;

/**
 * TODO: Write class-level documentation.
 */
public interface ModifierCategory<T extends ModifierCategory> {
    T combine(T next);
}
