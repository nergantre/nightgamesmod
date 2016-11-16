package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.NPC;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.pet.CharacterPet;
import nightgames.pet.Pet;

public class Simulacrum extends Skill {
    public Simulacrum(Character self) {
        super("Simulacrum", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().get(Attribute.Divinity) >= 12;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && c.getPetsFor(getSelf()).size() < getSelf().getPetLimit() && !target.isPet();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 30;
    }

    @Override
    public String describe(Combat c) {
        return "Summons a copy of your opponent to assist you.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Pet pet;
        int power = Math.max(10, getSelf().getLevel() - 2);
        int ac = 4 + power / 3;
        if (getSelf().has(Trait.leadership)) {
            power += 5;
        }
        if (getSelf().has(Trait.tactician)) {
            ac += 3;
        }

        String cloneName = String.format("%s clone", target.nameOrPossessivePronoun());
        if (target instanceof Player) {
            pet = new CharacterPet(cloneName, getSelf(), (Player)target, power, ac);
        } else if (target instanceof NPC) {
            pet = new CharacterPet(cloneName, getSelf(), (NPC)target, power, ac);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        pet.getSelf().body.autoTG();
        CharacterSex finalSex = pet.getSelf().body.guessCharacterSex();
        writeOutput(c, Result.valueOf(finalSex.name()), target);
        c.addPet(pet.getSelf());

        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Simulacrum(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.summoning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return Global.format("Reaching into your divine spark, you command {other:name-possessive} very soul to serve you. "
                            + "{other:PRONOUN} looks momentarily confused as nothing happened.", getSelf(), target);
        }
        return Global.format("Reaching into your divine spark, you command {other:name-possessive} very soul to serve you. "
                        + "{other:PRONOUN} looks confused for a second before suddenly noticing a translucent figure shifting into existence between you and {other:direct-object}. "
                        + "The projection stabilizes into a split image of {other:name-do}!", getSelf(), target);
    }
    
    private String getSubText(Result modifier) {
        switch(modifier) {
            case asexual:
                return "As the figure stands up, you see that she looks extremely familiar. It's a face that you've seen in the mirror every day. "
                                + "The clone looks like your identical twin, complete with the missing genitalia! She gives her newly formed nipples a few experimental tweaks before turning to face you. ";
            case shemale:
            case herm:
                return "As the figure stands up, you see that she looks extremely familiar. It's a face that you've seen in the mirror every day. "
                                + "The simularities end there however; you see that the rest of the clone looks like an idealized female version of yourself with bountiful breasts and a shapely rear. "
                                + "What surprises you the most though is that she managed to keep your stiff dick. She gives her newly formed cock a few experimental pumps before "
                                + "turning to facing you. ";
            case female:
                return "As the figure stands up, you see that she looks extremely familiar. It's a face that you've seen in the mirror every day. "
                                + "The simularities end there however; you see that the rest of the clone looks like an idealized female version of yourself with bountiful breasts and a shapely rear. "
                                + "She smiles at you and licks her lips while cupping her newly formed tits. ";
            case male:
                return "As the figure stands up, you see that he looks extremely familiar. It's a face that you've seen in the mirror every day. "
                                + "The simularities end there however; you see that the rest of the clone looks like an idealized male version of yourself, with chiseled abs and a stiff cock raring to go. "
                                + "He gives his newly formed cock a few experimental pumps before turning to facing you.";
            default:
                return "";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            Global.format("{self:SUBJECT} closes {self:possessive} eyes momentarily before slowly rising into the air. "
                            + "{other:SUBJECT-ACTION:are|is} not sure what {self:pronoun} is up to, but it's definitely not good for {other:direct-object}. "
                            + "Fortunately, {other:subject:were|was} close enough to leap at {self:direct-object} and interrupt whatever {self:pronoun} was trying to do.", 
                            getSelf(), target);
        }
        return Global.format("{self:SUBJECT} closes {self:possessive} eyes momentarily before slowly rising into the air. "
                        + "{other:SUBJECT-ACTION:are|is} not sure what {self:pronoun} is up to, but it's definitely not good for {other:direct-object}. "
                        + "{other:SUBJECT-ACTION:run|runs} towards {other:direct-object} in a mad dash to try interrupting whatever it is {self:pronoun} is doing. "
                        + "However it is too late, {self:subject} opens her now-glowing golden eyes and intonates <i>\"{other:NAME}... SERVE ME.\"</i> "
                        + "The command pierces through {other:direct-object} giving {other:direct-object} a strange sense of vertigo. {other:SUBJECT-ACTION:almost collapse|almost collapses} "
                        + "but when {other:pronoun-action:raise|raises} {other:possessive} head, {other:subject-action:see|sees} a figure kneeling before {self:name-do}. "
                        + "<br/><br/>"
                        + getSubText(modifier)
                        + "You're now fighting your own clone!", getSelf(), target);
    }
}
