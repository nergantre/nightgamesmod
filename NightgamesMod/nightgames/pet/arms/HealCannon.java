package nightgames.pet.arms;

import java.util.Collections;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.HealRay;

public class HealCannon extends RoboArm {

    public HealCannon(ArmManager manager) {
        super(manager, ArmType.HEAL_CANNON);
    }

    @Override
    List<ArmSkill> getSkills(Combat c, Character owner, Character target) {
        return Collections.singletonList(new HealRay());
    }

}
