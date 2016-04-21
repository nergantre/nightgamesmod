package nightgames.nskills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.nskills.effects.SkillEffect;
import nightgames.nskills.struct.SkillResultStruct;
import nightgames.nskills.struct.CharacterResultStruct;
import nightgames.nskills.struct.NormalSkillResultStruct;
import nightgames.nskills.tags.SkillRequirement;
import nightgames.nskills.tags.SkillTag;

public class SkillResult {
    private final Map<SkillTag, Double> userTags, targetTags;
    private final Map<SkillRequirement, Double> requirements;
    private final List<SkillResult> children;
    private final List<SkillEffect> effects;
    private String label;
    private int priority;
    private boolean success;

    protected SkillResult(Optional<SkillResult> parent) {
        if (parent.isPresent()) {
            SkillResult parentResult = parent.get();
            userTags = new HashMap<>(parentResult.userTags);
            targetTags = new HashMap<>(parentResult.targetTags);
            requirements = new HashMap<>(parentResult.requirements);
            children = new ArrayList<>(parentResult.children);
            effects = new ArrayList<>(parentResult.effects);
            this.label = parentResult.label;
            this.priority = parentResult.priority;
            this.success = parentResult.success;
        } else {
            userTags = new HashMap<>();
            targetTags = new HashMap<>();
            requirements = new HashMap<>();
            children = new ArrayList<>();
            effects = new ArrayList<>();
            this.label = "";
            this.priority = -1;
            this.success = true;
        }
    }

    protected SkillResult() {
        this(Optional.empty());
    }

    private boolean meetsRequirements(Map<SkillTag, Double> tags, SkillResultStruct results) {
        return tags.entrySet().stream().allMatch(entry -> entry.getKey().getRequirements().meets(results, entry.getValue()));
    }

    private boolean meetsUsableRequirements(Map<SkillTag, Double> tags, SkillResultStruct results) {
        return tags.entrySet().stream().allMatch(entry -> entry.getKey().getUsableRequirements().meets(results, entry.getValue()));
    }

    public boolean meetsRequirements(Combat c, Character user, Character target, int roll) {
        SkillResultStruct results = createResultStruct(c, user, target, roll);
        return requirements.entrySet().stream().allMatch(entry -> entry.getKey().meets(results, entry.getValue()))
                        && meetsRequirements(userTags, results)
                        && meetsRequirements(targetTags, results.reversed());
    }

    public boolean meetsUsableRequirements(Combat c, Character user, Character target, int roll) {
        SkillResultStruct results = createResultStruct(c, user, target, roll);
        return requirements.entrySet().stream().allMatch(entry -> entry.getKey().meets(results, entry.getValue()))
                        && meetsUsableRequirements(userTags, results)
                        && meetsUsableRequirements(targetTags, results.reversed());
    }

    public SkillResult addRequirement(SkillRequirement req, double value) {
        requirements.put(req, value);
        return this;
    }

    public SkillResult createChildResult() {
        SkillResult newResult = new SkillResult(Optional.of(this));
        children.add(newResult);
        return newResult;
    }

    public SkillResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public SkillResult setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public SkillResult setLabel(String label) {
        this.label = label;
        return this;
    }
    
    public boolean getSuccess() {
        return success;
    }

    public SkillResult addUserEffect(SkillEffect effect) {
        effects.add(effect);
        return this;
    }

    public SkillResult addTargetEffect(SkillEffect effect) {
        effects.add(effect.reversed());
        return this;
    }

    public SkillResult addUserTag(SkillTag tag, double value) {
        userTags.put(tag, value);
        return this;
    }

    public SkillResult addUserTag(SkillTag tag) {
        return addUserTag(tag, 0);
    }

    public SkillResult addTargetTag(SkillTag tag, double value) {
        targetTags.put(tag, value);
        return this;
    }

    public SkillResult addTargetTag(SkillTag tag) {
        return addTargetTag(tag, 0);
    }

    /**
     * Get all sub results. If this is a final result, return an empty list.
     * @param roll TODO
     * 
     * @return a list of sub skill results
     */
    public List<SkillResult> getSubResults(Combat c, Character user, Character target, int roll) {
        return children.stream().filter(child -> child.meetsRequirements(c, user, target, roll)).collect(Collectors.toList());
    }

    public boolean hasUserTag(SkillTag tag) {
        return userTags.containsKey(tag);
    }

    public boolean hasTargetTag(SkillTag tag) {
        return targetTags.containsKey(tag);
    }

    public Set<BodyPart> getUserParts(Character user) {
        return userTags.keySet().stream()
                        .filter(tag -> tag.getBodyPartType().isPresent())
                        .map(tag -> user.body.getRandom(tag.getBodyPartType().get()))
                        .collect(Collectors.toSet());
    }

    public Set<BodyPart> getTargetParts(Character target) {
        return targetTags.keySet().stream()
                        .filter(tag -> tag.getBodyPartType().isPresent())
                        .map(tag -> target.body.getRandom(tag.getBodyPartType().get()))
                        .collect(Collectors.toSet());
    }

    public SkillResultStruct createResultStruct(Combat c, Character user, Character target, int roll) {
        Set<BodyPart> userParts = getUserParts(user);
        Set<BodyPart> targetParts = getTargetParts(target);
        CharacterResultStruct userStruct = new CharacterResultStruct(userParts, user);
        CharacterResultStruct targetStruct = new CharacterResultStruct(targetParts, target);
        return new NormalSkillResultStruct(c, userStruct, targetStruct, this, roll);
    }

    public boolean resolve(Combat c, Character user, Character target, int roll) {
        boolean failed = false;
        SkillResultStruct results = createResultStruct(c, user, target, roll);
        for (SkillEffect effect : effects) {
            failed |= effect.apply(results);
        }
        return failed;
    }

    public double getWeight(Combat c, Character user, Character target) {
        return 1;
    }

    public int getPriority() {
        return priority;
    }

    public String getLabel() {
        return label;
    }

    public Set<Attribute> getAllAttributes() {
        return userTags.keySet().stream().filter(tag -> tag.getAttribute().isPresent()).map(tag -> tag.getAttribute().get()).collect(Collectors.toSet());
    }
}
