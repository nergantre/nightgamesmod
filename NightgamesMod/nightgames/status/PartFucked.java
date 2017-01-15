package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.json.JsonUtils;

public class PartFucked extends Status implements InsertedStatus {
    private String target;
    private Character other;
    private BodyPart penetrator;

    public PartFucked(Character affected, Character other, BodyPart stick, String hole) {
        super(Global.capitalizeFirstLetter(stick.getType()) + (hole.equals("ass") ? " Pegged" : " Fucked"), affected);
        target = hole;
        this.penetrator = stick;
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
        BodyPart hole = affected.body.getRandom(target);
        if (hole == null || penetrator == null) {
            return "";
        }
        return Global.capitalizeFirstLetter(String.format("%s now fucking %s %s with %s %s\n",
                        other.subjectAction("are", "is"), affected.nameOrPossessivePronoun(), hole.describe(affected),
                        other.possessiveAdjective(), penetrator.describe(other)));
    }

    @Override
    public String describe(Combat c) {
        BodyPart hole = affected.body.getRandom(target);
        if (hole == null || penetrator == null) {
            return "";
        }
        return Global.capitalizeFirstLetter(String.format("%s fucking %s %s with %s %s\n",
                            other.subjectAction("are", "is"), affected.nameOrPossessivePronoun(),
                            hole.describe(affected), other.possessiveAdjective(), penetrator.describe(other)));
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
        BodyPart hole = affected.body.getRandom(target);
        if (hole == null || penetrator == null || c == null) {
            affected.removelist.add(this);
            return;
        }
        c.write(other, Global.capitalizeFirstLetter(
                        Global.format("{other:name-possessive} %s relentlessly fucks {self:name-do} in {self:possessive} {self:body-part:"
                                        + target + "}.", affected, other, penetrator.describe(other))));
        affected.body.pleasure(other, penetrator, hole, 10, c);
        other.body.pleasure(affected, hole, penetrator, 2, c);
        affected.emote(Emotion.desperate, 10);
        affected.emote(Emotion.nervous, 10);
    }

    public void onRemove(Combat c, Character other) {
        c.write(other, Global.format("{other:NAME-POSSESSIVE} slick %s slips out of {self:direct-object} with an audible pop.", affected, other));
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
        return Global.capitalizeFirstLetter(penetrator.getType()) + " fucked";
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new PartFucked(newAffected, newOther, penetrator, target);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("penetrator", JsonUtils.gson.toJson(penetrator));
        obj.addProperty("target", target);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new PartFucked(null, null, JsonUtils.gson.fromJson(obj.get("penetrator"), BodyPart.class), obj.get("target").getAsString());
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }

    @Override
    public BodyPart getHolePart() {
        return affected.body.getRandom(target);
    }

    @Override
    public Character getReceiver() {
        return affected;
    }

    @Override
    public BodyPart getStickPart() {
        return penetrator;
    }

    @Override
    public Character getPitcher() {
        return other;
    }
}
