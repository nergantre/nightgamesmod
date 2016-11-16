package nightgames.skills;

import java.util.function.Predicate;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.Horny;

public class Aphrodisiac extends Skill {
    public Aphrodisiac(Character self) {
        super("Use Aphrodisiac", self);
        addTag(SkillTag.debuff);
        addTag(SkillTag.arouse);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    private final Predicate<BodyPart> hasSuccubusPussy = (bodyPart) -> bodyPart.isType("pussy") && bodyPart.moddedPartCountsAs(getSelf(), PussyPart.succubus);

    @Override
    public boolean usable(Combat c, Character target) {
        boolean canMove = c.getStance()
                           .mobile(getSelf())
                        && getSelf().canAct();
        boolean hasItem = getSelf().has(Item.Aphrodisiac);
        boolean canGetFromOwnBody = !(getSelf().body.getCurrentPartsThatMatch(hasSuccubusPussy)
                                                    .isEmpty())
                        && getSelf().getArousal()
                                    .get() >= 10
                        && !c.getStance()
                             .prone(getSelf());
        return canMove && (hasItem || canGetFromOwnBody);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        float magnitude = 10;
        if (!getSelf().body.getCurrentPartsThatMatch(hasSuccubusPussy)
                           .isEmpty()
                        && getSelf().getArousal()
                                    .percent() >= 15) {
            writeOutput(c, (int) magnitude, Result.strong, target);
            target.add(c, Horny.getWithBiologicalType(getSelf(), target, magnitude, 5, getSelf().nameOrPossessivePronoun() + " aphrodisiac juices"));
            target.emote(Emotion.horny, 20);
        } else if (getSelf().has(Item.Aersolizer)) {
            writeOutput(c, Result.special, target);
            getSelf().consume(Item.Aphrodisiac, 1);
            target.add(c, Horny.getWithBiologicalType(getSelf(), target, magnitude, 5, getSelf().nameOrPossessivePronoun() + " aphrodisiac spray"));
            target.emote(Emotion.horny, 20);
        } else if (target.roll(getSelf(), c, accuracy(c))) {
            writeOutput(c, (int) magnitude, Result.normal, target);
            target.emote(Emotion.horny, 20);
            getSelf().consume(Item.Aphrodisiac, 1);
            target.add(c, Horny.getWithBiologicalType(getSelf(), target, magnitude, 5, getSelf().nameOrPossessivePronoun() + " aphrodisiacs"));
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Aphrodisiac(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return String.format(
                            "You pop an Aphrodisiac into your Aerosolizer and spray %s"
                                            + " with a cloud of mist. %s flushes and %s eyes fill with lust as it takes hold.",
                            target.name(), Global.capitalizeFirstLetter(target.pronoun()), target.possessivePronoun());
        } else if (modifier == Result.miss) {
            return "You throw an Aphrodisiac at " + target.name() + ", but " + target.pronoun()
                            + " ducks out of the way and it splashes harmlessly on the ground. What a waste.";
        } else if (modifier == Result.strong) {
            return getSelf().subjectAction("dip") + " a finger " + (getSelf().crotchAvailable() ? ""
                            : "under " + getSelf().possessivePronoun() + " " + getSelf().getOutfit()
                                                                                        .getTopOfSlot(ClothingSlot.bottom)
                                                                                        .getName()
                                            + " and ")
                            + "into " + getSelf().possessivePronoun() + " pussy. Once "
                            + getSelf().subjectAction("have", "has") + " collected a drop of "
                            + getSelf().possessivePronoun() + " juices" + " on " + getSelf().possessivePronoun()
                            + " fingertip, " + getSelf().subjectAction("pull") + " it out and flicks it at "
                            + target.directObject() + "," + " skillfully depositing it in " + target.possessivePronoun()
                            + " open mouth. " + Global.capitalizeFirstLetter(target.subject()) + " immediately feel"
                            + " a flash of heat spread through " + target.directObject()
                            + " and only a small part of it results from the anger caused by "
                            + getSelf().possessivePronoun() + " dirty move.";
        } else {
            return "You uncap a small bottle of Aphrodisiac and splash it in " + target.name()
                            + "'s face. For a second, " + target.possessivePronoun()
                            + "'s just surprised, but gradually a growing desire " + "starts to make "
                            + target.directObject() + " weak in the knees.";
        }

    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return getSelf().subjectAction("splash", "splashes") + " a bottle of liquid in "
                            + target.nameOrPossessivePronoun() + " direction, but none of it hits "
                            + target.directObject() + ".";
        } else if (modifier == Result.special) {
            return String.format(
                            "%s inserts a bottle into the attachment on %s arm. %s suddenly surrounded by a sweet smelling cloud of mist. %s %s %s blood boil "
                                            + "with desire as the unnatural gas takes effect.",
                            getSelf().name(), getSelf().possessivePronoun(),
                            Global.capitalizeFirstLetter(target.subjectAction("are", "is")),
                            Global.capitalizeFirstLetter(target.pronoun()), target.action("feel"),
                            target.possessivePronoun());
        } else if (modifier == Result.strong) {
            return getSelf().subjectAction("dip") + " a finger " + (getSelf().crotchAvailable() ? ""
                            : "under " + getSelf().possessivePronoun() + " " + getSelf().getOutfit()
                                                                                        .getTopOfSlot(ClothingSlot.bottom)
                                                                                        .getName()
                                            + " and ")
                            + "into " + getSelf().possessivePronoun() + " pussy. Once "
                            + getSelf().subjectAction("have", "has") + " collected a drop of "
                            + getSelf().possessivePronoun() + " juices" + " on " + getSelf().possessivePronoun()
                            + " fingertip, " + getSelf().subjectAction("pull") + " it out and flicks it at "
                            + target.directObject() + "," + " skillfully depositing it in " + target.possessivePronoun()
                            + " open mouth. " + Global.capitalizeFirstLetter(target.subject()) + " immediately feel"
                            + " a flash of heat spread through " + target.directObject()
                            + " and only a small part of it" + " results from the anger caused by "
                            + getSelf().possessivePronoun() + " dirty move.";
        } else {
            String part;
            if (target.hasDick())
                part = target.body.getRandomCock().describe(target);
            else if (target.hasPussy())
                part = "clit";
            else
                part = "nipples";
            return String.format("%s throws a strange, sweet-smelling liquid in %s face."
                            + " An unnatural warmth spreads through %s body and gathers in %s %s like a fire.",
                            getSelf().name(), target.nameOrPossessivePronoun(), target.possessivePronoun(),
                             target.possessivePronoun(), part);
        }
    }

    @Override
    public String describe(Combat c) {
        return "Throws a bottle of Aphrodisiac at the opponent";
    }
}
