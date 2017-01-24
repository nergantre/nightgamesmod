package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.stance.Position;

public class WingWrapped extends Status {

    private final Character wrapper;
    private final int strength;
    private Position initialPosition;

    public WingWrapped(Character affected, Character wrapper, int strength) {
        super("Wing Wrapped", affected);
        this.wrapper = wrapper;
        this.strength = strength;
        flag(Stsflag.wrapped);
    }
    
    public WingWrapped(Character affected, Character wrapper) {
        this(affected, wrapper, calcStrength(wrapper));
    }

    private static int calcStrength(Character wrapper) {
        return wrapper.get(Attribute.Power) / 4 + wrapper.get(Attribute.Dark) / 6;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        String msg = "{other:NAME-POSSESSIVE} powerful {other:body-part:wings} are holding"
                        + " {self:name-do} in place";
        if (wrapper.has(Trait.VampireWings) && affected.outfit.slotEmpty(ClothingSlot.top)) {
            if (wrapper.human()) {
                msg += ", and they are feeding you {self:possessive} power";
            } else {
                msg += ", and every bit of {self:possessive} skin they touch seems to go numb with weakness";
            }
        }
        return Global.format(msg + ".", affected, wrapper);
    }

    @Override
    public String describe(Combat c) {
        String msg = "{self:SUBJECT-ACTION:are|is} held tightly by {other:name-possessive} {other:body-part:wings}";
        if (wrapper.has(Trait.VampireWings) && affected.outfit.slotEmpty(ClothingSlot.top)) {
            if (wrapper.human()) {
                msg += ", and they are feeding you {self:possessive} power";
            } else {
                msg += ", and every bit of {self:possessive} skin they touch seems to go numb with weakness";
            }
        }
        return Global.format(msg + ".", affected, wrapper);
    }

    @Override
    public int mod(Attribute a) {
        if (a == Attribute.Speed) {
            return -strength/4;
        }
        return 0;
    }

    @Override
    public void tick(Combat c) {
        if (initialPosition == null) {
            initialPosition = c.getStance();
        } else if (!c.getStance().equals(initialPosition) && !canPersist(c)) {
            c.write(wrapper, Global.format("Lacking sufficient purchase to keep"
                            + " {self:name-do} in check any longer, {other:name-possessive}"
                            + " {other:body-part:wings} return to their regular place behind"
                            + " {other:possessive} back.", affected, wrapper));
            affected.removelist.add(this);
            return;
        }
        if (!wrapper.body.has("wings")) {
            c.write(Global.format("Now that {other:name-possessive} wings are gone,"
                            + " they can no longer confine {self:name-do}.", affected, wrapper));
            affected.removelist.add(this);
        } else if (wrapper.has(Trait.VampireWings) && affected.outfit.slotEmpty(ClothingSlot.top)) {
            if (affected.get(Attribute.Power) < 6) {
                c.write(wrapper, Global.format("{other:NAME-POSSESSIVE} {other:body-part:wings}, pressed"
                                + " against {self:name-possessive} bare skin, try to reel in"
                                + " {self:possessive} power, but they fail to draw on what little"
                                + " remains within {self:direct-object}.", affected, wrapper));
            } else {
                c.write(wrapper, Global.format("{other:NAME-POSSESSIVE} {other:body-part:wings}, pressed"
                                + " against {self:name-possessive} bare skin, leech {self:possessive}"
                                + " power from {self:possessive} body, letting it flow back into"
                                + " {other:direct-object}.", affected, wrapper));
                affected.add(c, new Abuff(affected, Attribute.Power, -3, 20));
                wrapper.add(c, new Abuff(affected, Attribute.Power, 3, 20));
                Abuff.drain(c, wrapper, affected, Attribute.Power, 3, 20, true);
            }
        }
    }
    
    private boolean canPersist(Combat c) {
        return c.getStance().havingSex(c) || c.getStance().distance() < 2;
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        return 0;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }

    @Override
    public int weakened(int x) {
        return 0;
    }

    @Override
    public int tempted(int x) {
        return 0;
    }

    @Override
    public int evade() {
        return -strength;
    }

    @Override
    public int escape() {
        return -strength;
    }

    @Override
    public int gainmojo(int x) {
        return 0;
    }

    @Override
    public int spendmojo(int x) {
        return 0;
    }

    @Override
    public int counter() {
        return -strength;
    }

    @Override
    public int value() {
        return -strength/5;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new WingWrapped(newAffected, newOther, strength);
    }

    @Override
    public JsonObject saveToJson() {
        return null;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return null;
    }

}
