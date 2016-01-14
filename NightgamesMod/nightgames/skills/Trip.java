package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Falling;

public class Trip extends Skill {
    public Trip(Character self) {
        super("Trip", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && c.getStance().mobile(getSelf()) && !c.getStance().prone(target)
                        && c.getStance().front(getSelf()) && getSelf().canAct();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(this, c, accuracy(c)) && getSelf().check(Attribute.Cunning, target.knockdownDC())) {
            if (getSelf().has(Trait.slime)) {
                if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.special, target));
                } else if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.special, target));
                }
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.normal, target));
                } else if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.normal, target));
                }
            }
            target.add(c, new Falling(target));
        } else {
            if (getSelf().has(Trait.slime)) {
                if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.weak, target));
                } else if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.weak, target));
                }
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.miss, target));
                } else if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.miss, target));
                }
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
                                                        - c.getOther(getSelf()).get(Attribute.Cunning))
                                        + (getSelf().has(Trait.slime) ? 100 : 75)),
                        getSelf().has(Trait.slime) ? 70 : 40));
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
            return getSelf().name() + " hooks your ankle, but you recover without falling.";
        } else if (modifier == Result.special) {
            return String.format(
                            "%s shoves a mass of %s slime under your feet, destabilizing you. With a few"
                                            + " pulls, %s throws you onto your back.",
                            getSelf().name(), getSelf().pronoun(), getSelf().pronoun());
        } else if (modifier == Result.weak) {
            return String.format(
                            "%s forms some of %s slime into a sheet and slides it towards your feet."
                                            + " You jump away from it, and %s harmlessly retracts the slime.",
                            getSelf().name(), getSelf().possessivePronoun(), getSelf().pronoun());
        } else {
            return getSelf().name() + " takes your feet out from under you and sends you sprawling to the floor.";
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
