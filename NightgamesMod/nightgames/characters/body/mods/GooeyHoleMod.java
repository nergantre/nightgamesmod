package nightgames.characters.body.mods;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.CockBound;

public class GooeyHoleMod extends HoleMod {
    public GooeyHoleMod() {
        super("gooey", .2, .5, .2, 2);
    }
    public void onOrgasmWith(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, boolean selfCame) {
        if (target.isType("cock")) {
            String partName = part.describe(self);
            c.write(self, Global.format(
                            "{self:NAME-POSSESSIVE} %s clenches down hard"
                                            + " on {other:name-possessive} {other:body-part:cock}. The suction is so strong that the cum"
                                            + " leaves the shaft in a constant flow rather than spurts. When {other:possessive} orgasm is"
                                            + " over, {other:subject-action:are} much more drained of cum than usual.",
                            self, opponent, partName));
            opponent.loseWillpower(c, 10 + Global.random(Math.min(20, self.get(Attribute.Bio))));
        }
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
        String partName = part.describe(self);
        c.write(self, Global.format(
                        "The slimy filaments inside {self:possessive} %s constantly massage"
                                        + " {other:possessive} %s, filling every inch of it with pleasure.",
                        self, opponent, partName, otherOrgan.describe(opponent)));
        opponent.body.pleasure(self, part, otherOrgan, 1 + Global.random(7), c);
    }

    public Optional<String> getFluids() {
        return Optional.of("slime");
    }
}
