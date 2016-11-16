package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.skills.damage.DamageType;

public class Sedate extends Skill {

    public Sedate(Character self) {
        super("Sedate", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf()) && getSelf().canAct() && getSelf().has(Item.Sedative)
                        && !c.getStance().prone(getSelf());
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().consume(Item.Sedative, 1);
        if (getSelf().has(Item.Aersolizer)) {
            writeOutput(c, Result.special, target);
            target.weaken(c, (int) getSelf().modifyDamage(DamageType.gadgets, target,30));
            target.loseMojo(c, (int) getSelf().modifyDamage(DamageType.gadgets, target,25));
        } else if (target.roll(getSelf(), c, accuracy(c))) {
            writeOutput(c, Result.normal, target);
            target.weaken(c, (int) getSelf().modifyDamage(DamageType.gadgets, target,30));
            target.loseMojo(c, (int) getSelf().modifyDamage(DamageType.gadgets, target,25));
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Sedate(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return "You pop a sedative into your Aerosolizer and spray " + target.name()
                            + " with a cloud of mist. She stumbles out of the cloud looking drowsy and unfocused.";
        } else if (modifier == Result.miss) {
            return "You throw a bottle of sedative at " + target.name()
                            + ", but she ducks out of the way and it splashes harmlessly on the ground. What a waste.";
        } else {
            return "You through a bottle of sedative at " + target.name()
                            + ". She stumbles for a moment, trying to clear the drowsiness from her head.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return String.format("%s inserts a bottle into the attachment on %s arm. "
                            + "%s suddenly surrounded by a cloud of dense fog. The "
                            + "fog seems to fill %s head and %s body feels heavy.",
                            getSelf().subject(), getSelf().possessivePronoun(),
                            Global.capitalizeFirstLetter(target.action("are", "is")),
                            target.possessivePronoun(), target.possessivePronoun());
        } else if (modifier == Result.miss) {
            return String.format("%s splashes a bottle of liquid in %s direction, but none of it hits %s.",
                            getSelf().subject(), target.nameOrPossessivePronoun(), target.directObject());
        } else {
            return String.format("%s hits %s with a flask of liquid. Even the fumes make %s feel"
                            + " sluggish and %s limbs become heavy.",
                            getSelf().subject(), target.nameDirectObject(),
                            target.directObject(), target.possessivePronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Throw a sedative at your opponent, weakening " + c.getOpponent(getSelf()).directObject();
    }
}
