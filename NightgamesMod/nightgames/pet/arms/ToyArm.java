package nightgames.pet.arms;

import java.util.Arrays;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.ToyAttack;

public class ToyArm extends RoboArm {
    ToyArm(ArmManager manager) {
        super(manager, ArmType.TOY_ARM);
    }

    @Override
    List<ArmSkill> getSkills(Combat c, Character owner, Character target) {
        return Arrays.asList(new ToyAttack());
    }
}
