package nightgames.status;

import nightgames.characters.body.BodyPart;
import nightgames.characters.Character;

public interface InsertedStatus {
    BodyPart getHolePart();
    Character getReceiver();
    BodyPart getStickPart();
    Character getPitcher();
}
