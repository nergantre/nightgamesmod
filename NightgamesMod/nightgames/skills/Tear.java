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

public class Tear extends Skill {

    public Tear(Character self) {
        super("Tear Clothes", self);
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
                            && (getSelf().check(Attribute.Power,
                                            article.dc() + (target.getStamina().percent()
                                                            - (target.getArousal().percent()) / 4)
                                            + getSelf().get(Attribute.Medicine) * 4))
                            || !target.canAct()) {
                if (getSelf().human()) {
                    c.write(getSelf(),
                                    Global.format("{self:SUBJECT} leaps forward. {self:POSSESSIVE} hand is a blur but you spot the glint of steel in them. Reflexively, you cover yourself with your arms to prevent as much damage as possible. When nothing happens you open your eyes to see {self:subject} grinning at you, a scalpel still in {self:POSSESSIVE} hands. Looking down you see that some of your clothes have been cut to ribbons!",
                                                    getSelf(), target));
                } else if (target.human()) {
                    c.write(getSelf(),
                                    Global.format("Grabbing your scalpel, you jump forward. The sharp blade makes quick work of {other:possessive}} clothing and your skill with the blade allows you avoid harming them completely. {other:SUBJECT} can only look at you with shock as {other:possessive} shredded clothes float to the ground between you.",
                                                    getSelf(), target));
                }
                target.shred(ClothingSlot.top);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(target.nakedLiner(c));
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
                } else if (target.human()) {
                    c.write(getSelf(), getSelf().name() + " lunges towards you and rakes her nails across your "
                                    + article.getName() + ", shredding the garment. That shouldn't be possible. Her "
                                    + "nails are not that sharp, and if they were, you surely wouldn't have gotten away unscathed.");
                }
                target.shred(ClothingSlot.top);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(target.nakedLiner(c));
                }
            } else if (!article.is(ClothingTrait.indestructible)
                            && getSelf().check(Attribute.Power, article.dc()
                                            + (target.getStamina().percent() - target.getArousal().percent()) / 4)
                            || !target.canAct()) {
                if (getSelf().human()) {
                    c.write(getSelf(), target.name() + " yelps in surprise as you rip her " + article.getName()
                                    + " apart.");
                } else if (target.human()) {
                    c.write(getSelf(), getSelf().name() + " violently rips your " + article.getName() + " off.");
                }
                target.shred(ClothingSlot.top);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(target, target.nakedLiner(c));
                }
            } else if (isMedical) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You try to cut apart " + target.name() + "'s " + article.getName()
                                    + ", but the material is more durable than you expected.");
                } else if (target.human()) {
                    c.write(getSelf(), getSelf().name() + " tries to cut your " + article.getName()
                                    + ", but fails to remove them.");
                }
                getSelf().consume(Item.MedicalSupplies, 1);
                return false;
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), "You try to tear apart " + target.name() + "'s " + article.getName()
                                    + ", but the material is more durable than you expected.");
                } else if (target.human()) {
                    c.write(getSelf(), getSelf().name() + " yanks on your " + article.getName()
                                    + ", but fails to remove it.");
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
                                    Global.format("{self:SUBJECT} leaps forward. {self:POSSESSIVE} hand is a blur but you spot the glint of steel in them. Reflexively, you cover yourself with your arms to prevent as much damage as possible. When nothing happens you open your eyes to see {self:subject} grinning at you, a scalpel still in {self:POSSESSIVE} hands. Looking down you see that some of your clothes have been cut to ribbons!",
                                                    getSelf(), target));
                } else if (target.human()) {
                    c.write(getSelf(),
                                    Global.format("Grabbing your scalpel, you jump forward. The sharp blade makes quick work of {other:possessive}} clothing and your skill with the blade allows you avoid harming them completely. {other:SUBJECT} can only look at you with shock as {other:possessive} shredded clothes float to the ground between you.",
                                                    getSelf(), target));
                }
                target.shred(ClothingSlot.bottom);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(target.nakedLiner(c));
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
                } else if (target.human()) {
                    c.write(getSelf(), getSelf().name() + " lunges towards you and rakes her nails across your "
                                    + article.getName() + ", shredding the garment. That shouldn't be possible. Her "
                                    + "nails are not that sharp, and if they were, you surely wouldn't have gotten away unscathed.");
                }
                target.shred(ClothingSlot.bottom);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(target, target.nakedLiner(c));
                }
                if (target.human() && target.crotchAvailable() && target.hasDick()) {
                    if (target.getArousal().get() >= 15) {
                        c.write("Your boner springs out, no longer restrained by your pants.");
                    } else {
                        c.write(getSelf().name() + " giggles as your flaccid dick is exposed");
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
                } else if (target.human()) {
                    c.write(getSelf(), getSelf().name() + " violently rips your " + article.getName() + " off.");
                }
                target.shred(ClothingSlot.bottom);
                if (getSelf().human() && target.mostlyNude()) {
                    c.write(target, target.nakedLiner(c));
                }
                if (target.human() && target.crotchAvailable()) {
                    if (target.getArousal().get() >= 15) {
                        c.write("Your boner springs out, no longer restrained by your pants.");
                    } else {
                        c.write(getSelf().name() + " giggles as your flaccid dick is exposed");
                    }
                }
                target.emote(Emotion.nervous, 10);
            } else if (isMedical) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You try to cut apart " + target.name() + "'s " + article.getName()
                                    + ", but the material is more durable than you expected.");
                } else if (target.human()) {
                    c.write(getSelf(), getSelf().name() + " tries to cut your " + article.getName()
                                    + ", but fails to remove them.");
                }
                getSelf().consume(Item.MedicalSupplies, 1);
                return false;
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), "You try to tear apart " + target.name() + "'s " + article.getName()
                                    + ", but the material is more durable than you expected.");
                } else if (target.human()) {
                    c.write(getSelf(), getSelf().name() + " yanks on your " + article.getName()
                                    + ", but fails to remove them.");
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
