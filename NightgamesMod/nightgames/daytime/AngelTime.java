package nightgames.daytime;

import java.util.ArrayList;
import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.ModdedCockPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.WingsPart;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.requirements.BodyPartRequirement;
import nightgames.requirements.NotRequirement;
import nightgames.requirements.RequirementShortcuts;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class AngelTime extends BaseNPCTime {
    public AngelTime(Character player) {
        super(player, Global.getNPC("Angel"));
        knownFlag = "AngelKnown";
        giftedString = "\"Hmph thanks.\"";
        giftString = "\"A present mm? Alright let's see it.\"";
        transformationOptionString = "Sacraments";
        advTrait = Trait.demigoddess;
        transformationIntro =
                        "[Placeholder]<br/>Angel says she may be able to try a few things with her new divine powers.";
        loveIntro = "You meet Angel at her room, but for once, she doesn't seem eager to get to sex. You can tell she has something on her mind, so you let her lean "
                        + "against you on the futon while she thinks. It's quiet. You aren't used to your time with Angel being quiet, mostly because you so rarely meet her alone. You "
                        + "lose track of time sitting there before she breaks the silence. <i>\"" + player.name()
                        + ", what do you think about my friends?\"</i> That's not the question you expected. "
                        + "You've gotten along with her friends quite well so far. Angel shifts her position so you can't see her face. <i>\"I've had several lovers who couldn't get along with "
                        + "my friends. Some of them pretended they did for awhile, some of them wanted me to spend less time with them.\"</i> Her friends are very socially and sexually aggressive. "
                        + "You can see how that might make some " + Global.getPlayer().guyOrGirl() + "s uncomfortable. The fact that Angel and Mei occasionally have sex could probably also be a point of contention. It's probably "
                        + "fortunate that the night games got you used to casual and group sex before you met them. <i>\"A lot of people are superficially interested in me, but "
                        + "most lose interest when they find out what I'm really like. Sarah, Mei and Caroline know me better than anyone, but they don't think any less of me. Mei and Sarah "
                        + "need me as much as I need them. Caroline is good at making friends, but she chooses to stick with us anyway. If I have to choose between them or a " + Global.getPlayer().boyOrGirl() + "friend, "
                        + "I choose them without a second thought.\"</i> Angel looks you in the eye. You've never seen her this worried and vulnerable. <i>\"Do you really like them?\"</i> You can "
                        + "reply with confidence that you've grown quite fond of Sarah, Mei and Caroline, personalities, quirks and all. <i>\"Good, because I don't kno-... No one's going to make "
                        + "me choose between you and them, got it? That's just not going to happen.\"</i> Angel stands up, back to her normal self. <i>\"So should we meet up with them or spend a "
                        + "little more time with just the two of us?\"</i>";
        transformationFlag = "";
    }

    @Override
    public void buildTransformationPool() {
        options = new ArrayList<>();
        {
            TransformationOption growCock = new TransformationOption();
            growCock.ingredients.put(Item.PriapusDraft, 3);
            growCock.requirements.add(RequirementShortcuts.rev(new NotRequirement(new BodyPartRequirement("cock"))));
            growCock.additionalRequirements = "";
            growCock.option = "Angel: Grow a cock";
            growCock.scene = "[Placeholder]<br/>Angel chugs down the three priapus drafts one after another and grows a splendid new blessed cock.";
            growCock.effect = (c, self, other) -> {
                other.body.add(new ModdedCockPart(BasicCockPart.big, CockMod.blessed));
                return true;
            };
            options.add(growCock);
        }
        {
            TransformationOption removeCock = new TransformationOption();
            removeCock.ingredients.put(Item.FemDraft, 3);
            removeCock.requirements.add(RequirementShortcuts.rev(new BodyPartRequirement("cock")));
            removeCock.additionalRequirements = "";
            removeCock.option = "Angel: Remove her cock";
            removeCock.scene = "[Placeholder]<br/>Angel drinks the three femdrafts one after another and her blessed cock shrinks back into her normal clitoris.";
            removeCock.effect = (c, self, other) -> {
                other.body.removeAll("cock");
                return true;
            };
            options.add(removeCock);
        }
        {
            TransformationOption blessedCock = new TransformationOption();
            blessedCock.ingredients.put(Item.HolyWater, 3);
            blessedCock.requirements.add(new BodyPartRequirement("cock"));
            blessedCock.requirements.add((c, self, other) -> {
                return self.body.get("cock")
                                .stream()
                                .anyMatch(cock -> ((CockPart) cock).isGeneric(self));
            });
            blessedCock.requirements.add((c, self, other) -> {
                return self.get(Attribute.Divinity) >= 10;
            });
            blessedCock.additionalRequirements = "A normal cock<br/>Divinity greater than 10";
            blessedCock.option = "Blessed Cock";
            blessedCock.scene =
                            "[Placeholder]<br/>Angel performs a sacrament on your cock, imbuing it with holy powers.";
            blessedCock.effect = (c, self, other) -> {
                Optional<BodyPart> optPart = self.body.get("cock")
                                                      .stream()
                                                      .filter(cock -> ((CockPart) cock).isGeneric(self))
                                                      .findAny();
                BasicCockPart target = (BasicCockPart) optPart.get();
                self.body.remove(target);
                self.body.add(new ModdedCockPart(target, CockMod.blessed));
                return true;
            };
            options.add(blessedCock);
        }
        {
            TransformationOption divinePussy = new TransformationOption();
            divinePussy.ingredients.put(Item.HolyWater, 3);
            divinePussy.requirements.add(new BodyPartRequirement("pussy"));
            divinePussy.requirements.add((c, self, other) -> {
                return self.body.get("pussy")
                                .stream()
                                .anyMatch(part -> part == PussyPart.normal);
            });
            divinePussy.requirements.add((c, self, other) -> {
                return self.get(Attribute.Divinity) >= 10;
            });
            divinePussy.additionalRequirements = "A normal pussy<br/>Divinity greater than 10";
            divinePussy.option = "Divine Pussy";
            divinePussy.scene =
                            "[Placeholder]<br/>Angel performs a sacrament on your pussy, imbuing it with holy powers.";
            divinePussy.effect = (c, self, other) -> {
                self.body.addReplace(PussyPart.divine, 1);
                return true;
            };
            options.add(divinePussy);
        }
        {
            TransformationOption angelWings = new TransformationOption();
            angelWings.ingredients.put(Item.HolyWater, 2);
            angelWings.requirements.add((c, self, other) -> {
                return self.body.get("wings")
                                .size() == 0;
            });
            angelWings.requirements.add((c, self, other) -> {
                return self.get(Attribute.Divinity) >= 10;
            });
            angelWings.additionalRequirements = "No wings<br/>Divinity greater than 10";
            angelWings.option = "Angelic Wings";
            angelWings.scene = "[Placeholder]<br/>Angel gives you white feathery wings on your back.";
            angelWings.effect = (c, self, other) -> {
                self.body.addReplace(WingsPart.angelic, 1);
                return true;
            };
            options.add(angelWings);
        }
        {
            TransformationOption divinity = new TransformationOption();
            divinity.ingredients.put(Item.HolyWater, 1);
            divinity.option = "Bestow Divinity";
            divinity.scene = "[Placeholder]<br/>Angel has sex with you, lending you a part of her divinity.";
            divinity.effect = (c, self, other) -> {
                self.mod(Attribute.Divinity, 1);
                return true;
            };
            options.add(divinity);
        }
    }

    @Override
    public void subVisitIntro(String choice) {
        if (npc.getAffection(player) > 0) {
            Global.gui()
                  .message("You text Angel, suggesting to meet up. She responds with a location where to meet her. When you arrive however, you find her friends waiting "
                                  + "for you instead. One of the girls, Caroline, waves you over to where they're sitting. <i>\"Angel stepped away for a minute. Sit down and talk with us until "
                                  + "she gets back.\"</i> You spend some time chatting with the girls about their hobbies (Caroline plays a lot of video games and Sarah is fond of romance novels) "
                                  + "and about how you met Angel (you make up something plausible). Mei is sitting right next to you and makes a habit of resting her hand on your leg while "
                                  + "you're talking. Her whole body language suggests she'd be interested in getting to know you better. After a few minutes however, she has to excuse herself "
                                  + "to make a quick phone call. On her way out, she slips a small piece of paper into your hand with a flirtatious smile. It turns out to contain her phone number.<br/><br/><i>\"Be "
                                  + "careful with that girl,\"</i> Caroline warns as soon as Mei is out of earshot. <i>\"She has a bad habit of stealing Angel's men.\"</i> That's quite a specific bad habit. "
                                  + "It must put a considerable strain on their friendship. <i>\"No, Angel never really holds a grudge against her. She has an effective way of punishing Mei and then considers "
                                  + "them even.\"</i> Caroline doesn't elaborate on this 'punishment,' but you notice Sarah has turned bright red. <i>\"She'd probably get mad if Mei stole you though. "
                                  + "Angel doesn't usually run off to fix her make-up just because she's meeting a " + Global.getPlayer().guyOrGirl() + ". I think she really likes you.\"</i><br/><br/>With perfect timing, Angel arrives. Caroline "
                                  + "stops talking, but gives you an encouraging wink. Angel takes the seat next to you that Mei just vacated and kisses you on the cheek in greeting. <i>\"Hello lover. "
                                  + "What sexy and scandalous plans do you have for us today?\"</i>");
            Global.gui()
                  .choose(this, "Games");
            Global.gui()
                  .choose(this, "Sparring");
            Global.gui()
                  .choose(this, "Sex");
            if (Global.getPlayer()
                      .checkAddiction(AddictionType.ZEAL)) {
                Global.gui()
                      .choose(this, "Worship");
            }
        } else if (Global.getPlayer()
                         .checkAddiction(AddictionType.ZEAL)) {
            Global.gui()
                  .message("Angel low-affection zeal intro");
            if (npc.getAttraction(player) < 15) {
                npc.gainAttraction(player, 2);
                player.gainAttraction(npc, 2);
            } else {
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
                  .choose(this, "Worship");
        } else if (npc.getAttraction(player) < 15) {
            Global.gui()
                  .message("While walking through the quad, you spot Angel talking with three other girls. They're too far away to hear what they are talking about, but "
                                  + "you can tell they're close friends. If Angel was alone, you wouldn't hesitate to talk to her, but their group is radiating an almost impenetrable atmosphere. "
                                  + "What strikes you more than anything else is how... normal they seem. At night, Angel is always an insatiable sex queen, but here she's just like any other college "
                                  + "girl. You're still musing on this as the girls pass by. It doesn't seem like Angel noticed you. You decide you'll probably have better luck approaching "
                                  + "her when she's alone.");
            npc.gainAttraction(player, 2);
            player.gainAttraction(npc, 2);
        } else {
            Global.gui()
                  .message("Using the information you got from Aesop, you go looking for Angel. Your hope is to invite her to spend time training together and possibly sex. "
                                  + "You find her pretty quickly, but she's at a table with several of her friends. You'd need to talk to her in private if you're going to bring up the night games, "
                                  + "but you don't have an excuse ready that'll work on her friends. You also don't know how she's going to react if you talk to her out of nowhere. Right when you "
                                  + "decide to give up and try to catch her after a match, Angel looks up and makes eye contact with you. She tells her friends something that you're too far away to "
                                  + "catch and walks over to you.<br/><br/><i>\"Did you want to talk to me or were you just planning to stare?\"</i> Her words are harsh, but her tone is light enough that you can "
                                  + "tell she's just teasing you. You mention Aesop's suggestions on how to train between matches. She thinks for a moment and then holds out her hand. <i>\"Phone,\"</i> she "
                                  + "demands. You hand over your phone, not understanding her intentions. She enters her number into it and returns it to you. <i>\"Give me a call whenever you want to train, "
                                  + "or if you're just horny,\"</i> she says with a relaxed smile.<br/><br/>You glance behind Angel to her friends, just outside of earshot. Two of them are trying (unsuccessfully) "
                                  + "to look like they aren't watching you. The third is simply staring openly. Do they think you're Angel's " + Global.getPlayer().boyOrGirl()+ "friend? <i>\"Oh, I told them we were fuck-buddies,\"</i> Angel "
                                  + "explains. <i>\"Come on, I'll introduce you.\"</i> She leads you back to the table and introduces each of her friends. The first girl, Caroline, seems very friendly and laid back. "
                                  + "Even though you've just met, she talks like you're old friends. The one who was blatantly watching you earlier is a slim, asian girl named Mei. Though she doesn't look "
                                  + "anything like Angel, something in her eyes tells you they're birds of a feather. Lastly, there's a quiet girl named Sarah who seems nice, but can't seem to say two sentences "
                                  + "to you without blushing and lowering her eyes.<br/><br/>The five of you chat idly for a few minutes before Angel returns to your previous topic in the least subtle way possible. "
                                  + "<i>\"So, sex? or...?\"</i> She doesn't seem to mind discussing this in front of her friends. None of them seem surprised at her frankness, though Sarah's blush deepens a bit. As "
                                  + "long as you don't mention your nightly activities, it's probably fine.");
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            Global.gui()
                  .choose(this, "Games");
            Global.gui()
                  .choose(this, "Sparring");
            Global.gui()
                  .choose(this, "Sex");
            if (npc.has(Trait.zealinspiring)) {
                Global.gui()
                      .choose(this, "Worship");
            }
        }
        Global.gui()
              .choose(this, "Leave");
    }

    @Override
    public void subVisit(String choice) {
        if (choice.equals("Worship")) {
            if (npc.getAffection(player) == 0) {
                Global.gui()
                      .message("Angel allows you to worship her, having you do some simple, menial tasks. This"
                                      + " is a placeholder. It still increases your addiction, though. You"
                                      + " won't suffer withdrawal effects tonight.");
            } else {
                Global.gui()
                      .message("You feel very nervous approaching Angel like this. A growing part of you feels a need to "
                                      + "pay respect to this... Angel? Goddess? The other part finds the idea ridiculous. "
                                      + "But perhaps it can't hurt to try? You sheepishly ask Angel if there is anything "
                                      + "you can do for her. <i>\"Do for me? Well, there's always some chores to be done, "
                                      + "but... Why do you ask? You know you don't have to bribe me into sex.\"</i> She's not"
                                      + " going to make this easy on you, is she? You try to explain your predicament "
                                      + "as best you can, trying not to appear too needy about the whole thing. "
                                      + "<i>\"Oh... Oh! I mean... Well... Sure, I guess. Whatever is a girl to do when a cute "
                                      + "" + Global.getPlayer().guyOrGirl() + " comes along, asking to worship her?\"</i> You give a start at the word 'worship',"
                                      + " but then, it does ring kind of true... <i>\"Oh, yes I am sure I can think of some nice"
                                      + " tasks for you. Hmm, I might enjoy this. Follow me, altar " + Global.getPlayer().boyOrGirl() + ".\"</i> Whatever confusion"
                                      + " she felt clearly did not stand up to her dirty mind. Well, what's a sex goddess for?"
                                      + "\n\nAs soon as you reach the privacy of Angel's room, she regains the divine aura "
                                      + "you are used to seeing at night. The sight of the plush white wings sprouting from "
                                      + "her back drives away the hesitation you felt. This is clearly a being worthy of "
                                      + "worship. You stand still as she gets comfortable, patiently awaiting her for first"
                                      + " command. <i>\"Right then, \"</i> she starts, wearing only her bra and short skirt, "
                                      + "<i>\"first, let's first have you bow to me.\"</i> You immediately bend at the waist. The"
                                      + " act feels shockingly natural. <i>\"Now, kiss my foot.\"</i> That's a bridge too far, isn't"
                                      + " it? But no, as she dangles a shapely foot in front of you, you find that it isn't."
                                      + " You kneel and kiss the upside of her foot. <i>\"Hmm, higher.\"</i> You move up to her ankle."
                                      + " <i>\"Higher still. No, even higher.\"</i> Continuously spurred on, you work your way up to"
                                      + " her thigh like this, where you discover a distinct lack of panties beneath her "
                                      + "skirt. Angel doesn't stop prompting, so you don't stop kissing as you delve "
                                      + "underneath the garment. You were planning to tease her, to slow your ascent to a "
                                      + "crawl and not quite reach where she obviously wants you to go. Faced with her pussy,"
                                      + " however, the core of her divinity, her commands gain yet more weight and leave you "
                                      + "unable to even think of defying her. You lap away at her, doing the best job you can. "
                                      + "The air trapped underneath the skirt grows hot and moist, and you struggle to breathe "
                                      + "enough as you lick. Your efforts do seem to be effective, though, as Angel's knees "
                                      + "tremble against your shoulders. <i>\"Keep going! Eat your Goddess!\"</i> The rod in your"
                                      + " pants had grown rock-solid, and it's getting really uncomfortable. Still, you keep"
                                      + " up your licking, launching Angel into a series of orgasms. She pulls your head against"
                                      + " her tightly, nearly smothering you. You need to gasp for breath by the time she "
                                      + "releases her hold, but you cannot bring yourself to feel any anger for it. Or even "
                                      + "frustration. You have pleased Angel! Wait, where did that come from? Whatever the case"
                                      + ", you get up and embrace Angel, pressing your hard bulge against her in hopes of "
                                      + "drawing her attention towards it. <i>\"Mmm, got all excited, did you? Good.\"</i> You can't "
                                      + "help but feel a twitch at her praise. <i>\"But we're not here for you, are we? Aw, I "
                                      + "almost feel bad for you, leaving you like this. Okay, I do feel bad, but it's so much"
                                      + " fun to be a little bad sometimes. I'll help you out next time. Was there anything "
                                      + "else you wanted?\"</i> No. The itch that drove you here is gone, and you feel more at "
                                      + "ease than before despite the raging boner in your pants. You manage to feel a little"
                                      + " annoyance this time at being left blue-balled. Not enough to do something about it,"
                                      + " though, so you leave Angel and hurry home to fix it yourself. ");
            }
            Global.gui()
                  .choose(this, "Leave");
            Global.getPlayer()
                  .addict(AddictionType.ZEAL, npc, Addiction.MED_INCREASE);
            Global.getPlayer().getAddiction(AddictionType.ZEAL).ifPresent(Addiction::flagDaytime);
        } else if (choice.equals("Sex")) {
            if (npc.getAffection(player) >= 12 && (!player.has(Trait.experttongue) || Global.random(2) == 1)) {
                Global.gui()
                      .message("You're in Angel's room, naked and feeling a little overwhelmed. She embraces your from behind and you can feel her soft, heavy breasts pressed against "
                                      + "your back. She nibbles lightly on your ear while motioning toward the naked girl on her bed. <i>\"She's all yours. Show me what you can do.\"</i> As for how you got here... "
                                      + "we should probably back up a bit.<br/><br/>You spent some time chatting with Angel and her friends. At this point it might be fair to call them your friends too. The conversation "
                                      + "inevitably turned to sex, though you can't quite remember how. <i>\"Boys are simple. Five minutes with one hand and they're happy as a clam.\"</i> Caroline said this while casually miming a "
                                      + "handjob. <i>\"They expect girls to be simple too, but no amount of fingerwork is a substitute for a kiss and a romantic atmosphere.\"</i> Ok, so mood is important, but pleasing a "
                                      + "girl isn't really that different from pleasing a guy. It's still the fingers, or tongue, or whatever direct stimulation that finishes the job. There's nothing particularly mysterious "
                                      + "about that. Angel stroked your thigh and gave you a confident smile. <i>\"You only need the fingers because boys are clumsy kissers. If you were a little better at seduction, "
                                      + "the kiss would be enough.\"</i> If Angel could actually make girls cum by kissing them, you probably would have seen her do it by now. You couldn't really bring up the number of times "
                                      + "you've seen her finger her opponents in front of the whole group, so you settled for general skepticism. At this point Angel frowned and ruminated for a while. <i>\"We're going to "
                                      + "need a volunteer,\"</i> she eventually declared.<br/><br/>So that's how the three of you ended up here. Mei modestly keeps her hands in front of her groin, but doesn't show any indication "
                                      + "that she's embarrassed about being nude in front of you. Angel circles in front of you, swaying her hips seductively. <i>\"If you're nervous, I'll show you what a real kiss is like.\"</i> "
                                      + "She gently pulls your head towards her and presses her lips softly against yours. It seems like a surprisingly innocent kiss until her tongue darts out to brush your lips. As she "
                                      + "withdraws her tongue, she presses the kiss deeper. She nibbles your lower lip gently then attacks your tongue with her own. This cycle of invading your mouth with her tongue and deepening "
                                      + "the kiss drives all conscious thought from your head. When she pulls away and you regain your senses, you're so aroused that precum is dripping from your erection.<br/><br/>Angel pushes you "
                                      + "gently towards the bed. <i>\"Your turn. Try to make Mei cum with a kiss like that.\"</i> Mei glances down at your crotch and smirks. <i>\"Try not to cum on your own first.\"</i> You feel weird "
                                      + "about kissing Mei in front of Angel, but it was her idea. You push her gently down onto the bed and kiss her softly. You match Angel's style of gradually increasing pressure and "
                                      + "cycles of aggressive tongue-play. Within seconds, Mei whimpers softly into your mouth as she realizes she underestimated you. Not much later, you feel her body tensing up. Then "
                                      + "she pulls away from you abruptly, flushed deep red and panting for breath. <i>\"Ok, that's enough,\"</i> she gasps out. <i>\"You win.\"</i> So she came already? Mei nods, avoiding eye contact.<br/><br/>"
                                      + "\"Not bad,\" Angel says. <i>\"Of course Mei orgasms pretty easily. You'll still need to practice more before that'll work on someone like me.\"</i> You cut her off by kissing her with "
                                      + "the same passion you used to get Mei off. Her soft noises of protest melt away as you press the kiss deeper. She makes no attempts to pull away or retaliate and soon you feel a "
                                      + "shudder run through her and she has to cling to your shoulders to stay on her feet. She smiles as you pull away and whispers, <i>\"You're a quick learner, lover.\"</i> She grasps your "
                                      + "pent-up cock and strokes it until you cum in her hands. She offers her semen covered hand to Mei, who hesitantly begins to lick it clean. Angel looks back to address you. <i>\"I hope that's "
                                      + "enough for a little while, I need to punish Mei now.\"</i><br/><br/>Mei jumps back in shock. <i>\"What did I do!?\"</i> Angel gives her a sadistically sweet smile. <i>\"Are you going to pretend you "
                                      + "weren't kissing my " + Global.getPlayer().boyOrGirl() + " on my bed a minute ago? He made you orgasm and you even drank his cum.\"</i> Angel walks over to the refrigerator in the corner, gets a couple ice cubes from the "
                                      + "freezer, and puts them in a glass. Mei stammers out protest while she walks back to the bed, but Angel silences her with a kiss. She takes one of the ice cubes and teases each of "
                                      + "Mei's nipples with it. She runs the cube down Mei's front and touches it to her clit. Mei yelps and tries to jerk away from the sensation, but Angel manages to slip the ice cube into "
                                      + "her pussy. While Mei shivers and tries to endure the cold object in her most sensitive area, Angel moves between her legs and starts to eat her out. She teases her for awhile before "
                                      + "she retrieves the ice cube with her tongue and presses it against Mei's clit. After a few minutes of alternating between pleasure and torture, Mei screams in passion and goes limp. As "
                                      + "she's recovering, Angel slips the other ice cube into Mei's mouth and straddles her face. \"If this is punishment then you shouldn't have all the fun. Give me some attention this time.\"<br/><br/>"
                                      + "After some of the most spectacular girl-on-girl action you've ever scene, Mei starts collecting her clothes and prepares to leave. You and Angel suggest she stick around and just relax "
                                      + "for a while, but she shakes her head. <i>\"Three's a crowd. You two deserve some alone time. Besides, if I stay longer, who knows what Angel will do to me.\"</i> After she leaves, you ask Angel "
                                      + "to explain what exactly the relationship between her and Mei is. You've heard Mei often tries to steal her boyfriends, but they're clearly quite close. Angel settles comfortably into the "
                                      + "bed, exhausted from multiple orgasms. <i>\"Mei is straight, so even when she's seeking my affection, I have to act like it's punishment. If I neglect her for too long, she steals a boy from "
                                      + "me so I have an excuse to punish her. She doesn't mean any harm, she just gets lonely.\"</i> That seems unnecessarily complicated. Still, it's very considerate of Angel to indulge Mei even when "
                                      + "they're fighting over guys. <i>\"Of course,\"</i> she says. <i>\"Mei is my friend.\"</i>");
                if (!player.has(Trait.experttongue)) {
                    Global.gui()
                          .message("<br/><br/><b>You've improved your kissing technique to the point where it may render opponents temporarily helpless.</b>");
                    player.add(Trait.experttongue);
                    npc.getGrowth().addTrait(0, Trait.experttongue);
                }
            } else {
                Global.gui()
                      .message("You suggest to Angel that maybe the two of you should head somewhere a bit more private. You barely finish the sentence before she grabs you by the arm and "
                                      + "practically drags you away. You give the other girls a quick parting wave and you hear Caroline call after you. <i>\"Have good sex.\"</i><br/><br/>Angel eventually brings you to her "
                                      + "dorm room and stops in front of the door, gestures for you to wait, and disappears inside. Isn't she going to invite you in? <i>\"Not today,\"</i> she replies. <i>\"I have a better "
                                      + "idea.\"</i> You feel a bit awkward loitering in the hall, so you try to make some small talk.<br/><br/>Her friends seem nice. <i>\"My friends are very important to me, so I want you to get along "
                                      + "with them. Just be careful with Mei. If she tries to seduce you, it's just to get attention. She'll dump you in a day or two.\"</i> You thank her for the warning, though Angel "
                                      + "also has reputation for dumping guys when she gets bored with them. She reappears in the doorway and pushes you against the wall, kissing you forcefully. <i>\"Not you.\"</i> she says "
                                      + "after breaking the kiss. <i>\"Not today at least.\"</i> She keeps you pinned against the wall, looking as if she's trying to decide whether to fuck you right here. There are a few people passing "
                                      + "through the hall and you're already getting some weird looks.<br/><br/>After a few moments, she pulls you into the shower room, and for the first time you notice she's holding a towel and a "
                                      + "couple bottles of lotion. She tosses the lotion into an available shower stall and disrobes in record time. She then sets to undressing you with such haste that you're worried "
                                      + "she's going to literally tear off your clothes. She can take her time, it's not like you're going anywhere. She pulls your hand to her womanhood, which you find hot and very wet. "
                                      + "<i>\"I'm not really in a patient mood.\"</i> You help her remove your clothes and retreat into the stall before someone else comes in. You turn on the water at a comfortable temperature "
                                      + "and then you return your attention to Angel. She's got the lotion in hand and starts rubbing it on your body. She pours enough into your hands for you to rub on her.<br/><br/><i>\"It's oil "
                                      + "based,\"</i> she explains. <i>\"So it won't get washed off by the water. It's also safe to use in sensitive holes.\"</i> You're impressed by how thoroughly Angel planned this, despite her "
                                      + "impatience to get started. You decide to test the lotion out by sliding two well-lubricated fingers "
                                      + "into her pussy. She groans softly and starts oiling up your cock in return. Your fingers explore inside her looking to map out her most sensitive areas. Eventually you find "
                                      + "a spot that makes her weak in the knees, so obviously you focus on it. When she's trembling on the verge of orgasm, you deliver the coup de grace by pulling back her clitoral hood "
                                      + "and rubbing her love buzzer with a lubed-up fingertip. She orgasms intensely and you have to catch her to keep you from falling to the floor.<br/><br/>Angel's first words after she "
                                      + "catches her breath are: <i>\"I want you in me right now.\"</i> You couldn't agree more. You push her against the wall and hold her leg up to get clear access to her entrance, while you "
                                      + "thrust your lotion-slick shaft into her. She moans passionately, still sensitive from her recent orgasm. You thrust into her again and again while her nails dig into your back. "
                                      + "<i>\"Fuck! I'm going to cum again!\"</i> Angel is practically screaming now. If someone else is anywhere near the showers, they must be able to hear her. You kiss her passionately to quiet her "
                                      + "as you feel your own orgasm approaching. she grabs your ass with both hands and pulls you as deep into her as possible. Your cock spasms as it shoots its load into her womb and "
                                      + "fuels her climax.<br/><br/>You pull out of her and let the hot water from the shower wash away your sweat and fluids. The oily lotion proves much more water resistant and you're both "
                                      + "still covered in the slippery substance. Presumably the other bottle Angel brought will clean it off. <i>\"Nope that's just shampoo. The lotion will come off eventually,\"</i> she says "
                                      + "while grasping your lubricated dick again. <i>\"We just need to rub it all off.\"</i> In the end it takes more than thirty minutes - and a few more orgasms - before you're both clean, but you "
                                      + "don't regret it.");
            }
            Global.gui()
                  .choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Seduction);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Games")) {
            if (npc.getAffection(player) >= 8 && (!player.has(Trait.pokerface) || Global.random(2) == 1)) {
                Global.gui()
                      .message("Today all the girls left the responsibility of choosing a game to you, with Angel's stipulation that it had to including stripping. You picked a simple, but intense "
                                      + "bluffing game with stripping rules slotted in so naturally it's like the game was designed for them. Not to be immodest, but clearly your genius knows no bounds. The first five "
                                      + "minutes of the game clearly separate the people who can bluff from those who can't. Mei bet far too aggressively and is already completely naked. You lost one coaster and your belt. "
                                      + "Angel and Caroline have both been playing cautiously and are still fully clothed.<br/><br/>Poor Sarah is far too easy to read "
                                      + "when she's nervous and has already been called on three bad bluffs. She's down to one coaster and her panties. This is a rather unusual situation since she is always the moderator in "
                                      + "whatever game the group is playing and doesn't tend to lose her clothes. Unlike the other girls, who treat nudity very casually, she's extremely embarrassed about how much of her body "
                                      + "you can see. Next round, she's forced to make a wager early since she only has one coaster to put down. She bets three and no one raises her. She flips her own coaster first - a rose, "
                                      + "fortunately for her, though she no longer has any way to bluff - and flips Caroline's coaster, a skull. Sarah looks crestfallen, but she does stand up and remove her panties. You catch "
                                      + "a glimpse of her very wet pussy before she covers herself with her hands and sits back down. Mei pats her on the head to comfort her.<br/><br/>Caroline starts the next round, and - though she loses "
                                      + "her top in the next wager - she makes the first successful bet of the game. You and Angel have to play more aggressively now. With the strip rules, she doesn't immediately win the game if she wins another "
                                      + "bet, but she does get to select and eliminate another player. Over the next few rounds, you make some riskier bluffs and Angel calls you on them every God damn time! You've won one bet, but "
                                      + "you're down to your skull and your boxers. Fortunately Angel is in the same boat, because she apparently can't read Caroline as well as you. Caroline is in the best shape, but she's down to "
                                      + "her bra and panties.<br/><br/>The next round is almost certainly going to be your or Angel's last and you're determined not to go down without a fight. All the coasters are played this round and "
                                      + "you have to make a wager, but if anyone calls you, you'll be eliminated. You quietly wager one and think 'rose' in your head. Angel studies your face carefully and slowly smiles. <i>\"I've seen "
                                      + "every expression you make. You can't hide anything from me. Two.\"</i> No one is going to risk three. You keep your face passive as she flips her rose and reaches for your face-down coaster. "
                                      + "Her stunned expression when she turns over a skull is priceless. She slips off her panties and gives you a glare that promises angry, aggressive sex the next chance she gets. Caroline speaks "
                                      + "up. <i>\"Well played, but there's literally no way you can win now that I know you're holding a skull. Do you want to just hand over the boxers now?\"</i><br/><br/>You concede defeat and forfeit your boxers. "
                                      + "Caroline looks you over slowly while deep in thought. <i>\"As the victor, I can have my way with the losers, but... I think I'll leave Angel's " + Global.getPlayer().boyOrGirl() + " to her.\"</i> Angel grins and pats the couch next to "
                                      + "her. As you somewhat hesitantly sit down, she begins to slowly stroke and play with your erection. She leans in close to whisper in your ear. <i>\"Everyone wants to see you cum, so I can tease "
                                      + "them by teasing you.\"</i> You groan quietly. You can't deny the pleasure she's giving you, but she's clearly not going to let you finish anytime soon. Caroline watches the two of you with a flushed "
                                      + "smile. Mei and Sarah are both watching rapt, each with one hand under the table. <i>\""
                                      + player.name()
                                      + " isn't the only loser in the room,\"</i> Caroline says while glancing at the naked girls. <i>\"How about "
                                      + "the two of you give him some eye candy by showing him what you're doing under there.\"</i> Sarah lets out a surprised whimper at the command, but she and Mei both obey Caroline as she has them sit "
                                      + "facing you while they masturbate openly. Sarah looks so embarrassed she may cry, but you can tell she's getting off on this more than anyone else. Mei looks more composed, but from time to time you see her "
                                      + "bite her lip and shudder. You realize she's having multiple little orgasms and trying to hide it. After Sarah climaxes (loudly), Angel speeds up her handjob and whispers in your ear, <i>\"Cum for me. "
                                      + "You have my permission.\"</i> You couldn't hold back even if you wanted to. You shoot your load so far that some of it even hits Caroline, who is standing quite a ways away.");
                if (!player.has(Trait.pokerface)) {
                    Global.gui()
                          .message("<br/><br/><b>You've mastered the art of bluffing.</b>");
                    player.add(Trait.pokerface);
                    npc.getGrowth().addTrait(0, Trait.pokerface);
                }
            } else {
                Global.gui()
                      .message("You know this can't end well for you. When you suggested playing a game, all of the girls shared a conspiratorial smile and Angel declared she had a game in mind. "
                                      + "Now you're sitting in a surprisingly roomy girls' dorm room, waiting to see what craziness Angel has in store for you. <i>\"Found it,\"</i> you hear Sarah call from the next room. Mei "
                                      + "speaks up while she's clearing a spot on the floor. <i>\"It's been awhile since we got to play this. We need to find more interesting " + Global.getPlayer().boyOrGirl() + "s.\"</i> Sarah and Angel return holding a "
                                      + "cardboard spinner and laying out a mat with multicolored circles on the floor. Huh, you weren't expecting something so normal. You haven't played this since you were a kid. "
                                      + "<i>\"We added a couple things to the spinner,\"</i> Sarah explains. <i>\"Also, you have to remove an article of clothing every time you fall.\"</i> Of course, it couldn't be a completely "
                                      + "normal game.<br/><br/>Sarah keeps the spinner and the other four of you remove your shoes and wait on the mat. <i>\"Left foot red.\"</i> Simple enough. <i>\"Left hand yellow.\"</i> The mat seemed "
                                      + "bigger when you were a kid. It's pretty cramped with four of you. <i>\"Right foot blue.\"</i> Ok, now you're pressed between Angel and Mei, which is not a bad place to be. Angel's "
                                      + "breasts specifically are touching the side of your face. You're pitching a tent, and you're not the only one who noticed. A hand grabs at your erection through your pants and it "
                                      + "surprises enough that you fall down, taking Angel and Mei with you. <i>\"Don't be in such a hurry,\"</i> Angel teases, while taking off her top. <i>\"Everyone will be naked soon enough.\"</i>"
                                      + "<br/><br/>About ten minutes and some lost clothing later and Sarah starts calling some weird commands. <i>\"Right hand free.\"</i> Free? What exactly does that mean? <i>\"It means you can move "
                                      + "your hand and do whatever you want with it,\"</i> Angel explains. <i>\"Like so.\"</i> She reaches over and tickles Mei, causing her to fall and lose her last piece of clothing. <i>\"Left hand "
                                      + "Angel.\"</i> Angel? Not wanting to waste an opportunity, you grab her ample breast. Caroline has a slightly different definition of 'touch,' and spanks Angel hard on the ass. She yelps "
                                      + "and falls down onto you. There go her pants, and yours.<br/><br/>A few more minutes and you end up reaching over Caroline to get a green circle, which is slightly awkward since she's "
                                      + "topless and you're in your boxers. <i>\"Right hand boob\"</i> You palm her breast without even thinking, and she lets out a soft noise. You apologize, but she just smiles back at you. "
                                      + "<i>\"Don't worry about it. You can probably repay me if you hold that position for a little while longer.\"</i> <i>\"Right hand male!\"</i> Wait... what? Caroline grins victoriously and her hand darts "
                                      + "into your boxers to grab your dick. Sarah cheers excitedly and leans in for a closer look. <i>\"Caroline got the penis! That means everyone else loses an article of clothing and that "
                                      + "means Angel and " + player.name()
                                      + " are out. Caroline wins!\"</i> <br/><br/>There was definitely no mention of a bonus for grabbing your cock during the rules explanation. Caroline's response to "
                                      + "your protests is to rub the sensitive head of your dick with her thumb, making you squirm a bit despite your indignation. Angel speaks up from behind you while removing her panties. <i>\"If we warned you, you would have tried to protect your man "
                                      + "parts. That would have been no fun.\"</i> Caroline smiles while still holding your shaft. <i>\"I think of this as a team victory. If you want, I don't mind rewarding you for your cooperation.\"</i> "
                                      + "You decline her offer to tug you off for everyone's entertainment, but since she's the winner, you let her remove your boxers. You spend about 20 minutes just hanging out and chatting. "
                                      + "No one is allowed to get dressed and you get the lion's share of the interest, but you feel strangely comfortable with most of the girls in similar states of undress. ");
            }
            Global.gui()
                  .choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Cunning);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Sparring")) {
            if (npc.getAffection(player) >= 16 && (!player.has(Trait.disciplinarian) || Global.random(2) == 1)) {
                Global.gui()
                      .message("Your strip wrestling with Angel has become a routine secondary version of your nightly competitions. Unfortunately, in this venue, Angel has a a much better "
                                      + "win rate than you. You've also gotten used to your three girl audience. Mei's sadistic side comes to the fore as she cheers on Angel whenever she's got the upper hand. Caroline, "
                                      + "on the other hand, consistently encourages the underdog, usually you. Sarah seems to support both of you evenly, but spends most of the matches covertly masturbating to the "
                                      + "show.<br/><br/>This match seems pretty par for the course. You've managed to get Angel to her bra and panties (that's good), but you're down to just your boxers (that's bad). It's "
                                      + "going to be a lot harder to remove Angel's underwear than for her to just grab and pull your boxers down. Your best bet is to try to incapacitate her using unconventional techniques "
                                      + "so you can take your time undressing her. On the subject of unconventional strategy though, your boxers won't provide much protection if Angel starts aiming for your groin. You "
                                      + "decide it's best to go on the offensive for now. You lunge at Angel and pull her into a bearhug. Being careful not to knock teeth with your aggressive movements, you kiss Angel "
                                      + "firmly on the lips to distract her as you unhook her bra. She doesn't lose her composure though, and you are forced to give up on the bra when she gets a hold of the waistband "
                                      + "of your boxers. You struggle to hold your boxers up and you barely manage to pull them out of her grasp and jump away. Since you didn't completely remove the bra, Angel fixes it "
                                      + "and is no worse of. On the other hand, the tug of war stretched out the elastic waistband of your boxers, making them much looser. You also got a bit of a wedgie from the encounter.<br/><br/>"
                                      + "You're suddenly struck by inspiration, though it's a particularly questionable inspiration. You wait for Angel to charge you to try to finish the match. As soon as she gets within "
                                      + "reach, you grab the waistband of her panties and pull upwards as hard as you can. Angel yelps as the fabric digs into her delicate pussy. She stops in mid lunge to nurse her girl parts. "
                                      + "You have to work fast to capitalize on this momentary advantage. You yank her panties down and spank her bare ass cheek. Angel lets out a whimper and you see her shiver in a mixture of "
                                      + "pain and pleasure. You hit her on the other ass cheek and get the same reaction. Your third slap aims between her legs and hits her squarely on the pussy. She shrieks and falls to "
                                      + "the mat. You manage to secure her panties and you try to get her bra, but she kicks at you and forces you back.<br/><br/>The match is still on, but you clearly came out on top in that "
                                      + "engagement. You got her panties, but more importantly, you broken Angel's legendary composure. She's angry and humiliated now, and she's not thinking straight. She lunges at you "
                                      + "recklessly, aiming for your delicates, but you easily counter her. You're able to take control of the match and avoid her clumsy attacks until you find an opening and catch her from "
                                      + "behind. You manage to undo her bra and pull it off despite her thrashing. Angel finally manages to calm down to a quiet seethe with the realization that she's lost.<br/><br/>It occurs to you "
                                      + "that you haven't heard anything from the spectators in awhile. The three of them are all stunned into silence at the turnaround. A sadistic thought crosses your mind and you decide to give "
                                      + "them an even better show. You turn Angel toward her friends and use your feet to force her legs apart and spread her pussy lips open with your fingers. She flushes deeply at being exposed "
                                      + "to her friends and you can feel her rapidly getting wetter. Mei and Sarah are staring at Angel and you can see desire in their eyes, while Caroline mostly looks embarrassed about the "
                                      + "situation. You begin to finger Angel while licking and sucking her neck. As she moans in pleasure, you whisper sadistic questions in her ear. Does her pussy still hurt from being slapped? "
                                      + "Is she getting off on being watched? Is she ashamed to cum in front of her friends? At the last question, she shakes her head vigorously. <i>\"They-AH! T-they're my best friends. I won't "
                                      + "hide anything from themMM!... or you. I-I want the people I love to see everything about me. Even-AH! this!\"</i><br/><br/>You feel Angel tense up in you arms and a flood of love juice hits your "
                                      + "hand as she orgasms. As she goes limp in your arms, the other girls snap out of whatever what keeping them silent and they gather around you. Mei stands nearby, but can't seem to make eye "
                                      + "contact with Angel. Caroline pats Angel on the head while congratulating you on your victory. Sarah crouches next to Angel and kisses her on the cheek affectionately. It occurs to you, not "
                                      + "for the first time, what a close group of friends they are. You feel honored that they're starting to count you as one of them.");
                if (!player.has(Trait.disciplinarian)) {
                    Global.gui()
                          .message("<br/><br/><b>You've learn how to spank your opponent in a way that can ruin their morale.</b>");
                    player.add(Trait.disciplinarian);
                    npc.getGrowth().addTrait(0, Trait.disciplinarian);
                }
            } else {
                Global.gui()
                      .message("You and Angel borrow a small fitness room and some wrestling mats for some sparring practice. She suggests clothing removal as the victory condition instead of "
                                      + "pins or submission, which doesn't surprise you at all. What you weren't expecting was for her to invite an audience. Strip wrestling has become a normal nightly activity for "
                                      + "you now, but the thought of doing it in front of three uninvolved girls is still a little unsettling. You do some simple stretches to warm up and Angel does the same. When you "
                                      + "feel sufficiently limber, you ask Angel if she's ready to start.<i>\"I'm ready. I borrowed a pair of panties so we'd have about the same amount of clothing.\"</i> Wait... what? "
                                      + "You know from sexfighting her that she often forgoes a bra, but was she not even wearing panties? Whose panties is she wearing now? No. It's probably not a good idea to let yourself "
                                      + "get distracted before a match. You try your best to put it out of your mind.<br/><br/>Angel moves first, lunging toward you and trying to take out your legs, but you move fast and step "
                                      + "to the side. She catches one of your legs, but now her momentum is carrying her the wrong way and she falls to the mat. You manage to regain your balance and grab the waistband "
                                      + "of her shorts, but she rolls out of reach and scrambles to her feet.<br/><br/>Caroline gives a low whistle. <i>\"They're both pretty good at this,\"</i> you hear her say. <i>\"When did Angel learn "
                                      + "to wrestle?\" \"They must do this a lot. Do you think it's his fetish or hers?\"</i> Mei this time. While you're distracted by the side conversation, Angel closes the distance again "
                                      + "and manages to get a hold of your shirt. Pulling you off balance, she manages to trip you and strip off your shirt at the same time. Thinking fast, you manage to catch hold of her "
                                      + "shorts as you fall, pulling them down to her ankles. As she stumbles back, you escape with shorts in hand. The net result is you are shirtless and Angel is wearing her T-shirt "
                                      + "and a pair of bright red, side-tie panties. She looks good in them, but she's going to have a hell of a time trying to keep those panties on.<br/><br/>You take the initiative this time, "
                                      + "feinting toward her panties, but actually taking her legs out from under her and landing on top. You take this opportunity to try to pull her top off, but she defends it frantically. "
                                      + "In response, you change your target and successfully untie both sides of her panties and pull them off. While your hands are occupied though, Angel suddenly slips her hand down the front of your pants and grabs "
                                      + "your balls. After that, pain happens.<br/><br/>When the spectators see your plight, they each react differently. Mei cheers enthusiastically, <i>\"Come on Angel, twist his balls off!\"</i> Caroline is a "
                                      + "bit more sympathetic, though her cheeks are tinted with a light blush. <i>\"Ouch, poor " + Global.getPlayer().boyOrGirl() + ". That's probably checkmate.\"</i> Sarah says nothing, but is flushed with arousal and has a hand between "
                                      + "her legs. Angel herself has a smile that's equal parts sadistic and affectionate. She kisses you while trying to work off your pants with her free hand, but eventually decides she needs both hands "
                                      + "and releases your genitals. Most of the fight has already been squeezed out of you and you collapse to the floor.<br/><br/>Angel straddles your head while she strips off your pants and underwear. "
                                      + "Her naked, wet pussy hovers right in front of your face, and despite the pain, you can't help feeling turned on by the view and her scent. It's not until she grabs your dick and starts "
                                      + "jerking you off that you realize you're completely hard. You blush in embarrassment when you realize Angel is showing you off to her friends, but you can't resist the pleasure she's giving "
                                      + "you. In no time, you disgracefully cum in front of four horny girls, leaving your balls even more sore than before.");
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
        return Global.getPlayer().checkAddiction(AddictionType.ZEAL) ? Optional.of("Worship") : Optional.empty();
    }
}
