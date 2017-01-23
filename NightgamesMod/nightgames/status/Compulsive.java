package nightgames.status;

import nightgames.characters.Character;
import nightgames.combat.Combat;

import java.util.Optional;

public interface Compulsive {

    String describe(Combat c, Situation sit);
    
    default void doPostCompulsion(Combat c, Situation sit) {
        // NOP
    }
    
    default boolean applicable(Situation sit) {
        return true;
    }
    
    static Optional<String> describe(Combat c, Character self, Situation sit) {
        if (!self.is(Stsflag.compelled)) {
            return Optional.empty();
        }
        Compulsive comp = (Compulsive) self.getStatus(Stsflag.compelled);
        if (!comp.applicable(sit)) {
            return Optional.empty();
        }
        return Optional.of(comp.describe(c, sit));
    }
    
    static void doPostCompulsion(Combat c, Character self, Situation sit) {
        ((Compulsive) self.getStatus(Stsflag.compelled)).doPostCompulsion(c, sit);
    }
    
    enum Situation {
        STANCE_FLIP,
        PUNISH_PAIN,
        PREVENT_ESCAPE,
        PREVENT_STRUGGLE,
        PREVENT_REMOVE_BOMB
    }
    
}
