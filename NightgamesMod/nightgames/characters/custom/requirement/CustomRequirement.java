package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.nskills.tags.SkillRequirement;

public interface CustomRequirement {
    boolean meets(Combat c, Character self, Character other);
    default SkillRequirement toSkillRequirement() {
        return (results, value) -> this.meets(results.getCombat(), results.getSelf().getCharacter(), results.getOther().getCharacter());
    }
}
