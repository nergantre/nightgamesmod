package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class UpsideDownMaledom extends MaledomSexStance {
    public UpsideDownMaledom(Character top, Character bottom) {
        super(top, bottom, Stance.upsidedownmaledom);
    }

    @Override
    public int pinDifficulty(Combat c, Character self) {
        return 8;
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You are holding " + bottom.name() + " upsidedown by her legs while fucking her pussy.";
        } else {
            return String.format("%s is holding %s upsidedown by %s legs while fucking %s pussy.",
                            top.subject(), bottom.nameDirectObject(), bottom.possessivePronoun(),
                            top.possessivePronoun());
        }
    }

    @Override
    public String image() {
        return "upsidedownmaledom.jpg";
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c != top && c != bottom;
    }

    @Override
    public boolean dom(Character c) {
        return c == top;
    }

    @Override
    public boolean sub(Character c) {
        return c == bottom;
    }

    @Override
    public boolean reachTop(Character c) {
        return c != top && c != bottom;
    }

    @Override
    public boolean reachBottom(Character c) {
        return true;
    }

    @Override
    public boolean prone(Character c) {
        return c == bottom;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public boolean inserted(Character c) {
        return c == top;
    }

    @Override
    public boolean facing(Character c, Character target) {
        return (c != bottom && c != top) || (target != bottom && target != top);
    }

    @Override
    public Position insertRandom(Combat c) {
        return new StandingOver(top, bottom);
    }

    @Override
    public Position reverse(Combat c) {
        if (bottom.human()) {
            c.write(bottom, Global.format(
                            "Summoning your remaining strength, you hold your arms up against the floor and use your hips to tip {other:name-do} off-balance with {other:possessive} dick still held inside of you. "
                                            + "{other:SUBJECT} lands on the floor with you on top of {other:direct-object} in a reverse cow-girl.",
                            bottom, top));
        } else {
            c.write(bottom, Global.format(
                            "{self:SUBJECT} suddenly pushes against the floor and knocks you to the ground with {self:possessive} hips. "
                                            + "You land on the floor with {self:direct-object} on top of you with in a reverse cow-girl position.",
                            bottom, top));
        }
        return new ReverseCowgirl(bottom, top);
    }

    @Override
    public double pheromoneMod(Character self) {
        if (self == bottom) {
            return 10;
        }
        return 2;
    }

    @Override
    public int dominance() {
        return 4;
    }
}
