package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Hypersensitive;

public class BreastRay extends Skill {
    public BreastRay(Character self) {
        super("Breast Ray", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Science) >= 12;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && target.mostlyNude() && getSelf().has(Item.Battery, 2);
    }

    @Override
    public float priorityMod(Combat c) {
        return 2.f;
    }

    @Override
    public String describe(Combat c) {
        return "Grow your opponent's boobs to make her more sensitive: 2 Batteries";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().consume(Item.Battery, 2);
        boolean permanent = Global.random(20) == 0 && (getSelf().human() || c.shouldPrintReceive(target))
                        && !target.has(Trait.stableform);
        writeOutput(c, permanent ? 1 : 0, Result.normal, target);
        target.add(c, new Hypersensitive(target));
        BreastsPart part = target.body.getBreastsBelow(BreastsPart.f.size);
        if (permanent) {
            if (part != null) {
                target.body.addReplace(part.upgrade(), 1);
            }
        } else {
            if (part != null) {
                target.body.temporaryAddOrReplacePartWithType(part.upgrade(), 10);
            }
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new BreastRay(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        String message;
        message = "You point your growth ray at " + target.name()
                        + "'s breasts and fire. Her breasts balloon up and the new sensitivity causes her to moan.";
        if (damage > 0) {
            message += " The change in " + target.name() + " looks permanent!";
        }
        return message;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String message;
        boolean plural = target.body.getRandomBreasts().size > 0 || target.get(Attribute.Power) > 25;
        message = String.format("%s a device at %s chest and giggles as %s %s"
                        + " %s ballooning up. %s %s and %s to cover %s, but the increased sensitivity "
                        + "distracts %s in a delicious way.",
                        getSelf().subjectAction("point"), target.nameOrPossessivePronoun(), target.possessivePronoun(),
                        target.body.getRandomBreasts().describe(target), plural ? "start" : "starts",
                                        Global.capitalizeFirstLetter(target.pronoun()), 
                                        target.action("flush", "flushes"),
                                        target.action("try", "tries"),
                                        target.reflectivePronoun(), target.directObject());;
        if (damage > 0) {
            message += " You realize the effects are permanent!";
        }
        return message;
    }

}
