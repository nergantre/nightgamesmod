package nightgames.characters.body;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public interface BodyPartMod {
    BodyPartMod noMod = () -> "none";

    String getModType();

    public default boolean partHasThisMod(BodyPart other) {
        if (other != null) {
            return equals(other.getMod());
        }
        return false;
    }
}
