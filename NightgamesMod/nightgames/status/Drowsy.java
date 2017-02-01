package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Drowsy extends DurationStatus {

    private int magnitude;

    public Drowsy(Character affected) {
        super("Drowsy", affected, 4);
        flag(Stsflag.drowsy);
        flag(Stsflag.purgable);
        flag(Stsflag.debuff);
        magnitude = 1;
    }

    public float fitnessModifier() {
        return -10;
    }

    public Drowsy(Character affected, int magnitude, int duration) {
        super("Drowsy", affected, duration);
        this.magnitude = magnitude;
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return "Your head feels woozy, as if you've just woken up.";
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "You feel lethargic and sluggish. You're struggling to remain standing";
        }

        return affected.subject() + " looks extremely sleepy.";
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int regen(Combat c) {
        return -3 * magnitude + super.regen(c);
    }

    @Override
    public void tick(Combat c) {
        affected.loseMojo(c, magnitude * -5);
    }

    @Override
    public int damage(Combat c, int x) {
        return 0;
    }

    @Override
    public int weakened(Combat c, int x) {
        return x * (1 + magnitude);
    }

    @Override
    public int tempted(Combat c, int x) {
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
        return x * 1 / (1 + magnitude);
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
    public Status instance(Character newAffected, Character newOther) {
        return new Drowsy(newAffected);
    }

    @Override
    public boolean lingering() {
        return true;
    }

     @Override public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("magnitude", magnitude);
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Drowsy(null, obj.get("magnitude").getAsInt(), obj.get("duration").getAsInt());
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }

}
