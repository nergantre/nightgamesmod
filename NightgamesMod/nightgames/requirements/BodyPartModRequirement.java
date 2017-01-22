package nightgames.requirements;

import nightgames.characters.Character;
import nightgames.characters.body.mods.PartMod;
import nightgames.combat.Combat;

/**
 * Returns true if character self has at least one of the bodypart.
 */
public class BodyPartModRequirement extends BaseRequirement {
    private final String type;
    private final PartMod mod;

    public BodyPartModRequirement(String type, PartMod mod) {
        this.type = type;
        this.mod = mod;
    }

    @Override public boolean meets(Combat c, Character self, Character other) {
        return self.body.has(type) && self.body.getRandom(type).moddedPartCountsAs(self, mod);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BodyPartModRequirement that = (BodyPartModRequirement) o;

        return type.equals(that.type) && mod.equals(that.mod);

    }

    @Override public int hashCode() {
        return super.hashCode() * 31 + String.format("%s:%s", type, mod.getModType()).hashCode();
    }
    
    public String getType() {
        return type;
    }
}
