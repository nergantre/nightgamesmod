package nightgames.nskills.effects;

import nightgames.nskills.struct.SkillResultStruct;

public interface SkillEffect {
    static class Holder {
        private static final SkillEffect INSTANCE = new SkillEffect() {
            @Override
            public boolean apply(SkillResultStruct results) {
                return true;
            }

            @Override
            public String getType() {
                return "none";
            }
        };
    }

    public static SkillEffect noEffect() {
        return Holder.INSTANCE;
    }

    boolean apply(SkillResultStruct results);

    String getType();

    default SkillEffect reversed() {
        return new ReversedEffect(this);
    }
}
