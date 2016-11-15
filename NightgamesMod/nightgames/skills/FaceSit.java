package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.FaceSitting;
import nightgames.status.BodyFetish;
import nightgames.status.Enthralled;
import nightgames.status.Shamed;

public class FaceSit extends Skill {

    public FaceSit(Character self) {
        super("Facesit", self);
        addTag(SkillTag.pleasureSelf);
        addTag(SkillTag.dominant);
        addTag(SkillTag.facesit);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.getLevel() >= 10 || user.get(Attribute.Seduction) >= 30;
    }

    @Override
    public float priorityMod(Combat c) {
        return getSelf().has(Trait.lacedjuices) || getSelf().has(Trait.addictivefluids)
                        || (getSelf().body.has("pussy") && getSelf().body.
                                        getRandomPussy().moddedPartCountsAs(getSelf(), PussyPart.feral)) ? 3.0f : 0;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().crotchAvailable() && getSelf().canAct() && c.getStance().dom(getSelf())
                        && c.getStance().reachTop(getSelf()) && !c.getStance().penetrated(c, getSelf())
                        && !c.getStance().inserted(getSelf()) && c.getStance().prone(target)
                        && !getSelf().has(Trait.shy);
    }

    @Override
    public String describe(Combat c) {
        return "Shove your crotch into your opponent's face to demonstrate your superiority";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().has(Trait.entrallingjuices) && Global.random(4) == 0 && !target.wary()) {
            writeOutput(c, Result.special, target);
            target.add(c, new Enthralled(target, getSelf(), 5));
        } else {
            writeOutput(c, getSelf().has(Trait.lacedjuices) ? Result.strong : Result.normal, target);
        }
        
        int m = 10;
        if (target.has(Trait.silvertongue)) {
            m = m * 3 / 2;
        }
        if (getSelf().hasBalls()) {
            getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandom("balls"), m, c, this);
        } else {
            getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandom("pussy"), m, c, this);
        }
        double n = 4 + Global.random(4);
        if (c.getStance().front(getSelf())) {
            // opponent can see self
            n += 3 * getSelf().body.getHotness(target);
        }
        if (target.has(Trait.imagination)) {
            n *= 1.5;
        }

        target.tempt(c, getSelf(), getSelf().body.getRandom("ass"), (int) Math.round(n / 2));
        target.tempt(c, getSelf(), getSelf().body.getRandom("pussy"), (int) Math.round(n / 2));

        target.loseWillpower(c, 5);
        target.add(c, new Shamed(target));
        if (!c.getStance().isFaceSitting(getSelf())) {
            c.setStance(new FaceSitting(getSelf(), target));
        }
        if (Global.random(100) < 5 + 2 * getSelf().get(Attribute.Fetish)) {
            target.add(c, new BodyFetish(target, getSelf(), "ass", .25));
        }
        return true;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 25;
    }

    @Override
    public Skill copy(Character user) {
        return new FaceSit(user);
    }

    @Override
    public Tactics type(Combat c) {
        if (c.getStance().isFaceSitting(getSelf())) {
            return Tactics.positioning;
        } else {
            return Tactics.pleasure;
        }
    }

    @Override
    public String getLabel(Combat c) {
        if (getSelf().hasBalls() && !getSelf().hasPussy()) {
            return "Teabag";
        } else if (c.getStance().isFaceSitting(getSelf())) {
            return "Facesit";
        } else {
            return "Ride Face";
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (getSelf().hasBalls()) {
            if (modifier == Result.special) {
                return "You crouch over " + target.nameOrPossessivePronoun()
                                + " face and dunk your balls into her mouth. She can do little except lick them submissively, which does feel "
                                + "pretty good. She's so affected by your manliness that her eyes glaze over and she falls under your control. Oh yeah. You're awesome.";
            } else if (modifier == Result.strong) {
                return "You crouch over " + target.nameOrPossessivePronoun()
                                + " face and dunk your balls into her mouth. She can do little except lick them submissively, which does feel "
                                + "pretty good. Your powerful musk is clearly starting to turn her on. Oh yeah. You're awesome.";
            } else {
                return "You crouch over " + target.nameOrPossessivePronoun()
                                + " face and dunk your balls into her mouth. She can do little except lick them submissively, which does feel "
                                + "pretty good. Oh yeah. You're awesome.";
            }
        } else {
            if (modifier == Result.special) {
                return "You straddle " + target.nameOrPossessivePronoun()
                                + " face and grind your pussy against her mouth, forcing her to eat you out. Your juices take control of her lust and "
                                + "turn her into a pussy licking slave. Ooh, that feels good. You better be careful not to get carried away with this.";
            } else if (modifier == Result.strong) {
                return "You straddle " + target.nameOrPossessivePronoun()
                                + " face and grind your pussy against her mouth, forcing her to eat you out. She flushes and seeks more of your tainted juices. "
                                + "Ooh, that feels good. You better be careful not to get carried away with this.";
            } else {
                return "You straddle " + target.nameOrPossessivePronoun()
                                + " face and grind your pussy against her mouth, forcing her to eat you out. Ooh, that feels good. You better be careful "
                                + "not to get carried away with this.";
            }
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (getSelf().hasBalls()) {
            if (modifier == Result.special) {
                return String.format("%s straddles %s head and dominates %s by putting %s balls in %s mouth. "
                                + "For some reason, %s mind seems to cloud over and %s %s "
                                + "desperate to please %s. %s gives a superior smile as %s obediently %s on %s nuts.",
                                getSelf().subject(), target.nameOrPossessivePronoun(), target.directObject(),
                                getSelf().possessivePronoun(), target.possessivePronoun(),
                                target.nameOrPossessivePronoun(), target.pronoun(),
                                target.action("are", "is"), getSelf().directObject(),
                                Global.capitalizeFirstLetter(getSelf().subject()),
                                target.subject(), target.action("suck"), getSelf().possessivePronoun());
            } else if (modifier == Result.strong) {
                return String.format("%s straddles %s head and dominates %s by putting %s balls in %s mouth. "
                                + "Despite the humiliation, %s scent is turning %s on incredibly. "
                                + "%s gives a superior smile as %s obediently %s on %s nuts.",
                                getSelf().subject(), target.nameOrPossessivePronoun(), target.directObject(),
                                getSelf().possessivePronoun(), target.possessivePronoun(),
                                getSelf().nameOrPossessivePronoun(), target.subject(),
                                getSelf().subject(), target.subject(), target.action("suck"),
                                getSelf().possessivePronoun());
            } else {
                return String.format("%s straddles %s head and dominates %s by putting %s balls in %s mouth. "
                                + "%s gives a superior smile as %s obediently %s on %s nuts.",
                                getSelf().subject(), target.nameOrPossessivePronoun(), target.directObject(),
                                getSelf().possessivePronoun(),
                                target.possessivePronoun(),
                                getSelf().subject(), target.subject(), target.action("suck"),
                                getSelf().possessivePronoun());
            }
        } else {
            if (modifier == Result.special) {
                return String.format("%s straddles %s face and presses %s pussy against %s mouth. %s "
                                + "%s mouth and %s to lick %s freely offered muff, but %s just smiles "
                                + "while continuing to queen %s. As %s %s %s juices, %s %s"
                                + " eyes start to bore into %s mind. %s can't resist %s. %s %s even want to.",
                                getSelf().subject(), target.nameOrPossessivePronoun(), getSelf().possessivePronoun(),
                                target.possessivePronoun(), target.subjectAction("open"), target.possessivePronoun(),
                                target.action("start"), getSelf().possessivePronoun(), getSelf().pronoun(),
                                target.directObject(), target.pronoun(),
                                target.action("drink"), getSelf().possessivePronoun(),
                                target.subjectAction("feel"), getSelf().nameOrPossessivePronoun(), 
                                target.possessivePronoun(),
                                Global.capitalizeFirstLetter(target.pronoun()), 
                                Global.capitalizeFirstLetter(target.pronoun()), 
                                getSelf().nameDirectObject(), target.action("don't", "doesn't"));
            } else if (modifier == Result.strong) {
                return String.format("%s straddles %s face and presses %s pussy against %s mouth. %s "
                                + "%s mouth and start to lick %s freely offered muff, but %s just smiles "
                                + "while continuing to queen %s. %s %s body start to heat up as %s "
                                + "juices flow into %s mouth, %s %s giving %s a mouthful of aphrodisiac straight from "
                                + "the source!", getSelf().subject(), target.nameOrPossessivePronoun(),
                                getSelf().possessivePronoun(), target.possessivePronoun(), target.subjectAction("open"),
                                target.possessivePronoun(), getSelf().nameDirectObject(), getSelf().pronoun(),
                                 target.directObject(), Global.capitalizeFirstLetter(target.subjectAction("feel")),
                                 target.possessivePronoun(), getSelf().nameOrPossessivePronoun(), target.possessivePronoun(),
                                 getSelf().pronoun(), getSelf().action("are", "is"), target.directObject());
            } else {
                return String.format("%s straddles %s face and presses %s pussy against %s mouth. %s "
                                + "%s mouth and start to lick %s freely offered muff, but %s just smiles "
                                + "while continuing to queen %s. %s clearly doesn't mind accepting some pleasure"
                                + " to demonstrate %s superiority.",getSelf().subject(), target.nameOrPossessivePronoun(),
                                getSelf().possessivePronoun(), target.possessivePronoun(), target.subjectAction("open"),
                                target.possessivePronoun(), getSelf().nameDirectObject(), getSelf().pronoun(),
                                 target.directObject(), Global.capitalizeFirstLetter(getSelf().pronoun()), getSelf().possessivePronoun());
            }
        }
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
