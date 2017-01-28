package nightgames.stance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Stsflag;

public abstract class MaledomSexStance extends Position {
    public MaledomSexStance(Character top, Character bottom, Stance stance) {
        super(top, bottom, stance);
    }

    @Override
    public float priorityMod(Character self) {
        float priority = 0;
        priority += getSubDomBonus(self, 4.0f);
        if (self.hasPussy()) {
            priority += self.body.getRandomPussy().priority(self);
        }
        if (self.hasDick()) {
            priority += self.body.getRandomCock().priority(self);
        }
        return priority;
    }

    @Override
    public void checkOngoing(Combat c) {
        Character inserter = inserted(top) ? top : bottom;
        Character inserted = inserted(top) ? bottom : top;

        if (!inserter.hasInsertable()) {
            if (inserter.human()) {
                c.write("With your phallus gone, you groan in frustration and cease your merciless movements.");
            } else {
                c.write(inserter.getName() + " groans with frustration with the sudden disappearance of "
                                + inserter.possessiveAdjective() + " pole.");
            }
            c.setStance(insertRandom(c));
        }
        if (!inserted.hasPussy() && !anallyPenetratedBy(c, inserted, inserter)) {
            if (inserted.human()) {
                c.write("With your pussy suddenly disappearing, " + inserter.subject()
                                + " can't continue fucking you anymore.");
            } else {
                c.write("You groan with frustration with the sudden disappearance of "
                                + inserted.nameOrPossessivePronoun() + " pussy.");
            }
            c.setStance(insertRandom(c));
        }
    }

    @Override
    public boolean oral(Character c, Character target) {
        return false;
    }

    @Override
    public boolean inserted(Character c) {
        return c == top;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return false;
    }

    @Override
    public List<BodyPart> topParts(Combat c) {
        return Arrays.asList(domSexCharacter(c).body.getRandomInsertable()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public List<BodyPart> bottomParts() {
        return Arrays.asList(bottom.body.getRandomPussy()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public double pheromoneMod(Character self) {
        return 2;
    }
    
    @Override
    public int distance() {
        return 1;
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        Character opponent = getPartner(c, struggler);
        boolean knotted = opponent.is(Stsflag.knotted);

        int selfM = Global.random(6, 11);
        int targM = Global.random(6, 11);
        if (knotted) {
            c.write(struggler,
                            Global.format("{self:SUBJECT-ACTION:struggle} fruitlessly against the lump of {other:name-possessive} knotted cock, "
                                            + "arousing the hell out of both of %s in the process.",
                            struggler, opponent, c.bothDirectObject(opponent)));
            selfM += 5;
        } else {
            c.write(struggler, Global.format("{self:SUBJECT-ACTION:try} to tip {other:name-do} off balance, but {other:pronoun-action:grip} {other:possessive} hips firmly, "
                            + "pushing {other:possessive} cock deep inside {self:direct-object} and pinning {self:direct-object} to the floor. "
                            + "The sensations from wrestling with {other:possessive} cock buried inside {self:direct-object} almost make {self:direct-object} cum.", struggler, opponent));
        }

        struggler.body.pleasure(opponent, opponent.body.getRandomInsertable(), struggler.body.getRandomPussy(), selfM, c);
        if (!opponent.has(Trait.strapped)) {
            opponent.body.pleasure(struggler, struggler.body.getRandomPussy(), opponent.body.getRandomCock(), targM, c);            
        }
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        Character opponent = getPartner(c, escapee);
        boolean knotted = opponent.is(Stsflag.knotted);

        int selfM = Global.random(6, 11);
        int targM = Global.random(6, 11);
        if (knotted) {
            c.write(escapee,
                            Global.format("{self:SUBJECT-ACTION:tickle} {other:name-do} and {self:action:try} to escape with {other:direct-object} distracted. "
                                            + "Problem is, the knot in {other:possessive} {other:body-part:cock} makes trying to pull out a arousing yet futile task.",
                            escapee, opponent));
            selfM += 5;
        } else {
            c.write(escapee, Global.format("{self:SUBJECT-ACTION:try} to escape {other:name-possessive} relentless pounding, "
                            + "but {other:pronoun-action:grip} {other:possessive} hips firmly, pushing {other:possessive} cock deep inside {self:direct-object} once again "
                            + "and pinning {self:direct-object} to the floor. "
                            + "The sensations from moving around so much with {other:possessive} cock buried inside {self:direct-object} almost make {self:direct-object} cum.", escapee, opponent));
        }

        escapee.body.pleasure(opponent, opponent.body.getRandomInsertable(), escapee.body.getRandomPussy(), selfM, c);
        if (!opponent.has(Trait.strapped)) {
            opponent.body.pleasure(escapee, escapee.body.getRandomPussy(), opponent.body.getRandomCock(), targM, c);            
        }
        super.escape(c, escapee);
    }
}
