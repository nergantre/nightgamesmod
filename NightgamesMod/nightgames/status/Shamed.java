package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class Shamed extends DurationStatus {

    private int magnitude;

    public Shamed(Character affected) {
        super("Shamed", affected, 4);
        flag(Stsflag.shamed);
        flag(Stsflag.debuff);
        flag(Stsflag.purgable);
        magnitude = 1;
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "You're a little distracted by self-consciousness, and it's throwing you off your game.";
        } else {
            return affected.getName() + " is red faced from embarrassment as much as arousal.";
        }
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return String.format("%s now shamed.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public boolean mindgames() {
        return true;
    }

    @Override
    public float fitnessModifier() {
        return -1.0f * magnitude;
    }

    @Override
    public void onRemove(Combat c, Character other) {
        affected.addlist.add(new Cynical(affected));
    }

    @Override
    public int mod(Attribute a) {
        if (a == Attribute.Seduction || a == Attribute.Cunning) {
            return Math.min(-2 * magnitude, -affected.getPure(a) * magnitude / 5);
        } else if (a == Attribute.Submissive && affected.getPure(Attribute.Submissive) > 0) {
            return magnitude;
        } else {
            return 0;
        }
    }

    @Override
    public void tick(Combat c) {
        affected.emote(Emotion.nervous, 20);
        if (affected.getPure(Attribute.Submissive) > 0) {
            affected.buildMojo(c, 3 * magnitude, " (Shamed)");
        } else {
            affected.loseMojo(c, 5 * magnitude, " (Shamed)");
        }
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
        return magnitude;
    }

    @Override
    public int tempted(int x) {
        return 2 * magnitude;
    }

    @Override
    public int evade() {
        return 0;
    }

    @Override
    public int escape() {
        return -2 * magnitude;
    }

    @Override
    public int gainmojo(int x) {
        return -x * magnitude / 2;
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
        return new Shamed(newAffected);
    }

    @Override
    public void replace(Status newStatus) {
        assert newStatus instanceof Shamed;
        Shamed other = (Shamed) newStatus;
        setDuration(Math.max(other.getDuration(), getDuration()));
        magnitude += other.magnitude;
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Shamed(null);
    }
}
