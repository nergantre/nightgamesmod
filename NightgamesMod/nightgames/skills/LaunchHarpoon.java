package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;

public class LaunchHarpoon extends Skill {

    public LaunchHarpoon(Character self) {
        super("Launch Harpoon", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.harpoon);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) &&
                        !c.getStance().sub(getSelf())
                        && (target.hasDick() || target.hasPussy())
                        && target.outfit.slotEmpty(ClothingSlot.bottom);
    }

    @Override
    public String describe(Combat c) {
        return "Launch your harpoon toy at your opponent's genitals.";
    }
    
    @Override
    public float priorityMod(Combat c) {
        return 5.f;
    }
    
    @Override
    public int getMojoCost(Combat c) {
        int cost = 20;
        if (getSelf().has(Trait.yank)) {
            cost += 10;
        }
        if (getSelf().has(Trait.conducivetoy)) {
            cost += 10;
        }
        if (getSelf().has(Trait.intensesuction)) {
            cost += 10;
        }
        return cost;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (!target.canAct() || c.getStance().sub(target) || target.roll(getSelf(), c, accuracy(c, target))) {
            String aim;
            if (!target.canAct()) {
                aim = "With {other:subject} unable to do anything to evade it, when"
                                + " {self:subject-action:level|levels} {self:possessive} arm-mounted"
                                + " device at {other:possessive} crotch and fires {self:possessive}"
                                + " harpoon toy at {other:direct-object}, it easily finds its goal. ";
            } else if (c.getStance().sub(target)) {
                aim = "From {self:name-possessive} dominant position, it's trivial for {self:direct-object}"
                                + " to lower {self:possessive} arm-launched harpoon to {other:name-possessive} crotch to make"
                                + " sure it doesn't miss. ";
            } else {
                aim = "{self:SUBJECT-ACTION:raise|raises} {self:possessive} arm at {other:name-do}. The harpoon-mounted"
                                + " toy attached to it glistens with lubricant, and when"
                                + " {self:pronoun-action:let|lets} it fly at {other:possessive} crotch,"
                                + " {other:pronoun-action:have|has} no chance to evade it. ";
            }
            if (target.hasDick()) {
                c.write(getSelf(), Global.format("%sThe soft material of the toy shapes itself"
                                + " around {other:name-possessive} {other:body-part:cock}, creating"
                                + " an airtight seal around the shaft. {self:SUBJECT-ACTION:press|presses}"
                                + " a button on the device on {self:possessive} arm, and a strong suction"
                                + " ripples through the toy, firmly locking it in place and starting a"
                                + " strangely pleasurable vibration.", getSelf(), target, aim));
               target.outfit.equip(Clothing.getByID("harpoononahole"));
            } else {
                c.write(getSelf(), Global.format("%sThe pliable material crawls its way inside of"
                                + " {other:name-possessive} {other:body-part:pussy}, shaping itself"
                                + " to fill it perfectly. The excess at the base forms a cup which"
                                + " settles over {other:possessive} mons, which"
                                + " {self:subject}, with the press of a button, activates. The cup"
                                + " sucks itself tightly to {other:name-possessive} skin; it's not going"
                                + " to come off easily. Meanwhile, the part of the toy lodged inside of"
                                + " {other:direct-object} starts vibrating and squirming against"
                                + " {other:possessive} sensitive flesh.", getSelf(), target, aim));
                target.outfit.equip(Clothing.getByID("harpoondildo"));
            }
            return true;
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:let|lets} {self:possessive} harpoon-like"
                            + " toy fly from its slot on {self:possessive} arm device towards"
                            + " {other:name-do}, but {other:pronoun} easily {other:action:evade|evades} it,", 
                            getSelf(), target));
        }
        return false;
    }

    @Override
    public Skill copy(Character user) {
        return new LaunchHarpoon(user);
    }
    
    @Override
    public int accuracy(Combat c, Character target) {
        int acc = 80;
        if (getSelf().has(Trait.yank)) {
            acc += 4;
        }
        if (getSelf().has(Trait.conducivetoy)) {
            acc += 4;
        }
        if (getSelf().has(Trait.intensesuction)) {
            acc += 4;
        }
        return acc;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

}
