package nightgames.status;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public interface Resistance {
    String resisted(Combat combat, Character character, Status s);
}
