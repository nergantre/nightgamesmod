package nightgames.characters.body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.mods.HoleMod;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.json.JsonUtils;
import nightgames.status.Abuff;
import nightgames.status.Stsflag;
import nightgames.status.Trance;

public class AssPart extends GenericBodyPart {
    public static int SIZE_SMALL = 0;
    public static int SIZE_NORMAL = 1;
    public static int SIZE_BIG = 2;
    public static int SIZE_HUGE = 3;
    public static AssPart generic = new AssPart("ass", 0, 1.2, SIZE_NORMAL);
    private int size;
    private List<HoleMod> mods;
    private static final Map<Integer, String> SIZE_DESCRIPTIONS = new HashMap<>(); 
    static {
        SIZE_DESCRIPTIONS.put(SIZE_SMALL, "small ");
        SIZE_DESCRIPTIONS.put(SIZE_NORMAL, "");
        SIZE_DESCRIPTIONS.put(SIZE_BIG, "large ");
        SIZE_DESCRIPTIONS.put(SIZE_HUGE, "huge ");
    }

    public JsonObject toJson() {
        return JsonUtils.gson.toJsonTree(this, this.getClass()).getAsJsonObject();
    }

    public BodyPart fromJson(JsonObject object) {
        return JsonUtils.gson.fromJson(object, this.getClass());
    }

    public static AssPart generateGeneric() {
        return new AssPart("ass", 0, 1.2, 1);
    }

    public AssPart(String desc, String longDesc, double hotness, double pleasure, double sensitivity, int size) {
        super(desc, longDesc, hotness, pleasure, sensitivity, false, "ass", "a ");
        this.mods = new ArrayList<>();
        this.size = size;
    }

    public AssPart(String desc, double hotness, double pleasure, double sensitivity) {
        this(desc, "", hotness, pleasure, sensitivity, SIZE_NORMAL);
    }

    public AssPart() {
        this("ass", 0, 1.2, 1);
    }

    @Override
    public boolean isNotable() {
        return mods.stream().map(mod -> mod.getDescriptionOverride(Global.noneCharacter(), this)).anyMatch(Optional::isPresent);
    }

    @Override
    public String canonicalDescription() {
        return mods.stream().sorted().map(HoleMod::getModType).collect(Collectors.joining(" ")) + " " + desc;
    }

    @Override
    public double getFemininity(Character c) {
        return size - SIZE_NORMAL;
    }

    @Override
    public int mod(Attribute a, int total) { 
        int bonus = 0;
        for (HoleMod mod : mods) {
            bonus += mod.mod(a, total);
        }
        if (size > SIZE_NORMAL & a == Attribute.Seduction) {
            bonus += (size - SIZE_NORMAL) * 2;
        }
        if (size > SIZE_BIG & a == Attribute.Speed) {
            bonus += (size - SIZE_BIG);
        }
        return bonus;
    }

    @Override
    public String fullDescribe(Character c) {
        return SIZE_DESCRIPTIONS.get(size) + describe(c);
    }

    @Override
    public String describe(Character c) {
        Optional<String> override = mods.stream().map(mod -> mod.getDescriptionOverride(c, this)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
        String normalDescription = super.describe(c);
        if (override.isPresent()) {
            normalDescription = adjective() + " " +  override.get();
        }

        return mods.stream().sorted()
                        .filter(mod -> !mod.getDescriptionOverride(c, this).isPresent())
                        .map(HoleMod::getModType)
                        .map(string -> string + " ")
                        .collect(Collectors.joining())
                        + normalDescription;
    }

    @Override
    public void describeLong(StringBuilder b, Character c) {
        Optional<String> override = mods.stream().map(mod -> mod.getDescriptionOverride(c, this)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
        if (override.isPresent()) {
            b.append(override);
        }
    }

    @Override
    public double getPleasure(Character self, BodyPart target) {
        double pleasureMod = super.getPleasure(self, target);
        double pleasureBonus = 1.0;
        for (HoleMod mod : mods) {
            pleasureBonus += mod.modPleasure(self);
        }
        pleasureMod *= pleasureBonus;
        pleasureMod += self.has(Trait.analTraining1) ? .5 : 0;
        pleasureMod += self.has(Trait.analTraining2) ? .7 : 0;
        pleasureMod += self.has(Trait.analTraining3) ? .7 : 0;
        return pleasureMod;
    }

    @Override
    public double getHotness(Character self, Character opponent) {
        double hotnessMod = super.getHotness(self, opponent);
        double bonus = 1.0;
        for (HoleMod mod : mods) {
            bonus += mod.modHotness(self);
        }
        return hotnessMod * bonus;
    }

    @Override
    public double getSensitivity(Character self, BodyPart target) {
        double sensitivityMod = super.getSensitivity(self, target);
        double bonus = 1.0;
        for (HoleMod mod : mods) {
            bonus += mod.modSensitivity(self);
        }
        return sensitivityMod * bonus;
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = 0;
        for (HoleMod mod : mods) {
            bonus += mod.applyBonuses(c, self, opponent, this, target, damage);
        }
        if (self.has(Trait.oiledass) && c.getStance().anallyPenetratedBy(c, self, opponent)) {
            c.write(self, Global.format(
                            "{self:NAME-POSSESSIVE} naturally oiled asshole swallows {other:name-possessive} cock with ease.",
                            self, opponent));
            bonus += 5;
        }

        if ((self.has(Trait.tight) || self.has(Trait.holecontrol)) && c.getStance().anallyPenetrated(c, self)) {
            String desc = "";
            if (self.has(Trait.tight)) {
                desc += "powerful ";
            }
            if (self.has(Trait.holecontrol)) {
                desc += "well-trained ";
            }
            c.write(self, Global.format(
                            "{self:SUBJECT-ACTION:use|uses} {self:possessive} " + desc
                                            + "sphincter muscles to milk {other:name-possessive} cock, adding to the pleasure.",
                            self, opponent));
            bonus += self.has(Trait.tight) && self.has(Trait.holecontrol) ? 10 : 5;
            if (self.has(Trait.tight)) {
                opponent.pain(c, self, Math.min(30, self.get(Attribute.Power)));
            }
        }
        if (self.has(Trait.drainingass) && !opponent.has(Trait.strapped) && c.getStance().anallyPenetratedBy(c, self, opponent)) {
            if (Global.random(3) == 0) {
                c.write(self, Global.format("{self:name-possessive} ass seems to <i>inhale</i>, drawing"
                                + " great gouts of {other:name-possessive} strength from {other:possessive}"
                                + " body.", self, opponent));
                opponent.drain(c, self, self.getLevel());
                opponent.add(c, new Abuff(opponent, Attribute.Power, -3, 10));
                self.add(c, new Abuff(self, Attribute.Power, 3, 10));
            } else {
                c.write(self, Global.format("The feel of {self:name-possessive} ass around"
                                + " {other:name-possessive} {other:body-part:cock} drains"
                                + " {other:direct-object} of {other:possessive} energy.", self, opponent));
                opponent.drain(c, self, self.getLevel()/2);
            }
        }
        return bonus;
    }

    // Should be called whenever a combatant is penetrated in any way
    public void onStartPenetration(Combat c, Character self, Character opponent, BodyPart target) {
        super.onStartPenetration(c, self, opponent, target);
        for (HoleMod mod : mods) {
            mod.onStartPenetration(c, self, opponent, this, target);
        }
    }

    // Should be called when either combatant orgasms
    public void onOrgasm(Combat c, Character self, Character opponent) {
        super.onOrgasm(c, self, opponent);
        for (HoleMod mod : mods) {
            mod.onOrgasm(c, self, opponent, this);
        }
    }

    // Should be called when either combatant orgasms in/with body parts
    public void onOrgasmWith(Combat c, Character self, Character opponent, BodyPart other, boolean selfCame) {
        super.onOrgasmWith(c, self, opponent, other, selfCame);
        for (HoleMod mod : mods) {
            mod.onOrgasmWith(c, self, opponent, this, other, selfCame);
        }
    }

    @Override
    public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
        for (HoleMod mod : mods) {
            mod.tickHolding(c, self, opponent, this, otherOrgan);
        }
        if (self.has(Trait.autonomousAss)) {
            c.write(self, Global.format(
                            "{self:NAME-POSSESSIVE} " + fullDescribe(self)
                                            + " churns against {other:name-possessive} cock, "
                                            + "seemingly with a mind of its own. {self:POSSESSIVE} internal muscles feel like a hot fleshy hand inside her asshole, jerking {other:possessive} shaft.",
                            self, opponent));
            opponent.body.pleasure(self, this, otherOrgan, 10, c);
        }
    }

    @Override
    public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = 0;
        for (HoleMod mod : mods) {
            bonus += mod.applyReceiveBonuses(c, self, opponent, this, target, damage);
        }
        if (opponent.has(Trait.asshandler) || opponent.has(Trait.anatomyknowledge)) {
            c.write(opponent,
                            Global.format("{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} ass causes {self:subject} to shudder uncontrollably.",
                                            self, opponent));
            if (opponent.has(Trait.asshandler)) {
                bonus += 5;
            }
            if (opponent.has(Trait.anatomyknowledge)) {
                bonus += 5;
            }
        }
        if (self.has(Trait.buttslut)) {
            bonus += 10;
            if (Global.random(4) == 0 && self.is(Stsflag.trance)) {
                c.write(opponent, Global.format(
                                "The foreign object rummaging around inside {self:name-possessive} ass <i><b>just feels so right</b></i>. {self:SUBJECT-ACTION:feel|feels} {self:reflective} slipping into a trance!",
                                                self, opponent));
                self.add(c, new Trance(self, 3, false));
            }
            c.write(opponent, Global.format(
                            "The foreign object rummaging around inside {self:name-possessive} ass feels so <i>right</i>. {self:SUBJECT} can't help moaning in time with the swelling pleasure.",
                                            self, opponent));
        }
        return bonus;
    }

    @Override
    public boolean isReady(Character c) {
        return c.has(Trait.oiledass) || c.is(Stsflag.oiled);
    }

    @Override
    public String getFluids(Character c) {
        Optional<String> nonJuicesMod = mods.stream().filter(mod -> mod.getFluids().isPresent() && !mod.getFluids().equals("juices")).findFirst().flatMap(HoleMod::getFluids);
        if (nonJuicesMod.isPresent()) {
            return nonJuicesMod.get();
        }
        if (c.has(Trait.oiledass)) {
            return "oils";
        }
        Optional<String> anyMod = mods.stream().filter(mod -> mod.getFluids().isPresent()).findFirst().flatMap(HoleMod::getFluids);
        return anyMod.orElse("");
    }

    @Override
    public boolean isErogenous() {
        return true;
    }

    @Override
    public double priority(Character c) {
            return (c.has(Trait.tight) ? 1 : 0) + (c.has(Trait.holecontrol) ? 1 : 0) + (c.has(Trait.oiledass) ? 1 : 0)
                            + (c.has(Trait.autonomousAss) ? 4 : 0);
    }
    public int counterValue(BodyPart otherPart, Character self, Character other) {
        int counterValue = 0;
        for (HoleMod mod : mods) {
            counterValue += mod.counterValue(this, otherPart, self, other);
        }
        return counterValue;
    }

    @Override
    public String adjective() {
        return "anal";
    }

    private AssPart instance() {
        AssPart newPart = new AssPart(desc, descLong, hotness, pleasure, sensitivity, size);
        newPart.mods = new ArrayList<>(mods);
        return newPart;
    }

    public AssPart applyMod(HoleMod mod) {
        AssPart newPart = instance();
        newPart.mods.add(mod);
        return newPart;
    }

    public BodyPart upgrade() {
        AssPart newPart = instance();
        newPart.size = Global.clamp(newPart.size + 1, SIZE_SMALL, SIZE_HUGE);
        return newPart;
    }

    public BodyPart downgrade() {
        AssPart newPart = instance();
        newPart.size = Global.clamp(newPart.size - 1, SIZE_SMALL, SIZE_HUGE);
        return newPart;
    }

    public List<? extends BodyPartMod> getMods(Character npc) {
        return mods;
    }
}
