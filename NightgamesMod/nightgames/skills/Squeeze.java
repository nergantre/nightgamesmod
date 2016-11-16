package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;

public class Squeeze extends Skill {

    public Squeeze(Character self) {
        super("Squeeze Balls", self);
        addTag(SkillTag.mean);
        addTag(SkillTag.hurt);
        addTag(SkillTag.positioning);
        addTag(SkillTag.staminaDamage);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.hasBalls() && c.getStance().reachBottom(getSelf()) && getSelf().canAct()
                        && !getSelf().has(Trait.shy);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            double m = Global.random(10, 20);
            DamageType type = DamageType.physical;
            if (target.has(Trait.brassballs)) {
                if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.weak2, target));
                } else if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.weak2, target));
                }
                m = 0;
            } else if (target.crotchAvailable()) {
                if (getSelf().has(Item.ShockGlove) && getSelf().has(Item.Battery, 2)) {
                    getSelf().consume(Item.Battery, 2);
                    if (target.human()) {
                        c.write(getSelf(), receive(c, 0, Result.special, target));
                    } else if (getSelf().human()) {
                        c.write(getSelf(), deal(c, 0, Result.special, target));
                    }
                    type = DamageType.gadgets;
                    m += 15;
                    if (target.has(Trait.achilles)) {
                        m += 5;
                    }
                } else if (target.has(ClothingTrait.armored)) {
                    if (target.human()) {
                        c.write(getSelf(), receive(c, 0, Result.item, target));
                    } else if (getSelf().human()) {
                        c.write(getSelf(), deal(c, 0, Result.item, target));
                    }
                    m *= .5;
                } else {
                    if (target.human()) {
                        c.write(getSelf(), receive(c, 0, Result.normal, target));
                    } else if (getSelf().human()) {
                        c.write(getSelf(), deal(c, 0, Result.normal, target));
                    }
                    if (target.has(Trait.achilles)) {
                        m += 5;
                    }
                }
            } else {
                if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.weak, target));
                } else if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.weak, target));
                }
                m *= target.getExposure(ClothingSlot.bottom);
            }
            target.pain(c, getSelf(), (int) getSelf().modifyDamage(type, target, m));

            target.emote(Emotion.angry, 15);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 9 && user.get(Attribute.Seduction) >= 9;
    }

    @Override
    public Skill copy(Character user) {
        return new Squeeze(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to grab " + target.name() + "'s balls, but she avoids it.";
        } else if (modifier == Result.special) {
            return "You use your shock glove to deliver a painful jolt directly into " + target.name()
                            + "'s testicles.";
        } else if (modifier == Result.weak) {
            return "You grab the bulge in " + target.name() + "'s "
                            + target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName() + " and squeeze.";
        } else if (modifier == Result.weak2) {
            return "You grab " + target.name() + "by the balls and squeeze hard, but" + target.pronoun()
                            + " does not flinch at all.";
        } else if (modifier == Result.item) {
            return "You grab the bulge in " + target.name() + "'s "
                            + target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()
                            + ", but find it solidly protected.";
        } else {
            return "You manage to grab " + target.name()
                            + "'s balls and squeeze them hard. You feel a twinge of empathy when she cries out in pain, but you maintain your grip.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s grabs at %s balls, but misses.",
                            getSelf().subject(), target.nameOrPossessivePronoun());
        } else if (modifier == Result.special) {
            return String.format("%s grabs %s naked balls roughly in %s gloved hand. A painful jolt "
                            + "of electricity shoots through %s groin, sapping %s will to fight.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            getSelf().possessivePronoun(), target.possessivePronoun(),
                            target.possessivePronoun());
        } else if (modifier == Result.weak) {
            return String.format("%s grabs %s balls through %s %s and squeezes hard.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.possessivePronoun(), 
                            target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName());
        } else if (modifier == Result.weak2) {
            return String.format("%s grins menacingly and firmly grabs %s nuts. %s squeezes as hard as "
                            + "%s can, but %s hardly %s it.", getSelf().subject(),
                            target.nameOrPossessivePronoun(),
                            Global.capitalizeFirstLetter(getSelf().subject()),
                            getSelf().pronoun(), target.pronoun(), target.action("feel"));
        } else if (modifier == Result.item) {
            return String.format("%s grabs %s crotch through %s %s, but %s can barely feel it.",
                            getSelf().subject(), target.nameOrPossessivePronoun(), target.possessivePronoun(),
                            target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName(),
                            target.pronoun());
        } else {
            return String.format("%s reaches between %s legs and grabs %s exposed balls. %s "
                            + "in pain as %s pulls and squeezes them.", getSelf().subject(),
                            target.nameOrPossessivePronoun(), target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.subjectAction("writhe")),
                            getSelf().subject());
        }
    }

    @Override
    public String getLabel(Combat c) {
        if (getSelf().has(Item.ShockGlove)) {
            return "Shock Balls";
        } else {
            return getName(c);
        }
    }

    @Override
    public String describe(Combat c) {
        return "Grab opponent's groin; deals more damage if she's naked";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
