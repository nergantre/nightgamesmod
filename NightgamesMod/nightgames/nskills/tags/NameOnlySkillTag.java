package nightgames.nskills.tags;

public class NameOnlySkillTag extends SkillTag {
    private final String name;
    public NameOnlySkillTag(String name) {
        this.name = name;
    }
    @Override
    public SkillRequirement getRequirements() {
        return SkillRequirement.noRequirement();
    }
    @Override
    public SkillRequirement getUsableRequirements() {
        return SkillRequirement.noRequirement();
    }
    @Override
    public String getName() {
        return name;
    }
}
