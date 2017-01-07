package nightgames.skills.strategy;

import java.util.HashSet;
import java.util.Set;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.skills.Embrace;
import nightgames.skills.Reversal;
import nightgames.skills.Skill;
import nightgames.skills.SuccubusNurse;
import nightgames.skills.WingWrap;

public class DisablingStrategy extends KnockdownThenActionStrategy {
    @Override
    public double weight(Combat c, Character self) {
        if (!self.has(Trait.SuccubusWarmth)) {
            return Double.MIN_VALUE;
        }
        double weight = 4;
        if (self.has(Trait.Pacification)) {
            weight += 2;
        }
        if (self.has(Trait.DemonsEmbrace)) {
            weight++;
        }
        if (self.has(Trait.VampireWings)) {
            weight += 2;
        }
        
        return weight;
    }

    @Override
    public CombatStrategy instance() {
        return new DisablingStrategy();
    }
    
    @Override
    protected Set<Skill> filterSkills(Combat c, Character self, Set<Skill> allowedSkills) {
        
        Set<Skill> skills = new FuckStrategy().filterSkills(c, self, allowedSkills);
        if (skills.isEmpty()) {
            skills = new HashSet<>();
        }
        
        if (!c.getStance().havingSex(c)) {
            return skills;
        }
        
        skills.add(new Embrace(self));
        
        if (c.getStance().sub(self)) {
            skills.add(new Reversal(self));
        }
        
        if (self.has(Trait.Pacification)) {
            skills.add(new SuccubusNurse(self));
        }
        
        if (self.has(Trait.DemonsEmbrace)) {
            skills.add(new WingWrap(self));
        }
        
        return skills;
    }

}
