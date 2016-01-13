package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Aggressive;
import nightgames.status.Stsflag;

public class Spores extends Skill {

    public Spores(Character self) {
        super("Spores", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().get(Attribute.Bio) >= 13;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && !target.is(Stsflag.aggressive);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 20;
    }

    @Override
    public String describe(Combat c) {
        return "Release some spores to force your opponent into a frenzied attack.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.wary()) {
            c.write(getSelf(),
                            Global.format("{self:SUBJECT-ACTION:release|releases} a mass of tiny particles, but "
                                            + "{other:subject-action:avoid|avoids} breathing any of them in.",
                            getSelf(), target));
            return false;
        } else {
            c.write(getSelf(),
                            Global.format("{self:SUBJECT-ACTION:release|releases} a mass of tiny particles, and "
                                            + "{other:subject-action:are|is} forced to breathe them in. The scent"
                                            + " drives {other:pronoun} into a frenzy.", getSelf(), target));
            target.add(new Aggressive(target, getSelf().nameOrPossessivePronoun() + " spores", 5));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Spores(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

}
