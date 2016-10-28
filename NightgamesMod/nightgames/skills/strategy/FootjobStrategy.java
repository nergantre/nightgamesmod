package nightgames.skills.strategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Footjob;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.skills.TakeOffShoes;

public class FootjobStrategy extends AbstractStrategy {
    @Override
    public double weight(Combat c, Character self) {
        double weight = 1;
        if (self.getMood().equals(Emotion.dominant)) {
            weight *= 2;
        }
        if (!(new Footjob(self)).requirements(c, self, c.getOther(self))) {
            weight = 0;
        }
        return weight;
    }

    @Override
    protected Set<Skill> filterSkills(Combat c, Character self, Set<Skill> allowedSkills) {
        Character other = c.getOther(self);
        Set<Skill> footjobSkills = allowedSkills.stream()
                        .filter(skill -> skill.getTags().contains(SkillTag.usesFeet)
                                        && !skill.getTags().contains(SkillTag.suicidal))
                        .collect(Collectors.toSet());

        if (!self.outfit.hasNoShoes()) {
            return Collections.singleton(new TakeOffShoes(self));
        }

        if (!footjobSkills.isEmpty()) {
            return footjobSkills;
        }

        Set<Tactics> positioningTactics = new HashSet<>();
        positioningTactics.add(Tactics.damage);
        positioningTactics.add(Tactics.positioning);

        Set<Skill> positioningSkills = allowedSkills.stream()
                        .filter(skill -> positioningTactics.contains(skill.type(c)))
                        .collect(Collectors.toSet());
        if (!c.getStance().mobile(self) || c.getStance().mobile(other)) {
            return positioningSkills;
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