package nightgames.stance;

import java.util.Collections;
import java.util.List;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class HeldPaizuri extends AbstractFacingStance {
    public HeldPaizuri(Character top, Character bottom) {
        super(top, bottom, Stance.paizuripin);
    }

    @Override
    public String describe(Combat c) {
        return Global.format(
                        "{self:SUBJECT-ACTION:are|is} holding {other:name-do} down with {self:possessive} breasts nested around {other:possessive} cock.",
                        top, bottom);
    }

    @Override
    public boolean inserted(Character c) {
        return c == bottom;
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom && c != top;
    }

    @Override
    public boolean getUp(Character c) {
        return c == top;
    }

    @Override
    public String image() {        
        return "paizuri_pin.png";       
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c != top && target != top;
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
        return false;
    }

    @Override
    public boolean reachBottom(Character c) {
        return false;
    }

    @Override
    public boolean prone(Character c) {
        return c == bottom;
    }

    public List<BodyPart> topParts(Combat c) {
        BodyPart part = top.body.getRandom("breasts");
        if (part != null) {
            return Collections.singletonList(part);
        } else {
            return Collections.emptyList();
        }
    }

    public List<BodyPart> bottomParts() {
        if (bottom.hasDick()) {
            return Collections.singletonList(bottom.body.getRandom("cock"));
        }
        return Collections.emptyList();
    }

    @Override
    public boolean feet(Character c, Character target) {
        return false;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return c == top;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public float priorityMod(Character self) {
        float bonus = getSubDomBonus(self, 2);
        bonus += self.body.getRandom("breasts").priority(self);
        return bonus;
    }

    @Override
    public Position reverse(Combat c, boolean writeMessage) {
        if (writeMessage) {
            
        }
        return new Mount(bottom, top);
    }

    @Override
    public boolean faceAvailable(Character target) {
        return target == bottom;
    }

    @Override
    public double pheromoneMod(Character self) {
        if (self == bottom) {
            return 10;
        }
        return 2;
    }
    
    @Override
    public int dominance() {
        return 3;
    }

    @Override
    public int distance() {
        return 1;
    }

    private void pleasureStruggle(Combat c, Character self, Character opponent, String cockString) {
        int targM = Global.random(6, 11);
        c.write(self, Global.format(cockString, self, opponent));
        self.body.pleasure(opponent, opponent.body.getRandomBreasts(), self.body.getRandomCock(), targM, c);
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        Character opponent = getPartner(c, struggler);
        pleasureStruggle(c, struggler, opponent,
                        "{self:SUBJECT-ACTION:try} to remove {self:possessive} cock from between {other:name-possessive} impressive cleavage, but {other:pronoun-action:have} other ideas. "
                      + "Using {other:possessive} hands to press her soft breasts together, the impish {other:girl} follows {self:possessive} attempts to escape and {other:action:manage} "
                      + "to titfuck {self:direct-object} even as {self:pronoun-action:struggle}.");
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        Character opponent = getPartner(c, escapee);
        pleasureStruggle(c, escapee, opponent,
                        "{self:SUBJECT-ACTION:try} to sneak out of {other:name-possessive} breast-press, but {other:pronoun-action:have} other ideas. "
                      + "The well-endowed {other:girl} presses {other:possessive} chest against {self:possessive} crotch and slides it back and forth around {self:possessive} shaft. "
                      + "Not only does it cut off {self:possessive} escape, but it also has the beneficial consequence of arousing the hell out of {self:direct-object}.");
        super.escape(c, escapee);
    }
}
