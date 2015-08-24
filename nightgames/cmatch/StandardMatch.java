package nightgames.cmatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nightgames.areas.Area;
import nightgames.areas.Cache;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.cmatch.prepare.ChangeSet;
import nightgames.cmatch.rules.StopRule;
import nightgames.global.Challenge;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.Item;
import nightgames.status.Hypersensitive;
import nightgames.status.Stsflag;

/**
 * Re-implementation of the original Match.
 */
public class StandardMatch extends CustomMatch {

	private List<Character> combatants;
	private Modifier condition;
	private Map<Character, Integer> scores;
	private int dropOffTime;

	public StandardMatch(Collection<Character> combatants, Modifier condition) {
		this.combatants = new ArrayList<>(combatants);
		this.condition = condition;
		this.scores = new HashMap<>();
		this.dropOffTime = 0;
		combatants.forEach(c -> scores.put(c, 0));
		map = Global.buildMap();
		this.combatants.get(0).place(map.get("Dorm"));
		this.combatants.get(1).place(map.get("Engineering"));
		if (this.combatants.size() >= 3) {
			this.combatants.get(2).place(map.get("Liberal Arts"));
		}
		if (this.combatants.size() >= 4) {
			this.combatants.get(3).place(map.get("Dining"));
		}
		if (this.combatants.size() >= 5) {
			this.combatants.get(4).place(map.get("Union"));
		}
		if (this.combatants.size() >= 6) {
			this.combatants.get(5).place(map.get("Bridge"));
		}
		if (this.combatants.size() >= 7) {
			this.combatants.get(6).place(map.get("Pool"));
		}
		for (Character player : combatants) {
			player.getStamina().fill();
			player.getArousal().empty();
			player.getMojo().empty();
			player.getWillpower().fill();
			if (player.getPure(Attribute.Science) > 0) {
				player.chargeBattery();
			}
		}
	}

	@Override
	public List<Character> getCombatants() {
		return combatants;
	}

	@Override
	public Map<Character, Integer> getScores() {
		return scores;
	}

	@Override
	public String preparationScene() {
		return /* TODO? */ "";
	}

	@Override
	protected ChangeSet changeSet() {
		return ChangeSet.emptyChangeSet();
	}

	@Override
	protected Collection<StopRule> stopRules() {
		return defaultStopRules();
	}

	public void manageConditions(Character player) {
		if (condition == Modifier.vibration) {
			player.tempt(5);
		} else if (condition == Modifier.vulnerable) {
			if (!player.is(Stsflag.hypersensitive)) {
				player.add(new Hypersensitive(player));
			}
		}
	}

	public int meanLvl() {
		int mean = 0;
		for (Character player : combatants) {
			mean += player.getLevel();
		}
		return mean / combatants.size();
	}

	public void dropPackage() {
		ArrayList<Area> areas = new ArrayList<Area>();
		areas.addAll(map.values());
		for (int i = 0; i < 10; i++) {
			Area target = areas.get(Global.random(areas.size()));
			if (!target.corridor() && !target.open() && target.env.size() < 5) {
				target.place(new Cache(meanLvl() + Global.random(11) - 4));
				Global.gui().message("<br><b>A new cache has been dropped off at " + target.name + "!</b>");
				break;
			}
		}
	}

	public void dropChallenge() {
		ArrayList<Area> areas = new ArrayList<Area>();
		areas.addAll(map.values());
		Area target = areas.get(Global.random(areas.size()));
		if (!target.open() && target.env.size() < 5) {
			target.place(new Challenge());
		}
	}

	@Override
	public String endScene() {
		return "";
	}

	@Override
	protected void preRound() {
		if (meanLvl() > 3 && Global.random(10) + dropOffTime >= 12) {
			dropPackage();
			dropOffTime = 0;
		}
		if (Global.checkFlag(Flag.challengeAccepted) && (getRoundCount() % 6 == 0)) {
			dropChallenge();
		}
		dropOffTime++;
	}

	@Override
	protected void postMatch() {
		int cloth = 0;
		int creward = 0;
		Character player = null;
		Character winner = null;
		for (Character combatant : scores.keySet()) {
			Global.gui().message(combatant.name() + " scored " + scores.get(combatant) + " victories.");
			combatant.gainMoney(scores.get(combatant) * combatant.prize());
			if (winner == null || scores.get(combatant) >= scores.get(winner)) {
				winner = combatant;
			}
			if (combatant.human()) {
				player = combatant;
			}
			while (combatant.has(Item.CassieTrophy)) {
				combatant.consume(Item.CassieTrophy, 1);
				combatant.gainMoney(combatant.prize());
				if (combatant.human()) {
					cloth++;
				}
			}
			while (combatant.has(Item.MaraTrophy)) {
				combatant.consume(Item.MaraTrophy, 1);
				combatant.gainMoney(combatant.prize());
				if (combatant.human()) {
					cloth++;
				}
			}
			while (combatant.has(Item.JewelTrophy)) {
				combatant.consume(Item.JewelTrophy, 1);
				combatant.gainMoney(combatant.prize());
				if (combatant.human()) {
					cloth++;
				}
			}
			while (combatant.has(Item.AngelTrophy)) {
				combatant.consume(Item.AngelTrophy, 1);
				combatant.gainMoney(combatant.prize());
				if (combatant.human()) {
					cloth++;
				}
			}
			while (combatant.has(Item.PlayerTrophy)) {
				combatant.consume(Item.PlayerTrophy, 1);
				combatant.gainMoney(combatant.prize());
				if (combatant.human()) {
					cloth++;
				}
			}
			while (combatant.has(Item.ReykaTrophy)) {
				combatant.consume(Item.ReykaTrophy, 1);
				combatant.gainMoney(combatant.prize());
				if (combatant.human()) {
					cloth++;
				}
			}
			while (combatant.has(Item.AiriTrophy)) {
				combatant.consume(Item.AiriTrophy, 1);
				combatant.gainMoney(combatant.prize());
				if (combatant.human()) {
					cloth++;
				}
			}
			while (combatant.has(Item.KatTrophy)) {
				combatant.consume(Item.KatTrophy, 1);
				combatant.gainMoney(combatant.prize());
				if (combatant.human()) {
					cloth++;
				}
			}
			for (Challenge c : combatant.challenges) {
				if (c.done) {
					combatant.gainMoney(c.reward());
					if (combatant.human()) {
						creward += c.reward();
					}
				}
			}
			combatant.challenges.clear();
			combatant.state = State.ready;
			combatant.change(Modifier.normal);
		}
		Global.gui().message("You made $" + scores.get(player) * player.prize() + " for defeating opponents.");
		int bonus = scores.get(player) * condition.bonus();
		winner.gainMoney(bonus);
		if (bonus > 0) {
			Global.gui().message("You earned an additional $" + bonus + " for accepting the handicap.");
		}
		if (winner == player) {
			Global.gui().message("You also earned a bonus of $" + 5 * player.prize() + " for placing first.");
		}
		winner.gainMoney(5 * winner.prize());
		Global.gui()
				.message("You traded in " + cloth + " sets of clothes for a total of $" + cloth * player.prize() + ".");
		if (creward > 0) {
			Global.gui().message("You also discover an envelope with $" + creward
					+ " slipped under the door to your room. Presumably it's payment for completed challenges.");
		}
		Character closest = null;
		int maxaffection = 0;
		for (Character rival : combatants) {
			if (rival.getAffection(player) > maxaffection) {
				closest = rival;
				maxaffection = rival.getAffection(player);
			}
		}
		if (Global.checkFlag(Flag.metLilly) && !Global.checkFlag(Flag.challengeAccepted) && Global.random(10) >= 7) {
			Global.gui().message(
					"\nWhen you gather after the match to collect your reward money, you notice Jewel is holding a crumpled up piece of paper and ask about it. "
							+ "<i>\"This? I found it lying on the ground during the match. It seems to be a worthless piece of trash, but I didn't want to litter.\"</i> Jewel's face is expressionless, "
							+ "but there's a bitter edge to her words that makes you curious. You uncrumple the note and read it.<p>'Jewel always acts like the dominant, always-on-top tomboy, "
							+ "but I bet she loves to be held down and fucked hard.'<p><i>\"I was considering finding whoever wrote the note and tying his penis in a knot,\"</i> Jewel says, still "
							+ "impassive. <i>\"But I decided to just throw it out instead.\"</i> It's nice that she's learning to control her temper, but you're a little more concerned with the note. "
							+ "It mentions Jewel by name and seems to be alluding to the games. You doubt one of the other girls wrote it. You should probably show it to Lilly.<p><i>\"Oh for fuck's "
							+ "sake..\"</i> Lilly sighs, exasperated. <i>\"I thought we'd seen the last of these. I don't know who writes them, but they showed up last year too. I'll have to do a second "
							+ "sweep of the grounds each night to make sure they're all picked up by morning. They have competitors' names on them, so we absolutely cannot let a normal student find "
							+ "one.\"</i> She toys with a pigtail idly while looking annoyed. <i>\"For what it's worth, they do seem to pay well if you do what the note says that night. Do with them what "
							+ "you will.\"</i><br>");
			Global.flag(Flag.challengeAccepted);
		}
		if (maxaffection >= 15 && closest != null) {
			closest.afterParty();
		} else {
			Global.gui().message("You walk back to your dorm and get yourself cleaned up.");
		}
	}
}
