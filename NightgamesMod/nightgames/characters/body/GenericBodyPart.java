package nightgames.characters.body;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.json.JsonUtils;

public class GenericBodyPart implements BodyPart {
    /**
     *
     */
    public String type;
    public String desc;
    public String prefix;
    public double hotness;
    public double sensitivity;
    public double pleasure;
    public String descLong;
    private boolean notable;

    public GenericBodyPart(String desc, String descLong, double hotness, double pleasure, double sensitivity,
                    boolean notable, String type, String prefix) {
        this.desc = desc;
        this.descLong = descLong;
        this.hotness = hotness;
        this.pleasure = pleasure;
        this.sensitivity = sensitivity;
        this.type = type;
        this.notable = notable;
        this.prefix = prefix;
    }

    public GenericBodyPart(String desc, double hotness, double pleasure, double sensitivity, String type,
                    String prefix) {
        this(desc, "", hotness, pleasure, sensitivity, false, type, prefix);
    }

    public GenericBodyPart(String desc, double hotness, double pleasure, double sensitivity, boolean notable,
                    String type, String prefix) {
        this(desc, "", hotness, pleasure, sensitivity, notable, type, prefix);
    }

    public GenericBodyPart(GenericBodyPart original) {
        this(original.desc, original.descLong, original.hotness, original.pleasure, original.sensitivity,
                        original.notable, original.type, original.prefix);
    }

    public GenericBodyPart() {
        this("generic", "a generic body part", 0, 0, 0, false, "generic", "");
    }

    @Override
    public String canonicalDescription() {
        return desc;
    }

    @Override
    public void describeLong(StringBuilder b, Character c) {
        String parsedDesc = Global.format(descLong, c, c);
        b.append(parsedDesc);
    }

    @Override
    public boolean isType(String type) {
        return this.getType().equalsIgnoreCase(type);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String describe(Character c) {
        return desc;
    }

    @Override
    public double priority(Character c) {
        return (getPleasure(c, null) - 1) * 3;
    }

    @Override
    public String fullDescribe(Character c) {
        if (notable) {
            return desc;
        } else {
            return "normal " + desc;
        }
    }

    @Override
    public String toString() {
        return fullDescribe(null);
    }

    @Override
    public double getHotness(Character self, Character opponent) {
        return hotness;
    }

    @Override
    public double getPleasure(Character self, BodyPart target) {
        double pleasureMod = pleasure;
        if (type.equals("hands") || type.equals("feet")) {
            pleasureMod += self.has(Trait.limbTraining1) ? .5 : 0;
            pleasureMod += self.has(Trait.limbTraining2) ? .7 : 0;
            pleasureMod += self.has(Trait.limbTraining3) ? .7 : 0;
            pleasureMod += self.has(Trait.dexterous) ? .4 : 0;
        }
        if (type.equals("hands")) {
            pleasureMod += self.has(Trait.pimphand) ? .2 : 0;
        }
        return pleasureMod;
    }

    @Override
    public double getSensitivity(BodyPart target) {
        return sensitivity;
    }

    @Override
    public boolean isReady(Character self) {
        return true;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        if (!(other instanceof GenericBodyPart))
            return false;
        return toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return (type + ":" + toString()).hashCode();
    }

    public JsonObject toJson() {
        return JsonUtils.gson.toJsonTree(this, this.getClass()).getAsJsonObject();
    }

    public BodyPart fromJson(JsonObject object) {
        return JsonUtils.gson.fromJson(object, this.getClass());
    }

    @Override public JsonObject save() {
        return toJson();
    }

    @Override public BodyPart load(JsonObject obj) {
        return fromJson(obj);
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        int bonus = 0;
        if (self.has(ClothingTrait.nursegloves) && type.equals("hands")) {
            c.write(self, Global
                            .format("{self:name-possessive} rubber gloves provide an unique sensation as {self:subject-action:run|runs} {self:possessive} hands over {other:possessive} "
                                            + target.describe(opponent) + ".", self, opponent));
            bonus += 5 + Global.random(5);
            if (Global.random(5) == 0) {
                c.write(self, "Unfortunately, the gloves wear out with their usage.");
                self.shred(ClothingSlot.arms);
            }
        }
        if (type.equals("hands") && self.has(Trait.defthands)) {
            c.write(self, Global
                            .format("{self:name-possessive} hands dance across {other:possessive} "
                                            + target.describe(opponent) + ", hitting all the right spots.", self, opponent));
            bonus += Global.random(2, 6);
        }
        if (type.equals("feet") && self.has(Trait.nimbletoes)) {
            c.write(self, Global
                            .format("{self:name-possessive} nimble toes adeptly massage {other:possessive} "
                                            + target.describe(opponent) + " elicting a quiet gasp.", self, opponent));
            bonus += Global.random(2, 6);
        }
        return bonus;
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
        return notable;
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
        return true;
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
        return BodyPartMod.noMod;
    }
}
