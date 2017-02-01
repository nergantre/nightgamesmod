package nightgames.characters.body.mods;

import java.util.Optional;

import com.google.gson.JsonElement;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BodyPartMod;
import nightgames.characters.body.GenericBodyPart;
import nightgames.combat.Combat;

public abstract class PartMod implements BodyPartMod, Comparable<PartMod> {
    protected String modType;
    protected double hotness;
    protected double pleasure;
    protected double sensitivity;
    protected int sortOrder;

    public PartMod(String modType, double hotness, double pleasure, double sensitivity, int sortOrder) {
        this.modType = modType;
        this.hotness = hotness;
        this.pleasure = pleasure;
        this.sensitivity = sensitivity;
        this.sortOrder = sortOrder;
    }

    /*
     * Implement these if necessary to load more data than the default construct can.
     */
    public void loadData(JsonElement element) {}
    public JsonElement saveData() { return null; }

    /**
     * This should be overridden if there needs to be more than only one variant of the mod active at the same time.
     */
    public String getVariant() {
        return modType;
    }

    @Override
    public String getModType() {
        return modType;
    }

    public String adjective(GenericBodyPart part) {
        return modType;
    }

    // override these if needed
    public double applyReceiveBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { return 0; }
    public void onStartPenetration(Combat c, Character self, Character opponent, BodyPart part, BodyPart target) {}
    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { return 0; }
    public int mod(Attribute a, int total) { return 0; }
    public void tickHolding(Combat c, Character self, Character opponent, BodyPart part, BodyPart otherOrgan) {}
    public int counterValue(BodyPart part, BodyPart otherPart, Character self, Character other) { return 0; }
    public void onOrgasm(Combat c, Character self, Character opponent, BodyPart part) {}
    public void onOrgasmWith(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, boolean selfCame) {}
    public void receiveCum(Combat c, Character self, BodyPart part, Character donor, BodyPart sourcePart) {}
    
    public double modPleasure(Character self) {
        return getBasePleasure();
    }

    public double modHotness(Character self) {
        return getBaseHotness();
    }

    public double modSensitivity(Character self) {
        return getBaseSensitivity();
    }

    protected String lowerOrRear(BodyPart part) {
        if (part.isType("ass")) {
            return "rear";
        } else {
            return "lower";
        }
    }

    public Optional<String> getFluids() {
        return Optional.empty();
    }

    public Optional<Boolean> getErogenousOverride() {
        return Optional.empty();
    }

    public String getLongDescriptionOverride(Character self, BodyPart part, String previousDescription) {
        return previousDescription;
    }

    public Optional<String> getDescriptionOverride(Character self, BodyPart part) {
        return Optional.empty();
    }

    @Override
    public int compareTo(PartMod other) {
        return Integer.compare(getSortOrder(), other.getSortOrder());
    }

    protected int getSortOrder() {
        return sortOrder;
    }

    public double getBaseHotness() {
        return hotness;
    }

    public double getBasePleasure() {
        return pleasure;
    }

    public double getBaseSensitivity() {
        return sensitivity;
    }

    public abstract String describeAdjective(String partType);
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof PartMod) {
            return other.toString().equals(this.toString());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "PartMod:" + getClass().getSimpleName() + ":" + modType;
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}