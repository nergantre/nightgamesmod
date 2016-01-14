package nightgames.skills;

import java.util.Arrays;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;

public class CommandGive extends PlayerCommand {

    public static final List<Item> TRANSFERABLES =
                    Arrays.asList(Item.EnergyDrink, Item.SPotion, Item.Aphrodisiac, Item.Sedative, Item.Battery,
                                    Item.Beer, Item.Lubricant, Item.Rope, Item.ZipTie, Item.Tripwire, Item.Spring);
    private Item transfer;

    public CommandGive(Character self) {
        super("Take Item", self);
        transfer = null;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        if (!super.usable(c, target)) {
            return false;
        }
        for (Item transferable : TRANSFERABLES) {
            if (target.has(transferable)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String describe(Combat c) {
        return "Make your opponent give you an item.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        do {
            transfer = Item.values()[Global.random(Item.values().length)];
            if (!(target.has(transfer) && TRANSFERABLES.contains(transfer))) {
                transfer = null;
            }
        } while (transfer == null);
        target.consume(transfer, 1);
        getSelf().gain(transfer);
        c.write(getSelf(), deal(c, 0, Result.normal, target));
        transfer = null;
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new CommandGive(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int magnitude, Result modifier, Character target) {
        return target.name() + " takes out " + transfer.pre() + transfer.getName() + " and hands it to you.";
    }

    @Override
    public String receive(Combat c, int magnitude, Result modifier, Character target) {
        return "<<This should not be displayed, please inform The" + " Silver Bard: CommandGive-receive>>";
    }

}
