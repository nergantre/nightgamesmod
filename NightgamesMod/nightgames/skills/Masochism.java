package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Masochistic;
import nightgames.status.Stsflag;

public class Masochism extends Skill {

    public Masochism(Character self) {
        super("Masochism", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Fetish) >= 1;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && getSelf().getArousal().get() >= 15
                        && !getSelf().is(Stsflag.masochism);
    }

    @Override
    public String describe(Combat c) {
        return "You and your opponent become aroused by pain: Arousal at least 15";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        getSelf().add(c, new Masochistic(getSelf()));
        target.add(c, new Masochistic(target));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Masochism(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You fantasize about the pleasure that exquisite pain can bring. You share this pleasure with "
                        + target.getName() + ".";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s shivers in arousal. %s suddenly bombarded with thoughts of "
                        + "letting %s hurt %s in wonderful ways.", getSelf().subject(),
                        target.subjectAction("are", "is"), getSelf().subject(), target.directObject());
    }

}
