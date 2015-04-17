package skills;

import global.Global;

import items.Item;

import java.util.Arrays;
import java.util.List;

import status.Hypersensitive;
import status.Oiled;
import status.Stsflag;
import characters.Character;
import combat.Combat;
import combat.Result;

public class CommandUse extends PlayerCommand {

	public static final List<Item> CANDIDATES = Arrays.asList(Item.Lubricant,
			Item.SPotion);
	private Item used;

	public CommandUse(Character self) {
		super("Force Item Use", self);
		used = null;
	}

	public boolean usable(Combat c, Character target) {
		if (!super.usable(c, target) || !target.nude())
			return false;
		boolean usable = false;
		for (Item candidate : CANDIDATES)
			if (target.has(candidate))
				switch (candidate) {
				case Lubricant:
					usable = !target.is(Stsflag.oiled);
					break;
				case SPotion:
					usable = !target.is(Stsflag.hypersensitive);
					break;
				default:
					break;
				}
		return usable;
	}

	@Override
	public String describe() {
		return "Force your thrall to use a harmful item on themselves";
	}

	@Override
	public void resolve(Combat c, Character target) {
		do {
			used = Item.values()[Global.random(Item.values().length)];
			boolean hasStatus = false;
			switch (used) {
			case Lubricant:
				hasStatus = target.is(Stsflag.oiled);
				break;
			case SPotion:
				hasStatus = target.is(Stsflag.hypersensitive);
				break;
			default:
				break;
			}
			if (!(CANDIDATES.contains(used) && target.has(used)) && !hasStatus)
				used = null;
		} while (used == null);
		switch (used) {
		case Lubricant:
			target.add(new Oiled(target));
			c.write(self,deal(c, 0, Result.normal, target));
			break;
		case SPotion:
			target.add(new Hypersensitive(target));
			c.write(self,deal(c, 0, Result.special, target));
			break;
		default:
			c.write("<<This should not be displayed, please inform The"
					+ " Silver Bard: CommandUse-resolve>>");
			return;
		}
		target.consume(used, 1);
		used = null;
	}

	@Override
	public Skill copy(Character user) {
		return new CommandUse(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int magnitude, Result modifier, Character target) {
		switch (modifier) {
		case normal:
			return target.name()
					+ " coats herself in a shiny lubricant at your 'request'.";
		case special:
			return "Obediently, " + target.name()
					+ " smears a sensitivity potion on herself.";
		default:
			return "<<This should not be displayed, please inform The"
					+ " Silver Bard: CommandUse-deal>>";
		}
	}

	@Override
	public String receive(Combat c, int magnitude, Result modifier, Character target) {
		return "<<This should not be displayed, please inform The"
				+ " Silver Bard: CommandUse-receive>>";
	}

}
