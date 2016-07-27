package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Bound extends Status {
    private float toughness;
    private String binding;

    public Bound(Character affected, float dc, String binding) {
        super("Bound", affected);
        toughness = dc;
        this.binding = binding;
        flag(Stsflag.bound);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s now bound by %s.\n", affected.subjectAction("are", "is"), binding);
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "Your hands are bound by " + binding + ".";
        } else {
            return "Her hands are restrained by " + binding + ".";
        }
    }

    @Override
    public float fitnessModifier() {
        return -(5 + Math.min(20, toughness) / 2);
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int regen(Combat c) {
        affected.emote(Emotion.desperate, 10);
        affected.emote(Emotion.nervous, 10);
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
        return -15;
    }

    @Override
    public int escape() {
        return -Math.round(toughness);
    }

    @Override
    public void struggle(Character self) {
        if (toughness > 50) {
            toughness = Math.max(Math.round(toughness * .33f), 25);
        } else {
            toughness = Math.round(toughness * .5f);
        }
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
        return -10;
    }

    @Override
    public String toString() {
        return "Bound by " + binding;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Bound(newAffected, toughness, binding);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("toughness", toughness);
        obj.addProperty("binding", binding);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Bound(null, obj.get("toughness").getAsFloat(), obj.get("binding").getAsString());
    }
}
