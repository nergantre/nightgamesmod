package nightgames.modifier.skill;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.modifier.ModifierComponent;
import nightgames.skills.Skill;

public class BanSkillsModifier extends SkillModifier implements ModifierComponent<BanSkillsModifier> {

	private final Set<Skill> skills;

	public BanSkillsModifier(Skill... skills) {
		this.skills = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(skills)));
	}

	@Override
	public Set<Skill> bannedSkills() {
		return skills;
	}

	@Override
	public String name() {
		return "ban-skills";
	}

	@Override
	public BanSkillsModifier instance(JSONObject obj) {
		if (obj.containsKey("skill")) {
			String name = JSONUtils.readString(obj, "skill");
			return new BanSkillsModifier(Global.getSkillPool().stream().filter(s -> s.getName().equals(name)).findAny()
					.orElseThrow(() -> new IllegalArgumentException("No such skill: " + name)));
		} else if (obj.containsKey("skills")) {
			List<String> names = JSONUtils.loadStringsFromArr(obj, "skills");
			Skill[] skills = names.stream()
					.map(name -> Global.getSkillPool().stream().filter(s -> s.getName().equals(name)).findAny()
							.orElseThrow(() -> new IllegalArgumentException("No such skill: " + name)))
					.toArray(Skill[]::new);
			return new BanSkillsModifier(skills);
		}
		throw new IllegalArgumentException("'ban-skills' must have 'skill' or 'skills'");
	}
	
	@Override
	public String toString() {
		return "Banned:" + skills.toString();
	}
}
