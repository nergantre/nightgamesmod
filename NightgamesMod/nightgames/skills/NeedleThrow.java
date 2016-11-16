package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Drowsy;
import nightgames.status.Horny;

public class NeedleThrow extends Skill {

    public NeedleThrow(Character self) {
        super("Needle Throw", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Ninjutsu) >= 1;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance()
                .mobile(getSelf())
                        && !c.getStance()
                             .prone(getSelf())
                        && getSelf().canAct() && getSelf().has(Item.Needle);
    }

    @Override
    public String describe(Combat c) {
        return "Throw a drugged needle at your opponent.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().consume(Item.Needle, 1);
        if (getSelf().roll(getSelf(), c, accuracy(c))) {
            c.write(getSelf(), String.format(
                            "%s %s with one of %s drugged needles. "
                                            + "%s %s with arousal and %s it difficult to stay on %s feet.",
                            getSelf().subjectAction("hit"), target.subject(), getSelf().possessivePronoun(),
                            Global.capitalizeFirstLetter(target.pronoun()), target.action("flush", "flushes"),
                            target.action("find", target.pronoun() + " is finding"), target.possessivePronoun()));
            target.add(Horny.getWithBiologicalType(getSelf(), target, 3, 4, getSelf().nameOrPossessivePronoun() + " drugged needle"));
            target.add(new Drowsy(target));
        } else {
            c.write(getSelf(),
                            String.format("%s a small, drugged needle at %s, but %s %s it.",
                                            getSelf().subjectAction("throw"), target.subject(),
                                            target.pronoun(), target.action("dodge")));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new NeedleThrow(user);
    }

    public int accuracy() {
        return 8;
    }

    public int speed() {
        return 9;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

}
