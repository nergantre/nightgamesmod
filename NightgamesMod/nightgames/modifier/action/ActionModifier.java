package nightgames.modifier.action;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import nightgames.actions.Action;
import nightgames.characters.Character;
import nightgames.global.Match;

public abstract class ActionModifier {

	public static final ActionModifier NULL_MODIFIER = new ActionModifier(){};

	public Set<Action> bannedActions() {
		return Collections.emptySet();
	}

	public Map<Action, BiPredicate<Character, Match>> conditionalBans() {
		return Collections.emptyMap();
	}

	public boolean actionIsBanned(Action act, Character user, Match match) {
		return bannedActions().contains(act)
				|| (conditionalBans().containsKey(act)
						&& conditionalBans().get(act).test(user, match));
	}
}
