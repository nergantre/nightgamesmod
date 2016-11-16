package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.json.JsonUtils;

public class Sensitized extends DurationStatus {
    BodyPart part;
    double magnitude;
    double maximum;

    public Sensitized(Character affected, BodyPart part, double magnitude, double maximum, int duration) {
        super("Sensitized (" + part.getType() + ")", affected, duration);
        this.part = part;
        this.magnitude = magnitude;
        this.maximum = maximum;
        flag(Stsflag.sensitized);
        flag(Stsflag.purgable);
    }

    @Override
    public float fitnessModifier() {
        return (float) (-magnitude * 5);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (replaced)
            return "";
        return Global.format(String.format("{self:NAME-POSSESSIVE} groans as {self:possessive} %s grows hot.",
                        part.describe(affected)), affected, c.getOpponent(affected));
    }

    @Override
    public String describe(Combat c) {
        return "";
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
        if (targetPart.isType(part.getType())) {
            return x * magnitude;
        }
        return 0;
    }

    public boolean overrides(Status s) {
        return s.name.equals(this.name);
    }

    public void replace(Status newStatus) {
        if (newStatus instanceof Sensitized) {
            this.magnitude = Math.min(maximum, magnitude + ((Sensitized) newStatus).magnitude);
            setDuration(Math.max(((Sensitized) newStatus).getDuration(), getDuration()));
        }
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
    public boolean lingering() {
        return true;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Sensitized(newAffected, part, magnitude, maximum, getDuration());
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("magnitude", magnitude);
        obj.addProperty("maximum", maximum);
        obj.addProperty("duration", getDuration());
        obj.add("part", JsonUtils.gson.toJsonTree(part));
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Sensitized(Global.noneCharacter(), JsonUtils.gson.fromJson(obj.get("part"), BodyPart.class), obj.get("magnitude").getAsFloat(),
                        obj.get("maximum").getAsFloat(), obj.get("duration").getAsInt());
    }

}
