package nightgames.characters.custom;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import nightgames.Resources.ResourceLoader;
import nightgames.global.JSONUtils;
import nightgames.skills.Skill;
import nightgames.stance.Stance;
import nightgames.status.Stsflag;

public class AiModifiers {

	public static final Map<String, AiModifiers> DEFAULTS;
	public static final double AI_MOD_WEIGHT = 1.0;

	static {
		Map<String, AiModifiers> temp = new HashMap<>();
		InputStream is = ResourceLoader
				.getFileResourceAsStream("data/DefaultAiModifications.json");
		JSONArray root = (JSONArray) JSONValue.parse(new InputStreamReader(is));
		for (Object obj : root) {
			JSONObject jobj = (JSONObject) obj;
			String pers = JSONUtils.readString(jobj, "personality");
			AiModifiers mods = readMods((JSONArray) jobj.get("mods"));
			temp.put(pers, mods);
		}
		DEFAULTS = Collections.unmodifiableMap(temp);
	}

	private Map<Class<? extends Skill>, Double>	attackMods;
	private Map<Stance, Double>					positionMods;
	private Map<Stsflag, Double>				selfStatusMods;
	private Map<Stsflag, Double>				oppStatusMods;

	public AiModifiers() {
		this(new HashMap<>(), new HashMap<>(), new HashMap<>(),
				new HashMap<>());
	}

	public AiModifiers(Map<Class<? extends Skill>, Double> attackMods,
			Map<Stance, Double> positionMods,
			Map<Stsflag, Double> selfStatusMods,
			Map<Stsflag, Double> oppStatusMods) {
		this.attackMods = attackMods;
		this.positionMods = positionMods;
		this.selfStatusMods = selfStatusMods;
		this.oppStatusMods = oppStatusMods;
	}

	public double modAttack(Class<? extends Skill> clazz) {
		return AI_MOD_WEIGHT * attackMods.getOrDefault(clazz, 0.0);
	}

	public double modPosition(Stance pos) {
		return AI_MOD_WEIGHT * positionMods.getOrDefault(pos, 0.0);
	}

	public double modSelfStatus(Stsflag flag) {
		return AI_MOD_WEIGHT * selfStatusMods.getOrDefault(flag, 0.0);
	}

	public double modOpponentStatus(Stsflag flag) {
		return AI_MOD_WEIGHT * oppStatusMods.getOrDefault(flag, 0.0);
	}

	public Map<Class<? extends Skill>, Double> getAttackMods() {
		return attackMods;
	}

	public void setAttackMods(Map<Class<? extends Skill>, Double> attackMods) {
		this.attackMods = attackMods;
	}

	public Map<Stance, Double> getPositionMods() {
		return positionMods;
	}

	public void setPositionMods(Map<Stance, Double> positionMods) {
		this.positionMods = positionMods;
	}

	public Map<Stsflag, Double> getSelfStatusMods() {
		return selfStatusMods;
	}

	public void setSelfStatusMods(Map<Stsflag, Double> selfStatusMods) {
		this.selfStatusMods = selfStatusMods;
	}

	public Map<Stsflag, Double> getOppStatusMods() {
		return oppStatusMods;
	}

	public void setOppStatusMods(Map<Stsflag, Double> oppStatusMods) {
		this.oppStatusMods = oppStatusMods;
	}

	public static AiModifiers getDefaultModifiers(String personality) {
		if (!DEFAULTS.containsKey(personality))
			System.err.println(
					"No default AI modifications for " + personality + "!");
		return DEFAULTS.getOrDefault(personality, new AiModifiers());
	}

	@SuppressWarnings("unchecked")
	private static AiModifiers readMods(JSONArray array) {
		AiModifiers mods = new AiModifiers();
		for (Object obj : array) {
			JSONObject mod = (JSONObject) obj;
			String type = JSONUtils.readString(mod, "type");
			String value = JSONUtils.readString(mod, "value");
			double weight = JSONUtils.readFloat(mod, "weight");
			switch (type) {
				case "skill":
					try {
						Class<? extends Skill> clazz = (Class<? extends Skill>) Class
								.forName(value);
						mods.attackMods.put(clazz, weight);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					break;
				case "position":
					mods.positionMods.put(Stance.valueOf(value), weight);
					break;
				case "self-status":
					mods.selfStatusMods.put(Stsflag.valueOf(value), weight);
					break;
				case "opp-status":
					mods.oppStatusMods.put(Stsflag.valueOf(value), weight);
					break;
			}
		}
		return mods;
	}
}
