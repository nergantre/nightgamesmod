package nightgames.stance;

import nightgames.characters.Character;
import nightgames.characters.body.BreastsPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class SuccubusEmbrace extends FemdomSexStance {

    public SuccubusEmbrace(Character top, Character bottom) {
        super(top, bottom, Stance.succubusembrace);
    }

    private boolean hasBreasts(Character c) {
        return c.body.getLargestBreasts() != BreastsPart.flat;
    }
    
    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "TODO?";
        }
        String breastDesc;
        if (hasBreasts(top) && hasBreasts(bottom)) {
            breastDesc = "your {other:body-part:breasts} against {self:possessive} own pair";
        } else if (hasBreasts(top)) {
            breastDesc = "your chest against {self:possessive} soft tits";
        } else if (hasBreasts(bottom)) {
            breastDesc = "your sensitive nipples against {self:possessive} hard chest";
        } else {
            breastDesc = "you tightly against {self:direct-object}";
        }
        return Global.format("{self:name} is sitting on top of you, with your"
                        + " {other:body-part:cock} nestled deep within {self:possessive}"
                        + " {self:body-part:pussy}. {self:POSSESSIVE} {self:body-part:wings}"
                        + " are wrapped around your back, pressing %s.", top, bottom, breastDesc);
    }

    @Override
    public int dominance() {
        return 4;
    }
    
    @Override
    public Position reverse(Combat c, boolean writeMessage) {
        if (writeMessage) {
            c.write(bottom, Global.format(
                            "{self:SUBJECT-ACTION:pinch|pinches} {other:possessive} clitoris with {self:possessive} hands as {other:subject-action:try|tries} to ride {self:direct-object}. "
                                            + "While {other:subject-action:yelp|yelps} with surprise, {self:subject-action:take|takes} the chance to swing around into a dominant missionary position.",
                            bottom, top));
        }
        return new CoiledSex(top, bottom);
    }
    
    @Override
    public boolean mobile(Character c) {
        return c == top;
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
        return true;
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
        return false;
    }


    @Override
    public String image() {
        return "succubus_embrace.jpg";
    }

}
