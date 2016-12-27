package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.PetCharacter;

public class PetInitiatedReverseThreesome extends ReversePetThreesome {
    public PetInitiatedReverseThreesome(Character self) {
        super("Initiate Reverse Threesome", self, 0);
    }

    @Override
    public float priorityMod(Combat c) {
        return 2.0f;
    }

    @Override
    public Skill copy(Character user) {
        return new PetInitiatedReverseThreesome(user);
    }

    protected Character getFucker(Combat c) {
        return getSelf();
    }

    protected Character getMaster(Combat c) {
        if (getSelf() instanceof PetCharacter) {
            return ((PetCharacter)getSelf()).getSelf().owner();
        }
        return null;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && c.getStance().time >= 2 && getSelf() instanceof PetCharacter;
    }

    @Override
    public String describe(Combat c) {
        return "Fucks the opponent as a pet.";
    }
}
