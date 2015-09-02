package nightgames.daytime;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.characters.Trait;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.ModdedCockPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.custom.requirement.BodyPartRequirement;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;

public class CassieTime extends Activity {
	private NPC cassie;

	public CassieTime(Character player) {
		super("Cassie", player);
		cassie = Global.getNPC("Cassie");
		buildTransformationPool();
	}

	@Override
	public boolean known() {
		return Global.checkFlag(Flag.CassieKnown);
	}

	List<TransformationOption> options;

	public void buildTransformationPool() {
		options = new ArrayList<>();
		TransformationOption blessedCock = new TransformationOption();
		blessedCock.ingredients.put(Item.PriapusDraft, 10);
		blessedCock.ingredients.put(Item.BewitchingDraught, 20);
		blessedCock.ingredients.put(Item.FaeScroll, 1);
		blessedCock.requirements.add(new BodyPartRequirement("cock"));
		blessedCock.requirements.add((c, self, other) -> {
			return self.body.get("cock").stream().anyMatch(cock -> ((CockPart)cock).isGeneric());
		});
		blessedCock.additionalRequirements = "A normal cock";
		blessedCock.option = "Blessed Cock";
		blessedCock.scene = "[Placeholder]<br>Cassie blesses your cock with the power of the fairies.";
		blessedCock.effect = (c, self, other) -> {
			Optional<BodyPart> optPart = self.body.get("cock").stream().filter(cock -> ((CockPart)cock).isGeneric()).findAny();
			BasicCockPart target = (BasicCockPart) optPart.get();
			self.body.remove(target);
			self.body.add(new ModdedCockPart(target, CockMod.blessed));
			return true;
		};
		options.add(blessedCock);
		TransformationOption arcanePussy = new TransformationOption();
		arcanePussy.ingredients.put(Item.BewitchingDraught, 20);
		arcanePussy.ingredients.put(Item.FemDraft, 10);
		arcanePussy.ingredients.put(Item.FaeScroll, 1);
		arcanePussy.requirements.add(new BodyPartRequirement("pussy"));
		arcanePussy.requirements.add((c, self, other) -> {
			return self.body.get("pussy").stream().anyMatch(pussy -> pussy == PussyPart.normal);
		});
		arcanePussy.additionalRequirements = "A normal pussy";
		arcanePussy.option = "Arcane Pussy";
		arcanePussy.scene = "[Placeholder]<br>Cassie draws intricate arcane tattoos on your pussy";
		arcanePussy.effect = (c, self, other) -> {
			self.body.addReplace(PussyPart.arcane, 1);
			return true;
		};
		options.add(arcanePussy);
	}

	@Override
	public void visit(String choice) {
		Global.gui().clearText();
		Global.gui().clearCommand();
		Optional<TransformationOption> optionalOption = options.stream().filter(opt -> choice.equals(opt.option)).findFirst();
		if (optionalOption.isPresent()) {
			TransformationOption option = optionalOption.get();
			boolean hasAll = option.ingredients.entrySet().stream().allMatch(entry -> player.has(entry.getKey(), entry.getValue()));
			if (hasAll) {
				Global.gui().message(Global.format(option.scene, cassie, player));
				option.ingredients.entrySet().stream().forEach(entry -> player.consume(entry.getKey(), entry.getValue(), false));
				option.effect.execute(null, player, cassie);
				Global.gui().choose(this, "Leave");
			} else {
				Global.gui().message("Cassie frowns when she sees that you don't have the requested items.");
				Global.gui().choose(this, "Back");
			}
		} else if (choice.equals("Enchantments")) {
			Global.gui().message("[Placeholder]<br>Cassie tells you she could perhaps enchant some of your body.");
			options.forEach(opt -> {
				Global.gui().message(opt.option + ":");
				opt.ingredients.entrySet().forEach((entry) -> {
					Global.gui().message(entry.getValue() + " " + entry.getKey().getName());					
				});
				if (!opt.additionalRequirements.isEmpty()) {
					Global.gui().message(opt.additionalRequirements);
				}
				Global.gui().message("<br>");
			});
			options.forEach(opt -> {
				if (opt.requirements.stream().allMatch(req -> req.meets(null, player, cassie))) {
					Global.gui().choose(this, opt.option);
				}
			});
			Global.gui().choose(this, "Back");
		} else if (choice.equals("Start") || choice.equals("Back")) {
			if(cassie.getAffection(player)>25&&cassie.has(Trait.witch)){
				Global.gui().message("You and Cassie lay together in her bed while she casts spells above you. Every twitch of her fingers brings a new burst of light and color. She weaves " +
						"the colors into abstract pictures, but sometimes you can make out figures and familiar places in the patterns. There's no clear narrative or purpose emerging, Cassie " +
						"probably just likes practicing her witchcraft. <i>\"Not everything I learned has practical applications,\"</i> she says quietly. <i>\"But it's pretty isn't it?\"</i> It is pretty. " +
						"For a moment you're tempted to say that it's not as pretty as she is, but the line is so cliché that you can't manage it. Cassie rests her head on your chest in silence, " +
						"but you can tell it's a silence caused by her hesitating to speak rather than having nothing to say.<p><i>\"I had a crush on you almost as long as we've known each other,\"</i> " +
						"she says without looking at you. <i>\"You're cute, funny, and we got along so well whenever we talked. I tried to think of ways to flirt with you so you'd see me as more than " +
						"a friend, but I don't think I'd have ever worked up the courage to try. When I saw that we had both joined the games, I can't properly describe what I felt. Embarrassed" +
						" of course - maybe more embarrassed than I've been in years - to be seen at a sexfighting competition by someone I knew. I was also really excited about the possibility " +
						"of being intimate with the boy I liked. Most of all, I was scared that you might look down on me when you found out what a horny girl I am.\"</i> She grasps your hand and you " +
						"squeeze it reassuringly. You pull her towards you and kiss her softly. <i>\"I guess it turned out better than I could have hoped.\"</i><p>She sits up and looks at you, blushing " +
						"deeply. <i>\"We can't keep lying here with my embarrassing story in the air, let's do some training.\"</i>");
					Global.gui().choose(this,"Games");
					Global.gui().choose(this,"Sparring");
					Global.gui().choose(this,"Sex");
					Global.gui().choose(this,"Enchantments");
			}
			else if(cassie.getAffection(player)>0){
				Global.gui().message("You text Cassie and suggest meeting up to spend some time together. You get to the meeting place first and settle down on a bench to wait for her. " +
					"You don't end up waiting long, but you manage to get distracted by something on your phone and don't notice Cassie approaching until she's right next to you. Before " +
					"you can stand up, she leans over you and kisses you on the mouth. She cuddles up next to you on the bench and happily rests her head on your shoulder. You're a little " +
					"embarrassed about her public display of affection, but it hasn't drawn too many stares from nearby students. Recently Cassie has been acting very affectionate to you " +
					"during the day. From an outsider's perspective you probably look like an overenthusiastic couple of newlyweds, but you have to admit her behavior is really cute. <i>\"I wouldn't " +
					"mind spending all day like this,\"</i> she murmurs contently. <i>\"But it sounded like you had something specific planned.\"</i>");
				Global.gui().choose(this,"Games");
				Global.gui().choose(this,"Sparring");
				Global.gui().choose(this,"Sex");
			}
			else if(cassie.getAttraction(player)<15){
				Global.gui().message("You find Cassie studying in the library, a ways out of earshot of the other students. You give her a friendly greeting and sit down next to her. " +
						"But after a little bit of awkward small talk she excuses herself and practically runs away, red faced. The two of you weren't particularly close friends, but " +
						"you always used to be able to have a friendly conversation with her. It's a little lonely having her avoid you so blatantly.");
				cassie.gainAttraction(player,2);
				player.gainAttraction(cassie,2);
			}
			else{
				Global.gui().message("You're in the library, looking around to see if Cassie is around. Soon you spot her entering a private study room. When you follow her in, she jumps " +
						"like a frightened animal, but forces a friendly smile. As the two of you chat, she doesn't try to flee, but is still acting uncomfortable and avoids making eye " +
						"contact. You eventually decide to broach the subject directly and ask her if you've done something to upset her. She goes quiet for awhile, looking at the floor. " +
						"Finally she takes a deep breath and leans against your chest.<p><i>\"You haven't done anything wrong. I don't want to avoid you, I just don't know how to act around you,\"</i> she says in a fragile whisper. <i>\"At " +
						"night it's like a whole different world. I'm not the same person during a match as I am the rest of the time. When dawn comes, I'm me again and I leave all that " +
						"behind, but you're part of both worlds. When I see you during the day, am I the girl you had sex with last night or the just a normal student?\"</i><p>Whether you're " +
						"chatting after class or competing in wild sex games, Cassie's always the same person at her core. She can pretend to be whoever she wants to be when you're hanging " +
						"out together or practicing for the night games, but it won't change who she is. There's surely enough overlap between her two worlds for you to fit. She's quiet " +
						"for another short while, then stands up on her toes and presses her mouth softly against yours. You've tasted her lips before, but this is something different. this " +
						"is hesitant and innocent, like a lover's first kiss.<p><i>\"If I can pretend to be anyone, can I pretend to be your girlfriend?\"</i> You answer by wrapping your arms " +
						"around her and kissing her tenderly. <i>\"Hanging out with a cute boy interspersed with wild sex games? Sounds like a great date,\"</i> she says, face flushed but " +
						"lit up in a genuine smile. <i>\"What exactly do you have in mind?\"</i>");
				cassie.gainAffection(player,1);
				player.gainAffection(cassie,1);
				Global.gui().choose(this,"Games");
				Global.gui().choose(this,"Sparring");
				Global.gui().choose(this,"Sex");
			}		
			Global.gui().choose(this,"Leave");
		}
		else if(choice == "Sex"){
			if(cassie.getAffection(player)>=12&&(!player.has(Trait.silvertongue)||Global.random(2)==1)){
				Global.gui().message("Cassie eagerly invites you to her room for some intimate time. The room is quite tidy, though you're surprised to see a couple anime " +
						"posters on the wall. Cassie gets a little embarrassed as you look around, but she kisses you softly and leads you to the bed. You quickly strip each other " +
						"naked, sharing quick kisses when you get the chance. When you then start to fondle her breasts, she stops you gently. <i>\"Can we try something a little different " +
						"today?\"</i> she asks shyly. You let her take the lead as she has you lie down on the bed. She gets on her hands and knees next to you and begins to closely examine " +
						"your dick. You start to feel awkward under her intense scrutiny and ask her what she's doing. <i>\"I'm grown quite fond of this little guy and he's given me some " +
						"very nice memories, but during our matches we're so busy trying to win that I never get a chance to really look at him. I thought today I'd spend some time really " +
						"getting to know him.\"</i> It's more than a little embarrassing listening to her talk about your penis like it's a person, but you indulge her curiosity. Soon she's " +
						"graduated from just looking and starts licking your dick slowly and deliberately. She takes her time and you gradually, but inevitably feel your ejaculation building. " +
						"When she tongues you just under the glans and applies a little suction, it pushes you over the edge. You give a low groan and shoot your load into her mouth.<p>" +
						"Cassie swallows your semen and giggles. <i>\"I think I found a sensitive spot. I'll have to remember that.\"</i> She doesn't move away from your groin, watching with a smile " +
						"as your dick starts to soften. This isn't the first time she's seen a penis. Is it really that fascinating? Her cheeks grow slightly redder than they already were. " +
						"<i>\"Girls are interested in sex too. I've spent a lot of nights thinking about boys since puberty. You're the closest I've ever had to an actual boyfriend.\"</i> So she was a virgin " +
						"when she first joined the night games? She fidgets a bit at the question, which you notice makes her hips wiggle in quite an attractive way. <i>\"Not quite a virgin. " +
						"In high school, I had a friend, who was a boy, but we were just friends. One day as it's getting close to graduation, we end up talking about how we both want to lose " +
						"our virginities before we get to college, so we decide to help each other out.\"</i> She's bright red with embarrassment, but continues. <i>\"It was kinda awkward, but a lot of fun, " +
						"and afterwards we went back to being just friends and never talked about it again. I don't regret doing it, but I never felt any strong feelings for him.\"</i> She turns to " +
						"face you and gives you a shy smile. <i>\"That's probably why it feels so much better with you.\"</i><p>She looks back at your crotch and grins eagerly. <i>\"Looks like you're ready " +
						"for another round. I hope you have a few more shots in you. I want to get lots of practice today.\"</i> You're not likely to turn down more oral sex, but you aren't going to give " +
						"her complete control today. You grab her hips, pull her into 69 position, and start licking her soaked pussy. She makes a cute noise of surprise that melts into a soft moan. " +
						"<i>\"O-ok tha-mmm... I don't mind if you do that for a while.\"</i> She takes your cock into her mouth again and continues pleasuring you. Fair enough. If she's going to become better " +
						"acquainted with your manhood, you might as well learn the ins and outs of her most sensitive area.<p>The two of you keep up your oral activities until your tongues are too " +
						"tired to continue and both of you have orgasmed more times than you can count.");
				if(!player.has(Trait.silvertongue)){
					Global.gui().message("<p><b>Through diligent practice, you and Cassie have gotten more skilled at oral sex.</b>");
					player.add(Trait.silvertongue);
					cassie.add(Trait.silvertongue);
				}
			}
			else{
				Global.gui().message("Cassie is quiet as you lead her back to your room. Her nervousness is understandable given what you're planning to do together. It would " +
						"be more understandable if it wasn't something you do every night. You draw her close next to your bed and kiss her passionately. She lets out a soft noise "+
						"and returns the kiss enthusiastically. You break the kiss to remove her shirt and lower her onto the bed. You kiss and lick a trail down her neck to her " +
						"collarbone and linger there while you unhook her bra. As you pull the garment away, she hurriedly covers her breasts in embarrassment. You had planned to " +
						"devote attention to her breasts, but it seems you'll need to skip them for now. You move lower and trail kisses down her belly to the top of her skirt. " +
						"Cassie lets out a whimper when you slide off her skirt and covers her crotch to prevent you from removing her panties.<p><i>\"Turn off the lights... please?\"</i> "+
						"You comply, though there's enough daylight that it's a purely symbolic gesture. You lean over her unresisting form and slide her panties off, exposing her "+
						"feminine garden while she hides her face in shame. She's as wet as you've ever seen her, so why is she being so passive today? You know better than anyone "+
						"that Cassie is quite capable of taking what she wants when she's turned on. <i>\"This is different,\"</i> she protests meekly. <i>\"Usually we have sex because it's "+
						"part of the game, but right now we're doing it because we both want to. This is way more embarrassing.\"</i><p>If this is that special to her, you want to make sure "+
						"you both enjoy it as much as possible, and that means coaxing Cassie into being a more active participant. You strip off your own clothes, drawing her curious "+
						"gaze, and then slip your hand between her thighs. Her hips jerk upwards as you caress her sensitive petals and she lets out a sweet moan. She hesitantly reaches "+
						"out and grasps your erection in return, but the pleasure you're giving her is affecting her concentration and she can't manage much more than some clumsy stroking. "+
						"Even if her handjob is not terribly skilled, the idea that she's trying to please you is arousing enough to keep you hard. You kiss her again and like before, her "+
						"tongue comes out to meet yours.<p>Soon Cassie moans against your mouth and her body arches against you as she cums hard. You continue kissing her until she relaxes. "+
						"When you try to pull away, she clings to you with renewed vigor and rolls on top of you. She looks at you with eyes wet with desire. <i>\"I want you to cum inside me,\"</i> "+
						"she whispers while breathing heavily. She guides your dick into her flooded entrance and screams in pleasure as she takes your entire length at once. You sit up and "+
						"stifle her voice with another kiss. She clings to you desperately and rides you to another climax. When she cums again, her hot pussy clenches down, milking  your rod. "+
						"You shoot your load into her hot depths, feeding the intensity of her orgasm. The two of you collapse on the bed, still joined below the waist and completely spent.");
			}
			Global.gui().choose(this,"Leave");
			Daytime.train(player,cassie,Attribute.Seduction);
			cassie.gainAffection(player,1);
			player.gainAffection(cassie,1);
		}
		else if(choice == "Games"){
			if(cassie.getAffection(player)>=16&&(!player.has(Trait.misdirection)||Global.random(2)==1)){
				Global.gui().message("Cassie continues to impress you with her gaming prowess, but right now, you've got a decisive advantage. You're at match point, so if either of you can " +
						"score again, that'll almost certainly be the game. Unfortunately for her, you've got her R&D completely locked down. If she doesn't have the agenda she needs in her " +
						"hand, you're going to see it before she does. On her turn she installs one card face down, then plays Mushin No Shin to install another on a naked server with three advancement " +
						"tokens.<p>It's your turn and suddenly your situation is a lot less comfortable. If you weren't at match point, your probably wouldn't risk going after the three advance card, " +
						"but you need to strongly consider it. If you don't run on it and it's an agenda, she'll score it next turn and win. If you do run it and it's a trap, it'll probably kill " +
						"you. Still, if you spend the first half of your turn drawing, you should just barely survive anything that could be there. You draw two cards and run on the naked server, " +
						"revealing... a Psychic Field.<p>Fuck. You discard your entire hand, but survive the trap. You use your last click to draw a card so she can't flatline you next turn. " +
						"That could have gone a lot better. Your hand is almost empty and you weren't able to maintain your R&D lock this turn. She'll want to use this opening to try to score " +
						"the last two points she needs, so you won't be able to spend next turn recovering. You'll need to run whatever she plays this turn. Wait a minute, what about the first " +
						"card she installed last turn. You forgot about it because you were dealing with the three advance card, but the agenda she needs to win may already be on the table.<p>" +
						"Sure enough, next turn Cassie plays a Trick of Light to move two of the advancement tokens to the unknown card from the Psychic Field (You probably should have paid to trash " +
						"it last turn, but hindsight is 20/20). She scores the agenda this turn, gaining the last two points she needs to win the game. While you're cleaning up the cards, she tries " +
						"valiantly, but fails to hide her smile from completely outplaying you. She deserves a bit of gloating. You congratulate her on a well-played game and she beams at you.<p><i>\"" +
						"Misdirection is the key to most magic tricks and it works equally well in most games. It's also how I got your pants off with out you noticing.\"</i> You glance down at your pants, " +
						"which are clearly still on. Cassie suddenly closes the distance between you and kisses you passionately. She presses her body against yours and you embrace her gently. <i>\"Ok, so " +
						"I admit your pants are still on,\"</i> she whispers as you break for air. <i>\"Neither of us wants that. Maybe we should work together to remove the pants?\"</i> Perhaps some of her clothes need " +
						"to be removed too? <i>\"I agree, we're both wearing far too much clothing for the bedroom.\"</i> She's already got your belt off as you kiss her again and lead her to the bed.");
				if(!player.has(Trait.misdirection)){
					Global.gui().message("<p><b>You've learned the art of using a diversion to distract your opponent.</b>");
					player.add(Trait.misdirection);
					cassie.add(Trait.misdirection);
				}
			}
			else{
				Global.gui().message("Cassie is a bit coy about her geek credentials, but you discover she's quite fond of board games when she corrects you during a rules explanation of the "+
				"game you brought. Apparently she's already played this game a few times. It takes a bit of coaxing to get her to admit she plays a lot of these kinds of games, but by the "+
				"time you're ready to start playing, she's actually eager to show you the fruits of her experience.<p><i>\"Everything is terrible and the whole universe hates me!\"</i> You sympathize, "+
				"you really do. You rub Cassie's back to comfort her as she leans on you. <i>\"Look at my cute little astronauts. All they wanted to do was get home with a reasonable amount of "+
				"cargo.\"</i> Yeah, and now they're all slaves. <i>\"Slavers are jerks.\"</i> That's probably a bit of an understatement. Still, that wasn't her fault at all. When you set off, she had a "+
				"better ship than you and more than enough lasers to deal with some slavers. However, she's had a pretty nasty run of bad luck involving a space epidemic, some asteroids, and a detour "+
				"through a combat zone. Despite all that, she would have still probably won if she had any crew to fly what was left of her ship.<p>Cassie flops limply against you and buries her "+
				"face in your chest, completely drained by defeat. You stroke her hair and tickle her lightly around her neck. She coos contently and nuzzles your chest. After a few minutes of "+
				"enjoying her cute responses to your touch, you move your hand down to fondle her breast over her shirt. <i>\"Are you trying to make me forget about the game?\"</i> You give a shrug in " +
				"such a way as to convey that perhaps consoling her is part of your motivation, and perhaps part of it is that her breasts are quite tempting to fondle. She rolls onto her back "+
				"in such a way as to convey that if you're going to fondle her, you may as well do it properly. Her new position " +
				"allows you to slide your hand down the front of her pants and tease her pussy. Her cheeks flush red and she sighs in pleasure as you gradually feel her love juices start to flow. "+
				"When she's thoroughly wet, you push two fingers into her entrance to rub her sensitive walls. Your thumb locates and rubs her clit and in no time, you can feel her tense in orgasm. "+
				"As you take your hand out of her pants, she cuddles up against you sleepily. <i>\"Thanks,\"</i> she whispers. <i>\"Oh, I don't want to be selfish. If you're interested, I'd be happy to return "+
				"the favor.\"</i> You are a bit horny, but more than that, you're comfortable. For now you decide to just relax and enjoy Cassie's warmth.");					
			}
			Global.gui().choose(this,"Leave");
			Daytime.train(player,cassie,Attribute.Cunning);
			cassie.gainAffection(player,1);
			player.gainAffection(cassie,1);
		}
		else if(choice == "Sparring"){
			if(cassie.getAffection(player)>=8&&(!player.has(Trait.judonovice)||Global.random(2)==1)){
				Global.gui().message("You and Cassie manage to procure an actual fitness room with actual wrestling mats for your sparring practice. No more rolling around in couch cushions and pillows. " +
						"the downside it that you don't have the same level of privacy as in your dorm room, so today you'll need to stick with just sparring. Cassie seems a lot more confident than usual " +
						"and the two of you complete your warm ups in good spirits. You start the match with some simple lunges and takedowns. You notice that she's gotten much better at maintaining her " +
						"balance and avoiding getting caught in your holds. You decide it's ok to come at her more seriously. You grab her by the shoulders and try to use your superior upper body strength " +
						"to force her to the mat. There's a sudden whirl of movement, an impact on your back, and you find yourself looking at the ceiling.<p>Cassie bends over you, looking concerned. \"Are " +
						"you ok? Can you still move?\" You seem to be fine, you just don't know what happened there. She gives you relieved smile. <i>\"That was a harai goshi, a hip throw, or " +
						"it should have been at least. It was still a little rough.\"</i> That seemed plenty effective. You'd hate to have to face the refined version. Cassie giggle a bit. \"I've been learning " +
						"some basic judo on my own. Judo has a lot of techniques that can defeat an opponent without seriously hurting them, so I thought it would be perfect for me.\" That's some serious " +
						"dedication she's putting into this competition. <i>\"When we first started these night games, I thought it didn't really matter if I wasn't very good as long as I could earn enough to " +
						"help with tuition. After a few matches, I started really wanting to see if I could win. Each time I lost a fight, I wanted to figure out if I could have done better. I guess this " +
						"is my answer to that.\"</i><p>She extends her hand to help you up, but instead you pull her down on top of you and kiss her tenderly. She blushes and grins at you when you break the kiss. " +
						"<i>\"That's not a judo technique.\"</i> She stands back up and motions for you to do the same. <i>\"Come on, I'll show you what I've learned.\"</i>");
				if(!player.has(Trait.judonovice)){
					Global.gui().message("<p><b>By training with Cassie, you learned the Hip Throw skill.</b>");
					player.add(Trait.judonovice);
					cassie.add(Trait.judonovice);
				}
			}
			else{
				Global.gui().message("You and Cassie do your best to prepare your dorm room for an informal sparring match. You've moved any potentially dangerous or fragile furniture and placed " +
						"down a layer of cushions, blankets, and pillows on the floor. The result is pretty unprofessional looking and hard to keep your footing on, but should allow you to wrestle " +
						"without any risk of injury. Of course, during the night games you've fought in some fairly cluttered areas with very hard surfaces and no one has been hurt yet. On reflection " +
						"it seems like a small miracle that there haven't been any accidental injuries during a match. During the day, however, you don't see any reason to tempt fate, hence the safety " +
						"precautions.<p>The two of you agree that this match will focus completely on conventional sparring techniques and not include any sexual holds. You warm up by alternating " +
						"practicing simple takedowns and pins on each other. Cassie's technique is a bit rough but she is able to successfully execute the moves you show her. When you actually start "+
						"competing however, the match is woefully one sided. You're able to completely control her while she can only flail vainly to try to escape. You know she's better than this " +
						"so you ask her why she's holding back. <i>\"If this is just for practice, I don't want to actually hurt you. You've been pretty gentle with me too.\"</i> You have been careful " +
						"to avoid unnecessary pain, but you've only been able to do so because you were winning so easily. Compassion is the luxury of the strong.<p>After resetting to neutral position " +
						"again, you're quickly able to take her down and pin her on her back. In response, she tilts her head up and kisses you. You point out that kissing is not actually considered " +
						"a wrestling technique. <i>\"It should be. It's very satisfying.\"</i> She kisses you again. At this rate you're just going to end up having sex. <i>\"Sounds good. I like sex.\"</i> She's obviously " +
						"not taking sparring seriously, so you decide to punish her a bit.<p>You slip your hands under her shirt and start tickling her bare skin. She shrieks in surprise and tries desperately " +
						"to squirm away from your fingers. <i>\"Nooo! This is terrible!\"</i> She gasps out between fits of laughter. You move your right hand down between her legs and slip your fingers up the leg of " +
						"her shorts to reach her sensitive inner thigh. <i>\"This was supposed to be a friendly match. It's not fair using your secret ultimate technique!\"</i> She tries to protest " +
						"more, but it devolves into incoherent giggles. She tries to roll away to escape, but you hold her tightly from behind. Your fingers tickle their way up her thigh and reach the " +
						"edge of her panties. Pushing them aside, you find her pussy completely soaked and start fingering her without hesitation. As her laughter starts to turn into pleasured whimpers, " +
						"you move your other hand up to tease and pinch her nipples. <i>\"I-I thought sexual holds weren't allowed.\"</i> The match ended when you pinned her. This is just her punishment for " +
						"giving up so easily. She opens her mouth to reply, but can't form any words. Her pussy clamps down on your fingers as she orgasms. You continue to hold her as her climax dies " +
						"down and she goes limp from exhaustion. Looks like you're done sparring for now.");					
			}
			Global.gui().choose(this,"Leave");
			Daytime.train(player,cassie,Attribute.Power);
			cassie.gainAffection(player,1);
			player.gainAffection(cassie,1);
		}
		else if(choice == "Leave"){
			Global.modCounter(Flag.CassieLoneliness, -2);
			done(true);
		}
	}
	@Override
	public void shop(Character npc, int budget) {
		npc.gainAffection(cassie,1);
		cassie.gainAffection(npc,1);
	}
}
