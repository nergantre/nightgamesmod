package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class ReverseMount extends AbstractBehindStance {
    public ReverseMount(Character top, Character bottom) {
        super(top, bottom, Stance.reversemount);

    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You are straddling " + bottom.name() + ", with your back to her.";
        } else {
            return String.format("%s is sitting on %s chest, facing %s groin.",
                            top.subject(), bottom.nameOrPossessivePronoun(), bottom.possessivePronoun());
        }
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        if (bottom.hasPussy()) {
            return "mount_m.jpg";
        } else {
            return "mount_f.jpg";
        }
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
    public boolean feet(Character c, Character target) {
        return target == bottom;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return target == bottom;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public boolean inserted(Character c) {
        return false;
    }

    @Override
    public float priorityMod(Character self) {
        return getSubDomBonus(self, 4.0f);
    }

    @Override
    public double pheromoneMod(Character self) {
        return 2;
    }
    
    @Override
    public int dominance() {
        return 2;
    }
    
    @Override
    public int distance() {
        return 1;
    }
}
