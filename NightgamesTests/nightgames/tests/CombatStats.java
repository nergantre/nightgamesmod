package nightgames.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import nightgames.areas.Area;
import nightgames.characters.*;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.daytime.Daytime;
import nightgames.global.DebugFlags;
import nightgames.global.Global;

public class CombatStats {
    private static final Area NULL_AREA = new Area("", "", null);
    private static final int MATCH_COUNT = 1000;

    private List<Character> combatants;
    private Map<String, Record> records;
    private Setup setup;

    private final AtomicInteger counter = new AtomicInteger();
    private final Object recordLock = new Object();

    public CombatStats(Setup setup) {
        this.setup = setup;
        records = new HashMap<>();
        combatants = setup.execute();
        combatants.forEach(c -> records.put(c.name(), new Record(c)));
        //Global.save(true);
        Global.debug = new boolean[DebugFlags.values().length];
    }

    private void test() {
        for (int i = 0; i < combatants.size(); i++) {
            for (int j = 0; j < i; j++) {
                fightMany(combatants.get(i), combatants.get(j), MATCH_COUNT);
            }
        }
        StringBuilder results = new StringBuilder(setup.toString());
        System.out.println(counter.get());
        System.out.println(setup);
        records.forEach((c, r) -> {
            String record = c + ": " + (double) r.totalWins / (double) r.totalPlayed + "\n" + r.toString();
            System.out.println(record);
            results.append(record);
        });
        File output = new File(setup.outputName());
        FileWriter fw;
        try {
            fw = new FileWriter(output);
            fw.write(results.toString());
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void fightMany(Character c1, Character c2, int count) {
        // ExecutorService threadPool = Executors.newFixedThreadPool(50);
        System.out.println(String.format("%s vs. %s (%dX)", c1.name(), c2.name(), count));
        for (int i = 0; i < count; i++) {
            try {
                Character clone1 = c1.clone();
                Character clone2 = c2.clone();
                fight(clone1, clone2);
                // threadPool.execute(() -> fight(clone1, clone2));
            } catch (CloneNotSupportedException e1) {
                e1.printStackTrace();
            }
        }
        /*
         * threadPool.shutdown(); try { threadPool.awaitTermination(3, TimeUnit.DAYS); } catch (InterruptedException e) { e.printStackTrace(); }
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
        Global.newGame("Dummy");
        Setup s1 = new Setup(1);
        // new CombatStats(s1).test();

        Setup s2 = new Setup(5);
        // new CombatStats(s2).test();

        for (int i = 5; i < 75; i += 5) {
            Setup s3 = new Setup(i, new Reyka(), new Kat(), new Eve());
            new CombatStats(s3).test();
        }

        System.exit(0);
    }

    private class Record {

        private Character subject;
        private volatile int totalPlayed, totalWins, totalLosses, totalDraws;
        private Map<String, Integer> wins, losses, draws;

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
            return "Record [subject=" + subject + "\n\t totalPlayed=" + totalPlayed + "\n\t totalWins=" + totalWins
                            + "\n\t totalLosses=" + totalLosses + "\n\t totalDraws=" + totalDraws + "\n\t wins=" + wins
                            + "\n\t losses=" + losses + "\n\t draws=" + draws + "]";
        }
    }

    public static class Setup {

        private int level;
        private List<Personality> extraChars;

        public Setup(int level, Personality... extraChars) {
            this.level = level;
            this.extraChars = Arrays.asList(extraChars);
        }

        public String outputName() {
            return String.format("CombatStats-%d-%s-%d.txt", level, extraChars.stream()
                            .map(p -> p.getClass().getSimpleName().substring(0, 1)).collect(Collectors.joining()),
                            MATCH_COUNT);
        }

        public List<Character> execute() {
            extraChars.forEach(Global::newChallenger);
            List<Character> combatants = new ArrayList<>(Global.getCharacters());
            combatants.removeIf(Character::human);
            combatants.forEach(c -> {
                while (c.getLevel() < level) {
                    c.ding();
                    Character partner;
                    do {
                        partner = (Character) Global.pickRandom(combatants.toArray());
                    } while (c == partner);
                    Daytime.train(partner, c, (Attribute) Global.pickRandom(c.att.keySet().toArray()));
                }
                c.modMoney(level * 500);
                Global.day = new Daytime(new Player("<player>"));
                Global.day.advance(999);
                Global.day.plan(true);
            });

            return combatants;
        }

        @Override
        public String toString() {
            return "Setup [level=" + level + ", extraChars=" + extraChars + "]";
        }
    }
}
