package nightgames.characters.body.mods;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BodyPartMod;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.ModdedCockPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.status.Enthralled;

public class ArcaneHoleMod extends HoleMod {
    public ArcaneHoleMod() {
        super("arcane", .05, .1, 0, -5);
    }

    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { 
        if (target.isType("cock")) {
            String message;
            int strength;
            if (!target.getMod(opponent).countsAs(opponent, CockMod.bionic)) {
                if (target.getMod(opponent).countsAs(opponent, CockMod.primal)) {
                    message = String.format(
                                    "The tattoos around %s %s flare up with a new intensity, responding to the energy flow from %s %s."
                                                    + " The magic within them latches onto it and pulls fiercly, drawing %s strength into %s with great gulps.",
                                    self.nameOrPossessivePronoun(), part.describe(self), opponent.nameOrPossessivePronoun(),
                                    target.describe(opponent), opponent.possessiveAdjective(), self.directObject());
                    strength = 10 + self.get(Attribute.Arcane) / 4;
                } else {
                    message = self.nameOrPossessivePronoun() + " tattoos surrounding " + self.possessiveAdjective()
                                    + " " + part.getType() + " light up with arcane energy as " + opponent.subjectAction("are", "is")
                                    + " inside " + self.directObject() + ", channeling some of "
                                    + opponent.possessiveAdjective() + " energies back to its master.";
                    strength = 5 + self.get(Attribute.Arcane) / 6;
                }
                opponent.loseMojo(c, strength);
                self.buildMojo(c, strength);
                if (self.isPet()) {
                    Character master = ((PetCharacter) self).getSelf().owner();
                    c.write(self, Global.format("The energy seems to flow through {self:direct-object} and into {self:possessive} {other:master}.", self, master));
                    master.buildMojo(c, strength);
                }
            } else {
                message = String.format(
                                "%s tattoos shine with an eldritch light, but they do not seem to be able to affect %s only partially-organic %s",
                                self.nameOrPossessivePronoun(), opponent.nameOrPossessivePronoun(),
                                target.describe(opponent));
            }
            if (Global.random(8) == 0 && !opponent.wary() && !target.getMod(opponent).countsAs(opponent, CockMod.bionic)) {
                message += " The light seems to seep into " + opponent.possessiveAdjective() + " "
                                + target.describe(opponent) + ", leaving " + opponent.directObject() + " enthralled to "
                                + self.possessiveAdjective() + " will.";
                opponent.add(c, new Enthralled(opponent, self, 3));
            }
            c.write(self, message);
        }
        return 0;
    }

    public int counterValue(BodyPart part, BodyPart otherPart, Character self, Character other) { 
        if (otherPart instanceof ModdedCockPart) {
            BodyPartMod mod = otherPart.getMod(self);
            return mod.countsAs(other, CockMod.primal) ? 1 : mod.countsAs(other, CockMod.bionic) ? -1 : 0;
        }
        return 0;
    }
}
