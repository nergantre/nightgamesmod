package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Nimble extends DurationStatus {
    public Nimble(Character affected, int duration) {
        super("Nimble", affected, duration);
        flag(Stsflag.nimble);
        flag(Stsflag.purgable);
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "You're as quick and nimble as a cat.";
        } else {
            return affected.getName() + " darts around gracefully.";
        }
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return String.format("%s now more nimble.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public float fitnessModifier() {
        return affected.get(Attribute.Animism) / 10.0f;
    }

    @Override
    public int mod(Attribute a) {
        switch (a) {
            case Speed:
                return 2 + affected.getArousal().getReal() / 100;
            default:
                break;
        }
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
        return affected.get(Attribute.Animism) * affected.getArousal().percent() / 100;
    }

    @Override
    public int escape() {
        return affected.get(Attribute.Animism) * affected.getArousal().percent() / 100;
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
        return (affected.get(Attribute.Animism) / 2) * affected.getArousal().percent() / 100;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Nimble(newAffected, getDuration());
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Nimble(null, obj.get("duration").getAsInt());
    }
}
