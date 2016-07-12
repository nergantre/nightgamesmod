package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

public class DivineRecoil extends DurationStatus {
    public double magnitude;

    public DivineRecoil(Character affected, double magnitude) {
        super("Divine Recoil", affected, 5);
        flag(Stsflag.divinerecoil);
        flag(Stsflag.purgable);
        this.magnitude = magnitude;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        if (!replaced) {
            return String.format(
                            "Some leftover divine energy is rampaging through %s body, leaving %s incredibly sensitive.\n",
                            affected.nameOrPossessivePronoun(), affected.directObject());
        }
        return "";
    }

    @Override
    public String describe(Combat c) {
        return String.format("Divine energy rampages through %s body, leaving %s incredibly sensitive.\n",
                        affected.nameOrPossessivePronoun(), affected.directObject());

    }

    @Override
    public float fitnessModifier() {
        return (float) (-magnitude / 2);
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }

    @Override
    public void replace(Status s) {
        assert s instanceof DivineRecoil;
        DivineRecoil other = (DivineRecoil) s;
        magnitude = magnitude + other.magnitude / 2;
        setDuration(Math.max(getDuration(), other.getDuration()));
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
    public double sensitivity(double x) {
        return magnitude;
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
        return new DivineRecoil(newAffected, magnitude);
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("magnitude", magnitude);
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return new DivineRecoil(null, obj.get("magnitude").getAsFloat());
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }
}
