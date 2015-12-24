package nightgames.modifier.skill;

import java.util.function.BiFunction;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class EncourageTacticsModifier extends SkillModifier {

	private final Tactics								modified;
	private final BiFunction<Character, Combat, Double>	weight;

	public EncourageTacticsModifier(Tactics modified,
			BiFunction<Character, Combat, Double> weight) {
		this.modified = modified;
		this.weight = weight;
	}
	
	public EncourageTacticsModifier(Tactics modified, double weight) {
		this.modified = modified;
		this.weight = (ch, com) -> weight;
	}
	
	@Override
	public double encouragement(Skill skill, Combat c, Character user) {
		return skill.type(c) == modified ? weight.apply(user, c) : 0.0;
	}
	
	// This applies only to npcs anyway
	@Override
	public final boolean playerOnly() {
		return false;
	}

}
