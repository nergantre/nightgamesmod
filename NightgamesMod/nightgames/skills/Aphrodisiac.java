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

public class Aphrodisiac extends Skill {
    public Aphrodisiac(Character self) {
        super("Use Aphrodisiac", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }
    
    private final Predicate<BodyPart> hasSuccubusPussy = new Predicate<BodyPart>(){

        @Override
        public boolean test(BodyPart bodyPart) {
            return bodyPart.isType("pussy") && bodyPart.moddedPartCountsAs(getSelf(), PussyPart.succubus);
        }
        
    };

    @Override
    public boolean usable(Combat c, Character target) {
        boolean canMove = c.getStance().mobile(getSelf()) && getSelf().canAct();
        boolean hasItem = getSelf().has(Item.Aphrodisiac);
        boolean canGetFromOwnBody = !(getSelf().body.getCurrentPartsThatMatch(hasSuccubusPussy).isEmpty())
                        && getSelf().getArousal().get() >= 10 && !c.getStance().prone(getSelf());
        
        return canMove && (hasItem || canGetFromOwnBody);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int magnitude = Global.random(5) + 15;
        if (!getSelf().body.getCurrentPartsThatMatch(hasSuccubusPussy).isEmpty()) {//TODO: Arousal check? Otherwise, you can bypass the arousal check by having aphrodisiac in inventory
            c.write(getSelf(), receive(c, magnitude, Result.strong, target));
            target.arouse(magnitude, c);
            target.emote(Emotion.horny, 20);
        } else if (getSelf().has(Item.Aersolizer)) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.special, target));
            } else if (target.human()) {
                c.write(getSelf(), receive(c, 0, Result.special, getSelf()));
            }
            getSelf().consume(Item.Aphrodisiac, 1);
            target.arouse(magnitude, c);
            target.emote(Emotion.horny, 20);
        } else if (target.roll(this, c, accuracy(c))) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, magnitude, Result.normal, target));
            } else {
                c.write(getSelf(), receive(c, magnitude, Result.normal, getSelf()));
            }
            target.emote(Emotion.horny, 20);
            getSelf().consume(Item.Aphrodisiac, 1);
            target.arouse(magnitude, c);
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.miss, target));
            } else if (target.human()) {
                c.write(getSelf(), receive(c, 0, Result.miss, target));
            }
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
            return "You pop an Aphrodisiac into your Aerosolizer and spray " + target.name()
                            + " with a cloud of mist. She flushes and her eyes fill with lust as it takes hold.";
        } else if (modifier == Result.miss) {
            return "You throw an Aphrodisiac at " + target.name()
                            + ", but she ducks out of the way and it splashes harmlessly on the ground. What a waste.";
        } else if (modifier == Result.strong) {
            return getSelf().subjectAction("dip", "dips") + " a finger "
                            + (getSelf().crotchAvailable() ? ""
                                            : "under " + getSelf().possessivePronoun() + " "
                                                            + getSelf().getOutfit().getTopOfSlot(ClothingSlot.bottom)
                                                                            .getName()
                                                            + " and ")
                            + "into " + getSelf().possessivePronoun() + " pussy. Once "
                            + getSelf().subjectAction("have", "has") + " collected a drop of "
                            + getSelf().possessivePronoun() + " juices" + " on " + getSelf().possessivePronoun()
                            + " fingertip, " + getSelf().subjectAction("pull", "pulls") + " it out and flicks it at "
                            + target.directObject() + "," + " skillfully depositing it in " + target.possessivePronoun()
                            + " open mouth. " + Global.capitalizeFirstLetter(target.subject()) + " immediately feel"
                            + " a flash of heat spread through " + target.directObject()
                            + " and only a small part of it" + " results from the anger caused by "
                            + getSelf().possessivePronoun() + " dirty move.";
        } else {
            return "You uncap a small bottle of Aphrodisiac and splash it in " + target.name()
                            + "'s face. For a second, she's just surprised, but gradually a growing desire "
                            + "starts to make her weak in the knees.";
        }

    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return getSelf().subjectAction("splash", "splashes") + " a bottle of liquid in "
                            + target.nameOrPossessivePronoun() + " direction, but none of it hits you.";
        } else if (modifier == Result.special) {
            return getSelf().name()
                            + " inserts a bottle into the attachment on her arm. You're suddenly surrounded by a sweet smelling cloud of mist. You feel your blood boil "
                            + "with desire as the unnatural gas takes effect";
        } else if (modifier == Result.strong) {
            return getSelf().subjectAction("dip", "dips") + " a finger "
                            + (getSelf().crotchAvailable() ? ""
                                            : "under " + getSelf().possessivePronoun() + " "
                                                            + getSelf().getOutfit().getTopOfSlot(ClothingSlot.bottom)
                                                                            .getName()
                                                            + " and ")
                            + "into " + getSelf().possessivePronoun() + " pussy. Once "
                            + getSelf().subjectAction("have", "has") + " collected a drop of "
                            + getSelf().possessivePronoun() + " juices" + " on " + getSelf().possessivePronoun()
                            + " fingertip, " + getSelf().subjectAction("pull", "pulls") + " it out and flicks it at "
                            + target.directObject() + "," + " skillfully depositing it in " + target.possessivePronoun()
                            + " open mouth. " + Global.capitalizeFirstLetter(target.subject()) + " immediately feel"
                            + " a flash of heat spread through " + target.directObject()
                            + " and only a small part of it" + " results from the anger caused by "
                            + getSelf().possessivePronoun() + " dirty move.";
        } else {
            return getSelf().name()
                            + " throws a strange, sweet-smelling liquid in your face. An unnatural warmth spreads through your body and gathers in your dick like a fire.";
        }
    }

    @Override
    public String describe(Combat c) {
        return "Throws a bottle of Aphrodisiac at the opponent";
    }
}
