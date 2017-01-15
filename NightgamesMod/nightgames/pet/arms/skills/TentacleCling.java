package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;
import nightgames.status.Stsflag;
import nightgames.status.TentacleBound;

public class TentacleCling extends ArmSkill {
    public TentacleCling() {
        super("Cling", 20);
    }

    @Override
    public boolean usable(Combat c, Arm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target) && !target.is(Stsflag.tentacleBound);
    }

    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        boolean sub = c.getStance().dom(owner);
        int chance = Math.min(50, 15 + owner.get(Attribute.Slime));
        if (sub) {
            chance += 30;
        }
        boolean success = Global.random(100) < chance;

        if (success) {
            c.write(PetCharacter.DUMMY, Global.format("A %s shoots out from behind {self:name-do}"
                            + " and wraps itself around {other:name-possessive} waist, restricting {other:possessive} movement.", owner, target, arm.getName()));
            target.add(c, new TentacleBound(target, 75 + owner.get(Attribute.Slime), owner.nameOrPossessivePronoun() + " " + arm.getName(), 1));
        } else {
            c.write(PetCharacter.DUMMY, Global.format("A %s shoots out from behind {self:name-do}"
                            + " and attempts to wrap itself around {other:name-possessive}. "
                            + "However, {other:pronoun-action:manage} to twist away just in time.", owner, target, arm.getName()));
        }
        
        return false;
    }

}
