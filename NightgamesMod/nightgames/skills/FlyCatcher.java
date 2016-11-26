package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.skills.damage.DamageType;

public class FlyCatcher extends Skill {
    public FlyCatcher(Character self) {
        super("Fly Catcher", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Ki) >= 9 || user.get(Attribute.Cunning) >= 18;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !c.getPetsFor(target).isEmpty() && getSelf().canAct() && c.getStance().mobile(getSelf())
                        && !c.getStance().prone(getSelf());
    }

    @Override
    public int getMojoCost(Combat c) {
        return 15;
    }

    @Override
    public String describe(Combat c) {
        return "Focus on eliminating the enemy pet: 25% Stamina";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Optional<PetCharacter> targetPet = Global.pickRandom(c.getPetsFor(target));
        if (targetPet.isPresent()) {
            writeOutput(c, Result.normal, targetPet.get());
            double m = Global.random(30, 50);
            targetPet.get().pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, targetPet.get(), m));
            getSelf().weaken(c, getSelf().getStamina().max() / 4);
        return true;
        } else {
            writeOutput(c, Result.normal, target);
            return false;   
        }
    }

    @Override
    public Skill copy(Character user) {
        return new FlyCatcher(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.summoning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return receive(c, damage, modifier, target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return Global.format("{self:SUBJECT-ACTION:try|tries} to chase down {other:name-possessive} pet, but there are none!", getSelf(), target);
        }
        return Global.format("{self:SUBJECT-ACTION:take|takes} the time to focus on chasing down {other:name-do}, "
                        + "finally catching {other:direct-object} in a submission hold.", getSelf(), target);
    }

}
