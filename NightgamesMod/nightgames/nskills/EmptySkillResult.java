package nightgames.nskills;

import nightgames.nskills.tags.SkillTag;

public class EmptySkillResult extends SkillResult {
    protected EmptySkillResult(SkillInterface skill) {
        super();
    }

    private static class Holder {
        private static final SkillResult INSTANCE = new EmptySkillResult(EmptySkill.getInstance());
    }
    @Override
    public boolean hasUserTag(SkillTag tag) {
        return false;
    }

    @Override
    public boolean hasTargetTag(SkillTag tag) {
        return false;
    }

    public static SkillResult getEmptyResult() {
        return Holder.INSTANCE;
    }

}
