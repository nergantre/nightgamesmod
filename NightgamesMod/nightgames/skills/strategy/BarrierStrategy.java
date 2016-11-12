package nightgames.skills.strategy;

import java.util.Collections;
import java.util.Set;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Barrier;
import nightgames.skills.Skill;
import nightgames.status.Stsflag;

public class BarrierStrategy extends AbstractStrategy {
    private static final Barrier BARRIER = new Barrier(Global.noneCharacter());
    
    @Override
    public double weight(Combat c, Character self) {
        double weight = 3;
        if (self.is(Stsflag.shielded) || BARRIER.requirements(c, self, c.getOpponent(self))) {
            return 0;
        }
        return weight;
    }

    @Override
    protected Set<Skill> filterSkills(Combat c, Character self, Set<Skill> allowedSkills) {
        if (self.is(Stsflag.shielded)) {
            return Collections.emptySet();
        }
        if (allowedSkills.contains(BARRIER)) {
            return Collections.singleton(new Barrier(self));
        }
        return Collections.emptySet();
    }
    
    @Override
    public CombatStrategy instance() {
        return new BarrierStrategy();
    }

    @Override
    public int initialDuration(Combat c, Character self) {
        return Global.random(3, 5);
    }
}