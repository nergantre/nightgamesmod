package nightgames.stance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class TribadismStance extends Position {
    public TribadismStance(Character top, Character bottom) {
        super(top, bottom, Stance.trib);
    }

    @Override
    public String describe(Combat c) {
        return top.subjectAction("are", "is") + " holding " + bottom.nameOrPossessivePronoun() + " legs across "
                        + top.possessivePronoun() + " chest while grinding " + top.possessivePronoun()
                        + " soaked cunt into " + bottom.possessivePronoun() + " pussy.";
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return true;
    }

    @Override
    public boolean inserted(Character c) {
        return false;
    }

    @Override
    public String image() {
        return "trib.jpg";
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
    public boolean feet(Character c, Character target) {
        return false;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return false;
    }

    @Override
    public boolean behind(Character c) {
        return false;
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Mount(top, bottom);
    }

    @Override
    public List<BodyPart> bottomParts() {
        return Arrays.asList(bottom.body.getRandomPussy()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public List<BodyPart> topParts(Combat c) {
        return Arrays.asList(top.body.getRandomPussy()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
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
        return 2;
    }

    @Override
    public int distance() {
        return 1;
    }
}
