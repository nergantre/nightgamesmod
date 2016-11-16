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

public class MFMSpitroastThreesome extends MaledomSexStance {
    protected Character domSexCharacter;

    public MFMSpitroastThreesome(Character domSexCharacter, Character top, Character bottom) {
        super(top, bottom, Stance.doggy);
        this.domSexCharacter = domSexCharacter;
    }

    @Override
    protected Character domSexCharacter(Combat c) {
        return domSexCharacter;
    }

    @Override
    public float priorityMod(Character self) {
        return super.priorityMod(self) + 3;
    }

    @Override
    public void setOtherCombatants(List<? extends Character> others) {
        for (Character other : others) {
            if (other.equals(domSexCharacter)) {
                domSexCharacter = other;
            }
        }
    }

    public List<BodyPart> partsFor(Combat combat, Character c) {
        if (c == domSexCharacter(combat)) {
            return topParts(combat);
        }

        if (c == top) {
                return Arrays.asList(top.body.getRandomInsertable()).stream().filter(part -> part != null && part.present())
                                .collect(Collectors.toList());
        }

        return c.equals(bottom) ? Arrays.asList(bottom.body.getRandomPussy(), bottom.body.getRandomAss()).stream().filter(part -> part != null && part.present())
                        .collect(Collectors.toList()) : Collections.emptyList();
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
    public String describe(Combat c) {
        if (top.human()) {
            return "";
        } else {
            return String.format("%s is fucking %s ass while %s pounding away at %s pussy.",
                            top.subject(), bottom.nameOrPossessivePronoun(), domSexCharacter(c).subjectAction("are", "is"), bottom.possessivePronoun());
        }
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        if (top.useFemalePronouns() || domSexCharacter.useFemalePronouns()) {
            return "ThreesomeMFMDoublePenFuta.jpg";
        }
        return "ThreesomeMFMDoublePen.jpg";
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c != bottom && target != bottom;
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
        return c != bottom;
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
    public boolean behind(Character c) {
        return c == domSexCharacter;
    }

    @Override
    public Position insertRandom(Combat c) {
        return new Mount(top, bottom);
    }

    @Override
    public Position reverse(Combat c) {
        c.write(bottom, Global.format("{self:SUBJECT-ACTION:manage|manages} to unbalance {other:name-do} and push {other:direct-object} off {self:reflective}.", bottom, top));
        return new Neutral(bottom, top);
    }
    
    @Override
    public int dominance() {
        return 4;
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
}
