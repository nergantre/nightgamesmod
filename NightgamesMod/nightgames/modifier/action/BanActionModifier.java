package nightgames.modifier.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import org.json.simple.JSONObject;

import nightgames.actions.Action;
import nightgames.characters.Character;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.global.Match;
import nightgames.modifier.ModifierComponent;

public class BanActionModifier extends ActionModifier implements ModifierComponent<BanActionModifier> {

    private final Set<Action> absolutes;
    private final Map<Action, BiPredicate<Character, Match>> conditionals;

    public BanActionModifier(Action... actions) {
        absolutes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(actions)));
        conditionals = Collections.emptyMap();
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

    @Override
    public String name() {
        return "ban-action";
    }

    @Override
    public String toString() {
        return "Banned:" + absolutes.toString();
    }

    @Override
    public BanActionModifier instance(JSONObject obj) {
        if (obj.containsKey("action")) {
            String name = JSONUtils.readString(obj, "action");
            Action act = identify(name).orElseThrow(() -> new IllegalArgumentException("No such action: " + name));
            return new BanActionModifier(act);
        } else if (obj.containsKey("actions")) {
            List<String> names = JSONUtils.loadStringsFromArr(obj, "actions");
            Action[] acts = names.stream().map(this::identify).toArray(Action[]::new);
            return new BanActionModifier(acts);
        }
        throw new IllegalArgumentException("Invalid ban-action; it must have 'action' or 'actions'.");
    }

    private Optional<Action> identify(String name) {
        return Global.getActions().stream().filter(a -> a.getClass().getSimpleName().equals(name)).findAny();
    }
}
