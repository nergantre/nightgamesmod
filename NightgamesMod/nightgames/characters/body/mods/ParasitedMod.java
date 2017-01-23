package nightgames.characters.body.mods;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class ParasitedMod extends PartMod {
    public static final ParasitedMod INSTANCE = new ParasitedMod();

    public ParasitedMod() {
        super("parasited", 0, 0, 0, -1000);
    }

    public double applyReceiveBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) {
        c.write(self, Global.format("The parasite inhabiting {self:name-possessive} %s is making {self:direct-object} <b>extremely sensitive</b>.", self, opponent, part.getType()));
        return 10;
    }

    public void onOrgasm(Combat c, Character self, Character opponent, BodyPart part) {
        String partName = part.describe(self);
        c.write(self, Global.format(
                        "The force of {self:name-possessive} orgasm ejects the slimy lifeform from {self:possessive} %s.",
                        self, opponent, partName));
        self.body.removeTemporaryPartMod(part.getType(), this);
    }

    public Optional<String> getFluids() {
        return Optional.of("slime parasite");
    }

    @Override
    public String describeAdjective(String partType) {
        return "parasite";
    }
}