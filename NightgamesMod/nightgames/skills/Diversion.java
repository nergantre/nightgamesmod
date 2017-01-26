package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Behind;
import nightgames.status.Flatfooted;

public class Diversion extends Skill {

    public Diversion(Character self) {
        super("Diversion", self);
        addTag(SkillTag.undressing);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.misdirection);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && getSelf().canAct() && c.getStance().mobile(getSelf()) && c.getStance().facing(getSelf(), target)
                        && !getSelf().torsoNude() && !c.getStance().prone(getSelf()) && !c.getStance().inserted();
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 25;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Clothing article = getSelf().strip(ClothingSlot.top, c);
        if (article == null) {
            getSelf().strip(ClothingSlot.bottom, c);
        }
        if (article != null) {
            if (getSelf().human()) {
                c.write(getSelf(), "You quickly strip off your " + article.getName()
                                + " and throw it to the right, while you jump to the left. " + target.getName()
                                + " catches your discarded clothing, " + "losing sight of you in the process.");
            } else {
                c.write(getSelf(), Global.format("{other:SUBJECT-ACTION:lose} sight of {self:name-do} for just a moment, "
                                + "but then {other:pronoun-action:see} moving behind "
                                + "{other:reflective} in {other:possessive} peripheral vision. {other:SUBJECT} quickly {other:action:spin} "
                                + "around and {other:action:grab} {self:direct-object}, but {other:pronoun-action:find} {other:reflective} "
                                + "holding just {self:possessive} %s. Wait... what the fuck?", getSelf(), target,
                                article.getName()));
            }
            c.setStance(new Behind(getSelf(), target), getSelf(), true);
            target.add(c, new Flatfooted(target, 1));
            return true;
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:try} to divert {other:name-possessive} attention by stripping off {self:possessive} clothing, "
                            + "only to find out {self:pronoun-action:have} nothing left. ", getSelf(), target));
            return false;
        }
    }

    @Override
    public Skill copy(Character user) {
        return new Diversion(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        Clothing article = getSelf().strip(modifier == Result.normal ? ClothingSlot.top : ClothingSlot.bottom, c);
        return "You quickly strip off your " + article.getName()
            + " and throw it to the right, while you jump to the left. " + target.getName()
            + " catches your discarded clothing, " + "losing sight of you in the process.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character attacker) {
        Clothing article = getSelf().strip(modifier == Result.normal ? ClothingSlot.top : ClothingSlot.bottom, c);
        return String.format("%s sight of %s for just a moment, but then %s %s moving behind "
                        + "%s in %s peripheral vision. %s quickly %s around and grab %s, "
                        + "but you find yourself holding just %s %s. Wait... what the fuck?",
                        attacker.subjectAction("lose"), getSelf().subject(), attacker.pronoun(),
                        attacker.action("see"),
                        getSelf().directObject(), attacker.directObject(),
                        Global.capitalizeFirstLetter(attacker.subject()), attacker.action("spin"),
                        getSelf().nameDirectObject(), getSelf().possessiveAdjective(),
                        article.getName());
    }

    @Override
    public String describe(Combat c) {
        return "Throws your clothes as a distraction";
    }

}
