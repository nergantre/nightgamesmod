package nightgames.modifier.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import nightgames.actions.Action;
import nightgames.characters.Character;
import nightgames.global.Match;

public class BanActionModifier extends ActionModifier {

	private final Set<Action>									absolutes;
	private final Map<Action, BiPredicate<Character, Match>>	conditionals;

	public BanActionModifier(Action... actions) {
		this.absolutes = Collections
				.unmodifiableSet(new HashSet<>(Arrays.asList(actions)));
		this.conditionals = Collections.emptyMap();
	}
	
	public BanActionModifier(Action act, BiPredicate<Character, Match> pred) {
		this.absolutes = Collections.emptySet();
		this.conditionals = Collections.singletonMap(act, pred);
	}

	public BanActionModifier(Set<Action> absolutes,
			Map<Action, BiPredicate<Character, Match>> conditionals) {
		this.absolutes = absolutes;
		this.conditionals = conditionals;
	}

	public Set<Action> bannedActions() {
		return absolutes;
	}
	
	public Map<Action, BiPredicate<Character, Match>> conditionalBans() {
		return conditionals;
	}
}
