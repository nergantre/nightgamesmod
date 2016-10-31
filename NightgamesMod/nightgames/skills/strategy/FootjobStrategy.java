package nightgames.skills.strategy;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Footjob;
import nightgames.skills.Skill;
import nightgames.skills.StandUp;
import nightgames.skills.TakeOffShoes;

public class FootjobStrategy extends KnockdownThenActionStrategy {
    @Override
    public double weight(Combat c, Character self) {
        double weight = .25;
        if (!(new Footjob(self)).requirements(c, self, c.getOther(self))) {
            return 0;
        }
        if (c.getOther(self).has(Trait.footfetishist)) {
            weight += 2;
        }
        if (self.has(Trait.nimbletoes)) {
            weight += 1;
        }
        if (self.getMood().equals(Emotion.dominant)) {
            weight += .75;
        }
        return weight;
    }

    @Override
    protected Set<Skill> getPreferredSkills(Combat c, Character self, Set<Skill> allowedSkills) {
        Set<Skill> footjobSkills = allowedSkills.stream()
                        .filter(skill -> (skill.getTags().contains(SkillTag.usesFeet) || skill.getTags().contains(SkillTag.stripping))
                                        && !skill.getTags().contains(SkillTag.suicidal))
                        .collect(Collectors.toSet());

        if (!footjobSkills.isEmpty()) {
            return footjobSkills;
        }
        if (!self.outfit.hasNoShoes()) {
            return Collections.singleton(new TakeOffShoes(self));
        }
        
        StandUp standup = new StandUp(self);
        if (allowedSkills.contains(standup)) {
            return Collections.singleton(standup);
        }

        return Collections.emptySet();
    }
    
    @Override
    public CombatStrategy instance() {
        return new FootjobStrategy();
    }

    @Override
    public int initialDuration(Combat c, Character self) {
        return Global.random(2, 6);
    }
}