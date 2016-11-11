package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Tolerance extends DurationStatus {
    public Tolerance(Character affected, int duration) {
        super("Tolerance", affected, duration);
        flag(Stsflag.tolerance);
        flag(Stsflag.purgable);
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "You've built up a tolerance to addictive fluids.";
        } else {
            return affected.name() + " has built up a tolerance to "
                           +c.getOpponent(affected).nameOrPossessivePronoun()+" addictive fluids.";
        }
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s built a tolerance to addictive fluids.\n", affected.subjectAction("have", "has"));
    }

    @Override
    public float fitnessModifier() {
        return 1;
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
    public Status instance(Character newAffected, Character newOther) {
        return new Tolerance(newAffected, getDuration());
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Tolerance(null, obj.get("duration").getAsInt());
    }
}
