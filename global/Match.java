package global;

import items.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import status.Hypersensitive;
import status.Stsflag;


import characters.Character;
import characters.State;

import actions.Movement;
import areas.Area;
import areas.Cache;

public class Match {
	private int time;
	private int dropOffTime;
	private HashMap<String,Area> map;
	public ArrayList<Character> combatants;
	private HashMap<Character,Integer> score;
	private int index;
	private boolean pause;
	public Modifier condition;
	
	public Match(Collection<Character> combatants, Modifier condition){
		this.combatants=new ArrayList<Character>();
		for (Character c : combatants) {
				this.combatants.add(c);
		}
		this.score=new HashMap<Character,Integer>();
		this.condition=condition;
		Global.gui().startMatch();
		for(Character combatant:combatants){
			score.put(combatant, 0);
			Global.gui().message(Global.gainSkills(combatant));
			Global.learnSkills(combatant);
		}
		time=0;
		dropOffTime = 0;
		map = Global.buildMap();
		pause=false;
		this.combatants.get(0).place(map.get("Dorm"));
		this.combatants.get(1).place(map.get("Engineering"));
		if(this.combatants.size()>=3){
			this.combatants.get(2).place(map.get("Liberal Arts"));
		}
		if(this.combatants.size()>=4){
			this.combatants.get(3).place(map.get("Dining"));
		}
		if(this.combatants.size()>=5){
			this.combatants.get(4).place(map.get("Union"));
		}
		if(this.combatants.size()>=6){
			this.combatants.get(5).place(map.get("Bridge"));
		}
		if(this.combatants.size()>=7){
			this.combatants.get(6).place(map.get("Pool"));
		}
	}
	public void round(){
		while(time<36){
			if(index>=combatants.size()){
				index=0;
				if(meanLvl()>5&&Global.random(20)+dropOffTime>=22){
					dropPackage();
					dropOffTime = 0;
				}
				if(Global.checkFlag(Flag.challengeAccepted)&&(time==6||time==12||time==18||time==24)){
					dropChallenge();
				}
				time++;
				dropOffTime++;
			}
			while(index<combatants.size()){
				Global.gui().refresh();
				if(combatants.get(index).state!=State.quit){
					combatants.get(index).upkeep();
					if(combatants.get(index).human()){
						manageConditions(combatants.get(index));
					}
					combatants.get(index).move();
					if(Global.isDebugOn(DebugFlags.DEBUG_SCENE) && index < combatants.size()){
						System.out.println(combatants.get(index).name()+" is in "+combatants.get(index).location().name);
					}
				}
				index++;
				if(pause){
					return;
				}
			}
		}
		end();
	}
	public void pause(){
		pause=true;
	}
	public void resume(){
		pause=false;
		round();
	}
	public void end(){
		for(Character next:combatants){
			next.finishMatch();
		}
		Global.gui().clearText();
		Global.gui().message("Tonight's match is over.");
		int cloth=0;
		int creward=0;
		Character player=null;
		Character winner=null;
		for(Character combatant:score.keySet()){
			Global.gui().message(combatant.name()+" scored "+score.get(combatant)+" victories.");
			combatant.gainMoney(score.get(combatant)*combatant.prize());
			if(winner==null||score.get(combatant)>=score.get(winner)){
				winner=combatant;
			}
			if(combatant.human()){
				player=combatant;
			}
			while(combatant.has(Item.CassieTrophy)){
				combatant.consume(Item.CassieTrophy,1);
				combatant.gainMoney(combatant.prize());
				if(combatant.human()){
					cloth++;
				}
			}
			while(combatant.has(Item.MaraTrophy)){
				combatant.consume(Item.MaraTrophy,1);
				combatant.gainMoney(combatant.prize());
				if(combatant.human()){
					cloth++;
				}
			}
			while(combatant.has(Item.JewelTrophy)){
				combatant.consume(Item.JewelTrophy,1);
				combatant.gainMoney(combatant.prize());
				if(combatant.human()){
					cloth++;
				}
			}
			while(combatant.has(Item.AngelTrophy)){
				combatant.consume(Item.AngelTrophy,1);
				combatant.gainMoney(combatant.prize());
				if(combatant.human()){
					cloth++;
				}
			}
			while(combatant.has(Item.PlayerTrophy)){
				combatant.consume(Item.PlayerTrophy,1);
				combatant.gainMoney(combatant.prize());
				if(combatant.human()){
					cloth++;
				}
			}
			while(combatant.has(Item.ReykaTrophy)){
				combatant.consume(Item.ReykaTrophy,1);
				combatant.gainMoney(combatant.prize());
				if(combatant.human()){
					cloth++;
				}
			}
			while(combatant.has(Item.AiriTrophy)){
				combatant.consume(Item.AiriTrophy,1);
				combatant.gainMoney(combatant.prize());
				if(combatant.human()){
					cloth++;
				}
			}
			while(combatant.has(Item.KatTrophy)){
				combatant.consume(Item.KatTrophy,1);
				combatant.gainMoney(combatant.prize());
				if(combatant.human()){
					cloth++;
				}
			}
			for(Challenge c: combatant.challenges){
				if(c.done){
					combatant.gainMoney(c.reward());
					if(combatant.human()){
						creward += c.reward();
					}
				}
			}
			combatant.challenges.clear();
			combatant.state=State.ready;
			combatant.change(Modifier.normal);
		}
		Global.gui().message("You made $"+score.get(player)*player.prize()+" for defeating opponents.");
		int bonus = score.get(player)*condition.bonus();
		winner.gainMoney(bonus);
		if(bonus>0){
			Global.gui().message("You earned an additional $"+bonus+" for accepting the handicap.");
		}
		if(winner==player){
			Global.gui().message("You also earned a bonus of $"+5*player.prize()+" for placing first.");
		}
		winner.gainMoney(5*winner.prize());
		Global.gui().message("You traded in "+cloth+" sets of clothes for a total of $"+cloth*player.prize()+".");
		if(creward>0){
			Global.gui().message("You also discover an envelope with $"+creward+" slipped under the door to your room. Presumably it's payment for completed challenges.");
		}
		Character closest=null;
		int maxaffection=0;
		for(Character rival:combatants){
			if(rival.getAffection(player)>maxaffection){
				closest=rival;
				maxaffection=rival.getAffection(player);
			}
		}
		if(Global.checkFlag(Flag.metLilly)&&!Global.checkFlag(Flag.challengeAccepted)&&Global.random(10)>=7){
			Global.gui().message("\nWhen you gather after the match to collect your reward money, you notice Jewel is holding a crumpled up piece of paper and ask about it. " +
					"<i>\"This? I found it lying on the ground during the match. It seems to be a worthless piece of trash, but I didn't want to litter.\"</i> Jewel's face is expressionless, " +
					"but there's a bitter edge to her words that makes you curious. You uncrumple the note and read it.<p>'Jewel always acts like the dominant, always-on-top tomboy, " +
					"but I bet she loves to be held down and fucked hard.'<p><i>\"I was considering finding whoever wrote the note and tying his penis in a knot,\"</i> Jewel says, still " +
					"impassive. <i>\"But I decided to just throw it out instead.\"</i> It's nice that she's learning to control her temper, but you're a little more concerned with the note. " +
					"It mentions Jewel by name and seems to be alluding to the games. You doubt one of the other girls wrote it. You should probably show it to Lilly.<p><i>\"Oh for fuck's " +
					"sake..\"</i> Lilly sighs, exasperated. <i>\"I thought we'd seen the last of these. I don't know who writes them, but they showed up last year too. I'll have to do a second " +
					"sweep of the grounds each night to make sure they're all picked up by morning. They have competitors' names on them, so we absolutely cannot let a normal student find " +
					"one.\"</i> She toys with a pigtail idly while looking annoyed. <i>\"For what it's worth, they do seem to pay well if you do what the note says that night. Do with them what " +
					"you will.\"</i><br>");
			Global.flag(Flag.challengeAccepted);
		}
		if(maxaffection>=15&&closest!=null){
			closest.afterParty();
		}
		else{
			Global.gui().message("You walk back to your dorm and get yourself cleaned up.");
		}
		if(Global.checkFlag(Flag.autosave)){
			Global.save(true);
		}
		Global.gui().endMatch();
	}
	public String getTime(){
		int hour = 10+time/12;
		if(hour>12)
			hour = (hour%12);
		if(time%12<2){
			return (hour)+":0"+(time%12)*5;
		}
		else{
			return (hour)+":"+(time%12)*5;
		}
	}
	public Area gps(String name){
		if(map.containsKey(name)){
			return map.get(name);
		}
		return null;
	}
	public void score(Character character) {
		score.put(character, score.get(character)+1);
	}
	public void manageConditions(Character player){
		if(condition==Modifier.vibration){
			player.tempt(5);
		}
		else if(condition==Modifier.vulnerable){
			if(!player.is(Stsflag.hypersensitive)){
				player.add(new Hypersensitive(player));
			}
		}
	}
	public int meanLvl(){
		int mean = 0;
		for(Character player: combatants){
			mean+=player.getLevel();
		}
		return mean/combatants.size();
	}
	public void dropPackage(){
		ArrayList<Area> areas = new ArrayList<Area>();
		areas.addAll(map.values());
		Area target = areas.get(Global.random(areas.size()));
		if(!target.corridor()&&!target.open()&&target.env.size()<5){
			target.place(new Cache(meanLvl()+Global.random(11)-4));
		}
	}
	public void dropChallenge(){
		ArrayList<Area> areas = new ArrayList<Area>();
		areas.addAll(map.values());
		Area target = areas.get(Global.random(areas.size()));
		if(!target.open()&&target.env.size()<5){
			target.place(new Challenge());
		}
	}
	public void quit() {
		Character human = Global.getPlayer();
		if(human.state==State.combat){
			if(human.location().fight.getCombat()!=null){
				human.location().fight.getCombat().forfeit(human);
			}
			human.location().endEncounter();
		}
		human.travel(new Area("Retirement","",Movement.retire));
		human.state=State.quit;
		resume();
	}
}
