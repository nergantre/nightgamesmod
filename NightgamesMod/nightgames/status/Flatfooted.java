package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Flatfooted extends DurationStatus {
    private boolean makesWary;

    public Flatfooted(Character affected, int duration, boolean makesWary) {
        super("Flat-Footed", affected, duration);
        flag(Stsflag.distracted);
        flag(Stsflag.debuff);
        flag(Stsflag.disabling);
        flag(Stsflag.purgable);
        this.makesWary = makesWary;
    }

    public Flatfooted(Character affected, int duration) {
        this(affected, duration, true);
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "You are caught off-guard.";
        } else {
            return affected.getName() + " is flat-footed and not ready to fight.";
        }
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return String.format("%s now flatfooted.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public float fitnessModifier() {
        return -3;
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public void onRemove(Combat c, Character other) {
        if (makesWary && affected.canRespond()) {
            affected.addlist.add(new Wary(affected, 3));
        }
    }

    @Override
    public int regen(Combat c) {
        super.regen(c);
        affected.emote(Emotion.nervous, 5);
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
    public int weakened(Combat c, int x) {
        return 0;
    }

    @Override
    public int tempted(Combat c, int x) {
        return 0;
    }

    @Override
    public int evade() {
        return -10;
    }

    @Override
    public int escape() {
        return -20;
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
        return -3;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Flatfooted(newAffected, getDuration(), makesWary);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("duration", getDuration());
        obj.addProperty("wary", makesWary);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Flatfooted(null, obj.get("duration").getAsInt(), obj.get("wary").getAsBoolean());
    }
}
