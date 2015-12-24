package nightgames.skills;

import java.util.Arrays;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Hypersensitive;
import nightgames.status.Oiled;
import nightgames.status.Stsflag;

public class CommandUse extends PlayerCommand {

	public static final List<Item> CANDIDATES = Arrays.asList(Item.Lubricant,
			Item.SPotion);
	private Item used;

	public CommandUse(Character self) {
		super("Force Item Use", self);
		used = null;
	}

	public boolean usable(Combat c, Character target) {
		if (!super.usable(c, target) || !target.mostlyNude())
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
	public String describe(Combat c) {
		return "Force your thrall to use a harmful item on themselves";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
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
			target.add(c, new Oiled(target));
			c.write(getSelf(),deal(c, 0, Result.normal, target));
			break;
		case SPotion:
			target.add(c, new Hypersensitive(target));
			c.write(getSelf(),deal(c, 0, Result.special, target));
			break;
		default:
			c.write("<<This should not be displayed, please inform The"
					+ " Silver Bard: CommandUse-resolve>>");
			return false;
		}
		target.consume(used, 1);
		used = null;
		return true;
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
