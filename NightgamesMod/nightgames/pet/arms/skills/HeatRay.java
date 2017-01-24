package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;

public class HeatRay extends ArmSkill {

    public HeatRay() {
        super("Heat Ray", 30);
    }

    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        boolean sub = c.getStance().dom(owner);
        boolean success = sub || Global.random(100) < 10 + owner.get(Attribute.Science);
        
        if (success) {
            c.write(PetCharacter.DUMMY, Global.format("{self:NAME-POSSESSIVE} %s levels its"
                            + " opening at {other:name-do} menacingly. {other:PRONOUN-ACTION:don't|doesn't}"
                            + " see anything, but {other:pronoun} certainly {other:action:feel|feels} what"
                            + " the device is doing as areas all over {other:possessive} body grow uncomfortably,"
                            + " almost painfully hot.", owner, target, arm.getName()));
            target.pain(c, owner, 15 + Global.random((int) (owner.get(Attribute.Science) * 1.5)));
            return true;
        }

        return false;
    }

}
