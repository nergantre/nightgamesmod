package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class CoiledSex extends FemdomSexStance {

    public CoiledSex(Character top, Character bottom) {
        super(top, bottom, Stance.coiled);
    }

    @Override
    public int pinDifficulty(Combat c, Character self) {
        return 8;
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "Your limbs are coiled around " + bottom.nameOrPossessivePronoun() + " body and "
                            + bottom.possessivePronoun() + " cock is inside you.";
        } else {
            return String.format("%s on top of %s with %s cock trapped in %s pussy and %s face smothered in %s cleavage.",
                            bottom.subjectAction("are", "is"), top.nameDirectObject(),
                            bottom.possessivePronoun(), top.possessivePronoun(),
                            bottom.possessivePronoun(), top.possessivePronoun());
        }
    }

    @Override
    public String image() {
        return "coiledsex.png";
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return true;
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
        return c != bottom;
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
    public Position insertRandom(Combat c) {
        return new Mount(top, bottom);
    }

    @Override
    public Position reverse(Combat c) {
        c.write(bottom, Global.format(
                        "In a desperate gamble for dominance, {self:subject} piston wildly into {other:name-do}, making {other:direct-object} yelp and breaking {other:possessive} concentration. Shaking off {other:possessive} limbs coiled around {self:subject}, {self:subject} grab ahold of {other:possessive} legs and swing into a missionary position.",
                        bottom, top));
        return new Missionary(bottom, top);
    }

    @Override
    public double pheromoneMod(Character self) {
        return 10;
    }
    
    @Override
    public int dominance() {
        return 4;
    }
}
