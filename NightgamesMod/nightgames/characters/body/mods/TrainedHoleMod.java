package nightgames.characters.body.mods;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class TrainedHoleMod extends HoleMod {
    public TrainedHoleMod() {
        super("trained", .2, .2, -.2, -100);
    }

    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { 
        if (target.isType("cock") && opponent.human()) {
                c.write(self,
                                Global.format("Fucking {self:possessive} trained %s feels positively exquisite. It's taking all your concentration not to instantly shoot your load into {self:possessive} velvety canal.",
                                self, opponent, part.getType()));
        }
        return 0;
    }
}