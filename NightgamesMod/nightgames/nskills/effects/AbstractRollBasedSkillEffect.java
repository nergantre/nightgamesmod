package nightgames.nskills.effects;

import java.util.ArrayList;
import java.util.List;

import nightgames.nskills.effects.rolls.EffectNumberRoll;
import nightgames.nskills.struct.SkillResultStruct;

public abstract class AbstractRollBasedSkillEffect implements SkillEffect {
    interface EffectCall {
        public boolean apply(SkillResultStruct results, double roll);
    }
    private final List<EffectNumberRoll> rolls;
    private final EffectCall effect;
    protected AbstractRollBasedSkillEffect(EffectCall effect) {
        this.rolls = new ArrayList<>();
        this.effect = effect;
    }

    public AbstractRollBasedSkillEffect addRoll(EffectNumberRoll roll) {
        rolls.add(roll);
        return this;
    }



    @Override
    public boolean apply(SkillResultStruct results) {
        double total = rolls.stream()
                            .mapToDouble(roll -> roll.roll(results))
                            .sum();

        return effect.apply(results, total);
    }
}
