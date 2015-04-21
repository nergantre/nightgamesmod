package characters;

import global.Flag;
import global.Global;
import global.Modifier;

import items.Clothing;
import items.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import characters.body.Body;
import characters.body.BreastsPart;
import characters.body.GenericBodyPart;
import characters.body.PussyPart;
import characters.body.TailPart;
import characters.body.WingsPart;

import skills.Skill;
import skills.Tactics;
import stance.Stance;

import combat.Combat;
import combat.Result;
import daytime.Daytime;

import actions.Action;
import actions.Move;
import actions.Movement;
import actions.Resupply;
import areas.Area;

public class Angel extends BasePersonality {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8169646189131720872L;
	public Angel(){
		super();
		character = new NPC("Angel",1,this);
		character.outfit[0].add(Clothing.Tshirt);
		character.outfit[1].add(Clothing.thong);
		character.outfit[1].add(Clothing.miniskirt);
		character.closet.add(Clothing.Tshirt);
		character.closet.add(Clothing.thong);
		character.closet.add(Clothing.miniskirt);
		character.change(Modifier.normal);
		character.mod(Attribute.Seduction, 2);
		character.mod(Attribute.Perception, 1);
		Global.gainSkills(character);
		character.add(Trait.undisciplined);
		character.add(Trait.experienced);
		character.add(Trait.powerfulhips);
		character.add(Trait.alwaysready);
		character.add(Trait.lickable);
		character.setUnderwear(Item.AngelTrophy);
		character.plan = Tactics.hunting;
		character.mood = Emotion.confident;
		character.body.add(BreastsPart.dd);
		character.body.add(PussyPart.normal);
		character.body.finishBody("female");
	}

	@Override
	public void setGrowth() {
		growth.stamina = 1;
		growth.arousal = 5;
		growth.mojo = 2;
		growth.bonusStamina = 1;
		growth.bonusArousal = 4;
		growth.bonusMojo = 1;
		preferredAttributes.add(Attribute.Dark);
		preferredAttributes.add(Attribute.Seduction);
	}

	@Override
	public void rest() {
		if(character.rank>=1){
			if(!character.has(Trait.succubus)&&character.money>=1000){
				advance();
			}
		}
		if(!(character.has(Item.Dildo)||character.has(Item.Dildo2))&&character.money>=250){
			character.gain(Item.Dildo);
			character.money-=250;
		}
		if(!(character.has(Item.Onahole)||character.has(Item.Onahole2))&&character.money>=300){
			character.gain(Item.Onahole);
			character.money-=300;
		}
		if(!character.has(Item.Onahole2)&&character.has(Item.Onahole)&&character.money>=300){
			character.remove(Item.Onahole);
			character.gain(Item.Onahole2);
			character.money-=300;
		}
		if(!(character.has(Item.Strapon)||character.has(Item.Strapon2))&&character.money>=500){
			character.gain(Item.Strapon);
			character.money-=500;
		}
		if(!character.has(Item.Strapon2)&&character.has(Item.Strapon)&&character.money>=500){
			character.remove(Item.Strapon);
			character.gain(Item.Strapon2);
			character.money-=500;
		}

		if(character.money>0){
			Global.getDay().visit("Body Shop", character, Global.random(character.money));
		}
		if(character.rank >= 1) {
			if(character.money>0){
				Global.getDay().visit("Black Market", character, Global.random(character.money));
			}
			if(character.money>0){
				Global.getDay().visit("Workshop", character, Global.random(character.money));
			}
		}
		if(character.money>0){
			Global.getDay().visit("XXX Store", character, Global.random(character.money));
		}
		if(character.money>0){
			Global.getDay().visit("Black Market", character, Global.random(character.money));	
		}
		if(character.money>0){
			Global.getDay().visit("Bookstore", character, Global.random(character.money));
		}
		if(character.money>0){
			Global.getDay().visit("Hardware Store", character, Global.random(character.money));
		}
		Decider.visit(character);
		int r;
		for(int i=0;i<8;i++){
			r=Global.random(4);
			if(r==1){
				if(character.has(Trait.fitnessNut)){
					character.getStamina().gain(1);
				}
				character.getStamina().gain(1);
			}
			else if(r==3){
				if(character.has(Trait.expertGoogler)){
					character.getArousal().gain(4);
				}
				character.getArousal().gain(6);
			}
			else if(r==2){
				if(character.has(Trait.mojoMaster)){
					character.getMojo().gain(1);
				}
				character.getMojo().gain(1);
			}
		}
	}

	@Override
	public String bbLiner() {
		return "Angel seems to enjoy your anguish in a way that makes you more than a little nervous. <i>\"That's a great look for you, I'd like to see it more often.\"</i>";
	}

	@Override
	public String nakedLiner() {
		return "Angel gives you a haughty look, practically showing off her body. <i>\"I can't blame you for wanting to see me naked, everyone does.\"</i>";
	}

	@Override
	public String stunLiner() {
		return "Angel groans on the floor. <i>\"You really are a beast. It takes a gentle touch to please a lady.\"</i>";
	}

	@Override
	public String winningLiner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String taunt() {
		return "Angel pushes the head of your dick with her finger and watches it spring back into place. <i>\"You obviously can't help yourself. If only you were a little bigger, we could have a lot of fun.\"</i>";
	}

	@Override
	public String temptLiner(Character t) {
		return "Angel looks at you with a grin, <i>\"You're almost drooling. Is staring at my body that much fun? If you want me that much, why don't you just sit there and let me make you feel good.\"</i>";
	}

	@Override
	public String victory(Combat c,Result flag) {
		character.arousal.empty();
		if(flag==Result.anal){
			return "Angel leans over you as she grinds her hips against yours. <i>\"You're going to come for me, aren't you?\"</i> she purrs into your ear. You shake your head; " +
					"no way could you live it down if you came while you had something in your ass. Angel frowns and gives your ass a firm slap. <i>\"No reach around for you " +
					"then,\"</i> she snaps. <i>\"We'll just do this the old fashion way.\"</i> She renews her assault on your poor ass and you feel your will slipping. Another solid slap " +
					"to ass sends you into a shuddering orgasm. Angel's triumphant laughter rings in your head as the shame makes you flush bright read.<p>Pulling her "
					+ (character.hasDick() ? character.body.getRandomCock().describe(character) : "strapon") +
					" from your ass with a wet slurp Angel flips you over" + (!character.hasDick() ? " and removes the strapon." :". ")
					+ "She then squats down and lines your cock up with her now soaked pussy, <i>\"Do " +
					"a good enough good job and I might not tell my friends how you came like a whore while I fucked your ass.\"</i> She gloats with a smug grin on her face. " +
					"Appalled at the idea that she might share that information with anyone, you strengthen your resolve to fuck the woman above you.<p>Several minutes later, " +
					"you are breathing hard. Angel sits not far from you, face flush with pleasure. You smile internally as you sit, trying to catch your breath. No way " +
					"she could have been disappointed with that performance.  You can only gape as you look up to see Angel is gone along with your clothes. You sigh as you " +
					"stand and ready yourself to move on. You wouldn't put past Angel to tell her girlfriends regardless of how well you performed, you just hope that's as " +
					"far as that information goes.";
		}
/*		if(character.has(Trait.succubus)){
			return "";
		}*/
		else if(flag==Result.intercourse){
			return "Angel rides your cock passionately, pushing you inevitably closer to ejaculation. Her hot pussy is wrapped around your shaft like... well, exactly " +
					"what it is. More importantly, she's a master with her hip movements and you've held out against her as long as you can. You can only hope her own orgasm is equally " +
					"imminent. <i>\"Not even close,\"</i> She practically growls. <i>\"Don't give up now.\"</i> That's an impossible command. How can she expect you not to cum when " +
					"her slick love canal is milking your dick so expertly. As the last of your restraint crumbles, you let out a groan and shoot a thick load of semen " +
					"into her depths. <p>You lie on the floor panting as Angel looks down at you, somehow annoyed despite her victory. <i>\"Is that the best you can do? " +
					"You know it's rude to finish before your lover.\"</i> She starts to lick and suck on her finger, sensually. <i>\"Don't think you can get off on your own and the " +
					"sex is done just like that. I never let a man go until I'm satisfied.\"</i> You're quite willing to try to satisfy her in a variety of ways, but more " +
					"fucking is a physical impossibility at this point. Your spent penis has completely wilted by now, and it'll be a little while before there's any possibility " +
					"of it recovering. Angel gives you a pitiless smile and reaches behind her. <i>\"Don't worry. I know a good trick.\"</i><p>Whoa! You jerk in surprise as you feel " +
					"her spit-coated finger probing at your anus. <i>\"Don't complain,\"</i> She says, sliding the digit into your ass. <i>\"It's your own fault for being such a quick " +
					"shot.\"</i> As she moves her finger around, it creates an indescrible sensation. You dick starts to react immediately and returns to full mast faster than you " +
					"ever would have imagined. Angel wastes no time impaling herself on the you newly recovered member and rides you with renewed vigor. Fortunately she removes " +
					"the invading finger from your anus so you can focus on the pleasure of being back in her wonderful pussy. <p>She grinds against you, clearly turned on and " +
					"enjoying being filled again. She moans passionately and her vaginal walls rub and squeeze your cock. You move your hips to match Angel's movements and " +
					"her voice jumps in pitch. She's obviously enjoying your efforts much more this time, but she's so good too. You've just recently cum, but she's riding " +
					"through your endurance at an alarming rate. If you end up cumming again before she finishes, you're going to get the finger treatment again or worse. " +
					"Fortunately, you don't have to worry about that. Angel throws back her head and practically screams out her orgasm. Her love canal squeezes tightly, milking " +
					"out your second ejaculation. <p>Angel quickly recovers, standing up as a double load of cum leaks out between her thighs. <i>\"That'll do... for now.\"</i>";
		}
		return "It's too much. You can't focus on the fight with the wonderful sensations Angel is giving you. She smiles triumphantly and mercilessly teases your " +
				"twitching dick. Your orgasm is imminent, but you concentrate on holding it back as long as you can, determined not to give up until the end. Angel's " +
				"expression gradually changes to one of impatience. <i>\"Just cum already!\"</i> She slaps your dick and the shock breaks your concentration. Your pent-up " +
				"ejaculation bursts forth and covers her hands. <p>Without giving you a chance to recover, Angel pushes you on your back and positions her soaking " +
				"pussy over your face. <i>\"Show me you're good for more than cumming on command.\"</i> She grinds against your mouth as you eat her out. She reaches behind her " +
				"and roughly grabs your balls, encouraging you to focus more on pleasing her. Soon her writhing grows more passionate and her moans express her building " +
				"pleasure. She rewards your efforts by moving her hand to your dick, which is already starting to harden again. She jerks you off, using your previous climax " +
				"for lubricant. The growing volume of Angel's cries reveal that she's close to the end, so you focus on licking and sucking her clit, quickly bringing her to " +
				"a loud climax. Your own peak isn't far behind and soon you shoot another jet of cum into the air.\n\nAngel licks her semen covered hands clean and walks away " +
				"with your clothes, having seemingly forgotten about you.";
	}

	@Override
	public String defeat(Combat c,Result flag) {
		if(character.has(Trait.succubus)&&character.get(Attribute.Dark)>=6){
			return "Angel shivers as she approaches her climax and her legs fall open defenselessly. You can't resist taking advantage of this opening to deliver the " +
					"coup de grace. You grab hold of her thighs and run your tongue across her wet pussy. Her love juice is surprisingly sweet and almost intoxicating, " +
					"but you stay focused on your goal. You ravage her vulnerable love button with your tongue and a flood of tasty wetness hits you as she cums. You " +
					"prolong her climax by continuing to lick her while lapping up as much of her love juice as you can. The taste seems almost familiar, but you can't " +
					"quite place it. Sweet and tangy like a desert wine? Not a perfect comparison, but not far off.<p>Angel should be coming down from her peak, but " +
					"she's still moaning quite passionately. Oh well, it can't hurt to drink up the last of her love juice. You're the one who made her juice herself, so " +
					"it seems only fair. It is very tasty. Intoxicating was the word that came to mind early, but addictive seems to fit too. Angel's flower is mostly " +
					"clean, but you stick your tongue deep inside to be sure. There seems to be some fresh love juice in this bit... and this one.... Here too.<p>Angel's " +
					"pussy tenses up and you're treated to another flood of her wonderful flavor. You can't let this much juice go to waste. You diligantly continue to " +
					"lick Angel's trembling girl parts as she squeals in passion. You feel her hands grip your hair desperately and you have to hold her hips to keep her " +
					"from squirming away. She's producing a decent amount of delicious nectar, but it occurs to you that she'll probably give you more if you focus on her " +
					"clit. You target her pearl and lick it rapidly until she screams in pleasure and rewards you with another surge of juice. This seems like the best " +
					"way to get more of her wonderful juice. You could just stay here drinking this stuff all night, and you just may.<p>You suddenly feel Angel's tail wrap " +
					"tightly around your balls. Your head jerks up in surprise and her thighs clamp together on it, holding you out of reach of her delicious honey pot. " +
					"<i>\"Down boy!\"</i> Angel scolds you as she covers her groin protectively. <i>\"I appreciate the dedication, but after a couple orgasms, I need a chance to " +
					"catch my breath.\"</i> You feel your head clear a bit and realize you completely fell victim to her addictive love juice.<p>Angel uses her grip on your " +
					"head to force you onto your back. <i>\"I do love being eaten out, but right now I'm ready to be filled.\"</i> She releases the head scissor and positions herself " +
					"over your dick before dropping her hips to engulf you to the hilt. A jolt goes through you and you realize exactly how horny you are. I addition to " +
					"not having any relief, Angel's fluids have started to affect you. You're incredibly hard and sensitive, but even though Angel is riding you intensely, " +
					"your ejaculation feels painfully out of reach. You don't feel your climax start to build until Angel is moaning and approaching yet another orgasm. Is " +
					"that an innate succubus ability? Is she controlling the timing of your orgasm? You don't have time to dwell on the question, your hips thrust involuntarily " +
					"as you shoot your load into her waiting quim. Angel gives you a deep, passionate kiss as she gets off of you. <i>\"Thanks lover. You sure know how to show a " +
					"girl a good time.\"</i>";
		}
		else if(flag==Result.intercourse){
			return "You thrust your cock continously into Angel's dripping pussy. Her hot insides feel amazing, but you're sure you have enough of an advantage to risk " +
					"it. She lets out breathy moans in time to your thrusts and her arms are trembling too much to hold herself up. She's clearly about to cum, you just " +
					"need to push her over the edge. You maul her soft, heavy boobs and suck on her neck. Angel closes her eyes tightly and whimpers in pleasure. <p>You keep " +
					"going, sure that your victory is near, but after awhile there's no change in her reactions. How has she not cum yet? She's obviously loving your efforts, " +
					"but you can't seem to finish her off. Worse yet, if you keep going at this pace, your own control is going to give out. You'll have to pull out so you can " +
					"switch to your fingers and tongue. It'd be way more satisfying to win by fucking her, but right now you just have to focus on winning at all. When you try " +
					"to pull out, Angel's legs wrap around you and keep you from escaping. Her heels jab you in the butt, forcing you to thrust back inside and you feel her pussy " +
					"squeeze your cock tightly. <p>Oh God, she's actually going to make you cum while you're on top of her. You were overconfident in your dominant position, you " +
					"underestimated Angel's remarkable staying power, and now you've lost. Despite your desperate attempts to hang on, you're overwhelmed by pleasure and cum " +
					"inside her tight womanhood. You slump down on top of her as you both catch your breath. Pretty soon Angel is fully recovered and back on her feet, but you " +
					"continue to lie on the floor, too despirited to move. Angel gives you a sharp prod with her foot. <i>\"How long are you going to lay there? You only came once. " +
					"I had a continuous orgasm for at least two minutes and that's way more exhausting. It's been a long time since anyone's made me do that.\"</i> Wait, what? You'd " +
					"never have guessed that she came if she hadn't said anything. <i>\"Just because you managed to beat me this time doesn't mean you can suddenly start acting " +
					"lazy. If you let your guard down, I'm going to turn you into my own personal toy.\"</i> At that, she walks away naked.";
		}
		return "Angel trembles and moans as you guide her closer and closer to orgasm. You pump two fingers in and out of her pussy and lick her sensitive nether lips. " +
				"Her swollen clit peeks out from under its hood and you pinch it gently between your teeth. Angel instantly screams in pleasure and arches her back. A " +
				"flood of feminine juice sprays you as she loses control of her body.<p>It takes her a little while to catch her breath. She quickly pushes you on your " +
				"back and begins blowing you, never once meeting your eyes. What you can see of her face and ears is completely red. If you didn't know better, you'd " +
				"say that she's embarrassed about the one-sided orgasm you gave her earlier. You don't have much attention to devote to it though, Angel is a very good " +
				"cock-sucker. Her tongue finds all your most sensitive areas and soon you're filling her mouth with your seed.<p>Angel swallows your load and happily " +
				"licks the stray drops from her lips. <i>\"Did you enjoy that?\"</i> She asks, looking a lot more composed. <i>\"You weren't bad either.\"</i>";
	}

	@Override
	public String describe() {
		if(character.has(Trait.succubus)){
			return "Angel seems to have taken the path opposite her namesake. She has wings, but they're black as midnight and short horns peak out of her long hair. " +
					"Her appearance should be frightening, but she's more beautiful and seductive than ever. Her entire being seems to " +
					"radiate sex and you struggle to ignore a treacherous little voice in the back of your mind that tells you to just give yourself to her.";
		} else {
			return "Angel has long, straight blonde hair that almost reaches her waist." +
					"Beautiful, refined features complete the set, making her utterly irresistable. Her personality is prideful and overbearing, as though you belong to " +
					"her, but you don't know it yet.";
		}
	}

	@Override
	public String draw(Combat c,Result flag) {
		if(flag==Result.intercourse){
			return "Angel pins you on your back, riding you with passion. You're close to the edge, but she's too far gone to take advantage of it. She's fucking you " +
					"for her own pleasure rather than trying to win. Just as you feel your climax hit, Angel cries out in ecstasy and her pussy tightens to milk your " +
					"dick dry. <p>Angel stays on top of you as you both recover, and as your wilting penis starts to slip out of her, her vagina squeezes again to hang " +
					"onto it. <i>\"I hope you're not finished yet,\"</i> she whispers sultrily. <i>\"I won't be satisfied with just one time.\"</i> You're already starting to harden " +
					"again inside her. She pushes her perky breasts into your face and lets you lick and suck her nipples. By the time you're completely erect, she's " +
					"acting noticeably pleasure drunk again. She grinds her hips against yours and soon she reaches her second orgrasm. She only slows down for a moment, " +
					"riding you as quickly as when she started. Your next climax builds faster than hers. She grabs your balls, pinching and squeezing to delay your " +
					"ejaculation each time you get close. As she nears her peak, she lets you go. You cum inside her again, setting off her third screaming orgasm. <p>" +
					"By the time Angel's finally satisfied, you're exhausted, but very content.";
		}
		return "You and Angel lie on the floor in 69 position, desperately pleasuring each other. Angel is extremely good at giving blowjobs and each flick of her tongue " +
				"tests your self-control. Fortunately, she's quite receptive to your oral minstrations. Her pussy trembles as you polish her clit with your tongue. For a " +
				"moment, you think you have the upper hand, but then her tongue finds a particularly sensitive bit of flesh under your cockhead, and her hand fondles your " +
				"balls. Your hips jerk involuntarily as you cum in her mouth. Fortunately, a flood of Angel's love juice hits your face, indicating she orgasmed at the same " +
				"time.<p>You wipe the juice from your mouth, but Angel doesn't give you any time to rest. She continues licking and sucking your cock in the aftermath of your " +
				"ejaculation. Your penis is extremely sensitive right now, but she keeps it from softening. She gives your balls a light squeeze, which you interpret as a " +
				"demand to keep eating her out. You shove your tongue into her pussy and feel her tremble as she lets out a stifled moan. Angel redoubles her efforts and blows " +
				"you even more intensely. You retaliate by focusing on your tongue work, exploring her labia and clit to find her weaknesses.<p>You and Angel continue servicing " +
				"each other until you both cum again. She still shows no sign of stopping and continues sucking your painfully overstimulated dick. You were sensitive after the " +
				"first time you ejaculated, but now it almost feels like you're being shocked. This is practically torture. " +
				"You pull away from her slit and beg her to stop.<p>Angel gives you a few more very intentional licks before releasing you, as if to make a point. She sits on " +
				"your torso and looks down at you with a superior smirk. <i>\"Since we came at the same time, I was worried you might get the crazy idea that you're a match " +
				"for me. I figured I should prove to you which of us has the most staying power.\"</i> She strokes your hair with a surprising amount of affection. <i>\"Don't " +
				"worry if you can't keep up. As long as you keep making me cum, I'll let you be my pet.\"</i>";
	}

	@Override
	public boolean fightFlight(Character opponent) {
		return !character.nude()||opponent.nude();
	}

	@Override
	public boolean attack(Character opponent) {
		return true;
	}

	@Override
	public String victory3p(Combat c, Character target, Character assist) {
		if(target.human()){
			return "Angel looks over your helpless body like a predator ready to feast. She kneels between your legs and teasingly licks your erection. She circles her " +
					"tongue around the head, coating your penis thoroughly with saliva. When she's satisfied that it is sufficiently lubricated and twitching with need, " +
					"she squeezes her ample breasts around your shaft. Even before she moves, the soft warmth surrounding you is almost enough to make you cum. When she " +
					"does start moving, it's like heaven. It takes all of your willpower to hold back your climax against the sensation of her wonderful bust rubbing against " +
					"your slick dick. When her tongue attacks your glans, poking out of her cleavage, it pushes you past the limit. You erupt like a fountain into her face, " +
					"while she tries to catch as much of your seed in her mouth as she can.";
		}
		else{
			return "You present "+target.name()+"'s naked, helpless form to Angel's tender minstrations. Angel licks her lips and begins licking and stroking "+target.name()+"'s body. She's " +
					"hitting all the right spots, because soon "+target.name()+" is squirming and moaning in pleasure, and Angel hasn't even touched her pussy yet. " +
					"Angel meets your eyes to focus your attention and slowly moves her fingers down the front of "+target.name()+"'s body. You can't see her hands from " +
					"this position, but you know when she reaches her target, because "+target.name()+" immediately jumps as if she's been shocked. Soon it takes all of "+
					"your energy to control "+target.name()+" who is violently shaking in the throes of orgasm. You ease her to the floor as she goes completely limp, " +
					"while Angel licks the juice from her fingers.";
		}
	}
	@Override
	public String intervene3p(Combat c, Character target, Character assist) {
		if(target.human()){
			return "You manage to overwhelm "+assist.name()+" and bring her to the floor. You're able to grab both her arms and pin her helplessly beneath you. " +
					"Before you can take advantage of your position, pain explodes below your waist. "+assist.name()+" shouldn't have been able to reach your groin " +
					"from her position, but you're in too much pain to think about it. You are still lucid enough to feel large, perky breasts press against your back " +
					"and a soft whisper in your ear. <i>\"Surprise, lover.\"</i> The voice is unmistakably Angel's. She rolls you onto your back and positions herself over your face," +
					" with her legs pinning your arms. Her bare pussy is right in front of you, just out of reach of your tongue. It's weird that she's naked, considering " +
					"she caught you by surprise, but this is Angel after all.<p>";
		}
		else{
			return "You and "+target.name()+" grapple back and forth for several minutes. Soon you're both tired, sweaty, and aroused. You catch her hands for a moment and " +
					"run your tongue along her neck and collarbone. Recognizing her disadvantage, she jumps out of your grasp and directly into Angel. Neither of you " +
					"noticed Angel approach. Before "+target.name()+" can react, Angel pulls her into a passionate kiss. "+target.name()+" forgets to resist and goes limp " +
					"long enough for Angel to pin her arms.<p>";
		}
	}
	@Override
	public String startBattle() {
		return "Angel licks her lips and stalks you like a predator.";
	}
	@Override
	public boolean fit() {
		return !character.nude()&&character.getStamina().percent()>=50;
	}
	@Override
	public String night() {
		return "As you start to head back after the match, Angel grabs your hand and drags you in the other direction. <i>\"You're officially kidnapped, because I haven't had " +
				"enough sex yet tonight.\"</i> That makes sense... kinda? You did just finish three hours of intense sex-fighting. If she wants too much more than that, you're " +
				"both going to end up pretty sleep deprived. Angel looks like she's struggling to put her thoughts into words. <i>\"I had enough sex in general, but I want some " +
				"more time having you all to myself.\"</i> That's quite flattering coming from her, but why you specifically? Angel is openly bisexual, she could just as easily " +
				"take one of the other girls back with her. She looks back at you and blushes noticeably. <i>\"It's better with you, and not just because you have a cock. It is " +
				"a pretty good fit though. I don't know. It doesn't matter. I'm kidnapping you, so we're going to go back to my room, have sex, and you're going to stay the night " +
				"in case I want more sex in the morning.\"</i> You follow without protest. <br>You lose a lot of sleep, but you don't regret it.";
	}
	public void advance(){
		character.add(Trait.succubus);
		character.body.addReplace(PussyPart.succubus, 1);
		character.body.addReplace(TailPart.demonic, 5);
		character.body.addReplace(WingsPart.demonic, 5);
		character.outfit[0].removeAllElements();
		character.outfit[1].removeAllElements();
		character.outfit[0].add(Clothing.bikinitop);
		character.outfit[1].add(Clothing.bikinibottoms);
		character.mod(Attribute.Dark,1);
	}

	public boolean checkMood(Emotion mood, int value) {
		switch(mood){
		case horny:
			return value>=50;
		case nervous:
			return value>=150;
		default:
			return value>=100;
		}
	}
}
