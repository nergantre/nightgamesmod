package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class OrgasmRequirement implements CustomRequirement {
    int number;

    public OrgasmRequirement(int number) {
        this.number = number;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.orgasms >= number;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OrgasmRequirement that = (OrgasmRequirement) o;

        return number == that.number;

    }

    @Override public int hashCode() {
        return number;
    }
}
