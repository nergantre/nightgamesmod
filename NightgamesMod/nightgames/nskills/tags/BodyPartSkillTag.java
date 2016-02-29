package nightgames.nskills.tags;

import java.util.Optional;

import nightgames.global.Global;
import nightgames.nskills.struct.SkillResultStruct;

public class BodyPartSkillTag extends SkillTag {
    private final String type;
    private final String name;
    private final SkillRequirement requirement = new SkillRequirement() {
        @Override
        public boolean meets(SkillResultStruct results, double value) {
            return results.getSelf().getCharacter().body.has(type);
        }
    };
    public BodyPartSkillTag(String type) {
        this.type = type;
        this.name = "Uses"+Global.capitalizeFirstLetter(type);
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

    public Optional<String> getBodyPartType() {
        return Optional.of(type);
    }
}
