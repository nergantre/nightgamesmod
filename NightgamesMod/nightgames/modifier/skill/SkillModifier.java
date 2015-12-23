package nightgames.modifier.skill;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.characters.Character;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public abstract class SkillModifier {

	public static final SkillModifier NULL_MODIFIER = new SkillModifier() {
		
	};
	
	public Set<Skill> bannedSkills() {
		return Collections.emptySet();
	}
	
	public Set<Tactics> bannedTactics() {
		return Collections.emptySet();
	}
	
	public Map<Skill, Double> encouragedSkills() {
		return Collections.emptyMap();
	}
	
	public boolean playerOnly() {
		return true;
	}
	
	public Set<Skill> allowedSkills(Combat c) {
		Set<Skill> skills = new HashSet<>(Global.getSkillPool());
		skills.removeIf(s -> bannedSkills().contains(s));
		skills.removeIf(s -> bannedTactics().contains(s.type(c)));
		return skills;
	}
	
	public double encouragement(Skill s, Combat c, Character user) {
		return encouragedSkills().getOrDefault(s, 0.0);
	}
	
	public SkillModifier andThen(SkillModifier next) {
		SkillModifier me = this;
		return new SkillModifier() {
			@Override
			public Set<Skill> allowedSkills(Combat c) {
				Set<Skill> skills = me.allowedSkills(c);
				skills.retainAll(next.allowedSkills(c));
				return skills;
			}
			
			@Override
			public double encouragement(Skill s, Combat c, Character u) {
				return me.encouragement(s, c, u) + next.encouragement(s, c, u);
			}
		};
	}
	
	public static SkillModifier forAll(SkillModifier mod) {
		return new SkillModifier() {
			@Override
			public boolean playerOnly() {
				return false;
			}
			
			@Override
			public Set<Skill> allowedSkills(Combat c) {
				return mod.allowedSkills(c);
			}
			
			@Override
			public double encouragement(Skill s, Combat c, Character u) {
				return mod.encouragement(s, c, u);
			}
		};
	}
	
	public static SkillModifier allOf(SkillModifier... modifiers) {
		if (modifiers.length == 0)
			return NULL_MODIFIER;
		SkillModifier result = modifiers[0];
		for (int i = 1; i < modifiers.length; i++)
			result = result.andThen(modifiers[i]);
		return result;
	}
}
