package nightgames.nskills.effects.rolls;

import java.util.Random;

import nightgames.global.Global;
import nightgames.nskills.struct.SkillResultStruct;

public class BasicNumberRoll implements EffectNumberRoll {
    private final double from;
    private final double to;
    private final long seed;

    public BasicNumberRoll(double from, double to) {
        if (from > to) {
            double temp = from;
            from = to;
            to = temp;
        }
        this.from = from;
        this.to = to;
        seed = Global.randomlong();
    }

    @Override
    public double roll(SkillResultStruct result) {
        Random rng = new Random(Double.doubleToLongBits(result.getRoll()) ^ seed);
        return from + rng.nextDouble() * (to - from);
    }
}
