package nightgames.pet.arms;

import java.util.Arrays;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.StabilizerIdle;

public class Stabilizer extends RoboArm {

    Stabilizer(RoboArmManager manager, Character owner) {
        super(manager, owner, ArmType.STABILIZER);
    }

    @Override
    List<ArmSkill> getSkills(Combat c, Character owner, Character target) {
        return Arrays.asList(new StabilizerIdle());
    }

}
