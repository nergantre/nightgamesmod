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

public class FFMFacesittingThreesome extends FFMCowgirlThreesome {
    public FFMFacesittingThreesome(Character domSexCharacter, Character top, Character bottom) {
        super(domSexCharacter, top, bottom);
    }

    @Override
    public String describe(Combat c) {
        if (top.human()) {
            return "";
        } else {
            return Global.format("{self:SUBJECT-ACTION:are|is} pressing {self:POSSESSIVE} ass "
                            + "into {other:name-possessive} face while %s fucking {other:direct-object} in the Cowgirl position.", top, bottom, domSexCharacter.subjectAction("are", "is"));
        }
    }

    @Override
    public List<BodyPart> partsForStanceOnly(Combat combat, Character self, Character other) {
        if (self == domSexCharacter(combat) && other == bottom) {
            return topParts(combat);
        }
        if (self == top) {
                return Arrays.asList(top.body.getRandomPussy()).stream().filter(part -> part != null && part.present())
                                .collect(Collectors.toList());
        } else if (self == bottom) {
            if (other == top) {
                return Arrays.asList(top.body.getRandom("mouth")).stream().filter(part -> part != null && part.present())
                                .collect(Collectors.toList());
            } else if (other == domSexCharacter) {
                return Arrays.asList(top.body.getRandomInsertable()).stream().filter(part -> part != null && part.present())
                                .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
    
    @Override
    public void checkOngoing(Combat c) {
        if (!c.getOtherCombatants().contains(domSexCharacter)) {
            c.write(bottom, Global.format("With the disappearance of {self:name-do}, {other:subject-action:manage|manages} to escape.", domSexCharacter, bottom));
            c.setStance(new Neutral(top, bottom));
        }
    }

    public List<Character> getAllPartners(Combat c, Character self) {
        if (self == bottom) {
            return Arrays.asList(top, domSexCharacter);
        }
        return Collections.singletonList(getPartner(c, self));
    }

    @Override
    public String image() {
        return "ThreesomeFFMFacesitting.jpg";
    }

    @Override
    public float priorityMod(Character self) {
        return super.priorityMod(self) + 3;
    }


    @Override
    public boolean kiss(Character c, Character target) {
        return c != bottom && target != bottom;
    }

    @Override
    public int dominance() {
        return 5;
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
    public double pheromoneMod(Character self) {
        if (self == top) {
            return 10;
        } else if (self == domSexCharacter || self == bottom) {
            return 3;
        }
        return super.pheromoneMod(self);
    }

    public boolean isFaceSitting(Character self) {
        return self == top;
    }

    public boolean isFacesatOn(Character self) {
        return self == bottom;
    }
}
