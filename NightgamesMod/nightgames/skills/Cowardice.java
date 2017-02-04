package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Behind;
import nightgames.stance.Stance;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class Cowardice extends Skill {

    public Cowardice(Character self) {
        super("Cowardice", self);
        addTag(SkillTag.suicidal);
        addTag(SkillTag.positioning);
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
        c.setStance(new Behind(target, getSelf()), target, true);
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        if (getSelf().checkAddiction(AddictionType.MIND_CONTROL, target)) {
            getSelf().unaddictCombat(AddictionType.MIND_CONTROL, 
                            target, Addiction.LOW_INCREASE, c);
            c.write(getSelf(), "Acting submissively voluntarily reduces Mara's control over " + getSelf().nameDirectObject());
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Cowardice(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You try to run away, but " + target.getName() + " catches you and grabs you from behind.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s tries to sprint away, but %s quickly %s %s from behind before %s can escape", 
                            getSelf().subject(), target.subject(), target.action("grab"), 
                            getSelf().directObject(), getSelf().pronoun());
    }

}
