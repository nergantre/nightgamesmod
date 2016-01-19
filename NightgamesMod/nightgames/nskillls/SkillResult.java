package nightgames.nskillls;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.custom.effect.CustomEffect;
import nightgames.characters.custom.requirement.CustomRequirement;
import nightgames.combat.Combat;

public class SkillResult {
    Optional<SkillResult> parent;
    Set<SkillTag> userTags, targetTags;
    List<CustomRequirement> requirements;
    List<SkillResult> children;
    String label;
    CustomEffect effect;

    protected SkillResult(CustomEffect effect) {
        this(effect, "", Optional.empty());
    }

    protected SkillResult(CustomEffect effect, String label) {
        this(effect, label, Optional.empty());
    }

    protected SkillResult(CustomEffect effect, String label, Optional<SkillResult> parent) {
        userTags = EnumSet.noneOf(SkillTag.class);
        targetTags = EnumSet.noneOf(SkillTag.class);
        requirements = new ArrayList<>();
        children = new ArrayList<>();
        this.parent = parent;
    }

    public boolean meetsRequirements(Combat c, Character user, Character target) {
        return requirements.stream().allMatch(req -> req.meets(c, user, target));
    }

    /**
     * Get all sub results. If this is a final result, return an empty list.
     * 
     * @return a list of sub skill results
     */
    public List<SkillResult> getSubResults(Combat c, Character user, Character target) {
        return children.stream().filter(child -> child.meetsRequirements(c, user, target)).collect(Collectors.toList());
    }

    public boolean hasUserTag(SkillTag tag) {
        return userTags.contains(tag) || parent.orElse(EmptySkillResult.getEmptyResult()).hasUserTag(tag);
    }

    public boolean hasTargetTag(SkillTag tag) {
        return targetTags.contains(tag) || parent.orElse(EmptySkillResult.getEmptyResult()).hasTargetTag(tag);
    }

    public boolean resolve(Combat c, Character user, Character target) {
        return effect.execute(c, user, target);
    }

    public double getWeight(Combat c, Character user, Character target) {
        return 1;
    }

    public String getLabel() {
        return label;
    }
}
