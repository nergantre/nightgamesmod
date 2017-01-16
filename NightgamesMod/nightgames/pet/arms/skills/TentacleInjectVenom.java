package nightgames.pet.arms.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.Arm;
import nightgames.status.Abuff;
import nightgames.status.Atrophy;

public class TentacleInjectVenom extends TentacleArmSkill {
    public TentacleInjectVenom() {
        super("Tentacle Injection: Venom", 20);
    }

    public String getSourceString(Character self) {
        return Global.format("{self:NAME-POSSESSIVE} tentacle venom", self, self);
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
            c.write(owner, Global.format("{self:NAME-POSSESSIVE} injector tentacle shoots forward and embeds itself in {other:name-possessive} arm. "
                            + "{other:pronoun-action:yelp} and {other:action:pull} it out straight away. Unfortunately, "
                            + "{other:pronoun} already {other:action:start} to feel sluggish as {other:pronoun-action:realize} "
                            + "{other:pronoun-action:have} been poisoned.", owner, target));
            target.add(c, new Atrophy(target, owner.getLevel() / 3, 10, getSourceString(owner)));
            target.add(c, new Abuff(target, Attribute.Power, target.getPure(Attribute.Power) / 3, 10));
            target.add(c, new Abuff(target, Attribute.Speed, target.getPure(Attribute.Speed) / 3, 10));
            return true;
        } else {
            c.write(PetCharacter.DUMMY, Global.format("A %s flies towards {other:name-do}, "
                            + "but {other:pronoun-action:dodge} out of the way just in time.", owner, target, arm.getName()));
            return false;
        }
    }
}
