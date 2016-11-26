package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.ClothingTrait;
import nightgames.status.Falling;
import nightgames.status.Winded;

public class StunBlast extends Skill {

    public StunBlast(Character self) {
        super("Stun Blast", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Science) >= 9;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && c.getStance().front(getSelf())
                        && (getSelf().has(Item.Battery, 4) || 
                                        target.has(ClothingTrait.harpoonDildo) || 
                                        target.has(ClothingTrait.harpoonOnahole));
    }

    @Override
    public String describe(Combat c) {
        return "A blast of light and sound with a chance to stun: 4 Battery";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.has(ClothingTrait.harpoonDildo) || 
                                        target.has(ClothingTrait.harpoonOnahole)) { 
            writeOutput(c, Result.special, target);
            target.getStamina().empty();
            target.add(c, new Winded(target));
            target.add(c, new Falling(target));
        }
        getSelf().consume(Item.Battery, 4);
        if (Global.random(10) >= 4) {
            writeOutput(c, Result.normal, target);
            target.getStamina().empty();
            target.add(c, new Falling(target));
            target.add(c, new Winded(target));
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new StunBlast(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You overload the emitter on your arm, but " + target.name()
                            + " shields her face to avoid the flash.";
        } else {
            return "You overload the emitter on your arm, duplicating the effect of a flashbang. " + target.name()
                            + " staggers as the blast disorients her.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s covers %s face and points a device in %s direction. Sensing "
                            + "danger, %s %s %s eyes just as the flashbang goes off.", getSelf().subject(),
                            getSelf().possessivePronoun(), target.nameOrPossessivePronoun(),
                            target.pronoun(), target.action("cover"), target.possessivePronoun());
        } else if (modifier == Result.special) {
            return Global.format("{self:SUBJECT} presses a button on {self:possessive} arm device,"
                            + "and a bright flash suddenly travels along {self:possessive} connection to"
                            + " the toy which is still stuck to you. When it reaches you, a huge shock"
                            + " stuns your body, leaving you helpless on the ground while the toy"
                            + " still merrily churns away.."
                            , getSelf(), target);
        } else {
            return String.format("%s points a device in %s direction that glows slightly. A sudden "
                            + "flash of light disorients %s and %s ears ring from the blast.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.directObject(), target.possessivePronoun());
        }
    }

}
