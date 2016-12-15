package nightgames.status.addiction;

import nightgames.characters.Character;
import nightgames.characters.Player;

public enum AddictionType {
    MAGIC_MILK(MagicMilkAddiction::new),
    ZEAL(ZealAddiction::new),
    CORRUPTION(Corruption::new),
    BREEDER(Breeder::new),
    MIND_CONTROL(MindControl::new),
    DOMINANCE(Dominance::new)
    ;
    
    interface AddictionConstructor {
        Addiction construct(Player affected, Character supplier, Float magnitude);
    }
    
    private final AddictionConstructor constructor;

    private AddictionType(AddictionConstructor constructor) {
        this.constructor = constructor;
    }

    public Addiction build(Player affected, Character cause) {
        return build(affected, cause, .01f);
    }
    
    public Addiction build(Player affected, Character cause, float mag) {
        return constructor.construct(affected, cause, mag);
    }
}
