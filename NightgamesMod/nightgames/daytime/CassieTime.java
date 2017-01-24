package nightgames.daytime;

import java.util.ArrayList;
import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.GenericCockPart;
import nightgames.characters.body.mods.ArcaneMod;
import nightgames.characters.body.mods.SecondPussyMod;
import nightgames.characters.body.mods.SizeMod;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.requirements.BodyPartRequirement;
import nightgames.requirements.NotRequirement;
import nightgames.requirements.RequirementShortcuts;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.Addiction.Severity;
import nightgames.status.addiction.AddictionType;

public class CassieTime extends BaseNPCTime {
    public CassieTime(Character player) {
        super(player, Global.getNPC("Cassie"));
        knownFlag = "CassieKnown";
        giftedString = "\"Awww thanks!\"";
        giftString = "\"A present? You shouldn't have!\"";
        transformationOptionString = "Enchantments";
        transformationIntro = "[Placeholder]<br/>Cassie tells you she could perhaps enchant some of your body.";
        loveIntro = "You and Cassie lay together in her bed while she casts spells above you. Every twitch of her fingers brings a new burst of light and color. She weaves "
                        + "the colors into abstract pictures, but sometimes you can make out figures and familiar places in the patterns. There's no clear narrative or purpose emerging, Cassie "
                        + "probably just likes practicing her witchcraft. <i>\"Not everything I learned has practical applications,\"</i> she says quietly. <i>\"But it's pretty isn't it?\"</i> It is pretty. "
                        + "For a moment you're tempted to say that it's not as pretty as she is, but the line is so cliche that you can't manage it. Cassie rests her head on your chest in silence, "
                        + "but you can tell it's a silence caused by her hesitating to speak rather than having nothing to say.<br/><br/><i>\"I had a crush on you almost as long as we've known each other,\"</i> "
                        + "she says without looking at you. <i>\"You're cute, funny, and we got along so well whenever we talked. I tried to think of ways to flirt with you so you'd see me as more than "
                        + "a friend, but I don't think I'd have ever worked up the courage to try. When I saw that we had both joined the games, I can't properly describe what I felt. Embarrassed"
                        + " of course - maybe more embarrassed than I've been in years - to be seen at a sexfighting competition by someone I knew. I was also really excited about the possibility "
                        + "of being intimate with the " + Global.getPlayer().boyOrGirl() + " I liked. Most of all, I was scared that you might look down on me when you found out what a horny girl I am.\"</i> She grasps your hand and you "
                        + "squeeze it reassuringly. You pull her towards you and kiss her softly. <i>\"I guess it turned out better than I could have hoped.\"</i><br/><br/>She sits up and looks at you, blushing "
                        + "deeply. <i>\"We can't keep lying here with my embarrassing story in the air, let's do some training.\"</i>";
        advTrait = Trait.witch;
        transformationFlag = "";
    }

    @Override
    public void buildTransformationPool() {
        options = new ArrayList<>();
        {
            TransformationOption growCock = new TransformationOption();
            growCock.ingredients.put(Item.PriapusDraft, 3);
            growCock.addRequirement(RequirementShortcuts.rev(new NotRequirement(new BodyPartRequirement("cock"))), "Has no penis");
            growCock.option = "Cassie: Grow a cock";
            growCock.scene = "[Placeholder]<br/>Cassie hesistantly drinks the 3 priapus drafts and grows a large runic cock.";
            growCock.effect = (c, self, other) -> {
                other.body.add(new GenericCockPart().applyMod(new SizeMod(SizeMod.COCK_SIZE_BIG)).applyMod(CockMod.runic));
                return true;
            };
            options.add(growCock);
        }
        {
            TransformationOption removeCock = new TransformationOption();
            removeCock.ingredients.put(Item.FemDraft, 3);
            removeCock.addRequirement(RequirementShortcuts.rev(new BodyPartRequirement("cock")), "Has a Penis");
            removeCock.option = "Cassie: Remove her cock";
            removeCock.scene = "[Placeholder]<br/>Cassie hesistantly drinks the three femdrafts and her runic cock shrinks into her normal clitoris.";
            removeCock.effect = (c, self, other) -> {
                other.body.removeAll("cock");
                return true;
            };
            options.add(removeCock);
        }
        {
            TransformationOption runicCock = new ApplyPartModOption("cock", CockMod.runic);
            runicCock.ingredients.put(Item.PriapusDraft, 10);
            runicCock.ingredients.put(Item.BewitchingDraught, 10);
            runicCock.ingredients.put(Item.FaeScroll, 1);
            runicCock.option = "Runic Cock";
            runicCock.scene = "[Placeholder]<br/>Cassie enchants your cock with the power of the fairies.";
            options.add(runicCock);
        }
        {
            TransformationOption arcanePussy = new ApplyPartModOption("pussy", ArcaneMod.INSTANCE);
            arcanePussy.ingredients.put(Item.BewitchingDraught, 15);
            arcanePussy.ingredients.put(Item.FemDraft, 10);
            arcanePussy.ingredients.put(Item.FaeScroll, 1);
            arcanePussy.option = "Arcane Pussy";
            arcanePussy.scene = "[Placeholder]<br/>Cassie draws intricate arcane tattoos on your pussy";
            options.add(arcanePussy);
        }
        {
            TransformationOption arcaneMouth = new ApplyPartModOption("mouth", ArcaneMod.INSTANCE);
            arcaneMouth.ingredients.put(Item.BewitchingDraught, 15);
            arcaneMouth.ingredients.put(Item.FaeScroll, 3);
            arcaneMouth.addRequirement((c, self, other) -> {
                return self.getLevel() >= 30;
            }, "At least level 30");
            arcaneMouth.option = "Arcane Lipstick";
            arcaneMouth.scene = "[Placeholder]<br/>Cassie enchants a tube of lipstick and gives it to you.";
            options.add(arcaneMouth);
        }
        {
            TransformationOption arcaneAss = new ApplyPartModOption("ass", ArcaneMod.INSTANCE);
            arcaneAss.ingredients.put(Item.BewitchingDraught, 15);
            arcaneAss.ingredients.put(Item.FaeScroll, 3);
            arcaneAss.addRequirement((c, self, other) -> {
                return self.getLevel() >= 30;
            }, "At least level 30");
            arcaneAss.option = "Runic Ass Tattoos";
            arcaneAss.scene = "[Placeholder]<br/>Cassie decorates your rosebud with some runic tattoos.";
            options.add(arcaneAss);
        }
        {
            TransformationOption mouthPussy = new ApplyPartModOption("mouth", SecondPussyMod.INSTANCE);

            mouthPussy.ingredients.put(Item.BewitchingDraught, 10);
            mouthPussy.ingredients.put(Item.FemDraft, 10);
            mouthPussy.ingredients.put(Item.Dildo, 1);
            mouthPussy.ingredients.put(Item.FaeScroll, 3);
            mouthPussy.option = "Mouth Pussy";
            mouthPussy.scene =
                            "When you mention Cassie's modified mouth to her, she blushes bright red and averts her eyes."
                                            + "She replies shyly, <i>\"Uhm would you mind if we don't talk about that? I'm not entirely sure what I was thinking at the time...\"</i>"
                                            + "<br/>Now you're really interested. You try pressing her a bit, and after threatening her a bit with your tickler, she relents and tells you what you want to hear."
                                            + "<i>\"Well I found this spell in an old occult book I picked up when I was traveling in Greece last break. When I first tried to translate it, "
                                            + "I thought the spell would enhance my tongue control and make my blowjobs a bit better, giving myself an edge in the sex games. "
                                            + "Turns out... well I think you know how it turned out.\"</i><br/>"
                                            + "You a bit red as well, and mention to her that you can certainly vouch for the effectiveness."
                                            + "<br/><i>\"The main problem with it is that it leaves my mouth so sensitive. The first time I tried to practice my blowjobs with a dildo after completing the ritual, "
                                            + "I almost came from feeling the ridges on my tongue! It was super intense. It's a bit of a double edged sword. Or rather a double edged sheath if you know my drift,\"</i> Cassie chuckles softly."
                                            + "<br/>You ask if she could maybe try the spell on you as well"
                                            + "Cassie seems a bit surprised with your request, but agrees pretty quickly. She takes the ingredients from you and inscribes a few runes on the dildo you brought along."
                                            + "<i>\"Uhh I think this may be a bit late, but would you mind not watching me? Sorry this is a bit embarassing for me.\"</i>"
                                            + "<br/>You reassure her that whatever she's doing, it can't be too bad compared to the night games. Cassie sighs and waves for you to sit down."
                                            + "She starts the ritual by coating the inscribed dildo with all the potions you brought along. After starting a incomprehensible chant, she unbuttons her pants and starts fingering her pussy. "
                                            + "<i>\"I... uhhgh told you... that this would be, aah, embarassing... Nhhh!\"</i> Cassie grunts as she's masturbating. As she's nearing orgasm, she sticks the inscribed dildo inside her and clamps down on it hard."
                                            + "You can see a soft purple glow transfering itself from her body into the plastic rod. After taking a few seconds to calm down, Cassie beckons you over, <i>\"Your turn "
                                            + player.getTrueName() + ", say Ahh.\"</i>"
                                            + "<br/>You obediently sit down on the couch, and open your mouth. As expected, Cassie takes the glowing fake phallus, still coated with her juices, and starts putting it in your mouth. "
                                            + "At first, you gag a bit from having a large foreign object stuffed into your throat, but after a few moments, you start feeling your entire oral cavity reforming itself around the false cock. "
                                            + "Your teeth recede a bit back into your gums, and you feel walls of soft muscle line your mouth. Your tongue feels like it's on fire as it rapidly lengthens, filling your maw, and spilling out of your expanded lips. "
                                            + "Finally, the changes seem to stabilize and you're left feeling very strange. Cassie notices the expression on your face and grins at you, <i>\"Don't worry, you'll get used to it pretty quickly. I know I did.\"</i>"
                                            + "<br/>She begins to rub the now-drained dildo again against your newly formed mouth-pussy walls and tongue. "
                                            + "You blank out with the strange sensations that your organ transmits back to you, and Cassie takes the opportunity to mouth-fuck you to an almost-instantaneous climax."
                                            + "<br/>Cassie walks by you and gives you a quick kiss which almost makes you cum yet again, <i>\"I'm so looking forward to seeing what you do with that tonight!\"</i>";
            options.add(mouthPussy);
        }
    }

    @Override
    public void subVisitIntro(String choice) {
        if (npc.getAffection(player) > 0) {
            Global.gui().message(
                            "You text Cassie and suggest meeting up to spend some time together. You get to the meeting place first and settle down on a bench to wait for her. "
                                            + "You don't end up waiting long, but you manage to get distracted by something on your phone and don't notice Cassie approaching until she's right next to you. Before "
                                            + "you can stand up, she leans over you and kisses you on the mouth. She cuddles up next to you on the bench and happily rests her head on your shoulder. You're a little "
                                            + "embarrassed about her public display of affection, but it hasn't drawn too many stares from nearby students. Recently Cassie has been acting very affectionate to you "
                                            + "during the day. From an outsider's perspective you probably look like an overenthusiastic couple of newlyweds, but you have to admit her behavior is really cute. <i>\"I wouldn't "
                                            + "mind spending all day like this,\"</i> she murmurs contently. <i>\"But it sounded like you had something specific planned.\"</i>");
            Global.gui().choose(this, "Games");
            Global.gui().choose(this, "Sparring");
            Global.gui().choose(this, "Sex");
            if (npc.has(Trait.magicmilk) && Global.getPlayer().checkAddiction(AddictionType.MAGIC_MILK)) {
                Global.gui().choose(this, "Ask for milk");
            }
        } else if (Global.getPlayer().checkAddiction(AddictionType.MAGIC_MILK)) {
            Global.gui().message(
                            "You find Cassie studying in the library, a ways out of earshot of the other students. She catches your eye and smiles knowingly. "
                            + "Putting down her book, she walks to you and whispers in your ear, <i>Give me five minutes, then meet me in the girls bathroom on the third floor. Don't worry, it's always empty.</i>"
                            + "You're shocked at her unexpected forwardness, but your pulsing need for milk distracts you from her uncharacteristic attitude."
                            + "You pretend to be interested in a book on Roman history, and stand around the stacks for a while. After roughly five minutes, you put down the literature, and head upstairs."
                            + "<br/>"
                            + "The third floor of the library is a part of the archives, where old books go to die if they're not asked for, for over 5 years. "
                            + "You wander around for a bit and find the girls bathroom hidden in a little corner by the classic slavic literature section. No wonder why it's mostly unused. "
                            + "While idly speculating at why Cassie even knows of such a place, you open the door and step inside. The moment you enter, you see Cassie sitting by the sink. She looks up at the noise and smiles as you step inside. "
                            + "She looks at you knowingly and teases while cupping her generous chest, <i>\""+player.getTrueName()+", what's up? Was there something... you wanted?\"</i>");
            if (npc.getAttraction(player) < 15) {
                npc.gainAttraction(player, 2);
                player.gainAttraction(npc, 2);
            } else {
                npc.gainAffection(player, 1);
                player.gainAffection(npc, 1);
                Global.gui().choose(this, "Games");
                Global.gui().choose(this, "Sparring");
                Global.gui().choose(this, "Sex");
            }
            Global.gui().choose(this, "Ask for milk");
        } else if (npc.getAttraction(player) < 15) {
            Global.gui().message(
                            "You find Cassie studying in the library, a ways out of earshot of the other students. You give her a friendly greeting and sit down next to her. "
                                            + "But after a little bit of awkward small talk she excuses herself and practically runs away, red faced. The two of you weren't particularly close friends, but "
                                            + "you always used to be able to have a friendly conversation with her. It's a little lonely having her avoid you so blatantly.");
            npc.gainAttraction(player, 2);
            player.gainAttraction(npc, 2);
        } else {
            Global.gui().message(
                            "You're in the library, looking around to see if Cassie is around. Soon you spot her entering a private study room. When you follow her in, she jumps "
                                            + "like a frightened animal, but forces a friendly smile. As the two of you chat, she doesn't try to flee, but is still acting uncomfortable and avoids making eye "
                                            + "contact. You eventually decide to broach the subject directly and ask her if you've done something to upset her. She goes quiet for awhile, looking at the floor. "
                                            + "Finally she takes a deep breath and leans against your chest.<br/><br/><i>\"You haven't done anything wrong. I don't want to avoid you, I just don't know how to act around you,\"</i> she says in a fragile whisper. <i>\"At "
                                            + "night it's like a whole different world. I'm not the same person during a match as I am the rest of the time. When dawn comes, I'm me again and I leave all that "
                                            + "behind, but you're part of both worlds. When I see you during the day, am I the girl you had sex with last night or the just a normal student?\"</i><br/><br/>Whether you're "
                                            + "chatting after class or competing in wild sex games, Cassie's always the same person at her core. She can pretend to be whoever she wants to be when you're hanging "
                                            + "out together or practicing for the night games, but it won't change who she is. There's surely enough overlap between her two worlds for you to fit. She's quiet "
                                            + "for another short while, then stands up on her toes and presses her mouth softly against yours. You've tasted her lips before, but this is something different. this "
                                            + "is hesitant and innocent, like a lover's first kiss.<br/><br/><i>\"If I can pretend to be anyone, can I pretend to be your girlfriend?\"</i> You answer by wrapping your arms "
                                            + "around her and kissing her tenderly. <i>\"Hanging out with a cute " + Global.getPlayer().boyOrGirl() + " interspersed with wild sex games? Sounds like a great date,\"</i> she says, face flushed but "
                                            + "lit up in a genuine smile. <i>\"What exactly do you have in mind?\"</i>");
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            Global.gui().choose(this, "Games");
            Global.gui().choose(this, "Sparring");
            Global.gui().choose(this, "Sex");
            if (npc.has(Trait.magicmilk) && Global.getPlayer().getAddictionSeverity(AddictionType.MAGIC_MILK) != Severity.NONE) {
                Global.gui().choose(this, "Ask for milk");
            }
        }
        Global.gui().choose(this, "Leave");
    }

    @Override
    public void subVisit(String choice) {
        if (choice.equals("Ask for milk")) {
            if (npc.getAffection(player) > 0) {
                String msg;
                switch (Global.getPlayer().getAddictionSeverity(AddictionType.MAGIC_MILK)) {
                    case HIGH:
                        msg = "You haven't been able to think of anything but Cassie all day, whether simply because of"
                                        + " her milk or because of some deeper feelings. She seems to understand the"
                                        + " look in your eyes, and sets of for her dorm without a word. She's"
                                        + " practically skipping. You hurry after her, unsure whether you will be able"
                                        + " to hold back the thirst. Once inside, Cassie turns to you with a wide grin"
                                        + " and starts to speak. <i>\"So for today, I was thinking... Hey!\" </i>You don't let"
                                        + " her finish, instead you pick her up and deposit her swiftly on the dinner "
                                        + "table. <i>\""+player.getTrueName()+"! What are you doing?\"</i> No time for words. Time for milk. "
                                        + "You yank her shirt over her head and pull her bra down to expose her tits. "
                                        + "Then you dive in, drinking greedily. Ah! So alive! The bit she left you was"
                                        + " great, but nothing compares to drinking straight from the source. Cassie, "
                                        + "for her part, seems content to just let you have your fill, now that the "
                                        + "initial shock has passed. Your tongue is a whirlwind on her nipple, trying "
                                        + "to extract as much of the life-giving fluid as you can. Eventually you just"
                                        + " can't drink anymore, and sit down in one of the chairs. She sits up on the"
                                        + " table, not bothering to cover up. <i>\"OK, now I'm really worried. It's only "
                                        + "been a few hours, but you still were completely out of control just now. If"
                                        + " I had resisted, would you even have stopped?\"</i> You... aren't too sure, "
                                        + "honestly, and deeply apologize to Cassie as you say so. <i>\"The only solution"
                                        + " I can think of, aside from some way to actually fix this thing, is to just"
                                        + " let you drink more often. I asked at that Body Shop Aesop told us about, "
                                        + "but they've got nothing. So, more milk for you it is. That will mean we'll"
                                        + " have to see even more of each other, though. I hope that isn't too much "
                                        + "of a problem?\"</i> A problem? You can't think of anything you'd like to do"
                                        + " more! And this time you're sure it's not just the milk, either. You walk"
                                        + " back up to her and embrace her, telling her this. <i>\"Oh, "+player.getTrueName()
                                        +"!\"</i> She grabs "
                                        + "your head and pulls you in for a deep kiss. The kiss grows more and more"
                                        + " passionate, and she starts leaking milk on your chest. Through supreme "
                                        + "force of will, you ignore it. Instead you reach down to get rid of her "
                                        + "skirt and panties. She responds in kind, undoing your zipper and pulling "
                                        + "your pants and underwear half way down your thighs, enough to expose your"
                                        + " painfully hard erection. Focused as you are on the kiss and your rampaging "
                                        + "emotions, it takes a few tries to get yourself lined up. Eventually Cassie "
                                        + "helps you out, and you slowly but inexorably sink all the way down inside "
                                        + "of her. You both moan into the other's mouth and hug each other even more "
                                        + "fiercely. You just stay like that for a while, but then you can't resist "
                                        + "any longer and start moving. Slowly at first, but growing faster and "
                                        + "faster. Cassie's moans, at the same time, grow higher and higher, and it's"
                                        + " not long before they culminate in an orgasmic yell. You join her soon "
                                        + "after, filling her to the brim with your cum. You lie there on the kitchen "
                                        + "table for a good long while before either of you has any motivation to get"
                                        + " up. The thirst rears it's head again after a while, and you are briefly "
                                        + "worried that you really may be stuck like this. With Cassie... Perhaps that's"
                                        + " not such a bad thing after all? ";
                        break;
                    case LOW:
                        msg = "Something in Cassie's kind eyes puts you at ease, but you are still nervous at what you "
                                        + "are about to do. You explain the thirst you've been feeling, about how it's "
                                        + "getting stronger and stronger. She listens with growing concern, but lets "
                                        + "you finish completely before responding. <i>\"First off, I'm sorry. They "
                                        + "told me this stuff would grow my boobs, and that it would make my milk taste "
                                        + "better. I thought it would be like the other stuff, you know, where you "
                                        + "really get into it for a while and then shrug it off. I never wanted to "
                                        + "cause any damage, certainly not to you... But it's done, and I'm not going"
                                        + " to back off during the matches. Right now, though, you could drink some,"
                                        + " I suppose. But you'd have to earn it. That might just make things worse,"
                                        + " but if you think you can't handle it...\"</i> Honestly, you can't. So you go"
                                        + " with Cassie to her room, eager to hear what she wants in return. <i>\"Don't"
                                        + " worry,\"</i> she starts, <i>\"I'm not going to turn into some kind of dominatrix."
                                        + " I just... Have some needs of my own. Quid pro quo, you see?\"</i> You ask what"
                                        + " she specifically wants. <i>\"Well, I know you want milk, but I have some "
                                        + "other fluids as well which, shall we say, are starting to overflow down "
                                        + "there.\"</i> Having heard enough, you lift Cassie up in your arms and carry her"
                                        + " to her bed. You fall down on it with her and kiss her neck. From there, you "
                                        + "slowly travel down. When you reach the hem of her skirt you hook your "
                                        + "fingers into it, grabbing her panties as well, and continue your downwards "
                                        + "kisses along her leg until she's naked from the waist down. She starts "
                                        + "squirming more and more as you travel back up to your destination. You take "
                                        + "your time, kissing and then licking her inner thighs and lower belly, slowly "
                                        + "zeroing in on her soft cleft. You use your tongue to trace the intricate runes"
                                        + " around her lower lips, and she moans in approval. There is already a steady "
                                        + "stream of her wetness dripping onto the mattress, and you start at the bottom,"
                                        + " slowly lapping your way up to the source. You briefly tease her lips, moving "
                                        + "up to her clit. Cassie suddenly grabs your head and forcefully pulls you into"
                                        + " her pussy. <i>\"Enough with the teasing! I am so close to cumming already, so "
                                        + "get to it!\"</i> You are briefly shocked that calm, subdued Cassie is speaking "
                                        + "like this, but then you have been teasing her pretty hard. You delve in with "
                                        + "full energy, running your tongue through her folds and dipping it into her "
                                        + "hole. Cassie starts cumming almost instantly, and does not seem to stop for"
                                        + " several minutes. Eventually she pushes you away, weakly scrambling back. "
                                        + "<i>\"Please, I... I can't take it anymore. You've definitely earned your reward,"
                                        + " but you'll have to get it yourself...\"</i> She collapses onto her back before "
                                        + "she has even finished speaking. Quickly confirming she's alright, you gently "
                                        + "remove her shirt and bra. You lay down and embrace her with your head on her "
                                        + "breasts. After a few seconds, you take the nipple you've been laying on and "
                                        + "suck it into your mouth. As you start sucking, Cassie places a hand on the "
                                        + "back of your head, softly petting you as you drink. Your cock was already "
                                        + "painfully hard from licking her, and it grows even more so at the taste of "
                                        + "her sweet milk. You remove your pants with your free hand, but remain "
                                        + "completely focused on the delicious taste. You crawl up to face level once "
                                        + "you have had enough, and the two of you lay there for a few minutes of "
                                        + "silence. Then Cassie looks down towards the hard dick poking her in her leg."
                                        + " <i>\"Perhaps we should do something about that before you go...\"</i> You kiss her"
                                        + " in response, and you spend a good long while making tender love before you"
                                        + " leave. ";
                        break;
                    case MED:
                        msg = "<i>\"No, wait, don't answer that. You're thirsty again, aren't you? Are you sure you want to"
                                        + " do this? It only seems to get worse the more you drink...\"</i> She's right, but"
                                        + " that doesn't mean you're suddenly not parched anymore. <i>\"Well, in that case"
                                        + " maybe you can put that tongue of yours to work again. No! I've got the most"
                                        + " perfect idea! Come with me!\"</i> Whatever concern for your well-being she may "
                                        + "have had, it clearly went up in smoke. Now she's giddily almost-running to "
                                        + "her dorm. You follow, hoping that it's as good for you as she seems convinced"
                                        + " it will be for her.\n\n"
                                        + "Cassie does not stand on ceremony and you start making out the moment"
                                        + " the door closes. You're both in your underwear by the time you fall side "
                                        + "by side on her bed, leaving a trail of clothes to guide you back to the door"
                                        + " later. Cassie breaks the kiss and crawls down to the edge of the bed, taking "
                                        + "off her bra and revealing those glorious orbs in the process. Umm. Wasn't "
                                        + "this supposed to be about you paying her for milk? <i>\"Don't worry, you'll pay"
                                        + " your share. This is just preparation.\"</i> She rubs your flaccid cock through "
                                        + "your boxers, running her fingers along the covered shaft. Then she bends down"
                                        + " and licks you through the fabric. She pays special attention to the emerging "
                                        + "head, and it's not long before you are fully hard. <i>\"There's something I want"
                                        + " to try. Something I wasn't able to do, before. But now with these babies...\""
                                        + "</i> She gestures towards her recently-expanded busom and then removes your wet "
                                        + "boxers. Your cock makes an audible smack against your stomach, causing Cassie"
                                        + " to giggle slightly. She then grabs hold of it and places it firmly in her "
                                        + "deep valley, wrapping her breasts around you. The head still pokes out, and "
                                        + "she gives it an experimental lick, grinning. <i>\"Oh, I am going to like this, "
                                        + "I think.\"</i> You are pretty sure you will, too. That premonition is confirmed "
                                        + "as she starts moving her tits up and down in an alternating pattern. All the"
                                        + " while she keeps the swollen nob in her mouth, drawing lazy circles on it "
                                        + "with her tongue. You don't know if it's just the raw sensations, the proximity"
                                        + " to the milk you so desperately need, or the feelings you have for this "
                                        + "supremely cute girl between your legs, but as you lie there you cannot think "
                                        + "of anywhere you'd rather be. Some milk leaks from her nipples, but you somehow"
                                        + " resist lunging for it. After much too short a time, Cassie crawls back up "
                                        + "your body. The copious precum and milk leave a slick trail up your stomach "
                                        + "and chest, and the two of you share a wet embrace. <i>\"Now it's time for you "
                                        + "to earn your keep!\"</i> Cassie sits down squarely on your cock, humping it "
                                        + "through the soaked panties she's still wearing. She raises herself up and "
                                        + "pulls the wet cotton aside. You waste no time, reach down to align yourself,"
                                        + " and thrust upward. Cassie starts bouncing on you like a madwoman, and the "
                                        + "sight of those magnificently swaying boobs finally gets the better of you. "
                                        + "You sit up and pull Cassie against your body, placing her in your lap. Then "
                                        + "you tilt her backwards, bringing her nipples up to mouth-level. You latch on"
                                        + " and suck as hard as you can, and you are rewarded with a long, delicious "
                                        + "spurt of milk. The taste lights yet more fires within you, and you speed up"
                                        + " your movements. Thrusting is a bit difficult in this position, but you are"
                                        + " hitting her g-spot on every movement. It doesn't take long for her first "
                                        + "orgasm to hit, and the second. Breathing heavily, Cassie grinds against you"
                                        + " harder and harder, but you keep your lips sealed around your prize. At one"
                                        + " point she lifts up your chin to give you a kiss, but you quickly dive back"
                                        + " down to where the milk is. After a respectable amount of time, during which"
                                        + " Cassie came more times than you cared to count, you finally let go within "
                                        + "her. Still holding her tight, you let yourself fall back on the bed with her"
                                        + " in top of you. You stay like that for a while, catching your breath. Then "
                                        + "the both of you leisurely get dressed. <i>\"I must say that I am a bit concerned"
                                        + " with how strong this - let's call it for what it is - this addiction is "
                                        + "getting. Are you sure you're alright?\"</i> You assure her that, now that you have"
                                        + " had your fill, you are perfectly fine. Even though it isn't precisely true; "
                                        + "you feels the beginnings of thirst welling up again. <i>\"Well, I suppose that as"
                                        + " long as you can fuck like that, there's nothing to worry about. Just... Take"
                                        + " care of yourself, okay?\"</i> You kiss Cassie goodbye,"
                                        + " and return home to recuperate. ";
                        break;
                    case NONE:
                    default:
                        throw new IllegalStateException();                    
                }
                Global.gui().message(msg);
            } else {
                Global.gui().message("Cassie lets you drink some milk, as long as you do chores for her. This"
                                      + " is a placeholder. It still increases your addiction, though. You"
                                      + " won't suffer withdrawal effects tonight.");
            }

            Global.gui().choose(this, "Leave");
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            Global.getPlayer().addict(AddictionType.MAGIC_MILK, npc, Addiction.MED_INCREASE);
            Global.getPlayer().getAddiction(AddictionType.MAGIC_MILK).ifPresent(Addiction::flagDaytime);
        } else if (choice.equals("Sex")) {
            if (npc.getAffection(player) >= 12 && (!player.has(Trait.silvertongue) || Global.random(2) == 1)) {
                Global.gui().message(
                                "Cassie eagerly invites you to her room for some intimate time. The room is quite tidy, though you're surprised to see a couple anime "
                                                + "posters on the wall. Cassie gets a little embarrassed as you look around, but she kisses you softly and leads you to the bed. You quickly strip each other "
                                                + "naked, sharing quick kisses when you get the chance. When you then start to fondle her breasts, she stops you gently. <i>\"Can we try something a little different "
                                                + "today?\"</i> she asks shyly. You let her take the lead as she has you lie down on the bed. She gets on her hands and knees next to you and begins to closely examine "
                                                + "your dick. You start to feel awkward under her intense scrutiny and ask her what she's doing. <i>\"I'm grown quite fond of this little guy and he's given me some "
                                                + "very nice memories, but during our matches we're so busy trying to win that I never get a chance to really look at him. I thought today I'd spend some time really "
                                                + "getting to know him.\"</i> It's more than a little embarrassing listening to her talk about your penis like it's a person, but you indulge her curiosity. Soon she's "
                                                + "graduated from just looking and starts licking your dick slowly and deliberately. She takes her time and you gradually, but inevitably feel your ejaculation building. "
                                                + "When she tongues you just under the glans and applies a little suction, it pushes you over the edge. You give a low groan and shoot your load into her mouth.<br/><br/>"
                                                + "Cassie swallows your semen and giggles. <i>\"I think I found a sensitive spot. I'll have to remember that.\"</i> She doesn't move away from your groin, watching with a smile "
                                                + "as your dick starts to soften. This isn't the first time she's seen a penis. Is it really that fascinating? Her cheeks grow slightly redder than they already were. "
                                                + "<i>\"Girls are interested in sex too. I've spent a lot of nights thinking about " + Global.getPlayer().boyOrGirl() + "s since puberty. You're the closest I've ever had to an actual " + Global.getPlayer().boyOrGirl() + "friend.\"</i> So she was a virgin "
                                                + "when she first joined the night games? She fidgets a bit at the question, which you notice makes her hips wiggle in quite an attractive way. <i>\"Not quite a virgin. "
                                                + "In high school, I had a close friend like that, but in the end we were just friends. One day as it's getting close to graduation, we end up talking about how we both want to lose "
                                                + "our virginities before we get to college, so we decide to help each other out.\"</i> She's bright red with embarrassment, but continues. <i>\"It was kinda awkward, but a lot of fun, "
                                                + "and afterwards we went back to being just friends and never talked about it again. I don't regret doing it, but I never felt any strong feelings for him.\"</i> She turns to "
                                                + "face you and gives you a shy smile. <i>\"That's probably why it feels so much better with you.\"</i><br/><br/>She looks back at your crotch and grins eagerly. <i>\"Looks like you're ready "
                                                + "for another round. I hope you have a few more shots in you. I want to get lots of practice today.\"</i> You're not likely to turn down more oral sex, but you aren't going to give "
                                                + "her complete control today. You grab her hips, pull her into 69 position, and start licking her soaked pussy. She makes a cute noise of surprise that melts into a soft moan. "
                                                + "<i>\"O-ok tha-mmm... I don't mind if you do that for a while.\"</i> She takes your cock into her mouth again and continues pleasuring you. Fair enough. If she's going to become better "
                                                + "acquainted with your manhood, you might as well learn the ins and outs of her most sensitive area.<br/><br/>The two of you keep up your oral activities until your tongues are too "
                                                + "tired to continue and both of you have orgasmed more times than you can count.");
                if (!player.has(Trait.silvertongue)) {
                    Global.gui().message(
                                    "<br/><br/><b>Through diligent practice, you and Cassie have gotten more skilled at oral sex.</b>");
                    player.add(Trait.silvertongue);
                    npc.getGrowth().addTrait(0, Trait.silvertongue);
                }
            } else {
                Global.gui().message(
                                "Cassie is quiet as you lead her back to your room. Her nervousness is understandable given what you're planning to do together. It would "
                                                + "be more understandable if it wasn't something you do every night. You draw her close next to your bed and kiss her passionately. She lets out a soft noise "
                                                + "and returns the kiss enthusiastically. You break the kiss to remove her shirt and lower her onto the bed. You kiss and lick a trail down her neck to her "
                                                + "collarbone and linger there while you unhook her bra. As you pull the garment away, she hurriedly covers her breasts in embarrassment. You had planned to "
                                                + "devote attention to her breasts, but it seems you'll need to skip them for now. You move lower and trail kisses down her belly to the top of her skirt. "
                                                + "Cassie lets out a whimper when you slide off her skirt and covers her crotch to prevent you from removing her panties.<br/><br/><i>\"Turn off the lights... please?\"</i> "
                                                + "You comply, though there's enough daylight that it's a purely symbolic gesture. You lean over her unresisting form and slide her panties off, exposing her "
                                                + "feminine garden while she hides her face in shame. She's as wet as you've ever seen her, so why is she being so passive today? You know better than anyone "
                                                + "that Cassie is quite capable of taking what she wants when she's turned on. <i>\"This is different,\"</i> she protests meekly. <i>\"Usually we have sex because it's "
                                                + "part of the game, but right now we're doing it because we both want to. This is way more embarrassing.\"</i><br/><br/>If this is that special to her, you want to make sure "
                                                + "you both enjoy it as much as possible, and that means coaxing Cassie into being a more active participant. You strip off your own clothes, drawing her curious "
                                                + "gaze, and then slip your hand between her thighs. Her hips jerk upwards as you caress her sensitive petals and she lets out a sweet moan. She hesitantly reaches "
                                                + "out and grasps your erection in return, but the pleasure you're giving her is affecting her concentration and she can't manage much more than some clumsy stroking. "
                                                + "Even if her handjob is not terribly skilled, the idea that she's trying to please you is arousing enough to keep you hard. You kiss her again and like before, her "
                                                + "tongue comes out to meet yours.<br/><br/>Soon Cassie moans against your mouth and her body arches against you as she cums hard. You continue kissing her until she relaxes. "
                                                + "When you try to pull away, she clings to you with renewed vigor and rolls on top of you. She looks at you with eyes wet with desire. <i>\"I want you to cum inside me,\"</i> "
                                                + "she whispers while breathing heavily. She guides your dick into her flooded entrance and screams in pleasure as she takes your entire length at once. You sit up and "
                                                + "stifle her voice with another kiss. She clings to you desperately and rides you to another climax. When she cums again, her hot pussy clenches down, milking  your rod. "
                                                + "You shoot your load into her hot depths, feeding the intensity of her orgasm. The two of you collapse on the bed, still joined below the waist and completely spent.");
            }
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Seduction);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Games")) {
            if (npc.getAffection(player) >= 16 && (!player.has(Trait.misdirection) || Global.random(2) == 1)) {
                Global.gui().message(
                                "Cassie continues to impress you with her gaming prowess, but right now, you've got a decisive advantage. You're at match point, so if either of you can "
                                                + "score again, that'll almost certainly be the game. Unfortunately for her, you've got her R&D completely locked down. If she doesn't have the agenda she needs in her "
                                                + "hand, you're going to see it before she does. On her turn she installs one card face down, then plays Mushin No Shin to install another on a naked server with three advancement "
                                                + "tokens.<br/><br/>It's your turn and suddenly your situation is a lot less comfortable. If you weren't at match point, your probably wouldn't risk going after the three advance card, "
                                                + "but you need to strongly consider it. If you don't run on it and it's an agenda, she'll score it next turn and win. If you do run it and it's a trap, it'll probably kill "
                                                + "you. Still, if you spend the first half of your turn drawing, you should just barely survive anything that could be there. You draw two cards and run on the naked server, "
                                                + "revealing... a Psychic Field.<br/><br/>Fuck. You discard your entire hand, but survive the trap. You use your last click to draw a card so she can't flatline you next turn. "
                                                + "That could have gone a lot better. Your hand is almost empty and you weren't able to maintain your R&D lock this turn. She'll want to use this opening to try to score "
                                                + "the last two points she needs, so you won't be able to spend next turn recovering. You'll need to run whatever she plays this turn. Wait a minute, what about the first "
                                                + "card she installed last turn. You forgot about it because you were dealing with the three advance card, but the agenda she needs to win may already be on the table.<br/><br/>"
                                                + "Sure enough, next turn Cassie plays a Trick of Light to move two of the advancement tokens to the unknown card from the Psychic Field (You probably should have paid to trash "
                                                + "it last turn, but hindsight is 20/20). She scores the agenda this turn, gaining the last two points she needs to win the game. While you're cleaning up the cards, she tries "
                                                + "valiantly, but fails to hide her smile from completely outplaying you. She deserves a bit of gloating. You congratulate her on a well-played game and she beams at you.<br/><br/><i>\""
                                                + "Misdirection is the key to most magic tricks and it works equally well in most games. It's also how I got your pants off with out you noticing.\"</i> You glance down at your pants, "
                                                + "which are clearly still on. Cassie suddenly closes the distance between you and kisses you passionately. She presses her body against yours and you embrace her gently. <i>\"Ok, so "
                                                + "I admit your pants are still on,\"</i> she whispers as you break for air. <i>\"Neither of us wants that. Maybe we should work together to remove the pants?\"</i> Perhaps some of her clothes need "
                                                + "to be removed too? <i>\"I agree, we're both wearing far too much clothing for the bedroom.\"</i> She's already got your belt off as you kiss her again and lead her to the bed.");
                if (!player.has(Trait.misdirection)) {
                    Global.gui().message(
                                    "<br/><br/><b>You've learned the art of using a diversion to distract your opponent.</b>");
                    player.add(Trait.misdirection);
                    npc.getGrowth().addTrait(0, Trait.misdirection);
                }
            } else {
                Global.gui().message(
                                "Cassie is a bit coy about her geek credentials, but you discover she's quite fond of board games when she corrects you during a rules explanation of the "
                                                + "game you brought. Apparently she's already played this game a few times. It takes a bit of coaxing to get her to admit she plays a lot of these kinds of games, but by the "
                                                + "time you're ready to start playing, she's actually eager to show you the fruits of her experience.<br/><br/><i>\"Everything is terrible and the whole universe hates me!\"</i> You sympathize, "
                                                + "you really do. You rub Cassie's back to comfort her as she leans on you. <i>\"Look at my cute little astronauts. All they wanted to do was get home with a reasonable amount of "
                                                + "cargo.\"</i> Yeah, and now they're all slaves. <i>\"Slavers are jerks.\"</i> That's probably a bit of an understatement. Still, that wasn't her fault at all. When you set off, she had a "
                                                + "better ship than you and more than enough lasers to deal with some slavers. However, she's had a pretty nasty run of bad luck involving a space epidemic, some asteroids, and a detour "
                                                + "through a combat zone. Despite all that, she would have still probably won if she had any crew to fly what was left of her ship.<br/><br/>Cassie flops limply against you and buries her "
                                                + "face in your chest, completely drained by defeat. You stroke her hair and tickle her lightly around her neck. She coos contently and nuzzles your chest. After a few minutes of "
                                                + "enjoying her cute responses to your touch, you move your hand down to fondle her breast over her shirt. <i>\"Are you trying to make me forget about the game?\"</i> You give a shrug in "
                                                + "such a way as to convey that perhaps consoling her is part of your motivation, and perhaps part of it is that her breasts are quite tempting to fondle. She rolls onto her back "
                                                + "in such a way as to convey that if you're going to fondle her, you may as well do it properly. Her new position "
                                                + "allows you to slide your hand down the front of her pants and tease her pussy. Her cheeks flush red and she sighs in pleasure as you gradually feel her love juices start to flow. "
                                                + "When she's thoroughly wet, you push two fingers into her entrance to rub her sensitive walls. Your thumb locates and rubs her clit and in no time, you can feel her tense in orgasm. "
                                                + "As you take your hand out of her pants, she cuddles up against you sleepily. <i>\"Thanks,\"</i> she whispers. <i>\"Oh, I don't want to be selfish. If you're interested, I'd be happy to return "
                                                + "the favor.\"</i> You are a bit horny, but more than that, you're comfortable. For now you decide to just relax and enjoy Cassie's warmth.");
            }
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Cunning);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Sparring")) {
            if (npc.getAffection(player) >= 8 && (!player.has(Trait.judonovice) || Global.random(2) == 1)) {
                Global.gui().message(
                                "You and Cassie manage to procure an actual fitness room with actual wrestling mats for your sparring practice. No more rolling around in couch cushions and pillows. "
                                                + "the downside it that you don't have the same level of privacy as in your dorm room, so today you'll need to stick with just sparring. Cassie seems a lot more confident than usual "
                                                + "and the two of you complete your warm ups in good spirits. You start the match with some simple lunges and takedowns. You notice that she's gotten much better at maintaining her "
                                                + "balance and avoiding getting caught in your holds. You decide it's ok to come at her more seriously. You grab her by the shoulders and try to use your superior upper body strength "
                                                + "to force her to the mat. There's a sudden whirl of movement, an impact on your back, and you find yourself looking at the ceiling.<br/><br/>Cassie bends over you, looking concerned. \"Are "
                                                + "you ok? Can you still move?\" You seem to be fine, you just don't know what happened there. She gives you relieved smile. <i>\"That was a harai goshi, a hip throw, or "
                                                + "it should have been at least. It was still a little rough.\"</i> That seemed plenty effective. You'd hate to have to face the refined version. Cassie giggle a bit. \"I've been learning "
                                                + "some basic judo on my own. Judo has a lot of techniques that can defeat an opponent without seriously hurting them, so I thought it would be perfect for me.\" That's some serious "
                                                + "dedication she's putting into this competition. <i>\"When we first started these night games, I thought it didn't really matter if I wasn't very good as long as I could earn enough to "
                                                + "help with tuition. After a few matches, I started really wanting to see if I could win. Each time I lost a fight, I wanted to figure out if I could have done better. I guess this "
                                                + "is my answer to that.\"</i><br/><br/>She extends her hand to help you up, but instead you pull her down on top of you and kiss her tenderly. She blushes and grins at you when you break the kiss. "
                                                + "<i>\"That's not a judo technique.\"</i> She stands back up and motions for you to do the same. <i>\"Come on, I'll show you what I've learned.\"</i>");
                if (!player.has(Trait.judonovice)) {
                    Global.gui().message("<br/><br/><b>By training with Cassie, you learned the Hip Throw skill.</b>");
                    player.add(Trait.judonovice);
                    npc.getGrowth().addTrait(0, Trait.judonovice);
                }
            } else {
                Global.gui().message(
                                "You and Cassie do your best to prepare your dorm room for an informal sparring match. You've moved any potentially dangerous or fragile furniture and placed "
                                                + "down a layer of cushions, blankets, and pillows on the floor. The result is pretty unprofessional looking and hard to keep your footing on, but should allow you to wrestle "
                                                + "without any risk of injury. Of course, during the night games you've fought in some fairly cluttered areas with very hard surfaces and no one has been hurt yet. On reflection "
                                                + "it seems like a small miracle that there haven't been any accidental injuries during a match. During the day, however, you don't see any reason to tempt fate, hence the safety "
                                                + "precautions.<br/><br/>The two of you agree that this match will focus completely on conventional sparring techniques and not include any sexual holds. You warm up by alternating "
                                                + "practicing simple takedowns and pins on each other. Cassie's technique is a bit rough but she is able to successfully execute the moves you show her. When you actually start "
                                                + "competing however, the match is woefully one sided. You're able to completely control her while she can only flail vainly to try to escape. You know she's better than this "
                                                + "so you ask her why she's holding back. <i>\"If this is just for practice, I don't want to actually hurt you. You've been pretty gentle with me too.\"</i> You have been careful "
                                                + "to avoid unnecessary pain, but you've only been able to do so because you were winning so easily. Compassion is the luxury of the strong.<br/><br/>After resetting to neutral position "
                                                + "again, you're quickly able to take her down and pin her on her back. In response, she tilts her head up and kisses you. You point out that kissing is not actually considered "
                                                + "a wrestling technique. <i>\"It should be. It's very satisfying.\"</i> She kisses you again. At this rate you're just going to end up having sex. <i>\"Sounds good. I like sex.\"</i> She's obviously "
                                                + "not taking sparring seriously, so you decide to punish her a bit.<br/><br/>You slip your hands under her shirt and start tickling her bare skin. She shrieks in surprise and tries desperately "
                                                + "to squirm away from your fingers. <i>\"Nooo! This is terrible!\"</i> She gasps out between fits of laughter. You move your right hand down between her legs and slip your fingers up the leg of "
                                                + "her shorts to reach her sensitive inner thigh. <i>\"This was supposed to be a friendly match. It's not fair using your secret ultimate technique!\"</i> She tries to protest "
                                                + "more, but it devolves into incoherent giggles. She tries to roll away to escape, but you hold her tightly from behind. Your fingers tickle their way up her thigh and reach the "
                                                + "edge of her panties. Pushing them aside, you find her pussy completely soaked and start fingering her without hesitation. As her laughter starts to turn into pleasured whimpers, "
                                                + "you move your other hand up to tease and pinch her nipples. <i>\"I-I thought sexual holds weren't allowed.\"</i> The match ended when you pinned her. This is just her punishment for "
                                                + "giving up so easily. She opens her mouth to reply, but can't form any words. Her pussy clamps down on your fingers as she orgasms. You continue to hold her as her climax dies "
                                                + "down and she goes limp from exhaustion. Looks like you're done sparring for now.");
            }
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Power);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Leave")) {
            Global.modCounter(Flag.CassieLoneliness, -2);
            done(true);
        }
    }
    
    @Override
    public Optional<String> getAddictionOption() {
        return Global.getPlayer().checkAddiction(AddictionType.MAGIC_MILK) ? Optional.of("Ask for milk") : Optional.empty();
    }
}
