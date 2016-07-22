package nightgames.modifier.skill;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nightgames.global.Global;
import nightgames.json.JsonUtils;
import nightgames.modifier.ModifierComponentLoader;
import nightgames.skills.Skill;

public class BanSkillsModifier extends SkillModifier implements ModifierComponentLoader<SkillModifier> {
    private static final String name = "ban-skills";

    private final Set<Skill> skills;

    public BanSkillsModifier(Skill... skills) {
        this.skills = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(skills)));
    }

    public BanSkillsModifier(Collection<Skill> skills) {
        this(skills.toArray(new Skill[] {}));
    }

    @Override
    public Set<Skill> bannedSkills() {
        return skills;
    }

    @Override
    public String name() {
        return name;
    }

    @Override public BanSkillsModifier instance(JsonObject object) {
        Collection<Skill> skillPool = Global.getSkillPool();
        Optional<String> maybeName = JsonUtils.getOptional(object, "skill").map(JsonElement::getAsString);
        if (maybeName.isPresent()) {
            String name = maybeName.get();
            Skill skill = skillPool.stream().filter(matchName(name)).findAny()
                            .orElseThrow(() -> new IllegalArgumentException("No such skill: " + name));
            return new BanSkillsModifier(skill);
        }
        Collection<Skill> skills = JsonUtils.getOptionalArray(object, "skills")
                        .map(array -> JsonUtils.collectionFromJson(array, Skill.class))
                        .orElseThrow(() -> new IllegalArgumentException(
                                        "\"ban-skills\" must have \"skill\" or \"skills\""));

        return new BanSkillsModifier(skills);
    }

    private Predicate<Skill> matchName(String name) {
        return skill -> name.equals(skill.getName());
    }

    @Override
    public String toString() {
        return "Banned:" + skills.toString();
    }
}
