package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;
import nightgames.skills.ThrowSlime;
import nightgames.skills.ThrowSlime.HitType;
import nightgames.status.Slimed;

public class TentacleSquirt extends TentacleArmSkill {    
    public TentacleSquirt() {
        super("Tentacle Squirt", 10);
    }

    @Override
    public boolean usable(Combat c, Arm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target) && c.getStance().distance() > 1;
    }

    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        boolean sub = target.bound() || !c.getStance().mobile(target);
        boolean success = sub || Global.random(100) < 10 + owner.get(Attribute.Slime);
        ThrowSlime throwSlimeSkill = new ThrowSlime(owner);
        HitType type = throwSlimeSkill.decideEffect(c, target);

        if (success && type != HitType.NONE) {
            c.write(PetCharacter.DUMMY, Global.format("The %s rears up and fires a large gunk of slime at {other:name-do}", owner, target, arm.getName()));
            type.message(c, owner, target);
            target.add(c, type.build(owner, target));
            target.add(c, new Slimed(target, owner, Global.random(1, 5)));
            return true;
        } else {
            c.write(PetCharacter.DUMMY, Global.format("The %s rears up and fires a large gunk of slime at {other:name-do}. Luckily, it misses its mark.", owner, target, arm.getName()));
        }
        return false;
    }
}
