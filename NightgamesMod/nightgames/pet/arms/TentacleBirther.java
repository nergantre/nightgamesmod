package nightgames.pet.arms;

import java.util.Collections;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.DefabRay;

public class TentacleBirther extends TentacleArm {

    public TentacleBirther(ArmManager manager) {
        super(manager, ArmType.TENTACLE_BIRTHER);
    }

    @Override
    List<ArmSkill> getSkills(Combat c, Character owner, Character target) {
        return Collections.singletonList(new DefabRay());
    }
}
