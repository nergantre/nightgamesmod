package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Primed;

public class Rewind extends Skill {

    public Rewind(Character self) {
        super("Rewind", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.getPure(Attribute.Temporal) >= 10;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance()
                .mobile(getSelf())
                        && !c.getStance()
                             .prone(getSelf())
                        && getSelf().canAct() && Primed.isPrimed(getSelf(), 8);
    }

    @Override
    public String describe(Combat c) {
        return "Rewind your personal time to undo all damage you've taken: 8 charges";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().add(c, new Primed(getSelf(), -8));
        getSelf().getArousal()
                 .empty();
        getSelf().getStamina()
                 .fill();
        getSelf().clearStatus();
        writeOutput(c, Result.normal, target);
        getSelf().emote(Emotion.confident, 25);
        getSelf().emote(Emotion.dominant, 20);
        target.emote(Emotion.nervous, 10);
        target.emote(Emotion.desperate, 10);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Rewind(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.recovery;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "It takes a lot of time energy, but you manage to rewind your physical condition back to the very start "
                                        + "of the match, removing all damage you've taken.");
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "%s hits a button on %s wristband and suddenly seems to completely recover. It's like nothing "
                                        + "%s done even happened.",
                        getSelf().name(), getSelf().possessiveAdjective(),
                        target.subjectAction("have", "has"));
    }

}
