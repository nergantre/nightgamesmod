package nightgames.stance;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.skills.Skill;
import nightgames.status.Stsflag;

public abstract class Position implements Cloneable {
    public Character top;
    public Character bottom;
    public int time;
    public Stance en;

    public Position(Character top, Character bottom, Stance stance) {
        this.top = top;
        this.bottom = bottom;
        en = stance;
        time = 0;
    }

    public int pinDifficulty(Combat c, Character self) {
        return 4;
    }

    public int escape(Combat c, Character self) {
        if (sub(self) && !mobile(self)) {
            return -pinDifficulty(c, self) * Math.max(-5, 10 - time);
        }
        return 0;
    }

    public void struggle() {
        time += 2;
    }

    public void decay(Combat c) {
        time++;
    }

    public void checkOngoing(Combat c) {

    }

    public float getSubDomBonus(Character self, float bonus) {
        if (!self.human()) {
            if (self.has(Trait.submissive) && sub(self)) {
                return bonus;
            }
            if (!self.has(Trait.submissive) && dom(self)) {
                return bonus;
            }
        }
        return 0;
    }

    public abstract int distance();

    public abstract String describe();

    public abstract boolean mobile(Character c);

    public abstract boolean kiss(Character c, Character target);

    public abstract boolean dom(Character c);

    public abstract boolean sub(Character c);

    public abstract boolean reachTop(Character c);

    public abstract boolean reachBottom(Character c);

    public abstract boolean prone(Character c);

    public abstract boolean feet(Character c, Character target);

    public abstract boolean oral(Character c, Character target);

    public abstract boolean behind(Character c);

    public boolean getUp(Character c) {
        return mobile(c);
    }

    public boolean front(Character c) {
        return !behind(c);
    }

    public abstract boolean inserted(Character c);

    public boolean penisInserted(Character c) {
        return inserted(c) || c.is(Stsflag.inserted);
    }

    public abstract String image();

    public boolean inserted() {
        return inserted(top) || inserted(bottom);
    }

    public Position insert(Character pitcher, Character dom) {
        return this;
    }

    public Position insertRandom() {
        return insertRandomDom(top);
    }

    public Collection<Skill> availSkills(Character c) {
        return Collections.emptySet();
    }

    public boolean canthrust(Character c) {
        return dom(c) || c.has(Trait.powerfulhips);
    }

    public boolean facing(Character c, Character target) {
        return (!behind(top) && !behind(bottom)) || (c != bottom && c != top) || (target != bottom && target != top);
    }

    public float priorityMod(Character self) {
        return 0;
    }

    public boolean fuckable(Character self) {
        Character target;

        if (self == top) {
            target = bottom;
        } else {
            target = top;
        }
        return (self.crotchAvailable() || self.has(Trait.strapped) && target.hasPussy()) && target.crotchAvailable()
                        && mobile(self) && !mobile(target)
                        && ((self.hasDick() || self.has(Trait.strapped)) && !behind(target) || !behind(self))
                        && self.canAct();
    }

    public Stance enumerate() {
        return en;
    }

    @Override
    public Position clone() throws CloneNotSupportedException {
        return (Position) super.clone();
    }

    public Character other(Character character) {
        if (character.name().equals(top.name())) {
            return bottom;
        } else if (character.name().equals(bottom.name())) {
            return top;
        }
        return null;
    }

    public Position reverse(Combat c) {
        Position newStance;
        try {
            newStance = clone();
        } catch (CloneNotSupportedException e) {
            newStance = this;
        }
        Character nbot = top;
        Character ntop = bottom;
        newStance.bottom = nbot;
        newStance.top = ntop;
        return newStance;
    }

    public boolean anallyPenetrated() {
        return anallyPenetrated(top) || anallyPenetrated(bottom);
    }

    public boolean anallyPenetrated(Character self) {
        List<BodyPart> parts = partsFor(self);
        return BodyPart.hasType(parts, "ass") || self.is(Stsflag.pegged);
    }

    public Position insertRandomDom(Character target) {
        return this;
    }

    public Character getOther(Character c) {
        if (c == top) {
            return bottom;
        } else {
            return top;
        }
    }

    public boolean paizuri(Character self, Character target) {
        return oral(self, target);
    }

    public List<BodyPart> topParts() {
        if (inserted()) {
            throw new UnsupportedOperationException("Attempted to get topPart in position " + getClass().getSimpleName()
                            + ", but that position does not override the appropriate method.");
        }
        return Collections.emptyList();
    }

    public List<BodyPart> bottomParts() {
        if (inserted()) {
            throw new UnsupportedOperationException(
                            "Attempted to get bottomPart in position " + getClass().getSimpleName()
                                            + ", but that position does not override the appropriate method.");
        }
        return Collections.emptyList();
    }

    public BodyPart insertedPartFor(Character c) {
        return partsFor(c).stream().filter(part -> part.isType("cock") || part.isType("strapon")).findAny()
                        .orElse(Body.nonePart);
    }

    public BodyPart insertablePartFor(Character c) {
        BodyPart res = pussyPartFor(c);
        if (res.isType("none")) {
            return assPartFor(c);
        } else {
            return res;
        }
    }

    public BodyPart pussyPartFor(Character c) {
        return partsFor(c).stream().filter(part -> part.isType("pussy")).findAny().orElse(Body.nonePart);
    }

    public BodyPart assPartFor(Character c) {
        return partsFor(c).stream().filter(part -> part.isType("ass")).findAny().orElse(Body.nonePart);
    }

    public List<BodyPart> partsFor(Character c) {
        return c.equals(top) ? topParts() : bottomParts();
    }

    public boolean vaginallyPenetrated() {
        return vaginallyPenetrated(top) || vaginallyPenetrated(bottom);
    }

    public boolean penetrated(Character c) {
        return vaginallyPenetrated(c) || anallyPenetrated(c);
    }

    public boolean vaginallyPenetrated(Character c) {
        List<BodyPart> parts = partsFor(c);
        return (BodyPart.hasType(parts, "pussy") && inserted()) || c.is(Stsflag.fucked);
    }

    public boolean havingSexOtherNoStrapped(Character c) {
        Character other = getOther(c);
        return penetratedBy(other, c) || penetratedBy(c, other) && !other.has(Trait.strapped);
    }

    public boolean havingSexNoStrapped() {
        return penetratedBy(top, bottom) && !bottom.has(Trait.strapped)
                        || penetratedBy(bottom, top) && !top.has(Trait.strapped);
    }

    public boolean havingSex() {
        return penetratedBy(top, bottom) || penetratedBy(bottom, top);
    }

    public boolean penetratedBy(Character inserted, Character inserter) {
        return vaginallyPenetratedBy(inserted, inserter) || anallyPenetratedBy(inserted, inserter);
    }

    public boolean vaginallyPenetratedBy(Character self, Character other) {
        if (other != getOther(self)) {
            return false;
        }
        List<BodyPart> parts = partsFor(self);
        List<BodyPart> otherParts = partsFor(other);
        return BodyPart.hasType(parts, "pussy")
                        && (BodyPart.hasType(otherParts, "cock") || BodyPart.hasType(otherParts, "strapon"));
    }

    public boolean anallyPenetratedBy(Character self, Character other) {
        if (other != getOther(self)) {
            return false;
        }
        List<BodyPart> parts = partsFor(self);
        List<BodyPart> otherParts = partsFor(other);
        return (BodyPart.hasType(parts, "ass")
                        && (BodyPart.hasType(otherParts, "cock") || BodyPart.hasType(otherParts, "strapon"))) && inserted();
    }

    public boolean connected() {
        return anallyPenetrated() || vaginallyPenetrated() || inserted();
    }

    public boolean faceAvailable(Character target) {
        return true;
    }

    /*
     * returns likelihood modification of applying pheromones. 1 is normal, 2 is twice as likely, .5 is half as likely, 0 is never
     */
    public double pheromoneMod(Character self) {
        return 1;
    }
    
    /**
     * @return how dominant the dominant character is. positive for more dominant, negative for less.
     */
    public int dominance() {
        return 0;
    }

    public String name() {
        return getClass().getSimpleName();
    }

    /**
     * Stances have a dominance rating that benefits the dominant character, queried from Position.dominance().
     * 0: Not dominant at all. Seen in the Neutral position.
     * 1: Very give-and-take. Seen in the 69 position.
     * 2: Slightly dominant. Found in the TribadismStance and Mount positions.
     * 3: Average dominance. Missionary, Kneeling, Standing, and other "vanilla" positions all have this rating.
     * 4: High dominance. Anal positions and Pin are examples of positions with this rating.
     * 5: Absurd dominance. Exotic positions like Engulfed and FlyingCarry have this rating, as well as the more mundane FaceSitting and Smothering.
     *
     * @param self The character whose traits are checked to modify the current stance's dominance score.
     * @return The dominance of the current position, modified by one combatant's traits. Higher return values cause more willpower loss on each combat tick.
     * If a character is not the dominant character of the position, their effective dominance is 0.
     */
    public int getDominanceOfStance(Character self) {
        if (sub(self)) {
            return 0;
        }
        int stanceDominance = dominance();
        // It is unexpected, but not catastrophic if a character is at once a natural dom and submissive.
        if (self.has(Trait.naturalTop)) {
            // Rescales stance dominance values from 0-1-2-3-4-5 to 0-2-3-5-6-8
            stanceDominance = Double.valueOf(Math.ceil(stanceDominance * 1.5)).intValue();
        }
        if (self.has(Trait.submissive)) {
            // Rescales stance dominance values from 0-1-2-3-4-5 to 0-0-1-1-2-3
            stanceDominance = Double.valueOf(Math.floor(stanceDominance * 0.6)).intValue();
        }
        return Math.max(0, stanceDominance);
    }
}
