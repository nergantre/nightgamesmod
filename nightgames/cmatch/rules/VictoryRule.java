package nightgames.cmatch.rules;

import java.util.Optional;
import java.util.function.Function;

import nightgames.characters.Character;
import nightgames.cmatch.CustomMatch;

@FunctionalInterface
public interface VictoryRule extends Function<CustomMatch, Optional<Character>>{
	Optional<Character> apply(CustomMatch match);
}
