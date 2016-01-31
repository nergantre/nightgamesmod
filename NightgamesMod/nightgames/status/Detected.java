package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;

/**
 * Lingering status to mark on the minimap
 */
public class Detected extends DurationStatus {

    public Detected(Character affected, int duration) {
        super("Detected", affected, duration);
        flag(Stsflag.detected);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return "";
    }

    @Override
    public String describe(Combat c) {
        return "";
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        return 0;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }

    @Override
    public int weakened(int x) {
        return 0;
    }

    @Override
    public int tempted(int x) {
        return 0;
    }

    @Override
    public int evade() {
        return 0;
    }

    @Override
    public int escape() {
        return 0;
    }

    @Override
    public int gainmojo(int x) {
        return 0;
    }

    @Override
    public int spendmojo(int x) {
        return 0;
    }

    @Override
    public int counter() {
        return 0;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public boolean lingering() {
        return true;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Detected(newAffected, getDuration());
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject saveToJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", getClass().getSimpleName());
        obj.put("duration", getDuration());
        return obj;
    }

    @Override
    public Status loadFromJSON(JSONObject obj) {
        return new Detected(null, JSONUtils.readInteger(obj, "duration"));
    }
}
