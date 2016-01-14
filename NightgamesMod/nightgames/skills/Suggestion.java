package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Charmed;
import nightgames.status.Stsflag;

public class Suggestion extends Skill {

    public Suggestion(Character self) {
        super("Suggestion", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Hypnosis) >= 1;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().behind(getSelf())
                        && !c.getStance().behind(target) && !c.getStance().sub(getSelf()) && !target.is(Stsflag.charmed)
                        && getSelf().canSpend(5);
    }

    @Override
    public String describe(Combat c) {
        return "Hypnotize your opponent so she can't defend herself";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().spendMojo(c, 5);
        if (!target.is(Stsflag.cynical)) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            }
            target.add(c, new Charmed(target));
            return true;
        } else if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.miss, target));
        } else {
            c.write(getSelf(), receive(c, 0, Result.miss, target));
        }
        return false;
    }

    @Override
    public Skill copy(Character user) {
        return new Suggestion(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format(
                            "You attempt to put %s under hypnotic suggestion, but %s doesn't appear to be affected.",
                            new Object[] {target.name(), target.pronoun()});
        }
        return String.format(
                        "You speak in a calm, rhythmic tone, lulling %s into a hypnotic trance. Her eyes seem to glaze over slightly, momentarily slipping under your influence.",
                        new Object[] {target.name()});
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return getSelf().name()
                            + " attempts to put you under hypnotic suggestion, but you manage to regain control of your consciousness.";
        }
        return getSelf().name()
                        + " speaks in a firm, but relaxing tone, attempting to put you into a trance. Obviously you wouldn't let yourself be "
                        + "hynotized in the middle of a match, right? ...Right? ...Why were you fighting her again?";
    }

}
