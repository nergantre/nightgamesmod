package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.CockChoked;
import nightgames.status.Stsflag;

public class DenyOrgasm extends Skill {

    public DenyOrgasm(Character self) {
        super("Deny Orgasm", self, 4);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 20 && user.has(Trait.tight);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && !target.is(Stsflag.orgasmseal) && target.getArousal().percent() > 50
                        && c.getStance().penetratedBy(c, getSelf(), target) && !target.has(Trait.strapped);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    @Override
    public String describe(Combat c) {
        return "Prevents your opponents from cumming by tightening around their cock";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        target.add(c, new CockChoked(target, getSelf(), 4));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new DenyOrgasm(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You give " + target.subject() + " a quick smirk and tighten yourself around "
                        + target.possessivePronoun() + " cock, keeping " + target.possessivePronoun()
                        + " boiling cum from escaping";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().subject() + " gives " + target.subject() + " a quick smirk and tightens down on "
                        + target.possessivePronoun() + " cock, keeping " + target.possessivePronoun()
                        + " boiling cum from escaping";
    }
}
