package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Behind;
import nightgames.stance.Stance;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class Cowardice extends Skill {

    public Cowardice(Character self) {
        super("Cowardice", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Submissive) >= 3;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && target.canAct() && c.getStance().en == Stance.neutral;
    }

    @Override
    public String describe(Combat c) {
        return "Turning your back to an opponent will likely get you attacked from behind.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        c.setStance(new Behind(target, getSelf()));
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
            if (Global.getPlayer().checkAddiction(AddictionType.MIND_CONTROL, target)) {
                Global.getPlayer().unaddictCombat(AddictionType.MIND_CONTROL, 
                                target, Addiction.LOW_INCREASE, c);
                c.write(getSelf(), "Acting submissively voluntarily reduces Mara's control over you.");
            }
        } else {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Cowardice(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.negative;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You try to run away, but " + target.name() + " catches you and grabs you from behind.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().name() + " tries to sprint away, but you quickly grab " + getSelf().directObject()
                        + " from behind before " + getSelf().pronoun() + " can escape.";
    }

}
