package nightgames.modifier.skill;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nightgames.skills.Tactics;

public class BanTacticsModifier extends SkillModifier {

	private final Set<Tactics> tactics;

	public BanTacticsModifier(Tactics... skills) {
		this.tactics = Collections
				.unmodifiableSet(new HashSet<>(Arrays.asList(skills)));
	}

	@Override
	public Set<Tactics> bannedTactics() {
		return tactics;
	}
	
}
