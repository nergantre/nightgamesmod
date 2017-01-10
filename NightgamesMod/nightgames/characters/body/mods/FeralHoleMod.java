package nightgames.characters.body.mods;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BodyPartMod;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.ModdedCockPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Frenzied;
import nightgames.status.IgnoreOrgasm;
import nightgames.status.Pheromones;
import nightgames.status.Stsflag;

public class FeralHoleMod extends HoleMod {
    public FeralHoleMod() {
        super("feral", .2, .3, .2, -8);
    }
    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { 
        if (target.isType("cock")) {
            int chance = Math.max(5, 10 - self.getArousal()
                            .getReal() / 50);
            if (!self.is(Stsflag.frenzied) && !self.is(Stsflag.cynical) && target.isType("cock")
                      && Global.random(chance) == 0) {
                c.write(self, String.format(
                              "A cloud of lust descends over %s and %s, clearing both of your thoughts of all matters except to fuck. Hard.",
                              opponent.subject(), self.subject()));
                self.add(c, new IgnoreOrgasm(opponent, 3));
                self.add(c, new Frenzied(self, 3));
                opponent.add(c, new Frenzied(opponent, 3));
            }
        }
        return 0;
    }

    public double applyReceiveBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) {
        if (c.getStance().distance() < 2) {
            c.write(self, String.format("Musk emanating from %s %s leaves %s reeling.", self.possessiveAdjective(),
                            part.describe(self), opponent.directObject()));
            double base = 3;
            if (target.getMod(opponent).countsAs(opponent, CockMod.runic)) {
                c.write(self, String.format(
                                "The wild scent overwhelms %s carefully layered enchantments, instantly sweeping %s away.",
                                opponent.nameOrPossessivePronoun(), opponent.directObject()));
                base *= 2.5;
            } else if (target.getMod(opponent).countsAs(opponent, CockMod.incubus)) {
                c.write(self, String.format("Whilst certainly invigorating, the scent leaves %s largely unaffected.",
                                opponent.subject()));
                base /= 2;
            }
            opponent.add(c, Pheromones.getWith(self, opponent, (float) base, 5, " feral musk"));
        }
        return 0;
    }

    public void onOrgasm(Combat c, Character self, Character opponent, BodyPart part) {
        if (c.getStance().distance() < 2) {
            c.write(self, Global.format(
                            "As {self:SUBJECT-ACTION:cum|cums} hard, a literal explosion of pheromones hits {other:name-do}. {other:POSSESSIVE} entire body flushes in arousal; {other:subject} better finish this fast!",
                            self, opponent));
            opponent.add(c, Pheromones.getWith(self, opponent, 10, 5, " orgasmic secretions"));
        }
    }

    public int counterValue(BodyPart part, BodyPart otherPart, Character self, Character other) { 
        if (otherPart instanceof ModdedCockPart) {
            BodyPartMod mod = otherPart.getMod(self);
            return mod.countsAs(other, CockMod.runic) ? 1 : mod.countsAs(other, CockMod.incubus) ? -1 : 0;
        }
        return 0;
    }
}
