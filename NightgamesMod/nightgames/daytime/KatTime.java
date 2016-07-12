package nightgames.daytime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.ModdedCockPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.requirement.BodyPartRequirement;
import nightgames.requirement.NotRequirement;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

import static nightgames.requirement.RequirementShortcuts.bodypart;
import static nightgames.requirement.RequirementShortcuts.not;

public class KatTime extends BaseNPCTime {
    public KatTime(Character player) {
        super(player, Global.getNPC("Kat"));
        knownFlag = "Kat";
        giftedString = "\"Awww thanks!\"";
        giftString = "\"A present? That was unexpected nya\"";
        transformationOptionString = "Transformations";
        loveIntro = "[Placeholder]<br>You text Kat and she excitedly meets up with you.";
        transformationIntro = "[Placeholder]<br>Kat shares how her totemic magic works.";
        advTrait = null;
        transformationFlag = "";
    }

    @Override
    public void buildTransformationPool() {
        options = new ArrayList<>();
        TransformationOption primalCock = new TransformationOption();
        primalCock.ingredients.put(Item.PriapusDraft, 10);
        primalCock.ingredients.put(Item.Rope, 10);
        primalCock.ingredients.put(Item.Aphrodisiac, 50);
        primalCock.requirements.add(new BodyPartRequirement("cock"));
        primalCock.requirements.add((c, self, other) -> {
            return self.body.get("cock").stream().anyMatch(cock -> ((CockPart) cock).isGeneric(self));
        });
        primalCock.additionalRequirements = "A normal cock";
        primalCock.option = "Primal Cock";
        primalCock.scene = "[Placeholder]<br>Kat uses her totemic magic to convert your penis into a primal cock.";
        primalCock.effect = (c, self, other) -> {
            Optional<BodyPart> optPart =
                            self.body.get("cock").stream().filter(cock -> ((CockPart) cock).isGeneric(self)).findAny();
            BasicCockPart target = (BasicCockPart) optPart.get();
            self.body.remove(target);
            self.body.add(new ModdedCockPart(target, CockMod.primal));
            return true;
        };

        options.add(primalCock);
        TransformationOption feralPussy = new TransformationOption();
        feralPussy.ingredients.put(Item.Rope, 10);
        feralPussy.ingredients.put(Item.Aphrodisiac, 50);
        feralPussy.ingredients.put(Item.FemDraft, 10);
        feralPussy.requirements.add(new BodyPartRequirement("pussy"));
        feralPussy.requirements.add((c, self, other) -> {
            return self.body.get("pussy").stream().anyMatch(pussy -> pussy == PussyPart.normal);
        });
        feralPussy.option = "Feral Pussy";
        feralPussy.scene = "[Placeholder]<br>Kat uses her totemic magic to convert your pussy into a feral one.";
        feralPussy.effect = (c, self, other) -> {
            self.body.addReplace(PussyPart.feral, 1);
            return true;
        };
        feralPussy.additionalRequirements = "A normal pussy";
        options.add(feralPussy);
        TransformationOption catTail = new TransformationOption();
        catTail.ingredients.put(Item.Rope, 10);
        catTail.ingredients.put(Item.Aphrodisiac, 50);
        catTail.requirements.add(not(bodypart("tail")));
        catTail.requirements.add((c, self, other) -> {
            return self.body.get("tail").stream().anyMatch(part -> part != TailPart.cat) || !self.body.has("tail");
        });
        catTail.option = "Cat Tail";
        catTail.scene = "[Placeholder]<br>Kat uses her totemic magic to grow you a cat tail.";
        catTail.effect = (c, self, other) -> {
            self.body.addReplace(TailPart.cat, 1);
            return true;
        };
        options.add(catTail);
        TransformationOption catEars = new TransformationOption();
        catEars.ingredients.put(Item.Rope, 10);
        catEars.ingredients.put(Item.Aphrodisiac, 50);
        catEars.requirements.add(new BodyPartRequirement("ears"));
        catEars.requirements.add((c, self, other) -> {
            return self.body.get("ears").stream().anyMatch(part -> part != EarPart.cat) || !self.body.has("ears");
        });
        catEars.option = "Cat Ears";
        catEars.scene = "[Placeholder]<br>Kat uses her totemic magic to grow you cat ears.";
        catEars.effect = (c, self, other) -> {
            self.body.addReplace(EarPart.cat, 1);
            return true;
        };
        options.add(catEars);
    }

    @Override
    public void subVisitIntro(String choice) {
        if (npc.getAffection(player) > 0) {
            Global.gui().message(
                            "You send Kat a text to see if she's free. Since exchanging numbers with her, you've discovered that she's much more outgoing "
                                            + "when texting than she is in person. The two of you have chatted quite a bit, you just hope she'll eventually get more used to talking with you in "
                                            + "person.<br>You quickly receive a reply from Kat. 'i'm free right now. :) do you want to meet up?' You text her back, asking if there's a place you "
                                            + "can meet without her friends coming after you. 'i'm not with Mel and Emma right now. you can come here' About ten seconds later, she sends you a followup. "
                                            + "'please don't hate them. they're my best friends and they mean well. they just think i'm still an innocent virgin who needs to be protected.' To be "
                                            + "fair, she does inspire inspire that sort of protective attitude, even from her opponents. For Kat's sake, you'll do your best to get along with them, but "
                                            + "they may not be as agreeable, especially if they find out you're having sex with their protegée.<p>On your way to Kat's room, you get another text. "
                                            + "'i think i'm too excited waiting for you to get here. what are you planning?'");
            Global.gui().choose(this, "Games");
            Global.gui().choose(this, "Sparring");
            Global.gui().choose(this, "Sex");
            if (Global.checkFlag(Flag.metAisha) && !Global.checkFlag(Flag.catspirit)
                            && Global.getNPC("Kat").getAffection(player) >= 5) {
                Global.gui().choose(this, "Ask about Animal Spirit");
            }
            if (Global.getPlayer().checkAddiction(AddictionType.BREEDER)) {
                Global.gui().choose(this, "Must... Fuck...");
            }
        } else if (Global.getPlayer().checkAddiction(AddictionType.BREEDER)) {
            Global.gui()
            .message("Reyka low-affection corruption intro");
            if (npc.getAttraction(player) < 15) {
                npc.gainAttraction(player, 2); 
                player.gainAttraction(npc, 2);
            } else {
                npc.gainAffection(player, 1);
                player.gainAffection(npc, 1);
                Global.gui() .choose(this, "Games");
                Global.gui().choose(this, "Sparring");
                Global.gui().choose(this, "Sex");
            }
            Global.gui().choose(this, "Must... Fuck...");
        } else if (npc.getAttraction(player) < 10) {
            Global.gui().message(
                            "You decide to look for Kat and see if she's interested in spending some time together. You don't have any way to contact her directly, "
                                            + "but she apparently spends a lot of time in the campus gardens. That's probably your best hope for running into her.<p>You eventually spot Kat walking "
                                            + "through the gardens, but you almost don't recognize her. Instead of the light, casual clothes she usually wears during a match, she's currently dressed "
                                            + "in an excessively baggy outfit. She's still pretty cute, but those clothes make her look like she's trying to hide her small frame. She's probably trying "
                                            + "to hide something else, though. At night, she doesn't have to worry about people seeing her cat ears and tail, but in her day-to-day life, it's got to be "
                                            + "a real inconvenience. To her credit, you don't see any obvious bulge in her pants where her tail should be. It would be interesting to see how she "
                                            + "maneuvers her tail to keep it from showing. It also sounds like a good excuse to get her pants off. As for her ears, she has them hidden under a cloth "
                                            + "hat... that has cat ears on it. She's wearing a cat ear hat over her actual cat ears. Does that qualify as hiding in plain sight? In her hand, she's "
                                            + "carrying a paperback book that she's either just been reading or is looking for a place to read.<p>You call out to Kat and for a moment, she looks like "
                                            + "a small, startled animal. She looks noticeably relieved when she sees you. That's probably a good sign. Before the two of you have a chance to talk, someone "
                                            + "else calls out to Kat. Two girls insert themselves between you and Kat. Were they with her? You didn't even notice them. One of the girls puts an arm around "
                                            + "Kat's shoulder and leads her away. <i>\"Sorry, we're in a bit of a hurry.\"</i> Kat looks like she wants to say something, but can't get the words out.<p>After they"
                                            + "leave, the other girl gives you a smile with no goodwill. <i>\"I know. That girl's super cute, isn't she? Unfortunately, you'll need to find another girl to hit "
                                            + "on. She doesn't have any experience with men and isn't very good at asserting herself, so we can't let anyone take advantage of her.\"</i><p>Ugh. This is inconvenient. "
                                            + "You can't very well explain that you're already intimately acquainted with Kat. You'll need to find another opportunity to approach her.");
            npc.gainAttraction(player, 2);
            player.gainAttraction(npc, 2);
        } else {
            Global.gui().message(
                            "You head out to the campus gardens, hoping to find Kat so you can spend some time together. You aren't searching long before you find her reading "
                                            + "a book in the shade of a tree. She seems pretty absorbed in the book, so you're hesitant to disturb her. Instead of calling out to her, you just sit next to her "
                                            + "quietly. You don't recognize the book she's reading, but judging by the cover, it appears to be an urban fantasy romance novel. It must pretty engaging, because "
                                            + "Kat still hasn't noticed that you've been sitting with her for several minutes.<p>When Kat finally perceives your presence, she lets out a startled yelp and jumps "
                                            + "to her feet. You weren't expecting her to be that surprised. You give her a relaxed smile and a greeting. Before Kat can reply, you hear an angry voice behind you. "
                                            + "<i>\"What the hell are you doing to my best friend?!\"</i> A girl with curly red hair glares at you and hugs Kat protectively. Another girl, a brunette, looks calmer, but "
                                            + "also stands between you and Kat. This is bad. You quickly explain that you weren't trying to scare Kat, you just wanted to talk to her. <p><i>\"Our Kat is pretty delicate. "
                                            + "Maybe you should learn how to approach a girl without scaring her before you try to pick her up.\"</i> The brunette speaks in a reasonable tone, but there's a definite "
                                            + "edge to her voice. The redhead snorts and starts to lead Kat away. <br><i>\"I don't want this creep talking to Kat at all.\"</i> Kat tugs on the girl's sleeve to stop her and "
                                            + "whispers something in her ear. The girl looks back at you shocked. <i>\"This guy? Are you kidding me?\"</i> The brunette joins the two of them and they enter a brief huddle. "
                                            + "You stand there awkwardly, unable to hear their conversation. There are more than a few glances in your direction, and Kat's face is gradually turning red. <br>Eventually, "
                                            + "Kat leaves the huddle to stand behind you, as if hiding from her friends. The calmer of the two girls gives you an awkward smile. <i>\"We'll give you two some space.\"</i> "
                                            + "She has to practically drag away the other girl, who is glaring daggers at you."
                                            + "Kat doesn't make eye contact, but shyly touches your hand. <i>\"Will you come to my room for a little while?\"</i> she softly asks.<p>She's quiet all the way to her room "
                                            + "and that doesn't change after you arrive. You sit next to each other in silence on her bed. She's leaning against you slightly, but is looking away, so you can't see "
                                            + "her expression. You'll probably have to be the one to initiate conversation, but you aren't sure how to start. To your surprise Kat is the one to break the silence. "
                                            + "She takes your hand and gently moves it to her groin. <i>\"C-can you touch me? Just a little bit....\"</i> Her face it beet red and she can't look you in the eye, but that "
                                            + "was way more forward than you were expecting. You don't mind, of course.<p>You pull down Kat's pants and slip your hand into her underwear. Under your skilled touch, "
                                            + "her nethers quickly start to get wet. You gently lay her down on the bed as she whimpers in pleasure. Her tail twitches uncontrollably the more you finger her. You "
                                            + "slide her panties down to get better access to her girl parts and she ends up kicking them off completely.<p>Just as you're about to finish Kat off, she grabs your "
                                            + "wrist to stop you. <i>\"Wait! If you keep that up, I'm gonnya cum and we'll be right back where we started.\"</i> She isn't interesting in climaxing, she just wants you to "
                                            + "leave her horny and unsatisfied? She squirms noticeably as she sits up on the bed. <i>\"It's really frustrating to stop nyow, but I need to be patient. I'm counting on "
                                            + "you to reward me when we're done.\"</i> She settles into a comfortable seated position on the bed, apparently not bothered that her naked lower half is visible. <i>\"We've "
                                            + "fought together a bunch, but since you're myaking an effort to get to know me, I wanted to explain how my animal spirit works. The Girl was too flustered about being "
                                            + "alone with a boy to talk properly, so I needed your help to bring out the Cat to do the talking.\"</i><p>"
                                            + "By arousing Kat, you brought out her animal side. So that's who "
                                            + "you're talking to now? <i>\"The urge to mate is a very primal thing. The more I feel it, the stronger the cat spirit gets, which improves my instinct and my reflexes. It's "
                                            + "nyat like a Jekyll and Hyde thing though. The Girl and the Cat have the same memories, same intelligence, same personality, and same interests. I'm still Kat, just "
                                            + "imyagine you've turned up the catliness level on a mixer.\"</i> She said they have the same personality, but she doesn't sound at all like Kat. Her manner of speech is "
                                            + "completely different and she obviously talks a lot more. <i>\"Nya~. The Cat is a wild animal, so it doesn't add much in the way of myannerisms or speech style. The "
                                            + "only real difference is that I'm free from a lot of human social inhibitions. I don't get shy talking to others and I don't feel shame, see?\"</i> She spreads her legs "
                                            + "to flash you a good look at her soaked privates. <i>\"This is how the Girl's inner monologue sounds, or to put it another way, this is how Kat would talk if she wasn't "
                                            + "shy. Oh, I also do a lot more meowing. That's totally the Cat's fault.\"</i><p>Ok, you get the general idea. You just aren't sure what to call her when she's like this. "
                                            + "<i>\"Nya! I told you, I'm still Kat. I have all the same thoughts and all the same desires I do normally.\"</i> She seductively pulls you close to her and whispers in your "
                                            + "ear. <i>\"I want you to fuck me, because the Girl wants you to fuck her. I'm just nyot embarrassed to say it. Speaking of which....\"</i> She lies back on the bed and spreads "
                                            + "her legs open for you. <i>\"I've been patient for awhile. I think I deserve a reward.\"</i> You have some more questions, but you can't simply turn down Kat when she's asking "
                                            + "in such a lewd pose. You kneel between her legs and start to eat her out. She mews in pleasure and writhes on the bed. She's already very turned on, so don't need "
                                            + "to lick her clit very long to make her orgasm. She arches her back and lets out a breathless moan as she hits her peak.<p>You sit next to Kat and gently stroke her "
                                            + "head while you wait for her to recover. After she catches her breath, she blushes furiously and scrambles to retrieve her discarded panties. You enjoy the view of her "
                                            + "cute butt until she finishes putting her underwear back on. She sits down on the bed next to you and gives you a light kiss on the cheek. <i>\"Thanks.\"</i> She smiles shyly "
                                            + "and looks away. <i>\"That felt really good.\"</i> She holds her phone out to you. <i>\"Can we exchange numbers? You know... so we can train together?\"</i> You add yourself to her "
                                            + "contacts and return the phone. She sends a short text to you. She seems a lot less awkward now, but she still acts so innocent. It's still hard to believe this is the "
                                            + "same girl that was asking you to fuck her just minutes ago. You look at the text she sent you: 'i meant everything i said &#60;3.'");
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        }
        Global.gui().choose(this, "Leave");
    }

    @Override
    public void subVisit(String choice) {
        if (choice.equals("Must... Fuck...")) {
            if (npc.getAffection(player) == 0) {
                Global.gui().message("Kat teases you, but eventually you end up fucking like, well, animals. No"
                                + " surprise there. She also bites you again, which in the end just makes matters worse."
                                + " This is a placeholder.");
            } else {
                Global.gui().message("Your cock sits uncomfortably hard in your pants as you clear your throat. You NEED "
                                + "to fuck her, but you can't just say that, can you? Kat seems to see right through you, "
                                + "though. <i>\"Oh boy, I recognize that look. Pins and needles all over? Slight tunnel "
                                + "vision? Tense muscles? Increased saliva productionya? Enhanced sense of touch and smell? "
                                + "Overactive libido?\"</i> Correct on all counts. <i>\"Yes! It worked!\"</i> You know Kat"
                                + " would never hurt you, but her enthusiasm is still a little unsettling. You ask her to "
                                + "explain. <i>\"Oh, well. It's just... You know, secretly we all want to let the animal "
                                + "within us out from time to time, nya? I seem to bring those feelings a little more to the"
                                + " surface.\"</i> Giving her an askance glance, you ask whether it has anything to do with"
                                + " her newfound hobby of biting you, and in response she flushes redder than a strawberry."
                                + " A cute strawberry. <i>\"Well, the animal spirit which fused with me might carry over a "
                                + "little into my saliva... I figured if I could get a little of it into your bloodstream, "
                                + "it would make things easier for me. You know, get you going, nya? I didn't expect this, "
                                + "but... You seem to have gone into a rut! That's... Okay, right? I mean, I didn't mean any "
                                + "harm... \"</i> She looks absolutely terrified, looking down and to the side, voice dwindling "
                                + "to barely a whisper. You quickly reassure her that you aren't mad. <i>\"Oh, thank you! I "
                                + "was worried that you... That you'd feel... Anyway, I have some experience with this stuff,"
                                + " so I know how you must be feeling. Trust me, try as you might, it won't go away. Not until"
                                + " you, uhm... You know...\"</i> Before she even has a chance to flush over, you hook a"
                                + " finger under her chin and lean down for a kiss. When you break it and lean back, all the"
                                + " nerves have fled Kat's face. In their place you see a look which must be a mirror of your "
                                + "own; one filled with desire and need. You catch yourself almost growling as you snatch her "
                                + "hand and start to lead her to your room.\n\nKat jumps on your back the moment you unlock the"
                                + " door, and you awkwardly stumble in, trying to keep your balance. She sheds her shoes, and "
                                + "then uses her feet to pull your pants and underwear down in one go. <i>\"No reason to wait, "
                                + "nya? Might as well get a little practice in.\"</i> Oh, she wants a match, does she? Well, that"
                                + " can be arranged. You reach back to dislodge her, but that only allows her to hook her arms"
                                + " around yours, pinning them in place. Her feet, meanwhile, wrap themselves around your dick"
                                + " and start giving you a footjob. You could always crush her against a wall or something, "
                                + "but you don't want to hurt her, however devious a cat she may be. Instead, you stagger over"
                                + " to your bed and allow yourself to fall onto it sideways. The impact frees your arms, and "
                                + "you quickly wrench her legs away. Kat quickly grabs you again, but not before you've rolled"
                                + " over. Now pressed together face-to-face, you kiss her again and start rubbing her nipples."
                                + " In turn, she grinds your rod between her thighs. Her pheromones are wafting up to your face"
                                + " now, and that's certainly not going to help your staying power. You'll have to finish"
                                + " matters quickly. Using brute force, more than you thought you had, you force her onto her "
                                + "stomach and climb on top. After plying her legs open with your own and seizing her slender "
                                + "wrists in one hand, you use the other to line up your cock. This time you can't hold back the "
                                + "growl as you plunge into her. That only seems to arouse Kat more, and she starts bucking "
                                + "underneath you. You pull her up to her knees and continue to pound her hard and maul her "
                                + "nipples. It's not long before she unleashes a flood of pheromones in orgasm, which seem to go"
                                + " straight to your cock. You try to hold out for a second one, but you're already getting "
                                + "really close. Kat senses the irregularity of your thrusts. <i>\"Yes! Nya! Cum in me!\"</i> "
                                + "It would be rude to refuse a lady. With a final, mighty thrust, you let loose inside of her"
                                + " at the same time her second orgasm starts. You writhe against each other in ecstasy for almost"
                                + " a full minute before settling down. Strangely, however, you do not feel the lethargy that would"
                                + " normally wash over you after such a glorious climax. In fact, your dick is still ad hard as "
                                + "ever. <i>\"Surprised? That was fantastic, but once just won't do!\"</i> Agreed. The second "
                                + "round is slower-paced, more intimate. And the third, and fourth. Each time, you cum a lot "
                                + "sooner than normal, but Kat tells you that that's normal. You're not about to complain. By "
                                + "the time you do finish, it's because every other part of you is giving out. That said, you don't"
                                + " feel nearly as hot as before. You and Kat share a drink before she warmly kisses you goodbye. "
                                + "If you're not really careful, you're going to get addicted to this for sure. But with sex like"
                                + " that, is that a bad thing?");
            }
            Global.gui().choose(this, "Leave");
            Global.getPlayer().addict(AddictionType.BREEDER, npc, Addiction.MED_INCREASE);
            Global.getPlayer().getAddiction(AddictionType.BREEDER).flagDaytime();
        }
        if (choice.equals("Sex")) {
            Global.gui().message(
                            "Kat sits on her bed and looks at you hesitantly, with red cheeks. <i>\"Are we going to... you know?\"</i> Despite her shy appearance, there's definitely "
                                            + "an eagerness to her voice. You both want the same thing. You give her a quick kiss on the lips and help her remove her shirt. She shyly crosses her arms over her bra "
                                            + "and smiles weakly. <i>\"It's embarrassing if I'm the only one who is naked. Take off your shirt too.\"</i> You obligingly strip of your own top and she helps you remove "
                                            + "her bra. Her breasts are quite big and soft looking, considering her petite build. If she didn't cover up her body with baggy clothing during the day, her friends would "
                                            + "surely need to beat guys off of her left and right. Kat turns even redder when she catches you staring and covers her breasts. <i>\"D-don't stare at my boobs so much. It's "
                                            + "your turn to undress.\"</i> Of course, she deserves a little eye candy too. You kick off your pants, leaving only your boxers. Kat hesitantly takes her hands off her chest "
                                            + "so she can remove her pants. <p>"
                                            + "You're both down to your underwear and Kat stares at your boxers in anticipation. She's obviously ready for some passionate sex, but you "
                                            + "can't resist teasing her some more. You sit down on the bed next to her and push her gently onto her back. She looks confused about this sudden change in plan. You explain "
                                            + "that you came here for sexual training, not simply to have sex. Therefore there's no reason to take your boxers off right now. In one smooth motion, you pull down her panties "
                                            + "and expose her wet flower. She yelps in surprise and embarrassment and covers herself with her hands. You're going to start by training her self control. Kat gets taken over "
                                            + "by her animal spirit too easily when she's aroused. You're going to help her learn to contain it by fingering her while she tries to maintain her control. <p>"
                                            + "She looks a bit "
                                            + "dubious, but accepts your idea. She closes her eyes, takes a deep breath, and moves her hands away from her groin. You lightly move your finger up and down her slit, eliciting a soft whimper. "
                                            + "If you're going to do this properly, you need better access to her girl parts. She flushes in shame, but obediently spreads her legs at your prompting. You reposition yourself "
                                            + "to sit between her spread legs and start to slowly rub her lower lips. Kat squirms a bit, but focuses on controlling her breathing. You intensify your fingerwork a bit, but "
                                            + "she manages to maintain her composure, showing no sign of catliness. You notice that her hands are balled into fists, gripping the bedsheets tightly. Her earnestness in this "
                                            + "silly exercise you made up is charming. You lean down and lightly lick her inner thigh. She jerks as if shocked and lets out a moan. <i>\"Tongue is unfair! I can barely handle "
                                            + "the fingers!\"</i> She really is taking this seriously. You were planning to eat her out as a reward, but you decide instead to continue the exercise until she wants to stop. "
                                            + "<p>She's doing a very good job keeping her arousal in check. You've been fingering her long enough that your wrist is getting sore. You give her a brief respite while you move "
                                            + "to sit behind her. She's a bit startled when you pull her into your lap to lean against your chest. From this new position, you slip your hand between her legs and resume "
                                            + "fingering her. She lets out breathy moans and shivers in your arms. Within a handful of seconds, you feel her tail twitching against you leg and her moans have a clear mewing "
                                            + "quality. <p>"
                                            + "She was doing pretty well up until now. <i>\"I've been in the games long enyough to endure being fingered if I focus, but being held by a guy... and the "
                                            + "warmth... and the breath on my neck! How is a girl suppose to handle that?\"</i> She squirms out of your arms and turns to face you. <i>\"I know this 'training' was just an "
                                            + "excuse to tease me. I didn't complain because I liked what you were doing, but it's your turn nyow. Either get those boxers off, or they're gonnya be shredded.\"</i> You "
                                            + "quickly strip off your underwear. These are your good boxers, not like the cheap throwaway pairs that you wear during a match. Kat gives an approving purr as she looks over"
                                            + "your throbbing erection. She straddles your waist and lowers herself onto your member. She was talking about teasing you before, but apparently she's too impatient. She "
                                            + "rocks her hips, sliding her slick folds up and down your cock. You kiss her passionately and pull her body against yours. You're both extremely horny since you've been fooling "
                                            + "around for quite awhile. Kat moves her hips energetically as you thrust into her from below. Her tail whips back and forth with excitement and she moans softly into "
                                            + "your mouth. She has to break the kiss to breathe and buries her face in your neck. <i>\"It feels really good! Nya! I'm gonnya cum!\"</i> Her timing is pretty good; you're "
                                            + "about to cum too. She clings to you and digs her nails into your back as she shudders in orgasm. Her pussy clenches your cock and you erupt inside her. <p>"
                                            + "She slumps completely "
                                            + "limp into your arms, eyes closed. You hold her and gently stroke her head. You pet her for a little while before she opens her eyes with a sleepy smile. <i>\"You're still inside "
                                            + "me.... It's a nice feeling.\"</i> You lay her gently onto the bed and she hugs you tightly to make you aren't going anywhere. She quickly falls asleep, cuddling up against you. "
                                            + "You pull a blanket over your naked bodies and close your eyes. You were supposed to be training, but a quick nap suddenly seems very inviting.");
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Seduction);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Games")) {
            Global.gui().message(
                            "Despite your shared intimacy, Kat still has trouble speaking normally when you're alone together. You've turned to games as, not just a form of "
                                            + "strategy training, but also a means of getting her to relax. As you get deep into the game, it seems to be working. It probably helps that she seems to have a "
                                            + "knack for this game. She's practically bouncing in her seat and shadowboxing each time she crashes a gem. <p>"
                                            + "<i>\"You have no chance against my kitty cat attacks.\"</i> "
                                            + "You probably shouldn't point out that her character is suppose to have a fox motif, rather than a cat. Her cute antics are helping you overlook how badly you're "
                                            + "losing. You've had to focus most of your efforts on defense, so you've been falling behind on buying good chips. On Kat's turn she plays 'Speed of the Fox' (which "
                                            + "she continues to call 'Speed of the Cat') and several 'Combines', creating a pair of level 4 gems out of the level 2 gems she's been holding onto. That puts an "
                                            + "end to your defensive strategy, level 4 gems can't be countered.<p>"
                                            + "Kat sends the entire eight stack of gems crashing towards you and shoots her fist into the air in an enthusiastic pose. <i>\"Super Cat Punch!\"</i> Oh God, she's "
                                            + "just too damn cute! You can't resist tackling Kat and hugging her tightly, the game completely forgotten. She doesn't protest or try to resist, but you do notice "
                                            + "a distinctively dissatisfied look on her face. Does she not like being held like this? <i>\"I don't mind this sort of thing. It happens to me a lot. Aisha glomps me like this "
                                            + "almost every time I see her. It feels especially nice being held by you, but... I was winning....\"</i> Oh right, the game. The odds of you surviving another turn "
                                            + "after her Super Cat Punch is pretty low. Even if you did recover, she still has a significant advantage. It seems fair to concede defeat now and skip to some more "
                                            + "intimate fun. Kat pouts, clearly displeased at your lack of motivation. If she has her heart set on finishing the game instead of sexy times, you'll try to be "
                                            + "patient. She blushes and squirms a bit in your arms. <i>\"I'm not turning down down sexy times. Just remember that I won, so you should obey me today. You're not "
                                            + "allowed to tease me.\"</i> You don't really tease her that much, do you? Well, fair enough. She calls the shots this time. Kat looks you over and smiles. <i>\"First, take off "
                                            + "your clothes. Today, you get to be naked and embarrassed instead of me.\"</i> You obediently strip naked, but you aren't actually that embarrassed to be naked in front "
                                            + "of Kat. She cuddles up against you and starts to fondle your dick with both hands. She's pretty good at this, probably due to her experience in the games. She strokes "
                                            + "and pleasures you until your precum starts to leak out onto her fingers.<p>"
                                            + "<i>\"That's all for now,\"</i> Kat says, as she abruptly takes her hands off your penis. <i>\"The rest will be a reward if you service me properly.\"</i> You groan in "
                                            + "frustration at Kat's uncharacteristic cocktease. Well, you did agree to listen to her for now. You'll surely earn more than a handjob with your expert cunnilingus skills. "
                                            + "You reach down to remove Kat's pants, but she suddenly covers your eyes with her hands. <br>"
                                            + "<i>\"I know it's silly after all this time, but I still get embarrassed when a someone stares at my privates. No peeking, OK?\"</i> This has become slightly inconvenient. "
                                            + "You'll have to rely on your sense of touch. You clumsily remove her pants and underwear and quickly locate her vulva with your tongue. Kat shivers as you start to eat her "
                                            + "out and seems to be having trouble keeping her hands over your eyes. <i>\"Keep-Nya!~ Keep your eyes closed. You're doiNya!~ just fine without looking.\"</i><p>"
                                            + "You're glad she's enjoying your efforts, but you do wish you could look at her. It's not just so you can see what you're doing, but also to watch her reactions. She said "
                                            + "'no peeking', so you'll just have to harden your resolve and resist temptation. At least you can enjoy her moans, which are gradually becoming more and more feline. <br><i>\"You're "
                                            + "still nyat peeking?\"</i> You assure her that your eyes are still tightly shut. To your surprise, she lets out a low sound of annoyance and pushes your head away. <i>Why are you "
                                            + "being so obedient?! You're gonnya make me cum too fast!\"</i> You open your eyes (she's obviously too cat-like to care about being seen) and look at her in confusion.<p>"
                                            + "<i>\"Can't you take a hint? I obviously only told you nyat to tease me so you'd want to do it more. I even teased you and gave you that dumb 'no peeking' rule to put the "
                                            + "idea in your head. You shouldn't just blindly do whatever I say.\"</i> She's pouting, but it just looks ridiculous with no pants on. <p>"
                                            + "If she's giving you the green light to tease her, you're definitely getting the urge to do so right now. You mercilessly tickle her naked inner thighs, paralyzing her with "
                                            + "squeals of laughter. No matter how cute she is, you're still going to punish her for being so unreasonable. How can she expect you to pick up on reverse psychology when she's "
                                            + "giving you orders? She can't demand that you obey her as a prize for winning and then demand the opposite of what she wants. You stop tickling her long enough for her to "
                                            + "answer you. <i>\"I couldn't just tell you that I like it when you tease and deny me. Who would freely admit that? I can only tell you now because I don't have any shame at "
                                            + "the moment. I'm going to be totally mortified when I turn back to normal.\"</i><p>"
                                            + "If she's really a masochist, she probably will enjoy being embarrassed. Well, if she wants you to deny her an orgasm, that's easy enough. She'll just have to wait until you "
                                            + "leave to handle her arousal. <i>\"Nya?! That's going too far! I just want you to play with me a bit, not leave before we're finished!\"</i> She looks pretty desperate. You're "
                                            + "still horny too, so maybe you'll reconsider her punishment if she begs convincingly enough. Kat shouldn't have any shame right now, but she still flushes at  your demand. "
                                            + "<i>\"Please! You can do whatever you want to me, just help me cum!\"</i><p>"
                                            + "You make her get on her hands and knees while you move behind her. You pull her tail out of the way, revealing her soaked pussy. Just as you thought, she's even more drenched "
                                            + "then when you were eating her out. You lightly stroke her lower lips, making her moan and shiver. When you slide a finger into her, her arms give out and her upper body drops to "
                                            + "the floor. <i>\"NYA! Nyat just your fingers! Aren't you horny too? Do it for real!\"</i><br>"
                                            + "You seem to recall her begging you to make her cum, doing whatever you wanted to her. Now she's saying your fingers aren't good enough for her? She should be more clear about "
                                            + "what she wants. She squirms in embarrassment and looks hesitant to speak. She's in full cat mode, so she can't actually be feeling shame. The blushing innocent bit must be part "
                                            + "of her libido. <i>\"I want... your... d-dick.... I want you to f-fuck me.\"</i> She can barely get the words out, but you feel an fresh flood of moisture from her entrance.<p>"
                                            + "She's earned a reward, and so has your unsatisfied cock. You line your rod up with her wet hole and thrust into her before she realizes what you're doing. She moans in pleasure "
                                            + "at the sensation of being completely filled. She's way beyond teasing, you might as well fuck her properly. You get a good grip on her waist and settle into a steady rhythm thrusting "
                                            + "in and out of her. Her tail thrashes wildly as she meows with complete abandon. You feel your ejaculation building quickly, but you're sure Kat's about to cum too, if she isn't "
                                            + "already mid-orgasm. A wave of pleasure washes over you as you shoot your load into her womb.<p>"
                                            + "As you recover from your orgasm, you notice that Kat is fast asleep, or possibly pretending to be asleep to avoid an embarrassing conversation. You're exhausted, but neither of "
                                            + "you are going to be comfortable napping on the floor. You pick up Kat's petite body and carry her over to the bed.");
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Cunning);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Sparring")) {
            Global.gui().message(
                            "You and Kat are able to find a private room with a wrestling mat so you can do some sparring without covering up her cat parts. You spend some time stretching and warming up together, "
                                            + "before you have to figure out how you're going to actually train. You're a lot taller and heavier than her, so an actual sparring match would be problematic. "
                                            + "Instead you offer to show her some simple wrestling takedowns. Kat looks visibly indignant, which is an expression you're not used to on her. <i>\"I have "
                                            + "been doing this for a year now. I should be teaching you techniques.\"</i><p>"
                                            + "Kat demonstrates a simple tackle to take your opponent off her feet. It's not "
                                            + "quite as smooth as when she's in her cat form, but it seems fairly effective. However, Kat is strong for her size and very quick. A big tackle is probably "
                                            + "not the most efficient technique against her. On the other hand.... Kat lets out a confused yelp as you grab her under the armpits and lift her off the floor. With her "
                                            + "small size and light frame, this is probably the simplest method to incapacitate her. She flails in humiliation at being treated like a kid. You forgot that "
                                            + "she's actually much stronger than she looks. Her thrashing manages to knock you off balance and you both fall to the mat, her landing heavily on top of you. "
                                            + "You get the wind knocked out of you, but you're more worried about Kat, who has her face buried in your chest and isn't moving. Is she ok? Did she land wrong? "
                                            + "As you're starting to panic, you see her tail wag lazily and hear a low meow.<p>"
                                            + "Wait, did she go full feline? You haven't done anything to arouse her. Can that "
                                            + "be triggered by physical trauma? Kat raises her head and looks at you with flushed cheeks. She certainly doesn't look traumatized. <i>\"Nya, sorry.... You "
                                            + "smell and feel really masculine. An innyocent girl like me can't help getting a little turned on from lying on top of you.\"</i> She purrs happily, hops "
                                            + "to her feet, and starts to strip. Why is she undressing? It's a good thing you've got a private room, otherwise you'd have a serious situation. <i>\"Nya! I'm "
                                            + "nyot stupid. Even if I accidentally get aroused in public, I wouldn't expose myself to just anyone. I may nyot get embarrassed, but I have enough common sense "
                                            + "to stay out of trouble.\"</i> She tosses her clothes aside and hops around energetically, making her breasts bounce attractively. <i>\"I thought of a fun way "
                                            + "to spar and get me back to nyormal at the same time. You just have to try to catch me and myake me cum, which won't be that easy with my cat instincts.\"</i> That "
                                            + "does sound more fun than just showing each other takedowns, though probably less practical. However, Kat is overestimating her ability to avoid you. She may "
                                            + "be quick, but this will be over soon. <p>"
                                            + "You take the initiative and lunge at Kat. She nimbly twists out of the way and jumps back to put some space between you. "
                                            + "That hop put her back to the wall, cutting off her escape vectors. You quickly corner her and rush in to finish her off. She kicks off from the wall and "
                                            + "manages to hop completely over your head. She's more agile than you realized, but it won't make a difference in the end. Kat dances away to the opposite corner, "
                                            + "but you take your time and approach her carefully. You slowly advance while she's still waiting for another rush. Soon she's backed away as far as she can and "
                                            + "can't stall any longer. She dashes past you, coming just slightly too close. You finally spring forward and manage to get an arm around her waist, pulling her "
                                            + "towards you as you both fall to the mat. Kat squirms in your arms, but you easily manage to mount her and finger her pussy. She's already wet and receptive to "
                                            + "your touch. This is why she couldn't get away. She wanted to be caught just as much as you wanted to catch her. She moans frantically, but doesn't really try "
                                            + "convincingly to escape. You quickly locate her clitoris and start rapidly rubbing it with your fingertips. Her back arches and she yowls in ecstasy as she orgasms. "
                                            + "You lightly caress her body as she's recovering from her climax. She giggles softly at the ticklish sensation and looks up at you with a flushed smile. <i>\"You're "
                                            + "good at this kind of sparring. Can I put my clothes on before we continue? This feels nice, but it's really embarrassing.\"</i>");
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Power);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.startsWith("Ask about Animal Spirit")) {
            Global.flag(Flag.catspirit);
            Global.gui().message("You know that Kat's power comes from an animal spirit "
                            + "inside her, but she never mentioned how that came to be. It seems like "
                            + "that might be a useful ability, so you decide to ask her about it. <i>"
                            + "\"Hmm? My cat spirit? It's something I got from my friend, Aisha. Have "
                            + "you met her?\"</i> If she's referring to Aisha Song, the girl who gives"
                            + " magic lessons and temporarily gave you a massive penis, then yes. "
                            + "You've most certainly 'met' her.<p><i>\"Aisha is really nice. When "
                            + "I was doing really badly in the Games, she offered to teach me magic."
                            + " I was never able to learn it though. When she said she could use the "
                            + "magic on me to give me catlike reflexes, I jumped at the opportunity. "
                            + "We did a ritual and I got these ears and this tail.\"</i> She proudly "
                            + "shows off her normally hidden cat parts. <i>\"They're a little "
                            + "inconvenient, but they're really cute, aren't they?\"</i><p>It's "
                            + "interesting that Aisha never brought up the possibility of giving "
                            + "you a spirit. Maybe you should ask her about it.<br><i>\"You "
                            + "totally should! I'm imagining you with puppy ears now, or maybe"
                            + " a tiger.\"</i> She blushes a bit at her own mental image. "
                            + "<i>\"I think Aisha may feel bad about how the ritual turned out, "
                            + "even though I keep telling her I don't regret it. If she refuses"
                            + " to give you a spirit, I'll try to  help you talk her into it.\"</i>");
            Global.gui().choose(this, "Leave");
        } else if (choice.equals("Leave")) {
            done(true);
        }
    }
}
