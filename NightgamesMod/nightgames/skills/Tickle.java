package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;
import nightgames.status.Hypersensitive;
import nightgames.status.Winded;

public class Tickle extends Skill {

    public Tickle(Character self) {
        super("Tickle", self);
        addTag(SkillTag.weaken);
        addTag(SkillTag.staminaDamage);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && (c.getStance().mobile(getSelf()) || c.getStance().dom(getSelf()))
                        && (c.getStance().reachTop(getSelf()) || c.getStance().reachBottom(getSelf()));
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 7;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        DamageType type = DamageType.technique;
        if (getSelf().has(Trait.ticklemonster) || target.roll(getSelf(), c, accuracy(c))) {
            if (target.crotchAvailable() && c.getStance().reachBottom(getSelf()) && !c.getStance().havingSex(c)) {
                int bonus = 0;
                int weak = 0;
                Result result = Result.normal;

                if (getSelf().has(Item.Tickler2) && Global.random(2) == 1 && getSelf().canSpend(10)) {
                    getSelf().spendMojo(c, 10);
                    result = Result.special;
                    bonus += 2;
                    weak += 2;
                }
                if (hastickler()) {
                    result = Result.strong;
                    bonus += 5 + Global.random(4);
                    weak += 3 + Global.random(4);
                    type = DamageType.gadgets;
                }
                writeOutput(c, result, target);
                if (getSelf().has(Trait.ticklemonster) && target.mostlyNude()) {
                    writeOutput(c, Result.special, target);
                    bonus += 5 + Global.random(4);
                    weak += 3 + Global.random(4);
                    if (Global.random(4) == 0) {
                        target.add(c, new Winded(target, 1));
                    }
                }
                if (result == Result.special) {
                    target.add(c, new Hypersensitive(target));
                }
                if (target.has(Trait.ticklish)) {
                    bonus = 4 + Global.random(3);
                    c.write(target, Global.format(
                                    "{other:SUBJECT-ACTION:squirm|squirms} uncontrollably from {self:name-possessive} actions. Yup, definitely ticklish.",
                                    getSelf(), target));
                }
                target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("skin"),
                                (int) getSelf().modifyDamage(type, target, 2 + Global.random(4)), bonus, c, false, this);
                target.weaken(c, (int) getSelf().modifyDamage(type, target, weak + Global.random(10, 15)));
            } else if (hastickler() && Global.random(2) == 1) {
                type = DamageType.gadgets;
                int bonus = 0;
                if (target.breastsAvailable() && c.getStance().reachTop(getSelf())) {
                    writeOutput(c, Result.item, target);
                    if (target.has(Trait.ticklish)) {
                        bonus = 4 + Global.random(3);
                        c.write(target, Global.format(
                                        "{other:SUBJECT-ACTION:squirm|squirms} uncontrollably from {self:name-possessive} actions. Yup definitely ticklish.",
                                        getSelf(), target));
                    }
                    target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("skin"),
                                    4 + Global.random(4), bonus, c, false, this);
                } else {
                    writeOutput(c, Result.weak, target);
                    if (target.has(Trait.ticklish)) {
                        bonus = 4 + Global.random(3);
                        c.write(target, Global.format(
                                        "{other:SUBJECT-ACTION:squirm|squirms} uncontrollably from {self:name-possessive} actions. Yup definitely ticklish.",
                                        getSelf(), target));
                    }
                    target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("skin"),
                                    4 + Global.random(2), bonus, c, false, this);
                }
                target.weaken(c, (int) getSelf().modifyDamage(type, target, bonus + Global.random(5, 10)));
            } else {
                writeOutput(c, Result.normal, target);
                int bonus = 0;
                if (target.has(Trait.ticklish)) {
                    bonus = 2 + Global.random(3);
                    c.write(target, Global.format(
                                    "{other:SUBJECT-ACTION:squirm|squirms} uncontrollably from {self:name-possessive} actions. Yup definitely ticklish.",
                                    getSelf(), target));
                }
                int m = (int) Math.round((2 + Global.random(3)) * (.25 + target.getExposure()));
                int weak = (int) Math.round(bonus / 2 * (.25 + target.getExposure()));
                target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("skin"), (int) getSelf().modifyDamage(type, target, m),
                                bonus, c, false, this);
                target.weaken(c, (int) getSelf().modifyDamage(type, target, weak + Global.random(4, 7)));
            }
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 5;
    }

    @Override
    public Skill copy(Character user) {
        return new Tickle(user);
    }

    @Override
    public int speed() {
        return 7;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to tickle " + target.name() + ", but she squirms away.";
        } else if (modifier == Result.special) {
            return "You work your fingers across " + target.name()
                            + "'s most ticklish and most erogenous zones until she is writhing in pleasure and can't even make coherent words.";
        } else if (modifier == Result.critical) {
            return "You brush your tickler over " + target.name()
                            + "'s body, causing her to shiver and retreat. When you tickle her again, she yelps and almost falls down. "
                            + "It seems like your special feathers made her more sensitive than usual.";
        } else if (modifier == Result.strong) {
            return "You run your tickler across " + target.name()
                            + "'s sensitive thighs and pussy. She can't help but let out a quiet whimper of pleasure.";
        } else if (modifier == Result.item) {
            return "You tease " + target.name()
                            + "'s naked upper body with your feather tickler, paying close attention to her nipples.";
        } else if (modifier == Result.weak) {
            return "You catch " + target.name() + " off guard by tickling her neck and ears.";
        } else {
            return "You tickle " + target.name() + "'s sides as she giggles and squirms.";
        }

    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to tickle %s, but fails to find a sensitive spot.",
                            getSelf().subject(), target.nameDirectObject());
        } else if (modifier == Result.special) {
            return String.format("%s tickles %s nude body mercilessly, gradually working %s way to %s dick and balls. "
                            + "As %s fingers start tormenting %s privates, %s %s to "
                            + "clear %s head enough to keep from cumming immediately.", getSelf().subject(),
                            target.nameOrPossessivePronoun(), getSelf().possessivePronoun(),
                            target.possessivePronoun(), getSelf().possessivePronoun(), target.possessivePronoun(),
                            target.pronoun(), target.action("struggle"), target.possessivePronoun());
        } else if (modifier == Result.critical) {
            return String.format("%s teases %s privates with %s feather tickler. After %s stops,"
                            + " %s an unnatural sensitivity where the feathers touched %s.", getSelf().subject(),
                            target.nameDirectObject(), getSelf().possessivePronoun(), getSelf().pronoun(),
                            target.subjectAction("feel"), target.directObject());
        } else if (modifier == Result.strong) {
            return String.format("%s brushes %s tickler over %s balls and teases the sensitive head of %s penis.",
                            getSelf().subject(), getSelf().possessivePronoun(),
                            target.nameOrPossessivePronoun(), target.possessivePronoun());
        } else if (modifier == Result.item) {
            return String.format("%s runs %s feather tickler across %s nipples and abs.",
                            getSelf().subject(), getSelf().possessivePronoun(), 
                            target.nameOrPossessivePronoun());
        } else if (modifier == Result.weak) {
            return String.format("%s pulls out a feather tickler and teases any exposed skin %s can reach.",
                            getSelf().subject(), getSelf().pronoun());
        } else {
            return String.format("%s suddenly springs toward %s and tickles %s"
                            + " relentlessly until %s can barely breathe.", getSelf().subject(),
                            target.nameDirectObject(), target.directObject(), target.pronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Tickles opponent, weakening and arousing her. More effective if she's nude";
    }

    private boolean hastickler() {
        return getSelf().has(Item.Tickler) || getSelf().has(Item.Tickler2);
    }

    @Override
    public boolean makesContact() {
        return true;
    }
    
    @Override
    public Stage getStage() {
        return Stage.FOREPLAY;
    }
}
