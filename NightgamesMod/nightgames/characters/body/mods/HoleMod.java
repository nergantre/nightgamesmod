package nightgames.characters.body.mods;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BodyPartMod;
import nightgames.combat.Combat;

public abstract class HoleMod implements BodyPartMod, Comparable<HoleMod> {
    private final String modType;
    private final double hotness;
    private final double pleasure;
    private final double sensitivity;
    private int sortOrder;

    public HoleMod(String modType, double hotness, double pleasure, double sensitivity, int sortOrder) {
        this.modType = modType;
        this.hotness = hotness;
        this.pleasure = pleasure;
        this.sensitivity = sensitivity;
        this.sortOrder = sortOrder;
    }

    @Override
    public String getModType() {
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
    
    public double modPleasure(Character self) {
        return pleasure;
    }

    public double modHotness(Character self) {
        return hotness;
    }

    public double modSensitivity(Character self) {
        return sensitivity;
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

    public Optional<String> getLongDescriptionOverride(Character self, BodyPart part) {
        return Optional.empty();
    }

    public Optional<String> getDescriptionOverride(Character self, BodyPart part) {
        return Optional.empty();
    }

    @Override
    public int compareTo(HoleMod other) {
        return Integer.compare(sortOrder, other.sortOrder);
    }
}