package nightgames.characters.body;

import nightgames.characters.Character;
import nightgames.global.Global;

public interface CockPart extends BodyPart {
    double getSize();

    BodyPart applyMod(CockMod mod);

    // Returns the result of Enum.name().
    String getName();

    @Override
    public default double getFemininity(Character self) {
        return -3;
    }

    default PussyPart getEquivalentPussy() {
        for (PussyPart pussy : PussyPart.values()) {
            CockMod equivalentMod = pussy.getEquivalentCockMod();
            if (equivalentMod != CockMod.error && equivalentMod.equals(getMod(Global.noneCharacter()))) {
                return pussy;
            }
        }
        return PussyPart.normal;
    }
}
