package nightgames.pet.arms;

import java.util.Arrays;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.HeatRay;

public class HeatCannon extends RoboArm {

    public HeatCannon(RoboArmManager manager, Character owner) {
        super(manager, owner, ArmType.HEAT_RAY);
    }

    @Override
    List<ArmSkill> getSkills(Combat c, Character owner, Character target) {
        return Arrays.asList(new HeatRay());
    }

}
