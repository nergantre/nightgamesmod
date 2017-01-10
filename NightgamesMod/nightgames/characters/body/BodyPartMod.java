package nightgames.characters.body;

import nightgames.characters.Character;

public interface BodyPartMod {
    BodyPartMod noMod = () -> "none";

    String getModType();

    default boolean countsAs(Character self, BodyPartMod part) {
        return getModType().equals(part.getModType());
    }
}
