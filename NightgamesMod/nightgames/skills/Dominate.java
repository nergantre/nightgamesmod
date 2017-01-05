package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.StandingOver;

public class Dominate extends Skill {

    public Dominate(Character self) {
        super("Dominate", self, 3);
        addTag(SkillTag.positioning);
        addTag(SkillTag.knockdown);
        addTag(SkillTag.dark);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Dark) >= 9;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && !c.getStance().sub(getSelf()) && !c.getStance().prone(getSelf())
                        && !c.getStance().prone(target) && getSelf().canAct() && !getSelf().has(Trait.submissive);
    }

    @Override
    public String describe(Combat c) {
        return "Overwhelm your opponent to force her to lie down: 10% Arousal";
    }

    @Override
    public int getMojoCost(Combat c) {
        return 15;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().arouse(getSelf().getArousal().max() / 10, c);
        writeOutput(c, Result.normal, target);
        c.setStance(new StandingOver(getSelf(), target), target, false);
        getSelf().emote(Emotion.dominant, 20);
        target.emote(Emotion.nervous, 20);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Dominate(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You take a deep breathe, gathering dark energy into your lungs. You expend the power to command "
                        + target.name() + " to submit. The demonic command renders her "
                        + "unable to resist and she drops to floor, spreading her legs open to you. As you approach, she comes to her senses and quickly closes her legs. Looks like her "
                        + "will is still intact.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s forcefully orders %s to \"Kneel!\" %s body complies without waiting for"
                        + " %s brain and %s %s to %s knees in front of %s. %s smiles and "
                        + "pushes %s onto %s back. By the time %s free of %s suggestion, %s %s"
                        + " flat on the floor with %s foot planted on %s chest.", getSelf().subject(),
                        target.subject(), Global.capitalizeFirstLetter(target.pronoun()),
                        target.possessiveAdjective(), target.pronoun(), target.action("drop"),
                        target.possessiveAdjective(), getSelf().directObject(),
                        getSelf().name(), target.nameDirectObject(), target.possessiveAdjective(),
                        target.subjectAction("break"), getSelf().possessiveAdjective(), target.pronoun(),
                        target.action("are", "is"), getSelf().nameOrPossessivePronoun(), target.possessiveAdjective());
    }

}
