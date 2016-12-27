package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class ReversePetThreesome extends PetThreesome {
    public ReversePetThreesome(String name, Character self, int cooldown) {
        super(name, self, cooldown);
    }

    public ReversePetThreesome(Character self) {
        super("Reverse Threesome", self, 0);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target);
    }

    public BodyPart getSelfOrgan(Character fucker, Combat c) {
        BodyPart res = fucker.body.getRandomPussy();
        return res;
    }

    public BodyPart getTargetOrgan(Character target) {
        if (target.hasDick()) {
            return target.body.getRandomCock();            
        }
        return target.body.getRandomPussy();
    }

    @Override
    public Skill copy(Character user) {
        return new ReversePetThreesome(user);
    }
}
