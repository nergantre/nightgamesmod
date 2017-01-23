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
                        && !c.getStance().behind(target) && !c.getStance().sub(getSelf()) && !target.is(Stsflag.charmed);
    }
    
    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    @Override
    public String describe(Combat c) {
        return "Hypnotize your opponent so she can't defend herself";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
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
                            target.getName(), target.pronoun());
        }
        return String.format(
                        "You speak in a calm, rhythmic tone, lulling %s into a hypnotic trance. Her eyes seem to glaze over slightly, momentarily slipping under your influence.",
                        target.getName());
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s attempts to put %s under hypnotic suggestion, but"
                            + " %s %s to regain control of %s consciousness.",
                            getSelf().subject(), target.nameDirectObject(),
                            target.pronoun(), target.action("manage"), target.possessiveAdjective());
        }
        return String.format("%s speaks in a firm, but relaxing tone, attempting to put %s"
                        + " into a trance. Obviously %s wouldn't let %s be "
                        + "hynotized in the middle of a match, right? ...Right? ..."
                        + "Why %s %s fighting %s again?", getSelf().subject(),
                        target.nameDirectObject(), target.subject(),
                        target.reflectivePronoun(), target.action("was", "were"),
                        target.pronoun(), getSelf().subject());
    }

}
