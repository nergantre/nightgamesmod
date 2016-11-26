package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class Pin extends AbstractFacingStance {

    public Pin(Character top, Character bottom) {
        super(top, bottom, Stance.pin);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You're sitting on " + bottom.name() + ", holding her arms in place.";
        } else {
            return String.format("%s is pinning %s down, leaving %s helpless.",
                            top.subject(), bottom.nameDirectObject(), bottom.directObject());
        }
    }

    @Override
    public int pinDifficulty(Combat c, Character self) {
        return 10;
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        return new Behind(top, bottom).image();
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c != bottom;
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
        return c == top;
    }

    @Override
    public boolean prone(Character c) {
        return c == bottom;
    }

    @Override
    public boolean inserted(Character c) {
        return false;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return c != bottom && target == top;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return c != bottom && target == top;
    }

    @Override
    public boolean behind(Character c) {
        return c == top;
    }

    @Override
    public float priorityMod(Character self) {
        return getSubDomBonus(self, 2.0f);
    }

    @Override
    public double pheromoneMod(Character self) {
        return 1.5;
    }
    
    @Override
    public int dominance() {
        return 4;
    }
    
    @Override
    public int distance() {
        return 1;
    }
}
