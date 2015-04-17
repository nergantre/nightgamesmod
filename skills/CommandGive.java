package skills;

import global.Global;

import items.Item;

import java.util.Arrays;
import java.util.List;

import characters.Character;
import combat.Combat;
import combat.Result;

public class CommandGive extends PlayerCommand {

	public static final List<Item> TRANSFERABLES = Arrays.asList(Item.EnergyDrink, Item.SPotion,
			Item.Aphrodisiac, Item.Sedative, Item.Battery, Item.Beer, Item.Lubricant, Item.Rope,
			Item.ZipTie, Item.Tripwire, Item.Spring);
	private Item transfer;
	
	public CommandGive(Character self) {
		super("Take Item", self);
		transfer = null;
	}
	
	public boolean usable(Combat c, Character target) {
		if (!super.usable(c, target))
			return false;
		for (Item transferable : TRANSFERABLES)
			if (target.has(transferable))
				return true;
		return false;
	}

	@Override
	public String describe() {
		return "Make your opponent give you an item.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		do {
			transfer = Item.values()[Global.random(Item.values().length)];
			if (!(target.has(transfer) && TRANSFERABLES.contains(transfer)))
				transfer = null;
		} while (transfer == null);
		target.consume(transfer, 1);
		self.gain(transfer);
		c.write(self,deal(c, 0, Result.normal, target));
		transfer = null;
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
		return target.name() + " takes out " + transfer.pre() + transfer.name()
				+ " and hands it to you.";
	}

	@Override
	public String receive(Combat c, int magnitude, Result modifier, Character target) {
		return "<<This should not be displayed, please inform The"
				+ " Silver Bard: CommandGive-receive>>";
	}

}
