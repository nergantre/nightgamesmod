package nightgames.daytime;

import java.util.ArrayList;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.characters.Player;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.status.addiction.Addiction;

public class Daytime {
    private ArrayList<Activity> activities;
    private Player player;
    private int time;
    private int daylength;
    private DaytimeEventManager eventMgr;

    public Daytime(Player player) {
        this.player = player;
        this.eventMgr = new DaytimeEventManager(player);
        buildActivities();
        if (Global.checkFlag(Flag.metAlice)) {
            if (Global.checkFlag(Flag.victory)) {
                Global.unflag(Flag.AliceAvailable);
            } else {
                Global.flag(Flag.AliceAvailable);
            }
        }
        if (Global.checkFlag(Flag.YuiLoyalty)) {
            Global.flag(Flag.YuiAvailable);
        }
        if (Global.checkFlag(Flag.YuiUnlocking)) {
            Global.unflag(Flag.YuiUnlocking);
        }
        
        Global.unflag(Flag.threesome);
        time = 10;
        // do NPC day length
        if (Global.getDate() % 7 == 6 || Global.getDate() % 7 == 0) {
            daylength = 10;
        } else {
            daylength = 7;
        }
    }

    private boolean morning() {
        Global.gui()
              .clearText();
        Global.getPlayer().getAddictions().forEach(Addiction::clearDaytime);
        Global.getPlayer().getAddictions().stream().map(a -> a.describeMorning()).forEach(s -> Global.gui().message(s));
        if (eventMgr.playMorningScene()) {
            time = 12;
            return true;
        } else if (player.getLevel() >= 10 && player.getRank() == 0) {
            Global.gui()
                  .message("The next day, just after getting out of class you receive call from a restricted number. Normally you'd just ignore it, "
                                  + "but for some reason you feel compelled to answer this one. You're greeted by a man with a clear deep voice. <i>\"Hello "
                                  + player.name() + ", and "
                                  + "congratulations. Your performance in your most recent matches has convinced me you're ready for a higher level of play. You're promoted to "
                                  + "ranked status, effective immediately. This new status earns you a major increase in monetary rewards and many new opportunities. I'll leave "
                                  + "the details to others. I just wanted to congratulate you personally.\"</i> Wait, wait. That's not the end of the call. This guy is clearly "
                                  + "someone overseeing the Game, but he hasn't even given you his name. Why all the secrecy? <i>\"If you're looking for more information, you "
                                  + "know someone who sells it.\"</i> There's a click and the call ends.");
            player.rankup();
        } else if (player.getLevel() >= 20 && player.getRank() == 1) {
            Global.gui()
                  .message("In the morning, you receive a call from a restricted number. You have a pretty decent guess who it might be. Hopefully it is good news. <i>\"Hello again "
                                  + player.name()
                                  + ".\"</i> You were right, that voice is pretty hard to forget. <i>\"I am impressed. You and your opponents are "
                                  + "all quickly adapting to what most people would consider an extraordinary situation. If you are taking advantage of the people and services available "
                                  + "to you, you could probably use more money. Therefore, I am authorizing another pay increase. Congratulations.\"</i> This is the mysterious Benefactor "
                                  + "everyone keeps referring to, right? Is he ever planning to show himself in person? What is he getting out of all this? <i>\"Your curiosity is admirable. "
                                  + "Keep searching. If you have as much potential as I think you do, we'll meet soon enough.\"</i>");
            player.rankup();
            time = 15;
        } else if (player.getLevel() >= 30 && player.getRank() == 2) {
            Global.gui()
                  .message("In the morning, you receive a call from a restricted number. You are not at all surprised to hear the voice of your anonymous Benefactor again. It did seem about time for him to call again. <i>\"Hello "
                                  + player.name() + ". Have you been keeping busy? You've been putting "
                                  + "on a good show in your matches, but when we last spoke, you had many questions. Are you any closer to finding your answers?\"</i><p>"
                                  + "That's an odd question since it depends on whether or not he has become more willing to talk. Who else is going to fill you in about this "
                                  + "apparently clandestine organization. <i>\"Oh don't become lazy now. I chose you for this Game, in part, for your drive and initiative. Are "
                                  + "you limited to just the information that has been handed to you? Just because Aesop does not have the answers for sale does not mean there "
                                  + "are no clues. Will you simply give up?\"</i><p>"
                                  + "You know he's trying to provoke you, but it's working anyway. If he's offering a challenge, you'll show him you can track him down. The next "
                                  + "time you speak to this Benefactor, it will be in person. <i>\"Excellent!\"</i> His voice has only a trace of mockery in it. <i>\"You are "
                                  + "already justifying your new rank, which is what I am calling you about, incidently. Perhaps you can put your increased pay rate or the trust "
                                  + "you've built with your opponents to good use. Well then, I shall wait to hear from you this time.\"</i> There's a click and the call ends.");
            player.rankup();
            time = 15;
        } else if (player.getLevel() / 10 > player.getRank()) {
            Global.gui().message("You have advanced to rank " + ++player.rank + "!");
            time = 15;
        } else if (Global.getDate() % 7 == 6 || Global.getDate() % 7 == 0) {
            Global.gui()
                  .message("You don't have any classes today, but you try to get up at a reasonable hour so you can make full use of your weekend.");
            time = 12;
        } else {
            Global.gui()
                  .message("You try to get as much sleep as you can before your morning classes.<p>You're done with classes by mid-afternoon and have the rest of the day free.");
            time = 15;
        }
        return false;
    }

    public void plan() {
        boolean special = false;
        if (time == 10) {
            special = morning();
            if (special) {
                return;
            }
        }
        if (time < 22) {
            Global.gui()
                  .message("It is currently " + displayTime() + ". Your next match starts at 10:00pm.");
            Global.gui()
                  .refresh();
            Global.gui()
                  .clearCommand();
            if (eventMgr.playRegularScene())
                return;
            for (Activity act : activities) {
                if (act.known() && act.time() + time <= 22) {
                    Global.gui()
                          .addActivity(act);
                }
            }
        } else {
            for (Character npc : Global.everyone()) {
                if (!npc.human() && npc instanceof NPC) {
                    if (npc.getLevel() / 10 > npc.getRank()) {
                        npc.rankup();
                    }
                    ((NPC) npc).daytime(daylength);
                }
            }
            Global.endDay();

            /*
            if (Global.checkFlag(Flag.autosave)) {
                Global.autoSave();
            }
            Global.decideMatchType()
                  .buildPrematch(player);
            */
        }
    }

    public void buildActivities() {
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
        activities.add(new AiriTime(player));
        activities.add(new CassieTime(player));
        activities.add(new JewelTime(player));
        activities.add(new MaraTime(player));
        if (Global.checkFlag(Flag.Kat)) {
            activities.add(new KatTime(player));
        }
        activities.add(new Closet(player));
        activities.add(new ClothingStore(player));
        activities.add(new Boutique(player));
        if (Global.checkFlag(Flag.Reyka)) {
            activities.add(new ReykaTime(player));
        }
        if (Global.checkFlag(Flag.YuiLoyalty)) {
            activities.add(new YuiTime(player));
        }
        activities.add(new MagicTraining(player));
        activities.add(new Workshop(player));
        activities.add(new AddictionRemoval(player));
    }

    public String getTime() {
        return time + ":00";
    }

    public void advance(int t) {
        time += t;
        buildActivities();
    }

    public static void train(Character one, Character two, Attribute att) {
        int a;
        int b;
        if (one.getPure(att) > two.getPure(att)) {
            a = 100 - 2 * one.get(Attribute.Perception);
            b = 90 - 2 * two.get(Attribute.Perception);
        } else if (one.getPure(att) < two.getPure(att)) {
            a = 90 - 2 * one.get(Attribute.Perception);
            b = 100 - 2 * two.get(Attribute.Perception);
        } else {
            a = 100 - 2 * one.get(Attribute.Perception);
            b = 100 - 2 * two.get(Attribute.Perception);
        }
        if (Global.random(100) >= a) {
            one.mod(att, 1);
            if (one.human()) {
                Global.gui()
                      .message("<b>Your " + att + " has improved.</b>");
            }
        }
        if (Global.random(100) >= b) {
            two.mod(att, 1);
            if (two.human()) {
                Global.gui()
                      .message("<b>Your " + att + " has improved.</b>");
            }
        }
    }

    public void visit(String name, Character npc, int budget) {
        for (Activity a : activities) {
            if (a.toString()
                 .equalsIgnoreCase(name)) {
                a.shop(npc, budget);
                break;
            }
        }
    }

    private String displayTime() {
        if (time < 12) {
            return time + ":00am";
        }
        if (time == 12) {
            return "noon";
        }

        return time - 12 + ":00pm";
    }
}
