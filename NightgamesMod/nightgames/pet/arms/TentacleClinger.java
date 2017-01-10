package nightgames.pet.arms;

import java.util.Arrays;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.Cling;
import nightgames.pet.arms.skills.Reel;

public class TentacleClinger extends TentacleArm {

    public TentacleClinger(ArmManager manager) {
        super(manager, ArmType.TENTACLE_CLINGER);
    }

    @Override
    List<ArmSkill> getSkills(Combat c, Character owner, Character target) {
        return Arrays.asList(new Cling(), new Reel());
    }

}
