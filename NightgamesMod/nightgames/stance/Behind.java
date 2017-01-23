package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Behind extends AbstractBehindStance {
    public Behind(Character top, Character bottom) {
        super(top, bottom, Stance.behind);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You are holding " + bottom.getName() + " from behind.";
        } else {
            return String.format("%s is holding %s from behind.", top.subject(), bottom.nameDirectObject());
        }
    }

    @Override
    public String image() {
        if (top.hasPussy() && bottom.hasPussy()) {
            return "behind_ff.jpg";
        } else if (bottom.hasPussy()) {
            return "behind_m.jpg";
        } else {
            return "behind_f.jpg";
        }
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
        return c != bottom;
    }

    @Override
    public boolean reachBottom(Character c) {
        return true;
    }

    @Override
    public boolean prone(Character c) {
        return false;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return c != top && c != bottom && target == top;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return c != top && c != bottom && target == top;
    }

    @Override
    public boolean behind(Character c) {
        return c == top;
    }

    @Override
    public boolean inserted(Character c) {
        return false;
    }

    @Override
    public float priorityMod(Character self) {
        return (self.hasInsertable() ? 2 : 1) * getSubDomBonus(self, 1.0f);
    }

    @Override
    public double pheromoneMod(Character self) {
        return 1.5;
    }

    @Override
    public int dominance() {
        return 3;
    }

    @Override
    public int distance() {
        return 1;
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        c.write(struggler, String.format("%s to gain a more dominant position, but with"
                        + " %s behind %s holding %s waist firmly, there is nothing %s can do.",
                        struggler.subjectAction("struggle"), top.subject(), struggler.directObject(),
                        struggler.possessiveAdjective(), struggler.pronoun()));
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        c.write(escapee, Global.format("{self:SUBJECT-ACTION:try} to escape {other:name-possessive} hold, but with"
                        + " {other:direct-object} behind {self:direct-object} holding {self:possessive} waist firmly, there is nothing {self:pronoun} can do.",
                        escapee, top));
        super.escape(c, escapee);
    }
}
