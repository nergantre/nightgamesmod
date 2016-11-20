package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.skills.damage.DamageType;

public class Honeypot extends Skill {
    public Honeypot(Character self) {
        super("Honeypot", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Dark) >= 9 || user.get(Attribute.Seduction) >= 18;
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
        return "Focus on eliminating the enemy pet: 25% arousal";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Optional<PetCharacter> targetPet = Global.pickRandom(c.getPetsFor(target));
        if (targetPet.isPresent()) {
            writeOutput(c, Result.normal, targetPet.get());
            double m = Global.random(30, 50);
            targetPet.get().body.pleasure(getSelf(), getSelf().body.getRandom("hands"), 
                            targetPet.get().body.getRandomGenital(), 
                            (int) getSelf().modifyDamage(DamageType.technique, targetPet.get(), m), c);
            getSelf().arouse(getSelf().getArousal().max() / 4, c);
        return true;
        } else {
            writeOutput(c, Result.normal, target);
            return false;   
        }
    }

    @Override
    public Skill copy(Character user) {
        return new Honeypot(user);
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
            return Global.format("{self:SUBJECT-ACTION:try|tries} to entice {other:name-possessive} pet, but there are none!", getSelf(), target);
        }
        return Global.format("{self:SUBJECT-ACTION:take|takes} the time to entice {other:name-do}, "
                        + "rubbing {self:reflective} and putting on a show. "
                        + "{other:SUBECT} takes the bait and approaches {self:direct-object}. With a sudden motion, {self:pronoun-action:capture|captures} "
                        + "{other:direct-object} with {self:possessive} legs and {self:action:have|haves} {self:possessive} way with the poor follower.", getSelf(), target);
    }
}
