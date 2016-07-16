package nightgames.stance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Engulfed extends Position {

    private boolean slimePitches;

    public Engulfed(Character top, Character bottom) {
        super(top, bottom, Stance.engulfed);
        slimePitches = slimePitches();
    }

    @Override
    public String describe() {
        if (top.human()) {
            return "You have engulfed " + bottom.name() + " inside your slime body, with only "
                            + bottom.possessivePronoun() + " face outside of you";
        } else {
            return top.name() + " is holding your entire body inside " + top.possessivePronoun()
                            + " slime body, with only your face outside.";
        }
    }

    @Override
    public int pinDifficulty(Combat c, Character self) {
        return 15;
    }

    @Override
    public boolean mobile(Character c) {
        return c == top;
    }

    @Override
    public String image() {
        if (bottom.hasPussy()) {
            return "engulfed_f.jpg";
        } else {
            return "engulfed_m.jpg";
        }
    }

    @Override
    public boolean kiss(Character c) {
        return c == top;
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
        return c == top;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c == top;
    }

    @Override
    public boolean prone(Character c) {
        return false;
    }


    @Override
    public boolean feet(Character c) {
        return c == top;
    }

    @Override
    public boolean oral(Character c) {
        return c == top;
    }

    @Override
    public boolean behind(Character c) {
        return c == top;
    }

    @Override
    public boolean front(Character c) {
        return true;
    }

    @Override
    public boolean inserted(Character c) {
        return slimePitches == (c == top);
    }

    @Override
    public Position insertRandom() {
        return new Neutral(top, bottom);
    }

    @Override
    public Position reverse(Combat c) {
        if (bottom.has(Trait.slime)) {
            c.write(bottom, String.format("%s %s slimy body around %s, reversing %s hold.",
                            bottom.subjectAction("swirls", "swirl"), bottom.possessivePronoun(),
                            top.nameOrPossessivePronoun(), top.possessivePronoun()));
            return super.reverse(c);
        }
        c.write(bottom, String.format("%s loose from %s slimy grip and %s away from %s.", 
                        bottom.subjectAction("struggles", "struggle"), top.nameOrPossessivePronoun(),
                        bottom.action("stagger", "staggers"), top.directObject()));
        return new Neutral(top, bottom);
    }

    @Override
    public void decay(Combat c) {
        time++;
        bottom.weaken(c, 5);
        top.emote(Emotion.dominant, 10);
    }

    @Override
    public float priorityMod(Character self) {
        return dom(self) ? 5 : 0;
    }

    @Override
    public List<BodyPart> topParts() {
        List<BodyPart> parts = new ArrayList<>();
        if (slimePitches) {
            parts.addAll(top.body.get("cock"));
        } else {
            parts.addAll(top.body.get("pussy"));
            parts.addAll(top.body.get("ass"));
        }
        return parts.stream()
                    .filter(part -> part != null && part.present())
                    .collect(Collectors.toList());
    }

    @Override
    public List<BodyPart> bottomParts() {
        List<BodyPart> parts = new ArrayList<>();
        if (!slimePitches) {
            parts.addAll(bottom.body.get("cock"));
        } else {
            parts.addAll(bottom.body.get("pussy"));
            parts.addAll(bottom.body.get("ass"));
        }
        return parts.stream()
                    .filter(part -> part != null && part.present())
                    .collect(Collectors.toList());
    }

    @Override
    public boolean faceAvailable(Character target) {
        return target == top;
    }

    @Override
    public double pheromoneMod(Character self) {
        return 10;
    }

    private boolean slimePitches() {
        if (!top.hasDick())
            return false;
        if (!bottom.hasDick())
            return true;
        return Global.random(2) == 0;
    }
    
    @Override
    public int dominance() {
        return 5;
    }
}
