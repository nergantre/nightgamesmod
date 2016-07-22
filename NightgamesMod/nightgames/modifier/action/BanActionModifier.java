package nightgames.modifier.action;

import java.util.*;
import java.util.function.BiPredicate;

import com.google.gson.JsonObject;

import nightgames.actions.Action;
import nightgames.characters.Character;
import nightgames.global.Global;
import nightgames.json.JsonUtils;
import nightgames.global.Match;
import nightgames.modifier.ModifierComponentLoader;

public class BanActionModifier extends ActionModifier implements ModifierComponentLoader<ActionModifier> {
    private static final String name = "ban-action";

    private final Set<Action> absolutes;
    private final Map<Action, BiPredicate<Character, Match>> conditionals;

    public BanActionModifier(Action... actions) {
        absolutes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(actions)));
        conditionals = Collections.emptyMap();
    }

    public BanActionModifier(Collection<Action> actions) {
        this(actions.toArray(new Action[] {}));
    }

    public BanActionModifier(Action act, BiPredicate<Character, Match> pred) {
        absolutes = Collections.emptySet();
        conditionals = Collections.singletonMap(act, pred);
    }

    public BanActionModifier(Set<Action> absolutes, Map<Action, BiPredicate<Character, Match>> conditionals) {
        this.absolutes = absolutes;
        this.conditionals = conditionals;
    }

    @Override
    public Set<Action> bannedActions() {
        return absolutes;
    }

    @Override
    public Map<Action, BiPredicate<Character, Match>> conditionalBans() {
        return conditionals;
    }

    @Override public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "Banned:" + absolutes.toString();
    }

    @Override public BanActionModifier instance(JsonObject object) {
        if (object.has("action")) {
            String name = object.get("action").getAsString();
            Action act = identify(name).orElseThrow(() -> new IllegalArgumentException("No such action: " + name));
            return new BanActionModifier(act);
        } else if (object.has("actions")) {
            Collection<Action> acts = JsonUtils.collectionFromJson(object.getAsJsonArray("actions"), Action.class);
            return new BanActionModifier(acts);
        }
        throw new IllegalArgumentException("Invalid ban-action; it must have 'action' or 'actions'.");
    }

    private Optional<Action> identify(String name) {
        return Global.getActions().stream().filter(a -> a.getClass().getSimpleName().equals(name)).findAny();
    }
}
