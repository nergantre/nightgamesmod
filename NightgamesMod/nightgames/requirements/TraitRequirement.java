package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class TraitRequirement extends BaseRequirement {
    private final Trait trait;

    public TraitRequirement(Trait trait) {
        this.trait = trait;
    }

    public TraitRequirement(String trait) {
        this.trait = Trait.valueOf(trait);
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return self.has(trait);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TraitRequirement that = (TraitRequirement) o;

        return trait == that.trait;

    }

    @Override public int hashCode() {
        return super.hashCode() * 31 + trait.hashCode();
    }
    
    public Trait getTrait() {
        return trait;
    }
}
