package nightgames.stance;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Cowgirl extends FemdomSexStance {

    public Cowgirl(Character top, Character bottom) {
        super(top, bottom, Stance.cowgirl);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You're on top of " + bottom.name() + ".";
        } else {
            return String.format("%s is riding %s in Cowgirl position. %s breasts bounce in front of %s"
                            + " face each time %s moves %s hips.", top.subject(), bottom.nameDirectObject(),
                            Global.capitalizeFirstLetter(top.possessivePronoun()), bottom.possessivePronoun(),
                            top.pronoun(), top.possessivePronoun());
        }
    }

    @Override
    public String image() {
        return "cowgirl.jpg";
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
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
        return true;
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
                        "{self:SUBJECT-ACTION:pinch|pinches} {other:possessive} clitoris with {self:possessive} hands as {other:subject-action:try|tries} to ride {self:direct-object}. "
                                        + "While {other:subject-action:yelp|yelps} with surprise, {self:subject-action:take|takes} the chance to swing around into a dominant missionary position.",
                        bottom, top));
        return new Missionary(bottom, top);
    }

    public static Position similarInstance(Character top, Character bottom) {
        if (top.get(Attribute.Power) > 25 && Global.random(2) == 0) {
            return new UpsideDownFemdom(top, bottom);
        }
        return new Cowgirl(top, bottom);
    }
    
    @Override
    public int dominance() {
        return 3;
    }
}
