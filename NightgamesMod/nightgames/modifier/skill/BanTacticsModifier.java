package nightgames.modifier.skill;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

import nightgames.global.JSONUtils;
import nightgames.modifier.ModifierComponent;
import nightgames.skills.Tactics;

public class BanTacticsModifier extends SkillModifier implements ModifierComponent<BanTacticsModifier> {

	private final Set<Tactics> tactics;

	public BanTacticsModifier(Tactics... skills) {
		tactics = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(skills)));
	}

	@Override
	public Set<Tactics> bannedTactics() {
		return tactics;
	}

	@Override
	public String name() {
		return "ban-tactic";
	}

	@Override
	public BanTacticsModifier instance(JSONObject obj) {
		if (obj.containsKey("tactic")) {
			String name = JSONUtils.readString(obj, "tactic");
			Tactics tact = Tactics.valueOf(name);
			return new BanTacticsModifier(tact);
		} else if (obj.containsKey("tactics")) {
			return new BanTacticsModifier(JSONUtils.loadStringsFromArr(obj, "tactics").stream().map(Tactics::valueOf)
					.toArray(Tactics[]::new));
		}
		throw new IllegalArgumentException("'ban-tactics' must have 'tactic' or 'tactics'");
	}

	@Override
	public String toString() {
		return "Banned:" + tactics.toString();
	}
}
