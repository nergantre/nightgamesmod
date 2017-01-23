package nightgames.stance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.CockBound;
import nightgames.status.Stsflag;

public abstract class FemdomSexStance extends Position {
    public FemdomSexStance(Character top, Character bottom, Stance stance) {
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
        Character inserter = inserted(domSexCharacter(c)) ? domSexCharacter(c) : bottom;
        Character inserted = inserted(domSexCharacter(c)) ? bottom : domSexCharacter(c);

        if (!inserter.hasInsertable()) {
            if (inserter.human()) {
                c.write(inserted.getName() + " groans with frustration with the sudden disappearance of your pole.");
            } else {
                c.write("With " + inserter.nameOrPossessivePronoun()
                                + " phallus gone, you groan in frustration and cease your merciless riding.");
            }
            c.setStance(insertRandom(c));
        }
        if (!inserted.hasPussy()) {
            if (inserted.human()) {
                c.write("With your pussy suddenly disappearing, you can't continue riding " + inserter.getName()
                                + " anymore.");
            } else {
                c.write(inserted.getName() + " groans with frustration with the sudden disappearance of her pussy.");
            }
            c.setStance(insertRandom(c));
        }
    }

    @Override
    public List<BodyPart> topParts(Combat c) {
        return Arrays.asList(domSexCharacter(c).body.getRandomPussy()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public List<BodyPart> bottomParts() {
        return Arrays.asList(bottom.body.getRandomInsertable()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public boolean inserted(Character c) {
        return c == bottom;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return false;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return false;
    }

    @Override
    public double pheromoneMod(Character self) {
        return 3;
    }
    @Override
    public int distance() {
        return 1;
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        Character opponent = getPartner(c, struggler);
        boolean cockbound = opponent.is(Stsflag.cockbound);

        int selfM = Global.random(6, 11);
        int targM = Global.random(6, 11);
        if (cockbound) {
            CockBound s = (CockBound) struggler.getStatus(Stsflag.cockbound);
            c.write(struggler,
                            Global.format("{self:SUBJECT-ACTION:try|tries} to struggle out of {other:possessive} iron grip on {self:possessive} dick. However, {other:possessive} "
                                            + s.binding
                                            + " has other ideas. {other:SUBJECT-ACTION:run|runs} {other:possessive} "
                                            + s.binding
                                            + " up and down {self:possessive} cock and leaves {self:direct-object} gasping with pleasure.",
                            struggler, opponent));
            selfM += 5;
        } else {
            c.write(struggler, Global.format("{self:SUBJECT-ACTION:try} to tip {other:name-do} off balance, but {other:pronoun-action:drop} {other:possessive} hips firmly, "
                            + "pushing {self:possessive} cock deep inside {other:reflective} and pinning {self:direct-object} to the floor. "
                            + "The sensations from wrestling with {self:possessive} cock buried inside {other:direct-object} almost make {self:direct-object} cum.", struggler, opponent));
        }
        if (!struggler.has(Trait.strapped)) {
            struggler.body.pleasure(opponent, opponent.body.getRandomPussy(), struggler.body.getRandomCock(), selfM, c);
        }
        opponent.body.pleasure(struggler, struggler.body.getRandomInsertable(), opponent.body.getRandomPussy(), targM, c);
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        Character opponent = getPartner(c, escapee);
        boolean cockbound = opponent.is(Stsflag.cockbound);

        int selfM = Global.random(6, 11);
        int targM = Global.random(6, 11);
        if (cockbound) {
            CockBound s = (CockBound) escapee.getStatus(Stsflag.cockbound);
            c.write(escapee,
                            Global.format("{self:SUBJECT-ACTION:try|tries} to escape {other:possessive} iron grip on {self:possessive} dick. However, {other:possessive} "
                                            + s.binding
                                            + " has other ideas. {other:SUBJECT-ACTION:run|runs} {other:possessive} "
                                            + s.binding
                                            + " up and down {self:possessive} cock and leaves {self:direct-object} gasping with pleasure.",
                            escapee, opponent));
            selfM += 5;
        } else {
            c.write(escapee, Global.format("{self:SUBJECT-ACTION:attempt} to escape {other:name-possessive} embrace, "
                            + "but {other:pronoun-action:drop} {other:possessive} hips firmly, pushing {self:possessive} "
                            + "cock deep inside {other:reflective} and pinning {self:direct-object} to the floor. "
                            + "The sensations from moving around so much with {self:possessive} cock buried inside {other:direct-object} almost make {self:direct-object} cum.", escapee, opponent));
        }
        if (!escapee.has(Trait.strapped)) {
            escapee.body.pleasure(opponent, opponent.body.getRandomPussy(), escapee.body.getRandomCock(), selfM, c);
        }
        opponent.body.pleasure(escapee, escapee.body.getRandomInsertable(), opponent.body.getRandomPussy(), targM, c);
        super.escape(c, escapee);
    }
}