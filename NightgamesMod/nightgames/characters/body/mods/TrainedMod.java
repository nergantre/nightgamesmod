package nightgames.characters.body.mods;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class TrainedMod extends PartMod {
    public static final TrainedMod INSTANCE = new TrainedMod();

    public TrainedMod() {
        super("trained", .2, .2, -.2, -100);
    }

    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { 
        if (opponent.human()) {
            c.write(self,
                        Global.format("{self:POSSESSIVE} trained %s feels positively exquisite. It's taking all your concentration not to instantly shoot your load.",
                        self, opponent, part.getType()));
        }
        return 0;
    }

    @Override
    public String describeAdjective(String partType) {
        return "expertly-trained appearance";
    }
}