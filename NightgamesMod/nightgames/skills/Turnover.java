package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Behind;
import nightgames.stance.Stance;

public class Turnover extends Skill {

    public Turnover(Character self) {
        super("Turn Over", self);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 4;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().enumerate() == Stance.standingover && c.getStance().dom(getSelf());
    }

    @Override
    public String describe(Combat c) {
        return "Turn your opponent over and get behind her";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        c.setStance(new Behind(getSelf(), target), getSelf(), true);
        target.emote(Emotion.dominant, 20);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Turnover(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You turn " + target.getName() + " onto her hands and knees. You move behind her while she slowly gets up.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s rolls %s onto %s stomach. %s %s back "
                        + "up, but %s takes the opportunity to get behind %s.", getSelf().subject(),
                        target.nameDirectObject(), target.possessiveAdjective(),
                        Global.capitalizeFirstLetter(target.subjectAction("push", "pushes")),
                        target.reflectivePronoun(), getSelf().subject(), target.directObject());
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
