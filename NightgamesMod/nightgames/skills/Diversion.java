package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.stance.Behind;
import nightgames.status.Flatfooted;

public class Diversion extends Skill {

    public Diversion(Character self) {
        super("Diversion", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.misdirection);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && getSelf().canAct() && c.getStance().mobile(getSelf()) && c.getStance().facing()
                        && !getSelf().torsoNude() && !c.getStance().prone(getSelf()) && !c.getStance().inserted();
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 25;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().breastsAvailable()) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.special, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.special, target));
            }
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            }
        }
        c.setStance(new Behind(getSelf(), target));
        target.add(c, new Flatfooted(target, 1));
        return true;
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
                        getSelf().nameDirectObject(), getSelf().possessivePronoun(),
                        article.getName());
    }

    @Override
    public String describe(Combat c) {
        return "Throws your clothes as a distraction";
    }

}
