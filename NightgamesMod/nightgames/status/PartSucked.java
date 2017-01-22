package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.json.JsonUtils;

public class PartSucked extends Status implements InsertedStatus {
    private String target;
    private Character other;
    private BodyPart penetrated;

    public PartSucked(Character affected, Character other, BodyPart penetrated, String targetType) {
        super(penetrated + " Sucked", affected);
        target = targetType;
        this.penetrated = penetrated;
        this.other = other;
        requirements.add((c, self, opponent) -> {
            if (c.getStance().distance() > 1) {
                return false;
            }
            return false;
        });
        flag(Stsflag.debuff);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        BodyPart stick = affected.body.getRandom(target);
        if (stick == null || penetrated == null) {
            return "";
        }
        return Global.capitalizeFirstLetter(String.format("%s now fucking %s %s with %s %s\n",
                        other.subjectAction("are", "is"), affected.nameOrPossessivePronoun(), stick.describe(affected),
                        other.possessiveAdjective(), penetrated.describe(other)));
    }

    @Override
    public String describe(Combat c) {
        BodyPart stick = affected.body.getRandom(target);
        if (stick == null || penetrated == null) {
            return "";
        }
        return Global.capitalizeFirstLetter(String.format("%s fucking %s %s with %s %s\n",
                            other.subjectAction("are", "is"), affected.nameOrPossessivePronoun(),
                            stick.describe(affected), other.possessiveAdjective(), penetrated.describe(other)));
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
    public void tick(Combat c) {
        BodyPart stick = affected.body.getRandom(target);
        if (stick == null || penetrated == null || c == null) {
            affected.removelist.add(this);
            return;
        }
        c.write(other, Global.capitalizeFirstLetter(
                        Global.format("{other:name-possessive} %s mindlessly milks {self:name-possessive} {self:body-part:" + target + "}.", affected, other, penetrated.describe(other))));
        affected.body.pleasure(other, penetrated, stick, 10, c);
        other.body.pleasure(affected, stick, penetrated, 2, c);
        affected.emote(Emotion.desperate, 10);
        affected.emote(Emotion.nervous, 10);
    }

    public void onRemove(Combat c, Character other) {
        c.write(other, Global.format("{other:NAME-POSSESSIVE} slick %s falls off {self:direct-object} with an audible pop.", affected, other, penetrated.describe(other)));
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
        return -5;
    }

    @Override
    public int escape() {
        return -5;
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
        return Global.capitalizeFirstLetter(penetrated.getType()) + " fucked";
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new PartSucked(newAffected, newOther, penetrated, target);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("penetrator", JsonUtils.gson.toJson(penetrated));
        obj.addProperty("target", target);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new PartSucked(null, null, JsonUtils.gson.fromJson(obj.get("penetrator"), BodyPart.class), obj.get("target").getAsString());
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }

    @Override
    public BodyPart getHolePart() {
        return penetrated;
    }

    @Override
    public Character getReceiver() {
        return other;
    }

    @Override
    public BodyPart getStickPart() {
        return affected.body.getRandom(target);
    }

    @Override
    public Character getPitcher() {
        return affected;
    }
}
