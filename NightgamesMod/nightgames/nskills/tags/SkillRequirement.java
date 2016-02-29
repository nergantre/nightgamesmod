package nightgames.nskills.tags;

import nightgames.nskills.struct.SkillResultStruct;

public interface SkillRequirement {
    static class Holder {
        private static final SkillRequirement INSTANCE = new SkillRequirement() {
            @Override
            public boolean meets(SkillResultStruct results, double value) {
                return true;
            }
        };
    }

    public static SkillRequirement noRequirement() {
        return Holder.INSTANCE;
    }
    boolean meets(SkillResultStruct results, double value);
}
