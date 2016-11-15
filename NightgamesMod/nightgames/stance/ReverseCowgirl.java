package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class ReverseCowgirl extends FemdomSexStance {

    public ReverseCowgirl(Character top, Character bottom) {
        super(top, bottom, Stance.reversecowgirl);

    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "";
        } else {
            return String.format("%s is riding %s in Reverse Cowgirl position, facing %s feet.",
                            top.subject(), bottom.nameDirectObject(), bottom.directObject());
        }
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        return "reverse_cowgirl.jpg";
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return false;
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
        return c == bottom;
    }

    @Override
    public Position insertRandom(Combat c) {
        return new ReverseMount(top, bottom);
    }

    @Override
    public Position reverse(Combat c) {
        c.write(bottom, Global
                        .format("{self:SUBJECT-ACTION:manage|manages} to unbalance {other:name-do} and push {other:direct-object} forward onto {other:possessive} hands and knees. {self:SUBJECT-ACTION:follow|follows} {other:direct-object}, still inside {other:possessive} tight wetness, and continue "
                                        + "to fuck {other:direct-object} from behind.", bottom, top));
        return new Doggy(bottom, top);
    }
    
    @Override
    public int dominance() {
        return 3;
    }
}
