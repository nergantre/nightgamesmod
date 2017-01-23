package nightgames.characters.body.mods;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.CockBound;
import nightgames.status.Stsflag;

public class ExtendedTonguedMod extends PartMod {
    public static final PartMod INSTANCE = new ExtendedTonguedMod();

    public ExtendedTonguedMod() {
        super("extendedtongue", .3, 1.2, 0, 4);
    }

    @Override
    public String adjective(BodyPart part) {
        return "";
    }

    public String getLongDescriptionOverride(Character self, BodyPart part, String previousDescription) {
        if (part.isType("mouth")) {
            return previousDescription + Global.format(" Occasionally, a pink tongue slides out of %s and licks {self:possessive} second lips.", self, self, part.getType());
        } else {
            return previousDescription + Global.format(" When {self:pronoun-action:open} {self:pronoun} mouth, you see an unnaturally long tongue.", self, self, part.getType());
        }
    }

    public double applyBonuses(Combat c, Character self, Character opponent, BodyPart part, BodyPart target, double damage) { 
        if (c.getStance().isPartFuckingPartInserted(c, opponent, target, self, part) && part.isType("pussy")) {
            if (target.isType("cock") && !opponent.hasStatus(Stsflag.cockbound)) {
                opponent.add(c, new CockBound(opponent, 5, self.nameOrPossessivePronoun() + " " + part.adjective() + "-tongue"));
                c.write(self, self.nameOrPossessivePronoun() + " long sinuous " + part.adjective() + " tongue wraps around "
                                + opponent.nameOrPossessivePronoun() + " " + target.describe(opponent)
                                + ", preventing any escape.\n");
            }
        }
        return 0;
    }

    @Override
    public String describeAdjective(String partType) {
        if (partType.equals("mouth")) {
            return "extra length on its tongue";
        }
        return "tongue";
    }
}