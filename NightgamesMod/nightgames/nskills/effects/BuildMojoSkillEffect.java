package nightgames.nskills.effects;

import java.util.ArrayList;
import java.util.List;

import nightgames.nskills.effects.rolls.EffectNumberRoll;
import nightgames.nskills.struct.SkillResultStruct;

public class BuildMojoSkillEffect implements SkillEffect {

    private final List<EffectNumberRoll> rolls;

    protected BuildMojoSkillEffect() {
        rolls = new ArrayList<>();
    }

    public BuildMojoSkillEffect addRoll(EffectNumberRoll roll) {
        rolls.add(roll);
        return this;
    }

    @Override
    public String getType() {
        return "build_mojo";
    }

    @Override
    public boolean apply(SkillResultStruct results) {
        double total = rolls.stream()
                        .mapToDouble(roll -> roll.roll(results))
                        .sum();
        results.getSelf().getCharacter().buildMojo(results.getCombat(), (int) Math.round(total));
        return true;
    }
}
