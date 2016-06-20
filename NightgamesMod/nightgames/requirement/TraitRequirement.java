package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class TraitRequirement implements Requirement {
    private final Trait trait;

    public TraitRequirement(Trait trait) {
        this.trait = trait;
    }

    public TraitRequirement(String trait) {
        this.trait = Trait.valueOf(trait);
    }

    @Override public String getKey() {
        return "trait";
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.has(trait);
    }
}
