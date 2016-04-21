package nightgames.nskills.tags;

import nightgames.characters.Attribute;
import nightgames.nskills.struct.SkillResultStruct;

public class AttributeSkillTag extends SkillTag {
    private final Attribute attribute;
    private final String name;
    private SkillRequirement requirement = new SkillRequirement() {
        @Override
        public boolean meets(SkillResultStruct results, double value) {
            return results.getSelf().getCharacter().get(attribute) >= value;
        }
    };
    public AttributeSkillTag(Attribute attribute) {
        this.attribute = attribute;
        this.name = attribute.name() + "Tag";
    }
    @Override
    public SkillRequirement getRequirements() {
        return SkillRequirement.noRequirement();
    }
    @Override
    public SkillRequirement getUsableRequirements() {
        return requirement;
    }
    @Override
    public String getName() {
        return name;
    }
}
