package nightgames.characters.body;

import nightgames.characters.Character;
import nightgames.global.Global;

public class FacePart extends GenericBodyPart {
    private double femininity;

    /* femininity goes from [-5, 5] */
    public FacePart(double hotness, double femininity) {
        super("", hotness, 0, 0, "face", "a ");
        this.femininity = femininity;
    }

    @Override
    public void describeLong(StringBuilder b, Character c) {
        String desc;
        if (femininity < -3) {
            desc = "{self:PRONOUN-ACTION:have|has} a rugged face that speaks of {self:possessive} masculinity.";
        } else if (femininity < -1) {
            desc = "{self:PRONOUN-ACTION:have|has} a manly face.";
        } else if (femininity < .5) {
            desc = "{self:PRONOUN-ACTION:have|has} an androgynous face that makes one question {self:possessive} sex.";
        } else if (femininity < 1.5) {
            desc = "{self:PRONOUN-ACTION:have|has} an almost boyish face full of expression.";
        } else if (femininity < 2.5) {
            desc = "{self:PRONOUN-ACTION:have|has} a soft feminine face.";
        } else if (femininity < 4) {
            desc = "{self:PRONOUN-ACTION:have|has} a alluring womanly face.";
        } else {
            desc = "{self:PRONOUN-ACTION:have|has} a beautiful face at the height of femininity.";
        }
        String parsedDesc = Global.format(desc, c, c);
        b.append(parsedDesc);
    }

    @Override
    public double getFemininity(Character c) {
        return femininity;
    }

    @Override
    public String describe(Character c) {
        return "face";
    }

    @Override
    public String fullDescribe(Character c) {
        return "face";
    }
}
