package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Stsflag;

public class HypnoVisorRemove extends Skill {

    public HypnoVisorRemove(Character self) {
        super("Remove Hypno Visor", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public int accuracy(Combat c, Character target) {
        return (int) ((Math.min(0.8, (double) .2 + getSelf().get(Attribute.Cunning) / 100.0)) * 100);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && getSelf().is(Stsflag.hypnovisor) && c.getStance().mobile(getSelf());
    }

    @Override
    public String describe(Combat c) {
        return "Try to remove the Hypno Visor from your head.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c, target))) {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:find|finds} a small button"
                            + " on the side of the Hypno Visor, and pressing it unlocks whatever"
                            + " mechanisms held it in place. {self:PRONOUN-ACTION:make|makes} sure"
                            + " to throw it far away before refocussing on the fight.", getSelf(), target));
            getSelf().removeStatus(Stsflag.hypnovisor);
            return true;
        }
        c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:claw|claws} at the insidious visor"
                        + " around {self:possessive} head, but to no avail.", getSelf(), target));
        return false;
    }

    @Override
    public Skill copy(Character user) {
        return new HypnoVisorRemove(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.recovery;
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
