package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Oiled extends Status {
    public Oiled(Character affected) {
        super("Oiled", affected);
        flag(Stsflag.oiled);
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "Your skin is slick with oil and kinda feels weird.";
        } else {
            return String.format("%s is shiny with lubricant, making %s more tempted to touch and rub %s skin.",
                            affected.pronoun(), c.getOther(affected).subject(), affected.pronoun());
        }
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s now lubricated.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        return 0;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return x / 4;
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
        return 8;
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
        return new Oiled(newAffected);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Oiled(null);
    }
}
