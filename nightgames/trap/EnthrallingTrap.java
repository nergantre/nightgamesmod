package nightgames.trap;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Encounter;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Enthralled;
import nightgames.status.Flatfooted;

public class EnthrallingTrap implements Trap {

	private Character owner;

	@Override
	public void trigger(Character target) {
		if (target.human()) {
			if (target.check(Attribute.Perception, 25-target.get(Attribute.Perception))) {
				Global.gui()
						.message(
								"As you step across the "
										+ target.location().name
										+ ", you notice a pentagram drawn on the floor,"
										+ " appearing to have been drawn in cum. Wisely,"
										+ " you avoid stepping into it.");
			} else {
				target.add(new Enthralled(target,owner, 5));
				target.location().opportunity(target,this);
				Global.gui()
						.message(
								"As you step across the "
										+ target.location().name
										+ ", you are suddenly surrounded by purple flames. Your mind "
										+ "goes blank for a moment, leaving you staring into the distance."
										+ " When you come back to your senses, you shake your head a few"
										+ " times and hope whatever that thing was, it failed at"
										+ " whatever it was supposed to do. The lingering vision of two"
										+ " large red irises staring at you suggest differently, though.");
			}

		} else if (!target.check(Attribute.Perception, 10)
				&& !target.check(Attribute.Cunning, 10)) {
			if (target.location().humanPresent())
				Global.gui()
						.message(
								"You catch a bout of purple fire in your peripheral vision,"
										+ "but once you have turned to look the flames are gone. All that is left"
										+ " to see is "
										+ target.name()
										+ ", standing still and staring blankly ahead."
										+ " It would seem to be very easy to have your way with her now, but"
										+ " who or whatever left that thing there will probably be thinking"
										+ " the same.");
			target.add(new Enthralled(target,owner, 5));
			target.location().opportunity(target,this);
		}
	}

	@Override
	public boolean decoy() {
		return false;
	}

	@Override
	public boolean recipe(Character owner) {
		return owner.has(Item.semen);
	}

	@Override
	public boolean requirements(Character owner) {
		return owner.has(Trait.succubus);
	}

	@Override
	public String setup(Character owner) {
		this.owner = owner;
		owner.consume(Item.semen, 1);
		return "You pop open a bottle of cum and use its contents to draw"
				+ " a pentagram on the floor, all the while speaking"
				+ " incantations to cause the first person to step into"
				+ " it to be immediatly enthralled by you.";
	}

	@Override
	public Character owner() {
		return owner;
	}

	@Override
	public void capitalize(Character attacker, Character victim, Encounter enc) {
		victim.add(new Flatfooted(victim,1));
		enc.engage(new Combat(attacker,victim,attacker.location()));
		attacker.location().remove(this);
	}
	@Override
	public void resolve(Character active) {
		if(active!=owner){
			trigger(active);
		}
	}
}
