package nightgames.nskills;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.custom.requirement.CustomRequirement;
import nightgames.combat.Combat;

public class GenericSkill implements SkillInterface {
    List<SkillResult> results;
    List<CustomRequirement> requirements;
    List<CustomRequirement> usable;
    String label;

    public GenericSkill(String label) {
        this.results = new ArrayList<>();
        this.requirements = new ArrayList<>();
        this.usable = new ArrayList<>();
        this.label = label;
    }

    @Override
    public List<SkillResult> getPossibleResults(Combat c, Character user, Character target, int roll) {
        List<SkillResult> possibleResults = new ArrayList<>();
        Deque<SkillResult> edges = new ArrayDeque<>(results.stream()
                        .filter(res -> res.meetsRequirements(c, user, target, roll)).collect(Collectors.toList()));
        while (!edges.isEmpty()) {
            SkillResult res = edges.pop();
            Collection<SkillResult> subResults = res.getSubResults(c, user, target, roll);
            // only add to the possible result list if the result is a leaf result.
            if (subResults.isEmpty()) {
                possibleResults.add(res);
            }
        }
        return possibleResults;
    }

    /**
     * affects whether this skill is even selectable
     */
    @Override
    public boolean isSelectable(Combat c, Character user, Character target) {
        return usable.stream().allMatch(req -> req.meets(c, user, target));
    }

    public void addRequirement(CustomRequirement requirement) {
        requirements.add(requirement);
    }

    public void addUsableRequirement(CustomRequirement requirement) {
        usable.add(requirement);
    }

    public void addResult(SkillResult result) {
        results.add(result);
    }

    public String getName() {
        return label;
    }
}
