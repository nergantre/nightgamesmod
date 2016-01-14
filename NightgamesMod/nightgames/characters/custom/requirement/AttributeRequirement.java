package nightgames.characters.custom.requirement;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public class AttributeRequirement implements CustomRequirement {
    Attribute att;
    int amount;

    public AttributeRequirement(Attribute att, int amount) {
        this.att = att;
        this.amount = amount;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.get(att) >= amount;
    }
}
