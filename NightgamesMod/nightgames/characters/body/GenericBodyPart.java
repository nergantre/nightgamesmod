package nightgames.characters.body;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.mods.PartMod;
import nightgames.combat.Combat;
import nightgames.global.DebugFlags;
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
    private List<PartMod> mods;

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
        this.mods = new ArrayList<>();
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
        return mods.stream().sorted().map(PartMod::getModType).collect(Collectors.joining(" ")) + " " + desc;
    }

    @Override
    public void describeLong(StringBuilder b, Character c) {
        String parsedDesc = Global.format(descLong, c, c);
        for (PartMod mod : mods) {
            parsedDesc = mod.getLongDescriptionOverride(c, this, parsedDesc);
        }
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

    protected String modlessDescription(Character c) {
        return desc;
    }
    
    public String getModDescriptorString(Character c) {
        return mods.stream().sorted()
        .filter(mod -> !mod.getDescriptionOverride(c, this).isPresent())
        .map(mod -> mod.adjective(this))
        .filter(s -> !s.isEmpty())
        .map(string -> string + " ")
        .collect(Collectors.joining());
    }

    @Override
    public String describe(Character c) {
        Optional<String> override = mods.stream().map(mod -> mod.getDescriptionOverride(c, this)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
        String normalDescription = modlessDescription(c);
        if (override.isPresent()) {
            normalDescription = adjective() + " " +  override.get();
        }

        return getModDescriptorString(c) + normalDescription;
    }

    @Override
    public double priority(Character c) {
        return (getPleasure(c, null) - 1) * 3;
    }

    @Override
    public String fullDescribe(Character c) {
        if (isNotable()) {
            return describe(c);
        } else {
            return "normal " + describe(c);
        }
    }

    @Override
    public String toString() {
        return fullDescribe(null);
    }

    @Override
    public double getHotness(Character self, Character opponent) {
        double hotnessMod = hotness;
        double bonus = 1.0;
        for (PartMod mod : mods) {
            bonus += mod.modHotness(self);
        }
        return hotnessMod * bonus;
    }

    @Override
    public double getPleasure(Character self, BodyPart target) {
        double pleasureMod = pleasure;
        double pleasureBonus = 1.0;
        for (PartMod mod : mods) {
            pleasureBonus += mod.modPleasure(self);
        }
        pleasureMod *= pleasureBonus;
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
    public double getSensitivity(Character self, BodyPart target) {
        double sensitivityMod = sensitivity;
        double bonus = 1.0;
        for (PartMod mod : mods) {
            bonus += mod.modSensitivity(self);
        }
        return sensitivityMod * bonus;
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
        return canonicalDescription().equals(((GenericBodyPart)other).canonicalDescription());
    }

    @Override
    public int hashCode() {
        return (type + ":" + canonicalDescription()).hashCode();
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
    public void onStartPenetration(Combat c, Character self, Character opponent, BodyPart target) {
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.printf("Starting Penetration for %s -> (%s, %s, %s)\n", describe(self), self, opponent,
                            target.describe(opponent));
        }
        for (PartMod mod : mods) {
            mod.onStartPenetration(c, self, opponent, this, target);
        }
    }

    @Override
    public void onOrgasm(Combat c, Character self, Character opponent) {
        for (PartMod mod : mods) {
            mod.onOrgasm(c, self, opponent, this);
        }
    }

    @Override
    public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
        for (PartMod mod : mods) {
            mod.tickHolding(c, self, opponent, this, otherOrgan);
        }
    }

    @Override
    public void onOrgasmWith(Combat c, Character self, Character opponent, BodyPart other, boolean selfCame) {
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.printf("Processing OrgasmWith for %s -> (%s, %s, %s, %s)\n", describe(self), self, opponent,
                            other.describe(opponent), Boolean.toString(selfCame));
        }
        for (PartMod mod : mods) {
            mod.onOrgasmWith(c, self, opponent, this, other, selfCame);
        }
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        int bonus = 0;
        for (PartMod mod : mods) {
            bonus += mod.applyBonuses(c, self, opponent, this, target, damage);
        }
        if (self.has(ClothingTrait.nursegloves) && type.equals("hands")) {
            c.write(self, Global
                            .format("{self:name-possessive} rubber gloves provide a unique sensation as {self:subject-action:run|runs} {self:possessive} hands over {other:possessive} "
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

    public String getFluidsNoMods(Character c) {
        return "";
    }

    @Override
    public String getFluids(Character c) {
        Optional<String> nonJuicesMod = mods.stream().filter(mod -> mod.getFluids().isPresent() && !mod.getFluids().equals("juices")).findFirst().flatMap(PartMod::getFluids);
        if (nonJuicesMod.isPresent()) {
            return nonJuicesMod.get();
        }
        Optional<String> anyMod = mods.stream().filter(mod -> mod.getFluids().isPresent()).findFirst().flatMap(PartMod::getFluids);
        return anyMod.orElse(getFluidsNoMods(c));
    }

    @Override
    public final boolean isErogenous() {
        if (mods.stream().anyMatch(mod -> mod.getErogenousOverride().isPresent())) {
            return mods.stream().map(mod -> mod.getErogenousOverride()).filter(Optional::isPresent).map(Optional::get).reduce(false, (a, b) -> a || b);            
        }
        return getDefaultErogenous();
    }
    
    protected boolean getDefaultErogenous() {
        return false;
    }

    @Override
    public boolean isNotable() {
        return notable || !mods.isEmpty();
    }

    @Override
    public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = 0;
        for (PartMod mod : mods) {
            bonus += mod.applyReceiveBonuses(c, self, opponent, this, target, damage);
        }
        return bonus;
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
        int bonus = 0;
        for (PartMod mod : mods) {
            bonus += mod.mod(a, total);
        }
        return bonus;
    }

    @Override
    public int counterValue(BodyPart otherPart, Character self, Character other) {
        int counterValue = 0;
        for (PartMod mod : mods) {
            counterValue += mod.counterValue(this, otherPart, self, other);
        }
        return counterValue;
    }

    @Override
    public String adjective() {
        // implement when needed
        return type;
    }

    public BodyPart instance() {
        return this.fromJson(this.toJson());
    }

    public BodyPart applyMod(PartMod mod) {
        GenericBodyPart newPart = (GenericBodyPart) instance();
        if (!newPart.mods.contains(mod)) {
            newPart.mods.add(mod);
        }
        return newPart;
    }

    public BodyPart removeAllMods() {
        GenericBodyPart part = (GenericBodyPart) instance();
        part.mods.clear();
        return part;
    }

    public List<? extends BodyPartMod> getMods(Character npc) {
        return mods;
    }

    protected List<PartMod> getPartMods() {
        return mods;
    }
}