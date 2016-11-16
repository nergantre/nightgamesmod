package nightgames.stance;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import nightgames.characters.Character;
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
