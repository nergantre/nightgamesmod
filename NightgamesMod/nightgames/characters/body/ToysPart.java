package nightgames.characters.body;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public enum ToysPart implements BodyPart,BodyPartMod {
    dildo("a", "dildo", "dildo", 0, 1.0, 0),
    vibrator("a", "vibrator", "dildo", 0, 1.5, 0),
    onahole("an", "onahole", "onahole", 0, 1.0, 0),
    analbeads("", "anal beads", "analbeads", 0, 1.0, 0);

    public String desc;
    public double hotness;
    public double pleasure;
    public double sensitivity;
    private String type;
    private String prefix;

    ToysPart(String prefix, String desc, String type, double hotness, double pleasure, double sensitivity) {
        this.desc = desc;
        this.type = type;
        this.prefix = prefix;
        this.hotness = hotness;
        this.pleasure = pleasure;
        this.sensitivity = sensitivity;
    }

    @Override
    public void describeLong(StringBuilder b, Character c) {
        b.append("");
    }

    @Override
    public String describe(Character c) {
        return desc;
    }

    @Override
    public String canonicalDescription() {
        return desc;
    }

    @Override
    public double priority(Character c) {
        return getPleasure(c, null);
    }

    @Override
    public String fullDescribe(Character c) {
        return desc;
    }

    @Override
    public String toString() {
        return desc;
    }

    @Override
    public boolean isType(String type) {
        return type.equalsIgnoreCase(type);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public double getHotness(Character self, Character opponent) {
        return hotness;
    }

    @Override
    public double getPleasure(Character self, BodyPart target) {
        return pleasure;
    }

    @Override
    public double getSensitivity(BodyPart target) {
        return sensitivity;
    }

    @Override
    public boolean isReady(Character self) {
        return true;
    }

     @Override public JsonObject save() {
        JsonObject obj = new JsonObject();
        obj.addProperty("enum", name());
        return obj;
    }

    @Override public BodyPart load(JsonObject obj) {
        return ToysPart.valueOf(obj.get("enum").getAsString());
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        return 0;
    }

    @Override
    public String getFluids(Character c) {
        return "";
    }

    @Override
    public boolean isErogenous() {
        return false;
    }

    @Override
    public boolean isNotable() {
        return false;
    }

    @Override
    public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        return 0;
    }

    @Override
    public BodyPart upgrade() {
        return this;
    }

    @Override
    public BodyPart downgrade() {
        return this;
    }

    @Override
    public String prefix() {
        return prefix;
    }

    @Override
    public int compare(BodyPart other) {
        return 0;
    }

    @Override
    public boolean isVisible(Character c) {
        return false;
    }

    @Override
    public double applySubBonuses(Character self, Character opponent, BodyPart with, BodyPart target, double damage,
                    Combat c) {
        return 0;
    }

    @Override
    public int mod(Attribute a, int total) {
        return 0;
    }

    @Override
    public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {

    }

    @Override
    public int counterValue(BodyPart otherPart, Character self, Character other) {
        return 0;
    }

    @Override
    public BodyPartMod getMod(Character self) {
        return this;
    }

    @Override
    public String getModType() {
        return name();
    }
}
