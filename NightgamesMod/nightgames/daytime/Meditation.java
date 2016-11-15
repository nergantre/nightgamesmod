package nightgames.daytime;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;

public class Meditation extends Activity {
    private boolean acted;

    public Meditation(Character player) {
        super("Meditate", player);
        acted = false;
    }

    @Override
    public boolean known() {
        return Global.checkFlag(Flag.meditation);
    }

    @Override
    public void visit(String choice) {
        if (choice.equals("Start")) {
            acted = false;
        }
        Global.gui().clearText();
        Global.gui().clearCommand();
        if (Global.checkFlag(Flag.dojo) && choice.equals("Start")) {
            if (!Global.checkFlag(Flag.metSuzume)) {
                Global.gui().message(
                                "You go to the Suzuki dojo to meet Suzume. She looks like she's trying to looks like project the air of a dignified martial arts master, but "
                                                + "you can tell she's excited at the prospect of getting a student. <i>\"Aesop tells me you're interested in becoming my apprentice. Sorry I didn't offer to "
                                                + "train you earlier, but Aesop was very insistent that I wait.\"</i> She walks past you and locks the dojo door. <i>\"We have some privacy, so take off your clothes "
                                                + "and let me take a look at you.\"</i><p>You strip off your clothes, slightly embarrassed about the way she's staring at you. She runs her hands lightly over your "
                                                + "arms, chest, and back. <i>\"Good musculature. I think you have some of potential.\"</i> She's trying to sound business-like, but there's a noticeable blush on her "
                                                + "cheeks. You're also affected by her touch and your dick gradually becomes erect. Suzume looks down at your manhood and smiles. <i>\"Maybe a lot of potential... "
                                                + "Ok, you can get dressed now, unless you need a hand.\"</i> You quickly put your pants back on and cover up your erection. Normal social standards may be more "
                                                + "flexible here, but it's still probably not a good idea to request a handjob when you're already asking for training.<p><i>\"I'd be happy to have you as a student, "
                                                + "but I can't train you for free. I don't charge for guided meditation because it's not a traditional dojo service, but you'll need to pay for each training "
                                                + "session.\"</i> Her expression darkens as she bites her lip. <i>\"My father is not in good health and he can't take students. I need money to keep the dojo open, and "
                                                + "I know you're making plenty in the games.\"</i>");
                Global.flag(Flag.metSuzume);
                Global.gui().choose(this, "Train: $" + 1000 * (player.get(Attribute.Ki) + 1));
                Global.gui().choose(this, "Sharpen Senses");
                Global.gui().choose(this, "Shut Out Sensation");
                Global.gui().choose(this, "Leave");
            } else {
                Global.gui().message(
                                "You go to the Suzuki dojo and remove your shoes out of respect. Suzume (or Suzuki-shisho as she's instructed you to call her) give you a friendly "
                                                + "smile as you bow. <i>\"Welcome apprentice. Are you ready to continue your training or are you here to meditate?\"</i>");
                Global.gui().choose(this, "Train: $" + 1000 * (player.get(Attribute.Ki) + 1));
                Global.gui().choose(this, "Sharpen Senses");
                Global.gui().choose(this, "Shut Out Sensation");
                Global.gui().choose(this, "Leave");
            }
        } else if (choice.equals("Start")) {
            Global.gui().message(
                            "You contact Suzume and schedule a private meditation session. Dropping Aesop's name saved a lot of time; she didn't ask many questions. Apparently "
                                            + "he refers people to her pretty regularly. The two of you meet at a martial arts dojo that her family runs, but there's no one else there currently. Suzume herself is "
                                            + "a thin asian girl with short brown hair and a subtle japanese accent. She looks you over with a pleased expression. <i>\"If Tyler sent you to me, I'm guessing you want "
                                            + "me to train you to either be more alert, or to improve your endurance in... private activities.\"</i> You caught a glint of mischief in her eye, but she quickly covers it up. "
                                            + "<i>\"Now I need to be clear that hypnotic suggestion is not an exact science and there's no guarantee there will be any noticeable change in your sensitivity. If we are "
                                            + "successful, there will still be a drawback. Making yourself less sensitive can also cause you to miss fine details, and making yourself more perceptive can turn you into "
                                            + "a quick shot in bed. So knowing all that, do you still want to go through with this?\"</i>");
            Global.gui().choose(this, "Sharpen Senses");
            Global.gui().choose(this, "Shut Out Sensation");
            Global.gui().choose(this, "Leave");
        } else if (choice.equals("Leave")) {
            done(acted);
        } else if (choice.startsWith("Train")) {
            if (player.money >= 1000 * (player.getPure(Attribute.Ki) + 1)) {
                Global.gui().message(
                                "Suzuki-shisho insists that you train in the nude. She claims it's a tribute to an old Japanese grappling tradition. You're about 95% certain she's "
                                                + "lying. Fortunately the dojo doesn't appear to have any other students, so the two of you have plenty of privacy. It's also closer to the circumstances you're "
                                                + "normally fighting in.<p><i>\"Your Ki skills can be very useful, but be careful to pace yourself or you may run out of stamina.\"</i>");
                player.money -= 1000 * (player.getPure(Attribute.Ki) + 1);
                player.mod(Attribute.Ki, 1);
                acted = true;
                if (!player.has(Clothing.getByID("gi"))) {
                    player.gain(Clothing.getByID("gi"));
                }
                Global.gui().choose(this, "Leave");
            } else {
                Global.gui().message("You don't have enough money for training.");
                Global.gui().choose(this, "Sharpen Senses");
                Global.gui().choose(this, "Shut Out Sensation");
                Global.gui().choose(this, "Leave");
            }
        } else if (choice.equals("Sharpen Senses")) {
            if (Global.random(100) >= 50) {
                Global.gui().message(
                                "Suzume instructs you to sit in the middle of the dojo and close your eyes. <i>\"I'm going to count down from ten. With each number, you will feel your "
                                                + "mind openning and you will be more receptive to my words. When I reach zero, imagine your mind as an empty vessel, ready to take in everything around you.\"</i> As she "
                                                + "counts down, you can feel yourself slipping into a trance. You know you could pull back at any time, but that would defeat the entire purpose of this, so instead you "
                                                + "let yourself go. <i>\"I want you to imagine a thick fog has surrounded you your entire life, shrouding the world and dulling your senses. When I snap my fingers now, the fog "
                                                + "will clear and your senses will be sharper than ever before. You'll be able to see details you never would have been able to make out. You'll be able to hear a pin drop "
                                                + "in a storm. The entire universe will rush in through your skin and you'll perceive it all.\"</i><p>Suzume snaps her fingers and instantly everything is different. Your eyes "
                                                + "are still closed, but you feel like you can perceive the shape of the room around you. Suzume has been pacing behind you, but her footfalls were too light for you to notice. "
                                                + "Now you can pinpoint her exact location and movement. You hear a rustle of cloth from her direction and realize she's taking off her socks. <i>\"We're going to give your heightened "
                                                + "senses a little practice so you can adapt to them. Keep your eyes closed, but you can speak now. What am I doing?\"</i> When you reply that she's removing socks, you can somehow "
                                                + "sense her smile. <i>\"Very good, what about now?\"</i> Another rustle; shirt and pants this time. Then bra. Finally you hear the whisper of her panties sliding down her legs. Even "
                                                + "though you can't see her, you can perceive every move she makes as if she were performing a strip show. Your erection strains against your pants as you hear her nude form approach "
                                                + "you. When she gets close, you can feel the warmth of her body heat and catch the faint scent of feminine arousal.<p><i>\"I want you to wake up completely, but keep your eyes forward. "
                                                + "I'm a modest girl after all. You probably feel a bit like a superhero right now, but I'm going to demonstrate your new vulnerability.\"</i> She takes hold of your hand and caresses "
                                                + "your palm, causing you to jump at the unexpected sensation. <i>\"Your sense of touch is turned up so high right now that your entire body is an erogenous zone. Any place I touch will "
                                                + "feel the same as the head of your penis.\"</i> She brings your hand to her mouth and kisses the tip of your index finger. You can't help groaning as the pleasure is transmitted throughout "
                                                + "your body. When she puts your finger in her mouth and begins to lick it, it does feel like she's giving you a blowjob. You manage to endure her 'fingerjob' for about thirty seconds "
                                                + "before you hit your peak and cum in your pants. Suzume giggles when she realizes what happened. <i>\"I probably should have warned you to bring a change of underwear with you. Don't worry, "
                                                + "your hypersensitivity will level out over the next hour or so. You'll be a bit more perceptive, but not like you are now.\"</i>");
                if (player.getPure(Attribute.Perception) < 9) {
                    player.mod(Attribute.Perception, 1);
                }
            } else {
                Global.gui().message(
                                "Suzume instructs you to sit in the middle of the dojo and close your eyes. <i>\"I'm going to count down from ten. With each number, you will feel your "
                                                + "mind opening and you will be more receptive to my words. When I reach zero, imagine your mind as an empty vessel, ready to take in everything around you.\"</i> As she "
                                                + "counts down, you can feel yourself slipping into a trance. You know you could pull back at any time, but that would defeat the entire purpose of this, so instead you "
                                                + "let yourself go. <i>\"I want you to imagine a thick fog has surrounded you your entire life, shrouding the world and dulling your senses. When I snap my fingers now, the fog "
                                                + "will clear and your senses will be sharper than ever before. You'll be able to see details you never would have been able to make out. You'll be able to hear a pin drop "
                                                + "in a storm. The entire universe will rush in through your skin and you'll perceive it all.\"</i><p>When Suzume snaps her fingers, you immediately realize something is wrong. "
                                                + "You feel a sensation like your foot falling asleep but on every inch of your skin. You fidget in discomfort and as soon as you move, the sensation become unbearably "
                                                + "painful. You grit your teeth and struggle to remain completely still, while Suzume hurriedly brings you out of the trance. She squeezes your hand painfully tight and "
                                                + "eases you into a prone position. <i>\"I know this hurts, but focus on the pain from your hand and the rest of it will go away faster.\"</i> You take her advice and soon the "
                                                + "sensitivity in the rest of your body dies down. She releases your hand when you finally feel normal again. <i>\"Sorry, sometimes these techniques don't go the way we plan. "
                                                + "If you want, we can try again another time, but for now you need some time to recover.\"</i>");
            }
            acted = true;
            Global.gui().choose(this, "Leave");
        } else if (choice.equals("Shut Out Sensation")) {
            if (Global.random(100) >= 50) {
                Global.gui().message(
                                "Suzume has you lie down on the cold, somewhat uncomfortable floor. <i>\"Now please close your eyes while I count down from ten. I want you to imagine a set of stairs and "
                                                + "with at each count I want you to take one step down. As you descend, everything in the world will fade away except my voice.\"</i> She begins to count down and you imagine yourself "
                                                + "walking down the stairs. By the time she finishes counting, you no longer feel the hard floor beneath you. There's no sensation and no ambient noise. The only thing that matters "
                                                + "right now is Suzume's voice. <i>\"Your body is your fortress. Nothing can reach you until you allow it. Just keep out everything that doesn't matter.\"</i><p>Suzume stops talking and "
                                                + "you find yourself alone in your mind. With no sounds to track the time, you aren't sure how long you're waiting before she speaks again. <i>\"How are you feeling right now? You can "
                                                + "speak.\"</i> You tell her that you are feeling pretty numb, but it's hard to tell how effective the meditation is. You hear Suzume giggle quietly. <i>\"Stay calm, open your eyes, and look "
                                                + "down.\"</i> You open your eyes and raise your head. Your pants and boxers are gone, and Suzume is gripping your balls tightly. This should be cause for alarm, but her command to 'stay calm' "
                                                + "is working quite well. <i>\"Don't worry, I would never damage someone in my care. You would, however, be in a lot of discomfort if the suggestion wasn't working.\"</i> She releases your "
                                                + "genitals, helps you to your feet, and hands you your missing clothes. <i>\"Your sensitivity should already be starting to return. You'll keep some of your endurance, but you'll probably "
                                                + "feel it when someone undresses you.\"</i>");
                if (player.getPure(Attribute.Perception) > 1) {
                    player.mod(Attribute.Perception, -1);
                }
            } else {
                Global.gui().message(
                                "Suzume has you lie down on the cold, somewhat uncomfortable floor. <i>\"Now please close your eyes while I count down from ten. I want you to imagine a set of stairs and "
                                                + "with at each count I want you to take one step down. As you descend, everything in the world will fade away except my voice.\"</i> She begins to count down and you imagine yourself "
                                                + "walking down the stairs. By the time she finishes counting, you no longer feel the hard floor beneath you. There's no sensation and no sound.<p>There's nothing. You feel completely "
                                                + "separated from your body and lost in the void. Forget about the meditation, you need to wake up now. Nothing happens. You can't find your way back to the world and your body. You begin "
                                                + "to panic, but suddenly you can see Suzume's face, very close to yours and looking worried. <i>\"Can you hear me? You went too deep and lost my voice. It's not a good idea to be so deep inside "
                                                + "yourself without something to guide you back. Do you feel ok now?\"</i> You affirm that you're fully awake and feel normal. She gives a quiet sigh of relief. <i>\"We should stop for now. I "
                                                + "don't think we can salvage this session. Give me a call if you want to try again.\"</i>");
            }
            acted = true;
            Global.gui().choose(this, "Leave");
        }
    }

    @Override
    public String toString() {
        if (Global.checkFlag(Flag.dojo)) {
            return "Dojo";
        } else {
            return name;
        }
    }

    @Override
    public void shop(Character npc, int budget) {
        if (npc.getPure(Attribute.Ki) > 0 && budget >= 1000 * (npc.getPure(Attribute.Ki) + 1)) {
            if (budget >= 2000 * (npc.getPure(Attribute.Ki) + 2)) {
                npc.money -= 1000 * (npc.getPure(Attribute.Ki) + 1);
//                budget -= 1000 * (npc.getPure(Attribute.Ki) + 1);
                npc.mod(Attribute.Dark, 1);
            }
            npc.money -= 1000 * (npc.getPure(Attribute.Ki) + 1);
            //budget -= 1000 * (npc.getPure(Attribute.Ki) + 1);
            npc.mod(Attribute.Ki, 1);
        }
        int r = Global.random(4);
        if (r == 3 && npc.getPure(Attribute.Perception) < 9) {
            npc.mod(Attribute.Perception, 1);
        } else if (r == 2 && npc.getPure(Attribute.Perception) > 1) {
            npc.mod(Attribute.Perception, -1);
        }
    }

}
