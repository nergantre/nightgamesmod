package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;
import nightgames.stance.Kneeling;
import nightgames.status.Horny;

public class TentacleInjectAphrodisiac extends TentacleArmSkill {
    public TentacleInjectAphrodisiac() {
        super("Tentacle Injection: Aphrodisiac", 20);
    }

    public String getSourceString(Character self) {
        return Global.format("{self:NAME-POSSESSIVE} aphrodisiac poison", self, self);
    }

    @Override
    public boolean usable(Combat c, Arm arm, Character owner, Character target) {
        return super.usable(c, arm, owner, target) && c.getStance().distance() < 2 && !target.hasStatusVariant(getSourceString(owner));
    }

    @Override
    public boolean resolve(Combat c, Arm arm, Character owner, Character target) {
        boolean sub = target.bound() || !c.getStance().mobile(target);
        boolean success = sub || Global.random(100) < 10 + owner.get(Attribute.Slime);

        if (success) {
            c.write(owner, Global.format("{self:NAME-POSSESSIVE} tentacle with a needle-like tip makes a sudden motion and comes flying at {other:name-do}. "
                            + "With no time to dodge, {other:pronoun} can only yelp in pain at the sudden prick of a hypodermic needle penetrating {other:possessive} skin. "
                            + "{other:SUBJECT} quickly {other:action:pull} the tentacle off {other:possessive} arm, but by then it's too late: an unnatural heat rips through {other:possessive} body, "
                            + "causing {other:direct-object} to drop to {other:possessive} knees.", owner, target));
            target.add(c, new Horny(target, owner.getLevel() / 5, 10, getSourceString(owner)));
            if (!c.getStance().dom(owner)) {
                c.setStance(new Kneeling(owner, target));
            }
            return true;
        } else {
            c.write(PetCharacter.DUMMY, Global.format("A %s flies towards {other:name-do}, "
                            + "but {other:pronoun-action:dodge} out of the way just in time.", owner, target, arm.getName()));
            return false;
        }
    }
}
