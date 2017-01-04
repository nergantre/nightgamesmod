package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Slimed;
import nightgames.status.Stsflag;

public class ShakeOff extends Skill {

    public ShakeOff(Character self) {
        super("Shake Off", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().is(Stsflag.slimed) && getSelf().canAct() && c.getStance().mobile(getSelf());
    }

    @Override
    public String describe(Combat c) {
        return "Shake off some of that slime.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        getSelf().add(c, new Slimed(getSelf(), target, -10));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ShakeOff(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return Global.format("{self:SUBJECT-ACTION:take|takes} a moment to shake off the sticky slime all over {self:reflective}", getSelf(), target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return deal(c, damage, modifier, target);
    }
}
