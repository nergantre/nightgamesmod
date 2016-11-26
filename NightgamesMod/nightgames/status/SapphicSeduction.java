package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class SapphicSeduction extends Status {
    public SapphicSeduction(Character affected) {
        super("Sapphic Seduction", affected);
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
        if (a == Attribute.Seduction) {
            return affected.getLevel() / 2;
        }
        return 0;
    }

    @Override
    public float fitnessModifier() {
        return 4.0f;
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
        return new SapphicSeduction(newAffected);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new SapphicSeduction(null);
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }
}
