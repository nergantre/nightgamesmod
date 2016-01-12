package nightgames.ftc;

import nightgames.areas.Area;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.combat.Combat;
import nightgames.combat.Encounter;
import nightgames.global.Encs;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.stance.Mount;
import nightgames.stance.Pin;
import nightgames.status.Bound;
import nightgames.status.Flatfooted;
import nightgames.trap.Trap;

public class FTCEncounter extends Encounter {

	private static final long serialVersionUID = 5190164935968044626L;

	public FTCEncounter(Character first, Character second, Area location) {
		super(first, second, location);
	}

	@Override
	public boolean spotCheck() {
		if (!(p1.eligible(p2) && p2.eligible(p1)))
			return super.spotCheck();
		if (p1.state == State.inTree) {
			treeAmbush(p1, p2);
		} else if (p2.state == State.inTree) {
			treeAmbush(p2, p1);
		} else if (p1.state == State.inBushes) {
			bushAmbush(p1, p2);
		} else if (p2.state == State.inBushes) {
			bushAmbush(p2, p1);
		} else if (p1.state == State.inPass) {
			passAmbush(p1, p2);
		} else if (p2.state == State.inPass) {
			passAmbush(p2, p1);
		} else {
			return super.spotCheck();
		}
		return true;
	}

	private void treeAmbush(Character attacker, Character victim) {
		fightTime = 2;
		victim.add(new Flatfooted(victim, 3));
		if (attacker.has(Item.Handcuffs))
			victim.add(new Bound(victim, 75, "handcuffs"));
		else
			victim.add(new Bound(victim, 50, "zip-tie"));
		if (p1.human() || p2.human()) {
			fight = Global.gui().beginCombat(attacker, victim, 0);
			fight.setStance(new Pin(attacker, victim));
			String message = "";
			if (victim.human()) {
				message += "As you walk down the trail, you hear a slight rustling in the"
						+ " leaf canopy above you. You look up, but all you see is a flash of ";
				if (attacker.mostlyNude()) {
					message += "nude flesh";
				} else {
					message += "clothes";
				}
				message += " before you are pushed to the ground. Before you have a chance to process"
						+ " what's going on, your hands are tied behind your back and your"
						+ " attacker, who now reveals {self:reflective} to be {self:name},"
						+ " whispers in your ear \"Happy to see me, {other:name}?\"";
			} else {
				message += "Your patience finally pays off as {other:name} approaches the"
						+ " tree you are hiding in. You wait until the perfect moment,"
						+ " when {other:pronoun} is right beneath you, before you jump"
						+ " down. You land right on {other:possessive} shoulders, pushing"
						+ " {other:direct-object} firmly to the soft soil. Pulling our a ";
				if (attacker.has(Item.Handcuffs)) {
					message += "pair of handcuffs, ";
				} else {
					message += "zip-tie, ";
				}
				message += " you bind {other:possessive} hands together. There are worse" + " ways to start a match.";
			}
			Global.gui().message(Global.format(message, attacker, victim));
		} else {
			Global.gui().refresh();
			fight = new Combat(attacker, victim, location, 0);
			fight.setStance(new Pin(attacker, victim));
		}
	}

	private void bushAmbush(Character attacker, Character victim) {
		fightTime = 2;
		victim.add(new Flatfooted(victim, 3));
		if (attacker.has(Item.Handcuffs))
			victim.add(new Bound(victim, 75, "handcuffs"));
		else
			victim.add(new Bound(victim, 50, "zip-tie"));
		if (p1.human() || p2.human()) {
			fight = Global.gui().beginCombat(attacker, victim, 0);
			fight.setStance(new Mount(attacker, victim));
			String message = "";
			if (victim.human()) {
				message += "You are having a little difficulty wading through the dense"
						+ " bushes. Your foot hits something, causing you to trip and fall flat"
						+ " on your face. A weight settles on your back and your arms are"
						+ " pulled behind your back and tied together with something. You"
						+ " are rolled over, and {self:name} comes into view as {self:pronoun}"
						+ " settles down on your belly. \"Hi, {other:name}. Surprise!\"";
			} else {
				message += "Hiding in the bushes, your vision is somewhat obscured. This is"
						+ " not a big problem, though, as the rustling leaves alert you to"
						+ " passing prey. You inch closer to where you suspect they are headed,"
						+ " and slowly {other:name} comes into view. Just as {other:pronoun}"
						+ " passes you, you stick out a leg and trip {other:direct-object}."
						+ " With a satisfying crunch of the leaves, {other:pronoun} falls."
						+ " Immediately you jump on {other:possessive} back and tie "
						+ "{other:possessive} hands together.";
			}
			Global.gui().message(Global.format(message, attacker, victim));
		} else {
			Global.gui().refresh();
			fight = new Combat(attacker, victim, location, 0);
			fight.setStance(new Pin(attacker, victim));
		}
	}

	private void passAmbush(Character attacker, Character victim) {
		int attackerScore = 30 + attacker.get(Attribute.Speed) * 10 + attacker.get(Attribute.Perception) * 5
				+ Global.random(30);
		int victimScore = victim.get(Attribute.Speed) * 10 + victim.get(Attribute.Perception) * 5 + Global.random(30);
		String message = "";
		if (attackerScore > victimScore) {
			if (attacker.human()) {
				message += "You wait in a small alcove, waiting for someone to pass you."
						+ " Eventually, you hear footsteps approaching and you get ready."
						+ " As soon as {other:name} comes into view, you jump out and push"
						+ " {other:pronoun} against the opposite wall. The impact seems to"
						+ " daze {other:direct-object}, giving you an edge in the ensuing fight.";
			} else if (victim.human()) {
				message += "Of course you know that walking through a narrow pass is a"
						+ " strategic risk, but you do so anyway. Suddenly, {self:name}"
						+ " flies out of an alcove, pushing you against the wall on the"
						+ " other side. The impact knocks the wind out of you, putting you" + " at a disadvantage.";
			}
			fight = Global.gui().beginCombat(attacker, victim);
			victim.add(new Flatfooted(victim, 3));
		} else {
			if (attacker.human()) {
				message += "While you are hiding behind a rock, waiting for someone to"
						+ " walk around the corner up ahead, you hear a soft cruch behind"
						+ " you. You turn around, but not fast enough. {other:name} is"
						+ " already on you, and has grabbed your shoulders. You are unable"
						+ " to prevent {other:direct-object} from throwing you to the ground,"
						+ " and {other:pronoun} saunters over. \"Were you waiting for me,"
						+ " {self:name}? Well, here I am.\"";
			} else if (victim.human()) {
				message += "You are walking through the pass when you see {self:name}"
						+ " crouched behind a rock. Since {self:pronoun} is very focused"
						+ " in looking the other way, {self:pronoun} does not see you coming."
						+ " Not one to look a gift horse in the mouth, you sneak up behind"
						+ " {self:direct-object} and grab {self:direct-object} in a bear hug."
						+ " Then, you throw {self:direct-object} to the side, causing"
						+ " {self:direct-object} to fall to the ground.";
			}
			fight = Global.gui().beginCombat(attacker, victim);
			attacker.add(new Flatfooted(attacker, 3));
		}
		if (attacker.human() || victim.human()) {
			Global.gui().message(Global.format(message, attacker, victim));
		} else {

		}
	}

	@Override
	public void parse(Encs choice, Character self, Character target) {
		parse(choice, self, target, null);
	}

	@Override
	public void parse(Encs choice, Character self, Character target, Trap trap) {
		assert trap != null || choice != Encs.capitalize;
		if (!isFTCSpecific(choice)) {
			super.parse(choice, self, target, trap);
		} else {
			switch (choice) {
			case treeAmbush:
				treeAmbush(self, target);
				break;
			case bushAmbush:
				bushAmbush(self, target);
				break;
			case passAmbush:
				passAmbush(self, target);
				break;
			default:
			}
		}
	}

	private static boolean isFTCSpecific(Encs enc) {
		return enc == Encs.treeAmbush || enc == Encs.bushAmbush || enc == Encs.passAmbush;
	}
}
