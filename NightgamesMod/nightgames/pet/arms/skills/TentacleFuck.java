package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;
import nightgames.pet.arms.TentacleArm;
import nightgames.status.PartFucked;

public class TentacleFuck extends TentacleArmSkill {    
    public TentacleFuck() {
        super("Tentacle Fuck", 20);
    }

    @Override
    public boolean usable(Combat c, Arm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target) && target.hasPussy() && c.getStance().distance() < 2 && !c.getStance().vaginallyPenetrated(c, target);
    }

    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        boolean sub = target.bound() || !c.getStance().mobile(target);
        boolean success = sub || Global.random(100) < 10 + owner.get(Attribute.Slime);
        double strength = Global.random(10, 21);
        
        BodyPart tentaclePart;
        if (arm instanceof TentacleArm) {
            tentaclePart = ((TentacleArm)arm).getPart();
        } else {
            tentaclePart = TentacleArm.PART;
        }

        if (success) {
            c.write(PetCharacter.DUMMY, Global.format("{self:NAME-POSSESSIVE} %s shoots forward, snaking through {other:possessive} guard "
                            + "and impaling itself inside {self:possessive} defenseless pussy. "
                            + "{self:SUBJECT:try} pulling it out with {self:possessive} hands but the slippery appendage easily eludes {other:possessive} grip. "
                            + "The entire business just ends ups arousing {other:direct-object} to no end.", owner, target, arm.getName()));
            target.body.pleasure(owner, tentaclePart, target.body.getRandomPussy(), strength, c);
            target.add(c, new PartFucked(target, owner, tentaclePart, "pussy"));
            return true;
        } else {
            c.write(PetCharacter.DUMMY, Global.format("A %s flies towards {other:name-possessive} crotch, "
                            + "but {other:pronoun-action:dodge} out of the way just in time.", owner, target, arm.getName()));
        }
        return false;
    }

}
