package nightgames.characters.body.mods;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Trance;

public class PlantHoleMod extends PartMod {
    public PlantHoleMod() {
        super("plant", .3, 1, .2, 10);
    }

    public double applyReceiveBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) {
        if (damage > self.getArousal().max()/ 5 && Global.random(4) == 0) {
            c.write(self, String.format("An intoxicating scent emanating from %s %s leaves %s in a trance!",
            self.possessiveAdjective(), part.describe(self), opponent.directObject()));
            opponent.add(c, new Trance(opponent));
        }
        return 0;
    }

    public void onStartPenetration(Combat c, Character self, Character opponent, BodyPart part, BodyPart target) {}

    public void tickHolding(Combat c, Character self, Character opponent, BodyPart part, BodyPart otherOrgan) {
        if (c.getStance().isPartFuckingPartInserted(c, opponent, otherOrgan, self, part)) {
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
