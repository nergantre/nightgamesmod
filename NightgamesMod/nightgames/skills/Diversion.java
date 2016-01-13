package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
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
        if (modifier == Result.normal) {
            // TODO this is kind of terrible (deal does the effect...)
            Clothing article = getSelf().strip(ClothingSlot.top, c);
            return "You quickly strip off your " + article.getName()
                            + " and throw it to the right, while you jump to the left. " + target.getName()
                            + " catches your discarded clothing, " + "losing sight of you in the process.";
        } else {
            // TODO this is kind of terrible (deal does the effect...)
            Clothing article = getSelf().strip(ClothingSlot.bottom, c);
            return "You quickly strip off your " + article.getName()
                            + " and throw it to the right, while you jump to the left. " + target.getName()
                            + " catches your discarded clothing, " + "losing sight of you in the process.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character attacker) {
        if (modifier == Result.normal) {
            // TODO this is kind of terrible (deal does the effect...)
            Clothing article = getSelf().strip(ClothingSlot.top, c);
            return "You lose sight of " + getSelf().name()
                            + " for just a moment, but then see her moving behind you in your peripheral vision. You quickly spin around and grab her, "
                            + "but you find yourself holding just her " + article.getName()
                            + ". Wait... what the fuck?";
        } else {
            // TODO this is kind of terrible (deal does the effect...)
            Clothing article = getSelf().strip(ClothingSlot.bottom, c);
            return "You lose sight of " + getSelf().name()
                            + " for just a moment, but then see her moving behind you in your peripheral vision. You quickly spin around and grab her, "
                            + "but you find yourself holding just her " + article.getName()
                            + ". Wait... what the fuck?";
        }
    }

    @Override
    public String describe(Combat c) {
        return "Throws your clothes as a distraction";
    }

}
