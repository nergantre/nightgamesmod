package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.CombatantData;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.RoboArm;
import nightgames.status.Bound;
import nightgames.status.Stsflag;

public class Grab extends ArmSkill {

    public static final String FLAG = "AttachedGrabberCount";
    
    public Grab() {
        super("Grab", 10);
    }

    @Override
    public boolean usable(Combat c, RoboArm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target) && !target.is(Stsflag.bound);
    }
    
    @Override
    public boolean resolve(Combat c, RoboArm arm, Character owner, Character target) {
        boolean sub = c.getStance().dom(owner);
        boolean success = sub || Global.random(100) < 10 + owner.get(Attribute.Science);
        CombatantData data = c.getCombatantData(target);
        
        if (success) {
            if (data.getIntegerFlag(FLAG) == 0) {
                c.write(PetCharacter.DUMMY, Global.format("A %s, not content with hovering behind"
                                + " {self:name-possessive} back, lunges forward and seizes"
                                + " {other:subject} by a wrist. It doesn't impede {other:possessive}"
                                + " movements very much, but if another arm were to join it, that"
                                + " could be trouble.", owner, target, arm.getName()));
                data.setIntegerFlag(FLAG, 1);
            } else {
                c.write(PetCharacter.DUMMY, Global.format("Another %s shoots out from behind {self:subject}"
                                + " and catches {other:name-possessive} second wrist. The two"
                                + " robotic arms lock together behind {other:possessive} back,"
                                + " immobilizing {other:possessive} arms.", owner, target, arm.getName()));
                data.setIntegerFlag(FLAG, 0);
                target.add(new Bound(target, 75, owner.nameOrPossessivePronoun() + " " + arm.getName()));
            }
            return true;
        } else {
            c.write(PetCharacter.DUMMY, Global.format("A %s flies towards one of {other:name-possessive}"
                            + " wrists, but {other:pronoun-action:pull|pulls} {other:possessive}"
                            + " hand away just in time.", owner, target, arm.getName()));
        }
        
        return false;
    }

}
