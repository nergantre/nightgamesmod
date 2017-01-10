package nightgames.characters.body.mods;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BodyPartMod;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.ModdedCockPart;
import nightgames.combat.Combat;

public class FieryHoleMod extends HoleMod {
    public FieryHoleMod() {
        super("fiery", 0, .3, .2, -11);
    }

    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { 
        if (target.isType("cock")) {
            if (target.getMod(opponent).countsAs(opponent, CockMod.primal)) {
                c.write(self, String.format(
                                "The intense heat emanating from %s %s only serves to enflame %s primal passion.",
                                self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun()));
                opponent.buildMojo(c, 7);
            } else if (target.getMod(opponent).countsAs(opponent, CockMod.bionic)) {
                c.write(self, String.format(
                                "The heat emanating from %s %s is extremely hazardous for %s %s, nearly burning through its circuitry and definitely causing intense pain.",
                                self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun(),
                                target.describe(opponent)));
                opponent.pain(c, self, Math.max(30, 20 + self.get(Attribute.Ki)));
            } else {
                c.write(self, String.format("Plugging %s %s into %s %s leaves %s gasping from the heat.",
                                opponent.possessiveAdjective(), target.describe(opponent), self.possessiveAdjective(),
                                part.describe(self), opponent.directObject()));
                opponent.pain(c, self, 20 + self.get(Attribute.Ki) / 2);
            }
        }
        return 0;
    }

    public int counterValue(BodyPart part, BodyPart otherPart, Character self, Character other) { 
        if (otherPart instanceof ModdedCockPart) {
            BodyPartMod mod = otherPart.getMod(self);
            return mod.countsAs(other, CockMod.bionic) ? 1 : mod.countsAs(other, CockMod.primal) ? -1 : 0;
        }
        return 0;
    }
}
