package nightgames.modifier.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import nightgames.actions.Action;
import nightgames.characters.Character;
import nightgames.global.Match;

public abstract class ActionModifier {

	public static final List<ActionModifier> TYPES = Collections.singletonList(new BanActionModifier());
	public static final ActionModifier NULL_MODIFIER = new ActionModifier(){
		@Override
		public String toString() {
			return "null-action-modifier";
		}};

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
	
	public ActionModifier andThen(ActionModifier other) {
		ActionModifier me = this;
		return new ActionModifier() {
			@Override
			public Set<Action> bannedActions() {
				Set<Action> actions = new HashSet<>(me.bannedActions());
				actions.addAll(other.bannedActions());
				return Collections.unmodifiableSet(actions);
			}
			@Override
			public Map<Action, BiPredicate<Character, Match>> conditionalBans() {
				Map<Action, BiPredicate<Character, Match>> actions = new HashMap<>(me.conditionalBans());
				actions.putAll(other.conditionalBans());
				return Collections.unmodifiableMap(actions);
			}
			@Override
			public String toString() {
				return me.toString() + other.toString();
			}			
		};
	}
	
	public static ActionModifier allOf(ActionModifier...modifiers) {
		if (modifiers.length == 0)
			 return NULL_MODIFIER;
		ActionModifier mod = modifiers[0];
		for (int i = 1; i < modifiers.length; i++) {
			mod = mod.andThen(modifiers[i]);
		}
		return mod;
	}

	public abstract String toString();
}
