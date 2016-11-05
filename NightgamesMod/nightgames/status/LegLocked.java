package nightgames.status;

import static nightgames.requirements.RequirementShortcuts.eitherinserted;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class LegLocked extends Status {
    private float toughness;

    public LegLocked(Character affected, float dc) {
        super("Leg Locked", affected);
        requirements.add(eitherinserted());
        requirements.add((c, self, other) -> toughness > .01);
        toughness = dc;
        flag(Stsflag.leglocked);
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "Her legs are locked around your waist, preventing you from pulling out.";
        } else {
            return String.format("%s legs are wrapped around %s waist, preventing %s from pulling out.",
                            c.getOther(affected).nameOrPossessivePronoun(), affected.nameOrPossessivePronoun(),
                            affected.directObject());
        }
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s being held down.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public float fitnessModifier() {
        return -toughness / 10.0f;
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int regen(Combat c) {
        affected.emote(Emotion.horny, 10);
        toughness -= 2;
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
        return Math.round(-toughness);
    }

    @Override
    public void struggle(Character self) {
        toughness = Math.round(toughness * .5f);
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
        return "Bound by legs";
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new LegLocked(newAffected, toughness);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("toughness", toughness);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new LegLocked(null, obj.get("toughness").getAsFloat());
    }
}
