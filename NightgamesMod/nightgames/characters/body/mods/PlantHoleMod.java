package nightgames.characters.body.mods;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.CockBound;

public class PlantHoleMod extends HoleMod {
    public PlantHoleMod() {
        super("plant", .3, 1, .2, 10);
    }

    public double applyReceiveBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) {
        if (damage > self.getArousal().max()/ 5 && Global.random(4) == 0) {
            c.write(self, String.format("An intoxicating scent emanating from %s %s leaves %s in a trance!",
            self.possessiveAdjective(), part.describe(self), opponent.directObject()));
        }
        return 0;
    }

    public void onStartPenetration(Combat c, Character self, Character opponent, BodyPart part, BodyPart target) {
        if (target.isType("cock")) {
            String partName = part.describe(self);
            c.write(self, Global.format("{self:NAME-POSSESSIVE} %s envelops"
                            + " {other:possessive} {other:body-part:cock} in a sticky grip, making extraction more"
                            + " difficult.", self, opponent, partName));
            opponent.add(c, new CockBound(opponent, 7, self.nameOrPossessivePronoun() + " " + partName));
        }
    }

    public void tickHolding(Combat c, Character self, Character opponent, BodyPart part, BodyPart otherOrgan) {
        if (otherOrgan.isType("cock")) {
            String partType = part.getType();
            c.write(self, Global.format(
                            "The small rough fibery filaments inside {self:name-possessive} flower %s wrap around {other:name-possessive} cock. "
                                            + "A profound exhaustion settles on {other:direct-object}, as {other:subject-action:feel|feels} {self:name-possessive} insidious flower leeching {other:possessive} strength.",
                            self, opponent, partType));
            opponent.drainStaminaAsMojo(c, self, 20, 1.25f);
            opponent.loseWillpower(c, 5);
        }
    }

}
