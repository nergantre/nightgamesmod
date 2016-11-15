package nightgames.stance;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.damage.DamageType;

public class Jumped extends FemdomSexStance {
    public Jumped(Character top, Character bottom) {
        super(top, bottom, Stance.standing);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You are clinging to " + bottom.nameOrPossessivePronoun()
                            + " arms while her dick is buried deep in your pussy";
        } else {
            return String.format("%s clinging to %s shoulders and gripping %s waist "
                            + "with %s thighs while %s uses the leverage to ride %s.",
                            top.subjectAction("are", "is"), bottom.nameOrPossessivePronoun(),
                            bottom.possessivePronoun(), top.possessivePronoun(),
                            top.pronoun(), bottom.directObject());
        }
    }

    @Override
    public String image() {
        return "standing.jpg";
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom && c != top;
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
        return c != bottom && c != top;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c != bottom && c != top;
    }

    @Override
    public boolean prone(Character c) {
        return false;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Neutral(top, bottom);
    }

    @Override
    public void decay(Combat c) {
        time++;
        top.weaken(c, (int) bottom.modifyDamage(DamageType.stance, top, 2));
    }

    @Override
    public void checkOngoing(Combat c) {
        if (bottom.getStamina().get() < 2 && !top.has(Trait.petite)) {
            if (bottom.human()) {
                c.write("Your legs give out and you fall on the floor. " + top.name() + " lands heavily on your lap.");
                c.setStance(new Cowgirl(top, bottom));
            } else {
                c.write(bottom.name() + " loses her balance and falls, pulling you down on top of her.");
                c.setStance(new Cowgirl(top, bottom));
            }
        } else {
            super.checkOngoing(c);
        }
    }

    @Override
    public Position reverse(Combat c) {
        c.write(bottom, Global.format(
                        "{self:SUBJECT-ACTION:pinch|pinches} {other:possessive} clitoris with {self:possessive} hands as {other:subject-action:try|tries} to ride {self:direct-object}. "
                                        + "While {other:subject-action:yelp|yelps} with surprise, {self:subject-action:take|takes} the chance to push {other:direct-object} against a wall and fuck her in a standing position.",
                        bottom, top));
        return new Standing(bottom, top);
    }
    
    @Override
    public int dominance() {
        return 3;
    }
}
