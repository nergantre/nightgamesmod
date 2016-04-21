package nightgames.nskills.tags;

public class BasicSkillTag extends SkillTag {
    private final SkillRequirement req;
    private final SkillRequirement usableReq;

    private final String name;

    public BasicSkillTag(SkillRequirement req, SkillRequirement usableReq, String name) {
        this.req = req;
        this.usableReq = usableReq;
        this.name = name;
    }
    @Override
    public SkillRequirement getRequirements() {
        return req;
    }
    @Override
    public SkillRequirement getUsableRequirements() {
        return usableReq;
    }
    @Override
    public String getName() {
        return name;
    }
}
