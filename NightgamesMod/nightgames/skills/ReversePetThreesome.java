package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class ReversePetThreesome extends PetThreesome {
    public ReversePetThreesome(Character self) {
        super("Reverse Threesome", self, 0);
    }

    public BodyPart getSelfOrgan(Character fucker, Combat c) {
        BodyPart res = fucker.body.getRandomPussy();
        return res;
    }

    public BodyPart getTargetOrgan(Character target) {
        return target.body.getRandomCock();
    }
}
