package nightgames.stance;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Anal extends AnalSexStance {

    public Anal(Character top, Character bottom) {
        super(top, bottom, Stance.anal);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return String.format("%s behind %s and %s cock in buried in %s ass.",
                            top.subjectAction("are", "is"),
                            bottom.nameDirectObject(), top.possessivePronoun(),
                            bottom.possessivePronoun());
        } else if (top.has(Trait.strapped)) {
            return String.format("%s pegging %s with %s strapon dildo from behind.",
                           top.subjectAction("are", "is"), bottom.nameDirectObject(),
                           top.possessivePronoun());
        } else {
            return String.format("%s fucking %s in the ass from behind",
                            top.subjectAction("are", "is"), bottom.nameDirectObject());
        }
    }

    @Override
    public String image() {
        if (bottom.hasPussy()) {
            return "analf.jpg";
        } else {
            return "pegging.jpg";
        }
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
        return c != bottom;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c != bottom;
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
    public boolean inserted(Character c) {
        return c == top;
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Behind(top, bottom);
    }

    @Override
    public void checkOngoing(Combat c) {
        Character inserter = inserted(top) ? top : bottom;
        Character inserted = inserted(top) ? bottom : top;

        if (!inserter.hasInsertable()) {
            if (inserted.human()) {
                c.write("With " + inserter.name() + "'s pole gone, your ass gets a respite.");
            } else {
                c.write(inserted.name() + " sighs with relief with your phallus gone.");
            }
            c.setStance(insertRandom(c));
        }
        if (inserted.body.getRandom("ass") == null) {
            if (inserted.human()) {
                c.write("With your asshole suddenly disappearing, " + inserter.name()
                                + "'s dick pops out of what was once your sphincter.");
            } else {
                c.write("Your dick pops out of " + inserted.name() + " as her asshole shrinks and disappears.");
            }
            c.setStance(insertRandom(c));
        }
    }

    @Override
    public Position reverse(Combat c) {
        if (top.has(Trait.strapped)) {
            c.write(bottom, Global.format(
                            "As {other:subject-action:are|is} thrusting into {self:name-do} with {other:possessive} strapon, {self:subject-action:force|forces} {self:possesive} hips back and knock {other:direct-object} off balance. {self:SUBJECT-ACTION:quickly pull|quickly pulls} {other:possessive} fake cock out of {self:possessive} bottom while sitting on top of {other:direct-object}.",
                            bottom, top));
            return new ReverseMount(bottom, top);
        } else {
            c.write(bottom, Global.format(
                            "As {other:subject-action:are|is} thrusting into {self:name-do} with {other:possessive} {other:body-part:cock}, {self:subject-action:force|forces} {self:possesive} hips back and knock {other:direct-object} off balance. {self:SUBJECT-ACTION:quickly manuever|quickly manuevers} {self:reflective} on top of {other:direct-object}, now fucking {other:direct-object} back in an anal cowgirl position.",
                            bottom, top));
            return new AnalCowgirl(bottom, top);
        }
    }
    
    @Override
    public int dominance() {
        return 4;
    }
}
