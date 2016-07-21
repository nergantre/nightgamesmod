package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class SlimeMimicry extends DurationStatus {
    private final String mimickedName;
    private final PussyPart pussyMimicked;
    private final CockMod cockMimicked;

    public SlimeMimicry(String name, PussyPart pussyMimicked, CockMod cockMimicked, Character affected, int duration) {
        super("Mimicry: " + Global.capitalizeFirstLetter(name), affected, duration);
        this.mimickedName = name;
        this.pussyMimicked = pussyMimicked;
        this.cockMimicked = cockMimicked;
        this.flag(Stsflag.mimicry);
        this.flag(Stsflag.form);
    }

    public PussyPart getPussyMimicked() {
        return pussyMimicked;
    }

    public CockMod getCockMimicked() {
        return cockMimicked;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return Global.format("{self:SUBJECT} started mimicking a %s", affected, c.getOther(affected), name);
    }

    @Override
    public String describe(Combat c) {
        return Global.format("{self:SUBJECT} is mimicking a %s", affected, c.getOther(affected), name);
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
        return new SlimeMimicry(getMimickedName(), pussyMimicked, cockMimicked, newAffected, getDuration());
    }

    @SuppressWarnings("unchecked") @Override public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("mimickedName", getMimickedName());
        obj.addProperty("pussyMimicked", pussyMimicked.name());
        obj.addProperty("cockMimicked", cockMimicked.name());
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new SlimeMimicry(obj.get("mimickedName").getAsString(),
                        PussyPart.valueOf(obj.get("pussyMimicked").getAsString()),
                        CockMod.valueOf(obj.get("cockMimicked").getAsString()), null, obj.get("duration").getAsInt());
    }

    public String getMimickedName() {
        return mimickedName;
    }

}
