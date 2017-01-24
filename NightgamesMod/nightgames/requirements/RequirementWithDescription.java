package nightgames.requirements;

public class RequirementWithDescription {
    private final Requirement requirement;
    private final String description;

    public RequirementWithDescription(Requirement requirement, String description) {
        this.requirement = requirement;
        this.description = description;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public String getDescription() {
        return description;
    }
}