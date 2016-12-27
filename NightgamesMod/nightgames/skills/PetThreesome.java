package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.stance.FFMCowgirlThreesome;
import nightgames.stance.FFMFacesittingThreesome;
import nightgames.stance.FFXTribThreesome;
import nightgames.stance.XHFDaisyChainThreesome;
import nightgames.stance.MFFMissionaryThreesome;
import nightgames.stance.MFMDoublePenThreesome;
import nightgames.stance.MFMSpitroastThreesome;
import nightgames.stance.ReverseXHFDaisyChainThreesome;

public class PetThreesome extends Skill {
    public PetThreesome(String name, Character self, int cooldown) {
        super(name, self, cooldown);
    }

    @Override
    public float priorityMod(Combat c) {
        return 6.0f;
    }

    public PetThreesome(Character self) {
        super("Threesome", self);
    }

    public BodyPart getSelfOrgan(Character fucker, Combat c) {
        BodyPart res = fucker.body.getRandomCock();
        return res;
    }

    public BodyPart getTargetOrgan(Character target) {
        return target.body.getRandomPussy();
    }

    public boolean fuckable(Combat c, Character target) {
        Character fucker = getFucker(c);
        if (fucker == null) {
            return false;
        }

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
        return fuckable(c, target) && c.getStance().mobile(getSelf()) && (!c.getStance().mobile(target) || c.getStance().prone(target)) && getSelf().canAct();
    }

    protected Character getFucker(Combat c) {
        return Global.pickRandom(c.getPetsFor(getSelf())).orElse(null);
    }

    protected Character getMaster(Combat c) {
        return getSelf();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = 5 + Global.random(5);
        int otherm = m;
        Character fucker = getFucker(c);
        Character master = getMaster(c);
        BodyPart selfO = getSelfOrgan(fucker, c);
        BodyPart targetO = getTargetOrgan(target);
        if (selfO == null || targetO == null) {
            c.write("Something really weird happened here, [ERROR]");
            return false;
        }
        for (int i = 0; i < 10; i++) {
            if (fucker.clothingFuckable(selfO) || fucker.strip(ClothingSlot.bottom, c) == null) {
                break;
            }
        }
        if (targetO.isReady(target)) {
            Result result = Global.random(3) == 0 ? Result.critical : Result.normal;
            if (selfO.isType("pussy") && targetO.isType("cock") && target.hasPussy() && master.hasDick()) {
                c.write(getSelf(), Global.format("While {self:subject} is holding {other:name-do} down, "
                                + "{master:subject-action:move|moves} behind {other:direct-object} and {master:action:pierce|pierces} "
                                + "{other:direct-object} with {master:possessive} cock. "
                                + "Taking advantage of {other:possessive} surprise "
                                + "{self:subject-action:slip|slips} {other:name-possessive} "
                                + "hard cock into {self:reflective}, ending up in a erotic daisy-chain.", fucker, 
                                target));
                c.setStance(new ReverseXHFDaisyChainThreesome(fucker, master, target), getSelf(), true);
                target.body.pleasure(master, master.body.getRandomCock(), target.body.getRandomPussy(), otherm, 0, c, false, this);
                master.body.pleasure(target, target.body.getRandomPussy(), master.body.getRandomCock(), m, 0, c, false, this);
            } else if (selfO.isType("pussy") && targetO.isType("pussy")) {
                c.write(getSelf(), Global.format("While {master:subject:are|is} holding {other:name-do} down, "
                                + "{self:subject-action:mount|mounts} {other:direct-object} and {self:action:press|presses} "
                                + "{self:possessive} own pussy against {other:possessive}s.", fucker, 
                                target));
                c.setStance(new FFXTribThreesome(fucker, master, target), getSelf(), true);
                target.body.pleasure(master, master.body.getRandomCock(), target.body.getRandomPussy(), otherm, 0, c, false, this);
                master.body.pleasure(target, target.body.getRandomPussy(), master.body.getRandomCock(), m, 0, c, false, this);
            } else if (selfO.isType("pussy")) {
                if (result == Result.critical && master.useFemalePronouns()) {
                    c.write(getSelf(), Global.format("While %s holding {other:name-do} down with %s ass, "
                                    + "{self:subject} mounts {other:direct-object} and pierces "
                                    + "{self:reflective} with {other:possessive} cock.", fucker, 
                                    target, master.subjectAction("are", "is"), master.possessivePronoun()));
                    c.setStance(new FFMFacesittingThreesome(fucker, master, target), getSelf(), true);
                } else {
                    c.write(getSelf(), Global.format("While %s holding {other:name-do} down, "
                                    + "{self:subject} mounts {other:direct-object} and pierces "
                                    + "{self:reflective} with {other:possessive} cock.", fucker, 
                                    target, master.subjectAction("are", "is")));
                    c.setStance(new FFMCowgirlThreesome(fucker, master, target), getSelf(), true);
                }
            } else if (selfO.isType("cock") && master.hasPussy() && target.hasDick()) {
                c.write(getSelf(), Global.format("While %s holding {other:name-do} down, "
                                + "{self:subject} moves behind {other:direct-object} and pierces "
                                + "{other:direct-object} with {self:possessive} cock. "
                                + "Taking advantage of {other:possessive} surprise %s {other:name-possessive} "
                                + "hard cock into %s, ending up in a erotic daisy-chain.", fucker, 
                                target, master.subjectAction("are", "is"), master.subjectAction("slip"),
                                master.reflectivePronoun()));
                c.setStance(new XHFDaisyChainThreesome(fucker, master, target), getSelf(), true);
                target.body.pleasure(master, master.body.getRandomPussy(), target.body.getRandomCock(), otherm, 0, c, false, this);
                master.body.pleasure(target, target.body.getRandomCock(), master.body.getRandomPussy(), m, 0, c, false, this);
            } else if (selfO.isType("cock") && !master.hasDick()) {
                c.write(getSelf(), Global.format("While %s holding {other:name-do} down, "
                                + "{self:subject} mounts {other:direct-object} and pierces "
                                + "{other:direct-object} with {self:possessive} cock in the missionary position.", fucker, 
                                target, master.subjectAction("are", "is")));
                c.setStance(new MFFMissionaryThreesome(fucker, master, target), getSelf(), true);
            } else if (selfO.isType("cock")) {
                if (result == Result.critical) {
                    c.write(getSelf(), Global.format("While %s holding {other:name-do} from behind, "
                                    + "{self:subject} mounts {other:direct-object} and pierces "
                                    + "{other:direct-object} with {self:possessive} cock in the missionary position. "
                                    + "It does not end there however, as %s {other:possessive} remaining hole, "
                                    + "leaving {other:direct-object} completely stuffed front and back.", fucker, 
                                    target, master.subjectAction("are", "is"), master.pronoun() + master.action(" grin and take", " grins and takes")));
                    c.setStance(new MFMDoublePenThreesome(fucker, master, target), getSelf(), true);
                    target.body.pleasure(master, master.body.getRandomCock(), target.body.getRandomAss(), otherm, 0, c, false, this);
                    master.body.pleasure(target, target.body.getRandomAss(), master.body.getRandomCock(), m, 0, c, false, this);
                } else {
                    c.write(getSelf(), Global.format("While %s holding {other:name-possessive} head, "
                                    + "{self:subject} gets behind {other:direct-object} and pierces "
                                    + "{other:direct-object} with {self:possessive} cock. "
                                    + "It does not end there however, as %s {other:direct-object} %s cock, "
                                    + "leaving the poor {other:girl} spit-roasted.", fucker, 
                                    target, master.subjectAction("are", "is"), master.pronoun() + master.action(" feed", " feeds"), master.possessivePronoun()));
                    c.setStance(new MFMSpitroastThreesome(fucker, master, target), getSelf(), true);
                }
            }
            if (fucker.has(Trait.insertion)) {
                otherm += Math.min(fucker.get(Attribute.Seduction) / 4, 40);
            }
            target.body.pleasure(fucker, selfO, targetO, otherm, c, this);
            fucker.body.pleasure(target, targetO, selfO, m, c, this);
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:try|tries} to pull {other:name-do} into a threesome but {other:pronoun-action:are|is} not aroused enough yet.", 
                            getSelf(), target));
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
        return "You bowl your opponent over and pin her down while your pet fucks her [PLACEHOLDER]";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().name + " pins you down while her pet fucks you [PLACEHOLDER]";
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
