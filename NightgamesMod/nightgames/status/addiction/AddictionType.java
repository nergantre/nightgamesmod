package nightgames.status.addiction;

import java.util.function.BiFunction;
import nightgames.characters.Character;

public enum AddictionType {
    MAGIC_MILK(MagicMilkAddiction::new),
    ZEAL(ZealAddiction::new),
    CORRUPTION(Corruption::new)
    ;
    
    private final BiFunction<Character, Float, ? extends Addiction> constructor;
    
    private AddictionType(BiFunction<Character, Float, ? extends Addiction> constructor) {
        this.constructor = constructor;
    }
    
    public Addiction build(Character cause) {
        return build(cause, .01f);
    }
    
    public Addiction build(Character cause, float mag) {
        return constructor.apply(cause, mag);
    }
}
