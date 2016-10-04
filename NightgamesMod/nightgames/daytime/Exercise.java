package nightgames.daytime;

import java.util.ArrayList;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.global.Flag;
import nightgames.global.Global;

public class Exercise extends Activity {

    public Exercise(Character player) {
        super("Exercise", player);
    }

    @Override
    public boolean known() {
        return Global.checkFlag(Flag.metBroker);
    }

    @Override
    public void visit(String choice) {
        Global.gui().clearText();
        if (page == 0) {
            Global.gui().next(this);
            int gain = Global.random(3) + 3;
            if (player.has(Trait.fitnessNut)) {
                gain *= 2;
            }
            showScene(pickScene(gain));
            player.getStamina().gain(gain);
            Global.gui().message("<b>Your maximum stamina has increased by " + gain + ".</b>");
        } else {
            done(true);
        }
    }

    @Override
    public void shop(Character npc, int budget) {
        npc.getStamina().gain(Global.random(3) + 1);
    }

    private void showScene(Scene chosen) {
        switch (chosen) {
            case basic1:
                Global.gui().message(
                                "You're about halfway through your jog when a sudden downpour leaves you completely soaked. You squelch your way back to the dorm, looking like a drowned rat.");
                break;
            case basic2:
                Global.gui().message("You head to the campus gym and spend some time in a variety of exercises.");
                break;
            case basic3:
                Global.gui().message(
                                "You decide to take a brief jog around campus to improve your strength and stamina.");
                break;
            case fail1:
                Global.gui().message(
                                "Maybe you didn't stretch well enough before you started, but a few minutes into your run you feel like you've pulled a muscle in your leg. Better take a break rather than injure yourself before the match.");
                break;
            case cassie1:
                Global.gui().message(
                                "You head over to the campus gym and coincidentally run into Cassie there. <i>\"Hi. I'm not really much of a fitness enthusiast, but I need to get into better shape if I'm going to stay competitive.\"</i><br>The two of you spend some time doing light exercise and chatting.");

                Global.getNPC("Cassie").gainAffection(player, 1);
                player.gainAffection(Global.getNPC("Cassie"), 1);
                break;
            case jewel1:
                Global.gui().message(
                                "You're going for a run around the campus and run into Jewel doing the same. She makes an immediate beeline towards you. <i>\"You're not getting out of running today, no matter how tempting the alternative is. We're going to get some real exercise.\"</i> She pushes you a lot harder than you had planned and you're exhausted by the end of it, but you did manage to keep up with her.");

                Global.getNPC("Jewel").gainAffection(player, 1);
                player.gainAffection(Global.getNPC("Jewel"), 1);
                break;
            case yuiintro1:
                Global.gui().message("For a change of pace, you decide to try a different jogging route today that takes you outside the campus. There's less foot traffic to worry about here, "
                        + "which gives you more opportunity to just take in your surroundings. It's a fairly nice area. There are lots of small shops around, but at this time of day, "
                        + "it's pretty quiet... at first.<p>"
                        + "You notice raised voices and, despite your better judgement, you take a detour to check it out. You notice a guy in his early twenties arguing with "
                        + "a girl around your age. He is getting pretty heated and menacing, while she seems to be trying to extract herself from the situation. He's at "
                        + "least a head taller than her, clearly not a fair fight. You decide to intervene before things turn violent.<p>"
                        + "<i>\"Who the fuck are you?\"</i> The man accosts you as you approach. The girl moves to stand behind you, using you as a shield. <i>\"Are you with her? "
                        + "If not, fuck off! This is between me and her.\"</i><p>"
                        + "You were hoping to de-escalate the fight, not play hero. Well... maybe that's not entirely true. Regardless, you clearly can't abandon the girl now. You tell "
                        + "the guy he should leave, but he throws a punch before you can even finish the sentence. Fortunately, it's such a slow attack, you can easily dodge it. You shove "
                        + "him back and he falls to the ground. Wow, he's actually pretty weak... maybe. Before you joined the Games, that punch might have floored you. He's definitely not "
                        + "on Jewel's level.<p>"
                        + "Your attacker picks himself off the ground, looking pissed. Then you see a flash of fear in his eyes. Apparently he realizes he's outmatched. <i>\"Fine. Fourty "
                        + "bucks isn't worth this trouble. Enjoy my money you bitch!\"</i> He takes off in a hurry.<p>"
                        + "As you turn around, you think you see the girl slips something metal into her pocket. She gives you a bright smile of gratitude. She's quite cute, though her "
                        + "long bangs make it hard to see her face. Her blue eyes, however, are clearly visible and striking. Still, you have to ask what that argument was about.<p>"
                        + "<i>\"It was a major overreaction to a simple misunderstanding. Thank you for your help.\"</i> She sounds relieved. She brushes her bangs to the side, giving "
                        + "you a better look at her lovely face. <i>\"I just moved here and I wasn't sure what to expect from the local culture. That man gave a bit of a poor first impression, "
                        + "but I'm glad to see there are also kind men like you.\"</i><p>"
                        + "She gives you a little bow before she walks away. After she's gone, you kinda regret not at getting at least her name. Oh well, maybe you'll run into her again.");
                Global.flag(Flag.metYui);
                Global.flag(Flag.YuiUnlocking);
                Global.setCounter(Flag.YuiAffection, 0);
                break;
            case yuiintro2:
                Global.gui().message("As you jog around the campus, you stumble onto a wallet sitting on the ground. It's right next to a bench, so it probably fell out of the owner's pocket "
                        + "when he or she stood up. There's no one nearby who might have dropped it, but you're not "
                        + "particularly busy right now. Maybe you can figure out who the owner is and return it.<p>"
                        + "You look inside the wallet, which doesn't contain much. There's some cash, but you're not really tempted to take it. You're making plenty off the Games. There's a "
                        + "student ID with a name: 'Yui Ishida'. Very Japanese. No photo on the ID though. Nothing in here has a phone number or address, so the odds of finding the owner on "
                        + "your own are pretty slim. Since you know she's a student, it makes the most sense to just turn the wallet in to the campus Lost-and-Found.<p>"
                        + "You head over to the Student Union, which contains the Lost-and-Found. When you arrive, you notice a familiar girl with long blonde bangs. <i>\"Oh! You're the good "
                        + "samaritan who saved me from that thug.\"</i> She gives you a delighted smile as she recognizes you. <i>\"I should have guessed that you were a student here too.\"</i> "
                        + "You are briefly distracted from your original purpose by this second chance to get this cute girl's name. You introduce yourself with a friendly smile. <i>\"It's nice "
                        + "to meet you again. I missed the beginning of the semester because of a family thing, so I don't know anyone here yet. My name is Yui Ishida. I hope we can be friends.\"</i><p>"
                        + "That's an unexpected coincidence. You're not exactly an expert on ethnicity, but this girl doesn't look the slightest bit Asain. Still, by sheer luck, you've managed to "
                        + "locate the wallet's owner. She practically jumps for joy when you return it. <i>\"My wallet! I came here hoping someone had found it.\"</i> She puts it in her pocket, too "
                        + "polite to check the contents in front of you. <i>\"This is the second time you've helped me. You must be my guardian angel.\"</i> You're a little embarrassed by her "
                        + "sincerity, but you're glad to be of assistance. If she just transferred in, maybe you can show her around the campus.<p>"
                        + "She looks conflicted for a moment, but shakes her head regretfully. <i>\"I'm very tempted to take you up on that offer, but I've already had the full tour and I can't "
                        + "trouble you needlessly after you've helped me so much. Besides, now that I have my ID back, I need to finish my registration.\"</i> She gives you a small, Japanese-style "
                        + "bow, but seems hesistant to leave right away. <i>\"This is the first time I've really regretted not owning a cell phone. If I ever buy one, I promise I'll give you my "
                        + "number.\"</i> She smiles brightly. <i>\"We've had two serendipitous meetings in such a short period of time. I'm sure we'll meet again soon.\"</i>");
                Global.modCounter(Flag.YuiAffection, 2);
                Global.flag(Flag.YuiUnlocking);
                break;
            case yuiintro3:
                Global.gui().message("You take a jog around the campus. You find yourself thinking about the girl you encountered on a couple previous jogs, Yui. You haven't had any way to contact "
                        + "her other than hoping to run into her by chance. Does she really not own a cell phone? She didn't seem to be lying. Aesop, being an information broker, could probably find "
                        + "out more about her, but it feels wrong to ask him to investigate someone unrelated to the Games just to satisfy your curiosity. That would be like hiring a private detective "
                        + "to look into a girl you barely know. Oh well, maybe you'll get lucky and run into her again.<p>"
                        + "Your luck proves remarkable a few minutes later. You spot Yui sitting alone on a bench. She jumps up with an excited grin when she sees you. <i>\""+player.name()+"! I was "
                        + "hoping to see you again. This really must be fate!\"</i> She blushes a bit at her own overexcitement and regains her self-control. She seems quite nervous. <i>\"Do you have "
                        + "some time to talk? I have something important I want to tell you... and something to show you.\"</i> She sounds as nervous as she looks. Whatever she wants to talk about, "
                        + "she's obviously worried how you'll react. The rest of your workout can wait.<p>"
                        + "<i>\"Thanks. Can you come with me? It's a bit of a walk.\"</i><p>"
                        + "You follow her in silence for several minutes. She's clearly trying to figure out how to start. <i>\"Um... My family is a bit strange. It's a very old Japanese family with traditions "
                        + "dating back before the Edo period. Uh... about 400 years, if you're not big on Japanese history. My ancestors served various lords as... spies and assassins mostly.\"</i> "
                        + "She's trying really hard not to use the word 'ninja'. <i>\"Yes I am, even though it's super accurate.\"</i><p>"
                        + "She lapses into awkward silence again. She is very Caucasian for a traditional Japanese ninja. Still, you've been part of the Games too long to find anything beyond belief. "
                        + "You do your best to reassure her. She smiles at you nervously and continues.<p>"
                        + "<i>\"It's been a very long time since my family has had a lord to serve, but we still maintain the old arts for the sake of tradition. I've been trained in various covert "
                        + "techniques since I was twelve. It's not really practical for normal life, but I'm good at tailing someone without being seen, or picking a lock... or a pocket.\"</i> She trails "
                        + "off a bit at the end. Pickpocketing raises a red flag in your memory. She flinches visibly when you bring it up.<p>"
                        + "<i>\"Yeah... It's just for practice, of course. I return whatever I grabbed, pretending the person dropped it... as long as they don't catch me.\"</i> She looks at you "
                        + "sheepishly. <i>\"Thanks again for helping me with that guy. I wasn't sure how I was going to get out of that without hurting him. He didn't deserve that, even if he did call me a "
                        + "bitch... three times...\"</i><p>"
                        + "The awkward silence returns as you reach what appears to be an old shed. It looks old and abandoned. In this isolated corner of the campus, you'd probably never have known "
                        + "it existed. Yui pushes open the old door, which isn't locked. <i>\"This was apparently used for storage, but it hasn't been touched for years. I don't want to freak out my new "
                        + "roommate with my equipment and training, so I'm borrowing it.\"</i><p>"
                        + "You walk inside the old, rickety building. Yui has clearly made an attempt to clean up the interior. It doesn't look half bad in here. There's a workbench with assorted "
                        + "tools and chemicals, as well as a training dummy and some climbing equipment. This meets your basic expectations for a ninja hideout. <i>\"I haven't told anyone else about "
                        + "this place, but I want you to know about it.\"</i><p>"
                        + "She kneels on the floor of the shed and bows deeply. <i>\"Please allow me to serve as your ninja. Generations of my family have been masterless, but I don't believe it is a "
                        + "coincidence that we keep meeting. In the brief minutes we spent together, you displayed honor, bravery, and compassion. You are exactly the sort of person my ancestors would "
                        + "have pledged their loyalty to.\"</i><p>"
                        + "You're completely taken aback by her offer. What would you even do with a ninja? You don't need a bodyguard or an assassin. It's hard to turn down someone asking so earnestly, "
                        + "but you can't seriously accept either. Yui looks like she may start crying any moment, clearly she's anticipating rejection already.<br>"
                        + "Suddenly, an idea strikes you. If she really wants to help you, maybe she can do so indirectly. Obviously having her help you in the Games would be cheating, but maybe she "
                        + "can teach you some of her skills.<p>"
                        + "Yui's face lights up at your suggestion. <i>\"Of course, Master! I'll teach you everything I know!\"</i> She abruptly turns red and looks away. <i>\"Almost everything.... "
                        + "Some of the kunoichi techniques I read about... I don't think I could teach those to a guy....\"</i> She quickly recovers and changes the subject. <i>\"I'll also handle getting "
                        + "some equipment. I can scavenge a bit that no one will miss. I won't even ask what you're going to do with it, Master.\"</i><p>"
                        + "Well, if she's happy that's the most important thing. You feel kinda bad that she's devoting herself to helping you without anything in return, but clearly that's what she wants. "
                        + "If her lessons are any good, it'll benefit you in the Games. If not, you can at least keep her company. The only remaining issue is, does she really have to call you Master? "
                        + "People will assume you're in a kinky relationship.<p>"
                        + "Yui blushes again, but looks determined. <i>\"Please let me call you Master. It's really important to me to show you my commitment. I'll... try not to say it in front of "
                        + "other people.\"</i> Her expression is too sincere to turn down. Fine, she can call you whatever she wants." );
                Global.modCounter(Flag.YuiAffection, 2);
                Global.flag(Flag.YuiLoyalty);
                break;
        }
    }

    private Scene pickScene(int gain) {
        ArrayList<Scene> available = new ArrayList<Scene>();
        if(player.getRank()>=1&&!Global.checkFlag(Flag.metYui)){
            available.add(Scene.yuiintro1);
        }
        if(Global.checkFlag(Flag.metYui)&&Global.getValue(Flag.YuiAffection)<1&&!Global.checkFlag(Flag.YuiUnlocking)){
            available.add(Scene.yuiintro2);
        }
        if(Global.getValue(Flag.YuiAffection)>1&&!Global.checkFlag(Flag.YuiLoyalty)&&!Global.checkFlag(Flag.YuiUnlocking)){
            available.add(Scene.yuiintro3);
        }
        if (gain == 1) {
            available.add(Scene.fail1);
        } else {
            available.add(Scene.basic1);
            available.add(Scene.basic2);
            available.add(Scene.basic3);
            if (Global.getNPC("Cassie").getAffection(player) >= 5) {
                available.add(Scene.cassie1);
            }
            if (Global.getNPC("Jewel").getAffection(player) >= 5 && player.getStamina().max() >= 35) {
                available.add(Scene.jewel1);
            }
        }
        return available.get(Global.random(available.size()));
    }

    private static enum Scene {
        basic1,
        basic2,
        basic3,
        cassie1,
        jewel1,
        fail1,
        yuiintro1,
        yuiintro2,
        yuiintro3;
    }
}
