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
    public String describe() {
        if (top.human()) {
            return "You're coiled around " + bottom.nameOrPossessivePronoun()
                            + " body with his cock inside you and the petals of your flower wrapped around both of you like a cocoon.";
        } else {
            return "You're trapped in a giant flower bulb surrounding you and " + top.name()
                            + ". Inside, you're on top of " + top.nameDirectObject()
                            + " with your cock trapped in her pussy and your face smothered in her cleavage.";
        }
    }

    @Override
    public String image() {
        return "flower.png";
    }

    @Override
    public boolean mobile(Character c) {
        return c == top;
    }

    @Override
    public boolean kiss(Character c) {
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
        return c == top;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c == top;
    }

    @Override
    public boolean prone(Character c) {
        return c == bottom;
    }

    @Override
    public boolean feet(Character c) {
        return false;
    }

    @Override
    public boolean oral(Character c) {
        return false;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public Position insertRandom() {
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
}
