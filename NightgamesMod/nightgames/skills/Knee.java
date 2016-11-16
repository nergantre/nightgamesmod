package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingTrait;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;

public class Knee extends Skill {

    public Knee(Character self) {
        super("Knee", self);
        addTag(SkillTag.mean);
        addTag(SkillTag.hurt);
        addTag(SkillTag.positioning);
        addTag(SkillTag.staminaDamage);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf()) && getSelf().canAct()
                        && c.getStance().front(target) && !c.getStance().connected(c);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 25;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            double m = Global.random(40, 60);
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            } else if (c.shouldPrintReceive(target, c)) {
                if (c.getStance().prone(target)) {
                    c.write(getSelf(), receive(c, 0, Result.special, target));
                } else {
                    c.write(getSelf(), receive(c, 0, Result.normal, target));
                }
                if (target.hasBalls() && Global.random(5) >= 3) {
                    c.write(getSelf(), getSelf().bbLiner(c, target));
                }
            }
            if (target.has(Trait.achilles) && !target.has(ClothingTrait.armored)) {
                m += Global.random(16,20);
            }
            if (target.has(ClothingTrait.armored) || target.has(Trait.brassballs)) {
                m *= .75;
            }
            target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, m));

            target.emote(Emotion.angry, 20);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 10;
    }

    @Override
    public Skill copy(Character user) {
        return new Knee(user);
    }

    @Override
    public int speed() {
        return 4;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return target.name() + " blocks your knee strike.";
        }
        return "You deliver a powerful knee strike to " + target.name()
                        + "'s delicate lady flower. She lets out a pained whimper and nurses her injured parts.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String victim = target.hasBalls() ? "balls" : "crotch";
        if (modifier == Result.miss) {
            return String.format("%s tries to knee %s in the %s, but %s %s it.",
                            getSelf().subject(), target.nameDirectObject(),
                            victim, target.pronoun(),
                                            target.action("block"));
        }
        if (modifier == Result.special) {
            return String.format("%s raises one leg into the air, then brings %s knee "
                            + "down like a hammer onto %s %s. %s"
                            + " out in pain and instinctively try "
                            + "to close %s legs, but %s holds them open.",
                            getSelf().subject(), getSelf().possessivePronoun(),
                            target.nameOrPossessivePronoun(), victim,
                            Global.capitalizeFirstLetter(target.subjectAction("cry", "cries")),
                            target.possessivePronoun(), getSelf().subject());
        } else {
            return String.format("%s steps in close and brings %s knee up between %s legs, "
                            + "crushing %s fragile balls. %s and nearly %s from the "
                            + "intense pain in %s abdomen.", getSelf().subject(),
                            getSelf().possessivePronoun(), target.nameOrPossessivePronoun(),
                            target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.subjectAction("groan")),
                            target.action("collapse"), target.possessivePronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Knee opponent in the groin";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
