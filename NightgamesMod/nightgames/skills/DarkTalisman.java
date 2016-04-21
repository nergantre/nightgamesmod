package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Enthralled;
import nightgames.status.Stsflag;

public class DarkTalisman extends Skill {

    public DarkTalisman(Character self) {
        super("Dark Talisman", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getRank() >= 1;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && !target.is(Stsflag.enthralled) && getSelf().has(Item.Talisman);
    }

    @Override
    public String describe(Combat c) {
        return "Consume the mysterious talisman to control your opponent";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Result result = target.roll(this, c, accuracy(c)) ? Result.normal : Result.miss;
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, result, target));
        } else if (target.human()) {
            c.write(getSelf(), receive(c, 0, result, target));
        }
        getSelf().consume(Item.Talisman, 1);
        if (result == Result.normal) {
            target.add(c, new Enthralled(target, getSelf(), Global.random(3) + 1));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Skill copy(Character user) {
        return new DarkTalisman(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.normal) {
            return "You brandish the dark talisman, which seems to glow with power. The trinket crumbles to dust, but you see the image remain in the reflection of "
                            + target.name() + "'s eyes.";
        } else {
            return "You brandish the dark talisman, which seems to glow with power. The trinket crumbles to dust, with "
                            + target + " seemingly unimpressed.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.normal) {
            return getSelf().name()
                            + " holds up a strange talisman. You feel compelled to look at the thing, captivated by its unholy nature.";
        } else {
            return getSelf().name()
                            + " holds up a strange talisman. You feel a tiny tug on your consciousness, but it doesn't really affect you much.";
        }
    }

}
