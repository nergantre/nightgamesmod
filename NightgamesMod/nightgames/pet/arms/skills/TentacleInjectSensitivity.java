package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;
import nightgames.status.Hypersensitive;
import nightgames.status.Stsflag;

public class TentacleInjectSensitivity extends TentacleArmSkill {
    public TentacleInjectSensitivity() {
        super("Tentacle Injection: Sensitizer", 20);
    }

    @Override
    public boolean usable(Combat c, Arm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target) && c.getStance().distance() < 2 && !target.is(Stsflag.hypersensitive);
    }

    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        boolean sub = target.bound() || !c.getStance().mobile(target);
        boolean success = sub || Global.random(100) < 10 + owner.get(Attribute.Slime);

        if (success) {
            c.write(PetCharacter.DUMMY, Global.format("With a sudden whipping motion, {self:NAME-POSSESSIVE} needle-tipped tentacle flies forward and stabs itself into {other:name-possessive} skin. "
                            + "Without giving you a chance to react, the slimey appendage injects a mystery fluid into your body. "
                            + "The effects are unfortunately quite clear to {other:direct-object} as {other:pronoun-action:feel} {other:possessive} body's sensitivity get cranked up to eleven.", owner, target));
            target.add(c, new Hypersensitive(target, 10));
            return true;
        } else {
            c.write(PetCharacter.DUMMY, Global.format("A %s flies towards {other:name-do}, "
                            + "but {other:pronoun-action:dodge} out of the way just in time.", owner, target, arm.getName()));
            return false;
        }
    }
}
