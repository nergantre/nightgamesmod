package nightgames.cmatch.rules;

import java.util.Arrays;
import java.util.function.Predicate;

import nightgames.areas.Area;
import nightgames.cmatch.CustomMatch;
import nightgames.items.Item;
import nightgames.characters.Character;
import nightgames.characters.State;

@FunctionalInterface
public interface StopRule extends Predicate<CustomMatch> {

	static StopRule timeout(int limit) {
		return m -> m.getRoundCount() >= limit;
	}
	
	static StopRule scoreReached(int score) {
		return m -> m.getScores().values().stream().anyMatch(s -> s >= score);
	}
	
	static StopRule scoreReached(Character ch, int score) {
		return m -> m.getScores().get(ch) >= score;
	}
	
	static StopRule characterQuit(Character ch) {
		return m -> ch.state == State.quit;
	}
	
	static StopRule characterInLocation(Character ch, Area loc) {
		return m -> loc.present.contains(ch);
	}
	
	static StopRule itemInLocation(Item item, Area loc) {
		return m -> loc.present.stream().anyMatch(ch -> ch.has(item));
	}
	
	static StopRule characterHasItem(Character ch, Item item, int count) {
		return m -> ch.getInventory().get(item) >= count;
	}
	
	static StopRule characterHasItem(Character ch, Item item) {
		return characterHasItem(ch, item, 1);
	}
	
	static StopRule any(StopRule...rules) {
		if (rules.length == 0)
			return m -> true;
		return Arrays.asList(rules).stream().reduce(m -> true, (r1, r2) -> (StopRule) r1.or(r2));
	}
	
	static StopRule all(StopRule...rules) {
		if (rules.length == 0)
			return m -> false;
		return Arrays.asList(rules).stream().reduce(m -> true, (r1, r2) -> (StopRule) r1.and(r2));
	}
}
