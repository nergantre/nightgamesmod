package nightgames.characters.body;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.mods.PartMod;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class PussyPart extends GenericBodyPart {
    public static PussyPart generic = generateGeneric();

    public static PussyPart generateGeneric() {
        return new PussyPart();
    }

    public PussyPart(double hotness, double pleasure, double sensitivity) {
        super("pussy", "", hotness, pleasure, sensitivity, true, "pussy", "a ");
    }

    @Override
    public void describeLong(StringBuilder b, Character c) {
        Optional<String> override = getPartMods().stream()
                        .map(mod -> mod.getDescriptionOverride(c, this))
                        .filter(Optional::isPresent).findFirst().flatMap(Function.identity());
        if (override.isPresent()) {
            b.append(override);
            return;
        }
        b.append("A ");
        if (c.getArousal()
             .percent() > 15
                        && c.getArousal()
                            .percent() < 60) {
            b.append("moist ");
        } else if (c.getArousal()
                    .percent() >= 60) {
            b.append("drenched ");
        }
        if (this.isGeneric(c)) {
            b.append(Global.pickRandom(Arrays.asList("perfectly ordinary ", "normal ", "ordinary ")).get());
        }
        b.append(describe(c));
        b.append(' ');
        b.append("is nested between " + c.nameOrPossessivePronoun() + " legs.");
    }

    public PussyPart() {
        this(0, 1.2, 1);
    }

    @Override
    public double getFemininity(Character c) {
        return 3;
    }

    public static String synonyms[] = {"pussy", "vagina", "slit", "cunt"};

    @Override
    public String describe(Character c) {
        String syn = Global.pickRandom(synonyms).get();
        String origString = super.describe(c);
        return origString.replace("pussy", syn);
    }

    @Override
    public String fullDescribe(Character c) {
        return Global.pickRandom(Arrays.asList("normal ", "ordinary ")).get() + this.describe(c);
    }

    @Override
    public double getPleasure(Character self, BodyPart target) {
        double pleasureMod = super.getPleasure(self, target);
        pleasureMod += self.has(Trait.sexTraining1) ? .5 : 0;
        pleasureMod += self.has(Trait.sexTraining2) ? .7 : 0;
        pleasureMod += self.has(Trait.sexTraining3) ? .7 : 0;
        return pleasureMod;
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = super.applyBonuses(self, opponent, target, damage, c);
        if (self.canRespond() && (self.has(Trait.tight) || self.has(Trait.holecontrol)) && c.getStance()
                                                                       .vaginallyPenetrated(c, self)
                                                                       && target.isType("cock")) {
            String desc = "";
            if (self.has(Trait.tight)) {
                desc += "powerful ";
            }
            if (self.has(Trait.holecontrol)) {
                desc += "well-trained ";
            }
            c.write(self, Global.format(
                            "{self:SUBJECT-ACTION:use|uses} {self:possessive} " + desc
                                            + "vaginal muscles to milk {other:name-possessive} cock, adding to the pleasure.",
                            self, opponent));
            bonus += self.has(Trait.tight) && self.has(Trait.holecontrol) ? 10 : 5;
        }
        return bonus;
    }

    @Override
    public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
        super.tickHolding(c, self, opponent, otherOrgan);
        if (self.has(Trait.autonomousPussy)) {
            c.write(self, Global.format(
                            "{self:NAME-POSSESSIVE} " + fullDescribe(self)
                                            + " churns against {other:name-possessive} cock, "
                                            + "seemingly with a mind of its own. Warm waves of flesh rub against {other:possessive} shaft, elliciting groans of pleasure from {other:direct-object}.",
                            self, opponent));
            opponent.body.pleasure(self, this, otherOrgan, 10, c);
        }
    }

    @Override
    public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = super.applyReceiveBonuses(self, opponent, target, damage, c);
        if (opponent.canRespond() && (opponent.has(Trait.pussyhandler) || opponent.has(Trait.anatomyknowledge)) && c.getStance().mobile(opponent)) {
            c.write(opponent,
                            Global.format("{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} pussy causes {self:subject} to shudder uncontrollably.",
                                            self, opponent));
            if (opponent.has(Trait.pussyhandler)) {
                bonus += 5;
            }
            if (opponent.has(Trait.anatomyknowledge)) {
                bonus += 5;
            }
        }
        return bonus;
    }

    @Override
    public boolean isReady(Character c) {
        return c.has(Trait.alwaysready) || c.getArousal().percent() >= 15;
    }

    public String getFluidsNoMods(Character c) {
        return "juices";
    }

    @Override
    public boolean getDefaultErogenous() {
        return true;
    }

    @Override
    public double priority(Character c) {
        return (c.has(Trait.tight) ? 1 : 0) + (c.has(Trait.holecontrol) ? 1 : 0) + (c.has(Trait.autonomousPussy) ? 4 : 0);
    }

    @Override
    public String adjective() {
        return "anal";
    }

    public BodyPart upgrade() {
        return this;
    }

    public BodyPart downgrade() {
        return this;
    }

    public CockPart getEquivalentCock() {
        List<PartMod> newMods = getPartMods().stream()
                        .map(mod -> BodyUtils.getKeyFromValue(BodyUtils.EQUIVALENT_MODS, mod))
                        .filter(mod -> mod.isPresent())
                        .map(Optional::get)
                        .distinct()
                        .collect(Collectors.toList());
        GenericBodyPart newPart = GenericCockPart.generateGeneric();
        for (PartMod mod : newMods) {
            newPart = (GenericBodyPart)newPart.applyMod(mod);
        }
        return (CockPart)newPart;
    }
}
