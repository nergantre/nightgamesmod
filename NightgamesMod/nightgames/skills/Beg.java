package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Charmed;
import nightgames.status.Stsflag;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

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
        return getSelf().canAct() && !c.getStance()
                                       .dom(getSelf());
    }

    @Override
    public String describe(Combat c) {
        return "Beg your opponent to go easy on you";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if ((Global.random(30) <= getSelf().get(Attribute.Submissive) - target.get(Attribute.Cunning) / 2
                        && !target.is(Stsflag.cynical) || target.getMood() == Emotion.dominant)
                        && target.getMood() != Emotion.angry && target.getMood() != Emotion.desperate) {
            target.add(c, new Charmed(target));
            if (getSelf().human()) {
                c.write(deal(c, 0, Result.normal, target));
                if (Global.getPlayer()
                          .checkAddiction(AddictionType.MIND_CONTROL, target)) {
                    Global.getPlayer()
                          .unaddictCombat(AddictionType.MIND_CONTROL, target, Addiction.LOW_INCREASE, c);
                    c.write(getSelf(), "Acting submissively voluntarily reduces Mara's control over you.");
                }
            } else if (c.shouldPrintReceive(target)) {
                c.write(receive(c, 0, Result.normal, target));
            }
            return true;
        }
        writeOutput(c, Result.miss, target);
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
            return String.format("%s gives %s a pleading look and asks %s to go light on %s."+
                            "%s is cute, but %s is not getting away that easily.", getSelf().name(), target.subject(),
                            target.directObject(), getSelf().directObject(), Global.capitalizeFirstLetter(getSelf().pronoun()),
                            getSelf().pronoun());
        }
        return getSelf().name() + " begs you for mercy, looking ready to cry. Maybe you should give "
                        + getSelf().directObject() + " a break.";

    }

}
