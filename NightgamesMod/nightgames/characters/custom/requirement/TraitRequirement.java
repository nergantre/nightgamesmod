package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class TraitRequirement implements CustomRequirement {
	Trait trait;

	public TraitRequirement(Trait trait) {
		this.trait = trait;
	}

	@Override
	public boolean meets(Combat c, Character self, Character other) {
		return self.has(trait);
	}
}
