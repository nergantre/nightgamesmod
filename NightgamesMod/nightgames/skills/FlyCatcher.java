package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.skills.damage.DamageType;

public class FlyCatcher extends Skill {

    public FlyCatcher(Character self) {
        super("Fly Catcher", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Ki) >= 9;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !c.getPetsFor(target).isEmpty() && getSelf().canAct() && c.getStance().mobile(getSelf())
                        && !c.getStance().prone(getSelf());
    }

    @Override
    public int getMojoCost(Combat c) {
        return 15;
    }

    @Override
    public String describe(Combat c) {
        return "Focus on eliminating the enemy pet: 5 Stamina";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        double m = Global.random(50, 80);
        target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.physical, target, m));
        getSelf().weaken(c, 5);

        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new FlyCatcher(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.summoning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return receive(c, damage, modifier, target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format("{self:SUBJECT-ACTION:take|takes} the time to focus on chasing down {other:name-do}, "
                        + "finally catching {other:direct-object} in a submission hold.", getSelf(), target);
    }

}
