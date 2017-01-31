package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Hypersensitive extends DurationStatus {
    public Hypersensitive(Character affected) {
        this(affected, 20);
    }
    public Hypersensitive(Character affected, int duration) {
        super("Hypersensitive", affected, duration);
        flag(Stsflag.hypersensitive);
        flag(Stsflag.debuff);
        flag(Stsflag.purgable);
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "Your skin tingles and feels extremely sensitive to touch.";
        } else {
            return String.format("%s shivers from the breeze hitting %s skin and has goosebumps.",
                            affected.pronoun(), affected.possessiveAdjective());
        }
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        if (!replacement.isPresent()) {
            return String.format("%s now hypersensitive.\n", affected.subjectAction("are", "is"));
        } else {
            return "";
        }
    }

    @Override
    public float fitnessModifier() {
        return -1;
    }

    @Override
    public int mod(Attribute a) {
        if (a == Attribute.Perception) {
            return 4;
        }
        return 0;
    }

    @Override
    public int regen(Combat c) {
        super.regen(c);
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        return x / 2;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return x / 2;
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
    public boolean lingering() {
        return true;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Hypersensitive(newAffected);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Hypersensitive(null);
    }
}
