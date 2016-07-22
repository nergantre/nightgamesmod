package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Jumped;
import nightgames.status.Falling;

public class ReverseCarry extends Carry {
    public ReverseCarry(Character self) {
        super("Jump", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 20 && user.hasPussy();
    }

    @Override
    public BodyPart getSelfOrgan() {
        return getSelf().body.getRandomPussy();
    }

    @Override
    public BodyPart getTargetOrgan(Character target) {
        return target.body.getRandomCock();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        String premessage = premessage(c, target);

        if (target.roll(this, c, accuracy(c))) {
            if (getSelf().human()) {
                c.write(getSelf(), premessage + deal(c, premessage.length(), Result.normal, target));
            } else if (target.human()) {
                c.write(getSelf(), premessage + receive(c, premessage.length(), Result.normal, getSelf()));
            }
            int m = 5 + Global.random(5);
            int otherm = m;
            if (getSelf().has(Trait.insertion)) {
                otherm += Math.min(getSelf().get(Attribute.Seduction) / 4, 40);
            }
            target.body.pleasure(getSelf(), getSelfOrgan(), getTargetOrgan(target), m, c, this);
            getSelf().body.pleasure(target, getTargetOrgan(target), getSelfOrgan(), otherm, c, this);
            c.setStance(new Jumped(getSelf(), target), getSelf(), getSelf().canMakeOwnDecision());
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), premessage + deal(c, premessage.length(), Result.miss, target));
            } else if (target.human()) {
                c.write(getSelf(), premessage + receive(c, premessage.length(), Result.miss, target));
            }
            getSelf().add(c, new Falling(getSelf()));
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ReverseCarry(user);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return (damage > 0 ? "" : "You ") + "leap into " + target.possessivePronoun()
                            + " arms, but she deposits you back onto the floor.";
        } else {
            return Global.format(
                            (damage > 0 ? "" : "You ")
                                            + " leap into {other:possessive} arms, impaling yourself onto her {other:body-part:cock} "
                                            + ". She lets out a noise that's equal parts surprise and delight as you bounce on her pole.",
                            getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return (damage > 0 ? "" : target.subject() + " ")
                            + "jumps onto you, but you deposit her back onto the floor.";
        } else {
            return (damage > 0 ? "" : target.subject() + " ")
                            + "leaps into your arms and impales herself on your cock. She wraps her legs around your torso and you quickly support her so she doesn't "
                            + "fall and injure herself or you.";
        }
    }

    @Override
    public String describe(Combat c) {
        return "Jump into your opponent's arms and impale yourself on her cock: Mojo 10.";
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
