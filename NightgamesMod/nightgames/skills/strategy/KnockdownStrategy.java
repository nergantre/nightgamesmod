package nightgames.skills.strategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class KnockdownStrategy extends AbstractStrategy {
    @Override
    public double weight(Combat c, Character self) {
        double weight = 1;
        if (self.getMood().equals(Emotion.angry) || self.getMood().equals(Emotion.dominant)) {
            weight *= 2;
        }
        if (self.has(Trait.submissive)) {
            weight *= .3;
        }
        return weight;
    }

    @Override
    protected Set<Skill> filterSkills(Combat c, Character self, Set<Skill> allowedSkills) {
        Character other = c.getOther(self);
        if (c.getStance().dom(self)) {
            return Collections.emptySet();
        }
        Set<Tactics> positioningTactics = new HashSet<>();
        positioningTactics.add(Tactics.damage);
        positioningTactics.add(Tactics.positioning);

        Set<Skill> positioningSkills = allowedSkills.stream().filter(skill -> positioningTactics.contains(skill.type(c))).collect(Collectors.toSet());
        if (!c.getStance().mobile(self) || c.getStance().mobile(other)) {
            return positioningSkills;
        }
        return Collections.emptySet();
    }
    
    @Override
    public CombatStrategy instance() {
        return new KnockdownStrategy();
    }

    @Override
    public int initialDuration(Combat c, Character self) {
        return Global.random(3, 5);
    }
}