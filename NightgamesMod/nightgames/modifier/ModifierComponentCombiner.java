package nightgames.modifier;

/**
 * TODO: Write class-level documentation.
 */
public interface ModifierComponentCombiner<T extends ModifierCategory<T>> {
    T combine(T first, T next);

    T template();

    T nullModifier();
}
