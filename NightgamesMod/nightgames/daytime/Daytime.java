package nightgames.daytime;

import java.util.ArrayList;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.characters.Player;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.status.MagicMilkAddiction;

public class Daytime {
    private ArrayList<Activity> activities;
    private Player player;
    private int time;
    private Threesomes threesome;
    private int daylength;

    public Daytime(Player player) {
        this.player = player;
        buildActivities();
        if (Global.checkFlag(Flag.metAlice)) {
            if (Global.checkFlag(Flag.victory)) {
                Global.unflag(Flag.AliceAvailable);
            } else {
                Global.flag(Flag.AliceAvailable);
            }
        }
        Global.unflag(Flag.threesome);
        threesome = new Threesomes(player);
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
        player.setFlag(MagicMilkAddiction.MAGICMILK_DRANK_DAYTIME_FLAG, 0);
        if (MagicMilkAddiction.getMagicMilkAddictionLevel(player) >= 5) {
            Global.gui()
                  .message("You wake up in the morning with a burning need for Cassie's milk. The thought of resisting the urge doesn't even enter your mind. You quickly whip out your cellphone and dial Cassie's number. "
                                  + "Moments later, an amused voice answers. You sob into the phone, begging for Cassie's milk. Luckily, Cassie doesn't seem to want to tease you today, and readily agrees to drop by. "
                                  + "Fifteen agonizing minutes later, the doorbell rings and you rush to answer. Giving you a quick and dirty kiss at the door way, Cassie enters your room and sits down on your bed. "
                                  + "She pats her lap and motions for you to strip and lie down. You quickly comply and lay in her lap facing the ceiling, giddy for more milk. <br>"
                                  + "With a coying grin, Cassie strips off her top and lets her bountiful breasts bounce free of her bra. Your eyes immediately zeroes into her nipples, already dripping with opalescent white fluids. "
                                  + "Cassie lowers her breasts into your face, and you happily start drinking her mindbending milk. Seconds turn into minutes and minutes turn into hours. "
                                  + "You don't know how long your were nursing at her teats, but you seemed to have dozed off in the middle of it. You find yourself on the bed by yourself, with a blanket covering you. "
                                  + "Cassie has already left, but left a note on the kitchen table, <br><i>Hey hun, unfortunately I have to get to class. I made you some lunch that I put in the fridge, and left you a bottle of milk in case the cravings come back. I'll see you tonight at the games okay? Love you baby.</i><br><br>");
            player.setFlag(MagicMilkAddiction.MAGICMILK_DRANK_DAYTIME_FLAG, 1);
            player.setFlag(MagicMilkAddiction.MAGICMILK_ADDICTION_FLAG,
                            player.getFlag(MagicMilkAddiction.MAGICMILK_ADDICTION_FLAG) + 2);
        } else if (MagicMilkAddiction.getMagicMilkAddictionLevel(player) >= 4) {
            Global.gui()
                  .message("When you wake up in the morning, the first thing you think of is Cassie's breasts. And the second. And the third. In fact, you realize that's all you can think of right now. "
                                  + "You sigh and attempt to take a cold shower to tear your mind from her sinfully sweet milk. Unfortunately, it does you little good. You will have to make a choice between toughing it out, or caving and calling Cassie for a helping of her addictive cream.<br><br>");
        } else if (MagicMilkAddiction.getMagicMilkAddictionLevel(player) >= 3) {
            Global.gui()
                  .message("You wake up in the morning with damp underwear. You realize that you've been dreaming of Cassie's milk the entire night. This can't be healthy... <br>"
                                  + "You want to immediately head over to Cassie's and ask for another helping, but quickly realize that will just feed the addiction. "
                                  + "However, at this rate, you will be thinking of her the entire day, and affect your willpower. You will have to make a decision to tough it out or call her up and ask for more.<br><br>");
        } else if (MagicMilkAddiction.getMagicMilkAddictionLevel(player) >= 2) {
            Global.gui()
                  .message("You wake up in the morning with your throat feeling strangely parched. You step into the kitchen and take out a carton of milk to attempt to slake your thirst. "
                                  + "Five minutes and a empty carton later, you still don't feel much better. You decide to ignore it and head to class.<br><br>");
        }
        if (player.getLevel() >= 10 && player.getRank() == 0) {
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

    public void plan(boolean headless) {
        String threescene;
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
            if (!Global.checkFlag(Flag.threesome)) {
                threescene = threesome.getScene();
                if (!threescene.equals("")) {
                    threesome.visit(threescene);
                    return;
                }
            }
            for (Activity act : activities) {
                if (act.known() && act.time() + time <= 22) {
                    Global.gui()
                          .addActivity(act);
                }
            }
        } else {
            for (Character npc : Global.everyone()) {
                if (!npc.human()) {
                    if (npc.getLevel() >= 10 && npc.getRank() == 0 || npc.getLevel() >= 20 && npc.getRank() == 1) {
                        npc.rankup();
                    }
                    ((NPC) npc).daytime(daylength);
                }
            }
            int magicmilk_addiction = player.getFlag(MagicMilkAddiction.MAGICMILK_ADDICTION_FLAG);
            player.getWillpower()
                  .setTemporaryMax(-1);
            if (player.getFlag(MagicMilkAddiction.MAGICMILK_DRANK_DAYTIME_FLAG) != 0) {
                player.setFlag(MagicMilkAddiction.MAGICMILK_DRANK_DAYTIME_FLAG, 0);
            } else if (magicmilk_addiction > 0) {
                player.getWillpower()
                      .setTemporaryMax(player.getWillpower()
                                             .max()
                                      / MagicMilkAddiction.getMagicMilkAddictionLevel(player));
                player.setFlag(MagicMilkAddiction.MAGICMILK_ADDICTION_FLAG, Math.max(0, magicmilk_addiction - 3));
                Global.gui()
                      .message("You're in withdraw from abstaining from drinking Cassie's milk. You will have lowered willpower in tonight's match...");
            }
            if (!headless) {
                // Global.gui().nextMatch();
                if (Global.checkFlag(Flag.autosave)) {
                    Global.save(true);
                }
                Global.decideMatchType()
                      .buildPrematch(player);
            }
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
        activities.add(new MagicTraining(player));
        activities.add(new Workshop(player));
    }

    public String getTime() {
        return time + ":00";
    }

    public void advance(int t) {
        time += t;
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
