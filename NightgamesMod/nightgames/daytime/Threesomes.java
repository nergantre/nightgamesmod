package nightgames.daytime;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;

public class Threesomes extends DaytimeEvent {

    public Threesomes(Character player) {
        super(player);
        if (Global.getNPC("Cassie")
                  .getAffection(player) >= 20
                        && Global.getNPC("Jewel")
                                 .getAffection(player) >= 20
                        && Global.getNPC("Jewel")
                                 .getAffection(Global.getNPC("Cassie")) >= 5) {
                        //TODO && Global.getValue(Flag.CassieDWV) >= 5) {
            register("CassieJewel", 5);
        }
        if (Global.getNPC("Mara")
                  .getAffection(player) >= 20
                        && Global.getNPC("Jewel")
                                 .getAffection(player) >= 20
                        && Global.getNPC("Jewel")
                                 .getAffection(Global.getNPC("Mara")) >= 5) {
            register("MaraJewel", 5);
        }
        if (Global.getNPC("Mara")
                  .getAffection(player) >= 15
                        && Global.getNPC("Angel")
                                 .getAffection(player) >= 15
                        && Global.getNPC("Angel")
                                 .getAffection(Global.getNPC("Mara")) >= 10) {
            register("AngelMara", 5);
        }
        if (Global.getNPC("Mara")
                  .getAffection(player) >= 15
                        && Global.getNPC("Cassie")
                                 .getAffection(player) >= 15
                        && Global.getNPC("Cassie")
                                 .getAffection(Global.getNPC("Mara")) >= 10) {
            register("CassieMara", 5);
        }
    }

    protected void play(String choice) {
        switch (choice) {
            case "CassieJewel":
                Global.gui()
                      .message("You step out of the shower room with a towel wrapped around your waist to preserve your modesty. One of these days you should get a proper bathrobe, "
                                      + "but the showers aren't far from your room. When you reach your room, you notice your door is ajar. That's odd. You didn't lock the door because you aren't carrying "
                                      + "your keys, but you thought you had at least closed it. Maybe not. You go inside to get a change of clothes, when suddenly you feel your towel get yanked right off. "
                                      + "Before you have a chance to react, you're tackled from behind. Your unseen attacker is tall, strong, and - judging by the soft breasts pressing against your back - "
                                      + "female. She wraps her arms around you and grabs your balls firmly, effectively eliminating your capacity to resist. You're captured, but now you have time to take "
                                      + "stock of the situation. You can't turn your head far enough to see her face, but based on her build and the bright red hair you can see out of the corner of your eye, "
                                      + "your attacker is probably Jewel.<p>"
                                      + "<i>\"Boy acquired!\"</i> You hear her call out. Cassie steps into the room, looking nervous and apologetic. <br><i>\"I still don't know "
                                      + "what this accomplishes. It's not like " + player.name()
                                      + " was avoiding us. We could have just knocked on the door. Also you should be careful with what you're holding. "
                                      + "Those are a couple of anatomically vital parts.\"</i> <br>You hear Jewel chuckle as she gently plays with your sack. <i>\"Don't worry. I'm being very nice to all "
                                      + "of " + player.name()
                                      + "'s parts, aren't I?\"</i> She's enjoying herself so much she's practically purring in your ear. <p>"
                                      + "You don't mind indulging Jewel and Cassie a "
                                      + "bit, since you trust them not to do anything really bad, but it would be nice to know what is going on. Cassie fidgets awkwardly. <i>\"I'm not entirely sure. It's "
                                      + "complicated.\"</i> <br>"
                                      + "Jewel presses herself against your back more firmly. You can feel her body heat through her clothes. <i>\"Cassie came to me to ask for help. She was "
                                      + "so troubled, she was practically in tears.\"</i> Cassie starts to sputter out a protest, but Jewel continues. <i>\"She's been lonely since you're not spending time "
                                      + "with her. How could I refuse a lonely maiden, neglected by her beloved?\"</i> Has it been that long since you went to visit Cassie? You hadn't realized. You'll "
                                      + "need to make it up to her, but Cassie was right, this could have been resolved by just talking to you. <i>\"The reason I captured "
                                      + player.name() + " like this is because "
                                      + "it's not enough to wait for someone to give you what you want. You need to take it.\"</i> You can't see her face, but she's clearly talking to Cassie now. <i>\"Come "
                                      + "here. Get a good grip on " + player.name()
                                      + "'s 'vital parts'.\"</i> Cassie kneels over you and hesitantly grasps your balls. <i>\"Now he'll listen to whatever you say. Claim "
                                      + "him and he'll be all yours.\"</i><p>"
                                      + "<i>\"What?!\"</i> Cassie looks at Jewel in shock. <i>\"That's incredibly coercive. I don't want "
                                      + player.name() + " to pay attention to me "
                                      + "just because I threatened to hurt him. Besides, what about you and the other girls? Right before you came here, you were telling me that you like him too. How would you "
                                      + "feel if I tried to monopolize him?\"</i><p>"
                                      + "Jewel moves enough that you can see her face. She's got a confident grin and a challenging gleam in her eye. <i>\"You can try to monopolize him all you want. Me and the "
                                      + "other girls will just steal him back if we need a little love. Also, you've misunderstood the purpose of holding the boy's most important parts. it's not about the threat "
                                      + "of pain. You're just demonstrating your superiority. He's not afraid you're going to hurt him, he just can't resist you. You may call it forceful, but you can't claim he "
                                      + "doesn't like it. You've been shamelessly staring at his boner for awhile now.\"</i><br>"
                                      + "You groan as you realize Jewel is right. Maybe it's the attention your genitals are getting, the feel of Jewel pressed against your naked body, or the idea of Cassie acting "
                                      + "uncharacteristically bold. Whatever the reason and despite the bizarre situation, you're rock hard.<p>"
                                      + "Cassie bites her lip in indecision for several seconds, before shaking her head. She releases her grip on your testicles and starts to stroke your dick. Jewel doesn't seem "
                                      + "to mind this change of plan. She licks your neck and caresses your body, increasing your pleasure. Cassie lies down next to you and kisses you tenderly. <i>\"Sorry, I'm going "
                                      + "to be greedy.\"</i> She speaks in a whisper, her face mere inches from yours. <i>\"I don't want to claim you, but I want you to choose me. I love the way you make me feel "
                                      + "and I love making you feel good. I won't get jealous if you're spending time with other girls, but I do get lonely. Don't forget about me, OK?\"</i> You're starting to leak "
                                      + "precum from her touch, as she starts to strip off her own clothes. Jewel lets you go and guides you to lie on your back, while Cassie positions herself above your erection. "
                                      + "<i>\"In case you've forgotten how good we are together, I'll give you a reminder.\"</i><p>"
                                      + "Cassie slowly lowers her hips, impaling herself on your cock. You let out a low groan as her warm, tight folds surround you. She leans down and kisses you romantically. "
                                      + "<i>\"It's a perfect fit. I wish we could keep kissing, but I'm not the only one here who needs attention. I know Jewel was a little rough with you, but it's only because "
                                      + "she likes you too. Do you mind if she joins in?\"</i> <br>"
                                      + "Jewel looks hesitant for the first time. <i>\"Are you sure? The point of this was to give you two some alone time. I can wait.\"</i><br>"
                                      + "Cassie shakes her head. <i>\"I just said I wouldn't get jealous. I want to thank you for your help. "
                                      + player.name() + " is very good with his tongue, I can personally vouch "
                                      + "for that.\"</i> Jewel glances at you for confirmation and you lick your lips suggestively. She strips off her clothes and straddles your face. You grab Jewel's hips and thrust "
                                      + "you tongue into her slit, making her moan and almost lose her balance. You've been at Jewel's mercy since she ambushed you, so it's pretty satisfying to break her composure "
                                      + "with a lick.<p>"
                                      + "Your own composure is cracked soon afterward as Cassie starts moving. If your mouth wasn't covered, you'd moan audibly. The girls got you so turned on before you even started "
                                      + "the main event. The way Cassie is riding you, you won't be able to hold out for long. You can only hope they are somehow as aroused as you. Jewel gasps in pleasure and you feel "
                                      + "her shudder out of sync to your oral efforts. Looking up, you see Cassie's hands fondling her breasts and pinching her sensitive nipples. With this additional stimulation, you "
                                      + "can at least imagine that Jewel may finish when you do. You can't see Cassie, but you at least hear her breathy moans.<p>"
                                      + "You resist cumming as long as you can, but Cassie's pussy just feels too good. Your hips buck involuntarily as her vaginal walls squeeze you. You shoot hot jets of spunk into "
                                      + "her love canal. Jewel shudders in orgasm above you as she clutches your head desperately. At least two of you are satisfied, but you have no idea whether Cassie came or not. If "
                                      + "she hasn't you're not going to let her go anywhere until you've shown your appreciation.<p>"
                                      + "Both girls cuddle up next to you to enjoy the afterglow. Cassie sees the question in your eyes before you say anything and she smiles at you. <i>\"We orgasmed together. Feeling "
                                      + "you cum inside me, I'd have to try really hard to not orgasm from that alone.\"</i> She kisses you gently on the cheek. <i>\"I told you we were a perfect fit, but we make a good "
                                      + "trio too, don't we?\"</i>");
                break;
            case "CassieAngel":
                Global.gui()
                      .message("");
                break;
            case "MaraJewel":
                Global.gui()
                      .message("You're sitting in your room, working on some homework, when you hear a knock on your door. You open the door to find Mara, who pushes past you into "
                                      + "your room. <i>\"Sorry to bug you. Well... not that sorry. I'm promise this is more exciting than whatever you were up to. I briefly considered using Jett for this, but "
                                      + "I thought you would be much more fun. Also, if he gets mad at me and refuses to teach me anything, I'd be kinda screwed. I guess it would also be bad if you "
                                      + "held a grudge, but I know you won't hate us, no matter what we do, right?\"</i> Wait. She's all over the place and none of what she just said actually explains "
                                      + "why she's here. <i>\"Yeah, I know. To be honest, I was just trying to keep your attention away from the door while she sneaks up behind you.\"</i><p>"
                                      + "Before you can turn around, Jewel "
                                      + "grabs you from behind and forces you to the floor. She quickly secures your arms and sits on your shoulders. Mara helps hold your legs, but really surprise is the "
                                      + "only help Jewel needed. You end up awkwardly pinned face up under her thighs with your head between her legs. The position keeps your from moving your arms effectively, "
                                      + "but lets you clearly see what's happening. It doesn't however, help you understand why it's happening. Jewel grins mischievously at Mara. <i>\"The bully is "
                                      + "helpless. Next we remove his pants.\"</i> Mara nods and pulls down you pants and underwear, exposing your flaccid dick. Why are you being stripped? Also, why have "
                                      + "you suddenly been labeled a bully? Jewel strokes your head affectionately as Mara explains. <i>\"Sorry, I guess I didn't actually explain earlier. Jewel was telling "
                                      + "me stories about her school days, when she used to defeat and humiliate bullies. Really exciting stories, but we needed a boy for our reenactment. You're playing the "
                                      + "bully, I'm the girl you were picking on, and Jewel is herself. Her specialty was helping girls get revenge against the boys who were bullying on them.\"</i><br>"
                                      + "Jewel smiles down at you, not unkindly. <i>\"We appreciate your cooperation. I promise we'll make it up to you later.\"</i> Well, at least you know what's going on now. You're still "
                                      + "pinned, so your continued cooperation is probably not up to you. <br>"
                                      + "Mara prods your penis playfully. <i>\"His pants are dealt with, what's next?\"</i><br>"
                                      + "<i>\"You're the girl who was wronged. He's helpless and you have free access to his most important parts. It's up to you whether to punish or pleasure him.\"</i><br>"
                                      + "Mara looks up at Jewel surprised. <i>\"Pleasure? I thought this was revenge. Did that ever happen?\"</i><br>"
                                      + "<i>\"Sometimes the boys were really cute. Besides, being held down and forced to orgasm in front of a crowd is humiliating enough to serve as revenge. Many boys can't accept "
                                      + "that they'll get aroused and ejaculate while being dominated. Punishment was more common, but we like this boy, don't we?\"</i><br>"
                                      + "Mara giggles and starts to stroke your dick. <i>\"We like this boy a lot. Besides, he's already hard from listening to us. Pleasure it is.\"</i><p>"
                                      + "Pleasure is certainly happening. A soft groan escapes you as Mara's nimble fingers go to work. You struggle to keep your hips still as she strokes and teases all the "
                                      + "right areas. She sticks out her tongue explores your sensitive glans. Jewel smiles gently and caresses your cheek. <i>\"Does that feel good? It's only because Mara chooses it to.\"</i> "
                                      + "The girl in question wraps her lips around your shafts and starts to suck, causing your hips to jerk despite your attempts to remain still. <i>\"That petite girl has you "
                                      + "completely at her mercy. She could give you as much pleasure or pain as she wants. You should prostrate yourself in gratitude for her kindness.\"</i><p>"
                                      + "Mara releases your dick to smirk at Jewel <i>\"Your verbal torture would probably be more convincing if you weren't stroking his hair like a newlywed wife. Did you do that "
                                      + "for every bully?\"</i><br>"
                                      + "Jewel leans down, pressing her breasts against your face before sitting up. <i>\"Most bullies don't deserve as much affection as "
                                      + player.name() + ". I just wish I was a "
                                      + "little more flexible so I could kiss him from this position. Speaking of kissing things, you're going a little above and beyond with that blowjob. You're supposed to humiliate "
                                      + "him by jerking him off.\"</i><br>"
                                      + "Mara rubs her cheek happily against your shaft. <i>\"I just want to give him an extra special orgasm as thanks for helping us out. Besides, I don't think a handjob is going to "
                                      + "embarrass him anyway.\"</i> That's true. Getting held down and made to cum is a nightly event for you. At this point, it's not going to bruise your pride. <i>\"It's probably "
                                      + "because we don't have a crowd. Should we open the door and try to attract an audience? I'm sure I could find a lot of girl who'd like to watch.\"</i> Oi. That's going a bit "
                                      + "too far. They're just pretending you bullied them after all.<p>"
                                      + "Jewel ignores your protest and giggles. <i>\"It's tempting, but I don't want to share "
                                      + player.name()
                                      + "'s money shot with a bunch of strangers. I think we should be greedy and "
                                      + "keep this show to ourselves.\"</i><br>"
                                      + "They don't have to wait too long for their show. Jewel's other victims probably didn't have to endure the efforts of an experienced sex fighter. Mara's mouth and hands quickly "
                                      + "bring you to the edge. She takes her mouth off you when she sense your orgasm building and starts to pump your shaft with both hands. You let out a low moan as your cum shoots "
                                      + "into the air like a fountain, to the amusement of both girls.<p>"
                                      + "Jewel lets you out of the pin and helps massage the kinks out of your shoulders. Mara busies herself cleaning up your semen with her tongue. You lie on the floor, feeling too "
                                      + "exhausted to walk to your bed, until both girls cuddle up next to you. <i>\"Thanks for being a good sport. Most trips down memory lane don't get me this wet.\"</i> Jewel kisses "
                                      + "your cheek and guides your left hand down the front of her pants so you can feel her arousal.\"</i> Mara does the same with your right hand. <br>"
                                      + "<i>\"You seem to have two super-horny girls in your room who owe you a favor. How will you possibly manage?\"</i>");
                break;
            case "CassieMara":
                Global.gui()
                      .message("You return to your room after a brief errand. As you reach for the doorknob, the muffled sound of female voices inside makes you "
                                      + "hesitate. It seems that at least two girls have infiltrated your room in your absence. You left the door unlocked, didn't you? Well, whatever "
                                      + "happens, you have only yourself to blame.<p>"
                                      + "You open the door, revealing Cassie and Mara in your bed. Cassie yelps in surprise and dives under the covers, but not before you got a good eyeful "
                                      + "of her bare breasts. Mara, on the other hand, hops out of bed and scampers over to you as naked as the day she was born.<p>"
                                      + "<i>\"Ah! You're back early! We were preparing a little surprise for you, but we need a couple more minutes.\"</i><br>"
                                      + "She doesn't appear at all bothered by her nudity, but Cassie starts to freak out a bit.<p>"
                                      + "<i>\"Mara! You're standing in the doorway to a public hallway completely naked! Get back in here and close the door before someone sees you!\"</i><p>"
                                      + "The smaller girl stands on her toes to give you a light peck on the lips as she gently nudges you out of the room. <i>\"Just give us two minutes to "
                                      + "finish getting ready. I promise you'll love it.\"</i> She closes the door and you hear her footsteps trot back to the bed.<p>"
                                      + "You wait outside your own room for about five minutes before Mara signals to come in. Inside you're greeted by a pair of lovely treats. Mara's "
                                      + "nudity is tactically covered by a modest helping of whipped cream. The white cream contrasts dramatically with her dark skin. Beside her, Cassie's "
                                      + "naked body is decorated with copious amounts of melted chocolate. You see finger marks where it appears her accomplice was trying to draw flower "
                                      + "patterns in the dessert, but maybe she was just trying to get a taste.<p>"
                                      + "You're pretty sure it isn't anyone's birthday today. What's the occasion? Both girls giggle at the question. <i>\"We originally talked about doing this "
                                      + "for your birthday,\"</i> Cassie explains. <i>\"But someone got impatient and said we should just do it now.\"</i><p>"
                                      + "Mara gives a carefree shrug. <i>\"Why do we need to wait for a special occasion to do something nice for our favorite boy? Anyway, hurry up and dig in! "
                                      + "this whipped cream won't stay solid very long at body temperature.\"</i><p>"
                                      + "You have to start somewhere and Mara's situation appears a bit more urgent. You briefly entertain the idea of asking the girls to mix their toppings "
                                      + "together with a little intimate contact, but it would probably make a huge mess. Besides, Cassie isn't into other girls, so she wouldn't enjoy it as "
                                      + "much as you and Mara. You give her a tender kiss and promise you'll attend to her soon, then you turn your attention to the whipped cream on Mara's belly.<p>"
                                      + "Mara squirms and giggles as you lick her slim midriff clean. You leave the bits of cream protecting her modesty for now and instead focus on her sensitive "
                                      + "bellybutton. She gives up trying to hold back her voice and squeals with laughter.<br>"
                                      + "<i>\"You're tickling me on purpose! Quit teasing me and get to the good parts!.\"</i> Is she ready for something more stimulating? There's a way to find out.<p>"
                                      + "You cover her small breast with your mouth, quickly licking away the whipped cream and locating her hard nipple. She lets out a tiny yelp of surprise and "
                                      + "pleasure at your sudden assault on her sensitive nub. You give it a good licking and a quick nibble before moving on to the other one. Mara is practically a "
                                      + "melted puddle of shivering pleasure by the time you turn your attention to the remaining whipped cream concealing her slit.<p>"
                                      + "As Cassie looks on in obvious anticipation, you move between Mara's legs and start licking her pussy lips clean. She moans with abandon as you delve into her "
                                      + "wet folds. She's on the brink of orgasm in record time, so you focus on her engorged clit and give it a strong tongue-lashing. She shudders uncontrollably and "
                                      + "Cassie actually has to cover her mouth to keep her scream from echoing through the entire building.<p>"
                                      + "She goes completely limp and struggles to catch her breath. <i>\"Oh God. That was so much hotter than I imagined. I couldn't even hold out long enough to get "
                                      + "the cock.\"</i> Her voice is heavy with exhaustion and satisfaction, but there's a touch of regret. <i>\"Oh well. I don't mind watching you get fucked, Cass. "
                                      + "He's obviously ready to go, and I'm willing to bet you are too.\"</i><p>"
                                      + "The girls turn their attention to your obvious erection and Cassie flushes red.<br>"
                                      + "<i>\"W-well, I'm not going to turn down my favorite penis. If you want to skip to the main event, I don't mind. "
                                      + "But...\"</i> She squirms with uncertainty and arousal. <i>\"It took a while to spread all this chocolate on me and I was looking forward to getting it licked off....\"</i> "
                                      + "She trails off, looking at Mara's content expression. This may have been Mara's idea, but Cassie is clearly excited about this naked dessert idea. Well, you don't "
                                      + "mind putting of your own satisfaction for a bit. Beside, when you look at Cassie's chocolate-covered body, the idea of eating your dessert first is pretty tempting.<p>"
                                      + "You set to work cleaning the melted chocolate off her sensitive skin. She's not as ticklish as Mara, but even more turned on. She's soon trembling and letting out "
                                      + "little whimpers of pleasure. There's a problem though: Cassie's body is slathered with a ton of chocolate, and you soon feel like you can manage this quantity of "
                                      + "sugar by yourself. You are forced to abandon her upper half so you can focus on her tasty pussy. You skilled oral efforts on her sensitive vulva soon drives her "
                                      + "toward climax. Mara, having recovered from her earlier orgasm, leans over her friend's neglected breasts.<p>"
                                      + "<i>\"Looks like a couple of tasty chocolate-covered strawberries. It would be a shame to let them go to waste.\"</i> With that, she leans down to start licking and "
                                      + "sucking on Cassie's nipple. Cassie's moans increase in intensity at the second mouth targeting her erogenous zones. <br>"
                                      + "<i>\"Mara!\"</i> Cassie squeals in protest. <i>\"I'm really enjoying getting eaten out by my hot kinda-boyfriend, but you're making my body feel all confused! I don't have "
                                      + "sex with girls outside of the Games!\"</i><p>"
                                      + "Mara ignores the other girl's protests and attacks her other nipple with a grin. <i>\"Cassie, you're so cute when you're feeling good. There's no way I can resist joining "
                                      + "in. I hope you don't hate me for it.</i>\"<p>"
                                      + "Cassie squirms against the two tongues attending to her and desperately tries to speak while holding back her orgasm. <i>\"You're really cute too and I like you but-not-in-"
                                      + "a-sexual-way-so-pleasedon'tsuckonmyboobswhenI'mcumming! I'm cumming! I'M CUMMING!\"</i> Mara kisses the shuddering girl to help suppress her orgasmic shriek until she comes "
                                      + "down from her intense climax.");
                break;
            case "AngelMara":
                Global.gui()
                      .message("'Cum over ASAP.'<p>"
                                      + "The text message left you somewhat bewildered. It wasn't like Mara to be sparse with details. You quickly grab your jacket and rush out the front "
                                      + "door, perhaps she's gotten herself into trouble somehow. A few minutes later you arrive, completely out of breath knocking at Mara's door. You hear "
                                      + "some giggling from within and the door opens a few seconds later.<p>"
                                      + "Your salutation is cut short as you are pulled into the room by the neck of your shirt. Mara forces her silky, dark lips upon your own, making your "
                                      + "heart race at this unexpected sign of affection.<p>" + "*Thud*<p>"
                                      + "The door closes behind you, and as Mara breaks her kiss you turn your head to see Angel standing there, a devious smirk painted upon her creamy face. "
                                      + "She is dressed in raunchy black lingerie, with a push-up bra that further accentuates her already voluptuous breasts. A pair of thigh-high stockings "
                                      + "complete the set, enveloping each of her slender legs.<p>"
                                      + "<i>\"Mara has kindly asked me to help her train in the art of seduction. Naturally, I agreed,\"</i> she pauses briefly looking you up and down, <i>\"You "
                                      + "are going to be our test dummy. Now don't act unless I tell you to, we're the ones seducing you.\"</i><p>"
                                      + "The next words whispered from Mara's mouth trickle into your ear like golden honey, making every hair on the back of your neck stand at attention, "
                                      + "<i>\"I hope you listened to her. We got all dressed up for you, in our slutty little stockings and negligee.\"</i><p>"
                                      + "You take the chance to cast your gaze upon Mara, admiring how the red bra and panties she's selected compliment her skin-tone. To top off her outfit, "
                                      + "she has adorned a candy camisole. It is a thin garment, delicate and fragile enough that you can make it the smooth, soft surface of her skin beneath it.<p>"
                                      + "<i>\"Well what are you waiting for?\"</i> Angel inquires, <i>\"Just like we planned.\"</i><br>"
                                      + "Mara follows Angel's orders and pushes her hands up under your shirt, reaching your shoulders. Your eyes meet and her pretty lips quiver as her fingertips "
                                      + "soundly massage your tired muscles. You groan, letting out a deep sigh of relaxation and closing your eyes.<p>"
                                      + "Just as you are beginning to calm down you feel a second pair of hands from behind lift the back of your shirt, bringing it over your head and tossing "
                                      + "it aside. Now shirtless, Mara continues to massage the front of your body, trailing her fingernails lightly down your chest. Angel works from behind, "
                                      + "her lips planting a series of kisses up your spine, making your legs tremble at each sensual peck.<p>"
                                      + "As Angel reaches the nape of your neck she veers right to your earlobe and teasingly nibbles at it. Mara's fingers are barely an inch from your waistband "
                                      + "now, any second and they'll-<p>" + "She suddenly withdraws her hand.<br>"
                                      + "<i>\"Get on the bed, and strip off your bottoms. We've got a show for you\"</i> Mara winks, slapping you playfully on the cheek.<p>"
                                      + "You undress and move to the bed, laying back on a couple of pillows and spreading your legs for the girls. You aren't quite hard yet, but that isn't an "
                                      + "immediate problem. Mara and Angel face each other a few feet in front of you and lock their lips. Their hands dance over each others backs, Angel's fingers "
                                      + "crawling over Mara's derriere and clenching it. You notice that Mara is standing on her tiptoes to compensate a little for the difference in height, making "
                                      + "the scene before you all the cuter.<p>"
                                      + "Angel's fingers nimbly unclasp the back of Mara's negligee, allowing the flimsy garment to float to the floor. Her bra is the next thing to join it. Then, "
                                      + "leaning down Angel extends her tongue and begins swirling it over Mara's exposed nipples. They may be small, but you know from experience that it means they "
                                      + "are far more sensitive.<p>"
                                      + "<i>\"Ah ah!\"</i> Mara gasps in short sharp breaths, Angel's tongue clearly having an effect on her. Not to mention the effect on you, your shaft is now "
                                      + "firmly erect. You reach down and begin slowly stroking yourself while the two girls continue to perform their show of seduction for you.<p>"
                                      + "From what you gathered it seemed like Mara requested Angel to help her practice her seduction; but you have serious doubts about how genuine Mara was, "
                                      + "harbouring suspicions that she may just be seeking any excuse she can to enjoy a threesome. You don't have long to think though, for the girls soon turn "
                                      + "their attentions back to you and your stiff member. <p>"
                                      + "You lie back on the bed, half-inviting one of the girls to take your cock for a ride. Angel jumps at the opportunity, bouncing on the bed beside you and "
                                      + "straddling over your waist, parting her silk-soft panties and slipping your cock your shaft into her sweltering snug pussy.<p>"
                                      + "<i>\"Uhhhh!\"</i> she exclaims in pleasure, tossing her golden hair back and groping her breasts. Mara awkwardly hangs around the top of the bed. You "
                                      + "quickly gather what she wants to ask and open your mouth, licking your lips slowly. She takes the hint and slips her panties off, tossing them aside and "
                                      + "clambering atop the bed. You can already smell the sweet scent of her pussy as it hovers a few inches above your mouth. She slowly begins to lower herself "
                                      + "down onto your waiting tongue. The sensation of Angel bouncing on your hard cock combines with the taste of Mara's clitoris to create an amazing sensation. "
                                      + "You lap wildly at Mara's pearl, making her squirm and writhe in a fit of ecstasy.<p>"
                                      + "<i>\"Ohh ahhhh! R-right there!\"</i> Mara moans as you lightly spread her vulva with your skilled tongue. Her gasps of pleasure are suddenly silenced when "
                                      + "Angel leans over, grabbing her and pulling her into a passionate kiss, the two girls mouths locked together while they ride your body for their pleasure.<p>"
                                      + "Simultaneous orgasms happen sometimes. They're fairly rare, but you've had your fair share at your nightly matches. What amazed you was the fact that all "
                                      + "three of you came at the same time. Mara's pussy rippled and clenched over your eager tongue as you lapped up her juices. Meanwhile, Angel began grinding "
                                      + "her hips quicker and quicker, stimulating every inch of your cock. Your gut tensed up a couple of times, and you felt your dick pulsate, then you unleashed "
                                      + "a blast of your jizz deep into her body. She shakes, breaking her kiss with Mara as she begins climaxing as well, her legs tensing up and clenching into your "
                                      + "sides.");
                break;
        }
        Global.flag(Flag.threesome);
    }



    @Override
    protected Optional<String> getMandatoryReason() {
        return Optional.empty();
    }

    @Override
    protected Optional<String> getMorningReason() {
        return Optional.empty();
    }

    @Override boolean available() {
        return !Global.checkFlag(Flag.threesome);
    }

}
