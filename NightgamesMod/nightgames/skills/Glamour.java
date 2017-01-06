package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class Glamour extends Skill {

    public Glamour(Character self) {
        super("Glamour", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Arcane) >= 6;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf());
    }

    @Override
    public int getMojoCost(Combat c) {
        return 15;
    }

    @Override
    public String describe(Combat c) {
        return "Use illusions to make yourself appear more beautiful: 15 Mojo";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        getSelf().add(c, new nightgames.status.Glamour(getSelf(), 10));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Glamour(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You cast an illusion spell to create several images of yourself. At the same time, you add a charm to make yourself irresistible.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s casts a brief spell and %s vision is filled with "
                        + "naked copies of %s. %s can still tell which %s is real,"
                        + " but it's still a distraction. At the same "
                        + "time, %s suddenly looks irresistible.", getSelf().subject(),
                        target.nameOrPossessivePronoun(), getSelf().directObject(),
                        Global.capitalizeFirstLetter(target.pronoun()), getSelf().getName(),
                        getSelf().nameDirectObject());
    }
}
