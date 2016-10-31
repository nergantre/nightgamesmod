package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Mount;
import nightgames.stance.Stance;

public class Blindside extends Skill {

    public Blindside(Character self) {
        super("Blindside", self, 2);
        addTag(SkillTag.positioning);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 15;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.temptress) && user.get(Attribute.Technique) >= 10;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && c.getStance()
                                  .enumerate() == Stance.neutral;
    }

    @Override
    public String describe(Combat c) {
        return "Distract your opponent and take them down.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        c.setStance(new Mount(getSelf(), target));
        getSelf().emote(Emotion.confident, 15);
        getSelf().emote(Emotion.dominant, 15);
        target.emote(Emotion.nervous, 10);
        return false;
    }

    @Override
    public Skill copy(Character user) {
        return new Blindside(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "You move up to %s and kiss %s strongly. "
                                        + "While %s is distracted, you throw %s down and plant "
                                        + "yourself on top of %s.",
                        target.name(), target.directObject(), target.pronoun(), target.directObject(),
                        target.directObject());
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "Seductively swaying %s hips, %s shashays over to %s. "
                                        + "%s eyes fix %s in place as %s leans in and firmly kisses %s, shoving %s tongue down"
                                        + " %s mouth. %s are so absorbed in kissing back, that %s only notice %s ulterior motive"
                                        + " once %s has already swept %s legs out from under %s and %s has landed on top of %s.",
                        getSelf().possessivePronoun(), getSelf().name(), target.subject(),
                        Global.capitalizeFirstLetter(getSelf().possessivePronoun()), target.directObject(),
                        getSelf().pronoun(), target.directObject(), getSelf().possessivePronoun(),
                        target.possessivePronoun(), Global.capitalizeFirstLetter(target.pronoun()), target.pronoun(),
                        getSelf().possessivePronoun(), getSelf().pronoun(), target.possessivePronoun(),
                        target.directObject(), getSelf().pronoun(), target.directObject());
    }

}
