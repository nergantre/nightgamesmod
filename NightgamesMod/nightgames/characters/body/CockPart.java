package nightgames.characters.body;

import nightgames.characters.Character;
import nightgames.characters.body.mods.PartMod;

public interface CockPart extends BodyPart {
    int getSize();
    BodyPart applyMod(PartMod mod);

    @Override
    public default double getFemininity(Character self) {
        return -3;
    }

    PussyPart getEquivalentPussy();

    default String adjective(Character c) {
        return "phallic";
    }

    BodyPart removeAllMods();
}
