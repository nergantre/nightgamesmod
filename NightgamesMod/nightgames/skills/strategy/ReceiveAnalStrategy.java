package nightgames.skills.strategy;

import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.AssFuck;
import nightgames.skills.Skill;

public class ReceiveAnalStrategy extends AbstractStrategy {

    @Override
    public double weight(Combat c, Character self) {
        double weight = -3;
        if (self.getMood().equals(Emotion.horny)) {
            weight = 1;
        }
        if (self.has(Trait.drainingass)) {
            weight += 2;
        }
        if (self.has(Trait.bewitchingbottom)) {
            weight += 1;
        }
        if (self.has(Trait.powerfulcheeks) && weight > 0) {
            weight += 1;
        }
        if (weight > 0 && new AssFuck(c.getOpponent(self)).usable(c, self)) {
            weight *= 1.5;
        }
        return weight;
    }
    
    @Override
    protected Set<Skill> filterSkills(Combat c, Character self, Set<Skill> allowedSkills) {
        if (c.getStance().anallyPenetrated(c, self)) {
            return new FuckStrategy().filterSkills(c, self, allowedSkills);
        }
        Set<Skill> anal = allowedSkills.stream().filter(s -> s.getTags().contains(SkillTag.anal)).collect(Collectors.toSet());
        if (anal.isEmpty()) {
            if (new AssFuck(c.getOpponent(self)).usable(c, self)) {
                return new UseHandsStrategy().filterSkills(c, self, allowedSkills);
            }
            return new FuckStrategy().filterSkills(c, self, allowedSkills);
        }
        return anal;
    }

    @Override
    public int initialDuration(Combat c, Character self) {
        return Global.random(3, 6);
    }

    @Override
    public CombatStrategy instance() {
        return new ReceiveAnalStrategy();
    }
    
}
