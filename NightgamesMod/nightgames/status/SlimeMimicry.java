package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.global.JSONUtils;

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
        return Global.format("{self:SUBJECT} started mimicking a %s.", affected, c.getOther(affected), mimickedName);
    }

    @Override
    public String describe(Combat c) {
    	return Global.format("{self:SUBJECT-ACTION:are|is} mimicking a %s.", affected, c.getOther(affected), mimickedName);
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

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject saveToJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", getClass().getSimpleName());
        obj.put("mimickedName", getMimickedName());
        obj.put("pussyMimicked", pussyMimicked.name());
        obj.put("cockMimicked", cockMimicked.name());
        obj.put("duration", getDuration());
        return obj;
    }

    @Override
    public Status loadFromJSON(JSONObject obj) {
        return new SlimeMimicry(JSONUtils.readString(obj, "mimickedName"),
                        PussyPart.valueOf(JSONUtils.readString(obj, "pussyMimicked")),
                        CockMod.valueOf(JSONUtils.readString(obj, "cockMimicked")),
                        null,
                        JSONUtils.readInteger(obj, "duration"));
    }

    public String getMimickedName() {
        return mimickedName;
    }

}
