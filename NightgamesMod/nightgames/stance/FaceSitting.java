package nightgames.stance;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Anilingus;
import nightgames.skills.Blowjob;
import nightgames.skills.Cunnilingus;
import nightgames.skills.Escape;
import nightgames.skills.Nothing;
import nightgames.skills.Skill;
import nightgames.skills.Struggle;
import nightgames.skills.Wait;
import nightgames.skills.damage.DamageType;

public class FaceSitting extends AbstractBehindStance {
    FaceSitting(Character top, Character bottom, Stance en) {
        super(top, bottom, en);
    }
    public FaceSitting(Character top, Character bottom) {
        super(top, bottom, Stance.facesitting);
    }

    @Override
    public String describe(Combat c) {
        return Global.capitalizeFirstLetter(top.subjectAction("are", "is")) + " sitting on "
                        + bottom.nameOrPossessivePronoun() + " face while holding " + bottom.possessivePronoun()
                        + " arms so " + bottom.subject() + " cannot escape";
    }

    @Override
    public int pinDifficulty(Combat c, Character self) {
        return 7;
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        if (top.hasPussy() && bottom.hasPussy()) {
            return "facesitting_ff.jpg";
        }
        return "facesitting.jpg";
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return target == top && c != bottom;
    }

    @Override
    public boolean facing(Character c, Character target) {
        return c != bottom && target != bottom;
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
        return c == bottom;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return target == bottom;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return (c == bottom && target == top) || (target == bottom && c != top);
    }

    @Override
    public boolean behind(Character c) {
        return c == top;
    }

    @Override
    public boolean inserted(Character c) {
        return false;
    }

    @Override
    public Position insert(Combat c, Character pitcher, Character dom) {
        Character catcher = getPartner(c, pitcher);
        Character sub = getPartner(c, pitcher);
        if (pitcher.body.getRandomInsertable() == null || !catcher.hasPussy()) {
            // invalid
            return this;
        }
        if (pitcher == dom && pitcher == top) {
            // guy is sitting on girl's face facing her feet, and is the
            // dominant one in the new stance
            return new UpsideDownMaledom(pitcher, catcher);
        }
        if (pitcher == sub && pitcher == top) {
            // guy is sitting on girl's face facing her feet, and is the
            // submissive one in the new stance
            return Cowgirl.similarInstance(catcher, pitcher);
        }
        if (pitcher == dom && pitcher == bottom) {
            // girl is sitting on guy's face facing his feet, and is the
            // submissive one in the new stance
            return new Doggy(pitcher, catcher);
        }
        if (pitcher == sub && pitcher == bottom) {
            // girl is sitting on guy's face facing his feet, and is the
            // dominant one in the new stance
            return new ReverseCowgirl(catcher, pitcher);
        }
        return this;
    }

    @Override
    public void decay(Combat c) {
        time++;
        bottom.weaken(c, (int) top.modifyDamage(DamageType.stance, bottom, 5));
        top.emote(Emotion.dominant, 20);
        top.emote(Emotion.horny, 10);
        if (top.has(Trait.energydrain)) {
            c.write(top, Global.format(
                            "{self:NAME-POSSESSIVE} body glows purple as {other:subject-action:feel|feels} {other:possessive} very spirit drained through your connection.",
                            top, bottom));
            int m = Global.random(5) + 5;
            bottom.drain(c, top, (int) top.modifyDamage(DamageType.drain, bottom, m));
        }
    }

    @Override
    public Collection<Skill> availSkills(Combat c, Character self) {
        if (self != bottom) {
            return Collections.emptySet();
        } else {
            Collection<Skill> avail = new HashSet<Skill>();
            avail.add(new Cunnilingus(bottom));
            avail.add(new Anilingus(bottom));
            avail.add(new Blowjob(bottom));
            avail.add(new Escape(bottom));
            avail.add(new Struggle(bottom));
            avail.add(new Nothing(bottom));
            avail.add(new Wait(bottom));
            return avail;
        }
    }

    @Override
    public float priorityMod(Character self) {
        return getSubDomBonus(self, top.has(Trait.energydrain) ? 5.0f : 3.0f);
    }

    @Override
    public boolean faceAvailable(Character target) {
        return target == top;
    }

    @Override
    public double pheromoneMod(Character self) {
        if (self == top) {
            return 10;
        }
        return 2;
    }

    @Override
    public int dominance() {
        return 5;
    }
    @Override
    public int distance() {
        return 1;
    }

    public boolean isFaceSitting(Character self) {
        return self == top;
    }

    public boolean isFacesatOn(Character self) {
        return self == bottom;
    }
}
