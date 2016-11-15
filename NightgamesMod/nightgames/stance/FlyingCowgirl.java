package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.damage.DamageType;

public class FlyingCowgirl extends FemdomSexStance {

    private Character top, bottom;

    public FlyingCowgirl(Character succ, Character target) {
        super(succ, target, Stance.flying);
        top = succ;
        bottom = target;
    }

    @Override
    public String describe(Combat c) {
        return String.format(
                        "%s are flying some twenty feet up in the air,"
                                        + " joined to %s by %s hips. %s on top of %s and %s %s is strangling %s %s.",
                                        spectated() ? String.format("%s and %s", top.subject(), bottom.subject()) : "You",
                                                        spectated() ? "eachother" : "your partner",
                                                        spectated() ? "their" : "your",
                        top.subjectAction("are", "is"), bottom.subject(), top.possessivePronoun(),
                        top.body.getRandomPussy().describe(top), bottom.possessivePronoun(),
                        bottom.body.getRandomInsertable().describe(bottom));
    }
    
    private boolean spectated() {
        return !(top.human() || bottom.human());
    }

    @Override
    public String image() {
        return "flying.jpg";
    }

    @Override
    public boolean mobile(Character c) {
        return top == c;
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return (c == top || c == bottom) && (target == top || target == bottom);
    }

    @Override
    public boolean dom(Character c) {
        return top == c;
    }

    @Override
    public boolean sub(Character c) {
        return top == c;
    }

    @Override
    public boolean reachTop(Character c) {
        return c == top || c == bottom;
    }

    @Override
    public boolean reachBottom(Character c) {
        return top == c;
    }

    @Override
    public boolean prone(Character c) {
        return bottom == c;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    public boolean flying(Character c) {
        return true;
    }

    @Override
    public void decay(Combat c) {
        time++;
        top.weaken(c, (int) bottom.modifyDamage(DamageType.stance, top, 3));
    }

    @Override
    public void checkOngoing(Combat c) {
        if (top.getStamina().get() < 5) {
            if (top.human()) {
                c.write("You're too tired to stay in the air. You plummet to the ground and " + bottom.name()
                                + " drops on you heavily, knocking the wind out of you.");
            } else {
                c.write(top.name()
                                + " falls to the ground and so do you. Fortunately, her body cushions your fall, but you're not sure she appreciates that as much as you do.");
            }
            top.pain(c, bottom, (int) bottom.modifyDamage(DamageType.physical, top, Global.random(50, 75)));
            c.setStance(new Mount(bottom, top));
        } else {
            super.checkOngoing(c);
        }
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Mount(top, bottom);
    }

    @Override
    public Position reverse(Combat c) {
        if (bottom.body.getRandomWings() != null) {
            c.write(bottom, Global.format(
                            "In a desperate gamble for dominance, {self:subject-action:piston|pistons} wildly into {other:name-do}, making {other:direct-object} yelp and breaking {other:possessive} concentration. Shaking off {other:possessive} limbs coiled around {self:subject}, {self:subject-action:start|starts} flying on {self:possessive} own and starts fucking {other:direct-object} back in the air.",
                            bottom, top));
            return new FlyingCarry(bottom, top);
        } else {
            c.write("Weakened by {self:possessive} squirming, {other:SUBJECT-ACTION:fall|falls} to the ground and so {self:action:do|does} {self:name-do}. Fortunately, {other:possessive} body cushions {self:possessive} fall, but you're not sure {self:action:if she appreciates that as much as you do|if you appreciate that as much as she does}. "
                            + "While {other:subject-action:are|is} dazed, {self:subject-action:mount|mounts} {other:direct-object} and {self:action:start|starts} fucking {other:direct-object} in a missionary position.");
            top.pain(c, bottom, (int) bottom.modifyDamage(DamageType.physical, top, Global.random(50, 75)));
            return new Missionary(bottom, top);
        }
    }
    
    @Override
    public int dominance() {
        return 6;
    }
}
