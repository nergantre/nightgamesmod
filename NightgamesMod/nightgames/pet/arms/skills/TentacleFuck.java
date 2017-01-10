package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;

public class TentacleFuck extends ArmSkill {    
    public TentacleFuck() {
        super("Thrust", 20);
    }

    @Override
    public boolean usable(Combat c, Arm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target) && target.hasPussy() && !c.getStance().vaginallyPenetrated(c, target);
    }
    
    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        boolean sub = target.bound() || !c.getStance().mobile(target);
        boolean success = sub || Global.random(100) < 10 + owner.get(Attribute.Science);

        if (success) {
                c.write(PetCharacter.DUMMY, Global.format("%s shoots forward, snaking through your guard and impaling itself inside your defenseless pussy. ", owner, target, arm.getName()));
            return true;
        } else {
            c.write(PetCharacter.DUMMY, Global.format("A %s flies towards one of {other:name-possessive}"
                            + " wrists, but {other:pronoun-action:pull|pulls} {other:possessive}"
                            + " hand away just in time.", owner, target, arm.getName()));
        }
        
        return false;
    }

}
