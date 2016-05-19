package nightgames.nskills;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import nightgames.characters.Character;
import nightgames.characters.custom.requirement.CustomRequirement;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.struct.CharacterResultStruct;
import nightgames.nskills.struct.NormalSkillResultStruct;
import nightgames.nskills.struct.SkillResultStruct;

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

    private List<SkillResult> getPossibleResults(Combat c, Character user, Character target, double roll) {
        return filterResults(result -> result.meetsUsableRequirements(c, user, target, roll));
    }

    @Override
    public Optional<SkillResult> getHighestPriorityUsableResult(Combat c, Character user, Character target) {
        return filterResults(result -> result.meetsUsableRequirements(c, user, target, 0)).stream()
                                                                                          .max((a, b) -> Integer.compare(
                                                                                                          a.getPriority(),
                                                                                                          b.getPriority()));
    }

    private List<SkillResult> filterResults(Predicate<SkillResult> predicate) {
        List<SkillResult> possibleResults = new ArrayList<>();
        Deque<SkillResult> edges = new ArrayDeque<>(results);
        while (!edges.isEmpty()) {
            SkillResult res = edges.pop();
            if (predicate.test(res) && res.getPriority() >= 0) {
                possibleResults.add(res);
            }
            res.getChildren()
               .forEach(child -> edges.push(child));
        }
        return possibleResults;
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

    @Override
    public boolean resolve(Combat c, Character user, Character target) {
        double roll = Global.randomdouble();
        Optional<SkillResult> maybeResults = getPossibleResults(c, user, target, roll).stream().max((a, b) -> Integer.compare(a.getPriority(), b.getPriority()));
        if (!maybeResults.isPresent() || maybeResults.get().getPriority() < 0) {
            c.write(user, Global.format("{self:NAME-POSSESSIVE} %s failed.", user, target, getName()));
            return false;
        }
        return maybeResults.get().resolve(c, user, target, roll);
    }
}
