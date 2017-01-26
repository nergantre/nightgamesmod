package nightgames.characters.body;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.mods.PartMod;
import nightgames.characters.body.mods.SizeMod;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.status.Sensitized;

public class CockPart extends GenericBodyPart {
    public static String synonyms[] = {"cock", "dick", "shaft", "phallus"};

    public static CockPart generic = new CockPart();

    public static CockPart generateGeneric() {
        return new CockPart(0, 1.2, 1);
    }

    public CockPart(double hotness, double pleasure, double sensitivity) {
        super("cock", "", hotness, pleasure, sensitivity, false, "cock", "a ");
    }

    public CockPart() {
        this(0, 1.2, 1);
    }

    @Override
    public double getFemininity(Character c) {
        return SizeMod.COCK_SIZE_SMALL - getSize();
    }

    @Override
    public int mod(Attribute a, int total) { 
        int bonus = super.mod(a, total);
        int size = getSize();
        if (size > SizeMod.COCK_SIZE_AVERAGE & a == Attribute.Seduction) {
            bonus += (size - SizeMod.COCK_SIZE_AVERAGE) * 2;
        }
        if (size > SizeMod.COCK_SIZE_BIG & a == Attribute.Speed) {
            bonus += (size - SizeMod.COCK_SIZE_BIG);
        }
        return bonus;
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
    protected String modlessDescription(Character c) {
        String syn = Global.pickRandom(synonyms).get();
        return (c.hasPussy() && !moddedPartCountsAs(c, CockMod.incubus) ? "girl-" : "") + syn;
    }

    @Override
    public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = super.applyBonuses(self, opponent, target, damage, c);
        if (self.has(Trait.polecontrol) && self.canRespond()) {
            String desc = "";
            if (self.has(Trait.polecontrol)) {
                desc += "expert ";
            }
            c.write(self, Global.format(
                            "{self:SUBJECT-ACTION:use|uses} {self:possessive} " + desc
                                            + "cock control to grind against {other:name-possessive} inner walls, making {other:possessive} knuckles whiten as {other:pronoun} {other:action:moan|moans} uncontrollably.",
                            self, opponent));
            bonus += self.has(Trait.polecontrol) ? 8 : 0;
        }
        return bonus;
    }

    @Override
    public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {
        super.tickHolding(c, self, opponent, otherOrgan);
    }

    @Override
    public double applyReceiveBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c) {
        double bonus = super.applyReceiveBonuses(self, opponent, target, damage, c);
        if ((opponent.has(Trait.dickhandler) || opponent.has(Trait.anatomyknowledge)) && opponent.canRespond() && c.getStance().mobile(opponent)) {
            c.write(opponent,
                            Global.format("{other:NAME-POSSESSIVE} expert handling of {self:name-possessive} cock causes {self:subject} to shudder uncontrollably.",
                                            self, opponent));
            if (opponent.has(Trait.dickhandler)) {
                bonus += 5;
            }
            if (opponent.has(Trait.anatomyknowledge)) {
                bonus += 5;
            }
        }
        if (self.has(Trait.druglacedprecum) && !opponent.isPartProtected(target)) {
            opponent.add(c, new Sensitized(opponent, target, .2, 2.0, 20));
            c.write(self, Global.format("{self:NAME-POSSESSIVE} drug-laced precum is affecting {other:direct-object}.",
                            self, opponent));
        }
        return bonus;
    }

    @Override
    public boolean isReady(Character c) {
        return c.getArousal().percent() >= 15 || c.has(Trait.alwaysready);
    }

    public String getFluidsNoMods(Character c) {
        return "semen";
    }

    @Override
    public boolean getDefaultErogenous() {
        return true;
    }

    @Override
    public double priority(Character c) {
        return 2 + (c.has(Trait.polecontrol)? 2 : 0) + (c.has(Trait.druglacedprecum) ? 1 : 0)+ (c.has(Trait.hypnoticsemen) ? 2 : 0);
    }

    @Override
    public String adjective() {
        return "phallic";
    }

    @Override
    public boolean isVisible(Character c) {
        return c.crotchAvailable() || getSize() > SizeMod.COCK_SIZE_AVERAGE;
    }

    @Override
    public void describeLong(StringBuilder b, Character c) {
        b.append("There is a ");
        if (c.crotchAvailable()) {
            b.append(fullDescribe(c));
            if (c.getArousal().percent() <= 15) {
                b.append(" hanging between ");
            } else if (c.getArousal().percent() <= 50 ) {
                b.append(" erect between ");
            } else {
                b.append(" proudly erect between ");
            }
            b.append(c.nameOrPossessivePronoun());
            b.append(" legs.");
        } else {
            b.append(getSizeMod().adjective(this));
            b.append(" bulge in ");
            b.append(c.possessiveAdjective());
            b.append(" ");
            b.append(Optional.ofNullable(c.getOutfit().getTopOfSlot(ClothingSlot.bottom)).map(Clothing::getName).orElse("outfit"));
            b.append(" where ");
            b.append(c.possessiveAdjective());
            b.append(" crotch is.");
        }
    }

    public BodyPart upgrade() {
        return this.applyMod(new SizeMod(SizeMod.clampToValidSize(this, getSize() + 1)));
    }

    public BodyPart downgrade() {
        return this.applyMod(new SizeMod(SizeMod.clampToValidSize(this, getSize() - 1)));
    }

    public PussyPart getEquivalentPussy() {
        List<PartMod> newMods = getPartMods().stream().map(BodyUtils.EQUIVALENT_MODS::get).filter(mod -> mod != null).distinct().collect(Collectors.toList());
        GenericBodyPart newPart = PussyPart.generateGeneric();
        for (PartMod mod : newMods) {
            newPart = (GenericBodyPart)newPart.applyMod(mod);
        }
        return (PussyPart)newPart;
    }
}
