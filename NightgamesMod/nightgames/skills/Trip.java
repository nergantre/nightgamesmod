package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.Falling;

public class Trip extends Skill {
    public Trip(Character self) {
        super("Trip", self);
        addTag(SkillTag.positioning);
        addTag(SkillTag.knockdown);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && c.getStance().mobile(getSelf()) && !c.getStance().prone(target)
                        && c.getStance().front(getSelf()) && getSelf().canAct();
    }
    
    private boolean isSlime() {
        return getSelf().get(Attribute.Slime) > 11;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c)) && getSelf().check(Attribute.Cunning, target.knockdownDC())) {
            if (isSlime()) {
                writeOutput(c, Result.special, target);
            } else {
                writeOutput(c, Result.normal, target);
            }
            target.add(c, new Falling(target));
        } else {
            if (isSlime()) {
                writeOutput(c, Result.weak, target);
            } else {
                writeOutput(c, Result.miss, target);
            }
            return false;
        }
        return true;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 16;
    }

    @Override
    public Skill copy(Character user) {
        return new Trip(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public int accuracy(Combat c) {
        return Math.round(Math.max(
                        Math.min(150, 2.5f
                                        * (getSelf().get(Attribute.Cunning)
                                                        - c.getOpponent(getSelf()).get(Attribute.Cunning))
                                        + (isSlime() ? 100 : 75)),
                        isSlime() ? 70 : 40));
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to trip " + target.name() + ", but she keeps her balance.";
        } else if (modifier == Result.special) {
            return String.format(
                            "You reshape your hands into a sheet of slime and slide it under %s's feet."
                                            + " When you quickly pull it back, %s falls flat on %s back.",
                            target.name(), target.pronoun(), target.possessivePronoun());
        } else if (modifier == Result.weak) {
            return String.format(
                            "You reshape your hands into a sheet of slime and slide it towards %s."
                                            + " In the nick of time, %s jumps clear, landing safely back on %s feet.",
                            target.name(), target.pronoun(), target.possessivePronoun());
        } else {
            return "You catch " + target.name() + " off balance and trip " + target.directObject() + ".";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s hooks %s ankle, but %s %s without falling.", getSelf().subject(),
                            target.nameOrPossessivePronoun(), target.pronoun(), target.action("recover"));
        } else if (modifier == Result.special) {
            return String.format(
                            "%s shoves a mass of %s slime under %s feet, destabilizing %s. With a few"
                                            + " pulls, %s throws %s onto %s back.",
                            getSelf().name(), getSelf().possessivePronoun(),
                            target.nameOrPossessivePronoun(), target.directObject(),
                            getSelf().pronoun(), target.directObject(), target.possessivePronoun());
        } else if (modifier == Result.weak) {
            return String.format(
                            "%s forms some of %s slime into a sheet and slides it towards %s feet."
                                            + " %s %s away from it, and %s harmlessly retracts the slime.",
                            getSelf().name(), getSelf().possessivePronoun(), target.nameOrPossessivePronoun(), 
                            Global.capitalizeFirstLetter(target.pronoun()), target.action("jump"),
                            getSelf().pronoun());
        } else {
            return String.format("%s takes %s feet out from under %s and sends %s sprawling to the floor.",
                            getSelf().subject(), target.nameOrPossessivePronoun(), target.directObject(),
                            target.directObject());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Attempt to trip your opponent";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
