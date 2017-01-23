package nightgames.stance;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class FFXTribThreesome extends Position {
    private Character domSexCharacter;

    public FFXTribThreesome(Character domSexCharacter, Character top, Character bottom) {
        super(top, bottom, Stance.trib);
        this.domSexCharacter = domSexCharacter;
    }

    @Override
    public String describe(Combat c) {
        return domSexCharacter.subjectAction("are", "is") + " holding " + bottom.nameOrPossessivePronoun() + " legs across "
                        + top.possessiveAdjective() + " lap while grinding " + domSexCharacter
                        .possessiveAdjective()
                        + " soaked cunt into " + bottom.possessiveAdjective() + " pussy.";
    }

    @Override
    public float priorityMod(Character self) {
        return super.priorityMod(self) + 3;
    }

    @Override
    public Character domSexCharacter(Combat c) {
        return domSexCharacter;
    }

    @Override
    public void checkOngoing(Combat c) {
        if (!c.getOtherCombatants().contains(domSexCharacter)) {
            c.write(bottom, Global.format("With the disappearance of {self:name-do}, {other:subject-action:manage|manages} to escape.", domSexCharacter, bottom));
            c.setStance(new Neutral(top, bottom));
        }
    }

    @Override
    public void setOtherCombatants(List<? extends Character> others) {
        for (Character other : others) {
            if (other.equals(domSexCharacter)) {
                domSexCharacter = other;
            }
        }
    }

    public List<BodyPart> partsFor(Combat combat, Character self, Character other) {
        if (self == domSexCharacter(combat) && other == bottom) {
            return topParts(combat);
        }
        return self.equals(bottom) ? bottomParts() : Collections.emptyList();
    }

    public Character getPartner(Combat c, Character self) {
        Character domSex = domSexCharacter(c);
        if (self == top) {
            return bottom;
        } else if (domSex == self) {
            return bottom;
        } else {
            return domSex;
        }
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
        if (top.useFemalePronouns()) {
            return "ThreesomeFFFTrib.jpg";
        } else {
            return "ThreesomeMFFTrib.jpg";
        }
    }

    @Override
    public boolean dom(Character c) {
        return c == top || c == domSexCharacter;
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
        return true;
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
    public Position reverse(Combat c, boolean writeMessage) {
        if (writeMessage) {
            c.write(bottom, Global.format("{self:SUBJECT-ACTION:manage|manages} to unbalance {other:name-do} and push {other:direct-object} off {self:reflective}.", bottom, top));
        }
        return new Neutral(bottom, top);
    }

    @Override
    public Collection<Skill> availSkills(Combat c, Character self) {
        if (self != domSexCharacter) {
            return Collections.emptySet();
        } else {
            Collection<Skill> avail = self.getSkills().stream()
                            .filter(skill -> skill.requirements(c, self, bottom))
                            .filter(skill -> Skill.skillIsUsable(c, skill, bottom))
                            .filter(skill -> skill.type(c) == Tactics.fucking).collect(Collectors.toSet());
            return avail;
        }
    }

    @Override
    public List<BodyPart> topParts(Combat c) {
        return Arrays.asList(top.body.getRandomPussy()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList());
    }

    @Override
    public double pheromoneMod(Character self) {
        return 3;
    }
    
    @Override
    public int dominance() {
        return 2;
    }

    @Override
    public int distance() {
        return 1;
    }

    private void strugglePleasure(Combat c, Character self, Character opponent) {
        int selfM = Global.random(6, 11);
        int targM = Global.random(6, 11);
        self.body.pleasure(opponent, opponent.body.getRandomPussy(), self.body.getRandomPussy(), selfM, c);
        opponent.body.pleasure(self, self.body.getRandomPussy(), opponent.body.getRandomPussy(), targM, c);
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        Character opponent = getPartner(c, struggler);
        c.write(struggler, Global.format("{self:SUBJECT-ACTION:struggle} in {other:name-possessive} grip, "
                        + "but the slippery sensation of your sexes sliding against each other distracts "
                        + "{self:direct-object} long enough for {other:pronoun} to regain {other:possessive} grip on {self:possessive} leg.", struggler, opponent));
        strugglePleasure(c, struggler, opponent);
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        Character opponent = getPartner(c, escapee);
        c.write(escapee, Global.format("{self:SUBJECT-ACTION:attempt} to rock {self:possessive} hips wildly, "
                        + "hoping it will distract {other:name-do} long enough for {self:direct-object} to escape. "
                        + "Sadly, it doesn't accomplish much other than arousing the hell out of both of you.", escapee, opponent));
        strugglePleasure(c, escapee, opponent);
        super.escape(c, escapee);
    }
}
