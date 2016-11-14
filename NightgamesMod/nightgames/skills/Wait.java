package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.skills.damage.DamageType;

public class Wait extends Skill {

    public Wait(Character self) {
        super("Wait", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond();
    }

    @Override
    public int getMojoBuilt(Combat c) {
        if (channel(c)) {
            return 20 + getSelf().get(Attribute.Arcane) / 3;
        } else if (focused(c)) {
            return 20;
        } else {
            return 15;
        }
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (channel(c)) {
            writeOutput(c, Result.special, target);
            if (focused(c)) {
                getSelf().heal(c, (int) getSelf().modifyDamage(DamageType.physical, Global.noneCharacter(), Global.random(8, 16)));
                getSelf().calm(c, Global.random(8, 14));
            } else {
                getSelf().heal(c, (int) getSelf().modifyDamage(DamageType.physical, Global.noneCharacter(), Global.random(4, 8)));
            }
        } else if (focused(c)) {
            writeOutput(c, Result.strong, target);
            getSelf().heal(c, (int) getSelf().modifyDamage(DamageType.physical, Global.noneCharacter(), Global.random(8, 16)));
            getSelf().calm(c, Global.random(8, 14));
        } else {
            writeOutput(c, Result.normal, target);
            getSelf().heal(c, (int) getSelf().modifyDamage(DamageType.physical, Global.noneCharacter(), Global.random(4, 8)));
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Wait(user);
    }

    @Override
    public int speed() {
        return 0;
    }

    @Override
    public Tactics type(Combat c) {
        // focus takes priority here
        if (focused(c)) {
            return Tactics.calming;
        } else if (channel(c)) {
            return Tactics.recovery;
        } else {
            return Tactics.misc;
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return "You revitalize yourself by channeling some of the natural energies around you.";
        } else if (modifier == Result.strong) {
            return "You take a moment to clear your thoughts, focusing your mind and calming your body.";
        } else {
            return "You bide your time, waiting to see what " + target.name() + " will do.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return Global.format("{self:SUBJECT} closes {self:possessive} eyes and takes a deep breath. "
                            + "You see a warm glow briefly surround {self:direct-object} before disappearing. "
                            + "When {self:pronoun} opens {self:possessive} eyes, {self:pronoun} looks reinvigorated.",
                            getSelf(), target);
        } else if (modifier == Result.strong) {
            return String.format("%s closes %s eyes and takes a deep breath. When %s opens %s eyes, "
                            + "%s seems more composed.", getSelf().subject(), getSelf().possessivePronoun(),
                            getSelf().pronoun(), getSelf().possessivePronoun(), getSelf().pronoun());
        } else {
            return String.format("%s hesitates, watching %s closely.",
                            getSelf().subject(), target.nameDirectObject());
        }
    }

    @Override
    public String getLabel(Combat c) {
        if (channel(c)) {
            return "Channel";
        } else if (focused(c)) {
            return "Focus";
        } else {
            return getName(c);
        }
    }

    @Override
    public String describe(Combat c) {
        if (channel(c)) {
            return "Focus and channel the natual energies around you";
        } else if (focused(c)) {
            return "Calm yourself and gain some mojo";
        } else {
            return "Do nothing";
        }
    }

    private boolean focused(Combat c) {
        return getSelf().get(Attribute.Cunning) >= 15 && !getSelf().has(Trait.undisciplined) && getSelf().canRespond() && !c.getStance().sub(getSelf());
    }

    private boolean channel(Combat c) {
        return getSelf().get(Attribute.Arcane) >= 1 && getSelf().canRespond() && !c.getStance().sub(getSelf());
    }
}
