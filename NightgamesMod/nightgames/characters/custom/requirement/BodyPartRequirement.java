package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class BodyPartRequirement implements CustomRequirement {
    private String type;

    public BodyPartRequirement(String type) {
        this.type = type;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.body.has(type);
    }
}
