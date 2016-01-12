package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class SpecificBodyPartRequirement implements CustomRequirement {
	private BodyPart part;

	public SpecificBodyPartRequirement(BodyPart part) {
		this.part = part;
	}

	@Override
	public boolean meets(Combat c, Character self, Character other) {
		return self.body.contains(part);
	}
}
