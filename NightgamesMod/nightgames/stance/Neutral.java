package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Neutral extends Position {

    public Neutral(Character top, Character bottom) {
        super(top, bottom, Stance.neutral);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You and " + bottom.name() + " circle each other cautiously";
        } else {
            return String.format("%s and %s circle each other cautiously",
                            top.subject(), bottom.subject());
        }
    }

    @Override
    public String image() {
        return "neutral.jpg";
    }

    @Override
    public boolean inserted(Character c) {
        return false;
    }

    @Override
    public boolean mobile(Character c) {
        return true;
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return true;
    }

    @Override
    public boolean dom(Character c) {
        return false;
    }

    @Override
    public boolean sub(Character c) {
        return false;
    }

    @Override
    public boolean reachTop(Character c) {
        return true;
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
        return true;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return false;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public Position insertRandomDom(Combat c, Character dom) {
        Character other = getPartner(c, dom);
        boolean fuckPossible = dom.hasDick() && other.hasPussy();
        boolean reversePossible = other.hasDick() && dom.hasPussy();
        if (fuckPossible && reversePossible) {
            if (Global.random(2) == 0) {
                return new Standing(dom, other);
            } else {
                return new Jumped(dom, other);
            }
        } else if (fuckPossible) {
            return new Standing(dom, other);
        } else if (reversePossible) {
            return new Jumped(dom, other);
        } else {
            return this;
        }
    }

    @Override
    public Position insert(Combat c, Character pitcher, Character dom) {
        Character catcher = getPartner(c, pitcher);
        Character sub = getPartner(c, pitcher);
        if (pitcher.body.getRandomInsertable() == null || !catcher.hasPussy()) {
            // invalid
            return this;
        }
        if (pitcher == dom) {
            // guy is holding girl down, and is the dominant one in the new
            // stance
            return new Standing(pitcher, catcher);
        }
        if (pitcher == sub) {
            // guy is holding girl down, and is the submissive one in the new
            // stance
            return new Jumped(catcher, pitcher);
        }
        return this;
    }

    @Override
    public double pheromoneMod(Character self) {
        return .5;
    }
    
    @Override
    public int distance() {
        return 3;
    }
}
