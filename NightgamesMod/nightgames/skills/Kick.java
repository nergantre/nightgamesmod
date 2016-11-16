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

public class Kick extends Skill {

    public Kick(Character self) {
        super("Kick", self);
        addTag(SkillTag.hurt);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 17;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().feet(getSelf(), target) && getSelf().canAct() && (!c.getStance().prone(getSelf())
                        || getSelf().has(Trait.dirtyfighter) && !c.getStance().connected(c));
    }

    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (!target.getOutfit().slotUnshreddable(ClothingSlot.bottom) && getSelf().get(Attribute.Ki) >= 14
                        && Global.random(3) == 2) {
            writeOutput(c, Result.special, target);
            target.shred(ClothingSlot.bottom);
        } else
        if (target.roll(getSelf(), c, accuracy(c))) {
            double m = Global.random(16, 21);
            if (target.has(Trait.brassballs)) {
                m *= .8;
            }
            if (getSelf().human()) {
                if (c.getStance().prone(getSelf())) {
                    c.write(getSelf(), deal(c, 0, Result.strong, target));
                } else {
                    c.write(getSelf(), deal(c, 0, Result.normal, target));

                }
            } else if (c.shouldPrintReceive(target, c)) {
                if (c.getStance().prone(getSelf())) {
                    c.write(getSelf(), receive(c, 0, Result.strong, target));
                } else {
                    c.write(getSelf(), receive(c, 0, Result.normal, target));
                }
            }
            if (target.has(Trait.achilles) && !target.has(ClothingTrait.armored)) {
                m += 14 + Global.random(4);
            }
            if (target.has(ClothingTrait.armored)) {
                m = m / 2;
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
    public Skill copy(Character user) {
        return new Kick(user);
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String getLabel(Combat c) {
        if (getSelf().get(Attribute.Ki) >= 14) {
            return "Shatter Kick";
        } else {
            return "Kick";
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "Your kick hits nothing but air.";
        }
        if (modifier == Result.special) {
            return "You focus your ki into a single kick, targeting not " + target.name() + "'s body, but her "
                            + target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()
                            + ". The garment is completely destroyed, but "
                            + "she is safely left completely unharmed. Wait, you are actually fighting right now, aren't you?";
        }
        if (modifier == Result.strong) {
            return "Lying on the floor, you feign exhaustion, hoping " + target.name()
                            + " will lower her guard. As she approaches unwarily, you suddenly kick up between "
                            + "her legs, delivering a painful hit to her sensitive vulva.";
        } else {
            return "You deliver a swift kick between " + target.name()
                            + "'s legs, hitting her squarely on the baby maker.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return getSelf().name() + "'s kick hits nothing but air.";
        }
        if (modifier == Result.special) {
            return String.format("%s launches a powerful kick straight at %s groin, but pulls it back "
                            + "just before impact. %s a chill run down %s spine and %s testicles "
                            + "are grateful for the last second reprieve. %s %s crumble off %s body,"
                            + " practically disintegrating.... Still somewhat grateful.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            Global.capitalizeFirstLetter(target.subjectAction("feel")),
                            target.possessivePronoun(), target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.possessivePronoun()),
                            target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName(),
                            target.possessivePronoun());
        }
        if (modifier == Result.strong) {
            return String.format("With %s flat on %s back, %s quickly %s in to press %s advantage. "
                            + "Faster than %s can react, %s foot shoots up between "
                            + "%s legs, dealing a critical hit on %s unprotected balls.",
                            getSelf().subject(), getSelf().possessivePronoun(), target.subject(),
                            target.action("move"), target.possessivePronoun(), target.pronoun(),
                            getSelf().nameOrPossessivePronoun(), target.nameOrPossessivePronoun(),
                            target.possessivePronoun());
        } else {
            return String.format("%s foot lashes out into %s delicate testicles with devastating force.",
                            getSelf().nameOrPossessivePronoun(), target.nameOrPossessivePronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Kick your opponent in the groin";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
