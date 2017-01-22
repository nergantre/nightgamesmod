package nightgames.global;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;

import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.areas.Cache;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.characters.Trait;
import nightgames.modifier.Modifier;
import nightgames.status.Stsflag;
import nightgames.status.addiction.Addiction;

public class Match {
    protected int time;
    protected int dropOffTime;
    protected HashMap<String, Area> map;
    public ArrayList<Character> combatants;
    protected HashMap<Character, Integer> score;
    protected int index;
    protected boolean pause;
    public Modifier condition;
    public MatchData matchData;

    public Match(Collection<Character> combatants, Modifier condition) {
        this.combatants = new ArrayList<Character>();
        for (Character c : combatants) {
            this.combatants.add(c);
        }
        matchData = new MatchData();
        score = new HashMap<Character, Integer>();
        this.condition = condition;
        map = Global.buildMap();
        for (Character combatant : combatants) {
            score.put(combatant, 0);
            Global.gui().message(Global.gainSkills(combatant));
            Global.learnSkills(combatant);
            combatant.matchPrep(this);
        }
        time = 0;
        dropOffTime = 0;
        pause = false;
        Deque<Area> areaList = new ArrayDeque<>();
        areaList.add(map.get("Dorm"));
        areaList.add(map.get("Engineering"));
        areaList.add(map.get("Liberal Arts"));
        areaList.add(map.get("Dining"));
        areaList.add(map.get("Union"));
        areaList.add(map.get("Bridge"));
        areaList.add(map.get("Library"));
        areaList.add(map.get("Tunnel"));
        areaList.add(map.get("Workshop"));
        areaList.add(map.get("Pool"));
        combatants.forEach(character -> {
            if (character.has(Trait.immobile)) {
                character.place(map.get("Courtyard"));
            } else {
                character.place(areaList.pop());
            }
        });

        for (Character player : combatants) {
            player.getStamina().fill();
            player.getArousal().empty();
            player.getMojo().empty();
            player.getWillpower().fill();
            if (player.getPure(Attribute.Science) > 0) {
                player.chargeBattery();
            }
            manageConditions(player);
        }
    }

    public MatchType getType() {
        return MatchType.NORMAL;
    }

    public void round() {
        while (time < 36) {
            if (index >= combatants.size()) {
                index = 0;
                if (meanLvl() > 3 && Global.random(10) + dropOffTime >= 12) {
                    dropPackage();
                    dropOffTime = 0;
                }
                if (Global.checkFlag(Flag.challengeAccepted) && (time == 6 || time == 12 || time == 18 || time == 24)) {
                    dropChallenge();
                }
                time++;
                dropOffTime++;
            }
            getAreas().forEach(area -> area.setPinged(false));
            while (index < combatants.size()) {
                Global.gui().refresh();
                if (combatants.get(index).state != State.quit) {
                    Character self = combatants.get(index);
                    self.upkeep();
                    manageConditions(self);
                    self.move();
                    if (Global.isDebugOn(DebugFlags.DEBUG_SCENE) && index < combatants.size()) {
                        System.out.println(self.getTrueName() + (self.is(Stsflag.disguised) ? "(Disguised)" : "") + " is in "
                                        + self.location().name);
                    }
                }
                index++;
                if (pause) {
                    return;
                }
            }
        }
        end();
    }

    public void pause() {
        pause = true;
    }

    public void resume() {
        pause = false;
        round();
    }

    public void end() {
        for (Character next : combatants) {
            next.finishMatch();
        }
        Global.gui().clearText();
        Global.gui().message("Tonight's match is over.");
        int cloth = 0;
        int creward = 0;
        Character player = null;
        Character winner = null;
        for (Character combatant : score.keySet()) {
            Global.gui().message(combatant.getTrueName() + " scored " + score.get(combatant) + " victories.");
            combatant.modMoney(score.get(combatant) * combatant.prize());
            if (winner == null || score.get(combatant) >= score.get(winner)) {
                winner = combatant;
            }
            if (combatant.human()) {
                player = combatant;
            }
            for (Character other : combatants) {
                while (combatant.has(other.getTrophy())) {
                    combatant.consume(other.getTrophy(), 1, false);
                    combatant.modMoney(other.prize());
                    if (combatant.human()) {
                        cloth++;
                    }
                }
            }
            for (Challenge c : combatant.challenges) {
            	if(c.done){
                    combatant.money+=c.reward()+(c.reward()*3*combatant.getRank());
                    if(combatant.human()){
                        creward += c.reward()+(c.reward()*3*combatant.getRank());
                    }
                }
            }
            combatant.challenges.clear();
            combatant.state = State.ready;
            condition.undoItems(combatant);
            combatant.change();
        }
        Global.gui().message("You made $" + score.get(player) * player.prize() + " for defeating opponents.");
        int bonus = score.get(player) * condition.bonus();
        winner.modMoney(bonus);
        if (bonus > 0) {
            Global.gui().message("You earned an additional $" + bonus + " for accepting the handicap.");
        }
        condition.extraWinnings(player, score.get(player));
        if (winner == player) {
            Global.gui().message("You also earned a bonus of $" + 5 * player.prize() + " for placing first.");
            Global.flag(Flag.victory);
        }
        winner.modMoney(5 * winner.prize());
        Global.gui().message("You traded in " + cloth + " sets of clothes for a total of $" + cloth * player.prize()
                        + ".<br/>");
        if (creward > 0) {
            Global.gui().message("You also discover an envelope with $" + creward
                            + " slipped under the door to your room. Presumably it's payment for completed challenges.<br/>");
        }
        int maxaffection = 0;
        for (Character rival : combatants) {
            if (rival.getAffection(player) > maxaffection) {
                maxaffection = rival.getAffection(player);
            }
        }
        if (Global.checkFlag(Flag.metLilly) && !Global.checkFlag(Flag.challengeAccepted) && Global.random(10) >= 7) {
            Global.gui().message(
                            "\nWhen you gather after the match to collect your reward money, you notice Jewel is holding a crumpled up piece of paper and ask about it. "
                                            + "<i>\"This? I found it lying on the ground during the match. It seems to be a worthless piece of trash, but I didn't want to litter.\"</i> Jewel's face is expressionless, "
                                            + "but there's a bitter edge to her words that makes you curious. You uncrumple the note and read it.<br/><br/>'Jewel always acts like the dominant, always-on-top tomboy, "
                                            + "but I bet she loves to be held down and fucked hard.'<br/><br/><i>\"I was considering finding whoever wrote the note and tying his penis in a knot,\"</i> Jewel says, still "
                                            + "impassive. <i>\"But I decided to just throw it out instead.\"</i> It's nice that she's learning to control her temper, but you're a little more concerned with the note. "
                                            + "It mentions Jewel by name and seems to be alluding to the games. You doubt one of the other girls wrote it. You should probably show it to Lilly.<br/><br/><i>\"Oh for fuck's "
                                            + "sake..\"</i> Lilly sighs, exasperated. <i>\"I thought we'd seen the last of these. I don't know who writes them, but they showed up last year too. I'll have to do a second "
                                            + "sweep of the grounds each night to make sure they're all picked up by morning. They have competitors' names on them, so we absolutely cannot let a normal student find "
                                            + "one.\"</i> She toys with a pigtail idly while looking annoyed. <i>\"For what it's worth, they do seem to pay well if you do what the note says that night. Do with them what "
                                            + "you will.\"</i><br/>");
            Global.flag(Flag.challengeAccepted);
        }
        /*
         * if (maxaffection >= 15 && closest != null) { closest.afterParty(); } else { Global.gui().message("You walk back to your dorm and get yourself cleaned up."); }
         */
        for (Character character : combatants) {
            if (character.getFlag("heelsTraining") >= 50 && !character.hasPure(Trait.proheels)) {
                if (character.human()) {
                    Global.gui().message(
                                    "<br/>You've gotten comfortable at fighting in heels.<br/><b>Gained Trait: Heels Pro</b>");
                }
                character.add(Trait.proheels);
            }
            if (character.getFlag("heelsTraining") >= 100 && !character.hasPure(Trait.masterheels)) {
                if (character.human()) {
                    Global.gui().message("<br/>You've mastered fighting in heels.<br/><b>Gained Trait: Heels Master</b>");
                }
                character.add(Trait.masterheels);
            }
        }
        Global.getPlayer().getAddictions().forEach(Addiction::endNight);
        new Postmatch(Global.getPlayer(), combatants);
    }

    public int getHour() {
        return 10 + time / 12;
    }

    public String getTime() {
        int hour = getHour();
        if (hour > 12) {
            hour = hour % 12;
        }
        if (time % 12 < 2) {
            return hour + ":0" + time % 12 * 5;
        } else {
            return hour + ":" + time % 12 * 5;
        }
    }

    public Area gps(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        }
        return null;
    }

    public void score(Character character, int points) {
        score.put(character, Integer.valueOf(score.get(character).intValue() + points));
    }

    public void manageConditions(Character player) {
        /*
         * if (condition == DefaultModifier.vibration) { player.tempt(5); } else if (condition == DefaultModifier.vulnerable) { if (!player.is(Stsflag.hypersensitive)) { player.add(new Hypersensitive(player)); } }
         */
        condition.handleOutfit(player);
        condition.handleItems(player);
        condition.handleStatus(player);
        condition.handleTurn(player, this);
        if (player.human()) {
            Global.getPlayer().getAddictions().forEach(Addiction::refreshWithdrawal);
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
                Global.gui().message("<br/><b>A new cache has been dropped off at " + target.name + "!</b>");
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

    public void quit() {
        Character human = Global.getPlayer();
        if (human.state == State.combat) {
            if (human.location().fight.getCombat() != null) {
                human.location().fight.getCombat().forfeit(human);
            }
            human.location().endEncounter();
        }
        human.travel(new Area("Retirement", "", Movement.retire));
        human.state = State.quit;
        resume();
    }

    public Collection<Movement> getResupplyAreas(Character ch) {
        return Arrays.asList(Movement.union, Movement.dorm);
    }

    public Collection<Area> getAreas() {
        return map.values();
    }
    
    public String genericRoomDescription() {
        return "room";
    }
    
    public MatchData getMatchData() {
        return matchData;
    }
}
