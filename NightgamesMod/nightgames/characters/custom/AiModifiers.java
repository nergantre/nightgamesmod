package nightgames.characters.custom;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.*;

import nightgames.Resources.ResourceLoader;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.json.JsonUtils;
import nightgames.skills.Skill;
import nightgames.stance.Stance;
import nightgames.status.Stsflag;

public class AiModifiers {

    public static final Map<String, AiModifiers> DEFAULTS;
    public static final double AI_MOD_WEIGHT = 1.0;

    static {
        Map<String, AiModifiers> temp = new HashMap<>();
        JsonArray modifiersJson = JsonUtils.rootJson(new InputStreamReader(
                        ResourceLoader.getFileResourceAsStream("data/DefaultAiModifications.json"))).getAsJsonArray();
        for (JsonElement element: modifiersJson) {
            JsonObject modJson = element.getAsJsonObject();
            String pers = modJson.get("personality").getAsString();
            Optional<Double> malePref = JsonUtils.getOptional(modJson, "male-pref").map(JsonElement::getAsDouble);
            AiModifiers mods = readMods(modJson.getAsJsonArray("mods"));
            mods.setMalePref(malePref);
            temp.put(pers, mods);
        }
        DEFAULTS = Collections.unmodifiableMap(temp);
    }

    private Map<Class<? extends Skill>, Double> attackMods;
    private Map<Stance, Double> positionMods;
    private Map<Stsflag, Double> selfStatusMods;
    private Map<Stsflag, Double> oppStatusMods;
    private Optional<Double> malePref;

    public AiModifiers() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), Optional.empty());
    }

    public AiModifiers(Map<Class<? extends Skill>, Double> attackMods, Map<Stance, Double> positionMods,
                    Map<Stsflag, Double> selfStatusMods, Map<Stsflag, Double> oppStatusMods,
                    Optional<Double> malePref) {
        this.attackMods = attackMods;
        this.positionMods = positionMods;
        this.selfStatusMods = selfStatusMods;
        this.oppStatusMods = oppStatusMods;
        this.malePref = malePref;
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

    public double getMalePref() {
        return malePref.orElse((double) Global.getValue(Flag.malePref));
    }

    public void setMalePref(Optional<Double> malePref) {
        this.malePref = malePref;
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
        if (!DEFAULTS.containsKey(personality)) {
            System.err.println("No default AI modifications for " + personality + "!");
        }
        return DEFAULTS.getOrDefault(personality, new AiModifiers());
    }

    @SuppressWarnings("unchecked")
    private static AiModifiers readMods(JsonArray array) {
        AiModifiers mods = new AiModifiers();
        for (JsonElement element : array) {
            JsonObject modJson = element.getAsJsonObject();
            String type = modJson.get("type").getAsString();
            String value = modJson.get("value").getAsString();
            double weight = modJson.get("weight").getAsFloat();
            switch (type) {
                case "skill":
                    try {
                        Class<? extends Skill> clazz = (Class<? extends Skill>) Class.forName(value);
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
