package nightgames.stance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.skills.damage.DamageType;
import nightgames.status.InsertedStatus;

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

    public void setOtherCombatants(List<? extends Character> others) {}

    public Character domSexCharacter(Combat c) {
        return top;
    }

    public int pinDifficulty(Combat c, Character self) {
        return 4;
    }

    public int getEscapeMod(Combat c, Character self) {
        int dc = 0;
        if (sub(self) && !mobile(self)) {
            dc -= pinDifficulty(c, self) * Math.max(-5, 10 - time);
        }
        return dc;
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
            if ((!self.has(Trait.submissive) || self.has(Trait.flexibleRole)) && dom(self)) {
                return bonus;
            }
        }
        return 0;
    }

    public abstract int distance();

    public abstract String describe(Combat c);

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
        return mobile(c) && c == top;
    }

    public boolean front(Character c) {
        return !behind(c);
    }

    public abstract boolean inserted(Character c);

    public boolean penisInserted(Character self) {
        if (self == null || self.body.getRandomCock() == null) {
            return false;
        }
        return inserted(self) || self.getInsertedStatus().stream().anyMatch(status -> status.getPitcher().equals(self) && status.getStickPart() != null && status.getStickPart().isType("cock"));
    }

    public abstract String image();

    public boolean inserted() {
        return inserted(top) || inserted(bottom);
    }

    public Position insert(Combat c, Character pitcher, Character dom) {
        return this;
    }

    public Position insertRandom(Combat c) {
        return insertRandomDom(c, top);
    }

    public Collection<Skill> availSkills(Combat c, Character self) {
        return Collections.emptySet();
    }

    public boolean canthrust(Combat c, Character self) {
        return domSexCharacter(c) == self || self.has(Trait.powerfulhips);
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

    public Stance 
    enumerate() {
        return en;
    }

    @Override
    public Position clone() throws CloneNotSupportedException {
        return (Position) super.clone();
    }

    public Character other(Character character) {
        if (character.getTrueName().equals(top.getTrueName())) {
            return bottom;
        } else if (character.getTrueName().equals(bottom.getTrueName())) {
            return top;
        }
        return null;
    }

    public Position reverse(Combat c, boolean writeMessage) {
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

    public boolean anallyPenetrated(Combat combat) {
        return anallyPenetrated(combat, top) || anallyPenetrated(combat, bottom);
    }

    public boolean anallyPenetrated(Combat combat, Character self) {
        if (self == null || self.body.getRandomAss() == null) {
            return false;
        }
        List<BodyPart> parts = partsForStanceOnly(combat, self, getPartner(combat, self));
        return BodyPart.hasType(parts, "ass") || self.getInsertedStatus().stream().anyMatch(status -> status.getReceiver().equals(self) && status.getHolePart() != null && status.getHolePart().isType("ass"));
    }

    public Position insertRandomDom(Combat c, Character target) {
        return this;
    }

    public Character getPartner(Combat c, Character self) {
        if (self == top) {
            return bottom;
        } else {
            return top;
        }
    }

    public List<Character> getAllPartners(Combat c, Character self) {
        return Collections.singletonList(getPartner(c, self));
    }

    public boolean paizuri(Character self, Character target) {
        return oral(self, target);
    }

    public List<BodyPart> topParts(Combat c) {
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

    public BodyPart insertedPartFor(Combat combat, Character c) {
        return partsForStanceOnly(combat, c, combat.getOpponent(c)).stream().filter(part -> part.isType("cock") || part.isType("strapon")).findAny()
                        .orElse(Body.nonePart);
    }

    public BodyPart insertablePartFor(Combat combat, Character self, Character other) {
        BodyPart res = pussyPartFor(combat, self, other);
        if (res.isType("none")) {
            return assPartFor(combat, self, other);
        } else {
            return res;
        }
    }

    public BodyPart pussyPartFor(Combat combat, Character self, Character other) {
        return partsForStanceOnly(combat, self, other).stream().filter(part -> part.isType("pussy")).findAny().orElse(Body.nonePart);
    }

    public BodyPart assPartFor(Combat combat, Character self, Character other) {
        return partsForStanceOnly(combat, self, other).stream().filter(part -> part.isType("ass")).findAny().orElse(Body.nonePart);
    }

    public List<BodyPart> getPartsFor(Combat combat, Character self, Character other) {
        List<BodyPart> parts = new ArrayList<>(partsForStanceOnly(combat, self, other));
        Stream.concat(self.getInsertedStatus().stream(), other.getInsertedStatus().stream())
                        .filter(s -> (s.getPitcher() == self && s.getReceiver() == other) || (s.getReceiver() == self && s.getPitcher() == other))
                        .forEach(s -> {
                            if (s.getPitcher() == self) {
                                parts.add(s.getStickPart());
                            } else {
                                parts.add(s.getHolePart());
                            }
                        });
        return parts;
    }

    public List<BodyPart> partsForStanceOnly(Combat combat, Character self, Character other) {
        if (self.equals(top)) {
            return topParts(combat);
        } else if (self.equals(bottom)) {
            return bottomParts();
        } else {
            return Collections.emptyList();
        }
    }

    public boolean vaginallyPenetrated(Combat c) {
        return vaginallyPenetrated(c, top) || vaginallyPenetrated(c, bottom);
    }

    public boolean penetrated(Combat combat, Character c) {
        return vaginallyPenetrated(combat, c) || anallyPenetrated(combat, c);
    }
    
    public Character getPenetratedCharacter(Combat c, Character self) {
        return getPartner(c, self);
    }

    public boolean vaginallyPenetrated(Combat combat, Character self) {
        if (self == null || self.body.getRandomPussy() == null) {
            return false;
        }
        List<BodyPart> parts = partsForStanceOnly(combat, self, getPartner(combat, self));
        return (BodyPart.hasType(parts, "pussy") && inserted()) || self.getInsertedStatus().stream().anyMatch(status -> status.getReceiver().equals(self) && status.getHolePart() != null && status.getHolePart().isType("pussy"));
    }

    public boolean havingSexOtherNoStrapped(Combat c, Character self) {
        Character other = getPartner(c, self);
        return (penetratedBy(c, other, self) || penetratedBy(c, self, other)) && !other.has(Trait.strapped);
    }

    public boolean havingSexNoStrapped(Combat c) {
        return (penetratedBy(c, top, bottom) && !bottom.has(Trait.strapped)
                        || penetratedBy(c, bottom, top)) && !top.has(Trait.strapped);
    }

    public boolean havingSex(Combat c) {
        return penetratedBy(c, domSexCharacter(c), bottom) || penetratedBy(c, bottom, domSexCharacter(c)) || en == Stance.trib;
    }

    public boolean havingSex(Combat c, Character self) {
        if (domSexCharacter(c) == self || bottom == self) {
            return havingSex(c);
        }
        return false;
    }

    public boolean penetratedBy(Combat c, Character inserted, Character inserter) {
        return vaginallyPenetratedBy(c, inserted, inserter) || anallyPenetratedBy(c, inserted, inserter);
    }

    public boolean vaginallyPenetratedBy(Combat c, Character inserted, Character inserter) {
        if (inserter != getPartner(c, inserted)) {
            return false;
        }
        List<BodyPart> parts = partsForStanceOnly(c, inserted, inserter);
        List<BodyPart> otherParts = partsForStanceOnly(c, inserter, inserted);
        return BodyPart.hasType(parts, "pussy") && (BodyPart.hasType(otherParts, "cock") || BodyPart.hasType(otherParts, "strapon"));
    }

    public boolean anallyPenetratedBy(Combat c, Character self, Character other) {
        if (other != getPartner(c, self)) {
            return false;
        }
        List<BodyPart> parts = partsForStanceOnly(c, self, other);
        List<BodyPart> otherParts = partsForStanceOnly(c, other, other);
        return (BodyPart.hasType(parts, "ass")
                        && (BodyPart.hasType(otherParts, "cock") || BodyPart.hasType(otherParts, "strapon"))) && inserted();
    }

    public boolean connected(Combat c) {
        return anallyPenetrated(c) || vaginallyPenetrated(c) || inserted();
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

    public boolean isBeingFaceSatBy(Combat c, Character self, Character target) {
        return isFacesatOn(self) && isFaceSitting(target);
    }

    public boolean isFaceSitting(Character self) {
        return false;
    }
   
    public boolean isFacesatOn(Character self) {
        return false;
    }

    public boolean reversable(Combat c) {
        return reverse(c, false) != this;
    }

    public void struggle(Combat c, Character struggler) {
        time += 2;
        Character partner = getPartner(c, struggler);
        partner.weaken(c, (int) struggler.modifyDamage(DamageType.stance, partner, Global.random(6, 11)));
    }

    public void escape(Combat c, Character escapee) {
        time += 2;
    }

    public boolean isPartFuckingPartInserted(Combat c, Character inserter, BodyPart stick, Character inserted, BodyPart hole) {
        if (c == null || inserter == null || stick == null || inserted == null || hole == null) {
            return false;
        }
        if (hole.isType("mouth") && stick.isType("cock")) {
            // TODO fix me so this doesn't need to be true all the time.
            return true;
        }
        if (vaginallyPenetratedBy(c, inserted, inserter)) {
            return hole.isType("pussy") && stick.isType("cock");
        }
        if (anallyPenetratedBy(c, inserted, inserter)) {
            return hole.isType("ass") && stick.isType("cock");
        }
        List<InsertedStatus> insertedStatus = Stream.concat(inserter.getInsertedStatus().stream(), inserted.getInsertedStatus().stream()).collect(Collectors.toList());
        return insertedStatus.stream().anyMatch(is -> hole.equals(is.getHolePart()) && inserted.equals(is.getReceiver()) && inserter.equals(is.getPitcher()) && stick.equals(is.getStickPart()));
    }
}