package nightgames.nskills.struct;

import java.util.Set;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;

public class CharacterResultStruct {
    public final Set<BodyPart> selfParts;
    public final Character character;

    public CharacterResultStruct(Set<BodyPart> selfParts, Character character) {
        super();
        this.selfParts = selfParts;
        this.character = character;
    }

    public Set<BodyPart> getParts() {
        return selfParts;
    }

    public Character getCharacter() {
        return character;
    }
}