package nightgames.pet.arms.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.RoboArm;

public class HealRay extends ArmSkill {

    public HealRay() {
        super("Heal Ray", 20);
    }

    @Override
    public boolean resolve(Combat c, RoboArm arm, Character owner, Character target) {
        boolean stamina = Global.random(2) == 0;
        boolean mojo = Global.random(2) == 0;

        String msg = "The %s at the end of {self:name-possessive} %s" + " starts glowing with a lime green light, ";

        if (stamina && mojo) {
            msg += "and {self:pronoun} perks up. {self:PRONOUN-ACTION:seem|seems} less tired and much more"
                            + " confident than before.";
            owner.heal(c, 10 + Global.random(20), "Heal Ray");
            owner.buildMojo(c, 5 + Global.random(15), "Heal Ray");
        } else if (stamina) {
            msg += "and it seems to eliminate some of {self:possessive} weariness.";
            owner.heal(c, 10 + Global.random(20), "Heal Ray");
        } else if (mojo) {
            msg += "and something changes in {self:direct-object}. {self:POSSESSIVE} movements"
                            + " seem more confident, and {self:pronoun-action:watch|watches} "
                            + "{other:name-do} with a hint of a smile.";
            owner.buildMojo(c, 5 + Global.random(15), "Heal Ray");
        } else {
            msg += "but it soon sputters and dies out. {self:PRONOUN-ACTION:are not|does not seem} pleased.";
        }

        c.write(PetCharacter.DUMMY, Global.format(msg, owner, target, arm.getType()
                                                                         .getDesc(),
                        arm.getName()));

        return true;
    }

}
