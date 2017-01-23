package nightgames.pet.arms;

import java.util.Collections;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.TentacleSquirt;

public class TentacleSquirter extends TentacleArm {
    public TentacleSquirter(ArmManager manager) {
        super(manager, ArmType.TENTACLE_SQUIRTER);
    }

    @Override
    List<ArmSkill> getSkills(Combat c, Character owner, Character target) {
        return Collections.singletonList(new TentacleSquirt());
    }
}
