package nightgames.pet.arms.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;
import nightgames.stance.Engulfed;
import nightgames.status.Stsflag;
import nightgames.status.TentacleBound;

public class TentacleReel extends TentacleArmSkill {
    public TentacleReel() {
        super("Reel", 30);
    }

    @Override
    public boolean usable(Combat c, Arm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target) && target.is(Stsflag.tentacleBound);
    }

    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        TentacleBound bound = (TentacleBound) target.getStatus(Stsflag.tentacleBound);
        if (bound == null) {
            return false;
        }

        if (bound.getStacks() > 2) {
            c.write(PetCharacter.DUMMY, Global.format("The %s wrapped around {other:name-possessive} waist manages to pull {other:direct-object} right up against {self:name-do}. "
                            + "It does not stop there though; the tentacle somehow manages to pull {other:direct-object} into {self:possessive} very body, engulfing {other:direct-object} inside.", owner, target, arm.getName()));
            target.free();
            c.setStance(new Engulfed(owner, target), owner, true);
        } else {
            c.write(PetCharacter.DUMMY, Global.format("The %s wrapped around {other:name-possessive} waist starts pulling {other:direct-object} back towards {self:name-do}.", owner, target, arm.getName()));
            bound.setStacks(bound.getStacks() + 1);
        }

        return true;
    }

}
