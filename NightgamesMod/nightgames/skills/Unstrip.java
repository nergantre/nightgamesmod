package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Primed;

public class Unstrip extends Skill {

    public Unstrip(Character self) {
        super("Unstrip", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.getPure(Attribute.Temporal) >= 8;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance()
                .mobile(getSelf())
                        && !c.getStance()
                             .prone(getSelf())
                        && getSelf().canAct() && Primed.isPrimed(getSelf(), 6);
    }

    @Override
    public String describe(Combat c) {
        return "Rewinds your clothing's time to when you were still wearing it: 6 charges";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().outfit.dress(getSelf().outfitPlan);
        getSelf().add(new Primed(getSelf(), -6));
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else if (target.human()) {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        getSelf().emote(Emotion.confident, 20);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Unstrip(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.recovery;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "It's tricky, but with some clever calculations, you restore the state of your outfit. Your outfit from the "
                                        + "start of the night reappears on your body.");
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "You lose sight of %s for just a moment and almost do a double-take when you see %s again, fully dressed. "
                                        + "In the second you looked away, how did %s find the time to put %s clothes on?!",
                        getSelf().name(), getSelf().directObject(), getSelf().pronoun(), getSelf().possessivePronoun());
    }

}
