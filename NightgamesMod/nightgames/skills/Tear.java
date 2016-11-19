package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.nskills.tags.SkillTag;

public class Tear extends Skill {

    public Tear(Character self) {
        super("Tear Clothes", self);
        addTag(SkillTag.stripping);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 32 || user.get(Attribute.Animism) >= 12
                        || user.get(Attribute.Medicine) >= 12;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        boolean notMedical = getSelf().get(Attribute.Power) >= 32 || getSelf().get(Attribute.Animism) >= 12;
        return ((c.getStance().reachTop(getSelf()) && !target.breastsAvailable())
                        || ((c.getStance().reachBottom(getSelf()) && !target.crotchAvailable()))) && getSelf().canAct()
                        && (notMedical || getSelf().has(Item.MedicalSupplies, 1));
    }

    @Override
    public String describe(Combat c) {
        if (getSelf().get(Attribute.Medicine) >= 12 && getSelf().has(Item.MedicalSupplies, 1)) {
            return "Dissect your opponent's clothing";
        }
        return "Rip off your opponent's clothes";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean isMedical = (getSelf().get(Attribute.Medicine) >= 12 && getSelf().has(Item.MedicalSupplies, 1));
        if (c.getStance().reachTop(getSelf()) && !target.getOutfit().slotEmpty(ClothingSlot.top)) {
            Clothing article = target.getOutfit().getTopOfSlot(ClothingSlot.top);
            if (!article.is(ClothingTrait.indestructible) && isMedical
                            && (((getSelf().check(Attribute.Power,
                                            article.dc() + (target.getStamina().percent()
                                                            - (target.getArousal().percent()) / 4)
                                            + getSelf().get(Attribute.Medicine) * 4)) || !target.canAct()))) {
                if (getSelf().human()) {
                    c.write(getSelf(),
                                    Global.format("Grabbing your scalpel, you jump forward. The sharp blade makes quick work of {other:possessive}} clothing and your skill with the blade allows you avoid harming them completely. {other:SUBJECT} can only look at you with shock as {other:possessive} shredded clothes float to the ground between you.",
                                                    getSelf(), target));
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(),
                                    Global.format("{self:SUBJECT} leaps forward. {self:POSSESSIVE} hand is a blur but {other:subject-action:spot|spots} the glint of steel in them. Reflexively, {other:pronoun-action:cover|covers} {other:reflective} with {other:possessive} arms to prevent as much damage as possible. When nothing happens {other:subject-action:open|opens} {other:possessive} eyes to see {self:subject} grinning at {other:direct-object}, a scalpel still in {self:POSSESSIVE} hands. Looking down {other:pronoun-action:see|sees} that some of {other:possessive} clothes have been cut to ribbons!",
                                                    getSelf(), target));
                }
                target.shred(ClothingSlot.top);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(getSelf(), target.nakedLiner(c, target));
                }
                getSelf().consume(Item.MedicalSupplies, 1);
            } else if (!article.is(ClothingTrait.indestructible) && getSelf().get(Attribute.Animism) >= 12
                            && (getSelf().check(Attribute.Power,
                                            article.dc() + (target.getStamina().percent()
                                                            - (target.getArousal().percent()) / 4)
                                            + getSelf().get(Attribute.Animism) * getSelf().getArousal().percent() / 100)
                            || !target.canAct())) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You channel your animal spirit and shred " + target.name() + "'s "
                                    + article.getName() + " with claws you don't actually have.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s lunges towards %s and rakes %s nails across %s %s, "
                                    + "shredding the garment. That shouldn't be possible. %s "
                                    + "nails are not that sharp, and if they were, %s surely wouldn't have gotten away unscathed.",
                                    getSelf().subject(), target.nameDirectObject(), getSelf().possessivePronoun(),
                                    target.possessivePronoun(), article.getName(),
                                    Global.capitalizeFirstLetter(getSelf().pronoun()),
                                    target.nameDirectObject()));
                }
                target.shred(ClothingSlot.top);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(getSelf(), target.nakedLiner(c, target));
                }
            } else if (!article.is(ClothingTrait.indestructible)
                            && getSelf().check(Attribute.Power, article.dc()
                                            + (target.getStamina().percent() - target.getArousal().percent()) / 4)
                            || !target.canAct()) {
                if (getSelf().human()) {
                    c.write(getSelf(), target.name() + " yelps in surprise as you rip her " + article.getName()
                                    + " apart.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s violently rips %s %s off.",
                                    getSelf().subject(), target.nameOrPossessivePronoun(), article.getName()));
                }
                target.shred(ClothingSlot.top);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(target, target.nakedLiner(c, target));
                }
            } else if (isMedical) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You try to cut apart " + target.name() + "'s " + article.getName()
                                    + ", but the material is more durable than you expected.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s tries to cut %s %s, but fails to remove them.",
                                    getSelf().subject(), target.nameOrPossessivePronoun(), article.getName()));
                }
                getSelf().consume(Item.MedicalSupplies, 1);
                return false;
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), "You try to tear apart " + target.name() + "'s " + article.getName()
                                    + ", but the material is more durable than you expected.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s yanks on %s %s, but fails to remove it.",
                                    getSelf().subject(), target.nameOrPossessivePronoun(), article.getName()));
                }
            }
        } else if (!target.getOutfit().slotEmpty(ClothingSlot.bottom)) {
            Clothing article = target.getOutfit().getTopOfSlot(ClothingSlot.bottom);
            if (!article.is(ClothingTrait.indestructible) && isMedical
                            && (getSelf().check(Attribute.Power,
                                            article.dc() + (target.getStamina().percent()
                                                            - (target.getArousal().percent()) / 4)
                                            + getSelf().get(Attribute.Medicine) * 4))
                            || !target.canAct()) {
                if (getSelf().human()) {
                    c.write(getSelf(),
                                    Global.format("Grabbing your scalpel, you jump forward. The sharp blade makes quick work of {other:possessive} clothing and your skill with the blade allows you avoid harming them completely. {other:SUBJECT} can only look at you with shock as {other:possessive} shredded clothes float to the ground between you.",
                                                    getSelf(), target));
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(),
                                    Global.format("{self:SUBJECT} leaps forward. {self:POSSESSIVE} hand is a blur but {other:subject-action:spot|spots} the glint of steel in them. Reflexively, {other:pronoun-action:cover|covers} {other:reflective} with {other:possessive} arms to prevent as much damage as possible. When nothing happens {other:subject-action:open|opens} {other:possessive} eyes to see {self:subject} grinning at {other:direct-object}, a scalpel still in {self:POSSESSIVE} hands. Looking down {other:pronoun-action:see|sees} that some of {ohter:possessive} clothes have been cut to ribbons!",
                                                    getSelf(), target));
                }
                target.shred(ClothingSlot.bottom);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(getSelf(), target.nakedLiner(c, target));
                }
                getSelf().consume(Item.MedicalSupplies, 1);
            } else if (!article.is(ClothingTrait.indestructible) && getSelf().get(Attribute.Animism) >= 12
                            && getSelf().check(Attribute.Power,
                                            article.dc() + (target.getStamina().percent()
                                                            - (target.getArousal().percent()) / 4)
                                            + getSelf().get(Attribute.Animism) * getSelf().getArousal().percent() / 100)
                            || !target.canAct()) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You channel your animal spirit and shred " + target.name() + "'s "
                                    + article.getName() + " with claws you don't actually have.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s lunges towards %s and rakes %s nails across %s %s, "
                                    + "shredding the garment. That shouldn't be possible. %s "
                                    + "nails are not that sharp, and if they were, %s surely wouldn't have gotten away unscathed.",
                                    getSelf().subject(), target.nameDirectObject(), getSelf().possessivePronoun(),
                                    target.possessivePronoun(), article.getName(),
                                    Global.capitalizeFirstLetter(getSelf().pronoun()),
                                    target.nameDirectObject()));
                }
                target.shred(ClothingSlot.bottom);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(target, target.nakedLiner(c, target));
                }
                if (target.human() && target.crotchAvailable() && target.hasDick()) {
                    if (target.getArousal().get() >= 15) {
                        c.write(getSelf(), String.format("%s boner springs out, no longer restrained by %s pants.",
                                        target.nameOrPossessivePronoun(), target.possessivePronoun()));
                    } else {
                        c.write(getSelf(), String.format("%s giggles as %s flaccid dick is exposed.",
                                        getSelf().subject(), target.nameOrPossessivePronoun()));
                    }
                }
                target.emote(Emotion.nervous, 10);
            } else if (!article.is(ClothingTrait.indestructible)
                            && getSelf().check(Attribute.Power, article.dc()
                                            + (target.getStamina().percent() - target.getArousal().percent()) / 4)
                            || !target.canAct()) {
                if (getSelf().human()) {
                    c.write(getSelf(), target.name() + " yelps in surprise as you rip her " + article.getName()
                                    + " apart.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s violently rips %s %s off.",
                                    getSelf().subject(), target.nameOrPossessivePronoun(), article.getName()));
                }
                target.shred(ClothingSlot.bottom);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(target, target.nakedLiner(c, target));
                }
                if (target.human() && target.crotchAvailable()) {
                    if (target.getArousal().get() >= 15) {
                        c.write(getSelf(), String.format("%s boner springs out, no longer restrained by %s pants.",
                                        target.nameOrPossessivePronoun(), target.possessivePronoun()));
                    } else {
                        c.write(getSelf(), String.format("%s giggles as %s flaccid dick is exposed.",
                                        getSelf().subject(), target.nameOrPossessivePronoun()));
                    }
                }
                target.emote(Emotion.nervous, 10);
            } else if (isMedical) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You try to cut apart " + target.name() + "'s " + article.getName()
                                    + ", but the material is more durable than you expected.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s tries to cut %s %s, but fails to remove them.",
                                    getSelf().subject(), target.nameOrPossessivePronoun(), article.getName()));
                }
                getSelf().consume(Item.MedicalSupplies, 1);
                return false;
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), "You try to tear apart " + target.name() + "'s " + article.getName()
                                    + ", but the material is more durable than you expected.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s yanks on %s %s, but fails to remove it.",
                                    getSelf().subject(), target.nameOrPossessivePronoun(), article.getName()));
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Tear(user);
    }

    public String getLabel(Combat c) {
        if (getSelf().get(Attribute.Medicine) >= 12) {
            return "Clothing-ectomy";
        } else if (getSelf().get(Attribute.Animism) >= 12) {
            return "Shred Clothes";
        } else {
            return "Tear Clothes";
        }
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return "";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
