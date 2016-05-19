package nightgames.nskills.effects;

import java.util.Optional;

import nightgames.nskills.struct.SkillResultStruct;
import nightgames.nskills.tags.SkillRequirement;

public class ConditionalSkillEffect implements SkillEffect {
    private final SkillEffect storedEffect;
    private double value;
    private SkillRequirement requirement;
    private Optional<SkillEffect> elseEffect;

    protected ConditionalSkillEffect(SkillEffect storedEffect) {
        this.storedEffect = storedEffect;
        this.elseEffect = Optional.empty();
        this.requirement = SkillRequirement.noRequirement();
        this.value = 0;
    }
    
    public ConditionalSkillEffect withRequirement(SkillRequirement requirement, double value) {
        this.requirement = requirement;
        this.value = value;
        return this;
    }

    public ConditionalSkillEffect elseUse(SkillEffect elseEffect) {
        this.elseEffect = Optional.ofNullable(elseEffect);
        return this;
    }

    @Override
    public boolean apply(SkillResultStruct results) {
        if (requirement.meets(results, value)) {
            return storedEffect.apply(results);
        } else if (elseEffect.isPresent()) {
            return elseEffect.get().apply(results);
        }
        return false;
    }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }

}
