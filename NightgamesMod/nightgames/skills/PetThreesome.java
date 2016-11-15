package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.stance.FFMCowgirlThreesome;
import nightgames.stance.FFMFacesittingThreesome;
import nightgames.stance.MFFMissionaryThreesome;
import nightgames.stance.MFMDoublePenThreesome;
import nightgames.stance.MFMSpitroastThreesome;

public class PetThreesome extends Skill {
    public PetThreesome(String name, Character self, int cooldown) {
        super(name, self, cooldown);
    }

    public PetThreesome(Character self) {
        super("Threesome", self);
    }

    public BodyPart getSelfOrgan(Character fucker, Combat c) {
        BodyPart res = fucker.body.getRandomPussy();
        return res;
    }

    public BodyPart getTargetOrgan(Character target) {
        return target.body.getRandomCock();
    }

    public boolean fuckable(Combat c, Character target) {
        Optional<PetCharacter> fuckerOptional = Global.pickRandom(c.getPetsFor(getSelf()));
        if (!fuckerOptional.isPresent()) {
            return false;
        }
        Character fucker = fuckerOptional.get();

        BodyPart selfO = getSelfOrgan(fucker, c);
        BodyPart targetO = getTargetOrgan(target);
        // You can't really have a threesome with a fairy... or can you?
        boolean possible = fucker.body.getHeight() > 70 && selfO != null && targetO != null;
        boolean ready = possible;
        boolean stancePossible = !c.getStance().havingSex(c);
        return possible && ready && stancePossible && canGetToCrotch(target);
    }

    private boolean canGetToCrotch(Character target) {
        if (target.crotchAvailable())
            return true;
        return false;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return fuckable(c, target) && c.getStance().mobile(getSelf()) && !c.getStance().mobile(target) && getSelf().canAct() && !c.getPetsFor(getSelf()).isEmpty();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = 5 + Global.random(5);
        Optional<? extends Character> fuckerOptional = Global.pickRandom(c.getPetsFor(getSelf()));
        if (!fuckerOptional.isPresent()) {
            return false;
        }
        Character fucker = fuckerOptional.get();

        BodyPart selfO = getSelfOrgan(fucker, c);
        BodyPart targetO = getTargetOrgan(target);
        if (targetO.isReady(target)) {
            Result result = Global.random(3) == 0 ? Result.critical : Result.normal;
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, result, target));
            } else if (c.shouldPrintReceive(target, c)) {
                c.write(getSelf(), receive(c, 0, result, target));
            }
            if (selfO.isType("pussy")) {
                if (result == Result.critical && getSelf().useFemalePronouns()) {
                    c.setStance(new FFMFacesittingThreesome(fucker, getSelf(), target));
                } else {                    
                    c.setStance(new FFMCowgirlThreesome(fucker, getSelf(), target));
                }
            } else if (selfO.isType("cock") && getSelf().useFemalePronouns()) {
                c.setStance(new MFFMissionaryThreesome(fucker, getSelf(), target));
            } else if (selfO.isType("cock")) {
                if (result == Result.critical) {
                    c.setStance(new MFMDoublePenThreesome(fucker, getSelf(), target));
                } else {
                    c.setStance(new MFMSpitroastThreesome(fucker, getSelf(), target));
                }
            }
            int otherm = m;
            if (fucker.has(Trait.insertion)) {
                otherm += Math.min(fucker.get(Attribute.Seduction) / 4, 40);
            }
            target.body.pleasure(fucker, selfO, targetO, m, c, this);
            fucker.body.pleasure(target, targetO, selfO, otherm, c, this);
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.miss, target));
            } else if (c.shouldPrintReceive(target, c)) {
                c.write(getSelf(), receive(c, 0, Result.miss, target));
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new PetThreesome(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You bowl your opponent over and pin her down while your pet fucks her <<PLACEHOLDER>>";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().name + " pins you down while her pet fucks you <<PLACEHOLDER>>";
    }

    @Override
    public String describe(Combat c) {
        return "Holds your opponent down and have your pet fuck her.";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
