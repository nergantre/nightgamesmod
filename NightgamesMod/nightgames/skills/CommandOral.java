package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class CommandOral extends PlayerCommand {

    public CommandOral(Character self) {
        super("Force Oral", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && getSelf().crotchAvailable() && c.getStance().oral(target, target);
    }

    @Override
    public String describe(Combat c) {
        return "Force your opponent to go down on you.";
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 30;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean silvertongue = target.has(Trait.silvertongue);
        boolean lowStart = getSelf().getArousal().get() < 15;
        int m = (silvertongue ? 8 : 5) + Global.random(10);
        if (getSelf().human()) {
            if (lowStart) {
                if (m < 8) {
                    c.write(getSelf(), deal(c, 0, Result.weak, target));
                } else {
                    c.write(getSelf(), deal(c, 0, Result.strong, target));
                }
            } else {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            }
        }
        getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandom("cock"), m, c, this);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new CommandOral(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
    }

    @Override
    public String deal(Combat c, int magnitude, Result modifier, Character target) {
        switch (modifier) {
            case normal:
                return target.getName() + " is ecstatic at being given the privilege of"
                                + " pleasuring you and does a fairly good job at it, too. She"
                                + " sucks your hard dick powerfully while massaging your balls.";
            case strong:
                return target.getName() + " seems delighted to 'help' you, and makes short work"
                                + " of taking your flaccid length into her mouth and getting it " + "nice and hard.";
            case weak:
                return target.getName() + " tries her very best to get you ready by running"
                                + " her tongue all over your groin, but even"
                                + " her psychically induced enthusiasm can't get you hard.";
            default:
                return "<<This should not be displayed, please inform The" + " Silver Bard: CommandOral-deal>>";
        }
    }

    @Override
    public String receive(Combat c, int magnitude, Result modifier, Character target) {
        return "<<This should not be displayed, please inform The" + " Silver Bard: CommandOral-receive>>";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
