package nightgames.daytime;

import java.util.ArrayList;

import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;

public class VideoGames extends Activity {
    private boolean paid;

    public VideoGames(Character player) {
        super("Play Video Games", player);
    }

    @Override
    public boolean known() {
        return Global.checkFlag(Flag.metBroker);
    }

    @Override
    public void visit(String choice) {
        Global.gui().clearText();
        Global.gui().clearCommand();
        if (choice.equals("Start")) {
            if (player.money >= 50) {
                Global.gui().message(
                                "Do you want to purchase a new game? Your old games are still good, but you're unlikely to learn as much from replaying them.");
                Global.gui().choose(this, "Yes: $50");
                Global.gui().choose(this, "No");
            } else {
                visit("No");
            }
        } else if (choice.equals("Leave")) {
            done(true);
            return;
        } else {
            if (choice.startsWith("Yes")) {
                player.money -= 50;
                paid = true;
            } else if (choice.startsWith("No")) {
                paid = false;
            }
            showScene(pickScene());
            if (paid) {
                if (Global.random(3) == 0) {
                    Global.gui().message("<br/><br/><b>You feel like your experiences have grown from playing the game.</b>");
                    player.gainXPPure(50);
                    player.levelUpIfPossible(null);
                }
            }
        }
    }

    @Override
    public void shop(Character npc, int budget) {
        if (Global.random(5) == 0) {
            npc.availableAttributePoints += 1;
        }
    }

    private void showScene(Scene chosen) {
        switch (chosen) {
            case basic1:
                Global.gui().message(
                                "You decide to spend the afternoon playing an FPS on your WeeX3. Snipers, grenades and rattling of machine gun fire overwhelms you, leaving you languishing at the bottom of the scoreboard. If everything you heard online was to be believed your mother has a more adventurous sex life than you!");

                break;
            case basic2:
                Global.gui().message(
                                "You grab a soda and slump down in front of your WeeX3, booting up a new racing game. It doesn't take you long to take the pole position, turning almost every corner with perfect precision. You finish first place, pumping your fist in the air.");

                break;
            case basic3:
                Global.gui().message(
                                "You take some time to chill after your lectures, turning on your WeeX3 and putting in your favorite sports game. Picking Brazil you proceed to dominate Argentina three goals to nil. Unfortunately Uganda overwhelms you at the next stage, knocking you out.");

                break;
            case porn1:
                Global.gui().message(
                                "You turn on your WeeX3 and debate which game to play today. You're feeling in the mood for something a bit more lively. You pull your cheap red curtains across your window, shrouding the room in near darkness before reaching under your bed and taking out a game from your secret stash. You pop it in the disc tray, noting the bold red '18+' on the front of the box. The next hour passes by in a blur as you seduce, fight, and fuck your way through legions of monsters.");

                break;
            case cassie1:
                Global.gui().message(
                                "You settle down to play some video games. Just after you turn on your console through your phone starts buzzing. It's a text from Cassie asking if she can come over to hang out. Barely five minutes after you replied you hear a gentle knock at your door. You open it and she flits inside excitedly, her eyes darting around the room, examining your place.<br/><br/><i>\"So this is where you live...\"</i> she murmurs, her voice trailing off as she sees your game collection, diving down on her hands and knees to thoroughly inspect it.<br/><br/><i>\"Nope. Nope. Nope... Ahh! Let's play this,\"</i> she announces, raising this year's modern FPS with a beaming smile. You nod in approval and she pops it in the disc tray, grabbing a second controller and snuggling in beside you on the couch. A couple of co-op matches later and she's wrapped her legs around yours. You can feel the pleasant warm glow of her body radiating into you as blasts of bombs and clicks of controllers flood the room. She's very good, and manages to save your ass more times than you can count. Towards the end of the final mission though she gets too far ahead and is suddenly grabbed by two stereotypically evil mercenaries.<br/><br/><i>\"Oh no! Help!\"</i> she squeals, elbowing you in the side to get your attention.<br/><br/>You sprint forward, taking aim down your iron sights and reduce both of the hired guns to a red mist, freeing up her avatar to make the final push and complete the objective. Huge gold letters shine across the screen, \"YOU'RE WINNER!\"<br/><br/><i>\"My hero!\"</i> she beams, tossing the controller on the carpeted floor and wrapping her arms around your neck in a big hug. Her lips connect with your cheek and plant a soft, sweet kiss before nuzzling into your shoulder and closing her eyes. You carefully balance your controller on the thin arm of the couch and wrap your arms around her for the remainder of the hour.");

                player.gainAffection(Global.getNPC("Cassie"), 1);
                Global.getNPC("Cassie").gainAffection(player, 1);
                Global.flag(Flag.Cassievg1);
                break;
            case cassie2:
                Global.gui().message(
                                "Just before you turn on your console you decide to text Cassie and see if she wants to join you. She responds almost immediately and arrives a few minutes later. She's looking particularly cute today in a  short black skirt and a white tank top.<br/><br/><i>\"Shall we do versus mode today? How about we up the stakes this time? Every time you are killed you lose a piece of clothing.\"</i><br/>You agree, launching Duty Calls and letting Cassie pick the map. The two of you are soon running around an abandoned factory. There's no point in hiding, you're both on the same TV and she'll see you. You decide to grab an assault rifle and rush forward, keeping the pressure on her. Unfortunately she lands the first kill and you take off your shirt, tossing it aside.<br/><br/>As the match continues you can't help but notice Cassie becoming a bit distracted by your bare chest. Her eyes continually glance down to your abs, and you notice her biting her lower lip.<br/><br/>*Bang*<br/><br/>You score your first kill while she is distracted. You expect her to take off her top like you, but she responds unusually by unhooking her skirt.<br/><br/><i>\"What? It was short anyway! I'm not losing much.\"</i><br/>The game continues again, but only very briefly as you notice her running past a large metal generator.<br/><br/>*Bang*<br/><br/><i>\"Hey, stop looking at my screen, jerk!\"</i> she jeers, bumping into you teasingly and stripping off her shirt.<br/>Another two kills later and you begin to wonder whether she's deliberately losing, or just nervous of being nude in front of you. You look to your right and see her brightly blushing as she attempts to cover her nipples while slipping out of her panties.<br/><br/><i>\"G-guess you win!\"</i> she mutters, spreading her legs slightly in defeat. You glance down and look at her puffy vulva lips and perky breasts.<br/><br/><i>\"I'll win nex-\"</i> she begins, but is cut off mid sentence by you leaning on top of her and pressing her down into the couch with a romantic kiss directly on her lips. She blossoms under your touch and you feel her nipples stiffen as her sensitive nubs rub against your bare chest. You continue making out with her, when a devilish thought crosses your mind. You begin sucking on her neck to distract her, while looking up at the TV. You reach for the controller and go to the options menu and select 'Rumble test.' The controller begins vibrating in your hand, typically used to let you know when you've been shot during a game, but you have a far more nefarious use for it.<br/><br/>Cassie opens her eyes at the rumbling noise, <i>\"Huh, what is that? Ohhhhh!\"</i><br/>She gasps in pleasure as you slide the rounded controller handle in between her thighs to her glistening, wet pussy. The vibrations course through her body making her moan and writhe as it pushes up against her intensely delicate clitoris.<br/><br/><i>\"A-naaa... AHHHHHH!\"</i> she squeals as the beginnings of an orgasm begin to rush over her body. She buries her face between your shoulder and neck, her laboured stuttered breaths caressing down your chest. The controller continues to buzz unrelentingly against her bud until she can take it no longer. You feel a wetness spread over your hand as she reaches the pinnacle of her climax hard against the hard plastic controller.<br/><br/><i>\"Ah... ah...\"</i> she sighs, her breath returning to a normal level. <i>\"Ha... haha... you're evil,\"</i> she laughs, coming down from her climax and kissing you again.");

                player.gainAffection(Global.getNPC("Cassie"), 1);
                Global.getNPC("Cassie").gainAffection(player, 1);
                Global.flag(Flag.Cassievg2);
                break;
            case mara1:
                Global.gui().message(
                                "You've barely turned on your console when you hear three sharp knocks on your door. You weren't expecting any visitors, but are pleasantly surprised to see Mara standing there when you open it, clothed in a purple t-shirt and black leggings.<br/><br/><i>\"Jesus, I thought my place was a mess!\"</i> she jokes, lifting a pair of your unwashed pants from the floor and tossing them across the room. She notices the TV screen out of the corner of her eye flickering with the logo for the WeeX3.<br/><i>\"Sweet, what games have you got here? Nope. Nah, crap. Have you not got any decent strategy games?\"</i><br/>You bend over and pull out a few boxes until you come across a couple of games at the back and hand them to her. She glances over them for a few seconds before putting one of them back.<br/><i>\"Hmm... let's play Humanity.\"</i><br/>Humanity was an incredibly complex game where you guided a country through thousands of years of history. You've never been able to get your head around it, giving up on it months ago; Mara repeatedly assured you that she would help. So, with the controller in your hands, and a notepad in Mara's the two of you sat side by side and began your quest to forge a civilization together.<br/><br/>Time seems to pass almost as quickly as it does in game - Mara and you scheming, preparing, and engaging radical and genius tactics. While the AI takes their turn you place the palms of your hands around the back of her neck.<br/><br/><i>\"Huh? Wh- Ohhhh...\"</i> she gasps in pleasure as you gently draw your fingertips around her neck, massaging it softly, slowly sliding under the top of her t-shirt to do her shoulders as well.<br/><i>\"Yeah, right there, ah... go a little bit harder.\"</i><br/>You dig your fingers into her smooth black skin a little firmer this time, feeling a couple of knots melt away as her short dark hair tickles the dorsal side of your hand. Her shoulders begin to sag, and she leans back in the chair, much more relaxed now.<br/><br/><i>\"God, you're amazing at that!\"</i> she almost purrs, <i>\"But I think you'll be needing one too after what I tell you next.</i><br/>You're glad that she's enjoying this. She still needs to learn to relax. Even when she's gaming she picks the most difficult and challenging one she can find. She nods at the TV which is announcing it is your turn again, and lifts her notepad up showing a long list of calculations.<br/><br/><i>\"We need to cut the budget, our deficit is far too high. Slash military spending by half and disband the Eastern brigades.\"</i><br/>You briefly protest, shocked by how radical her plan sounds. The mathematics look probably right though, you'll be bankrupt in a few turns if your nation kept spending like it was doing. Much later, and the two of you have created a marvellous empire, spanning several continents, connected with one of the most impressive infrastructure networks in the world. Her strategical mind combined with your cunning and military tactics ensured an astounding victory.<br/><br/><i>\"We sure make a great team,\"</i> she whispers with a smile. You nod in agreement and wrap her in a huge hug, planting a kiss on her cheek; it was well played by both of you. Neither of you could have succeeded alone. As you lean over to turn off the console you think you hear her whisper something else.<br/><br/><i>\"I wish we...be more...\"</i> she cuts herself off mid-sentence, only to quickly stand up and announce that she has to go for a seminar, rushing out the door and slamming it behind her, leaving you somewhat stunned at her quick departure.");

                player.gainAffection(Global.getNPC("Mara"), 1);
                Global.getNPC("Mara").gainAffection(player, 1);
                Global.flag(Flag.Maravg1);
                break;
            case mara2:
                Global.gui().message(
                                "You boot up an FPS and slump down in your comfy chair. It'll be nice to get some relaxation in before the next match tonight. You jump into a multiplayer match and plug in your headset.<br/><br/>\"TEAM DEATHMATCH\" the TV loudly booms.<br/><br/>No sooner has the game begun than you hear a sharp rap at the door. You quickly take your headset off open it to find Mara standing there is a very short purple skirt and black t-shirt. She looks absolutely exhausted, her shoulders slumped and smile heavier than usual.<br/><br/><i>\"I kind of overworked myself again...\"</i><br/>You beckon her in before she can finish her sentence and wave her over to the couch, closing the door with a dull thud. She slumps down on the couch, closing her eyes and resting her head on the back of it. You invite her to put the headset on and take over for you.<br/><br/>She's a little hesitant at first, but soon gets into the way of the game, running and gunning her away around the map, barking commands to her team. The easiness of a no-frills first person shooter seems to do her some good, and you see her begin to return to the Mara you know and love from the nightly competition. As you sit on the floor in front of her a wicked thought crosses your mind.<br/><br/><i>\"Hit the bunker! Grab that flag!\"</i><br/>You lean over to her legs and begin slowly planting wet kisses up her knee, your fingers softly grabbing her thighs and parting them as you raise your head to the inside of her thighs.<br/><br/><i>\"Keep up the a-ah-assault!\"</i><br/>You hear her voice tremor as you approach her purple panties, your cheeks brushing against her soft thighs. Then, before she can pull away, you clamp your mouth over the front of her panties, feeling her jitter slightly in surprise. With your fingers slowly trailing their way up her sensitive, ticklish thighs you breathe heavily through the fabric of her panties, blasting her vulva with a wave of your hot breath.<br/><br/><i>\"I need a snip-ohhh... Y-yeah, I'm ok, I need a sniper.\"</i><br/>She seems to have recovered pretty quickly from your first teasing of her pussy, so you decide to ramp up the pleasure and begin stabbing your tongue in quick darting motions against her panties, bumping against her clitoris through the thin cloth. You hear her breathing grow noticeably louder and become more laboured;  her panties quickly dampen as her pussy swells in anticipation of your nimble tongue. You致e got her now. You strongly doubt that she will be able to stay quiet enough for the guys playing not to hear her moans.<br/><br/>Taking a single finger you slide her panties aside, revealing her glistening wet slit. With a agonisingly slow lap, you draw your tongue from the bottom of it, straight to the top and finish with a quick circumnavigation of her clit. Her thighs clench against your cheeks while the sweet taste of her juices collect on your dancing tongue.<br/><br/>You decide to take things to the next level and begin licking her faster and faster, your tongue rapidly darting past her pussy lips and into her warm, wet depths. You curl it up along the roof, trying to stimulate her g-spot. She responds by lifting her legs up over your shoulders, giving you even better access.<br/><br/><i>\"Ah ah! oh... N-no, I just ah! hurt my foot...mmm...\"</i><br/>It's turning you on to hear Mara desperately try to hide the fact she was being pleasured while playing. You slide your tongue out of her wet hole, returning to her clitoris and start licking in a regular rhythm. Her thighs begin shaking either side of your head, trembling and clenching in undulations of ecstasy.<br/><br/><i>\"God... oh my... God! SHIT! I'M CUMMING! I'M CUMMING!\"</i> she squealed as your tongue quickened its assault on her sensitive nub. You hear the headset thud to the floor as she tears it off and wraps her hands around the back of your head, pulling you in closer.<br/><br/><i>\"F-fuck! Don't stop! Ah D-oh ohhn! stop!\"</i><br/>You obey her orders and continue to lick furiously as she rides out her orgasm, her hips bucking wildly and legs trembling uncontrollably.<br/><i>\"AHH OHHH MMMMMM!! Enough! ToOOH sensitive!\"</i><br/>You slowly stop, letting her catch her breath as you pull her panties back across and sit down beside her. She cuddles up next to you, resting her head on your chest.<br/><br/><i>\"You're the best thing that happened to me all year. I was a catherine wheel spinning out of control before I met you.\"</i><br/>Her words pull at your heartstrings, and you run your fingers through her soft, short hair, falling asleep together.");

                player.gainAffection(Global.getNPC("Mara"), 1);
                Global.getNPC("Mara").gainAffection(player, 1);
                Global.flag(Flag.maravg2);
                break;
            case cassiemara:
                Global.gui().message(
                                "With just a few hours to go until the next match you decide to kill some time and chill playing video games. You are barely able to plug your console in at the wall when you are suddenly disturbed by a sharp knock on your door. Probably Cassie or Mara again looking to hang out.<br/><br/>Or both.<br/><br/>You blink a few times, surprised that both girls showed up at your place at once.<br/><i>\"Hey, we were gonna grab a coffee, but the cafe was closed. We both need to kill an hour, so we thought we'd come and pay you a visit!\"</i> Cassie beams happily. <br/><br/>Mara nods in agreement and you stand aside, letting the two girls come into your room. Cassie sits down on the couch while Mara instinctively heads over to check out your games. You plop down beside Cassie and sigh, so much for some free time to relax.<br/><br/><i>\"Aww, that was a big sigh,\"</i> Mara simpered, wheeling around and winking at Cassie, <i>\"It's almost as if you don't want two beautiful girls in your bedroom.\"</i><br/><br/>Cassie blushes slightly at Mara's remarks before quickly composing herself and nudging you in the arm. She seems to be a bit more anxious when compliments come from girls.<br/><br/><i>\"Yeah, what's the deal? Are we not good enough for you?\"</i> Cassie joins in.<br/><br/>Your protests are met with wicked laughter from the two girls. As the banter calms down you notice them looking at each other, their eyes darting to the WeeX3 controller on the couch and the long cable leading to the console. You suddenly realise what they are thinking that try to jump to your feet.<br/><br/><i>\"Grab the lead!\"</i> Cassie yells, pouncing on you and pinning you to the couch.<br/><br/>Mara grabs the controllers cable, disconnecting it from the WeeX3 and rushing over.<br/><br/><i>\"Get his hands behind his back! Haha!\"</i> Mara'z snickers flood the room as begins preparing a lasso with the cable.<br/><br/>You struggle against the girls as best you can, but they soon wrestle you face down onto the soft carpeted floor and you feel the cool plastic of the cable wrap around your wrists, holding them behind your back. There's no way you're breaking free of a knot tied by Mara. With your hands nor firmly bound you are forced over to the swivel chair over by your desk. Their hands scurry over your bottoms stripping you of your clothing and leaving you naked from the waist down.<br/><br/><i>\"Mmm, I'll never get tired of seeing this dick,\"</i> Mara taunts as your stiff cock is freed from your underwear.<br/><br/><i>\"Spread your legs a bit more,\"</i> Cassie commands, pushing your knees apart and revealing your sensitive testicles. After this she squirms in front of you looking at you through her big blue eyes and extending her slippery wet tongue from her lips. She leans forward and slowly drags it up from the base of your shaft to the tip. As it passes over your frenulum you gasp, feeling a wave of pleasure shock through your body,<br/><br/><i>\"Mmm, looks like he's enjoying that,\"</i> you tilt your head to see Mara with a hand down the front of her panties, flicking her digits over her pussy. <i>\"Hurry up and go down on him,\"</i> she urges to Cassie.<br/><br/>Cassie locks a finger and thumb around your shaft then wraps her lips around the tip, gently sucking. After a few seconds she begins twirling her tongue like a tornado, lapping at your shaft and stimulating every single nerve.<br/><br/>You groan, striving to prevent yourself cumming too soon, but Mara has other ideas. She crawls up behind Cassie, quickly pressing her hands on the back of the poor girl's head.<br/><br/><i>\"Mppf?\"</i> Cassie moans in confusion, her mouth still sucking on your shaft.<br/><br/>Mara then pushes down, forcing her to take your whole length in her mouth.<br/><br/><i>\"MMMPFFF!\"</i> she grunts in shock as Mara forces her to deepthroat your dick, pushing her head back down every time she manages to get it up. The sensation is amazing, you only wish you could hold Cassie's head yourself if your hands weren't tied.<br/><br/>Soon Cassie has gotten into the swing of it and is sucking your whole cock by herself. <br/><br/><i>\"Look, no hands!\"</i> Mara jokes, lifting them from Cassie's head to work their magic somewhere else. She lets her fingers crawl, along your thigh, kissing up your knee and giving you goosebumps. She then, places one of her fingers in her mouth, slowly sucking on it, before taking out her saliva soaked digit and sliding her hand down past your cock, past your balls...<br/><br/>You feel it press gently against your tight sphincter, before the wetness of her finger lets it slip inside your ass. Your gasps and protests only last as long as it takes her to find your prostate, making you arch your back and spread your legs even more.<br/><br/>While Mara fingers your most sensitive pleasure point Cassie continues bobbing her head on your cock, stimulating you to the edge of a climax. Then with a few whispered words from Mara you feel your balls clench up.<br/><br/><i>\"Cum for me... big boy.\"</i><br/><br/>Your ass clenches around her finger and your cock stiffens, twitching in Cassie's mouth as you send burst after burst of thick hot cum straight down her throat. You're pleasantly surprised to find that she swallowed it. It was one of off the most mind blowing orgasms you have ever experienced in your life. As you lie back in the chair gasping and catching your breath Mara unties your hands.<br/><br/><i>\"I think we should let him get some rest before the Game tonight, you coming Cassie?\"</i><br/><br/>Cassie is coughing, still recovering from that cumshot.<br/><br/><i>\"I'm gonna get my own back on you tonight for holding my head down like that!\"</i> she jokes, half laughing. It's a good thing these two are friends.<br/><br/>The two girls then walk over and simultaneously kiss your cheeks before waving as they walk out the door.");

                Global.getNPC("Mara").gainAffection(Global.getNPC("Cassie"), 1);
                Global.getNPC("Cassie").gainAffection(Global.getNPC("Mara"), 1);
                break;
            case caroline1:
                Global.gui().message(
                                "While spending so much time with Angel, you've quickly become friends with Caroline. It helps that she's very outgoing and apparently has no hesitation about coming to a guy's room to play video games. She's also fucking good at them.<br/><i>\"That's right, mash out that easy super. I'll just block it all!\"</i> She always seems cool and calculating when you're playing analog games, but apparently fighting games are able to get her fired up. Also, she's fucking good at them.<br/><i>\"And... you're dead.\"</i> You haven't lost yet. Oh wait, yes you have. You didn't realize she could cancel her special into her level 3 super. You're 0 for 3 against her so far.<br/><br/>Ok, new strategy. Your close up game isn't working, so you switch to a ranged character in hopes of zoning her out. It works out for a little while... kinda. It's a little closer at least. Your opportunity comes when you manage to break her Persona. Her character is heavily reliant on her Persona attacks, so she's almost helpless until she recovers. You rush in to finish her off. <i>\"Nope! Not gonna happen!\"</i> She's purely on the defensive, but she keeps avoiding your throws. The chip damage alone should be enough.<br/><br/><i>\"Almost got my Persona back!\"</i> No! no! no! She has a full meter, but you're not going to let her use it! <i>\"Yes! Yes! Yes!\"</i> Her Persona recovers right before you can finish her off and she interrupts you with her super. She lets out a loud whoop as she wins the match.<br/>Come to think of it, she wins most of the analog games too.");

                Global.modCounter(Flag.CarolineAffection, 1.0F);
                break;
            case caroline2:
                Global.gui().message(
                                "Caroline comes over to play games again. This time you've got something simpler, but more interesting. <i>\"Oh sweet! You can pick up a sword by just rolling over it.\"</i><br/> She still seems to be picking this up faster than you. She's steadily pushing you back. Still, you're not doing too bad. You seem to be more consistent than her when fighting unarmed. You end up in a tense back-and-forth battle on her last screen. At the very edge of the screen, you successfully disarm her, but she rolls past you before you can react. She cheers as she reaches her goal.<br/><br/><i>\"Man that was a hell of a fight. But, at the end of the day, who got eaten by the dick shaped monster?\"</i> Yeah, she did. Still, you're pretty sure you've gotten the hang of this. You'll probably beat her this time. <i>\"You can try, but I'm all about getting that dick. The dick will be mine again!\"</i><br/>Within a couple minutes, her words seem to be ringing true. She manages to push you back to her last screen, but you're not going to let it go the same way twice. <br/><br/>You get an easy kill using a door as a chokepoint and rush to the next screen before she can respawn. On the next screen, you throw your sword, killing her again and making yourself a little faster. You rush past her again and she throws a sword at your back. You roll under it and she respawns just in time to get hit by her own sword. The momentum is firmly on your side now. She can't stop you as you rush across the length of the map and reach your goal.<br/><br/>Finally. It's not much, but you've proven that you can beat Caroline with a little luck and a lot of effort.");

                Global.modCounter(Flag.CarolineAffection, 1.0F);
                break;
            case caroline3:
                Global.gui().message(
                                "This is probably the most streamlined game you've played with Caroline, but the most hopeless for you. <i>\"Fraud detection warning! Come on, you have to win at least once.\"</i> That's easier said than done. How much has she played this game? She finishes you with a headshot, just to show off.<br/><i>\"Oh wow! Fraud detected. You got rekt!\"</i> Caroline sets down her controller so she can do a little victory dance. <i>\"I've never seen that happen before. Well, I've only played a little of this, so I guess it's not that surprising.\"</i> As she finishes her victory dance, she notices your expression. <i>\"Aww... Are you sucking cause you got beat?\"</i> You're not sulking, you're just feeling a bit salty right now.<br/><br/>You looks at you for a few seconds before apparently coming to a decision. <i>\"Okay,\"</i> she says, moving closer to you. <i>\"I bet I can cheer you up. Take off your pants\"</i> You look at her blankly, wondering if you misheard her. <i>\"Don't be shy. It's just a special service for a friend. Angel already said it was fine. Now strip.\"</i><br/>You obediently remove your pants and underwear, still not entirely sure what is happening. <i>\"Now, we should start with some ground rules. This is purely a favor for a friend, it does not imply any sort of romantic relationship. That means there's no kissing, no fucking, and you don't get to be jealous if I jerk off my other friends. Also, I offer this service if I think you need it, you don't ask me for it. If all my guy friends pester me for handjobs whenever they get a little horny, I'd never get any rest. Are we clear?\"</i><br/><br/>You nod your assent and she starts massaging your soft dick into an erection. She seems very focused on her task and she's staring at your groin with an intensity that would make even a hardened sexfighter blush. Does she do this a lot? <i>\"I guess my perverted side is showing, just in case you thought I didn't fit in with Angel or Mei.\"</i> She twists her hands around the head of your penis, making your hips buck in pleasure. <i>\"I like playing with dicks. Guys like their dicks played with. It's a total win-win situation. Of course, I don't do it for guys in a relationship unless the girlfriend approves. Like I said earlier, Angel gave the OK before I even asked her. You're lucky your girlfriend is so generous.\"</i><br/><br/>You would debate some of the complexities of your and Angel's relationship, but Caroline is making it very hard for you to talk right now. She must have a lot of practice doing this, because her handjob is good enough to rival any you've received in the Games. She quickly strokes your cock and gently plays with your balls. You're on the verge of cumming and groan out a couple words of warning. In one fluid motion, Caroline pulls a tissue out of her pocket and uses it to catch your ejaculation. <i>\"That was close. You almost made a mess.\"</i> She leans down and licks a couple stray drops of jizz off your dick. <i>\"You're welcome.\"</i>");

                Global.modCounter(Flag.CarolineAffection, 1.0F);
        }

        Global.gui().choose(this, "Leave");
    }

    private Scene pickScene() {
        ArrayList<Scene> available = new ArrayList<Scene>();
        available.add(Scene.basic1);
        available.add(Scene.basic2);
        available.add(Scene.basic3);
        available.add(Scene.porn1);
        if (player.getAffection(Global.getNPC("Cassie")) >= 5) {
            available.add(Scene.cassie1);
        }
        if (player.getAffection(Global.getNPC("Cassie")) >= 10 && Global.checkFlag(Flag.Cassievg1)) {
            available.add(Scene.cassie2);
        }
        if (player.getAffection(Global.getNPC("Mara")) >= 5) {
            available.add(Scene.mara1);
        }
        if (player.getAffection(Global.getNPC("Mara")) >= 10 && Global.checkFlag(Flag.Maravg1)) {
            available.add(Scene.mara2);
        }
        if (Global.getNPC("Cassie").getAffection(Global.getNPC("Mara")) >= 10 && Global.checkFlag(Flag.Cassievg2)
                        && Global.checkFlag(Flag.maravg2)) {
            available.add(Scene.cassiemara);
        }
        if (player.getAffection(Global.getNPC("Angel")) >= 10) {
            available.add(Scene.caroline1);
        }
        if (Global.getValue(Flag.CarolineAffection) >= 3.0F) {
            available.add(Scene.caroline2);
        }
        if (Global.getValue(Flag.CarolineAffection) >= 10.0F) {
            available.add(Scene.caroline3);
        }
        return available.get(Global.random(available.size()));
    }

    private static enum Scene {
        basic1,
        basic2,
        basic3,
        porn1,
        cassie1,
        cassie2,
        mara1,
        mara2,
        caroline1,
        caroline2,
        caroline3,
        cassiemara;
    }
}
