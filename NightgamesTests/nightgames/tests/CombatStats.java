package nightgames.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import nightgames.areas.Area;
import nightgames.characters.Character;
import nightgames.characters.Eve;
import nightgames.characters.Kat;
import nightgames.characters.Player;
import nightgames.characters.Reyka;
import nightgames.characters.NPC;
import nightgames.characters.Personality;
import nightgames.characters.BasePersonality;
import nightgames.combat.Combat;
import nightgames.global.DebugFlags;
import nightgames.global.Global;

public class CombatStats {

	private static final Area	NULL_AREA	= new Area("", "", null);

	private List<Character>		combatants;
	private Map<String, Record>	records;
	private Setup				setup;

	private final AtomicInteger counter = new AtomicInteger();
	private final Object		recordLock	= new Object();

	public CombatStats(Setup setup) {
		this.setup = setup;
		records = new HashMap<>();
		combatants = setup.execute();
		combatants.forEach(c -> records.put(c.name(), new Record(c)));
		Global.debug = new boolean[DebugFlags.values().length];
	}

	private void test() {
		for (int i = 0; i < combatants.size(); i++) {
			for (int j = 0; j < i; j++) {
				fightMany(combatants.get(i), combatants.get(j), 100);
			}
		}
		System.out.println(counter.get());
		System.out.println(setup);
		records.forEach((c, r) -> System.out.println(
				c + ": " + (double) r.totalWins / (double) r.totalPlayed + "\n"
						+ r.toString()));
	}

	private void fightMany(Character c1, Character c2, int count) {
		//ExecutorService threadPool = Executors.newFixedThreadPool(50);
		System.out.println(
				String.format("%s vs. %s (%dX)", c1.name(), c2.name(), count));
		for (int i = 0; i < count; i++) {
			try {
				Character clone1 = c1.clone();
				Character clone2 = c2.clone();
				 fight(clone1, clone2);
				//threadPool.execute(() -> fight(clone1, clone2));
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();
			}
		}
		/*
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(3, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
	}

	private void fight(Character c1, Character c2) {
		((BasePersonality) ((NPC) c1).ai).character = (NPC) c1;
		((BasePersonality) ((NPC) c2).ai).character = (NPC) c2;
		Combat cbt = new Combat(c1, c2, NULL_AREA);
		cbt.automate();
		counter.incrementAndGet();
		synchronized (recordLock) {
			if (!cbt.winner.isPresent()) {
				System.err.println("Error - winner is empty");				
			} else if (cbt.winner.get().equals(Global.noneCharacter())) {
				recordOf(c1).draw(c2);
				recordOf(c2).draw(c1);
			} else if (cbt.winner.get().equals(c1)) {
				recordOf(c1).win(c2);
				recordOf(c2).lose(c1);
			} else if (cbt.winner.get().equals(c2)) {
				recordOf(c1).lose(c2);
				recordOf(c2).win(c1);
			} else {
				System.err.println("Error - unknown causes");
			}
		}
	}

	private Record recordOf(Character c) {
		return records.get(c.name());
	}

	public static void main(String[] args) {
		new Global(true);
		Global.newGame(new Player("Dummy"));
		Setup s1 = new Setup(1);
		// new CombatStats(s1).test();

		Setup s2 = new Setup(5);
		// new CombatStats(s2).test();

		Setup s3 = new Setup(20, new Reyka(), new Kat(), new Eve());
		new CombatStats(s3).test();

		System.exit(0);
	}

	private class Record {

		private Character				subject;
		private volatile int			totalPlayed, totalWins, totalLosses,
				totalDraws;
		private Map<String, Integer>	wins, losses, draws;

		Record(Character subject) {
			this.subject = subject;
			wins = new HashMap<>();
			losses = new HashMap<>();
			draws = new HashMap<>();
			combatants.stream().filter(c -> !c.equals(subject)).forEach(c -> {
				wins.put(c.name(), 0);
				losses.put(c.name(), 0);
				draws.put(c.name(), 0);
			});
		}

		synchronized void win(Character opp) {
			totalPlayed++;
			totalWins++;
			wins.put(opp.name(), wins.get(opp.name()) + 1);
		}

		synchronized void lose(Character opp) {
			totalPlayed++;
			totalLosses++;
			losses.put(opp.name(), losses.get(opp.name()) + 1);
		}

		synchronized void draw(Character opp) {
			totalPlayed++;
			totalDraws++;
			draws.put(opp.name(), draws.get(opp.name()) + 1);
		}

		@Override
		public String toString() {
			return "Record [subject=" + subject + "\n\t totalPlayed="
					+ totalPlayed + "\n\t totalWins=" + totalWins
					+ "\n\t totalLosses=" + totalLosses + "\n\t totalDraws="
					+ totalDraws + "\n\t wins=" + wins + "\n\t losses=" + losses
					+ "\n\t draws=" + draws + "]";
		}
	}

	private static class Setup {

		private int					level;
		private List<Personality>	extraChars;

		Setup(int level, Personality... extraChars) {
			this.level = level;
			this.extraChars = Arrays.asList(extraChars);
		}

		List<Character> execute() {
			extraChars.forEach(Global::newChallenger);
			List<Character> combatants = new ArrayList<>(
					Global.getCharacters());
			combatants.removeIf(Character::human);
			combatants.forEach(c -> {
				while (c.getLevel() < level)
					c.ding();
			});
			return combatants;
		}

		@Override
		public String toString() {
			return "Setup [level=" + level + ", extraChars=" + extraChars + "]";
		}

	}
}
