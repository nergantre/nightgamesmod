package nightgames.daytime;

import java.util.HashMap;
import java.util.Map;

import nightgames.characters.Character;
import nightgames.characters.Eve;
import nightgames.characters.Kat;
import nightgames.characters.NPC;
import nightgames.characters.Reyka;
import nightgames.characters.custom.RecruitmentData;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.status.addiction.Addiction;

public class Informant extends Activity {
    boolean acted;
    private Map<String, NPC> customNPCChoices;

    public Informant(Character player) {
        super("Information Broker", player);
        customNPCChoices = new HashMap<>();
        acted = false;
    }

    @Override
    public boolean known() {
        return true;
    }

    @Override
    public void visit(String choice) {
        Global.gui()
              .clearText();
        Global.gui()
              .clearCommand();
        if (!Global.checkFlag(Flag.metBroker)) {
            Global.gui()
                  .message("It's almost strange seeing the Quad bustling with people knowing how completely deserted it is at night. Of course, you've been here more "
                                  + "during the day than at night, so the unsettling feeling you have is probably just a delayed reaction to the eerie atmosphere last night. You're here to "
                                  + "meet an upperclassman who will apparently provide valuable information and possibly advice. At least that's what you were led to believe last night. Well, "
                                  + "any information would be welcome. It's not like anyone else will talk about the 'night games.' Fortunately, the bench in the shadow of the Liberal Arts "
                                  + "building where you're suppose to meet is away from the normal foot traffic. You should be able to talk freely without having to worry about passersby overhearing "
                                  + "you.<br>You take a seat and idly fiddle with your phone while you wait. Less than a minute later, a somewhat tall guy who looks a few years older than you sits down "
                                  + "next to you.<p><i>\"Good match last night for a bunch of virgins. There's a lot of potential in your year.\"</i><br>Presumably this is Tyler, the information broker you're "
                                  + "here to meet - that or a crazy person.<br><i>\"Everyone in the loop calls me Aesop: as long as you can pay, I'll have a fable for you.\"</i><p>Also, you let it pass without "
                                  + "comment a second ago, but you feel compelled to point out that you aren't actually a virgin.<br><i>\"I meant that it was your first match. Everyone this year is coming "
                                  + "in with some sexual experience, but your first match is a whole other virginity. You're either in the loop or out of it and the only people in the loop are current or "
                                  + "former participants.\"</i><br>That would imply that 'Aesop' here is also a participant.<br><i>\"Hell yeah, I used to rock some pussy. Male players are rare, so we have to make "
                                  + "up for it by being more impressive. You don't know it yet, but the same expectations are hanging over your head. Anyway, I made a tidy profit in my matches, but I "
                                  + "realized the big money is in selling secrets in a world where everything is secret. Don't worry, you're getting my I'm-mentoring-a-total-newbie-and"
                                  + "-don't-care-if-I-make-a-profit-right-now discount. Besides, most of what you need right now is dime-a-dozen common sense, so I'll even throw you some free advice. "
                                  + "Down the road I'm expecting you to reach the levels where you can afford my premium secrets. I have some much wealthier clients who will set me up for life if I "
                                  + "can get them the really juicy information.\"</i><p>It's nice to know you're a stepping stone to your broker's financial security, but how helpful is this dime-a-dozen "
                                  + "common sense going to be?<br><i>\"Obviously this is a physical competition, so conventional exercise is a good idea. You also need to condition your sex drive. I recommend "
                                  + "spending some time with the nastiest, most extreme porn you can get your hands on. After a few hours of that, everything else will seem milder in comparison. Lastly, don't "
                                  + "underestimate the effect of confidence in a fight. If you can consistently dish out a good fucking, you may find things going your way more often.\"</i><br>"
                                  + "Aesop suddenly looks at his phone and stands up. <i>\"Sorry, I need to run for now. Shoot me a text when you're interested in some more advice. I'll meet you here again.\"</i>");
            Global.flag(Flag.metBroker);
            Global.gui()
                  .choose(this, "Leave");
            acted = true;
            return;
        } else if (player.getRank() >= 1 && !Global.checkFlag(Flag.rank1)) {
            Global.gui()
                  .message("You go to meet Aesop at the usual spot, but today he's not alone. The woman who initially recruited you - named Maya, if you remember correctly - is waiting "
                                  + "for you. Looking at her next to Aesop, she's probably not much older than he is, but something about her makes her seem more mature. She has modest clothes, glasses and long "
                                  + "black hair tied in a braid that runs down her back. She smiles gently as you approach. <i>\"Congratulations on your new rank. You probably won't realize what an honor it is yet, "
                                  + "but you've proven yourself worthy of learning powerful and potentially dangerous secrets. Because Tyler here has made a business out of such secrets, I'm sure he'll be thrilled "
                                  + "to offer you details.\"</i><p>Aesop steps forward as if on cue. You get the impression they've done this a few times. <i>\"Congratulations. From here on is where your mind gets blown. "
                                  + "I'm now allowed to sell you some more valuable secrets and you now have the income to afford them. I can't afford to give you as big a discount anymore, but if you're willing to "
                                  + "pay, I can help you find some extremely rare and powerful artifacts, technology you couldn't imagine, a master who will train you in legendary martial arts, even magic.\"</i><p>While "
                                  + "you're still processing this, Maya speaks up again. <i>\"I appreciate that you haven't mentioned the Game to any outsiders. If the wrong people heard what we're doing, there would "
                                  + "be an unpleasant scandal. As you and your fellow competitors gain access to these gifts, the importance of secrecy grows. These are secrets that the general public is not ready "
                                  + "for.\"</i><p>This is all very far-fetched. The skeptical part of your brain is telling you this is an elaborate scam, but all the money you're paying Aesop is coming from the Game. "
                                  + "Besides, if they can operate secretly on campus at this scale, you're ready to believe your mysterious benefactor is capable of anything. What do you really have to lose?<p>Maya, "
                                  + "having apparently finished her part, excuses herself with a small bow. Aesop clears his throat once she's out of earshot. <i>\"In addition to the new services you have access to, "
                                  + "I can arrange some more competition for you if you're interested. Now I'm not trying to imply that you're the sort of greedy bastard who isn't satisfied with four girls in his "
                                  + "harem. Obviously a serious athlete such as yourself needs a wide range of competitors to hone his skills. I happen to know some girls who are currently taking a break from the "
                                  + "Game for various reasons. For a nominal fee, I could probably convince them to return. Let me know if you're interested in hearing their stories.\"</i>");
            Global.flag(Flag.rank1);
        } else if (choice.equals("Start") || choice.equals("Back")) {
            Global.gui()
                  .message("The spot where you're suppose to meet Aesop is quiet again. Given the number of people in the Quad during the day, you're starting to feel like people "
                                  + "are intentionally avoiding this bench. You don't have too long to think about it though, Aesop shows up shortly after you do.<br><i>\"It's good to see my favorite client "
                                  + "as always. What kind of tale are you looking for today?\"</i>");
            acted = false;
        }
        if (choice.equals("Leave")) {
            done(acted);
            return;
        }
        if (choice.equals("Purchasing supplies: Free")) {
            Global.gui()
                  .message("<i>\"As you were probably told when you entered, there are rules restricting the clothes you can wear to a match. If everyone wore a bunch of hard to remove layers, "
                                  + "it would bog the matches down. Other than that, you're allowed to bring just about any tools and toys you want. Most of the higher rank players carry an arsenal of various useful items.\"</i> "
                                  + "There can't be a lot of stores that sell sexfight supplies, unless there's more demand for it than you thought.<br><i>\"I can give you the address of a nearby sex shop, of course. "
                                  + "If you do go there, don't bother with any of the cheap crap. Their top shelf items should be just barely sufficient. Of course, you can also find useful stuff at normal stores. "
                                  + "A simple energy drink from the campus bookstore can keep you on your feet if you're running out of steam. If you're the type to sneak around preparing traps, you can find parts "
                                  + "in the local hardware store. If you're feeling particularly thrifty, you can raid the storage in the engineering workshops or under the dining hall. No one really cares if some "
                                  + "random crap disappears from there during the night.\"</i><br>This is all kinda mundane advice. You were half expecting the address of an abandoned building and a password.<br><i>\"Oh there's "
                                  + "that too, but it'll cost you. If you ever need some more illicit goods, I can sell you a location later.\"</i>");
            Global.flag(Flag.basicStores);
            Global.gui()
                  .choose(this, "Leave");
            acted = true;
            return;
        }
        if (choice.equals("The Competition: Free")) {
            Global.gui()
                  .message("<i>\"Looking for advice about girls? If you don't have any older brothers to ask, I might be able to help.\"</i><br>Aesop laughs a bit at his own crappy joke.<br><i>\"In all seriousness, you should definitely "
                                  + "get to know your enemies. This isn't a zero-sum game, everyone who participates can make some money. Obviously there would be issues if you tried to doctor the "
                                  + "results of a match, but it's not uncommon for players to collaborate during their free time. You can swap strategies, do some training together or do some "
                                  + "'training together' as the case may be.\"</i><br>He lets out the same humorless laugh again. That one didn't even really qualify as a joke.<p><i>\"Competitors pushing each "
                                  + "other to up their game makes for an overall higher level of play and makes the matches more interesting. That makes everyone happy. Besides, all your opponents are "
                                  + "girls of A+ caliber, you lucky bastard. I don't blame you for wanting to get to know them better. Just give me the name of the girl you're interested in (with a nominal "
                                  + "fee of course) and I'll give you some background, some interests, and some places where you're likely to 'coincidentally' run into her during the day. Even if she doesn't "
                                  + "seem interested in you at first, you can try again after a few matches. In my experience, after you have sex with a girl a few times, she starts to remember your name. Who "
                                  + "knows, you might get a girlfriend out of this. It's not entirely unprecedented.\"</i>");
            Global.flag(Flag.girlAdvice);
            Global.gui()
                  .choose(this, "Leave");
            acted = true;
            return;
        }
        if (choice.equals("Sharpening the mind: $200")) {
            if (player.money >= 200) {
                Global.gui()
                      .message("You ask if Aesop has any advice on how to condition yourself mentally. Picking up details in the environment and your opponents' behavior seems to be an important "
                                      + "element of winning the matches. Also, since you can only be defeated by orgasming, learning techniques to control your arousal would be valuable.<br>Aesop hesitates for a few seconds "
                                      + "before speaking. <i>\"I can point you in the right direction, but I should warn you that there's a tradeoff that every player who pursues this path has to face. The two aspects of "
                                      + "mental training that you just mentioned are conflicting. If you sharpen your senses to be more aware of your opponents, your sense of touch will also be sharpened and you'll be "
                                      + "more vulnerable to their 'attacks.' On the other hand, you can train yourself to block out the pleasure being inflicted on you, but you risk your brain filtering out crucial information.\"</i><br>"
                                      + "<i>\"If you're still interested, there's a girl named Suzume who leads guided meditation on campus. If you ask nicely, she may give you a private lesson to push you in either direction.\"</i>");
                Global.flag(Flag.meditation);
                player.money -= 200;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Black Market: $400")) {
            if (player.money >= 400) {
                Global.gui()
                      .message("<i>\"If you're looking for something you can't purchase at a reputable store - say, for example, powerful aphrodisiacs or chemicals that "
                                      + "can rapidly dissolve synthetic clothing without harming skin - I know a guy on campus.\"</i><br>You hand over a considerable sum of cash and Aesop slides you "
                                      + "a piece of paper.<br><i>\"The dude's name is Mike Ridley, definitely an asshole, but he can get you want you want. He probably won't ask what you want it "
                                      + "for and you shouldn't volunteer the information. He mostly sells pot to normal students. He's not in the loop, so to speak.\"</i>");
                Global.flag(Flag.blackMarket);
                player.money -= 400;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Cassie: $300")) {
            if (player.money >= 300) {
                Global.gui()
                      .message("<i>\"Cassie Brooke, huh? Sweet girl, a little shy, a little geeky, never been in trouble in her life. She's not the type of girl who usually "
                                      + "enters sexfights, but she's adapting to it pretty well, all things considered. This is apparently a matter of necessity for her, her family doesn't "
                                      + "have a whole lot of money, doesn't get enough financial aid, and they don't want her getting a job until she graduates. Of course, I seriously doubt "
                                      + "they'd approve of this either. Hopefully she's clever enough to explain away her sudden income.<br>I can give you her normal schedule. but you shouldn't "
                                      + "need any help finding her. The two of you are in a couple classes together this semester.\"</i><p>It's true, you see her in class regularly, and before the "
                                      + "two of you entered the matches, you chatted before and after class pretty frequently. Now that you're sharing a secret hobby though, you never "
                                      + "get a chance to really talk with her while the other students are around. After class she seems to disappear to quickly that you're starting to suspect she's actively "
                                      + "avoiding you.<br><i>\"I said she was shy, didn't I? In the afternoon, she spends a lot of time studying in the library like a good student. Seems like a good "
                                      + "place to talk to her privately.\"</i>");
                Global.flag(Flag.CassieKnown);
                player.money -= 300;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Angel: $300")) {
            if (player.money >= 300) {
                Global.gui()
                      .message("<i>\"Angel Hart, there's a hell of a name. Three words for you: Beautiful Blonde Nymphomaniac. Oh baby, that's a delicious combination. None of that "
                                      + "is news to you, of course, because it's obvious within ten seconds of meeting her, but I just had to say it out loud. mmmm...<br>Anyway, back on topic. He legal name "
                                      + "is Angelina, but she never uses it. Her father is rich and influential, the vice president of a company you probably haven't heard of, but is a bit of a heavyweight "
                                      + "in the financial world. Angel has probably never wanted for anything in her life. She's always been popular with both boys and girls. Over the past couple years she's "
                                      + "had a series of boyfriends and a couple girlfriends, none of whom have ever lasted more than a week. She'll throw just about anyone who piques her interest onto the "
                                      + "bed, but tends to get bored quickly. If you want her to take you seriously, you'll need to impress her. She likes being in control, but I bet she turns to jello if "
                                      + "you can beat her at her own game. Talking to her may be tough. I can give you some locations where she and her friends tend to hang out, but you're going to have a "
                                      + "hell of a time catching her alone.\"</i>");
                Global.flag(Flag.AngelKnown);
                player.money -= 300;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Mara: $300")) {
            if (player.money >= 300) {
                Global.gui()
                      .message("<i>\"There's a bit of a mystery surrounding Mara Cyr, to be honest. I can't figure out when, or even if, she sleeps. She's a freshman Computer Engineering "
                                      + "student, but her classload looks more like something I'd expect from a junior or senior in that major. Most students in that situation would be working late into the "
                                      + "night, but she's spending her nights sexfighting. I can totally understand why she'd need something to relieve the stress, but again it doesn't leave a lot of time "
                                      + "for sleep. If you told me she invented a time machine to pack more hours into the day, I wouldn't be particularly surprised. There's no question the girl is a genius. "
                                      + "Like a lot of geniuses, her personality is kinda problematic. On the surface she seems arrogant and manipulative, but I'd wager under that facade, she's actually "
                                      + "a nice girl. If you're interested in her, she's pretty easy to find. When she's not in class or in a match, she seems to spend all her time in on of the computer labs "
                                      + "or the electrical engineering workshop.\"</i>");
                Global.flag(Flag.MaraKnown);
                player.money -= 300;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Jewel: $300")) {
            if (player.money >= 300) {
                Global.gui()
                      .message("<i>\"Jewel Silvers... Her parents must like jewelry. Not really; her family has a long military tradition going back generations. I'm surprised she hasn't "
                                      + "joined the army, but she probably doesn't have the right temperament for it. She spent most of her youth bouncing between military bases, getting in fights with all "
                                      + "the local kids, mostly boys. Seems like she mastered the male anatomy pretty quickly, which probably won her a few fights growing up. I wonder what those kids would "
                                      + "say if they could see the beauty she turned into. It's not like she's putting any effort into her appearance, she must just have some seriously good genes. I found an "
                                      + "old picture of her mother when she was in college, just as gorgeous. Under the pretty face, Jewel is still the same rough and tumble tomboy she was growing up. She's "
                                      + "thrown herself almost obsessively into martial arts and is following a training regimen that probably tops what she would be doing if she was in the military. Competitive "
                                      + "sexfighting is probably a smaller leap for her than anyone else in your year. Your best bet to approach her is probably while she is training, but given her combative "
                                      + "personality, I suspect you're going to need to prove yourself a worthy rival before she'll show any interest in you.\"</i>");
                Global.flag(Flag.JewelKnown);
                player.money -= 300;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Exotic Artifacts: $2500")) {
            if (player.money >= 2500) {
                Global.gui()
                      .message("Rare artifacts sound more like something you should be smuggling than something that will help you in a sexfight. Still, curiosity compels "
                                      + "you to hand over an unreasonable amount of money for Aesop's information. <i>\"You once asked me about an address and a password for the black market. I "
                                      + "gave you the address. Now I think you're ready for the password. The black market stocks some rare and unusual items for their prefered customers. Tell "
                                      + "them Callisto sent you and they'll make them available for you. I do feel compelled to warn you that some of the shit they sell there is pretty dangerous. "
                                      + "I don't mean your life is at risk, nothing that serious, but be careful not to lose your humanity. Sometimes power really can change a person.\"</i>");
                Global.flag(Flag.blackMarketPlus);
                player.money -= 2500;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Body Shop: $2500")) {
            if (player.money >= 2500) {
                Global.gui()
                      .message("Aesop looks at you strangely then slips you a piece of paper.");
                Global.flag(Flag.bodyShop);
                player.money -= 2500;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Martial Arts: $2500")) {
            if (player.money >= 2500) {
                Global.gui()
                      .message("You're interested in improving your martial arts skill, but Jewel is probably the best martial artist on campus. Even if she agrees to train you, "
                                      + "becoming your rival's student probably won't get you to the top. Aesop smirks at you. <i>\"Jewel is not the best martial artist on campus. Suzuki Suzume, in addition "
                                      + "to her skill with guided meditation, is the successor to a long japanese martial arts legacy. During her participation in the Games, she adapted the karate "
                                      + "and judo she learned in her family's dojo into something completely new. She's pretty eager to take on a student, but she had to wait for the competitors in your "
                                      + "year to be ranked.\"</i> You've gotten used to paying Aesop for information by now, but if Suzume is so eager for a student she could have approached you directly and "
                                      + "saved you a lot of money. Aesop smiles in a way that reminds you of a shark. <i>\"I'm an information broker. Sometimes I sell people information, sometimes I buy "
                                      + "their silence. The Suzuki dojo has fallen on tough times recently, so when I offered her a thousand dollars to wait for you to come to me, she was fairly agreeable.\"</i> "
                                      + "Fucking hell. If nothing else, at least Aesop is giving you a lesson in economics 101.");
                Global.flag(Flag.dojo);
                player.money -= 2500;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Magic?: $2500")) {
            if (player.money >= 2500) {
                Global.gui()
                      .message("What exactly did Aesop mean when we said magic? Presumably you haven't actually stepped into a fantasy novel. If he means the art of misdirection "
                                      + "and sleight of hand, you can see how that would be useful, but it's not likely to blow your mind. Aesop just shakes his head as he gives you a phone number. "
                                      + "<i>\"I'm not going to try to convince you. You're better off seeing it yourself. That number belongs to an old friend of mine named Aisha. She's spent the last "
                                      + "couple years learning magic and teaching it to others. She'll take you on as an apprentice for a price.\"</i> Of course there's a price. Everyone involved in the "
                                      + "Games has been eager to take your money.<p>Aesop gives you a frown of disapproval. <i>\"I wasn't implying that she's greedy. Aisha is passionate about developing "
                                      + "her craft and only takes money to further her research. Of all the people I've sent you to, she's easily the most altruistic. I meant it when I called her a "
                                      + "friend, and I have a lot of respect for her, so don't make judgments before you've met her.\"</i>");
                Global.flag(Flag.magicstore);
                player.money -= 2500;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Advanced Equipment: $2500")) {
            if (player.money >= 2500) {
                Global.gui()
                      .message("You're interested in the technology Aesop mentioned. A handful of zipties and a rubber dildo will only get you so far. <i>\"You're going to want to meet "
                                      + "Jett. He's not much of a people person, but he's a mechanical genius. Last year, he was the only male competitor and he started out as a bit of a pushover. He wasn't "
                                      + "in great shape and didn't have much sexual experience. There was only one girl he even had a chance against, and she was a virgin when she started. After only a week "
                                      + "though, he learned to set traps and rig up impressive inventions. Pretty soon he became a major threat. Now he's got his own workshop on campus where he's basically "
                                      + "left to work on his inventions. He tells me that if I send people to him, he's willing to make some custom toys. If he takes a liking to you, he may even teach you "
                                      + "some of his secrets.\"</i>");
                Global.flag(Flag.workshop);
                player.money -= 2500;
                Global.gui()
                      .choose(this, "Leave");
                acted = true;
                return;
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        final String REMOVE_PREFIX = "Remove: ";
        final String RETURN_PREFIX = "Bring Back: ";
        if (choice.equals("Select Competitors")) {
            Global.gui()
            .message("Haha, feeling the heat? That's okay, I can talk to the organizers about redirecting some of the competitors to other sessions. Just let me know who is becoming too much for you.");
            Global.everyone().stream()
                  .filter(c -> !c.human())
                  .filter(c -> !Global.checkCharacterDisabledFlag(c))
                  .forEach(character -> Global.gui().choose(this, String.format(REMOVE_PREFIX + "%s", character.getName())));
            Global.everyone().stream()
                  .filter(c -> !c.human())
                  .filter(c -> Global.checkCharacterDisabledFlag(c) && !c.getType().equals("Yui"))
                  .forEach(character -> Global.gui().choose(this, String.format(RETURN_PREFIX + "%s", character.getName())));
            Global.gui().choose(this, "Back");
            return;
        }
        if (choice.startsWith(REMOVE_PREFIX)) {
            String name = choice.substring(REMOVE_PREFIX.length());
            Global.gui()
                  .message("Got it, I'll see about sending " + name+ " to another session.");
            Global.setCharacterDisabledFlag(Global.getCharacterByName(name));
            Global.gui()
                  .choose(this, "Select Competitors");
            return;
        }
        if (choice.startsWith(RETURN_PREFIX)) {
            String name = choice.substring(RETURN_PREFIX.length());
            Global.gui()
                  .message("Missing " + name+ " already? I'll see what I can do.");
            Global.unsetCharacterDisabledFlag(Global.getCharacterByName(name));
            Global.gui()
                  .choose(this, "Select Competitors");
            return;
        }
        if (choice.equals("More Competitors")) {
            if (!Global.checkFlag(Flag.Reyka) && Global.checkFlag(Flag.blackMarketPlus)) {
                Global.gui()
                      .message("<i>\"Let me tell you the story of a succubus named Reyka. So a while back a competitor decides it's a good idea to save up a bunch of money and buy the most "
                                      + "powerful summoning scroll on the black market and feed the demon enough mana to keep her fighting for several matches. You can imagine how well that worked. He summoned "
                                      + "a succubus, Reyka, but after he gave her all his energy, she became way too powerful for him to control. She went on a bit of a rampage, draining men until she was strong "
                                      + "enough to sever the link between her and the summoner. No one was seriously hurt, but it did cause a major fuss. Rin isn't allowed to sell such strong items anymore without "
                                      + "special permission. The summoner was suspended from matches for a couple weeks. Someone with some pull must have taken a liking to Reyka though, since no one has tried to "
                                      + "banish her and someone's paying her living expenses. She's pretty aggressive, but probably not actually dangerous... probably.\"</i><p>");
                Global.gui()
                      .choose(this, "Reyka: $1000");
            }
            if (!Global.checkFlag(Flag.Kat) && Global.checkFlag(Flag.magicstore)) {
                Global.gui()
                      .message("<i>\"So last year Kat (funny name, you'll see why later) was responsible for a couple of all time firsts. The first 'first'.... The first unprecedented event "
                                      + "was an actual virgin joining in a match. She had never had sex before and may not have even masturbated before if the rumors are true. Her first match, she became everyone's toy. Absolutely everyone "
                                      + "could dominate her and she came so many times she couldn't think straight. She was so out of it that her opponents paused the match to make sure she got back to her room safely. "
                                      + "That was the second 'first'. The next night, she was back again and didn't do much better. She was consistently the weakest competitor, but she was so cute and earnest that "
                                      + "the other players became very protective of her. She could manage some wins when her opponents underestimated her. She eventually saved up enough money to get turned into a catgirl "
                                      + "by channelling an animal spirit. She's got cat ears, a tail, a kinda cute verbal tic, the whole deal. The cat instincts have made her a lot more capable of holding her own "
                                      + "in a match. She never became the strongest in her year, but she'd be a good match for yours. Just don't dismiss her because she looks young. She is actually a year older "
                                      + "than you.\"</i><p>");
                Global.gui()
                      .choose(this, "Kat: $1000");
            }
            for (Character c : Global.allNPCs()) {
                if (c.isCustomNPC() && !Global.everyone()
                                              .contains(c)) {
                    NPC npc = (NPC) c;
                    RecruitmentData data = npc.getRecruitmentData();
                    if (data.requirement.stream()
                                        .allMatch((req) -> req.meets(null, player, null))) {
                        Global.gui()
                              .message("<i>\"" + data.introduction + "\"</i><p>");
                        customNPCChoices.put(data.action, npc);
                        Global.gui()
                              .choose(this, data.action);
                    }
                }
            }
            if (!Global.checkFlag(Flag.Eve) && Global.checkFlag(Flag.blackMarketPlus) && player.getRank() >= 2) {
                Global.gui()
                      .message("<i>\"Eve Ranger is... a lot of things, but mostly a cautionary tale. "
                                      + "She started the same year I did. From the beginning, it was obvious"
                                      + " she had more potential than any of us, She could have become a "
                                      + "dominant player, if she cared about actually winning, but instead "
                                      + "she focused almost obsessively on enhancing her own pleasure. She "
                                      + "spent most of her winnings on giving herself a bunch of new fetishes"
                                      + " so she can get off on almost anything. She got herself a black"
                                      + " market cock and even a set of testicles. They're a liability, as "
                                      + "I'm sure you've experienced more than once (not that I'd ever consider"
                                      + " lopping mine off), but she added them just to make her ejaculations "
                                      + "more satisfying. She's never taken the matches seriously and only "
                                      + "tries to win to indulge in her sadism. I'm mentioning her because "
                                      + "you guys can probably compete with her if she's just fucking around. "
                                      + "To be honest though, I'd be perfectly happy to never deal with her "
                                      + "again.\"</i><p>");

                Global.gui()
                      .choose(this, "Eve: $1000");
            }
            Global.gui()
                  .choose(this, "Back");
            return;
        }
        if (customNPCChoices.containsKey(choice)) {
            NPC npc = customNPCChoices.get(choice);
            RecruitmentData data = npc.getRecruitmentData();
            try {
                Character copy = player.clone();
                copy.finishClone(null);
                if (data.effects.stream()
                                .allMatch((effect) -> effect.execute(null, copy, null))) {
                    data.effects.stream()
                                .forEach((effect) -> effect.execute(null, player, null));
                    Global.gui()
                          .message("<i>\"" + data.confirm + "\"</i>");
                    acted = true;
                    Global.newChallenger(npc.ai);
                } else {
                    Global.gui()
                          .message("You cannot pay the cost.<p>");
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        if (choice.equals("Reyka: $1000")) {
            if (player.money >= 1000) {
                player.money -= 1000;
                Global.gui()
                      .message("<i>\"Ok, I'll talk to Reyka. She spends a lot of nights surfing the internet, but I'm sure she wouldn't mind an opportunity for some free prey.\"</i>");
                acted = true;
                Global.newChallenger(Global.getNPCByType(new Reyka().getType()).ai);
                Global.flag(Flag.Reyka);
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Kat: $1000")) {
            if (player.money >= 1000) {
                player.money -= 1000;
                Global.gui()
                      .message("<i>\"Pleasure doing business with you. Just be nice to Kat. She's very catlike and confident when she's turned on, but during the day or after climax, she's "
                                      + "just an ordinary girl. Besides, if her fans hear that you've been mean to her, they'll probably kick your ass. That includes me, by the way.\"</i>");
                acted = true;
                Global.newChallenger(Global.getNPCByType(new Kat().getType()).ai);
                Global.flag(Flag.Kat);
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Eve: $1000")) {
            if (player.money >= 1000) {
                player.money -= 1000;
                Global.gui()
                      .message("You think you see Aesop flinch slightly, but he does a "
                                      + "decent job hiding it. <i>\"I was kinda hoping not to have to talk to"
                                      + " her anytime soon. Nah, don't worry about it. It's what you're paying"
                                      + " for after all.\"</i>");

                acted = true;
                Global.newChallenger(Global.getNPCByType(new Eve().getType()).ai);
                Global.flag(Flag.Eve);
            } else {
                Global.gui()
                      .message("You don't have enough money<p>");
            }
        }
        if (choice.equals("Competition Info")) {
            String message = "<i>\"You want to know how the competition is doing? I can give you a breakdown on each of your opponents:\"</i><p>";
            for (Character npc : Global.everyone()) {
                if (!npc.human()) {
                    message = message + npc.dumpstats(false) + "<p>";
                }
            }
            Global.gui()
                  .message(message);
        }
        if (choice.equals("Help with Addiction")) {
            Addiction add = Global.getPlayer()
                                  .getStrongestAddiction()
                                  .get();
            String message = "You tell Aesop about the feelings you've been having"
                            + " lately, asking if he can do anything to help. <i>" +
                            add.informantsOverview();
            if (!Global.checkFlag(Flag.AddictionAdvice)) {
                message += "\n\nAnyway, if you want to get rid of it, I might have got an address for you."
                                + " Being as kind as I am, I'll give it to you for free. You know, help"
                                + " a pal in need and all. That's not to say it isn't going to cost you, though.</i>";
                Global.flag(Flag.AddictionAdvice);
            }
            Global.gui().message(message);
        }
        if (!Global.checkFlag(Flag.basicStores)) {
            Global.gui()
                  .choose(this, "Purchasing supplies: Free");
        }
        if (!Global.checkFlag(Flag.girlAdvice)) {
            Global.gui()
                  .choose(this, "The Competition: Free");
        }
        if (!Global.checkFlag(Flag.meditation)) {
            Global.gui()
                  .choose(this, "Sharpening the mind: $200");
        }
        if (Global.checkFlag(Flag.basicStores) && !Global.checkFlag(Flag.blackMarket)) {
            Global.gui()
                  .choose(this, "Black Market: $400");
        }
        if (Global.checkFlag(Flag.girlAdvice) && !Global.checkFlag(Flag.CassieKnown)) {
            Global.gui()
                  .choose(this, "Cassie: $300");
        }
        if (Global.checkFlag(Flag.girlAdvice) && !Global.checkFlag(Flag.AngelKnown)) {
            Global.gui()
                  .choose(this, "Angel: $300");
        }
        if (Global.checkFlag(Flag.girlAdvice) && !Global.checkFlag(Flag.MaraKnown)) {
            Global.gui()
                  .choose(this, "Mara: $300");
        }
        if (Global.checkFlag(Flag.girlAdvice) && !Global.checkFlag(Flag.JewelKnown)) {
            Global.gui()
                  .choose(this, "Jewel: $300");
        }
        if (Global.checkFlag(Flag.rank1)) {
            if (Global.checkFlag(Flag.blackMarket) && !Global.checkFlag(Flag.blackMarketPlus)) {
                Global.gui()
                      .choose(this, "Exotic Artifacts: $2500");
            }
            if (!Global.checkFlag(Flag.bodyShop)) {
                Global.gui()
                      .choose(this, "Body Shop: $2500");
            }
            if (Global.checkFlag(Flag.meditation) && !Global.checkFlag(Flag.dojo)) {
                Global.gui()
                      .choose(this, "Martial Arts: $2500");
            }
            if (!Global.checkFlag(Flag.workshop)) {
                Global.gui()
                      .choose(this, "Advanced Equipment: $2500");
            }
            if (!Global.checkFlag(Flag.magicstore)) {
                Global.gui()
                      .choose(this, "Magic?: $2500");
            }
            if (newCharacters()) {
                Global.gui()
                      .choose(this, "More Competitors");
            }
        }
        if (Global.checkFlag(Flag.girlAdvice)) {
            Global.gui()
                  .choose(this, "Competition Info");
            Global.gui()
            .choose(this, "Select Competitors");
        }
        if (Global.getPlayer()
                  .checkAddiction()) {
            Global.gui()
                  .choose(this, "Help with Addiction");
        }
        Global.gui()
              .choose(this, "Leave");
    }

    @Override
    public void shop(Character npc, int budget) {

    }

    public boolean newCharacters() {
        return Global.checkFlag(Flag.rank1);
    }
}
