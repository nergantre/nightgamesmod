package nightgames.status;

import nightgames.characters.Character;

public interface Resistance {
	String resisted(Character character, Status s);
}