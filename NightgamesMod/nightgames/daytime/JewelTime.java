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
import nightgames.characters.custom.requirement.BodyPartRequirement;
import nightgames.global.Global;
import nightgames.items.Item;

public class JewelTime extends BaseNPCTime {
    public JewelTime(Character player) {
        super(player, Global.getNPC("Jewel"));
        knownFlag = "JewelKnown";
        giftedString = "\"Thanks! You're a pretty nice you know?\"";
        giftString = "\"A present? I'm not going to go easy on you even if you bribe me you know?\"";
        transformationOptionString = "Training";
        transformationIntro = "[Placeholder]<br>Jewel explains her training to you and how you can too train yourself.";
        loveIntro = "You're about to go see Jewel, but she shows up at your dorm room first. You invite her inside and she sits on your bed with her legs crossed. <i>\"I need "
                        + "some advice and you're my best friend, but I'm not sure if I should ask you. It's probably something I should talk to another girl about, but there aren't any girls "
                        + "I can really confide in. When I was growing up I mostly hung out with the boys, so all that girl talk is kinda.... Will you hear me out?\"</i> You're not sure how much "
                        + "help you can be on feminine issues, but if you're the only person Jewel trusts enough to confide in, you have to at least hear her out. Jewel takes a deep breath "
                        + "before speaking. <i>\"I think I may be in love with you, but I'm not sure how to tell.\"</i> That was... not what you expected. Jewel's face turns a bit pink as you're "
                        + "struggling to process her words. <i>\"I shouldn't have told you that, should I?\"</i> You feel a bit guilty for hesitating. That wasn't a love confession, she's looking "
                        + "for advice. You should reply with that in mind. You ask her what her reasons are for thinking she's in love. <i>\"I was trying to meditate for my training earlier, but "
                        + "I was unable to clear my mind. Your image kept coming up. I know I'm attracted to you, all the time not just when we're fighting. I also like being with you even when "
                        + "we're not fighting or having sex. Is that love?\"</i> That's a hard question. You don't really have a strict definition of love available. Let's try a different tactic, if "
                        + "she is in love, what does she want to change? Should the two of you be exclusive? Jewel shakes her head. <i>\"That would mean we'd have to quit the games. I don't want that. "
                        + "Besides, seeing you with another girl is a real turn on. Maybe I'd like more kissing.\"</i> That's easy to fulfill. You move forward and kiss her passionately. She makes "
                        + "a pleased noise and returns the kiss enthusiastically. Eventually you have to break for air. What else? Does she want to go on dates? <i>\"We already train together and "
                        + "play challenging games. That's better than dinner and a movie. I think I would like to spend more nights together.\"</i> That's also quite a reasonable request. Jewel "
                        + "hesitates for a moment. <i>\"Does that mean we're lovers?\"</i> You are in every way you want to be. As for the rest, you can both figure that out as you go. Jewel nods. "
                        + "<i>\"I'll keep thinking about it. In the meantime, what do you want to do on our date today?\"</i>";
        advTrait = Trait.fighter;
        transformationFlag = "";
    }

    @Override
    public void buildTransformationPool() {
        options = new ArrayList<>();
        TransformationOption enlightenedCock = new TransformationOption();
        enlightenedCock.ingredients.put(Item.PriapusDraft, 10);
        enlightenedCock.ingredients.put(Item.EnergyDrink, 40);
        enlightenedCock.ingredients.put(Item.JuggernautJuice, 10);
        enlightenedCock.requirements.add(new BodyPartRequirement("cock"));
        enlightenedCock.requirements.add((c, self, other) -> {
            return self.body.get("cock").stream().anyMatch(cock -> ((CockPart) cock).isGeneric());
        });
        enlightenedCock.additionalRequirements = "A normal cock";
        enlightenedCock.option = "Enlightened Cock";
        enlightenedCock.scene = "[Placeholder]<br>Jewel trains your cock to be enlightened.";
        enlightenedCock.effect = (c, self, other) -> {
            Optional<BodyPart> optPart =
                            self.body.get("cock").stream().filter(cock -> ((CockPart) cock).isGeneric()).findAny();
            BasicCockPart target = (BasicCockPart) optPart.get();
            self.body.remove(target);
            self.body.add(new ModdedCockPart(target, CockMod.enlightened));
            return true;
        };
        options.add(enlightenedCock);
        TransformationOption fieryPussy = new TransformationOption();
        fieryPussy.ingredients.put(Item.EnergyDrink, 40);
        fieryPussy.ingredients.put(Item.JuggernautJuice, 10);
        fieryPussy.ingredients.put(Item.FemDraft, 10);
        fieryPussy.requirements.add(new BodyPartRequirement("pussy"));
        fieryPussy.requirements.add((c, self, other) -> {
            return self.body.get("pussy").stream().anyMatch(part -> part == PussyPart.normal);
        });
        fieryPussy.additionalRequirements = "A normal pussy";
        fieryPussy.option = "Fiery Pussy";
        fieryPussy.scene = "[Placeholder]<br>Jewel trains your pussy to be fiery";
        fieryPussy.effect = (c, self, other) -> {
            self.body.addReplace(PussyPart.fiery, 1);
            return true;
        };
        options.add(fieryPussy);
    }

    @Override
    public void subVisitIntro(String choice) {
        if (npc.getAffection(player) > 0) {
            Global.gui().message(
                            "You plan to intercept Jewel on her run again, but when you get to the gardens, you find her already there, sitting on a low stone wall. She smiles "
                                            + "when she sees you and stands up to meet you. Apparently she was waiting for you, but you hadn't made plans to meet. How long was she waiting here?<br><i>\"I just got here a "
                                            + "few minutes ago. I had a feeling you were going to come here to see me. I've learned to always trust my intuition.\"</i> Her instincts are impressive and apparently convenient. "
                                            + "When you're within reach, she plants a light kiss on your lips. <i>\"If you came looking for me, I assume you're eager for some training. Tell me what you have in mind.\"</i>");
            Global.gui().choose(this, "Games");
            Global.gui().choose(this, "Sparring");
            Global.gui().choose(this, "Sex");
        } else if (npc.getAttraction(player) < 15) {
            Global.gui().message(
                            "You wait by the campus gardens, which according to Aesop, is on Jewel's jogging route. Sure enough, she comes running by within ten minutes and "
                                            + "calls out to you when she notices you. <i>\"Hey " + player.name()
                                            + ". It's good to see you. Are you here to train too?\"</i> She's not breathing particularly heavily and there's only "
                                            + "a subtle touch of sweat on her skin, so presumably you've caught her early in her run. You could pretend to have run into her by coincidence, but "
                                            + "you decide to be honest and tell her you were hoping to train with her. She raises an eyebrow and looks over you appraisingly. <i>\"I wouldn't mind having a running partner, "
                                            + "but I don't plan to slow down for you. Keep up if you can.\"</i><p>With that she takes off again and you hurriedly fall in next to her. For awhile, the two of you just jog side "
                                            + "by side. She doesn't seem inclined to start a conversation so you decide to keep quiet as well. You do have plenty of opportunity to appreciate the way Jewel's form-fitting "
                                            + "exercise clothes accentuate her toned body, especially when she quickens her pace and pulls ahead of you. She catches you ogling her and smiles. <i>\"Do you like what you see? "
                                            + "I'll give you some motivation. If you can keep up with me until the end, I'll let you feel me up as much as you want.\"</i><p>That's certainly a tempting offer, but keeping up is "
                                            + "quickly becoming more of a problem. Jewel keeps speeding up, probably caught up in her competitive spirit. After a few minutes more struggling to keep pace, you have to admit defeat. Jewel "
                                            + "looks disappointed, but gives you a light smack on the butt and a <i>\"Thanks for trying\"</i> before she continues on her run.");
            npc.gainAttraction(player, 2);
            player.gainAttraction(npc, 2);
        } else {
            Global.gui().message(
                            "You look around the campus and manage to catch Jewel while she's preparing for her run. She's doing stretches in a tight workout outfit that shows "
                                            + "off her midriff and legs. She sees you and gives you a friendly wave as you approach. You greet her and suggest spending some time training together. <i>\"I was just about "
                                            + "to go for a run. You're free to tag along if you want, but I'll be setting a fast pace today.\"</i> That seems like a bit of a waste. She can go running anytime, but some "
                                            + "things are only possible with a partner. She stops her warmup and smirks at you in amusement. <i>\"Do you actually have some real training in mind or was that a clumsy "
                                            + "attempt to get laid?\"</i> There's that too, but you point out that some light sparring would be directly applicable to the sort of matches you engage in every night. Even "
                                            + "getting laid could be considered training, since sexual technique and endurance are both essential to victory in sex fights.<p>She mulls over this idea, cheeks slightly "
                                            + "flushed imagining it. She's clearly interested, so you decide to give her one last push. You lightly brush a hand over her exposed midriff and teasingly ask if she is up for "
                                            + "more one-on-one competition. She shivers at your touch and kisses you firmly. <i>\"You know exactly how to make me wet. I'll accept your challenge anytime.\"</i>");
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
            Global.gui().choose(this, "Games");
            Global.gui().choose(this, "Sparring");
            Global.gui().choose(this, "Sex");
        }
        Global.gui().choose(this, "Leave");
    }

    @Override
    public void subVisit(String choice) {
        if (choice.equals("Sex")) {
            if (npc.getAffection(player) >= 16 && (!player.has(Trait.spiral) || Global.random(2) == 1)) {
                Global.gui().message(
                                "Jewel leads you to her bedroom instead of the wilderness for once. She seems unusually eager and almost like she could break into skipping at any time. "
                                                + "She's always been as fond of sex as the next girl, but this is uncharacteristic. You eventually decide to ask her if anything special is happening today. <i>\"Hmm? Well, "
                                                + "I guess I have some pretty high expectations for you today. Your sexual prowess has always been above average, but my instincts are telling me that you're capable of "
                                                + "more. I figure if we focus purely on sex in a suitable setting, I'll get to experience your full potential.\"</i> You hadn't realized she was expecting so much out of your "
                                                + "skill in bed. You haven't really been holding anything back. <i>\"I don't usually agree to sex without some sort of competition first. I'm expecting a really mind-blowing "
                                                + "orgasm in return.\"</i><p>Oh great. You're not sure you're going to be able to perform at all under that kind of pressure. When you reach Jewel's bedroom, she lets you "
                                                + "take charge. You kiss her firmly and push your tongue into her mouth. She makes a noise of approval, so you start to undress her while stroking and fondling the areas "
                                                + "you know she likes. By the time you slide off her panties, she's wet enough that they cling to her. Unfortunately you're having performance anxiety. The pressure of her "
                                                + "expectations is keeping you from getting more than half-hard. Your best hope for pleasing her is to use your mouth. You kiss your way to her clavicle, where you know she's "
                                                + "especially sensitive and linger there awhile. Once she's moaning and shivering, you work your way down between her breasts and lick around her navel. That earns you a "
                                                + "shudder of pleasure before you move further downward and place a kiss on her lower lips. She's clearly on the home stretch and ready to be finished off. You dive into her "
                                                + "muff and put your tongue to good use. Your tongue is still inside her when she climaxes and squeezes it tightly.<p>Hopefully that orgasm lived up to her expectations. She's "
                                                + "still breathing heavily, but she gives you one of her most aggressive grins. <i>\"That was a good start, but I've never been a one orgasm girl. Besides, as long as you've "
                                                + "got your pants one, we're still on the appetizer.\"</i> She rolls on top of you and strips you naked. Fortunately licking Jewel to orgasm was sufficient to overcome your nerves "
                                                + "and bring you to full mast. Once Jewel finishes undressing you, she lays back and spreads her legs for you. You rub the head of your dick against her entrance to tease her "
                                                + "a bit before you thrust into her slippery depths. You start moving your hips, but have trouble establishing a good rhythm. Jewel moans softly in time to your thrusts, but "
                                                + "looks somewhat dissatisfied. She eventually rolls on top of you and takes control of the thrusting. Soon she starts speeding up as she nears her peak, but you know she won't "
                                                + "be satisfied if you let it end like this. You brace yourself and manage to keep from cumming when she does. Jewel gives you another smile. <i>\"That felt good, but I know you "
                                                + "can do better. If you're the kind of man I think you are, you aren't finished yet. Just remember that if you cum, I won't wait for you to recover. Show me what you can do.\"</i><p>"
                                                + "You accept her challenge, but it's not going to be easy. Jewel expects something far better than her last two orgasms, and you're still pretty close to the edge. You take top "
                                                + "position again and start with shallow thrusts at an angle that maximizes contact with her love button. She shivers and lets out a mix between a moan and a whimper. <i>\"Good start. "
                                                + "MM! But you know I prefer it deep when I'mMM! turned on. How will you MM-manage that in your current state?\"</i> You grit your teeth and deepen your strokes, trying desperately to "
                                                + "hold back your orgasm. You almost lose it and have to slow down. <i>\"Not good enough. I'm not going to cum at this rate.\"</i> She pulls your head close and kisses you tenderly. <i>\"You "
                                                + "can do this. I believe in you.\"</i> There's no way you can disappoint her after coming this far. You put everything you have left into thrusting your hips, determined to make her cum"
                                                + "as soon as possible. Your determination seems to take hold of your body and your hips move on their own, circling around in a drilling motion that you wouldn't have thought possible. "
                                                + "Your already overstimulated cock rubs against Jewel's vaginal walls and she screams in pleasure even as you ejaculate into her. For some reason the words 'Giga Drill Break' come "
                                                + "unbidden to your mind, but you resist the urge to yell. It takes Jewel a lot longer to recover from this orgasm, but when she does she smiles contently. <i>\"That was even better than I "
                                                + "expected.\"</i>");
                if (!player.has(Trait.spiral)) {
                    Global.gui().message(
                                    "<p><b>You've learned to harness the awesome power that has laid dormant in your soul... maybe. You've at least learned to move your hips a lot during sex.</b>");
                    player.add(Trait.spiral);
                    npc.add(Trait.spiral);
                }
            } else {
                Global.gui().message(
                                "<i>\"Come on, it's not far now.\"</i> Jewel encourages you as you fall behind her slightly. The two of you are running at a slower pace than usual. You "
                                                + "aren't sure why you're running at all. You suggested sex and she seemed very excited about the prospect, but now you're running. Horizontal jogging can be used "
                                                + "as a euphemism for... no, you're sure you said sex. <i>\"Think of this as a warm up,\"</i> she explains when you ask her about the surprising absence of sexy times. "
                                                + "<i>\"If we get a little sweaty and tired now, we can have more fun when we arrive.\"</i> This doesn't really seem to make much sense, but you don't know where you "
                                                + "are going so you keep following her anyway. You're going through a heavily wooded park and seem to be getting further from the trail. You start wondering if she's planning some "
                                                + "weird survival game, when you hear the sound of running water.<p>Jewel stops next to a clear, flowing stream and immediately starts stripping naked. <i>\"I love "
                                                + "this place. The water is usually the perfect temperature for cooling down after a run.\"</i> Jewel tosses her panties onto a dry rock and gives you an impatient look. "
                                                + "<i>\"This isn't a free peepshow. You need to skinny dip too. Don't worry, nobody knows this place but me.\"</i> You feel somewhat honored that she'd share her secret "
                                                + "skinny dipping spot with you. You quickly undress and join Jewel, who is wading into the knee deep water. The water would be uncomfortably cold if you hadn't just been "
                                                + "running. Jewel sits down to immerse more of her body in the water and you follow her lead, letting the current wash your sweat away.<p>After a few minutes, she stands "
                                                + "up and turns to you. <i>\"This is great, but we came here with some very important activities planned. Shall we?\"</i> When you stand up, she lets out an uncharacteristically "
                                                + "girly giggle. <i>\"It is very cold isn't it?\"</i> she says while looking between your legs. You flush in shame and try to hide the effects of the cold water, but she gently "
                                                + "takes hold of your genitals and starts warming them up with her hands. <i>\"It's ok, I have someplace nice and hot to put it.\"</i><p>Jewel sits down on a large rock and "
                                                + "spreads her legs invitingly. She uses two fingers to spread her lower lips open and show you she's already wet with more than just water. The sight instantly brings "
                                                + "you to full mast. You line up your cock with her entrance and thrust into her in one smooth motion. She moans loudly from the sudden entry and smiles at you lustily. "
                                                + "<i>\"That never gets old.\"</i> Her insides are tight and just as hot as she promised. After sitting in the chilly stream, her pussy feels like a volcano. You start to move "
                                                + "your hips and soon find a comfortable, steady rhythm to fucking her. She has to hold onto you to keep from sliding off the rock. You keep up this pace until you feel "
                                                + "like you're reaching your limit. Jewel notices you're about to cum and she seems on the verge too. <i>\"Cum in me! I need to feel your seed inside.\"</i> You oblige her and "
                                                + "shoot your hot load into her womb. You immediately feel her tense up as she hits her own peak. Once you catch you breath, you pull out of her. Your semen leaks out of "
                                                + "Jewel's vagina, but she looks no worse for wear. <i>\"Well worth a bit of a hike, don't you think?\"</i> she asks, smiling. <i>\"We should probably both wash up a bit before we "
                                                + "head back.\"</i>");
            }
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Seduction);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Games")) {
            if (npc.getAffection(player) >= 8 && (!player.has(Trait.fearless) || Global.random(2) == 1)) {
                Global.gui().message(
                                "Today you've brought a wargame to appeal to Jewel's love of military history. You let her play the Allies, both because her father is a soldier and because the "
                                                + "Allies are on offense in this scenario. She advances all her troops from the start, taking heavy casualties, but stealing a couple victory point locations from you. You bide "
                                                + "your time, not worried yet. You are being pushed back, but you're retreating into more defensible positions. More importantly, Jewel will start pulling back her damaged units "
                                                + "soon or risk losing them. That should give you some breathing room and maybe create openings where you can counterattack. Several turns later it's clear she's not going to pull "
                                                + "back. The sustained contact is taking a heavy toll on your troops, but you cycle the more damaged ones away from the front to minimize loses, while Jewel is losing units unnecessarily "
                                                + "by keeping them in combat. She has a bit of a buffer since she started with a lot more troops than you, but this isn't a sustainable long term strategy.<p>Jewel eventually stands up, walks "
                                                + "around the table, and kisses you firmly on the lips. You're used to this. She's gotten in the habit of kissing you when she gets too hot and bothered during a game. It doesn't "
                                                + "really seem to cool her arousal, but it apparently helps her restrain herself until the game is over. It is also a sign that you're going to be ravaged later. Maybe the reason for her "
                                                + "all-out offensive is that she's feeling impatient and not planning carefully.<p>When she settles back in across the table, you feel compelled to speak up and "
                                                + "warn her that she's going to lose if she doesn't pull her weakened units back, but she shakes her head. <i>\"Fortune favors the bold.\"</i> The very next turn she draws a new card and "
                                                + "immediately plays it: Their Finest Hour. This is disastrous for you. She can attack with all her units this turn and they're all in firing range. She hits you across the whole front, "
                                                + "wiping out your damaged units and earning her just enough victory points to win the scenario.<p>That was an extraordinary bit of luck for her. Those tactics would have been suicidal "
                                                + "on a real battlefield. <i>\"This isn't a real battlefield,\"</i> she retorts. She points to one of the troops in her casualty pile. <i>\"This is a piece of plastic, not a person with parents, "
                                                + "kids, or a spouse. In a game, the only thing that matters is victory. When you lose a piece, no one has to cry over a friend or family member's grave. You can't treat war like a game, "
                                                + "and likewise you can't treat a game like war.\"</i><p>She stands up and practically drags you out of your chair. <i>\"Enough serious talk. Strip and get on the bed. I'm on top today.\"</i>");
                if (!player.has(Trait.fearless)) {
                    Global.gui().message("<p><b>Jewel has taught you a valuable lesson in reckless determination.</b>");
                    player.add(Trait.fearless);
                    npc.add(Trait.fearless);
                }
            } else {
                Global.gui().message(
                                "You were somewhat worried how Jewel would respond to the prospect of playing some potentially geeky games, but it seems your concerns were completely unfounded. "
                                                + "The idea of running around with a longsword and slaying monsters immediately appeals to her. She's extremely sharp and learns complex rules in no time. She has also studied enough "
                                                + "military history to recognize a good strategy. No matter what you play, these gaming sessions always prove to be enjoyable mediums for the two of you to get closer. The only "
                                                + "problem is that Jewel's fetish for competition prevents you from finishing long games. Whenever a tense game's climax starts to drag on, she inevitably attacks you and tears "
                                                + "off your clothes. Honestly, as far as problems go, you could do much worse.");
            }
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Cunning);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Sparring")) {
            if (npc.getAffection(player) >= 12 && (!player.has(Trait.dirtyfighter) || Global.random(2) == 1)) {
                Global.gui().message(
                                "Your martial arts training has finally reached the point where Jewel is willing to spar with you seriously. She's all business when it comes to sparring. There "
                                                + "are no sex holds and no stripping. The only victory condition is to make your opponent admit defeat. Jewel darts in with a snap-kick, which you barely manage to block, and "
                                                + "ducks out of the way of your counter. In theory, neither of you is trying to actually hurt the other, but if you miss a block she won't be able to pull the punch in time. "
                                                + "You hate to admit it, but there's still a much better chance of her hitting you than the other way around. Still, you've definitely gotten a lot better at this. When she "
                                                + "takes a chance with a big roundhouse, you manage to block it and sweep her other leg, dropping her to the floor.<p>This is the first time you've managed a decisive takedown "
                                                + "against Jewel. You do your best to suppress a grin as you reach down to help her back up. She catches you off guard by grabbing your wrists as her foot shoots up between your "
                                                + "legs and connects solidly with your groin. You let a strangled groan as you fall to the floor next to her and curl up in the fetal position. She grasps your head and brings her "
                                                + "face close to yours. Her expression is unexpectedly frustrated. <i>\"Did you hear me surrender? Don't assume the match is over just because you have the advantage.\"</i><p>She kisses you "
                                                + "aggressively before climbing over your body to spoon against you from behind. <i>\"The Romans believed that there could be no victory as long as the defeated did not believe they "
                                                + "were vanquished.\"</i> She inexplicably begins an history lecture as if you weren't groaning in pain. <i>\"That's what saved them during the Second Punic War, when Hannibal outmaneuvered "
                                                + "and destroyed their armies time and again. Instead of surrendering, they rebuilt their armies, evaded Hannibal's forces, and struck the strategically valuable and unguarded city of New Carthage. In fighting terms, they "
                                                + "were flat on their backs, so they aimed right for Carthage's balls. Speaking of which...\"</i> She slides her hand down the front of your pants to lightly grasp your testicles. "
                                                + "<i>\"You haven't given up yet. Should I assume you want to keep fighting?\"</i> You hastily concede before your situation becomes any more painful. She breathes lustily in your ear and "
                                                + "fondles your penis. <i>\"Try to recover quickly. I need this in me as soon as possible.\"</i>");
                if (!player.has(Trait.dirtyfighter)) {
                    Global.gui().message(
                                    "<p><b>You've learned the hard way that kicks can be dangerous, even when you're down.</b>");
                    player.add(Trait.dirtyfighter);
                    npc.add(Trait.dirtyfighter);
                }
            } else {
                Global.gui().message(
                                "When you suggested a bit of sparring to Jewel, you weren't expecting anything like this. Despite your experience grappling and sex fighting each night, you don't "
                                                + "have any formal martial arts experience. Jewel was not very impressed when she saw your technique and she insisted that this time would be best spent training you. <i>\"I'll shape you "
                                                + "into a man who can proudly stand as my rival.\"</i> with that plan, the two of you soon find a small out-of-the-way courtyard and start training.<p>Jewel is a strict drill sergeant and "
                                                + "reprimands you whenever your technique fails to match up to her standards. You feel like you're learning quickly for a beginner, but her criticism doesn't let up. As she starts "
                                                + "teaching you more advanced techniques, she adds a new rule to keep you motivated. <i>\"From now on, each time you make a mistake, you have to remove an article of clothing.\"</i> Now this "
                                                + "would be fine if the two of you were training in a private room, but you're doing this outside. This yard isn't visible from any windows and is away from most foot traffic, but there "
                                                + "is no guarantee that someone won't wander in. <i>\"Try to keep your pants on then. If you do really well, I'll take some clothes off too.\"</i><p>You manage to keep your pants for about 15 "
                                                + "minutes. You lose your boxers a few minutes later. To be fair, you've done so well until now, that Jewel is topless. You're still trying to get used to being naked outside during "
                                                + "the day, when you hear giggling behind you. Turning around, you see a couple girls enjoying your nude martial arts exhibition. One of them is taking pictures with her phone. Jewel, "
                                                + "still caught up in drill instructor mode, immediately chews out the pair. <i>\"We have no room here for slack-jawed spectators. If you're going to laugh at my prized student, get in "
                                                + "here and show me what you can do!\"</i><p>It says a lot about the force of Jewel's personality that both girls joined the training without much protest. Apparently the same clothing loss "
                                                + "penalty applies to them too. They've long since stopped making fun of you when their clothes start coming off. By the time training is over, you still don't know either girl's name "
                                                + "but you feel a faint sense of solidarity with them, even though Jewel let them keep their panties on.");
            }
            Global.gui().choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Power);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Leave")) {
            done(true);
        }
    }

}
