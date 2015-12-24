package nightgames.modifier.skill;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.modifier.ModifierComponent;
import nightgames.skills.Skill;

public class EncourageSkillsModifier extends SkillModifier implements ModifierComponent<EncourageSkillsModifier> {

	private final Map<Skill, Double> absolutes;
	private final Map<Skill, BiFunction<Character, Combat, Double>> variables;

	public EncourageSkillsModifier(Skill s, double encouragement) {
		absolutes = Collections.unmodifiableMap(Collections.singletonMap(s, encouragement));
		variables = Collections.emptyMap();
	}

	public EncourageSkillsModifier(Skill s, BiFunction<Character, Combat, Double> encouragementFunc) {
		absolutes = Collections.emptyMap();
		variables = Collections.unmodifiableMap(Collections.singletonMap(s, encouragementFunc));
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
		return absolutes.getOrDefault(skill, 0.0) + variables.getOrDefault(skill, (ch, com) -> 0.0).apply(user, c);
	}

	// This applies only to npcs anyway
	@Override
	public final boolean playerOnly() {
		return false;
	}

	@Override
	public String name() {
		return "encourage-skills";
	}

	@Override
	public EncourageSkillsModifier instance(JSONObject obj) {
		if (obj.containsKey("list")) {
			JSONArray arr = (JSONArray) obj.get("list");
			Map<Skill, Double> encs = new HashMap<>();
			for (Object raw : arr) {
				JSONObject jobj = (JSONObject) raw;
				if (!(jobj.containsKey("skill") && jobj.containsKey("weight"))) {
					throw new IllegalArgumentException("All encouraged skills need a 'skill' and a 'weight'");
				}
				String name = JSONUtils.readString(jobj, "skill");
				Skill skill = Global.getSkillPool().stream().filter(s -> s.getName().equals(name)).findAny()
						.orElseThrow(() -> new IllegalArgumentException("No such skill: " + name));
				double weight = JSONUtils.readFloat(jobj, "weight");
				encs.put(skill, weight);
			}
			return new EncourageSkillsModifier(encs);
		} else if (obj.containsKey("skill") && obj.containsKey("weight")) {
			String name = JSONUtils.readString(obj, "skill");
			Skill skill = Global.getSkillPool().stream().filter(s -> s.getName().equals(name)).findAny()
					.orElseThrow(() -> new IllegalArgumentException("No such skill: " + name));
			double weight = JSONUtils.readFloat(obj, "weight");
			return new EncourageSkillsModifier(skill, weight);
		}
		throw new IllegalArgumentException("'encourage-skills' must have either 'list' or 'skill' and 'weight'");
	}
	
	@Override
	public String toString() {
		return absolutes.toString();
	}
}
