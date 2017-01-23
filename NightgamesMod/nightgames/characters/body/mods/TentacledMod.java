package nightgames.characters.body.mods;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.CockBound;
import nightgames.status.Stsflag;

public class TentacledMod extends PartMod {
    public static final TentacledMod INSTANCE = new TentacledMod();

    public TentacledMod() {
        super("tentacled", 0, 1, .2, 4);
    }

    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { 
        if (c.getStance().isPartFuckingPartInserted(c, opponent, target, self, part)) {
            String partType = part.getType();
            if (!opponent.is(Stsflag.cockbound)) {
                if (!self.human()) {
                    c.write(self, Global.format(
                                    "Deep inside {self:name-possessive} %s, soft walls pulse and strain against your cock. "
                                                    + "You suddenly feel hundreds of thin tentacles, probing like fingers, dancing over every inch of your pole. "
                                                    + "A thicker tentacle wraps around your cock, preventing any escape",
                                    self, opponent, partType));
                } else {
                    c.write(self, Global.format(
                                    "As {other:name-possessive} cock pumps into you, you focus your mind on your %s entrance. "
                                                    + "You mentally command the tentacles inside your tunnel to constrict and massage {other:possessive} cock. "
                                                    + "{other:name} almost starts hyperventilating from the sensations.",
                                    self, opponent, lowerOrRear(part)));
                }
                opponent.add(c, new CockBound(opponent, 10, self.nameOrPossessivePronoun() + " " + part.adjective() + " tentacles"));
            } else {
                if (!self.human()) {
                    c.write(self, Global.format(
                                    "As you thrust into {self:name-possessive} %s, hundreds of tentacles squirm against the motions of your cock, "
                                                    + "making each motion feel like it will push you over the edge.",
                                    self, opponent, partType));
                } else {
                    c.write(self, Global.format(
                                    "As {other:name-possessive} cock pumps into you, your %s tentacles reflexively curl around the intruding object, rhythmically"
                                                    + "squeezing and milking it constantly.",
                                    self, opponent, part.adjective()));
                }
                return 5 + Global.random(4);
            }
        }
        return 0;
    }

    @Override
    public String describeAdjective(String partType) {
        return "inner-tentacles";
    }
}
