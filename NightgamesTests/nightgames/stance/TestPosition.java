package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;

/**
 * Test position. Top character is dominant.
 */
public class TestPosition extends Position {
    public int dominance;

    public TestPosition(Character top, Character bottom, Stance stance, int dominance) {
        super(top, bottom, stance);
        this.dominance = dominance;
    }

    @Override public int dominance() {
        return dominance;
    }

    @Override public String describe(Combat c) {
        return null;
    }

    @Override public boolean mobile(Character c) {
        return false;
    }

    @Override public boolean kiss(Character c, Character target) {
        return false;
    }

    @Override public boolean dom(Character c) {
        return c.equals(top);
    }

    @Override public boolean sub(Character c) {
        return c.equals(bottom);
    }

    @Override public boolean reachTop(Character c) {
        return false;
    }

    @Override public boolean reachBottom(Character c) {
        return false;
    }

    @Override public boolean prone(Character c) {
        return false;
    }

    @Override public boolean feet(Character c, Character target) {
        return false;
    }

    @Override public boolean oral(Character c, Character target) {
        return false;
    }

    @Override public boolean behind(Character c) {
        return false;
    }

    @Override public boolean inserted(Character c) {
        return false;
    }

    @Override public String image() {
        return null;
    }

    @Override
    public int distance() {
        return 0;
    }
}
