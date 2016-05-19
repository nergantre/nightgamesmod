package nightgames.nskills.effects;

import nightgames.nskills.struct.SkillResultStruct;

public class ReversedEffect implements SkillEffect {
    private final SkillEffect innerEffect;
    protected ReversedEffect(SkillEffect innerEffect) {
        this.innerEffect = innerEffect;
    }

    @Override
    public boolean apply(SkillResultStruct results) {
        return innerEffect.apply(results.reversed());
    }

    @Override
    public String getType() {
        return innerEffect.getType();
    }
}
