package nightgames.daytime;

import java.util.ArrayList;
import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.global.Global;

public class AiriTime extends BaseNPCTime {
    public AiriTime(Character player) {
        super(player, Global.getNPC("Airi"));
        knownFlag = "Airi";
        giftedString = "\"Uhm... thank you!\"";
        giftString = "\"Oh wow... for me? th-thanks...!\"";
        transformationOptionString = "Error";
        advTrait = null;
        transformationIntro =
                        "[Placeholder]<br>Nothing here to see.";
        loveIntro = "[Placeholder]<br>Airi greets you at the door and flies into your arms. She looks pretty happy to see you.";
        transformationFlag = "";
    }

    @Override
    public void buildTransformationPool() {
        options = new ArrayList<>();
    }

    @Override
    public void subVisitIntro(String choice) {
        if (npc.getAffection(player) > 0) {
            Global.gui()
                  .message("You have some free time, so you decide to text Airi to see if she wants to meet up. You receive an answer in a few minutes with instructions to her flat off campus. "
                                  + "There's no time like the present, so you pack up and head towards her place. You arrive at a high-rise close to the north gate; following her instructions you press the dialer and button in her number. "
                                  + "A few cracks of static later, you hear her soft voice <i>\"Hey " + player.getName() + ", please come on in.\"</i> The sliding doors open for you and you head to her floor."
                                  + "<br/><br/>"
                                  + "You're a bit nervous, but you quickly find yourself standing outside a posh entrance ringing the bell. The sound of foot steps ring out from behind the door and you a flushed Airi welcoming you inside. "
                                  + "She's in human form again, wearing an elegant but sexy blouse and skirt set instead of her usual down to earth lab gear. You smile wryly at her as you realize that she's been getting ready for you.");
            Global.gui()
                  .choose(this, "Games");
            Global.gui()
                  .choose(this, "Sparring");
            Global.gui()
                  .choose(this, "Sex");
        } else if (npc.getAttraction(player) < 15) {
            Global.gui().loadPortrait("airi_human.jpg");
            Global.gui().showPortrait();
            Global.gui()
                  .message("You have late homework to do for your chemistry 102, so you head to the university lab. Upon entering, you notice someone has gotten here before you. "
                                  + "Airi quietly sits in the corner, concentrating on her equipment. Surprisingly, she looks nothing like the translucent slimegirl you're used to in the games. "
                                  + "While her face is identical to her slime form's, she looks like a completely normal slim asian girl wearing jeans and a loose tshirt. "
                                  + "None of the quiet confidence and hunger she displayed last night seems to exist anymore. Rather, her bangs seems to hide her eyes and her presence is very thin."
                                  + "<br><br>After observing her work for a bit, you decide not to bother her and start working on your own exercises.");
            npc.gainAttraction(player, 2);
            player.gainAttraction(npc, 2);
        } else if (npc.getAttraction(player) < 25) {
            Global.gui()
                  .message("You decide to go looking for the mysterious Airi. You know she's a student at the university, but everything else is a blank to you."
                                  + "You've seen her a few times when you were in the lab, so you decide to try your luck there again."
                                  + "<br><br>"
                                  + "It seem's like fortune was on your side today, and you find her once again sitting alone in the corner of the lab, this time having lunch. "
                                  + "You make a friendly gesture to her, and head towards her table. At first she seems confused, but once recognition registered in her eyes, Airi yelps and runs away."
                                  + "Confused by her now uncharacteristic behavior, you dejectedly decide to try again later.");
            npc.gainAttraction(player, 2);
            player.gainAttraction(npc, 2);
        } else {
            Global.gui().loadPortrait("airi_human.jpg");
            Global.gui().showPortrait();
            Global.gui().message("Once again, you chanced upon Airi in the university lab. You wrestle with yourself whether you should try approaching her again, seeing how well your last few attempts went. "
                            + "You glance up at the lithe asian girl, and you immediately notice something wrong. Airi has collapsed on her table with her face flushed and her skin moist with sweat."
                            + "Throwing your caution to the winds, you rush over to her and ask her if she's OK.");
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            Global.gui().choose(this, "Rush over");
            return;
        }
        Global.gui()
              .choose(this, "Leave");
    }

    @Override
    public void subVisit(String choice) {
        if (choice.equals("Rush over")) {
            Global.gui().loadPortrait("airi_mostly_human.jpg");
            Global.gui().showPortrait();
            Global.gui().message("She doesn't immediately respond to you, so you try to examine her to see if there's something you can do. As you try to turn her head to check if she's conscious, "
                            + "you fingers sink into her face. You yell in surprise and quickly withdraw your hand. Coating your fingers are pieces of moist transparent slime that you've become "
                            + "familiar with in the night games."
                            + "<br><br>"
                            + "Airi finally notices you standing next to her. After a momentary conflicted look passes her eyes, Airi softly mouths to you, <i>\"Please... help...\"</i> "
                            + "Well you're not one to leave a suffering girl alone; you quickly nod your agreement and ask Airi what she needs you to do. Airi answers with scarlet (now increasily translucent) cheeks,"
                            + "<i>\"I um... just need... to drink...\"</i> Well that seems like a simple enough request. You make a motion to stand up and grab your water bottle.");
            Global.gui().choose(this, "Stand Up");
        } else if (choice.equals("Stand Up")) {
            Global.gui().loadPortrait("airi_mostly_slime.jpg");
            Global.gui().showPortrait();
            Global.gui().message("As you're about to walk away, you are suddenly knocked to the floor from behind. When you try to turn around to see what's the big deal, you find Airi's crimson staring into yours."
                            + "<i>\"No... you can't leave... I won't let you... I'm so... thirsty...\"</i> Oh crap, she has turned back into the slime girl you remember from the games. Her face remains the same, but her "
                            + "skin looks like patches of it is melting into crystal blue goo. Recalling the last time she sucked you dry, you start struggling against her pin. Surprisingly Airi immediately lets you go. "
                            + "You observe a painful dejected look on her face as you stand up. Sitting on her rapidly melting legs, she looks up at you through the cracks of her soft-looking bangs, <i>\"I... can't..?\"</i>"
                            + "<br><br>"
                            + "With a sigh, you give up. It's hard to leave anyone like that alone, much less a cute girl. As soon as you nod your consent to her, Airi smiles brilliantly and springs on top of you again, "
                            + "knocking you off your feet. Remembering that you have your favorite pair of jeans on, you quickly ask her not to dissolve them like in the games. Airi nods, <i>\"No... problem...\"</i> "
                            + "However, instead of taking them off, Airi merely drops her pseudo slime hips into your pants her gooey pussy around your rock hard erection, completely unimpeded by your clothing. "
                            + "She flashes you a triumphant grin seeing your surprise."
                            + "<br><br>"
                            + "Airi gives you a long sloppy kiss and starts riding you with her now mostly liquid body. At first you try to hump back, but you quickly give up when you realize your efforts are merely "
                            + "going through her immaterial body. Airi notices this too and give you a soft giggle <i>\"Don't worry... Leave it... to me...\"</i> As soon as she finished talking, the feeling around your cock "
                            + "changes. Whereas before, you felt like you were fucking a drentched but normal pussy, now it feels like she's sucking on your pole like a seasoned whore's blowjob. You groan and try to resist cumming straight away. "
                            + "Slipping into old habits, you grin challengingly at Airi. Noticing your resistance, a cute pout appears on her lovely face. <i>\"It's no use...\"</i> Uh-oh, unfortunately for you, you seem to have made her serious. "
                            + "Her slime creeps up your body and covers it like a giant coat of jello. Her neck sprouts out from the slime covering your chest, and pseudo-hands hold your head so its facing her. With a sadistic grin, "
                            + "Airi looks you in the eye and kisses you with her purple jewel-like lips. "
                            + "<br><br>"
                            + "You relax at first thinking she was just giving you a kiss. However, Airi is not done yet. She extends her tongue and feeds it into your mouth. Her slimey appendage dances with your tongue, "
                            + "and tickles your oral cavity. Just as you started feeling out of breath though, Airi slithers her tongue down your throat. You start gagging, but her neverending tongue does not let up. She continues to feed it until "
                            + "it's half way down your throat. You can't breathe. You can't move either. All you can do is stare into Airi's crimson eyes and hope she lets you go."
                            + "<br><br>"
                            + "Airi takes this time to resume pumping at your cock. You struggle to maintain your consciousness, but the coying pleasure from her slimey pussy is too much and you spray your seed inside her clear body within seconds. "
                            + "Not letting up, Airi continues riding you even as you start finish cumming. Somehow you do not go flaccid. You don't know what she's done to you, but all you can do is hold on and hope not she lets you go before you "
                            + "suffocate or get fucked to death. Airi merely repeats her actions, tickling your throat with her tongue and squeezing you with her liquid hole. It's too much. You silently scream, ejaculate, and pass out. Not necessarily in that order."
                            + "<br><br>"
                            + "When you come to, it's already evening. You find yourself in the lab infirmary lying on a bed. You're surprised to also see Airi sitting next to you, back to her normal human form. Catching your eye, Airi immediately apologizes, "
                            + "<i>\"...uhm, I'm very very sorry! I didn't mean to take it that far!\"</i> Well that's rather surprising. You don't think you've ever heard her string that many words together in one sentence."
                            + "<i>\"I probably owe you an explanation...\"</i> Over the next half hour, Airi filled you in on what has happened to her. Apparently, she was also invited to the nightgames by the same sponsors that contacted you when she was a freshman a year ago. "
                            + "She tried participating, but didn't get many wins. However, she did notice that some of the blackmarket goods that Aesop introduced her to was scientifically inexplicable. She bought some of the mixtures and artifacts, and tried to perform composite "
                            + "analysis on them in order to find out how they were made. <i>\"It would be a big win for science! Some of the stuff there could actually grow limbs and organs! Imagine if we had that for everyone!\"</i> she exclaimed. Certainly, you thought as well "
                            + "that the games were strange, but then again, you already threw away your common sense when you met the likes of Reyka. Anyways, one of the reagents used in her experiements apparently reacted violently with a bottle of succubi draft, and the fumes "
                            + "turned her into something different."
                            + "<br><br>"
                            + "Airi tells you that she has since learned to mimic being human, but she discovered that internally she is more like a macrocolony of independent cells. This is all okay so far, but it seems like her transformation has even completely transformed her DNA. "
                            + "She can \"remember\" what its like to be human, but without that celluar blueprint, her mimicry falls appart after a certain amount of time. Apparently mammal supplimenting herself with mammal DNA through meat helps a lot, but live human cells are the best. "
                            + "That leaves her the choices of either becoming a cannibal... or having a lot of sex. It's pretty clear what she chose. It's not too inconvinient for her now, but apparently in slime form, her mind regresses back into an instinctive state, and it's hard to keep herself in check."
                            + "<br><br>"
                            + "Blushing a bit, Airi thanks you again for helping her out, as she was about to revert back into slime form again, and promises to keep in contact.");
            Global.gui().choose(this, "Leave");
        } else if (choice.equals("Sex")) {
            Global.gui().loadPortrait("airi_mostly_slime.jpg");
            Global.gui().showPortrait();
            Global.gui().message(Global.format("You know that Airi, while appearing kind of shy at first, has an incredible appetite for sex thanks to her condition. You try broaching the subject with her, hoping that she can help you with the boner that's been developing "
                            + "ever since the cute girl greated you. You're not sure if she's conscious of it, but every time she walks past you, some part of her brushes against <i>that</i> part, successfully teasing you to full mast. "
                            + "Hoping to cue her in, you press her dainty hands against your crotch. Airi's completely lack of surprise confirms that she's been doing it on purpose. Giving you a small smile, she skillfully reaches into your pants and starts "
                            + "stroking your hard shaft. You moan into her mouth as you kiss her needily, feeling your spunk rapidly boiling in your balls. Airi smiles at you and asks <i>\"Ready for something special?\"</i>"
                            + "<br/><br/>"
                            + "Suddenly you feel the sensation in your pants change. Looking down, you find a slimey blue onahole covering your cock. Airi somehow partially transformed her arm into a gooey sex toy and is now fucking "
                            + "you with it. At this rate, you're going to quickly lose control. With a guttural roar, you throw the giggling asian on her sofa and press yourself on top of her. As you rip off her panties, a wet sensation on your dick "
                            + "alerts you to the fact that she never retracted her slime onahole. In fact, the blue organic faux sex toy is not even attached to her anymore! You try to take it off, but your fingers just pass through the barely solid goo. "
                            + "In return though, the slippery sex sleeve continues massaging your shaft, quickly bringing you to the edge. "
                            + "<br/><br/>"
                            + "Not willing to admit defeat without even making it inside her, you endure the pleasant sensations and push your slime covered cock between her legs. You somehow miss her entrance though. Confused, you feel around for her pussy. "
                            + "It... is not there. Looking between Airi's legs, you find that the mischevious girl had sealed up her vagina, with her delta looking pretty much like the smooth mound of a barbie doll. At this point, Airi bursts out laughing, "
                            + "<i>\"Sorry {other:NAME}, you looked so cute I couldn't help teasing you a bit...\"</i> You glare at her, and she smiles apologetically. <i>\"I admit that was a bit mean. As an apology, I'll give you some service.\"</i>"
                            + "<br/><br/>"
                            + "Airi's body seems to melt under you. You find yourself falling <i>into</i> her body and her slime wraps around you. A familiar beautiful but translucent face forms in front of you, peppering your lips with a quick kiss. "
                            + "<i>\"{other:NAME}... how about... a massage...\"</i> Airi's slime starts flowing around you, rubbing your muscles and releaving aches that you didn't even know you had. It feels really really good, maybe even better than sex "
                            + "now with all the action you've been getting every night. You should have know though that Airi wouldn't be content leaving your dick alone. A gentle pressure laps at your hardness traped inside her, leading you to one of the most "
                            + "gentle climaxes you've had. You relax. You know that Airi will take good care of you, so you just let her do her thing."
                            + "<br/><br/>"
                            + "The time goes by all too quickly, and Airi finally releases you from her body. You almost fall when you try getting up; your muscles are so relaxed that you could barely move at first. Airi, now back in human form, reaches up on her tiptoes "
                            + "and gives you a sweet kiss. <i>\"That was fun, and the fluids really helped me out. Uhm... let's do it again soon okay?\"</i>", Global.getCharacterByType("Airi"), Global.getPlayer()));
            Daytime.train(player, npc, Attribute.Seduction);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            if (Global.random(5) == 0 && (!player.has(Trait.Clingy) || !npc.has(Trait.Clingy))) {
                Global.gui().message("You feel like you learned a lot about holding on to someone. Maybe you can apply this in the games?");
                player.add(Trait.Clingy);
                npc.add(Trait.Clingy);
            }
            Global.gui().choose(this, "Leave");
        } else if (choice.equals("Sparring")) {
            Global.gui().loadPortrait("airi_mostly_slime.jpg");
            Global.gui().showPortrait();
            Global.gui().message(Global.format("You suggest doing some practice to Airi, seeing how she's not incredibly physically fit. You tell her that being a bit stronger will help her in the night games when she doesn't make use of her slime form. Airi seems a bit hesistant at first but agrees when "
                            + "you promise her an nice date at the nice french resturaunt near campus if she can win once. It turns out sitting in the lab every day does not make her very fast nor very strong. You easily wrestle her to the ground every time. Airi tries everything from kicking you when you "
                            + "approach, slipping out from your hold, and slapping your privates when she gets the chance, but there's no overcoming the basic difference in fitness. Airi looks visibly frustrated, but urges you to continue. Looks like she has a bit of a competitive streak like Jewel."
                            + "<br/><br/>"
                            + "As you force her to land on the mat for the umpteenth time, something in her seems to snap. Airi smiles at you strangely and tells you to come at her one last time. Even you can tell her stance is suspicious, but if you walked away now it would look like you just quit after "
                            + "being ahead. You charge at here once more, planning on lightly tackling her to the ground again. However, this time Airi doesn't even try to dodge. Unprepared for this, you ram into her and unexpectedly sink into her rapidly melting body. You shoot an accusatory look at Airi for \"cheating\", "
                            + "but she just smiles at you and answers in her slow whispering voice, <i>\"...thank you for your instruction... but everyone... has their own strengths... right..? Now that I've got you... punishment time...\"</i> You don't remember agreeing to this!"
                            + "<br/><br/>"
                            + "You groan as you feel tendrils of her goo tickle your rear entrance. She's not thinking of... crap. You try looking away to hide your expression, but blue hands formed out of her slime force your head back into looking straight at her ruby red eyes. "
                            + "She's clearly still angry at you. You try struggling, but its of no use. Even though you're probably stronger than her, her slime body simply flows around you when you try exerting any force. You can only stare back at her cold gaze as her slime starts pressing at your prostate. "
                            + "With a gentle but instistent force, Airi prods and squeezes at your insides, gradually forcing unwanted pleasure into you. You groan as your cock finally leaks its white fluid into her body."
                            + "<br/><br/>"
                            + "After you cum, Airi finally lets you go and returns to human form. She smiles at you triumphantly and asks, <i>\"Looks like I won this time... Want to go again? Double or nothing... No more transforming, I promise.\"</i> You're not sure you can trust her promise, but the desire to get back at her cheating "
                            + "gets the better of you and you agree. Facing off one last time, you try the same tactics again and rush towards her. Suddenly, you feel a pressure at your prostate again and are forcibly made to cum again before you even reach Airi. Falling at her feet, you can only shoot your sperm into the air as the unexpected "
                            + "pleasure forces you to your knees. It looks like Airi never actually took out the slime that invaded your ass, and simply hid it inside you. The petite asian walks over to you and toys with your erection a bit with her feet. <i>\"Looks like there's many ways to win the same fight,\"</i> she giggles. "
                            + "Defeated, you can only sigh in resignation. Looks like you have a couple of expensive dates soon.", Global.getCharacterByType("Airi"), Global.getPlayer()));
            Daytime.train(player, npc, Attribute.Power);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            if (Global.random(5) == 0 && (!player.has(Trait.fakeout) || !npc.has(Trait.fakeout))) {
                Global.gui().message("You feel like you learned a lot about holding on to someone. Maybe you can apply this in the games?");
                player.add(Trait.fakeout);
                npc.add(Trait.fakeout);
            }
            Global.gui().choose(this, "Leave");
        } else if (choice.equals("Games")) {
            Global.gui().loadPortrait("airi_human.jpg");
            Global.gui().showPortrait();
            Global.gui().message(Global.format("[Placeholder]You play monster hunter with Airi.", Global.getCharacterByType("Airi"), Global.getPlayer()));
            Daytime.train(player, npc, Attribute.Cunning);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            if (Global.random(5) == 0 && (!player.has(Trait.fakeout) || !npc.has(Trait.fakeout))) {
                Global.gui().message("You feel like you learned a lot about holding on to someone. Maybe you can apply this in the games?");
                player.add(Trait.fakeout);
                npc.add(Trait.fakeout);
            }
            Global.gui().choose(this, "Leave");
        } else {
            done(true);
        }
    }
    
    @Override
    public Optional<String> getAddictionOption() {
        return Optional.empty();
    }
}
