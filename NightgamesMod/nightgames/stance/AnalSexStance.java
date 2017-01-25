package nightgames.stance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Stsflag;

public abstract class AnalSexStance extends Position {
    public AnalSexStance(Character top, Character bottom, Stance stance) {
        super(top, bottom, stance);
    }

    @Override
    public float priorityMod(Character self) {
        float priority = 0;
        priority += getSubDomBonus(self, 4);
        if (!inserted(self) && self.body.getRandom("ass") != null) {
            priority += self.body.getRandom("ass").priority(self);
        } else if (inserted(self) && self.body.getRandomInsertable() != null) {
            priority += self.body.getRandomInsertable().priority(self);
        }
        return priority;
    }

    @Override
    public List<BodyPart> topParts(Combat c) {
        return Arrays.asList(top.body.getRandomInsertable()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public List<BodyPart> bottomParts() {
        return Arrays.asList(bottom.body.getRandomAss()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public boolean feet(Character c, Character target) {
        return false;
    }

    @Override
    public boolean oral(Character c, Character target) {
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
        Character inserter = inserted(top) ? top : bottom;
        Character inserted = inserted(top) ? bottom : top;
        Character opponent = getPartner(c, struggler);
        boolean knotted = top.is(Stsflag.knotted);

        if (struggler.human()) {
            if (knotted) {
                c.write(struggler, "You try to force " + inserter.nameOrPossessivePronoun()
                                + " dick out of " + inserter.nameOrPossessivePronoun() + " ass, but the knot at its base is utterly unyielding.");
            } else if (struggler == inserted) {
                c.write(struggler, "You try to pull free, but " + inserter.subject()
                                + " has a good grip on your waist.");
            } else {
                c.write(struggler, "You try to pull dislodge " + inserted.nameDirectObject()
                                + ", but " + inserted.pronoun() + " holds you down with " + inserted.possessiveAdjective() + " ass.");
            }
            c.write(bottom, Global.format("{other:POSSESSIVE} hard cock grinding against {self:possessive} "
                            + "bowels as %s to twist out of %s grip brings both of you closer to the edge.", inserted, inserter,
                            struggler.pronoun() + " " + struggler.action("attempt"), opponent.possessiveAdjective()));
        } else if (c.shouldPrintReceive(getPartner(c, struggler), c)) {
            if (knotted) {
                c.write(bottom,
                                String.format("%s frantically attempts to get %s cock out of %s ass, "
                                                + "but %s knot is keeping it inside %s warm depths.",
                                                struggler.subject(), inserter.nameOrPossessivePronoun(),
                                                inserted.possessiveAdjective(), inserter.possessiveAdjective(),
                                                inserted.possessiveAdjective()));
            } else {
                c.write(bottom, String.format("%s tries to squirm away, but %s better leverage.",
                                struggler.subject(), opponent.subjectAction("have", "has")));
            }
            c.write(bottom, Global.format("{other:POSSESSIVE} hard cock grinding against {self:possessive} "
                            + "bowels as %s to twist out of %s grip brings both of %s closer to the edge.", inserted, inserter,
                            struggler.pronoun() + struggler.action(" attempt"), opponent.possessiveAdjective(), 
                            c.bothDirectObject(opponent)));
        }
        bottom.body.pleasure(top, Global.pickRandom(topParts(c)).orElse(null), Global.pickRandom(bottomParts()).orElse(null), Global.random(6, 10), c);
        top.body.pleasure(bottom, Global.pickRandom(bottomParts()).orElse(null), Global.pickRandom(topParts(c)).orElse(null), Global.random(6, 10), c);
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        Character inserter = inserted(top) ? top : bottom;
        Character opponent = getPartner(c, escapee);
        boolean knotted = top.is(Stsflag.knotted);

        if (knotted) {
            c.write(escapee, Global.capitalizeFirstLetter(escapee.subjectAction("try")) + " to force " + inserter.nameOrPossessivePronoun()
                            + " dick out of " + inserter.nameOrPossessivePronoun() + " ass, but the knot at its base is utterly unyielding.");
        } else if (escapee == inserter) {
            c.write(escapee, Global.format("{self:SUBJECT-ACTION:try} to take advantage of an opening in {other:name-possessive} stance to slip away, "
                            + "but {other:pronoun-action:force} {other:possessive} ass on top of {self:direct-object} again, pressing {self:direct-object} into the floor.",
                            escapee, opponent));
        } else {
            c.write(escapee, Global.format("{self:SUBJECT-ACTION:try} to take advantage of an opening in {other:name-possessive} stance to slip away, "
                            + "but {other:pronoun-action:pounds} {other:possessive} cock into {self:possessive} ass, forcing {self:direct-object} to give up.", escapee, opponent));
        }
        bottom.body.pleasure(top, Global.pickRandom(topParts(c)).orElse(null), Global.pickRandom(bottomParts()).orElse(null), Global.random(6, 10), c);
        top.body.pleasure(bottom, Global.pickRandom(bottomParts()).orElse(null), Global.pickRandom(topParts(c)).orElse(null), Global.random(6, 10), c);
        super.escape(c, escapee);
    }
}