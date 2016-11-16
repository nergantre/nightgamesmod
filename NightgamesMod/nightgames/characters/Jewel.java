package nightgames.characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import nightgames.characters.body.AnalPussyPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.FacePart;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.combat.CombatScene;
import nightgames.combat.CombatSceneChoice;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.skills.strategy.FacesitStrategy;
import nightgames.skills.strategy.FootjobStrategy;
import nightgames.skills.strategy.KnockdownStrategy;
import nightgames.skills.strategy.ReceiveAnalStrategy;
import nightgames.skills.strategy.StraponStrategy;
import nightgames.start.NpcConfiguration;

public class Jewel extends BasePersonality {
    /**
     *
     */
    private static final long serialVersionUID = 6677748046858370216L;

    public static final String JEWEL_ANAL_FOCUS = "JewelAnalFocus";
    public static final String JEWEL_MARTIAL_FOCUS = "JewelMartialFocus";
    public static final String JEWEL_MENTAL_FOCUS = "JewelMentalFocus";
    public static final String JEWEL_PHYSICAL_FOCUS = "JewelPhysicalFocus";
    
    public Jewel() {
        this(Optional.empty(), Optional.empty());
    }

    public Jewel(Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        super("Jewel", 1, charConfig, commonConfig, true);
    }

    @Override
    public void applyStrategy(NPC self) {
        self.plan = Plan.hunting;
        self.mood = Emotion.confident;

        self.addPersonalStrategy(new FootjobStrategy());
        self.addPersonalStrategy(new FacesitStrategy());
        self.addPersonalStrategy(new KnockdownStrategy());
        self.addPersonalStrategy(new StraponStrategy());
        getCharacter().addPersonalStrategy(new ReceiveAnalStrategy());
    }

    @Override
    public void applyBasicStats(Character self) {
        preferredCockMod = CockMod.enlightened;
        self.outfitPlan.add(Clothing.getByID("bra"));
        self.outfitPlan.add(Clothing.getByID("tanktop"));
        self.outfitPlan.add(Clothing.getByID("panties"));
        self.outfitPlan.add(Clothing.getByID("jeans"));
        self.outfitPlan.add(Clothing.getByID("sneakers"));
        self.outfitPlan.add(Clothing.getByID("socks"));
        self.change();
        self.modAttributeDontSaveData(Attribute.Power, 2);
        self.modAttributeDontSaveData(Attribute.Speed, 1);
        Global.gainSkills(self);
        self.getMojo().setMax(80);

        self.setTrophy(Item.JewelTrophy);
        self.body.add(BreastsPart.c);

        // fairly feminine face
        self.body.add(new FacePart(.1, 1.9));
        self.initialGender = CharacterSex.female;
    }

    @Override
    public void setGrowth() {
        Growth growth = character.getGrowth();
        growth.stamina = 3;
        growth.arousal = 3;
        growth.bonusStamina = 3;
        growth.bonusArousal = 1;
        growth.willpower = .7f;

        character.addCombatScene(new CombatScene((c, self, other) -> {
            return character.getLevel() >= 10 && !Global.checkFlag(JEWEL_ANAL_FOCUS) 
                            && !Global.checkFlag(JEWEL_MARTIAL_FOCUS);
        }, (c, self, player) -> character.name + " leans back a bit as she watches you recover from your fight."
                        + " <i>\"Hey, " + player.name + ". Pop quiz. If I were to knock you on your ass and then"
                        + " ride your cock with </i>my<i> ass, which part would you enjoy more?\"</i>", Arrays.asList(
                        new CombatSceneChoice("The takedown", (c, self, other) -> {
                            c.write("<i>\"Oh really? Well, at least you're being honest. "
                                            + "I'll reward your honesty. Oh, yes. I will.\"</i>");
                            Global.flag(JEWEL_MARTIAL_FOCUS);
                            growth.addTrait(11, Trait.grappler);
                            growth.addTrait(25, Trait.disablingblows);
                            growth.addTrait(37, Trait.naturalTop);
                            growth.addTrait(47, Trait.takedown);
                            return true;
                        }),
                        new CombatSceneChoice("The ass-fucking", (c, self, other) -> {
                            c.write("<i>\"Of course. Why do I even ask? Not very subtle, are you?"
                                            + " Well, anyway, I'm sure I can use that. Better get"
                                            + " yourself ready.\"</i>");
                            Global.flag(JEWEL_ANAL_FOCUS);
                            growth.addTrait(11, Trait.powerfulhips);
                            growth.addTrait(25, Trait.tight);
                            growth.addTrait(37, Trait.powerfulcheeks);
                            growth.addTrait(47, Trait.temptingass);
                            return true;
                        })
        )));

        character.addCombatScene(new CombatScene((c, self, other) -> {
            return character.getLevel() >= 10 && !Global.checkFlag(JEWEL_MENTAL_FOCUS) 
                            && !Global.checkFlag(JEWEL_PHYSICAL_FOCUS);
        }, (c, self, player) -> " <i>\"So, " + player.name + ". You're going to be doing what I tell"
                        + " you. No, don't interrupt. You are. My question is, are you just going"
                        + " to listen to me, or am I going to have to physically force you?\"</i>", Arrays.asList(
                        new CombatSceneChoice("No, No! I'll listen!", (c, self, other) -> {
                            c.write("<i>\"Good. Remember that. Because I </i>will<i>"
                                            + " be making demands.\"</i>");
                            Global.flag(JEWEL_MENTAL_FOCUS);
                            growth.addTrait(22, Trait.commandingvoice);
                            growth.addTrait(39, Trait.mentalfortress);
                            if (Global.checkFlag(JEWEL_ANAL_FOCUS)) {
                                growth.addTrait(50, Trait.bewitchingbottom);
                            } else {
                                growth.addTrait(50, Trait.unquestionable);
                            }
                            return true;
                        }),
                        new CombatSceneChoice("Yeah. No. Try me.", (c, self, other) -> {
                            c.write("<i>\"Oh ho ho! Think you're tough, do you? I am already looking"
                                            + " forward to knocking those balls you've apparently found"
                                            + " somewhere all the way up to your throat! And I'll make sure"
                                            + " you enjoy it, too.\"</i>");
                            Global.flag(JEWEL_PHYSICAL_FOCUS);
                            growth.addTrait(22, Trait.indomitable);
                            growth.addTrait(39, Trait.confidentdom);
                            if (Global.checkFlag(JEWEL_ANAL_FOCUS)) {
                                growth.addTrait(50, Trait.drainingass);
                            } else {
                                growth.addTrait(50, Trait.edger);
                            }
                            return true;
                        })
        )));
        
        preferredAttributes.add(c -> c.get(Attribute.Ki) < 15 ? Optional.of(Attribute.Ki) : Optional.empty());
        preferredAttributes.add(c -> c.get(Attribute.Ki) >= 15 && c.get(Attribute.Fetish) < 100
                        ? Optional.of(Attribute.Fetish) : Optional.empty());
        preferredAttributes.add(c -> c.get(Attribute.Power) < 80 ? Optional.of(Attribute.Power) : Optional.empty());
        growth.addTrait(0, Trait.wrassler);
        growth.addTrait(0, Trait.direct);
        growth.addTrait(0, Trait.insatiable);
        growth.addTrait(1, Trait.fitnessNut);
        growth.addTrait(4, Trait.QuickRecovery);
        growth.addTrait(7, Trait.analTraining1);
        growth.addTrait(10, Trait.powerfulhips);
        // 11 - Choice 1, trait 1
        growth.addTrait(13, Trait.shameless);
        growth.addTrait(16, Trait.limbTraining1);
        growth.addTrait(19, Trait.oiledass);
        growth.addTrait(20, Trait.dominatrix);
        growth.addTrait(22, Trait.alwaysready);
        // 22 - Choice 2, trait 1 -- Two traits at this level? alwaysready isn't that powerful
        // 25 - Choice 1, trait 2
        growth.addTrait(28, Trait.limbTraining2);
        growth.addTrait(31, Trait.analTraining2);
        growth.addTrait(34, Trait.exhibitionist);
        // 37 - Choice 1, trait 3
        character.getStamina().setMax(100);
        character.getArousal().setMax(70);
        growth.addBodyPart(40, new AnalPussyPart());
        // 39 - Choice 2, trait 2

        growth.addTrait(43, Trait.analTraining3);
        growth.addTrait(46, Trait.strongwilled);
        // 47 - Choice 1, trait 4
        growth.addTrait(49, Trait.smqueen);
        // 50 - Choice 2, trait 3
        growth.addTrait(52, Trait.autonomousAss);

    }

    @Override
    public void rest(int time) {
        if (character.rank >= 1) {
            if (!character.has(Trait.fighter) && character.money >= 1000) {
                advance();
            }
        }
        super.rest(time);
        if (!(character.has(Item.Crop) || character.has(Item.Crop2)) && character.money >= 200) {
            character.gain(Item.Crop);
            character.money -= 200;
        }
        if (!(character.has(Item.Strapon) || character.has(Item.Strapon2)) && character.money >= 600) {
            character.gain(Item.Strapon);
            character.money -= 600;
        }

        if (character.rank >= 1) {
            if (character.money > 0) {
                Global.getDay()
                      .visit("Dojo", character, Global.random(character.money));
                Global.getDay()
                      .visit("Meditation", character, Global.random(character.money));
            }
            if (character.money > 0) {
                Global.getDay()
                      .visit("Body Shop", character, Global.random(character.money));
            }
            if (character.money > 0) {
                Global.getDay()
                      .visit("Workshop", character, Global.random(character.money));
            }
        }

        if (character.money > 0) {
            Global.getDay()
                  .visit("Bookstore", character, Global.random(character.money));
        }
        if (character.money > 0) {
            Global.getDay()
                  .visit("XXX Store", character, Global.random(character.money));
        }
        if (character.money > 0) {
            Global.getDay()
                  .visit("Black Market", character, Global.random(character.money));
        }
        int r;

        for (int i = 0; i < time; i++) {
            r = Global.random(8);
            if (r == 1) {
                Global.getDay()
                      .visit("Exercise", this.character, 0);
            } else if (r == 0) {
                Global.getDay()
                      .visit("Browse Porn Sites", this.character, 0);
            }
        }
        Decider.visit(character);
    }

    @Override
    public String bbLiner(Combat c, Character other) {
        return "Jewel gently pats your injured testicles. <br><i>\"These things are the reason I'm glad I was born a girl. If I had a pair of big dangling targets between my legs, "
                        + "I could never concentrate on fighting.\"</i>";
    }

    @Override
    public String nakedLiner(Combat c, Character opponent) {
        return "Jewel smiles and makes no effort to hide her nakedness. <i>\"Feel free to enjoy the view. I love fighting naked, it gives me so much freedom of movement.\"</i>";
    }

    @Override
    public String stunLiner(Combat c, Character opponent) {
        return "Jewel takes several heaving breaths, looking beaten and exhausted. She suddenly grins ear to ear. <i>\"OK, I'm impressed.\"</i>";
    }

    @Override
    public String taunt(Combat c, Character opponent) {
        if (character.has(Trait.bitingwords) && c.getStance().dom(character)) {
            ArrayList<String> possible = new ArrayList<>(); 
            Character other = c.getOpponent(character);
            possible.add("Jewel looks down at you with a sadistic smirk, <i>\"That's a nice look on you there "
                            + c.getOpponent(character)
                               .getName()
                            + ".\"</i>");
            possible.add("Shifting her weight a bit to glare into your eyes, Jewel says happily, <i>\"Aha, it looks like you're quite comfortable there. Maybe you're a natural bottom?\"</i>");
            if (other.hasBalls()) {
                possible.add("Jewel cups your vulnerable balls and gives them a light squeeze. <i>\"Worthless boys like you should just give up. Why even try when you end up as my seat every time?\"</i>");
            }
            return possible.get(Global.random(possible.size()));
        }
        return "Jewel glares at you and squeezes your dick tightly. <i>\"No matter how horny you are, you better give me your best fight. I don't like fucking weaklings.\"</i>";
    }

    @Override
    public String temptLiner(Combat c, Character opponent) {
        return "Jewel licks her lips, <i>\"Soon, I'll have you wailing like a whore.\"</i>";
    }

    @Override
    public String victory(Combat c, Result flag) {
        Character other = c.getOpponent(character);
        Collection<BodyPart> selfOrgans = c.getStance().partsFor(c, character);
        Collection<BodyPart> otherOrgans = c.getStance().partsFor(c, other);
        if (BodyPart.hasType(otherOrgans, "ass") && c.getStance().anallyPenetrated(c, other)) {
            BodyPart insertable = character.body.getRandomInsertable();
            String selfODesc = insertable == null ? "[none]" : insertable.describe(character);
            return "You gasp as Jewel pounds away at your ass without mercy. You're going to cum, you realize. "
                            + (other.hasDick()
                                            ? "She isn't even touching your dick but the sensation of her " + selfODesc
                                                            + " moving over your prostate is mind blowing. "
                                            : "The sensation of the " + selfODesc
                                                            + " moving in and out of your rectum is mind blowing. ")
                            + "<i>\"Stop pretending to resist. Cum for me!\"</i> she grunts as she humps your backdoor. Your hands ball up and your toes curl in as you "
                            + "feel yourself hit the edge. Jewel must feel you tighten up as she redoubles her speed. A loud moan escapes your mouth as your resistance finally shatters "
                            + (other.hasDick() ? "and cum spurts from your cock. " : "and you cum hard. ")
                            + "<i>\"Man, that was hot.\"</i> Jewel whispers almost to herself as she pulls away. She doesn't give you long to collect yourself before she rolls "
                            + "you over and sits on your face. <i>\"Come on,\"</i> she chuckles in a gloating manner. <i>\"You seemed to enjoy that, so its only fair you return "
                            + "the favor.\"</i> You sigh mentally and resign yourself to licking her out. It's not long before she is moaning above you. After Jewel collects your clothes she "
                            + "looks at you over her shoulder as she leaves and confesses, <i>\"I've punished assholes before, but I always wanted to try pegging a "
                            + other.guyOrGirl()
                            + " I like. Thanks for being cool about it.\"</i> You smile back tell her she's welcome, but you'd appreciate it if she weren't so rough. "
                            + "The last thing you hear before she round the corner is her giggled response. <i>\"We'll see.\"</i>";
        }
        if (character.has(Trait.fighter) && character.get(Attribute.Ki) >= 10) {
            String message = "Your duel with Jewel is rapidly reaching its conclusion and it's not going that well for you. Something seems different about her, her moves have an additional level of "
                            + "coordination that is made worse by her already monstrous strength. But the worst thing is that she is throwing powerful moves at you like they were nothing!<p>"
                            + "All of this has culminated in the situation you find yourself in now, namely pinned to the ground by Jewel as "
                            + (other.hasDick() ? "she jerks you off," : "she fingers you,")
                            + " her teasing bringing you very rapidly to orgasm.<p>"
                            + "You give one more push against the woman on top of you trying to dislodge her but with another firm shove you find yourself once more pinned by Jewel. With "
                            + "this you are unable to stop Jewel from bringing you to climax and you groan in pleasure as you do so.<p>"
                            + "Once you are finished you look up to see Jewel grinning down at you.<br>"
                            + "<i>\"Well, you didn't put up much of a fight did ya?\"</i> She taunts, <i>\"must have really wanted me to be on top.\"</i><br>"
                            + "You feel yourself flush slightly from the embarrassment, but you are unable to complain as you can't deny that you did enjoy yourself. ";
            if (other.hasDick()) {
                message += "As all of this occurs Jewel has yet to fully release your dick and you soon feel yourself becoming hard again.<p>"
                                + "When Jewel notices your returning hard-on, her face flashes through surprise before fixing itself in a predatory smile. <i>\"You never do disappoint.\"</i> She quips as "
                                + "she moves to align herself with your now fully erect cock. As she does so, you feel a smirk make itself onto your face. However it doesn't last because as soon "
                                + "as your dick enters Jewel, you are unable to stop the gasp of yelp of surprise as the sensation hits you. Jewel's insides are incredibly hot! It's to the point where you "
                                + "aren't sure if it feels too hot or if its the best thing you've felt. As such, you make a strangled gasping sound as Jewel fully sheathes you in herself.<p>"
                                + "Only when you are fully inside her does Jewel notice the 'unique' expression that must be adorning your face. ";
            } else if (other.hasPussy()) {
                message += "As all of this occurs Jewel has yet to withdraw her roving hands from your cunt.<p>"
                                + "When Jewel notices your pussy moistening and your body flushing again, her face flashes through surprise before fixing itself in a predatory smile. "
                                + "<i>\"You never do disappoint.\"</i> She quips as she moves to align her cunt with yours. As she does so, you feel a smirk make itself onto your face. "
                                + "However it doesn't last because as soon as your girl parts touch, you are unable to stop the gasp of yelp of surprise as the sensation hits you. "
                                + "Jewel's pussy is incredibly hot! It's to the point where you aren't sure if it feels too hot or if its the best thing you've felt. "
                                + "As such, you make a strangled gasping sound as Jewel starts grinding into you.<p> Only after pressing her pussy against yours for a good minute does Jewel notice the "
                                + "'unique' expression that must be adorning your face. ";
            }
            message += "<i>\"Hey, what's wrong?\"</i> She asks momentarily dropping "
                            + "her grin but quickly regains it as understanding spreads across her face. <i>\"Ahhhh,\"</i> She exclaims, leaning down so that her face is close to yours, <i>\"Is it a bit "
                            + "too warm for you down there?\"</i> She asks, her face full of mock sympathy. You are still slightly off kilter from the unique feeling of Jewel's molten warmth, so you "
                            + "can only demurely shake you head as you look upwards at Jewel.<p>"
                            + "<i>\"Good,\"</i> she breathes as she begins to grind her hips against yours, <i>\"Because I don't think I'd stop anyway.\"</i>";
            if (other.hasDick()) {
                message += "With this said Jewel begins to ride you hard, jackhammering down onto you ruthlessly.<p>"
                                + "The sensations that this brings are overwhelming. Whenever your penis is extracted from Jewel, the cold air makes you gasp. But when you are fully inside her, the "
                                + "heat is nearly unbearable. Because of this, you don't know whether to tell Jewel to stop or beg her to keep going.<p>"
                                + "It's probably a moot point anyway as Jewel is in her own world above you, slamming your length in and out of her overheated canal and soon you begin to notice her "
                                + "breathing becoming more ragged and her pace reaching a crescendo.<p>"
                                + "With a mewl of pleasure Jewel climaxes above you, bringing you fully inside as she does so. The sensation of her superheated walls clamping down on you is too much "
                                + "and you find yourself coming once again.<p>";
            } else if (other.hasPussy()) {
                message += "With this said Jewel begins to scissor you, grinding down onto you ruthlessly.<p>"
                                + "The sensations that this brings are overwhelming. The heat is nearly unbearable and because of this, you don't know whether to tell Jewel to stop or beg her to keep going.<p>"
                                + "It's probably a moot point anyway as Jewel is in her own world above you, sliding her cunt across yours, and soon you begin to notice her "
                                + "breathing becoming more ragged and her pace reaching a crescendo.<p>"
                                + "With a mewl of pleasure Jewel climaxes above you, pressing her superheated nether lips against yours and making you come yet again.<p>";
            }
            message += "After you both catch your breath, Jewel stands and extracts you from herself. You can't help the groan of discomfort that escapes you when your privates are exposed to "
                            + "the cold once again. Jewel laughs at your discomfort and looks down at you as she collects her clothes.<p>"
                            + "<i>\"Better be careful,\"</i> She taunts, <i>\"If you don't step up your game, you're never gonna beat me. Then again, I do so enjoy fucking you, so feel free to be my bitch "
                            + "whenever you want.\"</i> Jewel turns on her heel and you just about catch the sight of her grinning face before she walks away, leaving you to try and recover from the "
                            + "hurricane that just passed by.";
            return message;
        }
        if (BodyPart.hasType(selfOrgans, "pussy") && BodyPart.hasType(otherOrgans, "cock")) {
            return "Jewel rocks her hips on top of you, full of confidence and in complete control. She reaches behind her with one hand to play with your balls, as if to prove "
                            + "you're completely at her mercy. Her powerful inner muscles squeeze your cock in time with her movements, creating an irresistable sensation. You moan as "
                            + "you pass the point of no return, but in one swift motion, she moves off your dick and finishes you off by hand. Your semen spills onto your stomach fruitlessly, "
                            + "leaving Jewel's body untouched. <p><i>\"That was a good effort, but losers don't get to cum inside me.\"</i> She crawls up your body until she's straddling your face and "
                            + "her wet pussy is just inches away. <i>\"Besides, you still have work to do and I'm not so cruel as to give you a mouthful of spunk.\"</i> She presses her pussy against "
                            + "your mouth. Her musky scent and tangy taste flood your senses, but you remember to do your job properly. You alternate between licking her clit and sticking your "
                            + "tongue into her entrance until you feel her body shudder. You get a faceful of her love juice. <p>Jewel hops to her feet, apparently recovering from her orgasm in just "
                            + "a couple seconds. <i>\"You're a good loser at least, but if you really want me, you're going to need to do better than that.\"</i> At that, she walks away without a "
                            + "second glance in your direction.";
        } else {
            String message = "";
            if (other.hasDick()) {
                message += "You can't hold on any longer and Jewel clearly knows it. She jerks you off mercilessly as cum sprays over your stomach. For a moment, she just looks "
                                + "at the semen coating her hand. Then she sighs and wipes it off on your chest. <i>\"That's disappointing. I was hoping you would be a real challenge. I suppose I'm just too strong for you.\"</i> ";
            } else if (other.hasPussy()) {
                message += "You can't hold on any longer and Jewel clearly knows it. She pumps her fingers in your cunt mercilessly as your juices fly all over the floor. For a moment, she just looks "
                                + "at the juices coating her hand. Then she sighs and wipes it off on your chest. <i>\"That's disappointing. I was hoping you would be a real challenge. I suppose I'm just too strong for you.\"</i> ";
            } else {
                message += "You can't hold on any longer and Jewel clearly knows it. She thrusts her tongue in your mouth while fingering your ass as you cum hard. "
                                + "For a moment, she just looks at you as you're panting from exertion as if considering asking for more. Then she looks away and sighs. "
                                + "<i>\"That's disappointing. I was hoping you would be a real challenge. I suppose I'm just too strong for you.\"</i>";
            }
            if (character.orgasms > 0 || character.getArousal()
                                                  .percent() > 50) {
                character.arousal.empty();
                if (other.hasDick()) {
                    message += "<i>\"Still, you were at least able to turn me on this much. Maybe you deserve a "
                                    + "reward.\"</i><p>She prods your rapidly softening dick with a frown. <i>\"If you'd held on just a little longer, you could have cum inside me. Oh well, I can at least give "
                                    + "you a show.\"</i> She stands up and straddles your head, giving you a clear view of her soaked pussy. She parts her lower lips with her fingers and caresses her entrance "
                                    + "with a soft moan. You watch, captivated by her masturbation as she slips two fingers into herself and begins pumping them in and out. The sight is enough to revitalize "
                                    + "your erection and soon your dick is straining with need. <i>\"Are you going to give me a show too? Well, go ahead.\"</i> At her prompting, you take your cock in your hand and "
                                    + "start to jerk off. She slows down her finger movements to let you catch up. As you start nearing your second ejaculation, she times her own orgasm to match yours. "
                                    + "When you shoot your load into the air, drops of her love juice spray down on your face as she moans and climaxes.";
                } else {
                    message += "<i>\"Still, you were at least able to turn me on this much. Maybe you deserve a "
                                    + "reward.\"</i><p>She prods your rapidly spent body with a frown. <i>\"If you'd held on just a little longer, you could have cum with me. Oh well, I can at least give "
                                    + "you a show.\"</i> She stands up and straddles your head, giving you a clear view of her soaked pussy. She parts her lower lips with her fingers and caresses her entrance "
                                    + "with a soft moan. You watch, captivated by her masturbation as she slips two fingers into herself and begins pumping them in and out. The sight is enough to revitalize "
                                    + "you and soon your pussy is pulsing with need. <i>\"Are you going to give me a show too? Well, go ahead.\"</i> At her prompting, you put your fingers inside yourself and "
                                    + "start to masturbate. She slows down her finger movements to let you catch up. As you start nearing your second orgasm, she times her own orgasm to match yours. "
                                    + "When you arch your back as you cum, drops of her love juice spray down on your face as she moans and climaxes.";
                }
            } else {
                if (other.hasDick()) {
                    message += "She grabs your tender spent dick, which doesn't really hurt, but gets your undivided attention.";
                } else if (other.hasPussy()) {
                    message += "She pinches your tender clit, which doesn't really hurt, but gets your undivided attention.";
                } 
                message += "<i>\"Try not to bore me next time or I may not play so nice with you.\"</i>"
                                + "<br/>"
                                + "She gets dressed and walks away with your clothes, leaving you naked and beaten."
                                + "<br/>";
            }
            return message;
        }
    }

    @Override
    public String defeat(Combat c, Result flag) {
        Character other = c.getOpponent(character);
        if (character.has(Trait.fighter)) {
            return "Jewel falters as her arousal begins to overwhelm her. You manage to force her to the ground and pin her hands. You press your thigh against her slick pussy, "
                            + "making her moan in pleasure. You rub her with your leg and suck on her neck until she can't resist grinding against you. She climaxes with a scream and you kiss "
                            + "her on the lips to quiet her. You start to get off her, but her voice stops you. <i>\"Don't tell me you're done already. I can keep going for another couple rounds "
                            + "at least.\"</i><p>She's talking big, considering she clearly just orgasmed a moment ago. The fight is yours. <i>\"Yes, technically you won, but how victorious can you "
                            + "really feel when I'm not satisfied yet?\"</i> She spreads her lower lips lewdly in a clear invitation. <i>\"Are you going to shrivel up and run away when there's a hot "
                            + "pussy waiting for rematch?\"</i> You knew Jewel gets off on a challenge, but she really turns into an insatiable slut when you beat her. She gives you a challenging "
                            + "grin. <i>\"Don't try to pretend you're any less horny than I am. Also I should warn you that I can kick really hard, even from this position.\"</i><p>She doesn't need to "
                            + "threaten you when she's offering you her gorgeous naked body. You thrust deep into her in a single push and she lets out a gasp of pleasure. You indulge yourself in "
                            + "fondling her big, perky breasts and pinching her nipples. Jewel lifts her hips to give you a better angle and you respond by pounding her pussy wildly. She is an "
                            + "orgasm ahead of you, but you know she'll cum again in no time when she's being fucked this hard. She's moaning in time with your thrusts and probably wouldn't be "
                            + "able to keep her hips up if you weren't supporting her. You feel your climax approaching and bury yourself deep in Jewel. Her pussy clenches around your spurting "
                            + "cock, squeezing out every drop of your seed.<p>Jewel goes completely limp and you collapse next to her. She better be satisfied after that, because you have nothing "
                            + "left to give. She suddenly grabs your head and pulls you into a forceful kiss. <i>\"You're fucking amazing. There aren't a lot of men I'd concede defeat to, but I like "
                            + "being under you.\"</i> She practically hops back to her feet. Jesus, her recovery speed is inhuman. <i>\"Of course, next time I'm going to have to throw you to the ground "
                            + "and ravage you. I promise you'll love it.\"</i>";
        }
        if (c.getStance().vaginallyPenetrated(c, character)) {
            return "You fuck Jewel passionately, driving her closer to orgasm. She runs her fingers through your hair and wraps her legs tightly around your hips. Judging by her moaning, "
                            + "she must know that she's losing, but that seems to be turning her on even more. Well, there's no reason to go easy on her. You bury your face in her neck and start to "
                            + "lick and suck. You feel a small tremor run through her body in response, followed by a much stronger shudder when she orgasms moments later. Her vaginal walls "
                            + "tightly squeeze your cock as her toned body tenses up.<p>Jewel goes limp in the aftermath of her climax and you brush her disheveled hair from her face. <i>\"You're still "
                            + "horny, aren't you?\"</i> Your unsatisfied erection is still fully buried inside her. You're definitely going to have to do something about that once she's ready to continue. "
                            + "Jewel clicks her tongue disapprovingly. <i>\"Don't wait for my permission. My body is all yours.\"</i> That catches you by surprise. She made quite a sexy declaration, but "
                            + "Jewel doesn't seem like a girl who would freely submit herself to someone else's will. <i>\"Oh, don't get me wrong. As soon as we part, we're back to being opponents. Next "
                            + "time I kick your ass, I'll do whatever I want to you. It's only fair that it works both ways.\"</i> Really? She's ok with anything? She shrugs, unconcerned. <i>\"If you tried "
                            + "something really nasty, I bet our chaperones would intervene. Still, I think I can take any fetish that you might have. I'll just have to return it tenfold next time.\"</i> "
                            + "Well, you weren't planning on anything bizarre, but the idea of dominating Jewel a little....<p>You flip Jewel over and put her on her hands and knees. You position yourself "
                            + "behind her and give her a light swat on the butt. She lets out a yelp of surprise and looks back at you with an expression of amusement and arousal. You penetrate her in "
                            + "a single, fast thrust, eliciting another surprised noise. You pound her pussy vigorously and fondle her soft breasts from behind. Jewel obviously loves this violent "
                            + "treatment. She's moaning with abandon, but it doesn't hide the wet sounds from her soaked pussy. As you fuck her doggy-style, you feel your orgasm building and Jewel "
                            + "seems to be in the same state. You maul her tits and thrust completely into her as you shoot your load. She screams in ecstasy when she cums and you both collapse to "
                            + "the floor. <p>You lie there, enjoying the afterglow. She rolls over and kisses you firmly. She seemed to enjoy that a lot. Is she still going to retaliate tenfold? <i>\"Oh "
                            + "yes, a combination of submission and pleasure. I'm sure I'll think of something good.\"</i>";
        } else if (other.hasDick()) {
            return "Jewel trembles and her moans rapidly increase in pitch. You take a dominant position on top of her with one hand knuckles deep in her flooded pussy and the other groping "
                            + "her beautiful breast. She's obviously on the verge of defeat, but she refuses to give in. She grits her teeth and endures the pleasure while desperately grasping at your "
                            + "erection. You redouble your efforts, stroking and kissing her most sensitive spots. She arches her back and "
                            + "shudders in orgasm, then goes limp so suddenly you think she may have passed out. <p>When you lean over her to check that she's ok, she grabs you and rolls "
                            + "on top of you. She smashes her lips against yours and forces her tongue into your mouth. <i>\"You win round 1,\"</i> she says as she lines up your cock with her wet "
                            + "entrance. <i>\"Time for round 2.\"</i> She impales herself on your dick, crying out in delight. She leans forward and shakes her breasts in front of you until you grasp "
                            + "and fondle them. As she rides you, you feel your own pleasure building to a peak. "
                            + "Jewel's pussy squeezes your cock as she reaches her second climax, pushing you over the edge. She collapses on top of you, both of you satisfied and spent.";
        } else {
            return "Jewel trembles and her moans rapidly increase in pitch. You take a dominant position on top of her with one hand knuckles deep in her flooded pussy and the other groping "
                            + "her beautiful breast. She's obviously on the verge of defeat, but she refuses to give in. She grits her teeth and endures the pleasure while desperately grasping at your "
                            + "clit. You redouble your efforts, stroking and kissing her most sensitive spots. She arches her back and "
                            + "shudders in orgasm, then goes limp so suddenly you think she may have passed out. <p>When you lean over her to check that she's ok, she grabs you and rolls "
                            + "on top of you. She smashes her lips against yours and forces her tongue into your mouth. <i>\"You win round 1,\"</i> she says, pinning hands against the ground. "
                            + "<i>\"Time for round 2.\"</i> She presses her thigh against your slick pussy, and you find yourself arcing your back with pleasure. "
                            + "She rubs your sensitive clit with her leg and sucks on your neck until you can't resist grinding against her. She rides you sweetly, her pussy hot against your hip. "
                            + "When you are losing senses in your climax you feel her burning body sliding down your and she smothers your mouth with "
                            + " kisses while you climax and shudder. She collapses on top of you, both of you satisfied and spent.";
        }
    }

    @Override
    public String describe(Combat c) {
        if (character.has(Trait.fighter)) {
            return "Something has changed about Jewel's demeanor, though it's hard to put your finger on it. Her body has always been toned, but now she seems like a weapon "
                            + "in human shape. She carries a calm composure subtly different from her normal arrogance. Her movements are deliberate and fluid, like you imagine a "
                            + "martial arts master would look.";
        } else {
            return "Jewel has one of the most appropriate names you've ever. Her eyes are as bright green as emeralds and her long ponytailed hair is ruby red. The combination "
                            + "makes her strikingly beautiful despite not bothering with any make-up. Her body is fit and toned, with almost no fat. "
                            + "She practically radiates confidence. By her expression alone, it's like her victory is already assured.";
        }
    }

    @Override
    public String draw(Combat c, Result flag) {
        if (flag == Result.intercourse) {
            return "You and Jewel grind against each other, both on the verge of climax, both desperate to push the other over first. At the same instant you feel your ejaculation "
                            + "hit, Jewel tenses and cries out in ecstacy. The two of you spend your afterglow in a sweaty, sticky embrace before she pushes herself up and sits on your chest. "
                            + "<i>\"It's nice to find someone who can keep up with me.\"</i> She leans down and kisses you passionately, leaving you breathless when she finally lets you go. <i>\"Just "
                            + "remember that next time we fight, I'll make you beg for mercy.\"</i><p>She stands up, looking no worse for wear and gathers up all your clothes. She tosses you "
                            + "her clothes and walks away humming a tune to herself.";
        }
        return "Your fight with Jewel is quickly approaching the end. You can tell she's extremely aroused, but you're also on the verge of cumming. You need to get a dominant "
                        + "position and fast. You recklessly lunge at her and manage to knock her off her feet. Before she can recover, you press your advantage by lifting up her hips and "
                        + "pushing her knees up to her chest. With Jewel effectively pinned, you are free to eat out her defenseless pussy. You feel her whole body tremble as you circle your "
                        + "tongue around her swollen love bud.<p>Grappling with Jewel while your cock is ready to burst is certainly a risky gamble, but it paid off this time. Despite her "
                        + "persistent struggles, she has no way to retaliate from this position. You're sure you can finish her off before she manages to squirm free. Jewel grits her teeth "
                        + "to hold back her pleasure as you push your tongue inside her quiverring entrance. She really is persistent. She should just relax and enjoy her inevitable orgasm "
                        + "instead of continuing to struggle. Just as she starts to shudder in climax, she manages to jerk out of your grip. Before you can react, you feel a wave of pleasure "
                        + "overwhelm your senses.<p>You try to understand what just happened, still dazed from your unexpected orgasm. Jewel is lying on the floor in front of you with one "
                        + "hand on your dick and your semen splattered on her face. She must have grabbed your cock and finished you off the moment she broke free. That's quite a feat, considering"
                        + "she was mid-orgasm at the time. Jewel lets out an exhausted laugh as she wipes the cum off her face. <i>\"That was the best fight I've had in a long time. I couldn't "
                        + "keep from cumming, but I don't mind a draw when it's that intense.\"</i> ";
    }

    @Override
    public boolean fightFlight(Character opponent) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean attack(Character opponent) {
        // TODO Auto-generated method stub
        return true;
    }

    public double dickPreference() {
        return 5;
    }

    @Override
    public String victory3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return "Jewel grabs your cock firmly with one hand and your balls with the other. <i>\"I'll make you surrender with one of these. Which one do you want?\"</i> She sounds "
                            + "like she's joking, but her face tells you she's actually waiting for an answer. You don't want her to abuse your balls, so you swallow your pride and "
                            + "ask her to focus on your dick. She smiles, releases your ballsack and slowly pumps your dick. She maintains the slow pace until you're leaking pre-cum "
                            + "and suddenly stops. She wets her free hand with your liquid and begins polishing the sensitive head of your dick with her palm. The sensation is so strong that you try "
                            + "to pull away, but Jewel is holding your manhood securely and doesn't let it get away. She thoroughly tortures you with pure pleasure. You beg for mercy, but she doesn't let up until "
                            + "you cum in her hands.";
        }
        if (target.hasDick()) {
            return String.format(
                            "Jewel looks over %s, trying to decide what to do with %s."
                                            + " She stands up and presses her bare foot against %s dick and balls. "
                                            + "%s groans in pleasure and pain as Jewel roughly grinds her foot against "
                                            + "the sensitive organs. <i>\"Do you like that? You can't help it, can "
                                            + "you?\"</i> She grins sadistically. <i>\"I've stomped many boys into "
                                            + "the ground, but no matter how much pride they have, they always end "
                                            + "up moaning in pleasure. It's like penises exist just to be dominated.\""
                                            + "</i> You feel a chill run down your back, watching Jewel's display of"
                                            + " dominance, but you're also rock hard. %s lets out a loud moan and "
                                            + "covers Jewel's foot with cum.",
                            target.name(), target.directObject(), target.possessivePronoun(), target.name(),
                            target.name());
        }
        return "Jewel looks over " + target.name()
                        + ", trying to decide what to do with her. She leans in and kisses the helpless girl firmly on the lips. Breaking the kiss, "
                        + "she starts to probe and inspect " + target.name()
                        + "'s other lips, making her twitch and whimper with each touch. " + target.name()
                        + " apparently passes the inspection, "
                        + "because Jewel slides her hips forward and presses her own wet pussy against " + target.name()
                        + "'s. Both girls moan softly as Jewel begins moving her hips, grinding their lips and clits together. For a moment, "
                        + "you think Jewel's plan may backfire and she may cum first, but " + target.name()
                        + " soon shudders to climax in your arms. Jewel doesn't stop until she reaches "
                        + "her own orgasm too.";
    }

    @Override
    public String intervene3p(Combat c, Character target, Character assist) {
        Character other = c.getOpponent(character);
        String vulnerability = other.hasBalls() ? "balls. You fall to the floor"
                        : other.hasPussy() ? "pussy. You fall to the floor"
                                        : other.hasBreasts() ? "breasts. You cringe" : "face. You cower";
        if (target.human()) {
            return "You skillfully force " + assist.name()
                            + " to the floor and begin pleasuring her into submission. You rub and finger her pussy until she's shivering and "
                            + "juices flow down her thighs. Before you finish her off, you hear a whistle behind you. You turn and see Jewel standing behind you. Before you can do "
                            + "anything, she sweeps you legs out from under you and deposits you on the floor next to "
                            + assist.name() + ". She traps your arms in her thighs, leaving "
                            + "you defenseless. By this time, " + assist.name()
                            + " has recovered and looks ready to take revenge.<br>";
        } else {
            return "You wrestle with " + target.name()
                            + " until you're both naked and sweaty. You seem to have a slight advantage until she manages to get a free hand between "
                            + "your legs and slap your " + vulnerability + " in pain, but " + target.name()
                            + " doesn't have a chance to follow up. Jewel has arrived, seemingly out "
                            + "of nowhere, and before " + target.name()
                            + " can react, Jewel slaps her on the pussy. She crumples in pain, almost mirroring you, and can't put up any defense "
                            + "when Jewel restrains her arms. You pull yourself back up so you can take advantage of your helpless opponent.<br>";
        }
    }

    @Override
    public String startBattle(Character other) {
        return "Jewel approaches, looking confident and ready to fight.";
    }

    @Override
    public boolean fit() {
        return true;
    }

    @Override
    public String night() {
        return "You head back to your dorm after saying a quick goodnight to each of your opponents. The only expection is Jewel, who unfortunately appears to have left before "
                        + "you. When you get near the dorm, you discover why she left in such a hurry. You spot Jewel waiting in front of your dorm before she sees you. She's fidgetting "
                        + "and looking around constantly, showing none of the confidence and composure you've always seen from her. When you greet her, she keeps her eyes averted and "
                        + "hesitates before speaking. <i>\"You had a good match tonight, and I wanted to.... I thought maybe we could spend some more time together... just the two of us...\"</i> "
                        + "She trails off a couple time before something occurs to her. <i>\"Oh, sex! I thought we could have some more sex in your room and maybe... stay the night and... maybe "
                        + "cuddle?... just a bit?\"</i> You can't quite supress a laugh, which you feel guilty about when she blushes and lowers her eyes. Jewel is a unique kind of girl who won't "
                        + "hesitate to throw you down for wild sex, but turns into a nervous wreck at the thought of cuddling. <i>\"I'm still a girl, even if I don't always act like it! Is it so "
                        + "weird for me to want a cuddle sometimes?!\"</i> You kiss her gently and you can feel her calm down a bit. <i>\"I've never really stuck around after sex, but I'm starting to "
                        + "wake up in the morning wishing I had someone to hold me. Can I wake up in your arms tomorrow?\"</i> You kiss her again and lead her to your room.";
    }

    public void advance() {
        character.getGrowth().addTrait(10, Trait.fighter);
        character.body.addReplace(PussyPart.fiery, 100);
        character.unequipAllClothing();
        character.outfitPlan.add(Clothing.getByID("gi"));
        character.outfitPlan.add(Clothing.getByID("panties"));
        character.mod(Attribute.Ki, 1);
    }

    @Override
    public boolean checkMood(Combat c, Emotion mood, int value) {
        switch (mood) {
            case angry:
                return value >= 10;
            case dominant:
                return value >= 50;
            case nervous:
            default:
                return value >= 100;
        }
    }

    @Override
    public String orgasmLiner(Combat c) {
        return "Jewel glares at you after calming down, <i>\"That one doesn't count... Come on, let's have a rematch!\"</i>";
    }

    @Override
    public String makeOrgasmLiner(Combat c) {
        return "<i>\"Heh, no matter what, you're just a horny " + c.getOpponent(character)
                                                                   .boyOrGirl()
                        + " aren'tcha? Come on, no time for rest, let's see how many times in a row you can cum\"</i>";
    }
}
