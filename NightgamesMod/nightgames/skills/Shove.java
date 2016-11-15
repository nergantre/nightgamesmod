package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;
import nightgames.stance.Mount;
import nightgames.stance.Neutral;
import nightgames.stance.ReverseMount;
import nightgames.status.Falling;
import nightgames.status.Stsflag;

public class Shove extends Skill {
    public Shove(Character self) {
        super("Shove", self);
        addTag(SkillTag.positioning);
        addTag(SkillTag.hurt);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.knockdown);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        if (target.hasStatus(Stsflag.cockbound)) {
            return false;
        }
        return !c.getStance().dom(getSelf()) && !c.getStance().prone(target) && c.getStance().reachTop(getSelf())
                        && getSelf().canAct() && !c.getStance().havingSex(c);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean success = true;
        if (getSelf().get(Attribute.Ki) >= 1 && !target.getOutfit().slotUnshreddable(ClothingSlot.top)
                        && getSelf().canSpend(5)) {
            writeOutput(c, Result.special, target);
            target.shred(ClothingSlot.top);
            target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(10, 25)));
            if (getSelf().check(Attribute.Power, target.knockdownDC() - getSelf().get(Attribute.Ki))) {
                c.setStance(new Neutral(getSelf(), target));
            }
        } else if (c.getStance().getClass() == Mount.class || c.getStance().getClass() == ReverseMount.class) {
            if (getSelf().check(Attribute.Power, target.knockdownDC() + 5)) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You shove " + target.name()
                                    + " off of you and get to your feet before she can retaliate.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s shoves %s hard enough to free %s and jump up.",
                                    getSelf().subject(), target.nameDirectObject(), getSelf().reflectivePronoun()));
                }
                c.setStance(new Neutral(getSelf(), target));
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), "You push " + target.name() + ", but you're unable to dislodge her.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s shoves %s weakly.", getSelf().subject(), 
                                    target.nameDirectObject()));
                }
                success = false;
            }
            target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(8, 20)));
        } else {
            if (getSelf().check(Attribute.Power, target.knockdownDC())) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You shove " + target.name() + " hard enough to knock her flat on her back.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s knocks %s off balance and %s %s at her feet.",
                                    getSelf().subject(), target.nameDirectObject(),
                                    target.pronoun(), target.action("fall")));
                }
                target.add(c, new Falling(target));
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), "You shove " + target.name() + " back a step, but she keeps her footing.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), String.format("%s pushes %s back, but %s %s able to maintain %s balance.",
                                    getSelf().subject(), target.nameDirectObject(), target.pronoun(),
                                    target.action("are", "is"), target.possessivePronoun()));
                }
                success = false;
            }
            target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(16, 25)));
        }
        return success;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 5;
    }

    @Override
    public Skill copy(Character user) {
        return new Shove(user);
    }

    @Override
    public int speed() {
        return 7;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public String getLabel(Combat c) {
        if (getSelf().get(Attribute.Ki) >= 1) {
            return "Shredding Palm";
        } else {
            return getName(c);
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You channel your ki into your hands and strike " + target.name() + " in the chest, destroying her "
                        + target.getOutfit().getTopOfSlot(ClothingSlot.top).getName() + ".";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s strikes %s in the chest with %s palm, staggering %s footing. Suddenly %s "
                        + "%s tears and falls off %s in tatters.", getSelf().subject(),
                        target.nameDirectObject(), getSelf().possessivePronoun(),
                        target.possessivePronoun(), getSelf().nameOrPossessivePronoun(),
                        target.getOutfit().getTopOfSlot(ClothingSlot.top).getName(),
                        target.directObject());
    }

    @Override
    public String describe(Combat c) {
        return "Slightly damage opponent and try to knock her down";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
