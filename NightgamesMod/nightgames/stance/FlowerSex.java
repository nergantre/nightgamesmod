package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class FlowerSex extends FemdomSexStance {

    public FlowerSex(Character top, Character bottom) {
        super(top, bottom, Stance.flowertrap);
    }

    @Override
    public int pinDifficulty(Combat c, Character self) {
        return 12;
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You're coiled around " + bottom.nameOrPossessivePronoun()
                            + " body with his cock inside you and the petals of your flower wrapped around both of you like a cocoon.";
        } else {
            return String.format("%s trapped in a giant flower bulb surrounding %s and %s. "
                            + "Inside, %s on top of %s with %s cock trapped in %s pussy "
                            + "and %s face smothered in %s cleavage.", bottom.subjectAction("are", "is"),
                            bottom.human() ? "you" : bottom.reflectivePronoun(), top.subject(),
                            bottom.subjectAction("are", "is"), top.nameDirectObject(),
                            bottom.possessivePronoun(), top.possessivePronoun(),
                            bottom.possessivePronoun(), top.possessivePronoun());
        }
    }

    @Override
    public String image() {
        return "flower.png";
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
        return this;
    }

    @Override
    public double pheromoneMod(Character self) {
        return 10;
    }
    
    @Override
    public int dominance() {
        return 5;
    }
}
