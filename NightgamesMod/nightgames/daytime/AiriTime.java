package nightgames.daytime;

import java.util.Optional;

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
        advTrait = Trait.slime;
        transformationIntro =
                        "[Placeholder]<br>Nothing here to see.";
        loveIntro = "";
        transformationFlag = "";
    }

    @Override
    public void buildTransformationPool() {
    }

    @Override
    public void subVisitIntro(String choice) {
        if (npc.getAffection(player) > 0) {
            Global.gui()
                  .message("");
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
                            + "You glance up at the demure asian girl, and you immediately notice something wrong. Airi has collapsed on her table with her face flushed and her skin moist with sweat."
                            + "Throwing your caution to the winds, you rush over to her and ask her if she's OK.");
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            Global.gui().choose(this, "Next");
            return;
        }
        Global.gui()
              .choose(this, "Leave");
    }

    @Override
    public void subVisit(String choice) {
        if (choice.equals("Next")) {
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
                            + "That leaves her the choices of either becoming a cannibal... or having a lot of sex. It's pretty clear what she chose. "
                            + "<br><br>"
                            + "Blushing a bit, Airi thanks you again for helping her out, as she was about to revert back into slime form again, and promises to keep in contact.");
            Global.gui().choose(this, "Leave");
        } else if (choice.equals("Leave")) {
            done(true);
        }
    }
    
    @Override
    public Optional<String> getAddictionOption() {
        return Optional.empty();
    }
}
