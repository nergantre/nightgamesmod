package nightgames.stance;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.skills.Escape;
import nightgames.skills.Nothing;
import nightgames.skills.Skill;
import nightgames.skills.Struggle;
import nightgames.skills.Suckle;
import nightgames.skills.Wait;
import nightgames.skills.damage.DamageType;

public class NursingHold extends AbstractFacingStance {
    public NursingHold(Character top, Character bottom) {
        super(top, bottom, Stance.nursing);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "You are cradling " + bottom.nameOrPossessivePronoun()
                            + " head in your lap with your breasts dangling in front of " + bottom.directObject();
        } else {
            return String.format("%s is holding %s head in %s lap, with %s enticing "
                            + "breasts right in front of %s mouth.", top.subject(),
                            bottom.nameOrPossessivePronoun(), top.possessivePronoun(),
                            top.possessivePronoun(), bottom.possessivePronoun());
        }
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        return "nursing.jpg";
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return target == top && c != bottom;
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
        return target == bottom && c != top && c != bottom;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return target == bottom && c != top && c != bottom;
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
    public void decay(Combat c) {
        time++;
        bottom.weaken(c, (int) top.modifyDamage(DamageType.temptation, bottom, 3));
        top.emote(Emotion.dominant, 10);
    }

    @Override
    public float priorityMod(Character self) {
        return dom(self) ? self.has(Trait.lactating) ? 5 : 2 : 0;
    }

    @Override
    public Collection<Skill> availSkills(Combat c, Character self) {
        if (self != bottom) {
            return Collections.emptySet();
        } else {
            Collection<Skill> avail = new HashSet<Skill>();
            avail.add(new Suckle(bottom));
            avail.add(new Escape(bottom));
            avail.add(new Struggle(bottom));
            avail.add(new Nothing(bottom));
            avail.add(new Wait(bottom));
            return avail;
        }
    }

    @Override
    public boolean faceAvailable(Character target) {
        return target == top;
    }

    @Override
    public double pheromoneMod(Character self) {
        return 3;
    }
    
    @Override
    public int dominance() {
        return 3;
    }
    @Override
    public int distance() {
        return 1;
    }
}
