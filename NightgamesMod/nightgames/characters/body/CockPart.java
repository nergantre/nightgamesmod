package nightgames.characters.body;

import nightgames.characters.Character;

public interface CockPart extends BodyPart {
    double getSize();

    BodyPart applyMod(CockMod mod);

    // Returns the result of Enum.name().
    String getName();

    @Override
    public default double getFemininity(Character self) {
        return -3;
    }
}
