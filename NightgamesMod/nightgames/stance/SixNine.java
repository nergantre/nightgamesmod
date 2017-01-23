package nightgames.stance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class SixNine extends AbstractBehindStance {
    public SixNine(Character top, Character bottom) {
        super(top, bottom, Stance.sixnine);
    }

    @Override
    public float priorityMod(Character self) {
        float priority = 0;
        priority += self.body.getRandom("mouth").priority(self) * 2;
        return priority;
    }

    @Override
    public String describe(Combat c) {
        String topParts = describeParts(top);
        String bottomParts = describeParts(bottom);
        if (top.human()) {
            return String.format("You are on top of %s in the 69 position. %s %s is right in front of your face "
                            + "and you can feel %s breath on your %s.", bottom.nameDirectObject(),
                            Global.capitalizeFirstLetter(bottom.possessiveAdjective()), bottomParts,
                            bottom.possessiveAdjective(), topParts);
        } else {
            return String.format("%s and %s are on the floor in 69 position. "
                            + "%s sitting on top of %s with %s %s right in "
                            + "front of %s face and %s %s in %s mouth.", bottom.subject(),
                            top.subject(), top.subjectAction("are", "is"), bottom.nameDirectObject(),
                            top.possessiveAdjective(), topParts, bottom.possessiveAdjective(),
                            bottom.possessiveAdjective(), bottomParts, top.possessiveAdjective());
        }
    }
    
    private String describeParts(Character c) {
        List<BodyPart> parts = parts(c);
        if (parts.size() == 1)
            return parts.get(0).describe(c);
        return String.format("%s and %s", parts.get(0).describe(c), parts.get(1).describe(c));
    }

    @Override
    public List<BodyPart> topParts(Combat c) {
        return Collections.emptyList();
    }

    @Override
    public List<BodyPart> bottomParts() {
        return Collections.emptyList();
    }

    private List<BodyPart> parts(Character c) {
        List<BodyPart> parts = new ArrayList<>(2);
        if (c.hasDick())
            parts.add(c.body.getRandomCock());
        if (c.hasPussy())
            parts.add(c.body.getRandomPussy());
        if (parts.isEmpty())
            parts.add(c.body.getRandomAss());
        return parts;
    }
    
    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return false;
    }

    @Override
    public String image() {
        if (bottom.hasDick() || top.hasDick()) {
            return "69.jpg";
        } else {
            return "les69.jpg";
        }
    }

    @Override
    public boolean facing(Character c, Character target) {
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
        return false;
    }

    @Override
    public boolean reachBottom(Character c) {
        return true;
    }

    @Override
    public boolean prone(Character c) {
        return c == top || c == bottom;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return false;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return c == top || c == bottom;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public boolean inserted(Character c) {
        return false;
    }

    @Override
    public Position insertRandom(Combat c) {
        return this;
    }

    @Override
    public boolean faceAvailable(Character target) {
        return false;
    }

    @Override
    public double pheromoneMod(Character self) {
        return 10;
    }
    
    @Override
    public int dominance() {
        return 1;
    }
    
    @Override
    public int distance() {
        return 1;
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        c.write(struggler, String.format("%s to gain a more dominant position, but with"
                        + " %s on top of %s sitting on %s chest, there is nothing %s can do.",
                        struggler.subjectAction("struggle"), top.subject(), struggler.directObject(),
                        struggler.possessiveAdjective(), struggler.pronoun()));
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        c.write(escapee, Global.format("{self:SUBJECT-ACTION:try} to escape {other:name-possessive} hold, but with"
                        + " {other:direct-object} sitting firmly on {self:possessive} chest, there is nothing {self:pronoun} can do.",
                        escapee, top));
        super.escape(c, escapee);
    }
}
