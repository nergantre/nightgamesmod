package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class FireStance extends DurationStatus {
    public FireStance(Character affected) {
        super("Fire Form", affected, 10);
        flag(Stsflag.form);
    }

    @Override
    public float fitnessModifier() {
        return 1;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s now in a fire stance.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "Your spirit burns in you, feeding your power";
        } else {
            return affected.name() + " is all fired up.";
        }
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int regen(Combat c) {
        super.regen(c);
        affected.emote(Emotion.confident, 5);
        affected.emote(Emotion.dominant, 5);
        return -5;
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
        return x;
    }

    @Override
    public int spendmojo(int x) {
        return -x / 2;
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
    public Status instance(Character newAffected, Character newOther) {
        return new FireStance(newAffected);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new FireStance(null);
    }
}
