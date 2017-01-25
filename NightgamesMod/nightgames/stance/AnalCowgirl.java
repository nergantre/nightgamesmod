package nightgames.stance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class AnalCowgirl extends AnalSexStance {

    public AnalCowgirl(Character top, Character bottom) {
        super(top, bottom, Stance.anal);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return String.format("You're sitting on top of %s with your ass squeezing her cock.",
                            bottom.nameDirectObject());
        } else {
            return String.format("%s flat on %s back with %s cock buried inside %s ass.",
                            bottom.subjectAction("are", "is"), bottom.possessiveAdjective(),
                            bottom.possessiveAdjective(), top.nameOrPossessivePronoun());
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
        return true;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c != bottom;
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
    public boolean inserted(Character c) {
        return c == bottom;
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Mount(top, bottom);
    }

    @Override
    public String image() {
        return "anal_cowgirl.jpg";
    }

    @Override
    public void checkOngoing(Combat c) {
        Character inserter = inserted(top) ? top : bottom;
        Character inserted = inserted(top) ? bottom : top;

        if (!inserter.hasInsertable()) {
            if (inserter.human()) {
                c.write("With " + inserter.possessiveAdjective()
                                + " pole gone, you groan in frustration and cease your merciless movements.");
            } else {
                c.write(inserted.getName() + " groans with frustration with the sudden disappearance of "
                                + inserter.nameOrPossessivePronoun() + " pole.");
            }
            c.setStance(insertRandom(c));
        }
        if (inserted.body.getRandom("ass") == null) {
            if (inserted.human()) {
                c.write("With your asshole suddenly disappearing, you can't continue riding " + inserter.getName()
                                + " anymore.");
            } else {
                c.write(inserted.getName() + " groans with frustration with the sudden disappearance of "
                                + inserted.possessiveAdjective() + " asshole.");
            }
            c.setStance(insertRandom(c));
        }
    }

    @Override
    public boolean anallyPenetrated(Combat combat, Character self) {
        return self == top;
    }

    @Override
    public List<BodyPart> topParts(Combat c) {
        return Arrays.asList(top.body.getRandomAss()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public List<BodyPart> bottomParts() {
        return Arrays.asList(bottom.body.getRandomInsertable()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }
    
    @Override
    public Position reverse(Combat c, boolean writeMessage) {
        if (writeMessage) {
            c.write(bottom, Global
                            .format("{self:SUBJECT-ACTION:manage|manages} to unbalance {other:name-do} and push {other:direct-object} forward onto {other:possessive} hands and knees. {self:SUBJECT-ACTION:follow|follows} {other:direct-object}, still inside {other:possessive} tight ass, and {self:SUBJECT-ACTION:continue|continues} "
                                            + "to fuck {other:direct-object} from behind.", bottom, top));
        }
        return new Anal(bottom, top);
    }
    
    @Override
    public int dominance() {
        return 4;
    }
}
