package nightgames.modifier.skill;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nightgames.skills.Skill;

public class BanSkillsModifier extends SkillModifier {

	private final Set<Skill> skills;

	public BanSkillsModifier(Skill... skills) {
		this.skills = Collections
				.unmodifiableSet(new HashSet<>(Arrays.asList(skills)));
	}

	@Override
	public Set<Skill> bannedSkills() {
		return skills;
	}
}
