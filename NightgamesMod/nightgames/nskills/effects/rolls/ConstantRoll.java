package nightgames.nskills.effects.rolls;

import nightgames.nskills.struct.SkillResultStruct;

public class ConstantRoll implements EffectNumberRoll {
    private final double number;

    public ConstantRoll(double number) {
        this.number = number;
    }

    @Override
    public double roll(SkillResultStruct result) {
        return number;
    }
}
