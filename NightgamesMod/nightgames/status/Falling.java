package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.stance.StandingOver;

public class Falling extends Status {
    public Falling(Character affected) {
        super("Falling", affected);
        flag(Stsflag.falling);
    }

    @Override
    public String describe(Combat c) {
        return "";
    }

    @Override
    public float fitnessModifier() {
        return -20;
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s knocked off balance.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public int regen(Combat c) {
        affected.removelist.add(this);
        c.setStance(new StandingOver(c.getOpponent(affected), affected));
        affected.add(new Stunned(affected));
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
        return new Falling(newAffected);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Falling(null);
    }
}
