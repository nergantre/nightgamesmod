package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Hypersensitive;

public class BreastGrowth extends Skill {
    public BreastGrowth(Character self) {
        super("Breast Growth", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Arcane) >= 12;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf());
    }

    @Override
    public float priorityMod(Combat c) {
        return 0;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 25;
    }

    @Override
    public String describe(Combat c) {
        return "Grow your opponent's boobs to make her more sensitive.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Result res = target.roll(this, c, 0) ? Result.normal : Result.miss;

        boolean permanent = Global.random(20) == 0 && (getSelf().human() || target.human())
                        && !target.has(Trait.stableform);
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, permanent ? 1 : 0, res, target));
        } else if (target.human()) {
            c.write(getSelf(), receive(c, permanent ? 1 : 0, res, target));
        }
        if (res != Result.miss) {
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
        }
        return res != Result.miss;
    }

    @Override
    public Skill copy(Character user) {
        return new BreastGrowth(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        String message;
        if (modifier != Result.miss) {
            message = "You channel your arcane energies into " + target.name()
                            + "'s breasts, causing them to grow rapidly. Her knees buckle with the new sensitivity you bestowed on her boobs.";
            if (damage > 0) {
                message += " You realize the effects are permanent!";
            }
        } else {
            message = "You attempt to channel your arcane energies into " + target.name()
                            + "'s breasts, but she dodges out of the way, causing your spell to fail.";
        }
        return message;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String message;
        if (modifier != Result.miss) {
            message = getSelf().name()
                            + " stops moving and begins chanting. You feel your breasts grow hot, and start expanding! "
                            + "You try to hold them back with your hands, but the growth continues until you're a full cup size bigger than before. "
                            + "The new sensations from your substantially larger breasts make you tremble.";
            if (damage > 0) {
                message += " You realize the effects are permanent!";
            }
        } else {
            message = getSelf().name()
                            + " stops moving and begins chanting. You start feeling some tingling in your breasts, but it quickly subsides as you dodge out of the way.";
        }
        return message;
    }

}
