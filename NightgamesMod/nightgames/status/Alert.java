package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Alert extends DurationStatus {
    public Alert(Character affected) {
        super("Alert", affected, 3);
        flag(Stsflag.alert);
        flag(Stsflag.purgable);
    }

    @Override
    public float fitnessModifier() {
        return 3;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s now more alert\n", affected.subjectAction("are", "is"));
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
    public int regen(Combat c) {
        super.regen(c);
        affected.emote(Emotion.confident, 5);
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
        return 15;
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
        return 15;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Alert(newAffected);
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject saveToJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", getClass().getSimpleName());
        return obj;
    }

    @Override
    public Status loadFromJSON(JSONObject obj) {
        return new Alert(null);
    }
}
