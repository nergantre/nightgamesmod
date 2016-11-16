package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;

public class PerfectTouch extends Skill {

    public PerfectTouch(Character self) {
        super("Sleight of Hand", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf()) && !target.torsoNude() && !c.getStance().prone(getSelf())
                        && getSelf().canAct() && !c.getStance().connected(c);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 25;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
                c.write(target, target.nakedLiner(c, target));
            } else if (c.shouldPrintReceive(target, c)) {
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            }
            target.undress(c);
            target.emote(Emotion.nervous, 10);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 18;
    }

    @Override
    public Skill copy(Character user) {
        return new PerfectTouch(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public int accuracy(Combat c) {
        return Math.round(Math.max(Math.min(150,
                        2.5f * (getSelf().get(Attribute.Cunning) - c.getOpponent(getSelf()).get(Attribute.Cunning)) + 65),
                        40));
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to steal " + target.name() + "'s clothes off of her, but she catches you.";
        } else {
            return "You feint to the left while your right hand makes quick work of " + target.name()
                            + "'s clothes. By the time she realizes what's happening, you've "
                            + "already stripped all her clothes off.";
        }

    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s lunges toward %s, but %s %s %s hands"
                            + " before %s can get ahold of %s clothes.",
                            getSelf().subject(), target.nameDirectObject(),
                            target.pronoun(), target.action("catch"),
                            target.possessivePronoun(), getSelf().pronoun(),
                            target.possessivePronoun());
        } else {
            return String.format("%s lunges towards %s, but dodges away without hitting %s. "
                            + "%s tosses aside a handful of clothes, "
                            + "at which point %s %s %s "
                            + "naked. How the hell did %s manage that?",
                            getSelf().subject(), target.nameDirectObject(), target.directObject(),
                            getSelf().subject(), target.subjectAction("realize"), target.pronoun(),
                            target.action("are", "is"), getSelf().pronoun());
        }

    }

    @Override
    public String describe(Combat c) {
        return "Strips opponent completely: 25 Mojo";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
