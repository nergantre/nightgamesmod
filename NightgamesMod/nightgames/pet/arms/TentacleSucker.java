package nightgames.pet.arms;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.body.TentaclePart;
import nightgames.characters.body.mods.PartMod;
import nightgames.combat.Combat;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.TentacleSuck;

public class TentacleSucker extends TentacleArm {
    private TentaclePart part;

    public TentacleSucker(ArmManager manager, Optional<? extends PartMod> mod) {
        super(manager, ArmType.TENTACLE_SUCKER);
        part = new TentaclePart("tentacle sucker", "back", "slime", 0.0, 1.0, 0.0);
        if (mod.isPresent()) {
            part = (TentaclePart) part.applyMod(mod.get());
        }
    }

    @Override
    List<ArmSkill> getSkills(Combat c, Character owner, Character target) {
        return Collections.singletonList(new TentacleSuck());
    }

    @Override
    public TentaclePart getPart() {
        return part;
    }
}
