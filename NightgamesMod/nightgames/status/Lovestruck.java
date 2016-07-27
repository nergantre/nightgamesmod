package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Lovestruck extends DurationStatus {
    Character other;

    public Lovestruck(Character affected, Character other, int duration) {
        super("Lovestruck", affected, duration);
        this.other = other;
        flag(Stsflag.lovestruck);
        flag(Stsflag.charmed);
        flag(Stsflag.purgable);
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "You feel an irresistable attraction to " + other.nameDirectObject() + ".";
        } else {
            return affected.name() + " is looking at you like a lovestruck teenager.";
        }
    }

    @Override
    public boolean mindgames() {
        return true;
    }

    @Override
    public float fitnessModifier() {
        return -(2 + getDuration() / 2.0f);
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public void onRemove(Combat c, Character other) {
        affected.addlist.add(new Cynical(affected));
    }

    @Override
    public void tick(Combat c) {
        affected.loseWillpower(c, 1, 0, false, " (Lovestruck)");
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
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s now lovestruck.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public int weakened(int x) {
        return 0;
    }

    @Override
    public int tempted(int x) {
        return 3;
    }

    @Override
    public int evade() {
        return 0;
    }

    @Override
    public int escape() {
        return -10;
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
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Lovestruck(newAffected, null, getDuration());
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Lovestruck(null, null, getDuration());
    }
}
