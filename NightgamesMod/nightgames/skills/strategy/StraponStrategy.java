package nightgames.skills.strategy;

import java.util.Collections;
import java.util.Set;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.skills.Skill;
import nightgames.skills.Strapon;

public class StraponStrategy extends FuckStrategy {
    private static final Skill STRAPON_SKILL = new Strapon(Global.noneCharacter());

    @Override
    public double weight(Combat c, Character self) {
        double weight = .25;
        if (!self.has(Item.Strapon) && !self.has(Item.Strapon2)) {
            return 0;
        }
        if (self.getMood().equals(Emotion.dominant)) {
            weight *= 2;
        }
        return weight;
    }
    
    protected Set<Skill> filterSkills(Combat c, Character self, Set<Skill> allowedSkills) {
        if (self.has(Trait.strapped) && allowedSkills.contains(STRAPON_SKILL)) {
            return Collections.singleton(new Strapon(self));
        }
        return super.filterSkills(c, self, allowedSkills);
    }

    @Override
    public CombatStrategy instance() {
        return new StraponStrategy();
    }
}
