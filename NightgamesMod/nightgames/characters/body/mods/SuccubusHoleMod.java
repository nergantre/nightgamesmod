package nightgames.characters.body.mods;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.skills.damage.DamageType;
import nightgames.status.Abuff;

public class SuccubusHoleMod extends PartMod {
    public SuccubusHoleMod() {
        super("succubus", .1, .5, .2, 5);
    }

    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) {
        boolean fucking = c.getStance().isPartFuckingPartInserted(c, opponent, target, self, part);
        if (target.moddedPartCountsAs(opponent, CockMod.runic)) {
            c.write(self, String.format(
                            "Putting in great effort, %s %s to draw upon %s power, but the fae enchantments in %s %s keep it locked away.",
                            self.nameOrPossessivePronoun(), self.human() ? "try" : " tries",
                            opponent.nameOrPossessivePronoun(), opponent.possessiveAdjective(),
                            target.describe(opponent)));
        } else {
            String fuckingText = Global.format("{self:POSSESSIVE} hot flesh kneads {other:possessive} %s as {self:pronoun-action:" + (part.isType("mouth") ? "suck" : "ride") + "} {other:direct-object}, drawing ", self, opponent, target.describe(opponent));
            String normalText = Global.format("As {self:possessive} %s touch {other:direct-object}, {self:pronoun-action:draw} large ", self, opponent, part.getType(), target.describe(opponent));
            c.write(self, (fucking ? fuckingText : normalText) + String.format("gouts of life energy out of %s %s, which is greedily absorbed by %s %s.",
                            opponent.possessiveAdjective(), target.describe(opponent), self.possessiveAdjective(),
                            part.describe(self)));
            int strength;
            if (target.moddedPartCountsAs(opponent, CockMod.enlightened)) {
                c.write(self, String.format(
                                "Since %s had focused so much of %s in %s %s, there is much more for %s to take.",
                                opponent.subject(), opponent.reflectivePronoun(), opponent.possessiveAdjective(),
                                target.describe(opponent), self.subject()));
                strength = 10 + self.get(Attribute.Dark);
            } else {
                strength = 10 + self.get(Attribute.Dark) / 2;
            }
            strength = (int) self.modifyDamage(DamageType.drain, opponent, strength);
            opponent.drain(c, self, strength);
            if (self.isPet()) {
                Character master = ((PetCharacter) self).getSelf().owner();
                c.write(self, Global.format("The stolen strength seems to be shared with {self:possessive} {other:master} through {self:possessive} infernal connection.", self, master));
                master.heal(c, strength);
            }
            for (int i = 0; i < 10; i++) {
                Attribute stolen = (Attribute) opponent.att.keySet()
                                                           .toArray()[Global.random(opponent.att.keySet()
                                                                                                .size())];
                if (stolen != Attribute.Perception && opponent.get(stolen) > 0) {
                    int stolenStrength = Math.min(strength / 10, opponent.get(stolen));
                    opponent.add(c, new Abuff(opponent, stolen, -stolenStrength, 20));
                    self.add(c, new Abuff(self, stolen, stolenStrength, 20));
                    if (self.isPet()) {
                        Character master = ((PetCharacter) self).getSelf().owner();
                        master.add(c, new Abuff(master, stolen, stolenStrength, 20));
                    }
                    break;
                }
            }
        }
        return 0;
    }

    public int counterValue(BodyPart part, BodyPart otherPart, Character self, Character other) { 
        return otherPart.moddedPartCountsAs(other, CockMod.runic) ? -1 : otherPart.moddedPartCountsAs(other, CockMod.enlightened) ? 1 : 0;
    }
}
