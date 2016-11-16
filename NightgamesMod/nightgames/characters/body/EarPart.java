package nightgames.characters.body;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public enum EarPart implements BodyPart, BodyPartMod {
    pointed("pointed ", .2, 1.2, 1),
    cat("cat ", .4, 1.5, 1.5),
    normal("normal ", 0, 1, 1);

    public String desc;
    public double hotness;
    public double pleasure;
    public double sensitivity;

    EarPart(String desc, double hotness, double pleasure, double sensitivity) {
        this.desc = desc;
        this.hotness = hotness;
        this.pleasure = pleasure;
        this.sensitivity = sensitivity;
    }

    @Override
    public String canonicalDescription() {
        return desc + "ears";
    }

    @Override
    public void describeLong(StringBuilder b, Character c) {
        switch (this) {
            case cat:
                b.append("Cute " + fullDescribe(c) + " tops " + c.possessivePronoun() + " head.");
                break;
            default:
                b.append(Global.capitalizeFirstLetter(fullDescribe(c)) + " frames " + c.possessivePronoun() + " face.");
        }
    }

    @Override
    public String describe(Character c) {
        if (this != normal) {
            return desc + "ears";
        }
        return "ears";
    }

    @Override
    public double priority(Character c) {
        return getPleasure(c, null);
    }

    @Override
    public String fullDescribe(Character c) {
        return desc + "ears";
    }

    @Override
    public String toString() {
        return desc + "ears";
    }

    @Override
    public boolean isType(String type) {
        return type.equalsIgnoreCase("ears");
    }

    @Override
    public String getType() {
        return "ears";
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
        return EarPart.valueOf(obj.get("enum").getAsString());
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
        return this != normal;
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
        return "";
    }

    @Override
    public int compare(BodyPart other) {
        return 0;
    }

    @Override
    public boolean isVisible(Character c) {
        return true;
    }

    @Override
    public double applySubBonuses(Character self, Character opponent, BodyPart with, BodyPart target, double damage,
                    Combat c) {
        return 0;
    }

    @Override
    public int mod(Attribute a, int total) {
        if (a == Attribute.Seduction) {
            switch (this) {
                case pointed:
                    return 2;
                case cat:
                    return 3;
                default:
                    return 0;
            }
        }
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
        if (this == normal) {
            return BodyPartMod.noMod;
        }
        return this;
    }

    @Override
    public String getModType() {
        return name();
    }
}
