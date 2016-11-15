package nightgames.stance;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Doggy extends MaledomSexStance {

    public Doggy(Character top, Character bottom) {
        super(top, bottom, Stance.doggy);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return bottom.name() + " is on her hands and knees in front of you, while you fuck her Doggy style.";
        } else {
            return String.format("Things aren't going well for %s. %s %s down on %s hands and knees, while %s"
                            + " is fucking %s from behind.", bottom.subject(),
                            Global.capitalizeFirstLetter(bottom.pronoun()), bottom.action("are", "is"),
                            bottom.possessivePronoun(), top.subject(), bottom.directObject());
        }
    }

    @Override
    public String image() {
        if (top.has(Trait.strapped)) {
            return "doggy_ff_strapped.jpg";
        }
        return "doggy.jpg";
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
        return c == top;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c == top;
    }

    @Override
    public boolean prone(Character c) {
        return false;
    }

    @Override
    public boolean behind(Character c) {
        return c == top;
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Behind(top, bottom);
    }

    @Override
    public Position reverse(Combat c) {
        c.write(bottom, Global.format(
                        "{self:SUBJECT-ACTION:manage|manages} to reach between {self:possessive} legs and grab hold of {other:possessive} "
                                        + (top.hasBalls() ? "ballsack" : "cock")
                                        + ", stopping {other:direct-object} in mid thrust. {self:SUBJECT-ACTION:smirk|smirks} at {other:direct-object} over {self:possessive} shoulder "
                                        + "and pushes {self:possessive} butt against {other:direct-object}, using the leverage of "
                                        + "{other:possessive} " + (top.hasBalls() ? "testicles" : "cock")
                                        + " to keep {other:direct-object} from backing away to maintain {self:possessive} balance. {self:SUBJECT-ACTION:force|forces} {other:direct-object} onto {other:possessive} back, while never breaking {other:possessive} connection. After "
                                        + "some complex maneuvering, {other:subject-action:end|ends} up on the floor while {self:subject-action:straddle|straddles} {other:possessive} hips in a reverse cowgirl position.",
                        bottom, top));
        return new ReverseCowgirl(bottom, top);
    }
    
    @Override
    public int dominance() {
        return 3;
    }
}
