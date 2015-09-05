package nightgames.skills;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.Item;
import nightgames.items.ItemEffect;

public class ThrowDraft extends Skill {
	private static final Set<Item> transformativeItems = new HashSet<>();
	{
		transformativeItems.add(Item.SuccubusDraft);
		transformativeItems.add(Item.BustDraft);
		transformativeItems.add(Item.TinyDraft);
		transformativeItems.add(Item.TentacleTonic);
		transformativeItems.add(Item.PriapusDraft);
		transformativeItems.add(Item.FemDraft);
	}

	public ThrowDraft(Character self) {
		super("Throw draft", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		boolean hasItems = subChoices().size() > 0;
		return hasItems&&getSelf().canAct()&&c.getStance().mobile(getSelf())&&(c.getStance().reachTop(getSelf())||c.getStance().reachBottom(getSelf()))
				&&(!getSelf().human()||Global.getMatch().condition!=Modifier.noitems);
	}

	@Override
	public Collection<String> subChoices() {
		ArrayList<String> usables = new ArrayList<String>();
		for (Item i : getSelf().getInventory().keySet()) {
			if (i.getEffects().get(0).throwable()) {
				usables.add(i.getName());
			}
		}
		return usables;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		Item used = null;
		if (getSelf().human()) {
			for (Item i : getSelf().getInventory().keySet()) {
				if (i.getName().equals(choice)) {
					used = i;
					break;
				}
			}
		} else {
			ArrayList<Item> usables = new ArrayList<Item>();
			for (Item i : getSelf().getInventory().keySet()) {
				if (i.getEffects().get(0).throwable()) {
					usables.add(i);
				}
			}
			if (usables.size() > 0) {
				used = usables.get(Global.random(usables.size()));
			}
		}
		if (used == null) {
			c.write(getSelf(), "Skill failed...");
		} else {
			String verb = used.getEffects().get(0).getOtherVerb();
			if (verb.isEmpty()) {
				verb = "throw";
			}
			c.write(getSelf(), Global.format(String.format("{self:SUBJECT-ACTION:%s|%ss} %s%s",
					verb, verb, used.pre(), used.getName()), getSelf(), target));
			if (transformativeItems.contains(used) && target.has(Trait.stableform)) {
				c.write(target, "...But nothing happened (Stable Form).");
			} else {
				boolean eventful = false;
				for (ItemEffect e : used.getEffects()) {
					eventful = e.use(c, target, getSelf(), used) || eventful;
				}
				if (!eventful) {
					c.write(getSelf(), "...But nothing happened.");
				}
			}
			getSelf().consume(used, 1);
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new ThrowDraft(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return "";
	}

	@Override
	public String describe(Combat c) {
		return "Throw a draft at your opponent";
	}

	@Override
	public boolean makesContact() {
		return false;
	}
}
