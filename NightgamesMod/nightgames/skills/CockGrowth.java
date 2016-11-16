package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.CockPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Hypersensitive;

public class CockGrowth extends Skill {
    public CockGrowth(Character self) {
        super("Cock Growth", self);
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
        return .5f;
    }

    @Override
    public String describe(Combat c) {
        return "Grows or enlarges your opponent's cock.";
    }

    @Override
    public int getMojoCost(Combat c) {
        return 25;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Result res = target.roll(getSelf(), c, accuracy(c)) ? Result.normal : Result.miss;
        if (res == Result.normal && !target.hasDick()) {
            res = Result.special;
        }

        boolean permanent = Global.random(20) == 0 && (getSelf().human() || c.shouldPrintReceive(target, c))
                        && !target.has(Trait.stableform);

        if (res != Result.miss) {
            target.add(c, new Hypersensitive(target));
            CockPart part = target.body.getCockBelow(BasicCockPart.massive.size);
            if (permanent) {
                if (part != null) {
                    target.body.addReplace(part.upgrade(), 1);
                } else {
                    target.body.addReplace(BasicCockPart.small, 1);
                }
            } else {
                if (part != null) {
                    target.body.temporaryAddOrReplacePartWithType(part.upgrade(), 10);
                } else {
                    target.body.temporaryAddPart(BasicCockPart.small, 10);
                }
            }
        }
        writeOutput(c, permanent ? 1 : 0, res, target);
        return res != Result.miss;
    }

    @Override
    public Skill copy(Character user) {
        return new CockGrowth(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        String message;
        if (modifier == Result.miss) {
            message = "You attempt to channel your arcane energies into " + target.name()
                            + "'s crotch, but she dodges out of the way, causing your spell to fail.";
        } else {
            if (modifier == Result.special) {
                message = "You channel your arcane energies into " + target.name() + "'s groin. A moment later, "
                                + target.name() + " yelps as her clitoris rapidly enlarges into a small girl-cock!";
            } else {
                message = "You channel your arcane energies into " + target.name() + "'s dick. A moment later, "
                                + target.name() + " yelps as her cock rapidly enlarges!";
            }
            if (damage > 0) {
                message += " You realize the effects are permanent!";
            }
        }
        return message;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String message;
        if (modifier == Result.miss) {
            message = String.format("%s moving and begins chanting. %s feeling some "
                            + "tingling in %s groin, but it quickly subsides as %s %s out of the way.", 
                            getSelf().subjectAction("stop"), Global.capitalizeFirstLetter(target.subjectAction("start")),
                            target.possessivePronoun(), target.pronoun(), target.action("dodge"));
        } else {
            if (modifier == Result.special) {
                message = String.format("%s moving and begins chanting. %s to feel %s clit grow hot, and start expanding! "
                                + "%s try to hold it back with your hands, but the growth continues until %s %s the proud owner of a new %s. "
                                + "The sensations from %s new maleness make %s tremble.",
                                getSelf().subjectAction("stop"), Global.capitalizeFirstLetter(target.subjectAction("start")),
                                target.possessivePronoun(),
                                Global.capitalizeFirstLetter(target.pronoun()), target.pronoun(), target.action("are", "is"), 
                                target.body.getRandomCock().describe(target),
                                target.possessivePronoun(), target.directObject());
            } else {
                message = String.format("%s moving and begins chanting. %s feel %s cock grow hot, and start expanding! "
                                + "%s try to hold it back with your hands, but the growth continues until it's much larger than before. "
                                + "The new sensations from %s new larger cock make %s tremble.",
                                getSelf().subjectAction("stop"), Global.capitalizeFirstLetter(target.subjectAction("start")),
                                target.possessivePronoun(),
                                Global.capitalizeFirstLetter(target.pronoun()),
                                target.possessivePronoun(), target.directObject());
            }
            if (damage > 0) {
                message += " You realize the effects are permanent!";
            }
        }
        return message;
    }

}
