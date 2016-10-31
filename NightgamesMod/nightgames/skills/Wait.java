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
        if (focused() && !c.getStance().sub(getSelf())) {
            return 25;
        } else {
            return 15;
        }
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (focused() && !c.getStance().sub(getSelf())) {
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
        if (focused()) {
            return Tactics.calming;
        } else {
            return Tactics.misc;
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return "You force yourself to look less tired and horny than you actually are. You even start to believe it yourself.";
        } else if (modifier == Result.strong) {
            return "You take a moment to clear your thoughts, focusing your mind and calming your body.";
        } else {
            return "You bide your time, waiting to see what " + target.name() + " will do.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return String.format("Despite %s best efforts, %s is still looking as calm and composed as ever. Either "
                            + "%s %s getting to %s at all, or %s's good at hiding it.", target.nameOrPossessivePronoun(),
                            getSelf().subject(), target.pronoun(), target.action("are", "is"),
                            getSelf().directObject(), getSelf().pronoun());
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
        if (focused()) {
            return "Focus";
        } else {
            return getName(c);
        }
    }

    @Override
    public String describe(Combat c) {
        if (focused()) {
            return "Calm yourself and gain some mojo";
        } else {
            return "Do nothing";
        }
    }

    private boolean focused() {
        return getSelf().get(Attribute.Cunning) >= 15 && !getSelf().has(Trait.undisciplined) && getSelf().canRespond();
    }
}
