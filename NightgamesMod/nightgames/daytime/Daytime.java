package nightgames.daytime;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.characters.Player;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.global.Prematch;

import java.util.ArrayList;

public class Daytime {
	private ArrayList<Activity> activities;
	private Player player;
	private int time;
	private Threesomes threesome;
	
	public Daytime(Player player){
		this.player=player;
		buildActivities();
		if (Global.checkFlag(Flag.metAlice)) {
			 if (Global.checkFlag(Flag.victory)) {
				 Global.unflag(Flag.AliceAvailable);
			 } else {
			 	Global.flag(Flag.AliceAvailable);
			 }
		}
		Global.unflag(Flag.threesome);
		this.threesome = new Threesomes(player);
		time=3;
		if(player.getLevel()>=10&&player.getRank()==0){
			Global.gui().clearText();
			Global.gui().message("The next day, just after getting out of class you receive call from a restricted number. Normally you'd just ignore it, " +
					"but for some reason you feel compelled to answer this one. You're greeted by a man with a clear deep voice. <i>\"Hello "+player.name()+", and " +
					"congratulations. Your performance in your most recent matches has convinced me you're ready for a higher level of play. You're promoted to " +
					"ranked status, effective immediately. This new status earns you a major increase in monetary rewards and many new opportunities. I'll leave " +
					"the details to others. I just wanted to congratulate you personally.\"</i> Wait, wait. That's not the end of the call. This guy is clearly " +
					"someone overseeing the Game, but he hasn't even given you his name. Why all the secrecy? <i>\"If you're looking for more information, you " +
					"know someone who sells it.\"</i> There's a click and the call ends.");
			player.rankup();
		}
		else{
			Global.gui().message("You try to get as much sleep as you can before your morning classes.<p>You're done with classes by mid-afternoon" +
					" and have the rest of the day free.");
		}
				
		plan();
	}
	public void plan(){
		String threescene;
		if(time<10){
			Global.gui().message("It is currently "+time+":00. Your next match starts at 10:00.");
			Global.gui().refresh();
			Global.gui().clearCommand();
			if(!Global.checkFlag(Flag.threesome)){
				threescene = threesome.getScene();
				if(threescene!=""){
					threesome.visit(threescene);
					return;
				}
			}
			for(Activity act: activities){
				if(act.known()&&act.time()+time<=10){
					Global.gui().addActivity(act);
				}
			}
		}
		else{
			for(Character npc: Global.everyone()){
				if(!npc.human()){
					if(npc.getLevel()>=10&&npc.getRank()==0){
						npc.rankup();
					}
					((NPC)npc).daytime();
				}
			}
			//Global.gui().nextMatch();
			if(Global.checkFlag(Flag.autosave)){
				Global.save(true);
			}
			new Prematch(player);
		}
	}
	public void buildActivities(){
		activities = new ArrayList<Activity>();
		activities.add(new Exercise(player));
		activities.add(new Porn(player));
		activities.add(new VideoGames(player));
		activities.add(new Informant(player));
		activities.add(new BlackMarket(player));
		activities.add(new BodyShop(player));
		activities.add(new XxxStore(player));
		activities.add(new HWStore(player));
		activities.add(new Bookstore(player));
		activities.add(new Meditation(player));
		activities.add(new AngelTime(player));
		activities.add(new CassieTime(player));
		activities.add(new JewelTime(player));
		activities.add(new MaraTime(player));
		if(Global.checkFlag(Flag.Kat)){
			activities.add(new KatTime(player));
		}
		activities.add(new Closet(player));
		activities.add(new ClothingStore(player));
		activities.add(new Boutique(player));
		if(Global.checkFlag(Flag.Reyka)){
			activities.add(new ReykaTime(player));
		}
		activities.add(new MagicTraining(player));
		activities.add(new Workshop(player));
	}
	public String getTime(){
		return time+":00";
	}
	public void advance(int t){
		time+=t;
	}
	public static void train(Character one, Character two, Attribute att){
		int a;
		int b;
		if(one.getPure(att)>two.getPure(att)){
			a=100-(2*one.get(Attribute.Perception));
			b=90-(2*two.get(Attribute.Perception));
		}
		else if(one.getPure(att)<two.getPure(att)){
			a=90-(2*one.get(Attribute.Perception));
			b=100-(2*two.get(Attribute.Perception));
		}
		else{
			a=100-(2*one.get(Attribute.Perception));
			b=100-(2*two.get(Attribute.Perception));
		}
		if(Global.random(100)>=a){
			one.mod(att, 1);
			if(one.human()){
				Global.gui().message("<b>Your "+att+" has improved.</b>");
			}
		}
		if(Global.random(100)>=b){
			two.mod(att, 1);
			if(two.human()){
				Global.gui().message("<b>Your "+att+" has improved.</b>");
			}
		}
	}
	public void visit(String name, Character npc, int budget){
		for(Activity a:activities){
			if(a.toString().equalsIgnoreCase(name)){
				a.shop( npc, budget );
				break;
			}
		}
	}
}
