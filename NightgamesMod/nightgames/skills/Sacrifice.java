package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class Sacrifice extends Skill {

    public Sacrifice(Character self) {
        super("Sacrifice", self, 5);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Dark) >= 15;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && !c.getStance().sub(getSelf()) && getSelf().getArousal().percent() >= 70;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 20;
    }

    @Override
    public String describe(Combat c) {
        return "Damage yourself to reduce arousal";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        getSelf().pain(c, getSelf(), getSelf().getStamina().max() / 3);
        getSelf().calm(c, getSelf().getArousal().max() / 3 + 20 + getSelf().get(Attribute.Dark));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Sacrifice(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.calming;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You feed your own lifeforce and pleasure to the darkness inside you. Your legs threaten to give out, but you've regained some self control.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s pinches %s nipples hard while screaming in pain. %s %s "
                        + "stagger in exhaustion, but %s seems much less aroused.",
                        getSelf().subject(), getSelf().nameOrPossessivePronoun(),
                        Global.capitalizeFirstLetter(target.subjectAction("see")), getSelf().directObject(),
                        getSelf().pronoun());
    }
}
