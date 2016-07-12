package nightgames.modifier.skill;

import java.util.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nightgames.json.JsonUtils;
import nightgames.modifier.ModifierComponentLoader;
import nightgames.skills.Tactics;

public class BanTacticsModifier extends SkillModifier implements ModifierComponentLoader<SkillModifier> {
    private static final String name = "ban-tactic";

    private final Set<Tactics> tactics;

    public BanTacticsModifier(Tactics... skills) {
        tactics = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(skills)));
    }

    @Override
    public Set<Tactics> bannedTactics() {
        return tactics;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public BanTacticsModifier instance(JsonObject object) {
        Optional<BanTacticsModifier> maybeBan =
                        JsonUtils.getOptional(object, "tactic").map(JsonElement::getAsString).map(Tactics::valueOf)
                                        .map(BanTacticsModifier::new);
        if (maybeBan.isPresent()) {
            return maybeBan.get();
        }
        Optional<Tactics[]> maybeTactics = JsonUtils.getOptionalArray(object, "tactics")
                        .map(array -> JsonUtils.collectionFromJson(array, Tactics.class))
                        .map(c -> c.toArray(new Tactics[] {}));
        return new BanTacticsModifier(maybeTactics.orElseThrow(
                        () -> new IllegalArgumentException("'ban-tactics' must have 'tactic' or 'tactics'")));
    }

    @Override
    public String toString() {
        return "Banned:" + tactics.toString();
    }
}
