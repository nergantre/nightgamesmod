package nightgames.skills.strategy;

import java.util.Collections;
import java.util.Optional;
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
        if (!(new Footjob(self)).requirements(c, self, c.getOpponent(self))) {
            return 0;
        }
        if (c.getOpponent(self).has(Trait.footfetishist)) {
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
    protected Optional<Set<Skill>> getPreferredSkills(Combat c, Character self, Set<Skill> allowedSkills) {
        Set<Skill> footjobSkills = allowedSkills.stream()
                        .filter(skill -> (skill.getTags(c).contains(SkillTag.usesFeet))
                                        && !skill.getTags(c).contains(SkillTag.suicidal))
                        .collect(Collectors.toSet());

        if (!footjobSkills.isEmpty()) {
            return Optional.of(footjobSkills);
        }
        if (!c.getOpponent(self).crotchAvailable()) {
            Set<Skill> strippingSkills = allowedSkills.stream()
                            .filter(skill -> (skill.getTags(c).contains(SkillTag.stripping))
                                            && !skill.getTags(c).contains(SkillTag.suicidal))
                            .collect(Collectors.toSet());
            return Optional.of(strippingSkills);
        }
        
        if (!self.outfit.hasNoShoes()) {
            return Optional.of(Collections.singleton(new TakeOffShoes(self)));
        }

        StandUp standup = new StandUp(self);
        if (allowedSkills.contains(standup)) {
            return Optional.of(Collections.singleton(standup));
        }

        return Optional.empty();
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