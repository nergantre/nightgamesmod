package nightgames.daytime;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;

public class MagicTraining extends Activity {
    private boolean acted;

    public MagicTraining(Player player) {
        super("Magic Training", player);
    }

    @Override
    public boolean known() {
        return Global.checkFlag(Flag.magicstore);
    }

    @Override
    public void visit(String choice) {
        Global.gui().clearText();
        Global.gui().clearCommand();
        if (!Global.checkFlag(Flag.metAisha)) {
            Global.gui().message(
                            "Aisha apparently spends most of her time in a mostly abandoned creative writing reference room in the back of the liberal arts building. On paper, she "
                                            + "apparently runs a fantasy writing workshop. You're not sure if she is serious about writing, but it makes a good cover.<p>When you get to the reference room, she's the "
                                            + "only one there. She's slightly taller than you with large, soft breasts. She has coffee colored skin and dark brown, long, wavy hair. When you introduce yourself she "
                                            + "gives you a gentle, charming smile. <i>\"Hello " + player.name()
                                            + ". I know who you are of course. The videos of your fights have been fascinating.\"</i> That catches you off "
                                            + "guard. It's no real surprise that your matches are being secretly recorded, but the idea that this woman has been watching you sex-fight before you even knew she existed is "
                                            + "somehow unnerving. She continues as if to distract you from your discomfort. <i>\"I'm pleased that you have an interest in learning magic. I haven't had many opportunities "
                                            + "to share this gift. I will unfortunately need to charge you for my services. Magic artifacts are very useful for researching the magical arts, but they are extremely rare and "
                                            + "sadly many are fakes. As reluctant as I am to take your money, this research will potentially benefit all of humanity.\"</i><p>Aisha is clearly very passionate about her work, "
                                            + "and Aesop vouched for her on no uncertain terms, but she's getting ahead of herself. Before you agree, you need a demonstration of what exactly she's offering. Aisha gives a "
                                            + "mysterious smile. <i>\"I think I can arrange a show for you.\"</i> She makes a gesture and the door disappears, leaving only a solid wall. She makes a second gesture and you are "
                                            + "completely unable to move. She walks closer and begins undressing you. <i>\"It would probably be more impressive if I disintegrated your clothes, but then you'd have a very "
                                            + "awkward walk back to your dorm.\"</i> Once you're completely naked, she touches your cock and chants softly. Nothing happens. Aisha steps back and disrobes. She's very beautiful "
                                            + "in the nude and her breasts are even bigger than you estimated. Despite the strange situation, you feel your flaccid dick start to harden. Your manhood grows to full mast, "
                                            + "and keeps growing. Your member keeps growing until it almost touches Aisha, standing three feet away. She touches the massive cockhead. <i>\"Very nice. Still sensitive, right?\"</i> "
                                            + "It is. The size of your penis defies all reason, but theoretically the sensitivity should be diffused over the increased surface area. That's not the case. Every inch is as sensitive as usual, there's just "
                                            + "a lot more of it.<p><i>\"I'm going to need some help to deal with this monster.\"</i> She snaps her fingers and you're suddenly surrounded by girls. It's Cassie, Mara, Angel and Jewel; "
                                            + "completely nude. Without a word, they all start licking and stroking your giant dick. The pleasure overwhelms you, but you're still lucid enough to hear Aisha's voice. <i>\"They're "
                                            + "not real of course, but they're close enough.\"</i> Aisha herself grinds her pussy against your tip, which is far too large to actually fit inside. You soon cum from the intense "
                                            + "stimulation, blasting Aisha with a huge quantity of semen.<p>She brings some of the liquid to her mouth and swallows it. She then reaches between her legs and coats her fingers "
                                            + "with her love juice before offering them to you. It takes you a second before you realize you can move again. After hesitating for a moment, you lick her fingers clean. <i>\"There "
                                            + "we go. I've tasted some of your essence and you've tasted some of mine.\"</i> What was that about? <i>\"Oh this wasn't just a demonstration. I also took the liberty of creating a magic "
                                            + "link between us. It'll make your training easier.\"</i>");
            Global.flag(Flag.metAisha);
            Global.gui().choose(this, "Lesson: $" + 1000 * (player.getPure(Attribute.Arcane) + 1));
            Global.gui().choose(this, "Leave");
            acted = true;
        } else if (choice.equals("Start")) {
            presentOptions();
            acted = false;
        } else if (choice.equals("Leave")) {
            done(acted);
        } else if (choice.startsWith("Lesson")) {
            if (player.money >= 1000 * (player.getPure(Attribute.Arcane) + 1)) {
                int scene;
                if (player.getPure(Attribute.Arcane) >= 6) {
                    scene = Global.random(4);
                } else if (player.getPure(Attribute.Arcane) >= 5) {
                    scene = 3;
                } else if (player.getPure(Attribute.Arcane) >= 3) {
                    scene = 2;
                } else if (player.getPure(Attribute.Arcane) >= 1) {
                    scene = 1;
                } else {
                    scene = 0;
                }
                switch (scene) {
                    case 0:
                        Global.gui().message(
                                        "Aisha nods and waves her hand through the air. As she does so, you feel the world around you shift and a huge wave of nausea "
                                                        + "catches you off guard. The sensation worsens to the point where you sink to your knees and clench your eyes shut in an attempt to stop the "
                                                        + "world spinning around you. Then all of a sudden the sensation stops and you open your eyes. You found yourself not in the small room you "
                                                        + "were in before, but a large windowless room. Despite the vastness of the room it is very empty save for several, what looks to be, archery "
                                                        + "targets at the far end of the room.<p><i>\"Sorry for not warning you,\"</i> Aisha says whilst leaning down to offer you a hand up. <i>\"The first "
                                                        + "transportation is always rough but it'll get better.\"</i> You take the offered hand and she helps you stand. <i>\"It's best to teach magic off campus,\"</i> "
                                                        + "she explains. <i>\"Fewer interruptions and well, with this first spell we'll need a bit more room.\"</i> She's teaching you a spell already? Nice. She grins "
                                                        + "and turns to face the targets. Raising one hand towards the targets, she focus for a brief moment before a bright light streaks across the room and "
                                                        + "slams into one of the furthest targets, sending the extremely heavy looking target careening into the back wall. <p>After the dust settles and "
                                                        + "you finish collecting your lower jaw, you ask how a spell like is going to be useful in the games. You want to make your opponents cum, not "
                                                        + "turn them to red mist. She flushes, seemingly embarrassed. <i>\"You won't be using it like that, I just wanted to show off a little. It's not "
                                                        + "often I get to do this, you know!\"</i> She pauses and appear to sober up a little. <i>\"The version I'm going to teach you is no "
                                                        + "more damaging than a solid kick but you should always remember to be careful,\"</i> she says, looking you directly in the eyes. <i>\"I don't "
                                                        + "think you're the kind of person to go crazy with this stuff.\"</i> she finishes with a grin.<p>She turns around and begins pacing taking on a "
                                                        + "lecturing tone. <i>\"The spell itself is actually quite easy. It requires very little concentration as all you're doing is pushing your mana "
                                                        + "outwards.\"</i> She pauses and turns her head towards you <i>\"You are familiar with the concept of mana, right?\"</i> You stop and consider the question. "
                                                        + "You've heard the word used in reference to fantasy fiction. It's suppose to be a sort of energy that powers magic spells. <i>\"Mana is essentially "
                                                        + "a refinement of your willpower. If I've judged you correctly, you've probably been using a raw form of that energy subconsciously. I'll teach you "
                                                        + "to shape and wield your power like a precision instrument. While you're learning, I'll also provide you with mana to practice with.\"</i> <p>The process really "
                                                        + "is simple and Aisha is an extremely supportive teacher. Within the hour, you think you have it down and you're able to hit the targets "
                                                        + "with relative ease. Aisha smiles once you hit the last target and, with a wave of her hand, you return to the campus. The nausea returns but its "
                                                        + "not so bad this time. You thank Aisha and make your way back to your dorm.");
                        if (!player.has(Clothing.getByID("cloak"))) {
                            player.gain(Clothing.getByID("cloak"));
                        }
                        break;
                    case 1:
                        Global.gui().message(
                                        "After you give Aisha the money she asked for she once again brings both of you to the same training room you were in before except "
                                                        + "this time there are no targets. In fact there is nothing in the room at all. You look to her confused and ask what she is going to be teaching "
                                                        + "you today. <i>\"Glad you asked.\"</i> She grins and reaches her hand out to the left; her arm disappearing to mid upper arm. A look of concentration "
                                                        + "comes over her face as she feels around with her arm, apparently looking for something. <i>\"Ah ha!\"</i> She exclaims and extracts...somthing...out "
                                                        + "of the air. <p>That something is what looks to be a naked small fat man... except he has small horns... and has a set dragonfly wings... and is wearing "
                                                        + "a crown.<i>\"This,\"</i> she explains, ignoring your strangled sounds of confusion, <i>\"is Xaldin, king of the fae. Xaldin, this is my adorable new "
                                                        + "apprentice.\"</i> The faerie regards you with a bored look for a brief moment before turning his attention back to Aisha. <i>\"Woman, you had better "
                                                        + "have a good reason for interrupting the best orgy I've seen for years.\"</i> He grumbles in a voice which sounds very similar to Danny Devito, if "
                                                        + "Danny Devito was six inches tall. <i>\"I do, actually.\"</i> She smiles patiently. <i>\"I wanted to introduce your newest summoner.\"</i> The fat pixie regards you again. <i>\"This kid?\" "
                                                        + "He scoffs. \"Why should I give him the contract?\"</i><br>Aisha sighs quietly, but her smile barely wavers. <i>\"For the same reason you contracted with me, "
                                                        + "the same reason you contracted with my previous apprentices, and the same reason you'll contract with my future apprentices. The arrangement benefits "
                                                        + "you as much as the summoner.\"</i><p>"
                                                        + "Xaldin doesn't appear to be listening and gives you a hard to read look. <i>\"So kid,\"</i> he sniffs, addressing you for the first time <i>\"You want to make a contract?\"</i> "
                                                        + "Xaldin keeps his eyes on you but behind him you can see Aisha nodding her head in an exaggerated manner. Clearly trying to tell you to agree. "
                                                        + "You tentatively nod, a bit unsure of what you're agreeing to. <i>\"Good.\"</i> He smiles in a manner that is slightly unnerving. With a brief gesture, a piece of "
                                                        + "parchment and an old fashion quill appears in front of you. You begin to read the contract but realize it's not in English. You turn and give Aisha a "
                                                        + "look that hopefully conveys your reluctance to sign something you can't read.<p><i>\"Fae writing isn't really a literal language, but I can "
                                                        + "understand it a bit.\"</i> she says, walking over to read it. After giving a quick once over, she turns "
                                                        + "to you and explains. <i>\"Looks like the standard terms. Basically if you sign it you will be allowed to summon faeries whenever you want to. They're not "
                                                        + "particularly strong, but they can use a bit of magic of their own and they're "
                                                        + "very good with their hands. In exchange you must offer up some mana as a gift when you summon them.\"</i> She turns to the faerie who is floating quietly near "
                                                        + "by. <p><i>\"Human mana is very rare commodity in faerie society,\"</i> he admits <i>\"So most young faeries will leap at the chance for any that is freely offered.\"</i><p><i>\"See?\"</i> "
                                                        + "Ashia smiles. <i>\"They're not after your first born or anything. Most faeries are much friendlier than this old grump and would probably help you for free. "
                                                        + "Of course, we mages have mana to spare, so it's only fair that we compensate those who offer their assistance.\"</i> <br>Aisha makes a good point, and "
                                                        + "there doesn't appear to be any risk. You take the quill and scribble your name down on the line at the bottom of the parchment. The old fae gestures "
                                                        + "towards the parchment and it disappears with a quick sparkle. <p><i>\"Oh, and one more thing kid.\"</i> Xaldin "
                                                        + "begins as he turns to look at you again. <i>\"Faeries are pretty sensitive compared to humans. If the faerie you summon cums or gets a strong shock, "
                                                        + "it'll break the summoning link and they'll be pulled back to my realm, so just watch out for that.\"</i> His warning given, the faerie winks out of existence "
                                                        + "with a sound like zip being pulled up.<p>Moments later you are walking out of Aisha room wondering if signing that contract will really be worth it in the long run.");
                        break;
                    case 2:
                        Global.gui().message(
                                        "Aisha grins and as per usual you find yourself in the training room, this time there's a bunch of mannequins with random assortments of "
                                                        + "clothes thrown on them. Not quite sure what to make of this you turn to Aisha with an expectant look on your face. <i>\"This spell is going a bit trickier,\"</i> "
                                                        + "Aisha smiles in a way that is not entirely reassuring <i>\"but don't worry I'm sure you'll be fine.\"</i> This feels the same as when doctors tell you that the "
                                                        + "next thing they will do won't hurt...and then is does. <i>\"Okay watch this!\"</i> Aisha says and waves her hand towards the closest mannequin. At first it doesn't "
                                                        + "look like anything happens but after a brief moment the clothes begin....turning into petals? <i>\"Cool right?\"</i> Aisha asks crossing her hand across her chest "
                                                        + "looking very proud of herself. <i>\"Only thing is....\"</i> She sighs <i>\"You know what, let me just show you.\"</i> She gestures to another mannequin only this time "
                                                        + "instead of turning petals the clothes burst into flames! <i>\"Yeah, if you put too much power into the spell that will happen.\"</i> She murmurs quietly. <i>\"But like I "
                                                        + "said, I'm sure it'll be fine.\"</i> Her smile returns. <i>\"That's never happened during an actual match. Besides, You seem to have a knack for this.\"</i> <br><i>\"Right let's get started.\"</i> She begins walking you "
                                                        + "through the mechanics of the spell. Huh this doesn't seem to hard... <p>...<p>...Oh god this is hard. A small pile of blackened mannequins sits solemnly in the "
                                                        + "corner of room, smoke gently wafting off it. <i>\"Perhaps we should try a different approach,\"</i> Aisha says thoughtfully. <i>\"You're charging the spell too long "
                                                        + "before applying the transmutation, that's why it's overheating. Let me show you an incantation I used when I was learning this spell. It's a bit silly, but it "
                                                        + "will help you learn the timing.\"</i><br>She makes a theatrical flourish towards one of the training dummies. <i>\"Flans Exarmatio!\"</i> The mannequin is disrobed in a "
                                                        + "bloom of flower petals. <br><i>\"The wonderous gifts of magic occassionally require us to look foolish.\"</i> She thinks for a minute before fixing you with a smile that is slightly "
                                                        + "too sweet. <i>\"I think some appropriate motivation would also help.\"</i> Uh oh. That doesn't sound good. <i>\"Here's what we'll do,\"</i> she begins. <i>\"For every mannequin you roast, "
                                                        + "I'll transmute a piece of your clothing, to demonstrate the correct technique.\"</i> She seems far too pleased about this idea, but you decide to let her finish before you object. <i>\"And for "
                                                        + "every mannequin you manage to de-clothe you can choose one piece of my clothes!\"</i> She exclaims, clearly pleased. A grin spread across your face. Fine, if she "
                                                        + "wants to play it this way. No way you're gonna pass up on an opportunity to see Aisha naked. You focus on the first target and begin chanting the spell. "
                                                        + "*Fwoosh* Flames erupt from the mannequin and you groan as you feel your shirt fall apart. <i>\"Nice, muscle tone.\"</i> Aisha notes appreciatively. <i>\"When we finish, "
                                                        + "I may need to run my hands over you before I let you get dressed.\"</i><p>You need to concentrate. You close your eyes and begin focusing on the spell again. "
                                                        + "You visualize Aisha's example and try to match the timing as best you can. <i>\"Flans Exarmatio!\"</i> <br>You hear Aisha gasp beside you and open your eyes to see "
                                                        + "the mannequin naked and petals all around it. You select Aisha's shirt and she sighs sheepishly "
                                                        + "before gesturing to her top. You grin as you watch a red lace bra appear as her shirt falls away. The delicate undergarment perfectly accentuates her generous bosom "
                                                        + "that seems ready to overflow out of it at any moment. The deep crimson suits her chocolate skin, giving her an elegant, yet sultry look. She either has a taste for "
                                                        + "high class lingerie or she was planning this when she got dressed. \"Shut up!\" She can't help smiling at your praise despite blushing deeply. "
                                                        + "<i>\"Just focus on the training.\"</i><br>Three mannequins later it becomes apparent that you have the spell mastered, and Aisha is down to just her panties. "
                                                        + "This time, instead of focusing on the mannequin, you cut out the middle-man by transmuting Aisha's last piece of clothing directly. <i>\"Hey!\"</i> She "
                                                        + "exclaims. <i>\"No cheating!\"</i> Her face quickly begins to change from one of shock into a wicked grin. <i>\"I never did show you how to counter magic, did I?\"</i> She asks a little too "
                                                        + "sweetly. You reflect with growing dread that she has not. <i>\"Too bad. Naughty boys need to be punished.\"</i> She smiles and waves her hand. <p>The room about you vanishes. You very quickly recognize your surroundings."
                                                        + "<br>No....<br>NOOOOOO!<br>You're in the small collection of bushes in the quad.<br>And you're naked.<br>You sigh and resign yourself to waiting till the college quiets down before "
                                                        + "running back to your room.");
                        break;
                    default:
                        Global.gui().message(
                                        "Aisha pauses and closes her eyes briefly before grinning and bringing you both to the training room. This time Aisha arrives before you... All four of "
                                                        + "her... Wait, what!? <i>\"Are you impressed yet?\"</i> You see the four Aishas' mouths moving in unison, but only hear one voice. <i>\"Look carefully.\"</i> When you focus, you notice "
                                                        + "that one of the four is subtlely different from the others. It's hard to articulate the difference, it's almost like the others aren't in quite as high resolution. "
                                                        + "<i>\"The purpose of this spell is to make you harder to hit. Humans are naturally good at distinguishing between real and illusionary figures. You can improve the detail with "
                                                        + "more time and focus, but I'd recommend keeping your clones close to you.\"</i> As she says this, the three illusions close around her real body until they're partially "
                                                        + "clipping through her. That's a little unsettling. <i>\"If you can manage it, try to have them move out of synch to keep your opponent from reading your movements or "
                                                        + "judging distance accurately.\"</i> Aisha suddenly lunges forward and reaches out to flick your forehead. You instinctly try to block her hand, but it passes right "
                                                        + "through you. You feel her real hand lightly squeeze your crotch teasingly, before she hops back out of range. <i>\"Don't give them time to spot the fake.\"</i> The "
                                                        + "spell wears off and the clones quickly fade away. <i>\"Let me show you how to cast it. The spell itself is not too difficult, but most apprentices stuggle "
                                                        + "with controlling the clones.\"</i><p>About an 20 minutes later you think you've got this down. You have found it was much "
                                                        + "easier than naked bloom, maybe it means you're improving? <i>\"You're a natural. I've never seen that combination of detail and control from a novice. If you focus "
                                                        + "a bit more on the creation, you could have some truly indistinguishable clones.\"</i> Aisha suggests and a you nod "
                                                        + "before quickly focusing. Seconds later you are looking at Aisha along side 3 copies of yourself. <i>\"So, which one is actually you?\"</i> She asks quickly looking across the line.You "
                                                        + "are about to raise your hand before you quickly realize that this is the perfect time for revenge for your trip to the quad at Aisha's hands at the end of the last "
                                                        + "training session. If you can convince her one of the other clones is you, you could scare the pants off her by sneaking up on her. Master plan decided upon, you will "
                                                        + "the clone of the far left to raise its hand and it does so perfectly. <i>\"Alright,\"</i> Aisha nods, oblivious to your masterful deception <i>\"Try getting one of your "
                                                        + "clones to walk closer to me.\"</i> Perfect! You walk in front of her while maintaining your best poker face. All that's left is to wait for the right moment to strike! <i>\"Not bad,\"</i> She intones as she "
                                                        + "steps right up to you. <i>\"But you can't trick a mage.\"</i> She whispers into your ear and before you can react, she slams her lips against yours. <p>In your surprise you can only "
                                                        + "gasp which gives Aisha to slip her tongue into your mouth and begin to assault your stunned tongue. The games, however, have taught you much in the ways of responding "
                                                        + "to the unexpected so you are quick to begin fighting her tongue back into her own mouth causing her to moan. The heavy kiss continues until the need for air becomes too "
                                                        + "pressing for both of you and break away. As you catch your breath Aisha gives a shove catching you off guard and you begin to fall backwards. <p>In mid fall, the world "
                                                        + "around you shifts and you land, not on cold hard floor, but a soft bed. Looking around you see a small room with books piled high on desks and bookcases that look there "
                                                        + "shelves are about to give up and snap under the weight of the heavy leather bound tomes on them. Looking up at Aisha you see she has already taken her top and bra off "
                                                        + "and thrown them across the room. Leaning back down over you she kiss begins to kiss you again. <i>\"Hope you don't mind but I brought you to my room,\"</i> she murmers between "
                                                        + "kisses. <i>\"No bed in the training room.\"</i> You certainly have no problem of this and help her remove your T-shirt, her hands rubbing up and down your chest.You both quickly "
                                                        + "return to kissing and before long all the clothes between you have disapeared. Your fingers slowly move from her back towards her snatch, which you find to be soaking "
                                                        + "wet. You gently rub your fingers up and down her lips, making gentle circles around her clit every so often. Aisha mewls above you, clearly enjoy the ministrations you're "
                                                        + "giving her but wanting more.<p>She sit upright forcing you to take your fingers away as she lines her pussy up with your cock. She grins at you and slowly eases herself "
                                                        + "down your shaft making both of you groan at the sensation. A happy gasp escapes her mouth as she sheaths you inside her fully. <i>\"God, that feels good.\"</i> She murmurs to herself "
                                                        + "sounding almost awed.Slowly she begins to move, grinding her hips in small circles initially before beginning to rock back and forth. No longer content to simply lie "
                                                        + "there you start to the thrust upwards towards her. She grins down at you, her smile haloed by her slightly messed up chestnut hair. Her eyes almost seem to be glowing "
                                                        + "as she looks down at you. You both continue slowly increasing the pace and Aisha moans slowly grow in volume. Along side your rapidly approaching orgasm, you feel something "
                                                        + "else shifting inside in a similar fashion to when you cast a spell.Looking up to Aisha you notice that it wasn't a trick of the light, her eyes really are glowing! "
                                                        + "Seeing you panicked look, Aisha places a hand on your cheek and kick her pace up another notch. The fever pitch pace brings you very close to the cusp. <i>\"Don't worry,\"</i> "
                                                        + "She gasps out <i>\"Just let it go, cum with me!\"</i> As she finishes speaking she throws her head back and moans loudly her pussy clamping down on you pushing you over the edge. "
                                                        + "As you come a sense of euphoria overcomes you as stars flash and colours swirl in front of your eyes. It's hard to tell where your body ends and Aisha's begins. Everything "
                                                        + "seems magnified and the pleasure continues to bounce between you both, feeding back on itself and growing ever larger until you begin to worry your brain will short circuit. "
                                                        + "After what feels like hours of this pleasure loop it begins to slow down and fade and after a while you find yourself laying along side Aisha in the bed, your arm circling "
                                                        + "her shoulder and her head resting on your chest. What the hell just happened to you? <i>\"Mana loop.\"</i> She states absently, clearly still enjoy the afterglow. "
                                                        + "<i>\"It happens when two mages cum at the same time. Good, right?\"</i> You can only nod. After lounging in bed for a while you finally excuse yourself. "
                                                        + "You walk back to your dorm, thinking about the awesome applications of magic.");
                }
                player.money -= 1000 * (player.getPure(Attribute.Arcane) + 1);
                player.mod(Attribute.Arcane, 1);
                acted = true;
            } else {
                Global.gui().message("You don't have enough money for training.");
            }
            Global.gui().choose(this, "Leave");
        } else if (choice.startsWith("Animism")) {
            if (player.money >= 500 + 500 * (player.getPure(Attribute.Animism) + 1)) {
                player.money -= 500 + 500 * (player.getPure(Attribute.Animism) + 1);
                Global.gui().message("Kat comes in again to help you practice tapping "
                                + "into your animal side. As per Kat's suggestion, you both spend "
                                + "the entire training session naked, so as to distance yourselves "
                                + "from civilized habits. Aisha volunteers to help out by teasing"
                                + " you to sufficient arousal for your instincts to come out. She "
                                + "also provides some actual help by monitoring your spirit's power.");
                player.mod(Attribute.Animism, 1);
                acted = true;
            } else {
                Global.gui().message("You don't have enough money for training.");
            }
            Global.gui().choose(this, "Leave");
        } else if (choice.startsWith("Ask about Animal Spirit")) {
            Global.flag(Flag.furry);
            Global.gui().message("You bring up the topic of Kat's animal spirit and "
                            + "how she mentioned Aisha's involvement. Aisha's smile fades and "
                            + "she nods somberly.<br><i>\"Yes. I told Kat that I was studying "
                            + "ways to harness and use spirit magic. She became interested in "
                            + "the idea and asked if I could use it to make her more capable "
                            + "in a fight. I should have refused until my research was more "
                            + "advanced, but... I was overconfident.\"</i> She sighs, her "
                            + "expression heavy with guilt. <i>\"I thought I could use the "
                            + "cat spirit to improve her strength and reflexes. I thought I"
                            + " understood how the spirit would affect her. I thought that"
                            + " even if something happened, I could simply release the "
                            + "spirit and return her to normal. I was mistaken about many "
                            + "things.</i>\"<p><i>\"When I performed the ritual, the cat "
                            + "spirit permanently fused to Kat's soul. She gained the "
                            + "strength and speed she wanted, but there were immediate "
                            + "physical and personality changes. She already had trouble "
                            + "dealing with people she didn't know, but from then on she"
                            + " was forced to keep people at arm's length so they wouldn't "
                            + "discover her supernatural attributes. I studied everything "
                            + "I could find on spirit magic and realized where I went wrong,"
                            + " but I could never find a way to fix it. She's likely stuck"
                            + " like that for the rest of her life.\"</i><p>Kat actually seems "
                            + "pretty positively inclined towards her cat spirit. Also, if Aisha's"
                            + " research has advanced so much, she could safely give you the spirit's"
                            + " abilities without the side-effects, right?<br>Aisha seems furious"
                            + " at your suggestion. <i>\"You want me to do to you what I did to "
                            + "Kat? No! I'm not going to risk ruining another life. How can you"
                            + " even ask such a thing?!\"</i> She doesn't appear likely to budge"
                            + " to you alone. If you really want this power, you'll probably "
                            + "need to rely on Kat's help.");
            Global.gui().choose(this, "Get Animal Spirit");
            Global.gui().choose(this, "Lesson: $" + (500 + 500 * (player.getPure(Attribute.Arcane) + 1)));
            Global.gui().choose(this, "Leave");
        } else if (choice.startsWith("Get Animal Spirit")) {
            Global.gui().message("Kat agrees to come to the creative writing reference"
                            + " room to try to convince Aisha. Aisha is delighted to see her and "
                            + "immediately pulls the shorter girl into a hug before she can say "
                            + "anything. <i>\"Oh my adorable little kitty Kat, you should come visit"
                            + " me more often.\"</i> Kat extracts her face from between Aisha's large"
                            + " breasts. Her expression appears midly annoyed, but you can tell she's"
                            + " secretly enjoying the attention.<p>After the two of the exchange "
                            + "greetings, Kat supports your request to be possessed with an animal"
                            + " spirit. Aisha looks crestfallen and shakes her head. <i>\"Kat. You of "
                            + "all people.... You know the dangers of this kind of ritual. What if"
                            + " he's changed the way you were. You can't want that for anyone else."
                            + "\"</i><br>Kat takes off her hat, revealing her feline ears, and "
                            + "smiles reassuringly. \"<i>These things aren't so bad. Sure they're"
                            + " a little inconvenient to hide, but my cat powers have given me so "
                            + "many wonderful things. I'm so grateful for what you've done for me. If"
                            + " spirit magic gives " + player.name()
                            + " even half as much happiness as it's given me, I'm "
                            + "sure he won't regret it.\"</i><p>"
                            + "Aisha finally relents and instructs you to get undressed while she leaves "
                            + "to gather the materials she needs. Kat fidgets awkwardly as you strip. "
                            + "She's bright red and tries not to stare at your naked body, but keeps "
                            + "sneaking obvious glances. You didn't think it was possible anymore, but "
                            + "her shy behavior is actually making you embarrassed.<br> Aisha returns "
                            + "after a couple minutes and quickly prepares for the ritual. You obediently "
                            + "follow her instructions and end up lying in a magic circle surrounded by"
                            + " lit candles and unidentified relics. You feel her drawing runes over "
                            + "your naked body, which feels odd and a bit ticklish, but you wait "
                            + "patiently until she's finished.<p> The ritual itself has strange effects "
                            + "on your consciousness, so you only have a vague recollection of it. "
                            + "It's apparently over by the time you come back to your senses. "
                            + "You open your eyes as you feel two pairs of hands running all over you. "
                            + "Aisha and Kat are both exploring your nude body, though not focusing too"
                            + " much on your erogenous zones. Kat runs her fingers through your hair, "
                            + "while Aisha reaches under your butt.<p> <i>\"No ears or tail, and I don't "
                            + "see any fur in unexpected areas.\"</i> Aisha looks very relieved and "
                            + "soon certifies you anatomically human. <i>\"With the safeguards I used "
                            + "this time, the effect may be less potent for you than it was for Kat, "
                            + "but your instincts seem pretty strong down here.\"</i> She grabs your "
                            + "rock-hard cock, sending a jolt of pleasure through you. <i>\"This big "
                            + "boy has been standing at attention for at least ten minutes now. We'll "
                            + "need to get your mental arousal to match your physical arousal before "
                            + "we'll know whether we succeeded. I'm tempted to try you out myself, but "
                            + "our Kitty Kat is already pretty needy. She'll probably be a more "
                            + "appropriate test anyway.\"</i><p><i>\"Nya?!\"</i> Kat lets out a startled "
                            + "noise at suddenly being the focus of attention. You hadn't noticed how "
                            + "animal-like she'd become, and maybe she didn't either. At Aisha's prompting,"
                            + " she strips naked and eagerly moves to straddle you. She lowers her hips "
                            + "and inserts your stiff rod into her soaked pussy. Pleasure shoots through "
                            + "you much more strongly than usual. The hot, wet, tightness fills a gap in "
                            + "your senses that you hadn't realized was there. The sensation lights a "
                            + "fire in your veins as she rides you energetically.<p>You feel...something..."
                            + " rise in you as your arousal builds. You can control it, but it's "
                            + "persistently guiding you. This feels good, but it would be better if you "
                            + "were on top and moving faster. You grab Kat and easily push her down,"
                            + " despite her increased feral strength. You position her on her hands and"
                            + " knees and impale her pussy from behind. She lets out a short yowl of "
                            + "protest, but decides she prefers this position as you vigorously thrust"
                            + " your hips. With this fierce fucking, it's not long until she's spasming "
                            + "with orgasm. You let out an involuntary howl as you shoot a thick load into"
                            + " her womb.<p>As you and Kat lie exhausted, your head starts to clear. "
                            + "Aisha clears her throat to remind you that you had an audience for your "
                            + "primal lovemaking. You doubt it would have mattered if you had remembered."
                            + " She looks a little flushed, though it's hard to tell with her dark skin "
                            + "tone. <i>\"I think we can consider that a success. If Kat is willing to "
                            + "continue to help you out, you should ask her to train you to control your new powers. "
                            + "I want you to do it here, so I can continue to watch you... just to be safe, of course.\"</i>");
            player.mod(Attribute.Animism, 1);
            acted = true;
            Global.gui().choose(this, "Leave");
        } else if (choice.startsWith("Buy a minor scroll: $200")) {
            Global.gui().message("You purchase a minor scroll. With the correct spell, "
                            + "you can use it to summon a team of fairies.");
            player.money -= 200;
            assert player.money >= 0;
            player.gain(Item.MinorScroll);
            acted = true;
            presentOptions();
        }
    }

    @Override
    public void shop(Character npc, int budget) {
        if (npc.getPure(Attribute.Arcane) > 0 && budget >= 1000 * (npc.getPure(Attribute.Arcane) + 1)) {
            if (budget >= 2000 * (npc.getPure(Attribute.Arcane) + 2)) {
                npc.money -= 1000 * (npc.getPure(Attribute.Arcane) + 1);
//                budget -= 1000 * (npc.getPure(Attribute.Arcane) + 1);
                npc.mod(Attribute.Dark, 1);
            }
            npc.money -= 1000 * (npc.getPure(Attribute.Arcane) + 1);
            //budget -= 1000 * (npc.getPure(Attribute.Arcane) + 1);
            npc.mod(Attribute.Arcane, 1);
        }
    }

    private void presentOptions() {
        Global.gui().choose(this, "Lesson: $" + 1000 * (player.getPure(Attribute.Arcane) + 1));
        if (player.getPure(Attribute.Animism) >= 1) {
            Global.gui().choose(this, "Animism training: $" + (500 + 500 * (player.getPure(Attribute.Animism) + 1)));
        }
        if (Global.checkFlag(Flag.catspirit) && !Global.checkFlag(Flag.furry)) {
            Global.gui().choose(this, "Ask about Animal Spirit");
        }
        if (Global.checkFlag(Flag.furry) && player.getPure(Attribute.Animism) == 0) {
            Global.gui().choose(this, "Get Animal Spirit");
        }
        if (player.getPure(Attribute.Arcane) >= 2 && player.money >= 200) {
            Global.gui().choose(this, "Buy a minor scroll: $200");
        }
        Global.gui().choose(this, "Leave");
    }

}
