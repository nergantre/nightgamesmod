package nightgames.pet.arms;

import java.util.Arrays;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.TentaclePart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public abstract class TentacleArm extends Arm {
    private String descriptionVariant;
    TentacleArm(ArmManager manager, ArmType type) {
        super(manager, type);
        descriptionVariant = Global.pickRandom(Arrays.asList("%s")).get();
    }

    @Override
    public String describe() {
        return String.format(descriptionVariant, getType().getDesc());
    }

    @Override
    int attackOdds(Combat c, Character owner, Character target) {
        return (int) Math.min(40, owner.get(Attribute.Slime) * .67);
    }
    
    public static TentaclePart PART = new TentaclePart("tentacle", "back", "slime", 0.0, 1.0, 0.0);

    public TentaclePart getPart() {
        return PART;
    }
}