package nightgames.characters.body.mods;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.GenericBodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class FieryMod extends PartMod {
    public static final FieryMod INSTANCE = new FieryMod();

    public FieryMod() {
        super("fiery", 0, .3, .2, -11);
    }

    public String adjective(GenericBodyPart part) {
        if (part.getType().equals("pussy")) {
            return "fiery";
        }
        if (part.getType().equals("ass")) {
            return "molten";
        }
        return "red-hot";
    }

    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { 
        if (!opponent.stunned()) {
            if (target.moddedPartCountsAs(opponent, CockMod.primal)) {
                c.write(self, String.format(
                                "The intense heat emanating from %s %s only serves to enflame %s primal passion.",
                                self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun()));
                opponent.buildMojo(c, 7);
            } else if (target.moddedPartCountsAs(opponent, CockMod.bionic)) {
                c.write(self, String.format(
                                "The heat emanating from %s %s is extremely hazardous for %s %s, nearly burning through its circuitry and definitely causing intense pain.",
                                self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun(),
                                target.describe(opponent)));
                opponent.pain(c, self, Math.max(30, 20 + self.get(Attribute.Ki)));
            } else {
                c.write(self, String.format("Plunging %s %s into %s %s leaves %s gasping from the heat.",
                                opponent.possessiveAdjective(), target.describe(opponent), self.possessiveAdjective(),
                                part.describe(self), opponent.directObject()));
                opponent.pain(c, self, 20 + self.get(Attribute.Ki) / 2);
            }
        } else {
            c.write(self, Global.format(
                            "The intense heat emanating from {self:possessive} %s overpowers {other:possessive} senses now that {other:pronoun} cannot respond.", self, opponent, part.getType()));
            return 20;
        }
        return 0;
    }

    public void tickHolding(Combat c, Character self, Character opponent, BodyPart part, BodyPart otherOrgan) {
        Optional<BodyPart> targetPart = c.getStance().getPartsFor(c, opponent, self).stream().findAny();
        if (targetPart.isPresent()) {
            BodyPart target = targetPart.get();
            if (target.moddedPartCountsAs(opponent, CockMod.primal)) {
                c.write(self, String.format(
                                "The intense heat emanating from %s %s only serves to enflame %s primal passion.",
                                self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun()));
                opponent.buildMojo(c, 7);
            } else if (target.moddedPartCountsAs(opponent, CockMod.bionic)) {
                c.write(self, String.format(
                                "The heat emanating from %s %s is extremely hazardous for %s %s, nearly burning through its circuitry and definitely causing intense pain.",
                                self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun(),
                                target.describe(opponent)));
                opponent.pain(c, self, Math.max(30, 20 + self.get(Attribute.Ki)));
            } else {
                c.write(self, String.format("The heat from %s %s leaves %s gasping.",
                                self.possessiveAdjective(), part.describe(self), opponent.directObject()));
                opponent.pain(c, self, 20 + self.get(Attribute.Ki) / 2);
            }
        }
    }

    public int counterValue(BodyPart part, BodyPart otherPart, Character self, Character other) { 
        return otherPart.moddedPartCountsAs(other, CockMod.bionic) ? 1 : otherPart.moddedPartCountsAs(other, CockMod.primal) ? -1 : 0;
    }

    @Override
    public String describeAdjective(String partType) {
        return "molten depths";
    }
}
