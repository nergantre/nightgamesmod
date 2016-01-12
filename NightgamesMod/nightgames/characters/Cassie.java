package nightgames.characters;

import java.util.HashSet;
import java.util.Optional;

import nightgames.actions.Action;
import nightgames.actions.Movement;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.FacePart;
import nightgames.characters.body.MouthPussyPart;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.status.Energized;

public class Cassie extends BasePersonality {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8601852023164119671L;
	public Cassie(){
		super("Cassie", 1);
		preferredCockMod = CockMod.runic;
		character.outfitPlan.add(Clothing.getByID("bra"));
		character.outfitPlan.add(Clothing.getByID("blouse"));
		character.outfitPlan.add(Clothing.getByID("panties"));
		character.outfitPlan.add(Clothing.getByID("skirt"));
		character.outfitPlan.add(Clothing.getByID("shoes"));

		character.change(Modifier.normal);
		character.mod(Attribute.Power, 1);
		character.mod(Attribute.Seduction, 1);
		character.mod(Attribute.Cunning, 1);
		character.mod(Attribute.Perception, 1);

		Global.gainSkills(character);
		character.add(Trait.softheart);
		character.add(Trait.romantic);
		character.add(Trait.imagination);

		character.setTrophy(Item.CassieTrophy);
		character.plan = Plan.hunting;
		character.mood = Emotion.confident;
		character.body.add(BreastsPart.c);
		character.body.add(PussyPart.normal);
		character.body.finishBody(CharacterSex.female);
	}

	@Override
	public void setGrowth() {
		growth.stamina = 2;
		growth.arousal = 4;
		growth.mojo = 3;
		growth.willpower = .4f;
		growth.bonusStamina = 1;
		growth.bonusArousal = 3;
		growth.bonusMojo = 1;
		preferredAttributes.add(c -> c.get(Attribute.Arcane) < 80 ? Optional.of(Attribute.Arcane) : Optional.empty());
		growth.addTrait(2, Trait.silvertongue);
		growth.addTrait(5, Trait.mojoMaster);
		growth.addTrait(8, Trait.tongueTraining1);
		growth.addTrait(11, Trait.pussyTraining1);
		growth.addTrait(14, Trait.submissive);
		growth.addTrait(17, Trait.cute);
		growth.addTrait(20, Trait.addictivefluids);
		growth.addTrait(23, Trait.experttongue);
		growth.addTrait(26, Trait.calm);
		growth.addTrait(29, Trait.tongueTraining2);
		growth.addTrait(32, Trait.autonomousPussy);
		growth.addTrait(35, Trait.desensitized);
		growth.addTrait(38, Trait.tongueTraining3);
		growth.addTrait(41, Trait.magicEyeEnthrall);
		growth.addTrait(41, Trait.magicEyeTrance);
		growth.addTrait(41, Trait.magicEyeArousal);
		growth.addTrait(44, Trait.soulsucker);
		growth.addTrait(47, Trait.pussyTraining2);
		growth.addTrait(50, Trait.desensitized2);
		//mostly feminine face, cute but not quite at Angel's level
		character.body.add(new FacePart(.1, 2.9));
		growth.actions.put(20, () -> {
			character.body.addReplace(new MouthPussyPart(), 1);
		});
	}

	@Override
	public Action move(HashSet<Action> available, HashSet<Movement> radar) {
		for(Action act: available){
			if(act.consider()==Movement.mana){
				return act;
			}
		}
		Action proposed = Decider.parseMoves(available, radar, character);
		return proposed;
	}
	@Override
	public void rest() {
		if(character.rank>=1){
			if(!character.has(Trait.witch)&&character.money>=1000){
				advance();
			}
		}
		super.rest();
		if(!(character.has(Item.Tickler)||character.has(Item.Tickler2))&&character.money>=300){
			character.gain(Item.Tickler);
			character.money-=300;
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
		while (character.money>Item.Lactaid.getPrice() && !character.has(Trait.lactating) && character.count(Item.Lactaid) < 3) {
			character.money-=Item.Lactaid.getPrice();
			character.gain(Item.Lactaid);
		}
		if (character.rank >= 1) {
			if (!character.has(Trait.lactating) && character.money >= 1000) {
				character.money -= 1000;
				character.add(Trait.lactating);
			}
			if(character.money>0){
				Global.getDay().visit("Magic Training", character, Global.random(character.money));
			}
			if(character.money>0){
				Global.getDay().visit("Body Shop", character, Global.random(character.money));
			}
			if(character.money>0){
				Global.getDay().visit("Workshop", character, Global.random(character.money));
			}
		}

		if(character.money>0){
			Global.getDay().visit("XXX Store", character, Global.random(character.money));
		}
		if(character.money>0){
			Global.getDay().visit("Bookstore", character, Global.random(character.money));
		}
		if(character.money>0){
			Global.getDay().visit("Hardware Store", character, Global.random(character.money));
		}
		if(character.money>0){
			Global.getDay().visit("Black Market", character, Global.random(character.money));
		}
		int r;
		for(int i=0;i<9;i++){
			r=Global.random(4);
			if(r==1){
				if(character.has(Trait.fitnessNut)){
					character.getStamina().gain(Global.random(6) + 4);
				}
				character.getStamina().gain(Global.random(3) + 2);
			}
			else if(r==3){
				if(character.has(Trait.expertGoogler)){
					character.getArousal().gain(5);
				}
				character.getArousal().gain(5);
			}
			else if(r==2){
				if(character.has(Trait.mojoMaster)){
					character.getMojo().gain(Global.random(6) + 4);
				}
				character.getMojo().gain(Global.random(3) + 2);
			}
		}
		if(Global.getValue(Flag.CassieLoneliness)<0){
			Global.setCounter(Flag.CassieLoneliness, 0);
		}
		Global.modCounter(Flag.CassieLoneliness, 5);
		Decider.visit(character);
	}

	@Override
	public String describe(Combat c) {
		if(character.has(Trait.witch)){
			return "Cassie has changed a lot since you started the Game. Maybe she isn't that different physically. She has the same bright blue eyes and the same sweet smile. " +
					"The magic spellbook and cloak are both new. She's been dabbling in the arcane, and it may be your imagination, but you feel like you can perceive the power " +
					"radiating from her. Her magic seems to have given her more confidence and she seems even more eager than usual.";
		}
		else{
			return character.name+" is a cute girl with shoulder-length auburn hair, clear blue eyes, and glasses. She doesn't look at all like the typical sex-fighter. " +
				"She's short but not chubby: you would describe her body as soft rather than athletic. Her gentle tone and occasional " +
				"flickers of shyness give the impression of sexual innocence, but she seems determined to win.";
		}
	}

	@Override
	public String victory(Combat c,Result flag) {
		if(flag==Result.anal){
			character.arousal.empty();
			return "Cassie bucks her hips against your ass wildly causing the strapon to rub hard against your prostate. Your arms and legs feel like jelly as she thrusts in again and again. " +
					"Your almost shocked as you feel yourself on the edge of orgasm and your certain you wouldn't be able to stop yourself if Cassie keeps this pace up. Above you Cassie moans " +
					"loudly clearly in a world of her own. You don't think she even notices as the pleasure from your prostate overcomes you and you shoot your white flag of surrender on the " +
					"ground. As you orgasm, Cassie's thrusting kicks up another notch and shortly afterwards she comes from the stimulation of the strapon rubbing against her clit. You both collapse " +
					"to the ground and lie there for a minute or so catching your breaths. <i>\"I guess I got a bit too carried away and lost.\"</i> murmurs Cassie. You sigh internally and point out that " +
					"you actually came while she was pegging you. <i>\"You came?\"</i> she gasps. <i>\"I mean the shopkeeper said it would work but....\"</i> she trails off.  She smiles, and stands. <i>\"I never knew " +
					"I'd enjoy that so much.\"</i> Her grin widens in a way that makes you nervous. <i>\"I might need to try that again in the future.\"</i> Your decide to bid a hasty retreat leaving your " +
					"clothes behind to the victor.";
		}
		if(character.has(Trait.witch)&&character.has(Trait.silvertongue) && Global.random(3) == 0){
			character.arousal.empty();
			return "Cassie's efforts to pleasure you finally break your resistance and you find yourself completely unable to stop her. She slips between your legs and takes your straining " +
					"dick into her mouth. She eagerly sucks on your cock, while glancing up to meet your eyes. Her talented oral technique blows away your endurance and you spill your seed " +
					"into her mouth. She swallows your cum and smiles at you excitedly. <i>\"Gotcha. Did that feel good?\"</i> You nod and slump to the floor to catch your breathe.\n\nCassie goes " +
					"quiet for a bit and you realize you still need to return the favor. <i>\"It's not that,\"</i> Cassie replies when you broach the subject. <i>\"I've just been learning a spell that " +
					"I kinda want to try. Can we try it?\"</i> You nod your consent. You trust Cassie not to do anything really bad to you. She softly chants a spell with a fairly long incantation " +
					"and then kisses you on the lips. <p><i>\"Let's see if it worked.\"</i> She seductively slides down your body to bring her face next to your now flaccid dick again. She licks and " +
					"sucks your member until it returns to full mast. She lets your dick go and grins, looking more flushed and aroused than usual. <i>\"So far, so good.\"</i> She reaches between her " +
					"legs and masturbates in front of you. A gasp escapes you as you feel an unfamiliar pleasure between your legs. <i>\"Our senses are temporarily linked,\"</i> Cassie explains. " +
					"<i>\"I've always wondered what a blowjob felt like. I can see why you like it.\"</i><p>She leans into your chest and you embrace her, feeling a comfortable warmth. <i>\"Now that we're " +
					"sharing everything, shall we really feel good together?\"</i> She guides your penis into her waiting entrance and you both let out a moan as you thrust into her. In addition to " +
					"the usual pleasure from being surrounded by her hot, wet pussy, you're hit by a second surge of pleasure and a pleasant fullness you're not used to. Cassie seems pretty " +
					"overwhelmed by that simple movement as well. It's unlikely either of you will be able to last very long at this rate. <i>\"Probably not,\"</i> Cassie gasps out. <i>\"But imagine how it'll " +
					"feel if we cum together.\"</i> That's an intimidating prospect given how much insertion affected you both, but it's worth undertaking. Cassie rocks her hips on top of you while you " +
					"thrust slowly and steadily. Despite the dual channel pleasure making concentrating on your movements almost impossible, the two of you are able to synchronize perfectly. You " +
					"can feel the exact angle and speed to thrust to maximize Cassie's pleasure and she's probably doing the same for you. Without saying anything, you both accelerate your thrusts " +
					"as you both approach climax. You've never felt a girl's orgasm before, the pleasure seems to come from deep inside your core (her core technically), but you're pretty sure she's " +
					"as close as you are. Cassie kisses you passionately right as you both go over the edge and you shoot your seed into her shuddering pussy. It feels like you've been struck by lightning " +
					"and your vision goes white.<p>You gradually come to your senses and see Cassie collapsed next to you. Your faces are only inches apart and you can't resist kissing her gently as " +
					"she regains consciousness. The feeling is noticeably singular and you feel somehow lonely as you realize her spell must have worn off. <i>\"Wow,\"</i> she lets out breathlessly. " +
					"<i>\"That felt like I was 12 again and masturbating for the first time.\"</i> She suddenly turns bright red and hides her face in your chest. <i>\"You didn't hear that! Just pretend I " +
					"didn't say anything.\"</i>";
		}
		else if(flag==Result.intercourse){
			return "You feel yourself rapidly nearing the point of no return as Cassie rides your dick. You fondle and tease her sensitive nipples to increase her pleasure, but it's a losing battle. You're " +
					"going to cum first. She smiles gently and kisses you as you ejaculate inside her hot pussy. She shivers slightly, but you know she hasn't climaxed yet. When she breaks the kiss, her flushed " +
					"face lights up in a broad smile. <i>\"It feels like you released a lot. Did you feel good?\"</i> You groan and slump flat on the ground in defeat. She gives you a light kiss on the tip of your nose " +
					"and starts to grind her clit against your pelvis. <i>\"Come on, don't be mean. Tell me I made you feel good,\"</i> she whispers in a needy voice. <i>\"It'll help me finish faster.\"</i> Is she really " +
					"getting off on praise, or on the knowledge that her technique gave you pleasure? Either way, there's no reason to lie, she definitely made you feel amazing. She shudders and starts to breathe " +
					"harder as you whisper to her how good her pussy felt. She leans forward to present her " + character.body.getLargestBreasts().describe(character) + " to you. <i>\"Can you touch my nipples more? I really like that.\"</i> You reach up and play with " +
					"her breasts as she continues to grind against you. She stops your pillow talk by kissing you desperately just before you feel her body tense up in orgasm. She collapses on top of you and kisses " +
					"you cheek contently. <i>\"I'll keep practicing and make you feel even better next time, \"</I> she tells you happily. <i>\"I promise.\"</i> ";
		}
		else if(character.arousal.percent()>50){
			character.arousal.empty();
			return "Despite your best efforts, you realize you've lost to Cassie's diligant manipulation of your penis. It takes so much focus to hold back your ejaculation " +
					"that you can't even attempt to retaliate. She pumps your twitching dick eagerly as the last of your endurance gives way. The pleasure building up in the base " +
					"of your shaft finally overwhelms you and you almost pass out from the intensity of your climax. White jets of semen coat Cassie's hands in the proof of your defeat. <p>" +
					"As you recover, you notice Cassie restlessly rubbing her legs together with unfulfilled arousal and offer to help get her off however she prefers. She looks down at " +
					"your spent, shrivelled dick and gently fondles it while pouting cutely. <i>\"I have a cute boy all to myself, but he's already worn out.\"</i> She leans in close and whispers in " +
					"your ear, <i>\"If you get hard again, we can have sex.\"</i><p>Your cock responds almost immediately to her words and her soft caress. In no time, you're back to full mast. Cassie " +
					"straddles your hips and guides the head of your member to her entrance. She leans down to kiss you passionately as she lowers herself onto you. As you pierce her tight, wet pussy, " +
					"she moans into your mouth. She rides you enthusiastically and you can feel your pleasure building again despite having just cum. Cassie is breathing heavily and clearly on the " +
					"verge of her own orgasm. You fondle and pinch her nipples, which pushes her over the edge. Her pussy clamps down on you, squeezing out your second load. As her stamina gives out, " +
					"she collapses next to you. <i>\"Best prize ever. I should beat you more often,\"</i> you hear her mutter.";
		}
		else{
			return "Despite your best efforts, you realize you've lost to Cassie's diligent manipulation of your penis. It takes so much focus to hold back your ejaculation " +
				"that you can't even attempt to retaliate. She pumps your twitching dick eagerly as the last of your endurance gives way. The pleasure building up in the base " +
				"of your shaft finally overwhelms you and you almost pass out from the intensity of your climax. White jets of semen coat Cassie's hands in the proof of your defeat. " +
				"You recover your senses enough to offer to return the favor.<br><i>\"No need,\"</i> she teases good-naturedly. <i>\"I have a bit more self-control than a horny boy.\"</i><br> Her victorious smile is " +
				"bright enough to light up a small city as she gives you a chaste kiss on the cheek and walks away, taking your clothes as a trophy.";
		}
	}
	@Override
	public String defeat(Combat c,Result flag) {
		Character opponent;
		if(c.p1==character){
			opponent=c.p2;
		}
		else{
			opponent=c.p1;
		}
		if(character.has(Trait.witch) && Global.random(3) == 0){
			opponent.add(c, new Energized(opponent,10));
			return "You capture Cassie's lips and slip your hand between her legs to facilitate her imminent orgasm. You rub her soaked pussy lips and she moans against your lips. Her body " +
					"tenses as she clings to you, letting you know she's reached her climax. You keep rubbing her petals as she starts to relax. She shows no sign of breaking the kiss or " +
					"letting you go, so you decide to see if you can give her consecutive orgams.<p>You dig your fingers into Cassie's sensitive pussy and rub her insides. She eyes open " +
					"wide and she lets out a noise of surprise. You tease her tongue with your own and she melts against you again. It only takes a few minutes before her pussy squeezes your " +
					"fingers and she hits her second orgasm. Your fingers don't even slow down this time. You move away from her lips to focus on licking and sucking her neck. Her pussy twitches " +
					"erraticly as you finger her. <i>\"It's so intense! I can't stop twitching!\"</i> She moans plaintively, but she doesn't seem to dislike it. <i>\"I love it! But I think I'm going to die!\"</i> " +
					"You've been going easy on her clit up until now, but now you rub it firmly with your thumb and gently bite down on her collarbone. She screams in pleasure through her third orgasm, " +
					"which lasts much longer than her first two.<p>Cassie goes limp as you hold her tenderly. You haven't had any release, but she seems in no condition to help now. She makes " +
					"a content noise and looks ready to fall asleep in your arms. You tickle her lightly and remind her that the match isn't over. <i>\"Can't go on,\"</i> She murmers sleepily. <i>\"Already " +
					"dead. You've slain me.\"</i> She looks at you with half closed eyes. <i>\"You must be an angel, you're practically glowing.\"</i><p>Suddenly her eyes go wide and she sits bolt upright. " +
					"<i>\"You are glowing! You've got all my mana.\"</i> You look at your hands, which do seem to be faintly glowing and you feel unusually energized. Cassie groans quietly. <i>\"When a " +
					"mage orgasms, she releases some of her mana. You made me cum so much I don't have enough mana left to use my magic. Give it back!\"</i> She looks on the verge of tears, but you " +
					"don't actually know how to return her magic energy. She pushes you onto your back and straddles your unsatisfied erection. <i>\"We can fix this,\"</i> she mutters as she guides your " +
					"dick into her. <i>\"If you cum inside me, I'll get nearly all of it back. I just need to make sure I don't orgasm again.\"</i> She swings her hips to ride you. It doesn't take long " +
					"since you've yet to cum even once, but Cassie's already had three orgasms and she looks like she's closing in on her fourth. When you hit your peak and shoot your load into her, " +
					"she bites her lips and braces herself against the pleasure. <i>\"Thanks,\"</i> she whispers in a strained voice. <i>\"I'm powered up again.\"</i> She lifts her hips to get off of you, but the " +
					"sensation of your cock sliding out of her catches her by surprise and she shudders uncontrollably again. <i>\"Goddammit,\"</i> she whines pitifully. <i>\"It's just not fair.\"</i>";
		}
		else if(flag==Result.intercourse){
			return "As you thrust repeatedly into Cassie's slick folds, she trembles and moans uncontrollably. You lean down to kiss her soft lips and she responds by wrapping her arms around " +
					"you. You feel her nails sink into your back as she clings to you desperately. Her insides tighten and shudder around your cock as she orgasms. You keep kissing her and stroking " +
					"her hair until she goes limp. When you break the kiss she covers her beet red face with both hands. <i>\"I can't believe I came alone. You made me feel so good, I couldn't help it.\"</i> " +
					"You can't see her expression, but her voice sounds sheepish rather than defeated. <p>You spot her glasses on the floor nearby, knocked off in the throes of her orgasm. You pick them " +
					"up and gently push her hands away from her face. She's flushed and her bangs are matted to her face with sweat, but she's as beautiful as ever. You place her glasses on her " +
					"head and she smiles shyly. <i>\"I want you to keep moving. We won't be done until you orgasm too.\"</i> Your cock twiches inside her as if trying to remind you of its need. Cassie must " +
					"feel it, because she giggles and kisses you lightly. You start thrusting again and she gasps with delight. She's still sensitive from her climax and if possible you want to give her " +
					"another. You suck gently on her earlobe and feel her twitch in surprise at the sensation. You know you won't last much longer in her warm, tight pussy, but Cassie is completely entrusting " +
					"her body to you, giving you the freedom to pleasure her. You work your way down her neck, kissing, licking and listening to her breathing grow heavier. <p>Her reactions are having a " +
					"more of an effect on you than you expected. Soon you need to slow down to maintain control. <i>\"Keep going,\"</i> Cassie coos. <i>\"I want you to feel good. I want you to feel good because " +
					"of me.\"</i> You don't think she's quite there yet, but you speed up like she asks. In moments, you hit your peak and shoot your load inside her. Cassie lets out a moan and you feel her " +
					"shudder. Did she just cum again? She giggles again. <i>\"I guess having a cute boy climax inside me is a big turn-on. We should do this more often.\"</i> If she wants to lose to you more " +
					"often, you aren't going to complain. She sits up and kisses you softly on the cheek. <i>\"Maybe I'll win next time.\"</i>";
		}
		else{
			return "As Cassie moans and shivers, it's clear she's past the point of no return. <i>\"Please,\"</i> she begs. <i>\"Give me a kiss before I cum.\"</i> You kiss her firmly on the lips and " +
				"rub her clit relentlessly. She shudders and holds you tight as she rides out an intense orgasm. You wait until she comes down before gently disentangling yourself " +
				"from her embrace. <p><i>\"Thanks. Not that I'm happy about losing, but that felt amazing.\"</i> Cassie smiles " +
				"sheepishly and takes hold of your still-hard cock. <i>\"I'm the one who got you this turned on, right? Then I'm going to take responsibility and finish you off.\"</i> " +
				"You're slightly skeptical of her reasoning, not that you're going to turn down her offer. <p><i>\"As a girl, it would be a disgrace to get a boy all hot and bothered, " +
				"only to have another girl make him cum.\"</i> She explains. She sets to licking and stroking your dick, showing no less enthusiasm than she did during the fight. " +
				"The delightful sensations from her fingers and tongue soon bring you to a messy climax on her face. You thank her as you collect your clothes and hers, " +
				"leaving her naked, but still in good spirits.";
		}
	}
	public String draw(Combat c,Result flag){
		if(flag==Result.intercourse){
			return "You and Cassie move your hips against each other, both rapidly approaching orgasm. As you thrust again and again into her tight folds, you feel yourself pass " +
					"the point of no return. You need to make her cum, now! You kiss her passionately, forcing your tongue into her mouth. The deep kiss combined with your continous " +
					"thrusting have the desired effect. She embraces you tightly as her climax washes over her. At the same time, you fill her womb with your seed. You didn't quite " +
					"win, but a draw means you both get points, though you both also forfeit your clothes. Right now, you're both too tired to really care about that. You lie on the " +
					"floor trying to regain your strength, still holding each other, still joined below the waist. You probably look more like lovers than opponents now and part of you " +
					"feels the same. However, the match isn't over and you both need to get moving before your other opponents find you. You give Cassie a light kiss on the lips and part " +
					"ways.";
		}
		return "You finger Cassie's wet pussy as she frantically strokes your dick. You're both so close to the end that one of you could cum at any moment. There's no room for " +
				"positioning or strategy, just a simple endurance contest as you hold each other tightly. Cassie leans forward and kisses you passionately. She must be almost done, " +
				"you can feel her body trembling. Unfortunately, you've reached the limit of your endurance. <i>\"Are you going to cum?\"</i> Cassie manages to ask between moans. <i>\"Me too. " +
				"I think we're going to finish together.\"</i> You thrust your fingers as deep into her flower as you can and rub her love bud with your thumb. Her free hand pulls you into " +
				"a tight embrace as the last of your restraint gives out and you cover her stomach with semen.<p>You and Cassie lean against each other, exhausted and sticky. You " +
				"can't help noticing her pleased smile. You thought you felt her climax, but did she actually win? Cassie shakes her head, still smiling. <i>\"We came at the same time. " +
				"I'm just happy we're so closely matched. Every good protagonist needs a good rival to keep pushing her forward. I may be the least suited for this type of competition, " +
				"but I'm going to keep practicing and improving.\"</i> She pulls you close and kisses you again. <i>\"You're going to need to improve too, so you can keep up with me.\"</i> " +
				"She's pretty affectionate for a rival. She's probably better suited to be the protagonist's love interest. <i>\"The protagonist and the rival usually share feelings of " +
				"mutual respect and friendship. There's not reason they couldn't be lovers too. Besides, you're the rival character here. I'm totally the protagonist.\"</i>";
	}
	@Override
	public String bbLiner(Combat c) {
		return "Cassie winces apologetically. <i>\"That looks really painful. Sorry, but I can't afford to go easy on you.\"</i>";
	}
	@Override
	public String nakedLiner(Combat c) {
		return "Cassie blushes noticeably and covers herself. <i>\"No matter how much time I spend naked, it doesn't get any less embarrassing.\"</i>";
	}
	@Override
	public String stunLiner(Combat c) {
		return "Cassie groans softly as she tends her bruises, <i>\"Come on, you don't have to be so rough.\"</i> she complains.";
	}
	@Override
	public String taunt(Combat c) {
		return "Cassie giggles and taps the head of your dick. <i>\"Your penis is so eager and cooperative,\"</i> she jokes. <i>\"Are you sure you're not just letting me win?\"</i>";
	}
	@Override
	public String temptLiner(Combat c) {
		return "Cassie catches you glancing at her body, and blows you a kiss. <i>\"Why don't you just stop resisting and let me make you cum?\"</i>";
	}
	@Override
	public boolean fightFlight(Character opponent) {
		return !character.mostlyNude();
	}
	@Override
	public boolean attack(Character opponent) {
		return !character.mostlyNude();
	}

	public double dickPreference() {
		return 0;
	}

	@Override
	public String victory3p(Combat c, Character target, Character assist) {
		if(target.human()){
			return "Cassie positions herself between your legs, enjoying her unrestricted access to your naked body. She lightly runs her fingers along the length of your " +
					"erection and places a kiss on the tip. <i>\"Don't worry,\"</i> she whispers happily. <i>\"I'm going to make sure you enjoy this.\"</i> She slowly begins licking and " +
					"sucking you penis like a popsickle. You tremble helplessly as she gradually brings you closer and closer to your defeat. A low grunt is the only warning " +
					"you can give of your approaching climax, but Cassie picks up on it. She backs off your dick just far enough to circle her tongue around the sensitive head, " +
					"pushing you over the edge. You shoot your load over her face and glasses as she pumps your shaft with her hand.";
		}
		else{
			return "Cassie settles herself in front of "+target.name()+" and tenderly kisses her on the lips. <i>\"I don't really swing this way, but setting the mood is " +
					"important.\"</i> She leans in to lick and suck "+target.name()+" neck, before moving down to her breasts. She gives each nipple attention until "+target.name()+
					" is panting with desire. She continues downward to "+target.name()+"'s pussy and starts eating her out. "+target.name()+" moans loudly and arches her back against " +
					"you. You gently lower her to the floor as she recovers from her climax, while Cassie wipes the juices from her mouth and looks satisfied at her work.";
		}
	}
	@Override
	public String intervene3p(Combat c, Character target, Character assist) {
		if(target.human()){
			return "You grapple with "+assist.name()+", but neither of you can find an opening. She loses her balance while trying to grab you and you manage to trip her. " +
					"Before you can follow up, a warm body presses against your back and a soft hand gently grasps your erection. Cassie whispers playfully in your ear. <i>\"Hello "
					+target.name()+". How about a threesome?\"</i> You start to break away from Cassie, but "+assist.name()+" is already back on her feet. You struggle valiantly, " +
					"but you're quickly overwhelmed by the two groping and grappling girls. Cassie manages to force both your arms under her, leaving you helpless.<br>";
		}
		else{
			return "You wrestle "+target.name()+" to the floor, but she slips away and gets to her feet before you. You roll away to a safe distance before you notice that " +
					"she's not coming after you. She seems more occupied by the hands that have suddenly grabbed her breasts from behind. You cautiously approach and realize " +
					"it's Cassie who is holding onto the flailing "+target.name()+". Releasing her boobs, Cassie starts tickling "+target.name()+" into submission and pins her " +
					"arms while she catches her breath.<br>";
		}
	}
	@Override
	public String startBattle(Character other) {
		return "Cassie looks hesitant for just a moment, but can't contain a curious little smile as she prepares to face you.";
	}
	@Override
	public boolean fit() {
		return !character.mostlyNude()&&character.getStamina().percent()>=50&&character.getArousal().percent()<=50;
	}
	@Override
	public String night() {
		return "After the match, you stick around for a few minutes chatting with your fellow competitors. You haven't seen Cassie yet, but you at least want to say goodnight to her. " +
				"You feel a warm hand grasp yours and find Cassie standing next to you, smiling shyly. She doesn't say anything, but that smile communicates her intentions quite well. " +
				"You bid the other girls goodnight and lead Cassie back to your room. The two of you quickly undress each other while sharing brief kisses. You lay down on the bed and " +
				"she straddles you, guiding your dick into her pussy. She lets out a soft, contented noise as you fill her. Without moving her hips, she lays against you chest and kisses " +
				"you romantically. You embrace her and start thrusting your hips against her. She matches her movements to your slow, but pleasurable strokes. You both take your time, " +
				"more interested in feeling each other's closeness than in reaching orgasm, but gradually you both feel your pleasure building. Cassie buries her face in your chest, letting " +
				"out hot, breathy moans. You run you hands through her hair and softly stroke her back and the nape of her neck. It's hard to tell whether her orgasm set off your ejaculation " +
				"or the other way around, but you release your load into her shuddering pussy. Neither of you make any movement to seperate from each other. Remaining inside her until morning " +
				"sounds quite nice. <i>\"I love you.\"</i> The whisper was so soft you're not sure you heard it. When you look at Cassie's face, she's fast asleep.";
	}
	public void advance(){
		character.add(Trait.witch);
		character.body.addReplace(PussyPart.arcane, 1);
		character.unequipAllClothing();
		character.outfitPlan.add(Clothing.getByID("bra"));
		character.outfitPlan.add(Clothing.getByID("blouse"));
		character.outfitPlan.add(Clothing.getByID("cloak"));
		character.outfitPlan.add(Clothing.getByID("panties"));
		character.outfitPlan.add(Clothing.getByID("skirt"));
		character.outfitPlan.add(Clothing.getByID("shoes"));

		character.mod(Attribute.Arcane,1);
	}

	@Override
	public boolean checkMood(Combat c, Emotion mood, int value) {
		switch(mood){
		case nervous:
			return value>=50;
		case angry:
			return value>=150;
		default:
			return value>=100;
		}
	}

	@Override
	public String orgasmLiner(Combat c) {
		return "<i>\"Aaah please let me rest a bit... That was far too intense!\"</i>";
	}

	@Override
	public String makeOrgasmLiner(Combat c) {
		return "Cassie grins at you <i>\"Did you feel good? Come on, no time for rest now, I bet I can make you feel even better!\"</i>";
	}
}
