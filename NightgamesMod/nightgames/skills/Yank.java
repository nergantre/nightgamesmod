package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.stance.Stance;
import nightgames.status.Falling;

public class Yank extends Skill {

    public Yank(Character self) {
        super("Yank", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.yank);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().en  == Stance.neutral && (target.has(ClothingTrait.harpoonDildo)
                        || target.has(ClothingTrait.harpoonOnahole));
    }

    @Override
    public String describe(Combat c) {
        return "Give a tug on your toy to trip your opponent.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int acc = 70;
        int removeChance = 50;
        if (getSelf().has(Trait.intensesuction)) {
            acc += 20;
            removeChance /= 2;
        }
        if (target.roll(getSelf(), c, acc)) {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:yank|yanks} {other:name-do}"
                            + " forward by the toy still connecting them, and "
                            + " {other:pronoun-action} stumbles and falls.", getSelf(), target));
            target.add(c, new Falling(target));
            if (Global.random(100) < removeChance) {
                c.write("The powerful tug dislodges the toy, causing it to retract back where it was launched from.");
                target.outfit.unequip(target.outfit.getBottomOfSlot(ClothingSlot.bottom));
            }
            return true;
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:pull|pulls} {other:name-do}"
                            + " forward by the toy still connecting them, but "
                            + " {other:pronoun-action:keep|keeps} {other:possessive}"
                            + " balance.", getSelf(), target));
        }
        return false;
    }

    @Override
    public Skill copy(Character user) {
        return new Yank(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
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
