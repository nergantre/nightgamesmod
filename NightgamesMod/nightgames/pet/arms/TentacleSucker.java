package nightgames.pet.arms;

import java.util.Collections;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.DefabRay;

public class TentacleSucker extends TentacleArm {
    public TentacleSucker(ArmManager manager) {
        super(manager, ArmType.TENTACLE_SUCKER);
    }

    @Override
    List<ArmSkill> getSkills(Combat c, Character owner, Character target) {
        return Collections.singletonList(new DefabRay());
    }
}
