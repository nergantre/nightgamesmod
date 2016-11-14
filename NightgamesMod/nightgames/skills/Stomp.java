package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;

public class Stomp extends Skill {

    public Stomp(Character self) {
        super("Stomp", self);
        addTag(SkillTag.usesFeet);
        addTag(SkillTag.physical);
        addTag(SkillTag.hurt);
        addTag(SkillTag.positioning);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.mean);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !c.getStance().prone(getSelf()) && c.getStance().prone(target) && c.getStance().feet(getSelf(), target)
                        && getSelf().canAct() && !c.getStance().inserted(target);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 20;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int pain = Global.random(1, 10);
        if (target.has(Trait.brassballs)) {
            if (getSelf().has(Trait.heeldrop) && target.crotchAvailable() && target.hasBalls()) {
                if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.strong, target));
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), receive(c, 0, Result.strong, target));
                    if (target.hasBalls() && Global.random(5) >= 1) {
                        c.write(getSelf(), getSelf().bbLiner(c, target));
                    }
                }
                pain = 15 - (int) Math
                                .round((5 + Global.random(5)) * target.getOutfit().getExposure(ClothingSlot.bottom));
            } else {
                writeOutput(c, Result.weak2, target);
            }
        } else if (getSelf().has(Trait.heeldrop) && target.crotchAvailable()) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.special, target));
            } else if (c.shouldPrintReceive(target, c)) {
                c.write(getSelf(), receive(c, 0, Result.special, target));
                if (target.hasBalls() && Global.random(5) >= 1) {
                    c.write(getSelf(), getSelf().bbLiner(c, target));
                }
            }
            if (target.has(Trait.achilles)) {
                pain += 20;
            }
            pain += 40 - (int) Math.round((5 + Global.random(5)) * target.getOutfit().getExposure(ClothingSlot.bottom));
        } else if (target.has(ClothingTrait.armored)) {
            writeOutput(c, Result.weak, target);
            pain += 15 - (int) Math.round((2 + Global.random(3)) * target.getOutfit().getExposure(ClothingSlot.bottom));
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            } else if (c.shouldPrintReceive(target, c)) {
                c.write(getSelf(), receive(c, 0, Result.normal, target));
                if (target.hasBalls() && Global.random(5) >= 1) {
                    c.write(getSelf(), getSelf().bbLiner(c, target));
                }
            }
            pain += 20;
            pain += 20 - (int) Math
                            .round((10 + Global.random(10)) * target.getOutfit().getExposure(ClothingSlot.bottom));
        }
        target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, pain));
        target.emote(Emotion.angry, 25);
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 16;
    }

    @Override
    public Skill copy(Character user) {
        return new Stomp(user);
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
    public String getLabel(Combat c) {
        if (getSelf().has(Trait.heeldrop)) {
            return "Double Legdrop";
        } else {
            return getName(c);
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            if (target.hasBalls()) {
                return "You push " + target.name()
                                + "'s legs apart, exposing her private parts. Her girl-cock is hanging over her testicles, so you prod it with your foot "
                                + "to push it out of the way. She becomes slightly aroused at your touch and your attention, not realizing you're planning something extremely "
                                + "painful. You're going to feel a bit bad about this, but probably not nearly as much as she will. You jump up, lifting both legs and coming "
                                + "down in a double legdrop directly hitting her unprotected jewels.";
            } else {
                return "You push " + target.name()
                                + "'s legs apart, fully exposing her womanhood. She flushes in shame, apparently misunderstanding you intentions, which "
                                + "compels you to mutter a quick apology before you jump up and slam your heel into her vulnerable quim.";
            }
        } else if (modifier == Result.strong) {
            return String.format(
                            "You use your foot to maneuver %s's %s out of the way and revealing %s soft balls. "
                                            + "%s does not seem to realize what you are planning before your foot is already plunging down towards them."
                                            + " When it lands, you feel a sympathetic jolt run up your spine as %s gonads are crushed beneath your foot."
                                            + " %s whimpers in pain, but not as much as you'd expect from such a magnificent impact.",
                            target.name(), target.body.getRandomCock().describe(target), target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.pronoun()), target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.pronoun()));
        } else if (modifier == Result.weak) {
            return "You step between " + target.name()
                            + "'s legs and stomp down on her groin. Your foot hits something solid and she doesn't seem terribly affected.";
        } else if (modifier == Result.weak2) {
            return String.format(
                            "You step between %s's legs and stomp down on %s groin."
                                            + "%s exhales sharply, but does not seem hurt much at all. Somehow.",
                            target.name(), target.possessivePronoun(), Global.capitalizeFirstLetter(target.pronoun()));
        } else {
            if (target.hasBalls()) {
                return "You pull " + target.name()
                                + "'s legs open and stomp on her vulnerable balls. She cries out in pain and curls up in the fetal position.";
            } else {
                return "You step between " + target.name()
                                + "'s legs and stomp down on her sensitive pussy. She cries out in pain and rubs her injured girl bits.";
            }
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return String.format("%s forces %s legs open and begins prodding %s genitals with %s foot. "
                            + "%s slightly aroused by %s attention, but %s is not giving "
                            + "%s a proper footjob, %s is mostly just playing with %s balls. Too late, "
                            + "%s that %s is actually lining up %s targets. Two torrents of pain "
                            + "erupt from %s delicates as %s feet crash down on them.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.possessivePronoun(), getSelf().possessivePronoun(),
                            Global.capitalizeFirstLetter(target.subjectAction("are", "is")),
                            getSelf().nameOrPossessivePronoun(), getSelf().pronoun(),
                            target.directObject(), getSelf().pronoun(), target.possessivePronoun(),
                            target.subjectAction("realize"), getSelf().pronoun(),
                            getSelf().possessivePronoun(), target.nameOrPossessivePronoun(),
                            getSelf().nameOrPossessivePronoun());
        } else if (modifier == Result.strong) {
            return String.format(
                            "%s forces %s legs out of the way and then starts using %s "
                                            + "foot to fondle %s %s. %s was just thinking that this could be much"
                                            + " worse, when %s suddenly lifts the foot up and slams it back down with"
                                            + " great force. %s often feel much pain from %s balls, but the"
                                            + " enormous impact still hurts a lot.",
                            getSelf().name(), target.nameOrPossessivePronoun(),
                            getSelf().possessivePronoun(), target.possessivePronoun(),
                            target.body.getRandomCock().describe(target), 
                            Global.capitalizeFirstLetter(target.subjectAction("were", "was")),
                            getSelf().pronoun(), 
                            Global.capitalizeFirstLetter(target.subjectAction("were", "was")),
                            target.possessivePronoun());
        } else if (modifier == Result.weak2) {
            return String.format("%s forces %s legs open and brutally stomps %s "
                            + "balls. Despite the great blow, %s feel much pain.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.possessivePronoun(), target.subjectAction("don't", "doesn't"));
        } else if (modifier == Result.weak) {
            return String.format("%s grabs %s ankles and stomps down on %s armored groin, doing little damage.",
                            getSelf().subject(), target.nameOrPossessivePronoun(), target.possessivePronoun());
        } else {
            return String.format("%s grabs %s ankles and stomps down on %s unprotected "
                            + "jewels. %s up in the fetal position, groaning in agony.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.subjectAction("curl")));
        }
    }

    @Override
    public String describe(Combat c) {
        return "Stomps on your opponent's groin for extreme damage";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
