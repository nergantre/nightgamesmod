package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Charmed;
import nightgames.status.Stsflag;

public class Beg extends Skill {

    public Beg(Character self) {
        super("Beg", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Submissive) >= 12;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && !c.getStance().dom(getSelf());
    }

    @Override
    public String describe(Combat c) {
        return "Beg your opponent to go easy on you";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (Global.random(30) <= getSelf().get(Attribute.Submissive) - target.get(Attribute.Cunning) / 2
                        && !target.is(Stsflag.cynical)) {
            target.add(c, new Charmed(target));
            if (getSelf().human()) {
                c.write(deal(c, 0, Result.normal, target));
            } else if (target.human()) {
                c.write(receive(c, 0, Result.normal, target));
            }
            return true;
        } else if (getSelf().human()) {
            c.write(deal(c, 0, Result.miss, target));
        } else if (target.human()) {
            c.write(receive(c, 0, Result.miss, target));
        }
        return false;
    }

    @Override
    public Skill copy(Character user) {
        return new Beg(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You throw away your pride and ask " + target.name() + " for mercy. This just seems to encourage "
                            + target.possessivePronoun() + " sadistic side.";
        }
        return "You put yourself completely at " + target.name() + "'s mercy. "
                        + Global.capitalizeFirstLetter(target.pronoun())
                        + " takes pity on you and gives you a moment to recover.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return getSelf().name() + " gives you a pleading look and asks you to go light on "
                            + getSelf().directObject() + ". " + Global.capitalizeFirstLetter(getSelf().pronoun())
                            + "'s cute, but " + getSelf().pronoun() + "'s not getting away that easily.";
        }
        return getSelf().name() + " begs you for mercy, looking ready to cry. Maybe you should give "
                        + getSelf().directObject() + " a break.";

    }

}
