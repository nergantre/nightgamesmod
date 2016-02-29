package nightgames.nskills.tags;

/**
 * Requirement skilltag where both the skill usable and normal requirements are the same.
 */
public class BothRequirementSkillTag extends BasicSkillTag {
    public BothRequirementSkillTag(SkillRequirement req, String name) {
        super(req, req, name);
    }
}
