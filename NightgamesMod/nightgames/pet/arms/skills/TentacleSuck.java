package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;
import nightgames.pet.arms.TentacleSucker;
import nightgames.status.PartSucked;

public class TentacleSuck extends TentacleArmSkill {    
    public TentacleSuck() {
        super("Tentacle Suck", 20);
    }

    @Override
    public boolean usable(Combat c, Arm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target) && target.hasDick() && c.getStance().distance() < 2 && !c.getStance().penisInserted(target);
    }

    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        boolean sub = target.bound() || !c.getStance().mobile(target);
        boolean success = sub || Global.random(100) < 10 + owner.get(Attribute.Slime);
        double strength = Global.random(10, 21);
        BodyPart tentaclePart = TentacleSucker.PART;

        if (success) {
            c.write(PetCharacter.DUMMY, Global.format("{self:NAME-POSSESSIVE} %s shoots forward, snaking through {other:possessive} guard "
                            + "and attaching itself to {self:possessive} defenseless cock. "
                            + "{self:SUBJECT:try} pulling it out with {self:possessive} hands but the vacuum-tight suction make it feel like {self:pronoun-action:are} giving {other:reflective} a tug-job.", owner, target, arm.getName()));
            target.body.pleasure(owner, tentaclePart, target.body.getRandomPussy(), strength, c);
            target.add(c, new PartSucked(target, owner, tentaclePart, "cock"));
            return true;
        } else {
            c.write(PetCharacter.DUMMY, Global.format("A %s flies towards {other:name-possessive} crotch, "
                            + "but {other:pronoun-action:dodge} out of the way just in time.", owner, target, arm.getName()));
        }
        return false;
    }
}
