package nightgames.characters.body.mods;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.GenericBodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.skills.damage.DamageType;
import nightgames.status.Drained;

public class DemonicMod extends PartMod {
    public static final DemonicMod INSTANCE = new DemonicMod();
    public DemonicMod() {
        super("demonic", .1, .5, .2, 5);
    }

    public String adjective(GenericBodyPart part) {
        if (part.getType().equals("pussy")) {
            return "succubus";
        }
        if (part.getType().equals("ass")) {
            return "devilish";
        }
        if (part.getType().equals("mouth")) {
            return "tainted";
        }
        return "demonic";
    }

    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) {
        if (opponent.has(Trait.succubus)) {
            c.write(self, Global.format(
                            "{self:NAME-POSSESSIVE} %s does nothing special against one of {self:possessive} own kind.", self, opponent, part.describe(self)));
            return 0;
        }
        boolean fucking = c.getStance().isPartFuckingPartInserted(c, opponent, target, self, part);
        if (target.moddedPartCountsAs(opponent, CockMod.runic)) {
            c.write(self, String.format(
                            "Putting in great effort, %s %s to draw upon %s power, but the fae enchantments in %s %s keep it locked away.",
                            self.nameOrPossessivePronoun(), self.human() ? "try" : " tries",
                            opponent.nameOrPossessivePronoun(), opponent.possessiveAdjective(),
                            target.describe(opponent)));
        } else {
            boolean bottomless = self.has(Trait.BottomlessPit);
            String domSubText = c.getStance().dom(self) ? ("{self:pronoun-action:" + (part.isType("mouth") ? "suck" : "ride") + "} {other:direct-object}") : "{other:pronoun-action:fuck} {self:direct-object}";
            String fuckingText = Global.format("{self:POSSESSIVE} hot flesh kneads {other:possessive} %s as " + domSubText + ", drawing ", self, opponent, target.describe(opponent));
            String normalText = Global.format("As {self:possessive} %s touches {other:poss-pronoun}, {self:pronoun-action:draw} large ", self, opponent, part.getType(), target.describe(opponent));
            c.write(self, (fucking ? fuckingText : normalText) + String.format("gouts of life energy out of %s %s which is %sabsorbed by %s %s%s.",
                            opponent.possessiveAdjective(), target.describe(opponent),
                            bottomless ? "greedily " : "",
                            self.possessiveAdjective(),
                            bottomless ? "seemingly bottomless " : "",
                            part.describe(self)));
            int strength;
            if (target.moddedPartCountsAs(opponent, CockMod.enlightened)) {
                c.write(self, String.format(
                                "Since %s had focused so much of %s in %s %s, there is much more for %s to take.",
                                opponent.subject(), opponent.reflectivePronoun(), opponent.possessiveAdjective(),
                                target.describe(opponent), self.subject()));
                strength = Global.random(20, 31);
            } else {
                strength = Global.random(10, 21);
            }
            if (bottomless) {
                strength = strength * 3 / 2;
            }
            strength = (int) self.modifyDamage(DamageType.drain, opponent, strength);
            opponent.drain(c, self, strength);
            if (self.isPet()) {
                Character master = ((PetCharacter) self).getSelf().owner();
                c.write(self, Global.format("The stolen strength seems to be shared with {self:possessive} {other:master} through {self:possessive} infernal connection.", self, master));
                master.heal(c, strength);
            }
            for (int i = 0; i < 10; i++) {
                Attribute canBeStolen[] = EnumSet.complementOf(EnumSet.of(Attribute.Speed, Attribute.Perception)).stream().filter(a -> opponent.get(a) > 0).toArray(size -> new Attribute[size]);
                Attribute stolen = Global.pickRandom(canBeStolen).orElse(null);
                if (stolen != null) {
                    int stolenStrength = Math.min(strength / 10, opponent.get(stolen));
                    Drained.drain(c, self, opponent, stolen, stolenStrength, 20, true);
                    if (self.isPet()) {
                        Character master = ((PetCharacter) self).getSelf().owner();
                        master.add(c, new Drained(master, opponent, stolen, stolenStrength, 20));
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

    @Override
    public String describeAdjective(String partType) {
        return "demonic nature";
    }
}
