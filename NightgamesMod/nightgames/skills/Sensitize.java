package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Hypersensitive;
import nightgames.status.Stsflag;

public class Sensitize extends Skill {

    public Sensitize(Character self) {
        super("Sensitivity Potion", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf()) && getSelf().canAct() && getSelf().has(Item.SPotion)
                        && target.mostlyNude() && !c.getStance().prone(getSelf()) && !target.is(Stsflag.hypersensitive);
    }

    @Override
    public String describe(Combat c) {
        return "Makes your opponent hypersensitive";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().consume(Item.SPotion, 1);
        if (getSelf().has(Item.Aersolizer)) {
            writeOutput(c, Result.special, target);
            target.add(c, new Hypersensitive(target));
        } else if (target.roll(getSelf(), c, accuracy(c))) {
            writeOutput(c, Result.normal, target);
            target.add(c, new Hypersensitive(target));
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Sensitize(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return "You pop a sensitivity potion into your Aerosolizer and spray " + target.name()
                            + " with a cloud of mist. She shivers as it takes hold and heightens her "
                            + "sense of touch.";
        } else if (modifier == Result.miss) {
            return "You throw a bottle of sensitivity elixir at " + target.name()
                            + ", but she ducks out of the way and it splashes harmlessly on the ground. What a waste.";
        } else {
            return "You throw a sensitivity potion at " + target.name()
                            + ". You see her skin flush as it takes effect.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return String.format("%s inserts a bottle into the attachment on %s arm. %s "
                            + "suddenly surrounded by a cloud of minty gas. %s skin becomes"
                            + " hot, but goosebumps appear anyway. "
                            + "Even the air touching %s skin makes %s shiver.", getSelf().subject(),
                            getSelf().possessivePronoun(), 
                            Global.capitalizeFirstLetter(target.subjectAction("are", "is")),
                            target.possessivePronoun(), target.possessivePronoun(),
                            target.directObject());
        } else if (modifier == Result.miss) {
            return String.format("%s splashes a bottle of liquid in %s direction, but none of it hits %s.",
                            getSelf().subject(), target.nameDirectObject(), target.directObject());
        } else {
            return String.format("%s throws a bottle of strange liquid at %s. The skin it touches grows hot"
                            + " and oversensitive.", getSelf().subject(), target.nameDirectObject());
        }
    }

}
