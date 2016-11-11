package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Stsflag;

public class EnergyDrink extends Skill {

    public EnergyDrink(Character self) {
        super("Energy Drink", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf()) && getSelf().canAct() && getSelf().has(Item.EnergyDrink);
    }

    @Override
    public String describe(Combat c) {
        return "Terrible stuff, but will make you less tired";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else if (target.human()) {
            if (!target.is(Stsflag.blinded))
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            else 
                printBlinded(c);
        } else if (c.isBeingObserved()) {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        getSelf().heal(c, Math.max(20, getSelf().getStamina().max() / 2));
        getSelf().buildMojo(c, 20 + Global.random(10));

        getSelf().consume(Item.EnergyDrink, 1);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new EnergyDrink(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.recovery;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You chug an energy drink and feel some of your fatigue vanish.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return target.name() + " opens up an energy drink and downs the whole can.";
    }

}
