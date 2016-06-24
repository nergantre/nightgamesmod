package nightgames.characters.custom.requirement;

import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class NotRequirement implements CustomRequirement {
    private List<CustomRequirement> req;

    /**
     * Requirement that checks that ALL the sub requirements are NOT met
     * 
     * @param req
     */
    public NotRequirement(List<CustomRequirement> req) {
        this.req = req;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return req.stream().allMatch(r -> !r.meets(c, self, other));
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        NotRequirement that = (NotRequirement) o;

        return req.equals(that.req);

    }

    @Override public int hashCode() {
        return req.hashCode();
    }
}
