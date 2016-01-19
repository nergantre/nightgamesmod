package nightgames.nskillls;

public class EmptySkillResult extends SkillResult {
    protected EmptySkillResult() {
        super((c, user, target) -> false, "ERROR:EMPTY_SKILL");
    }

    public static final SkillResult EMPTY_SKILL_RESULT = new EmptySkillResult();

    @Override
    public boolean hasUserTag(SkillTag tag) {
        return false;
    }

    @Override
    public boolean hasTargetTag(SkillTag tag) {
        return false;
    }

    public static SkillResult getEmptyResult() {
        return EMPTY_SKILL_RESULT;
    }

}
