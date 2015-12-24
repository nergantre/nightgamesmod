package nightgames.modifier.skill;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.skills.Skill;

public class EncourageSkillsModifier extends SkillModifier {

	private final Map<Skill, Double>								absolutes;
	private final Map<Skill, BiFunction<Character, Combat, Double>>	variables;

	public EncourageSkillsModifier(Skill s, double encouragement) {
		absolutes = Collections
				.unmodifiableMap(Collections.singletonMap(s, encouragement));
		variables = Collections.emptyMap();
	}

	public EncourageSkillsModifier(Skill s,
			BiFunction<Character, Combat, Double> encouragementFunc) {
		absolutes = Collections.emptyMap();
		variables = Collections.unmodifiableMap(
				Collections.singletonMap(s, encouragementFunc));
	}

	public EncourageSkillsModifier(Map<Skill, Double> encs) {
		this.absolutes = Collections.unmodifiableMap(encs);
		variables = Collections.emptyMap();
	}

	public EncourageSkillsModifier(Map<Skill, Double> absolutes,
			Map<Skill, BiFunction<Character, Combat, Double>> variables) {
		this.absolutes = Collections.unmodifiableMap(absolutes);
		this.variables = Collections.unmodifiableMap(variables);
	}

	public Map<Skill, Double> encouragedSkills() {
		return absolutes;
	}

	public double encouragement(Skill skill, Combat c, Character user) {
		return absolutes.getOrDefault(skill, 0.0) + variables
				.getOrDefault(skill, (ch, com) -> 0.0).apply(user, c);
	}
	
	// This applies only to npcs anyway
	@Override
	public final boolean playerOnly() {
		return false;
	}
}
