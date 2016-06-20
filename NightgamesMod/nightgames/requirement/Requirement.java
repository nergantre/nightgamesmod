package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.nskills.tags.SkillRequirement;

public interface Requirement {
    boolean meets(Combat c, Character self, Character other);

    String getKey();

    default SkillRequirement toSkillRequirement() {
        return (results, value) -> this.meets(results.getCombat(), results.getSelf().getCharacter(), results.getOther().getCharacter());
    }
}
