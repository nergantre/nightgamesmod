package nightgames.nskills.tags;

import nightgames.nskills.struct.SkillResultStruct;

public class CooldownSkillTag extends SkillTag {
    private final SkillRequirement requirement = new SkillRequirement() {
        @Override
        public boolean meets(SkillResultStruct results, double value) {
            // TODO
            return true;
        }
    };

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
        return "Cooldown";
    }
}
