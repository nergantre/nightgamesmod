package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Stsflag;
import nightgames.status.WaterStance;

public class WaterForm extends Skill {

    public WaterForm(Character self) {
        super("Water Form", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Ki) >= 3;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && !c.getStance().sub(getSelf()) && !getSelf().is(Stsflag.form);
    }

    @Override
    public String describe(Combat c) {
        return "Improves evasion and counterattack rate at expense of Power";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        getSelf().add(c, new WaterStance(getSelf()));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new WaterForm(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You relax your muscles, prepared to flow with and counter " + target.name() + "'s attacks.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s takes a deep breath and %s movements become much more fluid.",
                        getSelf().subject(), getSelf().possessivePronoun());
    }

}
