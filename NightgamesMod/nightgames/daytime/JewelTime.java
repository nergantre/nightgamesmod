package nightgames.daytime;

import java.util.ArrayList;
import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.mods.FieryMod;
import nightgames.characters.body.mods.SizeMod;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.requirements.BodyPartRequirement;
import nightgames.requirements.NotRequirement;
import nightgames.requirements.RequirementShortcuts;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

@SuppressWarnings("unused")
public class JewelTime extends BaseNPCTime {
    public JewelTime(Character player) {
        super(player, Global.getNPC("Jewel"));
        knownFlag = "JewelKnown";
        giftedString = "\"Thanks! You're pretty nice you know?\"";
        giftString = "\"A present? I'm not going to go easy on you even if you bribe me you know?\"";
        transformationOptionString = "Training";
        transformationIntro = "[Placeholder]<br/>Jewel explains her training to you and how you too can train yourself.";
        loveIntro = "You're about to go see Jewel, but she shows up at your dorm room first. You invite her inside and she sits on your bed with her legs crossed. <i>\"I need "
                        + "some advice and you're my best friend, but I'm not sure if I should ask you. It's probably something I should talk to another girl about, but there aren't any other girls "
                        + "I can really confide in. When I was growing up I mostly hung out with the boys, so all that girl talk is kinda.... Will you hear me out?\"</i> You're not sure how much "
                        + "help you can be on feminine issues, but if you're the only person Jewel trusts enough to confide in, you have to at least hear her out. Jewel takes a deep breath "
                        + "before speaking. <i>\"I think I may be in love with you, but I'm not sure how to tell.\"</i> That was... not what you expected. Jewel's face turns a bit pink as you're "
                        + "struggling to process her words. <i>\"I shouldn't have told you that, should I?\"</i> You feel a bit guilty for hesitating. That wasn't a love confession, she's looking "
                        + "for advice. You should reply with that in mind. You ask her what her reasons are for thinking she's in love. <i>\"I was trying to meditate for my training earlier, but "
                        + "I was unable to clear my mind. Your image kept coming up. I know I'm attracted to you, all the time not just when we're fighting. I also like being with you even when "
                        + "we're not fighting or having sex. Is that love?\"</i> That's a hard question. You don't really have a strict definition of love available. Let's try a different tactic, if "
                        + "she is in love, what does she want to change? Should the two of you be exclusive? Jewel shakes her head. <i>\"That would mean we'd have to quit the games. I don't want that. "
                        + "Besides, seeing you with another girl is a real turn on. Maybe I'd like more kissing.\"</i> That's easy to fulfill. You move forward and kiss her passionately. She makes "
                        + "a pleased noise and returns the kiss enthusiastically. Eventually you have to break for air. What else? Does she want to go on dates? <i>\"We already train together and "
                        + "play challenging games. That's better than dinner and a movie. I think I would like to spend more nights together.\"</i> That's also quite a reasonable request. Jewel "
                        + "hesitates for a moment. <i>\"Does that mean we're lovers?\"</i> You are in every way you want to be. As for the rest, you can both figure that out as you go. Jewel nods. "
                        + "<i>\"I'll keep thinking about it. In the meantime, what do you want to do on our date today?\"</i>";
        advTrait = Trait.fighter;
        transformationFlag = "";
    }

    @Override
    public void buildTransformationPool() {
        options = new ArrayList<>();
        {
            TransformationOption growCock = new TransformationOption();
            growCock.ingredients.put(Item.PriapusDraft, 3);
            growCock.addRequirement(RequirementShortcuts.rev(new NotRequirement(new BodyPartRequirement("cock"))), "Has no penis");
            growCock.option = "Jewel: Grow a cock";
            growCock.scene = "[Placeholder]<br/>Jewel chugs down the three priapus drafts one after the other, making her clit grow into a large enlightened cock.";
            growCock.effect = (c, self, other) -> {
                other.body.add(new CockPart().applyMod(new SizeMod(SizeMod.COCK_SIZE_BIG)).applyMod(CockMod.enlightened));
                return true;
            };
            options.add(growCock);
        }
        {
            TransformationOption removeCock = new TransformationOption();
            removeCock.ingredients.put(Item.FemDraft, 3);
            removeCock.addRequirement(RequirementShortcuts.rev(new BodyPartRequirement("cock")), "Has a penis");
            removeCock.option = "Jewel: Remove her cock";
            removeCock.scene = "[Placeholder]<br/>Jewel drinks the three femdrafts one after another and her penis shrinks back into her normal clitoris.";
            removeCock.effect = (c, self, other) -> {
                other.body.removeAll("cock");
                return true;
            };
            options.add(removeCock);
        }
        {
            TransformationOption enlightenedCock = new ApplyPartModOption("cock", CockMod.enlightened);
            enlightenedCock.ingredients.put(Item.PriapusDraft, 10);
            enlightenedCock.ingredients.put(Item.EnergyDrink, 20);
            enlightenedCock.ingredients.put(Item.JuggernautJuice, 10);
            enlightenedCock.option = "Enlightened Cock";
            enlightenedCock.scene = "[Placeholder]<br/>Jewel shows you how to focus your spiritual energy into your cock.";
            options.add(enlightenedCock);
        }
        {
            TransformationOption fieryPussy = new ApplyPartModOption("pussy", FieryMod.INSTANCE);
            fieryPussy.ingredients.put(Item.FemDraft, 10);
            fieryPussy.ingredients.put(Item.EnergyDrink, 20);
            fieryPussy.ingredients.put(Item.JuggernautJuice, 10);
            fieryPussy.option = "Fiery Pussy";
            fieryPussy.scene = "[Placeholder]<br/>Jewel trains you so you can concentrate your ki inside your cunt.";
            options.add(fieryPussy);
        }
        {
            TransformationOption moltenAss = new ApplyPartModOption("ass", FieryMod.INSTANCE);
            moltenAss.ingredients.put(Item.MoltenDrippings, 2);
            moltenAss.ingredients.put(Item.EnergyDrink, 20);
            moltenAss.ingredients.put(Item.JuggernautJuice, 10);
            moltenAss.addRequirement((c, self, other) -> {
                return self.getLevel() >= 30;
            }, "At least level 30");
            moltenAss.option = "Molten Ass";
            moltenAss.scene = "[Placeholder]<br/>Jewel trains you so you can concentrate your ki inside your ass.";
            options.add(moltenAss);
        }
        {
            TransformationOption retraining = new TransformationOption();
            retraining.ingredients.put(Item.MoltenDrippings, 1);
            retraining.addRequirement((c, s, o) -> Global.getDay().time <= 16, "Be before 4 pm");
            retraining.option = "Retraining";
            retraining.scene = "[Placeholder] Jewel spends the rest of the day breaking you down and building you back up again. By the end of your time together, you feel like a new person!";
            retraining.effect = (c, self, other) -> {
                Global.getDay().time = 21;
                int level = self.getLevel();
                for (int i = 1; i < level; i++) {
                    Global.gui().message(self.dong());
                }
                for (int i = 1; i < level; i++) {
                    self.ding(c);
                }
                return true;
            };
            options.add(retraining);
        }
    }

    @Override
    public void subVisitIntro(String choice) {
        if (npc.getAffection(player) > 0) {
            Global.gui()
                  .message("You plan to intercept Jewel on her run again, but when you get to the gardens, you find her already there, sitting on a low stone wall. She smiles "
                                  + "when she sees you and stands up to meet you. Apparently she was waiting for you, but you hadn't made plans to meet. How long was she waiting here?<br/><i>\"I just got here a "
                                  + "few minutes ago. I had a feeling you were going to come here to see me. I've learned to always trust my intuition.\"</i> Her instincts are impressive and apparently convenient. "
                                  + "When you're within reach, she plants a light kiss on your lips. <i>\"If you came looking for me, I assume you're eager for some training. Tell me what you have in mind.\"</i>");
            Global.gui()
                  .choose(this, "Games");
            Global.gui()
                  .choose(this, "Sparring");
            Global.gui()
                  .choose(this, "Sex");
            if (Global.getPlayer()
                      .checkAddiction(AddictionType.DOMINANCE, npc)) {
                Global.gui()
                      .choose(this, "Ask about Dominance");
            }
        } else if (npc.getAttraction(player) < 15) {
            Global.gui()
                  .message("You wait by the campus gardens, which according to Aesop, is on Jewel's jogging route. Sure enough, she comes running by within ten minutes and "
                                  + "calls out to you when she notices you. <i>\"Hey " + player.getTrueName()
                                  + ". It's good to see you. Are you here to train too?\"</i> She's not breathing particularly heavily and there's only "
                                  + "a subtle touch of sweat on her skin, so presumably you've caught her early in her run. You could pretend to have run into her by coincidence, but "
                                  + "you decide to be honest and tell her you were hoping to train with her. She raises an eyebrow and looks over you appraisingly. <i>\"I wouldn't mind having a running partner, "
                                  + "but I don't plan to slow down for you. Keep up if you can.\"</i><br/><br/>With that she takes off again and you hurriedly fall in next to her. For awhile, the two of you just jog side "
                                  + "by side. She doesn't seem inclined to start a conversation so you decide to keep quiet as well. You do have plenty of opportunity to appreciate the way Jewel's form-fitting "
                                  + "exercise clothes accentuate her toned body, especially when she quickens her pace and pulls ahead of you. She catches you ogling her and smiles. <i>\"Do you like what you see? "
                                  + "I'll give you some motivation. If you can keep up with me until the end, I'll let you feel me up as much as you want.\"</i><br/><br/>That's certainly a tempting offer, but keeping up is "
                                  + "quickly becoming more of a problem. Jewel keeps speeding up, probably caught up in her competitive spirit. After a few minutes more struggling to keep pace, you have to admit defeat. Jewel "
                                  + "looks disappointed, but gives you a light smack on the butt and a <i>\"Thanks for trying\"</i> before she continues on her run.");
            npc.gainAttraction(player, 2);
            player.gainAttraction(npc, 2);
        } else {
            Global.gui()
                  .message("You look around the campus and manage to catch Jewel while she's preparing for her run. She's doing stretches in a tight workout outfit that shows "
                                  + "off her midriff and legs. She sees you and gives you a friendly wave as you approach. You greet her and suggest spending some time training together. <i>\"I was just about "
                                  + "to go for a run. You're free to tag along if you want, but I'll be setting a fast pace today.\"</i> That seems like a bit of a waste. She can go running anytime, but some "
                                  + "things are only possible with a partner. She stops her warmup and smirks at you in amusement. <i>\"Do you actually have some real training in mind or was that a clumsy "
                                  + "attempt to get laid?\"</i> There's that too, but you point out that some light sparring would be directly applicable to the sort of matches you engage in every night. Even "
                                  + "getting laid could be considered training, since sexual technique and endurance are both essential to victory in sex fights.<br/><br/>She mulls over this idea, cheeks slightly "
                                  + "flushed imagining it. She's clearly interested, so you decide to give her one last push. You lightly brush a hand over her exposed midriff and teasingly ask if she is up for "
                                  + "more one-on-one competition. She shivers at your touch and kisses you firmly. <i>\"You know exactly how to make me wet. I'll accept your challenge anytime.\"</i>");
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            Global.gui()
                  .choose(this, "Games");
            Global.gui()
                  .choose(this, "Sparring");
            Global.gui()
                  .choose(this, "Sex");
        }
        Global.gui()
              .choose(this, "Leave");
    }

    @Override
    public void subVisit(String choice) {
        if (choice.contains("Dominance")) {
            Global.gui()
                  .message("You cautiously ask about Jewel's dominant manner in the Games. \"Oh, just the nights aren't"
                                  + " enough for you? You need to be dominated during the day as well? Well, that can be"
                                  + " arranged.\" Wait, no! You didn't mean that at all! Right? You back away from Jewel "
                                  + "as she advances towards you. Of course, that only works for so long before you hit "
                                  + "a wall. Smiling confidently, Jewel grabs your hair with one hand and your coat with "
                                  + "the other. She leans into you and whispers into your ear. \"You are coming with me, "
                                  + "now. We are going to my room, and then I am going to have some fun. If you really "
                                  + "are the good submissive little slave you so desperately want to be, then so will "
                                  + "you. Follow.\" There's something about the way she spoke that last word. It "
                                  + "reverberates in your soul and disobedience doesn't even cross your mind until your"
                                  + " feet have already started shuffling after her. This is going to be bad, isn't it? "
                                  + "<br/><br/>Jewel's roommate, who you haven't met before, is lounging on the couch "
                                  + "when you walk in. A tall, fit brunette, whose expression changes from surprise to "
                                  + "amusement as she recognizes the look on Jewel's face. \"Another one, Jewel? I'm not"
                                  + " going to do the cleaning again if you make this one piss himself as well. Anyway, "
                                  + "I'll leave you to it. I have stuff to do anyway.\" She walks right through you on "
                                  + "her way out, shoving you to the side. You watch her leave (how could you not watch "
                                  + "such a fine ass?), and when you look back you are faced with a malevolently grinning "
                                  + "Jewel, holding a length of rope in her hands. \"Take off your shirt,\" she says in "
                                  + "that same commanding tone of voice. Her roommate's comments did not help your nerves,"
                                  + " but you swiftly comply. While pulling it over your head, suddenly your legs are "
                                  + "swept out from under you. You fall hard on your back, and Jewel less-than-gracefully"
                                  + " drops herself onto your chest, knocking the wind out of you. Before you can even"
                                  + " catch your breath, Jewel finishes pulling off your shirt and attaches two ropes to"
                                  + " your wrists. <br/><br/>\"Right over here, " + Global.getPlayer().boyOrGirl() + ".\" She gets up and uses the ropes to"
                                  + " drag you to the couch, attaching the ropes to its legs. While you test how much "
                                  + "movement you still have, Jewel finishes her work by tying your ankles to other "
                                  + "furniture. Now spread-eagled on your back, the near future is looking rather bleak. "
                                  + "\"Now, let's see what we can do with you.\" Jewel stands over you and teases her "
                                  + "foot over your chest. She moves it downward over your belly and presses down. It's "
                                  + "not a lot of pressure, but gradually increasing. Before long it starts to get quite"
                                  + " uncomfortable, and you find yourself breathing faster. She doesn't stop, and presses "
                                  + "down harder. The moment you grunt in pain, she removes her foot and delivers a swift "
                                  + "kick between your legs. Despite her having clearly held back from using her full force"
                                  + ", the pain is immense and you cry out in agony. Jewel silences you by sitting down on"
                                  + " your open mouth, her skirt forming a curtain around your head. You are still "
                                  + "whimpering into her panties due to the ever-so-slowly dwindling pain, and she seems"
                                  + " to enjoy it. Jewel grinds on your face until the fire in your balls finally dies "
                                  + "down. \"Why aren't you whimpering anymore? I guess if you're not going to do that, "
                                  + "you have no use for any air.\" Pulling her panties aside, she sits down right over your"
                                  + " mouth and nose. \"You'd best show some enthousiasm if you want another breath!\" You "
                                  + "do. You suck on her lips and circle your tongue around whatever it can reach. Despite "
                                  + "the fact that you're doing the best you can, Jewel does not stop. After half a minute "
                                  + "you are getting quite desperate, but it's not until the last moment that Jewel rises "
                                  + "up a bit, allowing you to draw in some much-needed air. Her mercy lasts only a few seconds "
                                  + "before she drops down again, and so begins a cycle of near-asphyxiation which last an "
                                  + "uncomfortably long time. You lick, suck and even gently nibble as best you can, but either "
                                  + "Jewel isn't satisfied or she just doesn't care. After a string of orgasms, she finally "
                                  + "decides she's had enough and stands up. Finally she's going to reciprocate, you think. Jewel"
                                  + " unties your bonds and, stroking your sore wrists, you get back on your feet. She throws "
                                  + "your shirt in your face and says, \"That was sufficient. You may go now.\" You very tentatively "
                                  + "inquire whether or not you're going to get some action as well, but no. \"You had the privilege"
                                  + " of licking me. That should be good enough.\" She shoves you towards and out of the door,"
                                  + " leaving you standing in the hallway holding your shirt in your hands, to the great amusement"
                                  + " of some of Jewel's neighbors. You hurriedly pull the shirt on and make your way out of the dorm.");
            Global.getPlayer()
                  .addict(AddictionType.DOMINANCE, npc, Addiction.MED_INCREASE);
            Global.getPlayer()
                  .getAddiction(AddictionType.DOMINANCE)
                  .ifPresent(Addiction::flagDaytime);
            Global.gui()
                  .choose(this, "Leave");
        }
        if (choice.equals("Sex")) {
            if (npc.getAffection(player) >= 16 && (!player.has(Trait.spiral) || Global.random(2) == 1)) {
                Global.gui()
                      .message("Jewel leads you to her bedroom instead of the wilderness for once. She seems unusually eager and almost like she could break into skipping at any time. "
                                      + "She's always been as fond of sex as the next girl, but this is uncharacteristic. You eventually decide to ask her if anything special is happening today. <i>\"Hmm? Well, "
                                      + "I guess I have some pretty high expectations for you today. Your sexual prowess has always been above average, but my instincts are telling me that you're capable of "
                                      + "more. I figure if we focus purely on sex in a suitable setting, I'll get to experience your full potential.\"</i> You hadn't realized she was expecting so much out of your "
                                      + "skill in bed. You haven't really been holding anything back. <i>\"I don't usually agree to sex without some sort of competition first. I'm expecting a really mind-blowing "
                                      + "orgasm in return.\"</i><br/><br/>Oh great. You're not sure you're going to be able to perform at all under that kind of pressure. When you reach Jewel's bedroom, she lets you "
                                      + "take charge. You kiss her firmly and push your tongue into her mouth. She makes a noise of approval, so you start to undress her while stroking and fondling the areas "
                                      + "you know she likes. By the time you slide off her panties, she's wet enough that they cling to her. Unfortunately you're having performance anxiety. The pressure of her "
                                      + "expectations is keeping you from getting more than half-hard. Your best hope for pleasing her is to use your mouth. You kiss your way to her clavicle, where you know she's "
                                      + "especially sensitive and linger there awhile. Once she's moaning and shivering, you work your way down between her breasts and lick around her navel. That earns you a "
                                      + "shudder of pleasure before you move further downward and place a kiss on her lower lips. She's clearly on the home stretch and ready to be finished off. You dive into her "
                                      + "muff and put your tongue to good use. Your tongue is still inside her when she climaxes and squeezes it tightly.<br/><br/>Hopefully that orgasm lived up to her expectations. She's "
                                      + "still breathing heavily, but she gives you one of her most aggressive grins. <i>\"That was a good start, but I've never been a one orgasm girl. Besides, as long as you've "
                                      + "got your pants one, we're still on the appetizer.\"</i> She rolls on top of you and strips you naked. Fortunately licking Jewel to orgasm was sufficient to overcome your nerves "
                                      + "and bring you to full mast. Once Jewel finishes undressing you, she lays back and spreads her legs for you. You rub the head of your dick against her entrance to tease her "
                                      + "a bit before you thrust into her slippery depths. You start moving your hips, but have trouble establishing a good rhythm. Jewel moans softly in time to your thrusts, but "
                                      + "looks somewhat dissatisfied. She eventually rolls on top of you and takes control of the thrusting. Soon she starts speeding up as she nears her peak, but you know she won't "
                                      + "be satisfied if you let it end like this. You brace yourself and manage to keep from cumming when she does. Jewel gives you another smile. <i>\"That felt good, but I know you "
                                      + "can do better. If you're the kind of man I think you are, you aren't finished yet. Just remember that if you cum, I won't wait for you to recover. Show me what you can do.\"</i><br/><br/>"
                                      + "You accept her challenge, but it's not going to be easy. Jewel expects something far better than her last two orgasms, and you're still pretty close to the edge. You take top "
                                      + "position again and start with shallow thrusts at an angle that maximizes contact with her love button. She shivers and lets out a mix between a moan and a whimper. <i>\"Good start. "
                                      + "MM! But you know I prefer it deep when I'mMM! turned on. How will you MM-manage that in your current state?\"</i> You grit your teeth and deepen your strokes, trying desperately to "
                                      + "hold back your orgasm. You almost lose it and have to slow down. <i>\"Not good enough. I'm not going to cum at this rate.\"</i> She pulls your head close and kisses you tenderly. <i>\"You "
                                      + "can do this. I believe in you.\"</i> There's no way you can disappoint her after coming this far. You put everything you have left into thrusting your hips, determined to make her cum"
                                      + "as soon as possible. Your determination seems to take hold of your body and your hips move on their own, circling around in a drilling motion that you wouldn't have thought possible. "
                                      + "Your already overstimulated cock rubs against Jewel's vaginal walls and she screams in pleasure even as you ejaculate into her. For some reason the words 'Giga Drill Break' come "
                                      + "unbidden to your mind, but you resist the urge to yell. It takes Jewel a lot longer to recover from this orgasm, but when she does she smiles contently. <i>\"That was even better than I "
                                      + "expected.\"</i>");
                if (!player.has(Trait.spiral)) {
                    Global.gui()
                          .message("<br/><br/><b>You've learned to harness the awesome power that has laid dormant in your soul... maybe. You've at least learned to move your hips a lot during sex.</b>");
                    player.add(Trait.spiral);
                    npc.getGrowth().addTrait(0, Trait.spiral);
                }
            } else {
                Global.gui()
                      .message("<i>\"Come on, it's not far now.\"</i> Jewel encourages you as you fall behind her slightly. The two of you are running at a slower pace than usual. You "
                                      + "aren't sure why you're running at all. You suggested sex and she seemed very excited about the prospect, but now you're running. Horizontal jogging can be used "
                                      + "as a euphemism for... no, you're sure you said sex. <i>\"Think of this as a warm up,\"</i> she explains when you ask her about the surprising absence of sexy times. "
                                      + "<i>\"If we get a little sweaty and tired now, we can have more fun when we arrive.\"</i> This doesn't really seem to make much sense, but you don't know where you "
                                      + "are going so you keep following her anyway. You're going through a heavily wooded park and seem to be getting further from the trail. You start wondering if she's planning some "
                                      + "weird survival game, when you hear the sound of running water.<br/><br/>Jewel stops next to a clear, flowing stream and immediately starts stripping naked. <i>\"I love "
                                      + "this place. The water is usually the perfect temperature for cooling down after a run.\"</i> Jewel tosses her panties onto a dry rock and gives you an impatient look. "
                                      + "<i>\"This isn't a free peepshow. You need to skinny dip too. Don't worry, nobody knows this place but me.\"</i> You feel somewhat honored that she'd share her secret "
                                      + "skinny dipping spot with you. You quickly undress and join Jewel, who is wading into the knee deep water. The water would be uncomfortably cold if you hadn't just been "
                                      + "running. Jewel sits down to immerse more of her body in the water and you follow her lead, letting the current wash your sweat away.<br/><br/>After a few minutes, she stands "
                                      + "up and turns to you. <i>\"This is great, but we came here with some very important activities planned. Shall we?\"</i> When you stand up, she lets out an uncharacteristically "
                                      + "girly giggle. <i>\"It is very cold isn't it?\"</i> she says while looking between your legs. You flush in shame and try to hide the effects of the cold water, but she gently "
                                      + "takes hold of your genitals and starts warming them up with her hands. <i>\"It's ok, I have someplace nice and hot to put it.\"</i><br/><br/>Jewel sits down on a large rock and "
                                      + "spreads her legs invitingly. She uses two fingers to spread her lower lips open and show you she's already wet with more than just water. The sight instantly brings "
                                      + "you to full mast. You line up your cock with her entrance and thrust into her in one smooth motion. She moans loudly from the sudden entry and smiles at you lustily. "
                                      + "<i>\"That never gets old.\"</i> Her insides are tight and just as hot as she promised. After sitting in the chilly stream, her pussy feels like a volcano. You start to move "
                                      + "your hips and soon find a comfortable, steady rhythm to fucking her. She has to hold onto you to keep from sliding off the rock. You keep up this pace until you feel "
                                      + "like you're reaching your limit. Jewel notices you're about to cum and she seems on the verge too. <i>\"Cum in me! I need to feel your seed inside.\"</i> You oblige her and "
                                      + "shoot your hot load into her womb. You immediately feel her tense up as she hits her own peak. Once you catch you breath, you pull out of her. Your semen leaks out of "
                                      + "Jewel's vagina, but she looks no worse for wear. <i>\"Well worth a bit of a hike, don't you think?\"</i> she asks, smiling. <i>\"We should probably both wash up a bit before we "
                                      + "head back.\"</i>");
            }
            Global.gui()
                  .choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Seduction);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Games")) {
            if (npc.getAffection(player) >= 8 && (!player.has(Trait.fearless) || Global.random(2) == 1)) {
                Global.gui()
                      .message("Today you've brought a wargame to appeal to Jewel's love of military history. You let her play the Allies, both because her father is a soldier and because the "
                                      + "Allies are on offense in this scenario. She advances all her troops from the start, taking heavy casualties, but stealing a couple victory point locations from you. You bide "
                                      + "your time, not worried yet. You are being pushed back, but you're retreating into more defensible positions. More importantly, Jewel will start pulling back her damaged units "
                                      + "soon or risk losing them. That should give you some breathing room and maybe create openings where you can counterattack. Several turns later it's clear she's not going to pull "
                                      + "back. The sustained contact is taking a heavy toll on your troops, but you cycle the more damaged ones away from the front to minimize loses, while Jewel is losing units unnecessarily "
                                      + "by keeping them in combat. She has a bit of a buffer since she started with a lot more troops than you, but this isn't a sustainable long term strategy.<br/><br/>Jewel eventually stands up, walks "
                                      + "around the table, and kisses you firmly on the lips. You're used to this. She's gotten in the habit of kissing you when she gets too hot and bothered during a game. It doesn't "
                                      + "really seem to cool her arousal, but it apparently helps her restrain herself until the game is over. It is also a sign that you're going to be ravaged later. Maybe the reason for her "
                                      + "all-out offensive is that she's feeling impatient and not planning carefully.<br/><br/>When she settles back in across the table, you feel compelled to speak up and "
                                      + "warn her that she's going to lose if she doesn't pull her weakened units back, but she shakes her head. <i>\"Fortune favors the bold.\"</i> The very next turn she draws a new card and "
                                      + "immediately plays it: Their Finest Hour. This is disastrous for you. She can attack with all her units this turn and they're all in firing range. She hits you across the whole front, "
                                      + "wiping out your damaged units and earning her just enough victory points to win the scenario.<br/><br/>That was an extraordinary bit of luck for her. Those tactics would have been suicidal "
                                      + "on a real battlefield. <i>\"This isn't a real battlefield,\"</i> she retorts. She points to one of the troops in her casualty pile. <i>\"This is a piece of plastic, not a person with parents, "
                                      + "kids, or a spouse. In a game, the only thing that matters is victory. When you lose a piece, no one has to cry over a friend or family member's grave. You can't treat war like a game, "
                                      + "and likewise you can't treat a game like war.\"</i><br/><br/>She stands up and practically drags you out of your chair. <i>\"Enough serious talk. Strip and get on the bed. I'm on top today.\"</i>");
                if (!player.has(Trait.fearless)) {
                    Global.gui()
                          .message("<br/><br/><b>Jewel has taught you a valuable lesson in reckless determination.</b>");
                    player.add(Trait.fearless);
                    npc.getGrowth().addTrait(0, Trait.fearless);
                }
            } else {
                Global.gui()
                      .message("You were somewhat worried how Jewel would respond to the prospect of playing some potentially geeky games, but it seems your concerns were completely unfounded. "
                                      + "The idea of running around with a longsword and slaying monsters immediately appeals to her. She's extremely sharp and learns complex rules in no time. She has also studied enough "
                                      + "military history to recognize a good strategy. No matter what you play, these gaming sessions always prove to be enjoyable mediums for the two of you to get closer. The only "
                                      + "problem is that Jewel's fetish for competition prevents you from finishing long games. Whenever a tense game's climax starts to drag on, she inevitably attacks you and tears "
                                      + "off your clothes. Honestly, as far as problems go, you could do much worse.");
            }
            Global.gui()
                  .choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Cunning);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Sparring")) {
            if (npc.getAffection(player) >= 12 && (!player.has(Trait.dirtyfighter) || Global.random(2) == 1)) {
                Global.gui()
                      .message("Your martial arts training has finally reached the point where Jewel is willing to spar with you seriously. She's all business when it comes to sparring. There "
                                      + "are no sex holds and no stripping. The only victory condition is to make your opponent admit defeat. Jewel darts in with a snap-kick, which you barely manage to block, and "
                                      + "ducks out of the way of your counter. In theory, neither of you is trying to actually hurt the other, but if you miss a block she won't be able to pull the punch in time. "
                                      + "You hate to admit it, but there's still a much better chance of her hitting you than the other way around. Still, you've definitely gotten a lot better at this. When she "
                                      + "takes a chance with a big roundhouse, you manage to block it and sweep her other leg, dropping her to the floor.<br/><br/>This is the first time you've managed a decisive takedown "
                                      + "against Jewel. You do your best to suppress a grin as you reach down to help her back up. She catches you off guard by grabbing your wrists as her foot shoots up between your "
                                      + "legs and connects solidly with your groin. You let a strangled groan as you fall to the floor next to her and curl up in the fetal position. She grasps your head and brings her "
                                      + "face close to yours. Her expression is unexpectedly frustrated. <i>\"Did you hear me surrender? Don't assume the match is over just because you have the advantage.\"</i><br/><br/>She kisses you "
                                      + "aggressively before climbing over your body to spoon against you from behind. <i>\"The Romans believed that there could be no victory as long as the defeated did not believe they "
                                      + "were vanquished.\"</i> She inexplicably begins an history lecture as if you weren't groaning in pain. <i>\"That's what saved them during the Second Punic War, when Hannibal outmaneuvered "
                                      + "and destroyed their armies time and again. Instead of surrendering, they rebuilt their armies, evaded Hannibal's forces, and struck the strategically valuable and unguarded city of New Carthage. In fighting terms, they "
                                      + "were flat on their backs, so they aimed right for Carthage's balls. Speaking of which...\"</i> She slides her hand down the front of your pants to lightly grasp your testicles. "
                                      + "<i>\"You haven't given up yet. Should I assume you want to keep fighting?\"</i> You hastily concede before your situation becomes any more painful. She breathes lustily in your ear and "
                                      + "fondles your penis. <i>\"Try to recover quickly. I need this in me as soon as possible.\"</i>");
                if (!player.has(Trait.dirtyfighter)) {
                    Global.gui()
                          .message("<br/><br/><b>You've learned the hard way that kicks can be dangerous, even when you're down.</b>");
                    player.add(Trait.dirtyfighter);
                    npc.getGrowth().addTrait(0, Trait.dirtyfighter);
                }
            } else {
                Global.gui()
                      .message("When you suggested a bit of sparring to Jewel, you weren't expecting anything like this. Despite your experience grappling and sex fighting each night, you don't "
                                      + "have any formal martial arts experience. Jewel was not very impressed when she saw your technique and she insisted that this time would be best spent training you. <i>\"I'll shape you "
                                      + "into a man who can proudly stand as my rival.\"</i> with that plan, the two of you soon find a small out-of-the-way courtyard and start training.<br/><br/>Jewel is a strict drill sergeant and "
                                      + "reprimands you whenever your technique fails to match up to her standards. You feel like you're learning quickly for a beginner, but her criticism doesn't let up. As she starts "
                                      + "teaching you more advanced techniques, she adds a new rule to keep you motivated. <i>\"From now on, each time you make a mistake, you have to remove an article of clothing.\"</i> Now this "
                                      + "would be fine if the two of you were training in a private room, but you're doing this outside. This yard isn't visible from any windows and is away from most foot traffic, but there "
                                      + "is no guarantee that someone won't wander in. <i>\"Try to keep your pants on then. If you do really well, I'll take some clothes off too.\"</i><br/><br/>You manage to keep your pants for about 15 "
                                      + "minutes. You lose your boxers a few minutes later. To be fair, you've done so well until now, that Jewel is topless. You're still trying to get used to being naked outside during "
                                      + "the day, when you hear giggling behind you. Turning around, you see a couple girls enjoying your nude martial arts exhibition. One of them is taking pictures with her phone. Jewel, "
                                      + "still caught up in drill instructor mode, immediately chews out the pair. <i>\"We have no room here for slack-jawed spectators. If you're going to laugh at my prized student, get in "
                                      + "here and show me what you can do!\"</i><br/><br/>It says a lot about the force of Jewel's personality that both girls joined the training without much protest. Apparently the same clothing loss "
                                      + "penalty applies to them too. They've long since stopped making fun of you when their clothes start coming off. By the time training is over, you still don't know either girl's name "
                                      + "but you feel a faint sense of solidarity with them, even though Jewel let them keep their panties on.");
            }
            Global.gui()
                  .choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Power);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Leave")) {
            done(true);
        }
    }

    @Override
    public Optional<String> getAddictionOption() {
        return Global.getPlayer()
                     .checkAddiction(AddictionType.DOMINANCE) ? Optional.of("Ask about Dominance") : Optional.empty();
    }
}
