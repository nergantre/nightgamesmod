package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;

public class MutualUndress extends Skill {

    public MutualUndress(Character self) {
        super("Tempt Undress", self);
        addTag(SkillTag.undressing);
        addTag(SkillTag.stripping);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) > 50;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        if (getSelf().stripDifficulty(target) == 0 && getSelf().canAct() && c.getStance().mobile(getSelf())
                        && !getSelf().mostlyNude() && !target.mostlyNude()) {
            return true;
        }
        return false;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 30;
    }

    @Override
    public float priorityMod(Combat c) {
        return c.getStance().dom(getSelf()) ? 2.0f : 0.0f;
    }

    @Override
    public String describe(Combat c) {
        return "Tempt opponent to remove clothes by removing your own";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.strong, target);
        getSelf().undress(c);
        target.undress(c);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new MutualUndress(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "During a brief respite in the fight as " + target.name()
                        + " is catching her breath, you ask if we can finish the fight naked. "
                        + "Without waiting for an answer, you slowly strip off all your clothing."
                        + "By the time you finish, you find that she has also stripped naked while panting with arousal.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s asks for a quick time out and starts sexily slipping %s clothes off. Although there"
                        + " are no time outs in the rules, %s can't help staring "
                        + "at the seductive display until %s finishes with a cute wiggle of %s naked ass. "
                        + "%s asks %s if %s %s to join %s in feeling good, and before %s it "
                        + "%s has got %s naked as well.", getSelf().subject(), getSelf().possessiveAdjective(),
                        target.subject(), getSelf().subject(), getSelf().possessiveAdjective(),
                        Global.capitalizeFirstLetter(getSelf().pronoun()), target.directObject(),
                        target.pronoun(), target.action("want"), getSelf().directObject(),
                        target.subjectAction("realize"), getSelf().subject(),
                        target.directObject());
    }
}
