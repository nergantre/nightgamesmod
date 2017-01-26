package nightgames.stance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class HeldOral extends AbstractFacingStance {
    public HeldOral(Character top, Character bottom) {
        super(top, bottom, Stance.oralpin);
    }

    @Override
    public String describe(Combat c) {
        return Global.format(
                        "{self:SUBJECT-ACTION:are|is} holding {other:name-do} down with {self:possessive} face nested between {other:possessive} legs.",
                        top, bottom);
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom && c != top;
    }

    @Override
    public boolean getUp(Character c) {
        return c == top;
    }
    public List<BodyPart> topParts(Combat c) {
        BodyPart part = top.body.getRandom("mouth");
        if (part != null) {
            return Collections.singletonList(part);
        } else {
            return Collections.emptyList();
        }
    }

    public List<BodyPart> bottomParts() {
        if (bottom.hasDick()) {
            return Collections.singletonList(bottom.body.getRandom("cock"));
        } else if (bottom.hasPussy()){
            return Collections.singletonList(bottom.body.getRandomPussy());
        }
        return Collections.emptyList();
    }

    @Override
    public String image() {
        if (bottom.hasDick()) {
            return "oralhold_fm.jpg";
        } else if (bottom.hasPussy() && top.hasPussy()) {
            return "oralhold_ff.jpg";
        } else if (bottom.hasPussy()) {
            return "oralhold_mf.jpg";
        }
        return "err.jpg";
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
    public boolean inserted(Character c) {
        return c == bottom;
    }

    @Override
    public float priorityMod(Character self) {
        float bonus = getSubDomBonus(self, 2);
        bonus += self.body.getRandom("mouth").priority(self);
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

    private void pleasureRandomCombination(Combat c, Character self, Character opponent, String pussyString, String cockString) {
        int targM = Global.random(6, 11);
        List<Runnable> possibleActions = new ArrayList<>();
        if (self.hasPussy()) {
            possibleActions.add(() -> {
                c.write(self, Global.format(pussyString, self, opponent));
                self.body.pleasure(opponent, opponent.body.getRandom("mouth"), self.body.getRandomPussy(), targM, c);
            });
        }
        if (self.hasDick()) {
            possibleActions.add(() -> {
                c.write(self, Global.format(cockString, self, opponent));
                self.body.pleasure(opponent, opponent.body.getRandom("mouth"), self.body.getRandomCock(), targM, c);
            });
        }
        Optional<Runnable> action = Global.pickRandom(possibleActions);
        if (action.isPresent()) {
            action.get().run();
        }
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        Character opponent = getPartner(c, struggler);
        pleasureRandomCombination(c, struggler, opponent,
                        "{self:SUBJECT-ACTION:try} to peel {other:name-do} off {self:possessive} legs, "
                        + "but {other:pronoun-action:hold} on tightly. "
                      + "After thoroughly exhausting {self:possessive} attempts, {other:pronoun-action:smile} smugly "
                      + "and {other:action:give} {self:possessive} clit "
                      + "a victorious little lick.", 
    
                        "{self:SUBJECT-ACTION:try} to peel {other:name-do} off {self:possessive} legs,"
                        + " but {other:pronoun-action:hold} on tightly. "
                      + "After thoroughly exhausting {self:possessive} attempts, {other:pronoun-action:smile} smugly"
                      + " and {other:action:run} {other:possessive} tongue "
                      + "along {self:possessive} shaft to demostrate {other:possessive} victory.");
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        Character opponent = getPartner(c, escapee);
        pleasureRandomCombination(c, escapee, opponent,
                        "{self:SUBJECT-ACTION:try} to escape {other:name-possessive} grip on {self:possessive} waist,"
                        + " but {other:pronoun-action:hold} on tightly. "
                      + "After thoroughly exhausting every angle, {self:pronoun} can only give up in defeat. "
                      + "{other:PRONOUN-ACTION:smile} smugly and {other:action:give} {self:possessive} clit "
                      + "a victorious little lick.", 
    
                        "{self:SUBJECT-ACTION:try} to peel {other:name-possessive} grip on {self:possessive} waist,"
                        + " but {other:pronoun-action:hold} on tightly. "
                      + "After thoroughly exhausting every angle, {self:pronoun} can only give up in defeat. "
                      + "{other:PRONOUN-ACTION:smile} smugly and {other:action:run} {other:possessive} tongue "
                      + "along {self:possessive} shaft to demostrate {other:possessive} victory.");
        super.escape(c, escapee);
    }
}
