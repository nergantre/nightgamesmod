package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.damage.DamageType;

public class Horny extends DurationStatus {
    private float magnitude;
    protected String source;

    public static Status getWithPsycologicalType(Character from, Character target, float magnitude, int duration, String source) {
        return new Horny(target, (float) from.modifyDamage(DamageType.temptation, target, magnitude), duration, source);
    }
    public static Horny getWithBiologicalType(Character from, Character target, float magnitude, int duration, String source) {
        return new Horny(target, (float) from.modifyDamage(DamageType.biological, target, magnitude), duration, source);
    }
    
    public Horny(Character affected, float magnitude, int duration, String source) {
        super("Horny", affected, duration);
        this.source = source;
        this.magnitude = magnitude;
        flag(Stsflag.horny);
        flag(Stsflag.debuff);
        flag(Stsflag.purgable);
    }

    @Override
    public String toString() {
        return "Aroused from " + source + " (" + Global.formatDecimal(magnitude) + " x " + getDuration() + ")";
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "Your heart pounds in your chest as you try to surpress your arousal from contacting " + source
                            + ".";
        } else {
            return affected.name() + " is flushed and "+affected.possessiveAdjective()
            +" nipples are noticeably hard from contacting " + source + ".";
        }
    }

    @Override
    public float fitnessModifier() {
        return -Math.min(.5f, magnitude * getDuration());
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int regen(Combat c) {
        super.regen(c);
        return 0;
    }

    @Override
    public void tick(Combat c) {
        affected.arouse(Math.round(magnitude), c, " (" + source + ")");
        affected.emote(Emotion.horny, 20);
    }

    @Override
    public String getVariant() {
        return source;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s %saroused by %s.\n", affected.subjectAction("are", "is"), replaced ? "" : "now ",
                        source + " (" + Global.formatDecimal(magnitude) + " x " + getDuration() + ")");
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }

    @Override
    public void replace(Status s) {
        assert s instanceof Horny;
        Horny other = (Horny) s;
        assert other.source.equals(source);
        setDuration(other.getDuration());
        magnitude = other.magnitude;
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
    public boolean lingering() {
        return true;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Horny(newAffected, magnitude, getDuration(), source);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("source", source);
        obj.addProperty("magnitude", magnitude);
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Horny(null, obj.get("magnitude").getAsFloat(), obj.get("duration").getAsInt(),
                        obj.get("source").getAsString());
    }
    public float getMagnitude() {
        return magnitude;
    }
    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

}
