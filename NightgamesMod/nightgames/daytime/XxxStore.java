package nightgames.daytime;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;

public class XxxStore extends Store {
    public XxxStore(Character player) {
        super("XXX Store", player);
        Clothing.getAllBuyableFrom("XxxStore").forEach(article -> {
            add(article);
        });
        add(Item.Dildo);
        add(Item.Onahole);
        add(Item.Lubricant);
        add(Item.Crop);
        add(Item.Tickler);
        add(Item.Strapon);
        add(Item.Blindfold);
    }

    @Override
    public boolean known() {
        return Global.checkFlag(Flag.basicStores);
    }

    @Override
    public void visit(String choice) {
        if (choice.equals("Start")) {
            acted = false;
        }
        Global.gui().clearText();
        Global.gui().clearCommand();
        if (choice.equals("Leave")) {
            done(acted);
            return;
        }
        checkSale(choice);

        if (player.human()) {
            if (choice.startsWith("Learn to be Submissive")) {
                Global.gui().message(
                                "Your curiosity gets the best of you as you look over the leather collars on display. You were trying to figure out whether you could use on of these on an opponent, but clearly some of them are designed for men. You're idly wondering how uncomfortable they would be, when you suddenly feel something wrap around your neck.<p>You jump away in alarm and turn to face your attacker. There's a slim girl standing behind you, holding a black leather collar. She's less than intimidating. She's shorter than you, with blonde hair, short pigtails, and thick glasses. She has a friendly smile, that should suit her cute features, but there's something wrong with it. It must be her eyes, which are strangely hollow, like a doll's. She hands you the collar she just tried to put around your neck.<p>\"You should try this one. Most of those chokers are designed for gimps, or for pet play. You're not that kind of sub, are you? You should stick with something more comfortable.\" The collar is soft and supple, clearly higher quality than the ones you were looking at earlier. Is she an employee? That would explain why she's trying to sell you something. However, it is very presumptuous of her to assume you are a sub, just because you're looking at bondage gear. Her smile doesn't change, almost as if it's a mask. \"You're not much of a sub yet, but you have some aptitude for it. If you put in a little effort, I think you could become an amazing bottom.\"<p>She's kinda missing the point. You aren't trying to become a sub, you're looking for a way to gain an advantage. Sexfighting favors the dom. If you're going to win the Games, you need to overpower your opponents, not be at their mercy. The girl's expression still remains unchanged. For just a moment, though, her smile looks more like a sneer of contempt. \"Such a shallow understanding of power...\"<p>She starts to walk past you, but suddenly grabs your "
                                                +

                player.getOutfit().getTopOfSlot(ClothingSlot.top)
                                                + " and pulls you off balance. You catch yourself before you fall completely onto the smaller girl, but you're "
                                                + "still pressed closely against her. With her slumped meekly against the wall and you leaning over her, she looks pretty helpless. However, you feel her hand gripping "
                                                + "your balls through your "
                                                + player.getOutfit().getTopOfSlot(ClothingSlot.bottom)
                                                + ", preventing you from moving away from her. \"A dom can only be as strong as his sub is weak.\" She's whispering so softly that you "
                                                + "need to lean closer to hear her. \"Therefore it is the sub who really controls the relationship. If you really want to understand your relationships, you shouldn't "
                                                + "worry so much about winning or losing.\"<p>"
                                                + "The girl lets go of your groin and you notice the stares you're getting from the other customers. From their perspective, it must look like you aggressively pinned her "
                                                + "against the wall. You hastily move a step back. \"I have no interest in Doms. Any idiot can wield strength as a weapon. It takes a special individual to wield weakness. "
                                                + "I can teach you how to embrace submission, if I have nothing better to do, but I won't make any promises.\" She starts to walk away, but you stop her. You haven't "
                                                + "even got her name yet.<br>"
                                                + "She looks at you with that same hollow expression. \"Why would a ghost need a name? You're the only one who can see me anyway.\"<p>"
                                                + "...<p>"
                                                + "After a long pause, you squeeze her small, but obviously corporeal breast. She lets out a girly yelp, which earns you some more looks from customers. \"It was just a "
                                                + "joke. You didn't have to grope me so roughly. My name is Alice.\"<p>");
                Global.flag(Flag.metAlice);
                player.mod(Attribute.Submissive, 1);
            }
            if (choice.startsWith("Talk to Alice")) {
                Global.gui().message(
                                "Alice gives you a cheerless smile as you approach. You tell her that you are interested in becoming a better sub. She says nothing but beckons you to a room in the back of the store. There are some boxes of inventory stacked up, but it looks like this might be used as a break room. There an office chair, minifridge and a small TV. You don't see any sign of employees. In a little mom and pop adult store like this, there's probably only one person on a shift. Alice motions to the chair. \"Sit.\" Her tone is mild and sounds less like a command and more like a statement of fact.<p>You comply with her instruction and she sits on your lap. This training was inevitably going to lead to some sexual contact, but you are still caught off guard by the warmth of her slim body through your clothes. She's attractive, but something about her still makes you uncomfortable. She leans against you and whispers casually into your ear. \"The essence of being a sub is learning to be at the mercy of another.\" You hear a metallic click behind you and realize that Alice has handcuffed you to the chair. You feel a bit nervous, but you suppress the feeling. You came her so that she could dominate you, this shouldn't be a surprise. She stands up and shows you a keyring with a single key on it, presumably belonging to the handcuffs. \"Today, you're going to be at the mercy of complete strangers.\"<p>Wait.... You're starting to get an idea of what she's planning and it's not good. She's not planning to leave you here, is she? \"Don't worry. I've leave the key here. You just need to convince someone to come uncuff you. If you're still locked up when the store closes, I'll come get you, but that will be considered a failure.\" She kneels in front of you and removes your pants and underwear. Your situation is becoming more dire by the second. \"I'm just putting the key somewhere it'll be easy to find.\" She slips the keyring onto your flaccid penis and fondles you until you are erect enough to keep it in place. She walks behind you, ignoring your protests. When you turn the chair to face her, she leans down and kisses you firmly on the mouth. Before you can react, she forces a mouthful of sweet liquid into your mouth and you end up swallowing it on reflex. You look at her questioningly as she breaks the kiss. \"It's a little something to keep you from going soft anytime soon. We wouldn't want the key to fall off, would we?\" At that, she walks away, leaving you half-naked and alone.<p>You take a couple minutes to assess your situation. The aphrodisiac is taking effect and the keyring is uncomfortably tight around your fully erect boner. There's no way you're going to get it off without help. Is there anything in the room you could use to get out of your cuffs? No. This isn't an adventure game. Alice left the door to the main store open, so you can hear customers moving around. Well, of all the stores in which this could happen, a sex store is probably the best. Someone shopping for sex toys is more likely to accept the situation than someone buying a pair of socks. Left with no other option, you work up the nerve to call out for help.<br>");

                switch (Global.random(3)) {
                    case 2:
                        Global.gui().message(
                                        "You manage to get the attention of an attractive woman in her mid 20's. You explain that your ex-girlfriend left you in the compromising position out of spite. She gives you a skeptical look. <i>\"An ex-girlfriend. Right....\"</i> She clearly doesn't believe you, but she agrees to help you anyway. She attempts to pull the snug fitting keyring off of your penis. Ow ow ow! That's on way too tight! <i>\"Your stupid hard-on is holding the key in place. This isn't going to come off until you cum, is it?\"</i> She lets out a sigh of disgust. <i>\"If you weren't so pathetic looking, I'd probably call the cops, you exhibitionist pervert!\"</i><br>You swear that this situation is not your own doing. You were handcuffed and stripped against your will. <i>\"Like I'm going to believe that when you're this horny. Oh, fuck it.\"</i> She grabs your cock roughly and starts to rapidly jerk you off. Her other hand rubs the sensitive head of your penis. <i>\"You're probably enjoying this, but I'll at least make sure it's quick.</i><p>You squirm in agonzing pleasure. Her handjob feels really good, but it's so intense that it's practically torture. You whimper out a plea for her to be more gentle.<br><i>\"Shut up and cum!\"</i> She snaps at you forcefully.<p>You soon do as she commands, ejaculating on her hands with an involuntary moan of pleasure and relief. You slump down in the chair as you catch your breath. You keep your eyes lowered to avoid the woman's glare of contempt. As your sensitive penis finally softens, she roughly yanks the key ring off of it. She wipes her hands unceremoniously on your shirt before she uncuffs you. <i>\"There, satisfied?\"</i><p>You massage the feeling back into your wrists and offer a meek thanks. You'd been avoiding looking at the woman's face, so you only now notice how flushed she's become. She stares at the handcuffs she just removed. <i>\"Are these in stock? You may have just made a sale.\"</i>");

                        break;
                    case 1:
                        Global.gui().message(
                                        "Your cries catch the attention of a girl dressed like a punk rocker. She doesn't seem disturbed by your nakedness, but she is curious how you ended up like this. You explain that your ex-girlfriend cuffed you and left you in this compromising position. She looks slightly amused as she looks down at your bare groin. <i>\"Your ex, huh? What did you do to her?\"</i> You tell her you didn't do anything, which isn't a lie. <i>\"You must have done something to end up like this.\"</i> She casually grabs your sensitive cock so she can inspect the keyring on it. <i>\"This is on here pretty tight. We're going to need you to get small before we can get this thing off you. I don't mind helping with that.\"</i> This is a humiliating experience, but she is very pretty. You're already starting to leak precum as she handles your shaft and part of you is very much looking forward to her assistance.<p>Your expectations are betrayed when she reaches down and squeezes your testicles. You groan in pain and surprise. How is this suppose to be helping? <i>\"If I want to make my boyfriend soft, this is usually the fastest way. How else are we going to get that key off?\"</i> She gives you another firm squeeze and you stifle a cry of pain. <i>\"It's not working on you, though. You're still totally hard. Are you a masochist? Should I do it harder?\"</i> It's probably a result of the aphrodisiac still affecting you. Maybe she can try something other than torture? She smirks at you. <i>\"You're hoping I'll get you off?\"</i> You nod, desperate for relief. <i>\"You're going to have to earn it.\"</i> <p>She pushes the chair over so you're lying on your back. As you look up at her in confusion, she strips off her tight jeans and mounts your face. Her pussy presses against your mouth as she starts to stroke your shaft. <i>\"Hurry up and start licking. If you cum before I do, I'll have to punish you.\"</i> Fortunately, she's already very wet. Apparently, she's been very much enjoying 'helping' you. She's pretty good with her hands, but not sexfighter level good. Even with the aphrodisiac increasing your sensitivity, you're able to hold back your orgasm as you eat her out.<p><i>\"Ah! Your tongue feels so fucking good!\"</i> She's grinding herself against your mouth now and can't contain her moans. It's nice to see the effect you can have on a normal girl. The only girls you've been with recently are just as experienced as you are. Licking a novice to orgasm puts your techniques into perspective. You quickly feel her pussy spasm in ecstasy.<p>Finally, some relief is imminent. Since you've safely gotten her off first, you can stop resisting the pleasure of her relentless handjob. Your pent up ejaculation bursts forth with surprising force, hitting the girl's face and chest.<br><i>\"Hey!\"</i> She smacks you firmly in your very sensitive balls. You'd scream in pain if your mouth wasn't covered. <i>\"How about warning me before you cum? I was planning to go over to my boyfriend's place after this, but now I have to wash up and change so I don't smell like jizz! Oh hey, your dick finally got soft.\"</i><p>She retrieves the key without much difficulty and unlocks your handcuffs. You're finally free... to curl up in the fetal position after that last nutshot. She slips her jeans back on and does her best to clean the cum off her shirt. <i>\"Other than the mess, this was more fun than I was expecting to get out of this shopping trip. Maybe we'll run into each other again sometime. If you're really lucky, I may even tell you my name.\"</i>");

                        break;
                    default:
                        Global.gui().message(
                                        "A very familiar face pokes into the room. <i>\"I thought I recognized that voice.\"</i> Holy shit. You don't think you've ever been happier to see Mara. What is she doing here? <i>\"I just bought a new toy that I think you'll like.\"</i> That can wait until later. Right now, you just want to get free. She curiously inspects your situation as you explain how you ended up like this. <i>\"Submissive training? You know, I could always dominate you if you want me to.\"</i> She leans in close and looks at the key on your penis. <i>\"That looks pretty tight, but I have a good idea.\"</i> She starts to lick and suck your rod. You groan quietly at the pleasure from her warm, wet mouth. The aphrodisiac Alice slipped you is making you more sensitive than usual and her tongue feels great. Once she's thoroughly coated your member in saliva, she removes her mouth and teases your sensitive glans with her finger. You're so turned on that you're leaking liberal amounts of precum. She mixes the fluid with her saliva and soon your shaft is slick with the makeshift lubricant. She gently eases the keyring off your erection and waves the key victoriously in front of you.<p>That was brilliant! As soon as she uncuffs you, you're eager to give her a a good fucking to thank her. She wags a finger at you scoldingly and puts the key in her pocket. <i>\"Not so fast. You're still in the middle of training, right? You were just lucky that I happened to be here. If I let you out without bullying you, it'll defeat the entire point.\"</i><p>This particular scenario wasn't really what you signed up for. At this point, you're ready to just call it a day.<br><i>\"Nah.\"</i> She leans down and resumes her blowjob. The sudden pleasure causes you to throw your head back and moan in an undignified manner. Her small mouth can only contain part of your erect length, but her tongue is very energetic and her technique is quite skilled. This feels amazing and you're going to cum in no time, but if Mara's planning to be dommy, it's only a matter of time...<p><i>\"That's all.\"</i> She says cheerfully. She ceases her oral effort and the wonderful sensations disappear. You groan in frustration as your cock throbs with need. <i>\"This is fun. I don't get enough opportunities to tease and deny during the Games. I wish they would give me bonus points for making my opponents beg.\"</i> She leans down and plants a kiss on your cock head. <i>\"Ooh! We should try out my new toy!\"</i><p>She opens up a bag and pulls out a pink cocksleeve. <i>\"I don't have any lube, but you seem pretty slippery already.\"</i> She slips the toy over your erection without any resistance. It is snug, but comfortable. This seems like a normal cocksleeve, if a little heavier than it looked. Mara grins impishly and holds up a small remote. <i>\"Built in vibrator.\"</i> She hits the button and the toy starts vibrating all around your cock. You let out a little moan at the stimulation. It's not at pleasant as Mara's tongue, but it provides a different sort of pleasure.<p> She sits on your lap so her groin is touching the outside of the vibrating toy. <i>\"That feels nice, doesn't it? I like a toy we can share.\"</i> She pulls you into a soft kiss and carelessly drops the remote to the floor. <i>\"No stopping this time, go ahead and cum for me.\"</i> You feel self conscious about how eagerly she's staring at your face, but you can't deny the effect the persistent vibration is having on you. You're already past the point of no return.<p>You close your eyes and groan as you cum. Your seed quickly fills the snug cocksleeve and starts to leak out. Mara giggles as she watches your reaction. <i>\"You make some nice faces. I should record it next time.\"</i> As thrilled as you are to provide her entertainment, she still hasn't shut off this damn toy. It's starting to get very uncomfortable against your overstimulated cock. <i>\"Oh right, where did that thing get too?\"</i> She leans down to the floor, searching for the remote while continuing to press herself against the toy. She manages to retrieve the remote, displaying impressive flexibility, but still doesn't shut it off. You writhe in discomfort while she sighs contently. <i>\"This is pretty nice, maybe I'll leave it on a bit longer.\"</i> She kisses you tenderly, probing your mouth with her tongue. You find yourself unable to resist her adorable charm, even while she's basically torturing you.<p>Eventually, you have to break the kiss and beg for mercy. <i>\"Oh, fine.\"</i> She finally shuts off the vibration and you slump down in relief. She uncuffs you and retrieves her messy cocksleeve. <i>\"Feeling more subby yet? If you want to be tied up and teased, you can always just give me a call instead of going through all this trouble.\"</i>");
                }

                Global.unflag(Flag.AliceAvailable);
                player.mod(Attribute.Submissive, 1);
            }

            Global.gui().message("The adult specialty store stocks several items that could be useful during a match.");
            for (Item i : stock.keySet()) {
                if (!player.has(i)) {
                    Global.gui().message(i.getName() + ": $" + i.getPrice());
                } else {
                    Global.gui().message(i.getName() + ": $" + i.getPrice() + " (you have: "
                                    + player.getInventory().get(i) + ")");
                }
            }
            Global.gui().message("You have :$" + player.money + " to spend.");
            Global.gui().sale(this, Item.Lubricant);

            if (player.has(Item.Dildo)) {
                Global.gui().message("You already have a perfectly serviceable dildo. You don't need another.");
            } else {
                Global.gui().sale(this, Item.Dildo);
            }

            if (player.has(Item.Onahole)) {
                Global.gui().message("You already have the best onahole in stock. You don't need another.");
            } else if (player.has(Item.Onahole2)) {
                Global.gui().message("You already have the best onahole you can dream of.");
            } else {
                Global.gui().sale(this, Item.Onahole);
            }

            if (player.has(Item.Crop)) {
                Global.gui().message("You already have a riding crop. You don't need two.");
            } else if (player.has(Item.Crop2)) {
                Global.gui().message("Your current riding crop is already overkill.");
            } else {
                Global.gui().sale(this, Item.Crop);
            }

            if (player.has(Item.Tickler)) {
                Global.gui().message("Your current tickler is at least as good as anything they are selling.");
            } else if (player.has(Item.Tickler2)) {
                Global.gui().message("Nothing on sale is half as good as your current tickler.");
            } else {
                Global.gui().sale(this, Item.Tickler);
            }

            if (player.hasDick()) {
                Global.gui().message("You see a strap-on dildo for sale. It's no use to you since you have "
                                + "the real thing, but you should watch your ass if the girls start buying these.");
            } else if (player.has(Item.Strapon)) {
                Global.gui().message("You are plenty satisfied with the strap-on you already have.");
            } else if (player.has(Item.Strapon2)) {
                Global.gui().message("Your strapon is even better than the real thing already.");
            } else {
                Global.gui().sale(this, Item.Strapon);
            }

            displayClothes();
            if (Global.checkFlag(Flag.AliceAvailable)) {
                Global.gui().message(
                                "You see Alice hanging around near the bondage gear. You aren't sure whether she's waiting for you or not.");
                Global.gui().choose(this, "Talk to Alice");
            }
            if (player.getLevel() >= 5 && !Global.checkFlag(Flag.metAlice) && !Global.checkFlag(Flag.victory)) {
                Global.gui().choose(this, "Learn to be Submissive");
            }
            Global.gui().choose(this, "Leave");
        }
    }

    @Override
    public void shop(Character npc, int budget) {
        if (!npc.has(Item.Lubricant, 10)) {
            int i = Math.min(budget / Item.Lubricant.getPrice(), 10 - npc.count(Item.Lubricant));
            npc.gain(Item.Lubricant, i);
            //budget -= i * Item.Lubricant.getPrice();
            npc.money -= i * Item.Lubricant.getPrice();
        }
    }
}
