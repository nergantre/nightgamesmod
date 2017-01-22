package nightgames.characters;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.FacePart;
import nightgames.characters.body.GenericBodyPart;
import nightgames.characters.body.TentaclePart;
import nightgames.characters.body.mods.GooeyMod;
import nightgames.characters.custom.CharacterLine;
import nightgames.combat.Combat;
import nightgames.combat.CombatScene;
import nightgames.combat.CombatSceneChoice;
import nightgames.combat.CombatantData;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.pet.arms.ArmManager;
import nightgames.stance.Engulfed;
import nightgames.start.NpcConfiguration;
import nightgames.status.Flatfooted;
import nightgames.status.SlimeMimicry;
import nightgames.status.Stsflag;

public class Airi extends BasePersonality {
    private static final long serialVersionUID = -8169646189131720872L;
    private static final String AIRI_SLIME_FOCUS = "AiriSlimeFocus";
    private static final String AIRI_MIMICRY_FOCUS = "AiriMimicryFocus";
    private static final String AIRI_REPLICATION_FOCUS = "AiriReplicationFocus";
    private static final String AIRI_TENTACLES_FOCUS = "AiriTentaclesFocus";

    public Airi() {
        this(Optional.empty(), Optional.empty());
    }

    public Airi(Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        super("Airi", charConfig, commonConfig, false);
        constructLines();
    }

    @Override
    public void applyStrategy(NPC self) {
        self.plan = Plan.retreating;
        self.mood = Emotion.nervous;
    }

    @Override
    public void applyBasicStats(Character self) {
        self.change();
        self.setTrophy(Item.AiriTrophy);
        preferredCockMod = CockMod.error;

        self.outfitPlan.add(Clothing.getByID("shirt"));
        self.outfitPlan.add(Clothing.getByID("bra"));
        self.outfitPlan.add(Clothing.getByID("panties"));
        self.outfitPlan.add(Clothing.getByID("skirt"));
        self.outfitPlan.add(Clothing.getByID("pantyhose"));
        self.outfitPlan.add(Clothing.getByID("shoes"));
        self.change();
        self.rank = 1;
        self.modAttributeDontSaveData(Attribute.Power, -1);
        self.modAttributeDontSaveData(Attribute.Slime, 1);
        self.modAttributeDontSaveData(Attribute.Cunning, 2);
        self.modAttributeDontSaveData(Attribute.Speed, -1);
        self.modAttributeDontSaveData(Attribute.Seduction, 6);
        self.getStamina().setMax(50);
        self.getArousal().setMax(80);
        self.getMojo().setMax(100);
        self.getWillpower().setMax(80);
        self.initialGender = CharacterSex.female;
        self.body.add(new FacePart(.1, 3.0));
    }

    private void constructLines() {
        character.addLine(CharacterLine.BB_LINER, (c, self, other) -> self.has(Trait.slime) ? "Airi grimaces as you fall. <i>\"Apologies... but necessary.... Please understand...\"</i>" : "<i>\"Sorry... I hope it didn't hurt too badly...\"</i>");
        character.addLine(CharacterLine.NAKED_LINER, (c, self, other) -> self.has(Trait.slime) ? "" : "<i>Nooo! Don't look at me!</i>");
        character.addLine(CharacterLine.STUNNED_LINER, (c, self, other) -> self.has(Trait.slime) ? "Airi glares at you from the puddle she formed on the floor. <i>\"Unforgivable...\"</i>" : "<i>\"Unforgivable...\"</i>");
        character.addLine(CharacterLine.TAUNT_LINER, (c, self, other) -> self.has(Trait.slime) ? "Airi coos to you <i>\"About to cum..? ...even trying..?\"</i>" : "<i><b>Airi giggles, </b> \"Try a bit harder okay?\"</i>");
        character.addLine(CharacterLine.TEMPT_LINER, (c, self, other) -> self.has(Trait.slime) ? "<i>\"Fill me... now...\"</i>" : "<i>\"Uhm, it's okay, you can come inside...\"</i>");
        character.addLine(CharacterLine.ENGULF_LINER, (c, self, other) -> "\n<i>\"It's done... over... stop struggling... cum.\"</i>");

        character.addLine(CharacterLine.ORGASM_LINER, (c, self, other) -> {
            if (self.has(Trait.slime)) {
                return "<i>\"Ahhnn... forgot how good... feels... Will return favor...\"</i>.";
            } else if (self.getWillpower().percent() > 90) {
                return "<i>\"Aah that was a bit too much! Slow down a bit...\"</i>";
            } else if (self.getWillpower().percent() > 75) {
                return "<i>\"Aaahhh... my head's feeling fuzzy...\"</i>";
            } else {
                return "<i>\"I need more... Give me more...\"</i>";
            }
        });
        character.addLine(CharacterLine.MAKE_ORGASM_LINER, (c, self, other) -> self.has(Trait.slime) ? "<i>\"...Feels good..? I'll suck more out... I'll milk you dry...\"</i>" : "<i>\"Hyaa! Oh please warn me next time...\"</i>");
        character.addLine(CharacterLine.NIGHT_LINER, (c, self, other) -> "You walk back to your dorm after the match, and decide to take a shower after all that exertion. Who knew sex fighting a bunch of girls would be so exhausting? You strip off your shirt and boxers and head straight into the bathroom. As you flip on the lights, you notice that the tub seems already filled with water. Just as you wonder if you forgot to drain the tub from last night, the liquid in the tub quivers and… stands up. "
                        + "<br/><br/>It’s Airi. What’s she doing here? You ask her how did get in, since you were sure the door was locked. <i>Followed you… flowed under door… No problem…</i> Well, that explains it. Noticing the time, you let her know that you really need to take your shower now and head to bed or you won’t make it tomorrow for your morning classes. Airi looks at you for a second and nods. <i>Un… will help you clean…</i> Wait what? Oh n-! Airi pulls you into the tub with her gooey appendages and submerges you inside her body. <i>Relax… I’ll clean you up… Inside and out…</i>");
        character.addLine(CharacterLine.CHALLENGE, (c, self, other) -> {
            if (other.human()) {
                return character.has(Trait.slime)
                                ? "Airi's main body rises up from her slime blob and forms the demure beauty you're used to seeing. <i>\"Delicious... Quickly... Give me your seed...\"</i>"
                                : "You're fighting Airi, a rather mysterious asian girl. She looks pretty normal for now, but you know she's holding a secret.";
            } else {
                return "<i>You... will do...</i>";
            }
        });
        character.addLine(CharacterLine.LEVEL_DRAIN_LINER, (c, self, other) -> {
            String part = Global.pickRandom(c.getStance().getPartsFor(c, self, other)).map(bp -> bp.describe(self)).orElse("pussy");
            if (character.has(Trait.slime)) {
                if (other.getLevel() < self.getLevel() - 5) {
                    return "{self:SUBJECT} tousles {other:possessive} hair fondly as {other:subject} cum, \"<i>How does it feel... reduced to mere prey...? You're just food... for me now...</i>\"";
                } else if (other.getLevel() >= self.getLevel()) {
                    return "{self:SUBJECT} looks euphoric as your strength is stolen by her demonic " + part + " \"<i>Ahh... your strength... feels good... You have more to spare... I need more!</i>\"";
                } else {
                    return "\"<i>{other:NAME}... Did you know...? Stronger than you now... But can't stop... Need all you have...</i>\"";
                }
            } else {
                if (other.getLevel() < self.getLevel() - 5) {
                    return "{self:SUBJECT} looks rather surprised as {other:subject} abruptly cum, \"<i>Kyaa! Sorry, I didn't notice you were so close. I guess this isn't much of a challenge any more...</i>\"";
                } else if (other.getLevel() >= self.getLevel()) {
                    return "{self:SUBJECT} looks euphoric as your strength is stolen by her demonic " + part + " \"<i>Ughhh {other:name}, that feels good... Please... more?</i>\"";
                } else {
                    return "\"<i>Ahh... <b>*giggle*</b> I think I've finally caught up... But keep going... I want more!</i>\"";
                }
            }
        });

        character.addLine(CharacterLine.DESCRIBE_LINER, (c, self, other) -> {
            return character.has(Trait.slime) ? "A crystal blue figure stands before you. Well, \"stands\" might be slightly incorrect. "
            + "Airi sports a cute face and a tight body, but her thighs end in a giant ball of slime. "
            + "Indeed, while her body might look human at a distance, she seems to be composed of a soft, translucent gel."
            : "Airi looks at you cautiously. The lithe Japanese girl sports a cute face and a tight body with shoulder length black hair "
                            + "almost covering her bright intelligent black eyes. You're not too sure what she's thinking "
                            + "so you approach her cautiously.";
        });
    }

    @Override
    public void setGrowth() {
        character.getGrowth().stamina = 1;
        character.getGrowth().arousal = 5;
        character.getGrowth().willpower = 2.5f;
        character.getGrowth().bonusStamina = 1;
        character.getGrowth().bonusArousal = 1;
        character.addCombatScene(new CombatScene((c, self, other) -> {
            return self.getLevel() >= 13 && self.has(Trait.slime) && !Global.checkFlag(AIRI_SLIME_FOCUS) && !Global.checkFlag(AIRI_MIMICRY_FOCUS);
        }, (c, self, player) -> {
                return Global.format("After the match, you spend a few minutes examining the slime girl, much to her disconcertment. "
                                + "It's quite curious how she can easily transform between slime and human form. "
                                + "Somehow, she can solidify her fluid composition into something indistinguishable from normal flesh and skin. "
                                + "You suppose it must be pretty convenient that she can hide her exotic attribute and live a normal life even after "
                                + "turning into such an oddity."
                                + "<br/><br/>"
                                + "Airi is a bit wary of your poking and prodding, but she seems to be content to let you do your experiment. "
                                + "After throughly feeling her unusual body up (strictly for science of course), "
                                + "you wonder if you could ask her to show you some of her abilities.", self, player);
            },
                Arrays.asList(
                        new CombatSceneChoice("Slime?", (c, self, other) -> {
                            c.write("You can't hold back your curiosity on the composition of her slime, so you ask Airi if you could try playing around with it a bit. "
                                  + "She seems a bit uncomfortable, but relents after you promise to take buy her a nice dinner some time--something that you probably wouldn't mind doing even without the favor. "
                                  + "Anyways, with her blessings, you reach your hand into her gelantinous body and insert it into her skin, elliciting a gasp from the asian girl. "
                                  + "Her gel body feels nice and cool on your hands, almost like soaking in an aloe vera bath. Flexing your hands a bit, you try grabbing the insides of her odd body. "
                                  + "You find that there are various areas of differing density inside her uniform looking anatomy. "
                                  + "Parts of her feel like jello and are impossible to get any kind of a grip on, while other parts feel more like squeezing soft resin--mostly hard but with some give. "
                                  + "<br/><br/>"
                                  + "Satisfied at your discoveries, you pull your hand out of the strange girl only to find that your hand is coated with a thin layer of her slime. "
                                  + "You try shaking it off but find the substance to be far stickier than you thought. You raise your head to ask Airi to help you out, "
                                  + "but you quickly find yourself facing a slightly pissed goo-girl. <i>\"Had... your fun..?\"</i> "
                                  + "She doesn't seem too pleased at your prodding, which does seems a bit unfair since you DID have a deal. Airi doesn't seem to care though, "
                                  + "and tackles you to the ground again with her gooey body. The crystal blue girl shows a mischevious smile and says "
                                  + "<i>Since so interested... in my slime... I'll let you... have your fun...</i> "
                                  + "Even though you expected something, Airi simply reforms her body and leaves after taunting you. "
                                  + "Wondering what that was all about, you try getting up only to find that she left a huge amount of slime still stuck to your skin. "
                                  + "What's worse, it's already beginning to harden, leaving you completely immobilized in what feels like a thin layer of plastic. Well shit.");
                            useSlime();
                            return true;
                        }),
                        new CombatSceneChoice("Transform?", (c, self, other) -> {
                            List<Character> lovers = Global.getMatchParticipantsInAffectionOrder().stream().filter(pa -> !pa.getType().equals(getType())).collect(Collectors.toList());
                            Character lover = Global.getCharacterByType(new Angel().getType());
                            if (!lovers.isEmpty()) {
                                lover = lovers.get(0);
                            }
                            String loverBodyString = "face";
                            String loverFormString = "familiar young {other:man}";
                            String loverTalkString = "says in a perfect replica of {other:name-possessive} voice,";
                            if (lover.getType().equals(Angel.class.getSimpleName())) {
                                loverBodyString = "glamorous body";
                                loverFormString = "blonde beauty";
                                loverTalkString = "purrs in {other:name-possessive} haughty voice,";
                            } else if (lover.getType().equals(Cassie.class.getSimpleName())) {
                                loverBodyString = "soft, lovely body";
                                loverFormString = "brown haired {other:girl}";
                                loverTalkString = "speaks in {other:name-possessive} soft voice,";
                            } else if (lover.getType().equals(Jewel.class.getSimpleName())) {
                                loverBodyString = "sleek, trained form";
                                loverFormString = "powerful redhead";
                                loverTalkString = "demands in {other:name-possessive} confident alto,";
                            } else if (lover.getType().equals(Mara.class.getSimpleName())) {
                                loverBodyString = "petite lithe limbs";
                                loverFormString = "black beauty";
                                loverTalkString = "asks in {other:name-possessive} mischevious voice,";
                            } else if (lover.getType().equals(Kat.class.getSimpleName())) {
                                loverBodyString = "cute cat ears";
                                loverFormString = "lovely kitten";
                                loverTalkString = "inquires in {other:name-possessive} hesistant voice,";
                            } else if (lover.getType().equals(Reyka.class.getSimpleName())) {
                                loverBodyString = "devilishly beautiful visage";
                                loverFormString = "familiar seductive form";
                                loverTalkString = "tempts in {other:name-possessive} melodious voice,";
                            }
                            c.write(Global.format("You ask Airi if she could transform into someone else, or if she's just limited to her human body and her slime form. "
                                            + "Intrigued, Airi asks you in her weird breathy voice, <i>\"Interesting... who shall I become...?\"</i> Immediately, "
                                            + "{other:name-possessive} %s comes to mind, so you ask Airi if she could try turning into {other:direct-object}. "
                                            + "The asian girl seems to pout for a split second before her body collapses into itself. "
                                            + "The surface of her body seems to churn and twist, warping itself into a new shape. "
                                            + "Soon, a %s stands before you, a perfect copy of {other:name-do}."
                                            + "<br/><br/>"
                                            + "Marveling at her powers of transformation, you examine her new form. After spending a good minute, "
                                            + "you realize there's actually no way you can tell the difference between them! "
                                            + "Unfortunately, Airi seems to have had enough with your little science experiement, and %s <i>\"Had enough yet? Well it's my turn now.\"</i> "
                                            + "Without giving you a chance to respond, the copycat slime jumps on you and forces you onto the ground for the nth time tonight. "
                                            + "<i>\""+other.getName()+"\", don't you think you stared a bit long at <b>{other:direct-object}</b>? "
                                            + "You should be a bit more considerate when alone with a woman.\"</i> Okay now that is plain unfair. "
                                            + "Even if you were oogling {other:name-possessive} body, technically it <i>IS</i> still Airi inside. "
                                            + "<i>\"Ah ah, I'll be sure to teach you not to cheat... Just remember, the next time you try sleeping with another {self:guy}, "
                                            + "it might just be me... And I'll make sure to pay you back...\"</i>"
                                            + "<br/><br/>"
                                            + "Okay. Now that is just creepy.", self, lover, 
                                            loverBodyString, loverFormString, loverTalkString));
                            useMimicry();
                            return true;
                        }),
                        new CombatSceneChoice("Both? [Hard Mode]", (c, self, other) -> {
                            c.write("You ask Airi if she would mind showing you everything she can do. After all, it's impossible not to ask when faced with such a mysterious body right?"
                                  + "Sadly that seemed to be the wrong thing to ask. The blue girl doesn't seem too happy with your probing and trips you with a errant tentacle pseudopod she formed on the spot. "
                                  + "Looming over you, the asian girl speaks <i>\"Share my secrets...? No. Have to... find out yourself...\"</i> Uh oh, seems like you pissed her off. This could be bad.");
                            useSlime();
                            useMimicry();
                            character.getGrowth().extraAttributes += 1;
                            // some compensation for the added difficulty. She gets 6 traits and 2 attribute points/level, and you only get 2 traits, but you are fighting more people than just her.
                            Global.getPlayer().getGrowth().addTraitPoints(new int[]{12,39},Global.getPlayer());
                            return true;
                        })
                    )
                ));
        character.addCombatScene(new CombatScene((c, self, other) -> {
            return self.getLevel() >= 22 && self.has(Trait.slime) && !Global.checkFlag(AIRI_REPLICATION_FOCUS) && !Global.checkFlag(AIRI_TENTACLES_FOCUS)
                            && (Global.checkFlag(AIRI_SLIME_FOCUS) || Global.checkFlag(AIRI_MIMICRY_FOCUS));
        }, (c, self, player) -> "It's been a while since Airi has joined you in the games and it definitely shows. "
                        + "She seems much more confident and more experienced with sex fighting. "
                        + "The changes doesn't just stop there at mental ones though. Airi also seems... bigger..? than before. "
                        + "Her slime form seems to have more volume in her feet/ball/whatever it is that she moves on. "
                        + "You wonder what it's for?",
                Arrays.asList(
                        new CombatSceneChoice("Reproduction?", (c, self, other) -> {
                            String msg = "Since slimes seems pretty similar to some of the single-celled organism you studied in biology, "
                                  + "maybe she is putting on mass to reproduce? Actually, how <b>DOES</b> a slime girl reproduce? "
                                  + "Does she split like an amoeba? or does she give birth like a normal human? She <i>does</i> have sexual organs, "
                                  + "you can definitely attest to that. Curiosity gets the better of you and you ask Airi how she reproduces. "
                                  + "While you didn't know that slimes could blush before, you sure do now. "
                                  + "The poor girl flushes right before your eyes, and her normally blue gel turns positively pink. "
                                  + "Hesitantly, Airi asks you, <i>\"Want to... find out?\"</i>"
                                  + "<br/><br/>"
                                  + "Well you definitely didn't manage to get into college without being inquistive. "
                                  + "You confidently nod yes to the slime girl, eager to see what she does. "
                                  + "Airi, with an unnaturally shy voice informs you, <i>\"I... need some help first...\"</i>. "
                                  + "Wait what? ";
                            if (other.hasDick()) {
                                msg += "Faster than you can react, a tentacle shoots out from Airi's body and attaches itself to your still sensitive cock. "
                                     + "A powerful suction forms inside the hollow pseudopod and in no time at all, it manages to milk out some of your pearly white seed. "
                                     + "The tentacle retracts back into her and you can see your cum floating around in her translucent body. "
                                     + "Airi starts to visibly shake as soon as her tentacle delivers your seed into her body; her gelatinous body seems to be almost vibrating."
                                     + "With a sudden cry, the slime girl buds off a chunk of slime that detaches itself from her main body. "
                                     + "The slime chunk lands a few feet away from her and quiveringly reforms into what seems like a younger version of the cerulean girl. "
                                     + "She seemed to have cloned herself!"
                                     + "<br/><br/>";
                            } else {
                                msg += "Faster than you can react, Airi rushes at you and pins you to the ground. With a groan, she forms a slimy psuedo-cock on her body and plunges it into your pussy. "
                                     + "Different from before, the asian girl seems to be more focused on her own pleasure, pinching her own nipples and teasing her gooey vagina with her hands. "
                                     + "Before long, she stiffens and shoots her gooey cum into your depths with a cute cry. Strangely though, her orgasm doesn't seem to stop when it usually does. "
                                     + "She just keeps pumping your womb full of her slime. Letting out a strangled cry, you realize that your belly is inflating from all the slime she's putting into you. "
                                     + "After ages, it seems to finally stop, and Airi pops her slime dick out of you. Unfortunately for you, there's no way you can move with so much slime in your womb. "
                                     + "Suddenly, you feel a movement inside your belly. The slime she pumped into you is alive! You try to force it out with your inner muscles. It seems to work; the slime "
                                     + "seems to gather itself and flows out of your birth canal in a rush. As soon as it leaves you, the slime quiveringly reforms into what seems like a younger version "
                                     + "of the cerulean girl. She seemed to have somehow cloned herself!"
                                     + "<br/><br/>";
                            }
                            msg += "Airi gives you a innocent smile and asks \"<i>Like her...? our daughter...</i>\" The younger girl seems to catch on as well, and craws into your lap, <i>\"...Papa?\"</i> "
                                     + "You're not proud of it, but in that moment you definitely panicked. While starting a family isn't entirely out of your mind, it's definitely something that you thought "
                                     + "would be in the <b>far</b> future, not something you just did one night in a round of sex games! Visibly flustered, you start babbling to her when she finally cracks "
                                     + "and both of them starts chuckling in her disconcertingly breathy voice. The two girls seems to press together, and with a small audiable pop, merge back into one being. "
                                     + "<i>\"Hah... hah... Don't worry, not a father... <b>yet</b>... But I'll have you... take responsibility...\"</i> Oh boy, you'll have to be a bit careful from now on.";
                            c.write(msg);
                            useReplication();
                            return true;
                        }),
                        new CombatSceneChoice("Tentacles?", (c, self, other) -> {
                            c.write(Global.format("You note the extra mass that Airi now has, and wonder what she could do with them. Now if you were being honest with yourself, you've always been a fan of "
                                            + "tentacle hentai. Something about the wiggling fleshy arms just flips your switch, you know? Since Airi's from Japan, she must be familiar with that particular "
                                            + "flavor of porn... right? Mustering up some courage, you ask Airi if she could form tentacles with her slime, because... you know, you'd find them sexy. "
                                            + "The positively withering look that she sends you unfortunately seems to indicate that she does not necessarily share your procivilities."
                                            + "<br/><br/>"
                                            + "The blue girl seems to sigh (does she even breathe?), and crawls towards you. <i>{other:NAME}... A pervert...?\"</i> Okay that one seems below the belt. "
                                            + "You reply that you guys are sex fighters for fucks sakes, is there any one of you that is <b>NOT</b> a pervert? Airi seems to deflate a bit at being indirectly called a pervert, "
                                            + "but shakes it off after a minute. Sighing again, she closes her eyes and quickly forms four tentacle arms out of her amorphous torso. "
                                            + "<i>\"Happy? The things... I do for you...\"</i> Grumbling, the slime girl crawls away."
                                            + "<br/><br/>"
                                            + "Well look who's a sourpuss.", self, other));
                            useTentacles();
                            return true;
                        }),
                        new CombatSceneChoice("Getting Fat? [Hard Mode]", (c, self, other) -> {
                            c.write("You ask Airi if she's getting putting on some pounds. Judging by the positively murderous look the crystal blue girl is giving you, that was <b>NOT</b> the correct "
                                            + "thing to say. Seriously though, what made you ask that? Why would you think that was a good idea? Airi starts chuckling maliciously, "
                                            + "<i>Hah... hah... fat... I am...? I'll have you... eat your words... You will... pay...</i> Okay definitely not a good idea. NOT A GOOD IDEA.");
                            useReplication();
                            useTentacles();
                            character.getGrowth().extraAttributes += 1;
                            // some compensation for the added difficulty. She gets 5 traits and 1 attribute point/level, and you only get 2 traits, but you are fighting more people than just her.
                            Global.getPlayer().getGrowth().addTraitPoints(new int[]{21,48},Global.getPlayer());
                            return true;
                        })
                    )
                ));

        character.getGrowth().addTrait(0, Trait.dexterous);
        character.getGrowth().addTrait(0, Trait.imagination);
        character.getGrowth().addTrait(0, Trait.softheart);
        character.getGrowth().addTrait(0, Trait.repressed);
        character.getGrowth().addTrait(3, Trait.Sneaky);
        character.getGrowth().addTrait(6, Trait.limbTraining1);
        character.getGrowth().addTrait(10, Trait.defthands);
        character.getGrowth().addTrait(16, Trait.calm);
        character.getGrowth().addTrait(31, Trait.nimbletoes);
        character.getGrowth().addTrait(37, Trait.sexTraining1);
        character.getGrowth().addTrait(46, Trait.responsive);
        character.getGrowth().addTrait(61, Trait.desensitized);

        preferredAttributes.add(c -> c.getPure(Attribute.Slime) < c.getLevel() * 1.5 ? Optional.of(Attribute.Slime) : Optional.empty());
    }

    private void useMimicry() {
        Global.setFlag(AIRI_MIMICRY_FOCUS, true);
        character.getGrowth().addTrait(13, Trait.Imposter);
        character.getGrowth().addTrait(19, Trait.ImitatedStrength);
        character.getGrowth().addTrait(34, Trait.ThePrestige);
        character.getGrowth().addTrait(55, Trait.Masquerade);
    }

    private void useSlime() {
        Global.setFlag(AIRI_SLIME_FOCUS, true);
        character.getGrowth().addTrait(13, Trait.VolatileSubstrate);
        character.getGrowth().addTrait(19, Trait.ParasiticBond);
        character.getGrowth().addTrait(34, Trait.PetrifyingPolymers);
        character.getGrowth().addTrait(55, Trait.EnduringAdhesive);
    }

    private void useReplication() {
        Global.setFlag(AIRI_REPLICATION_FOCUS, true);
        character.getGrowth().addTrait(22, Trait.BinaryFission);
        character.getGrowth().addTrait(28, Trait.RapidMeiosis);
        if (Global.checkFlag(AIRI_MIMICRY_FOCUS)) {
            character.getGrowth().addTrait(43, Trait.StickyFinale);
        }
        if (Global.checkFlag(AIRI_SLIME_FOCUS)) {
            character.getGrowth().addTrait(43, Trait.MimicBodyPart);
        }
        character.getGrowth().addTrait(49, Trait.HiveMind);
        character.getGrowth().addTrait(58, Trait.NoblesseOblige);
    }

    private void useTentacles() {
        Global.setFlag(AIRI_TENTACLES_FOCUS, true);
        character.getGrowth().addTrait(22, Trait.Pseudopod);
    }

    @Override
    public void eot(Combat c, Character opponent) {
        if (character.has(Trait.slime)) {
            if (character.hasPussy() && !character.body.getRandomPussy().moddedPartCountsAs(character, GooeyMod.INSTANCE)) {
                character.body.temporaryAddPartMod("pussy", GooeyMod.INSTANCE, 999);
                c.write(character, 
                                Global.format("{self:NAME-POSSESSIVE} %s re-slime-ified.",
                                                character, opponent, character.body.getRandomPussy()));
            }
            if (character.hasDick() && !character.body.getRandomCock().moddedPartCountsAs(character, CockMod.slimy)) {
                character.body.temporaryAddPartMod("cock", CockMod.slimy, 999);
                c.write(character, 
                                Global.format("{self:NAME-POSSESSIVE} %s re-slime-ified.",
                                                character, opponent, character.body.getRandomCock()));
            }
        }
    }

    @Override
    public void resolveOrgasm(Combat c, NPC self, Character opponent, BodyPart selfPart, BodyPart opponentPart, int times, int totalTimes) {
        int orgasmsToUnmask = self.has(Trait.Masquerade) ? 2 : 1;
        boolean unmaskable = self.is(Stsflag.disguised) && self.orgasms >= orgasmsToUnmask;
        if (times == totalTimes && ((self.getWillpower().percent() < 60 && !self.has(Trait.slime)) || unmaskable)) {
            boolean unmasked = false;
            if (unmaskable) {
                c.write(self, Global.format("<b>As {self:subject} orgasms, {self:possessive} whole body shimmers and seems to melt into a puddle of goo. "
                                + "A human body rises from the slime and molds itself to a facsimile of an all-too-familiar Asian {self:boy} giving you a self satisfied little smirk. "
                                + "Shit, {self:pronoun} tricked you, it was Airi all along!</b><br/>", self, opponent));
                opponent.add(c, new Flatfooted(opponent, 2));
                unmasked = true;
            } else {
                c.write(self, Global.format("After {self:name-possessive} orgasm, her whole body shimmers and melts into a puddle of goo. A human body rises from the slime and molds itself to a facsimile of {self:reflective}. "
                                + "Gone is the slim repressed girl you knew. The new Airi that appears before you is a sexually idealized version of herself, with bigger breasts, a dynamic body line and long legs that end in a ball of blue goo. "
                                + "You're now fighting {self:name} in slime form!", self, opponent));
            }
            self.completelyNudify(c);
            self.purge(c);
            self.addTemporaryTrait(Trait.slime, 999);
            self.removeTemporaryTrait(Trait.repressed, 999);
            self.removeTemporaryTrait(Trait.softheart, 999);
            if (self.hasPussy() && !self.body.getRandomPussy().moddedPartCountsAs(self, GooeyMod.INSTANCE)) {
                self.body.temporaryAddOrReplacePartWithType(self.body.getRandomPussy().applyMod(new GooeyMod()), 999);
            }
            if (self.hasDick() && !self.body.getRandomCock().moddedPartCountsAs(self, CockMod.slimy)) {
                self.body.temporaryAddOrReplacePartWithType(self.body.getRandomCock().applyMod(CockMod.slimy), 999);
            }
            BreastsPart part = self.body.getBreastsBelow(BreastsPart.h.size);
            if (part != null) {
                self.body.temporaryAddOrReplacePartWithType(part.upgrade(), 10);
            }
            self.body.temporaryAddOrReplacePartWithType(new GenericBodyPart("gooey skin", .5, 1.5, .8, "skin", ""), 999);
            self.body.temporaryAddOrReplacePartWithType(new TentaclePart("slime pseudopod", "back", "slime", 0.0, 1.0, 1.0), 999);
            if (self.level >= 25) {
                self.addTemporaryTrait(Trait.shameless, 999);
            }
            if (self.level >= 40) {
                self.addTemporaryTrait(Trait.Slippery, 999);
            }
            if (self.level >= 52) {
                self.addTemporaryTrait(Trait.strongwilled, 999);
            }
            CombatantData data = c.getCombatantData(self);
            if (self.has(Trait.Pseudopod) && data.getManager().getActiveArms().isEmpty()) {
                ArmManager manager = new ArmManager();
                manager.selectArms(self);
                c.write(self, "<b>"+manager.getActiveArms().size() + " tentacle arms erupt out of " + self.possessiveAdjective() + " back!</b>");
                data.setManager(manager);
            }
            if (unmasked && self.has(Trait.ThePrestige) && c.getStance().distance() < 2) {
                c.write(self, Global.format("<b>Taking advantage of {other:name-possessive} bewilderment, {self:subject-action:swoop} {self:possessive} slime onto {other:possessive} hapless form, swiftly engulfing it in {self:possessive} amorphous body.</b><br/>", self, opponent));
                c.setStance(new Engulfed(self, opponent));
            }
            self.moodSwing(c);
            self.update();
        }
    }

    @Override
    public void rest(int time) {
        super.rest(time);
        Decider.visit(character);
        int r;
        for (int i = 0; i < time; i++) {
            r = Global.random(8);
            if (r == 1) {
                Global.getDay().visit("Exercise", this.character, 0);
            } else if (r == 0) {
                Global.getDay().visit("Browse Porn Sites", this.character, 0);
            }
        }
    }

    @Override
    public String victory(Combat c, Result flag) {
        Character opponent;
        if (c.p1 == character) {
            opponent = c.p2;
        } else {
            opponent = c.p1;
        }
        character.arousal.empty();
        opponent.arousal.empty();
        if (character.is(Stsflag.disguised)) {
            StringBuilder sb = new StringBuilder();
            sb.append(Global.format("Just as {self:subject-action:are} about to bring you to mind bending orgasm, {self:possessive} face shifts and melts. "
                            + "One moment was staring at you haughtily like {self:pronoun} usually does, and the next, {self:possessive} entire body melts into a familiar cerulean goo. "
                            + "Fuck, it seems like Airi has managed to trick you! The semi-opaque slime girl just smiles at you rather sarcastically "
                            + "and taunts <i>\"You thought... {self:name}? Too bad...\"</i> You try to escape {self:possessive} hold on you, but you're way too far gone, "
                            + "and struggling against her gelatinous body is just bringing to closer to your inevitable defeat. "
                            + "Airi frowns when she notices you struggling, <i>\"Unhappy... with me? ...rather be with {self:name}?\"</i> "
                            + "You notice something dangerous in her wispy voice, and shake your head as fast as you can. Unfortunately it was a bit too late and you can definitely tell Airi's mood has gone sour.<br/><br/>"
                            + "<i>\"{other:name}... If rather have {self:name}... I oblige... But only {self:possessive} body...\"</i> Wait what is she saying? "
                            + "How can she only--Oh right. She can transform. Airi's crystalline body <i>shudders</i> and starts to regain a fleshy tone and texture "
                            + "starting from her feet. Slowly the change creeps up her body and stops at her neckline. From the neck up, Airi retains her slime-girl look complete with her piercing ruby eyes. "
                            + "However, below that, she reverted into a carbon-copy of {self:name}! The transformed girl leers at you and asserts <i>\"I'll show you... how much better I can be...\"</i>"
                            + "<br/><br/>", character, opponent));
            if (opponent.hasDick()) {
                if (!character.hasPussy()) {
                    sb.append("Airi furrows her faux-eyebrows in a look of intense concentration. A split second later, a newly form slit opens between her legs with an audiable pop. "
                                    + "Seeing your shocked expression, the lithe Japanese girl climbs ontop of you with an almost malicious grin. ");
                }
                sb.append(Global.format("You almost lose it right there as Airi lowers herself on top of you. Her mimicked {self:body-part:pussy} swallows your {other:body-part:cock} with ease, "
                                + "and immediately <i>tightens</i> around your fleshy rod. You gasp as her transformed cunt becomes narrower and narrower around you, "
                                + "completely wrapping around the neck of your penis in a chokehold. You beg her to stop, but the smug asian girl just shakes her head and replies <i>\"No... this is your... punishment.\"</i>"
                                + "While sitting motionlessly on top of you, Airi flexes her counterfeit vaginal muscles and starts milking your cock in a wave like motion. "
                                + "It feels less like you're having sex with her, and more like she's giving you the best handjob ever inside her wetness. "
                                + "However, every time you are just about to reach your climax, her perfectly controlled canal chokes the root of your cock and stops you from cumming. "
                                + "Soon you grow desperate at Airi's inhumane teasing and beg for her to let you cum. Luckily for you, that seems to be what she was waiting for. "
                                + "<i>\"Say it {other:name}... Who is better... me or {self:name}..?\"</i> At this point, you're deliriously aroused and cannot not but help screaming Airi's name into the night, "
                                + "hoping she'll take mercy on your poor abused dick. The gelatinous girl seems happy with your answer. She finally starts moving her hips and starts fucking you in earnest. "
                                + "After a few good pumps, Airi releases her death grip on your cock, and your thick sperm floods her emulated {self:body-part:pussy}. "
                                + "She nods happily and leans close to give you a peck on the lips, <i>\"Good... And don't forget it...\"</i>.", character, opponent));
            } else if (opponent.hasPussy()) {
                if (character.hasDick()) {
                    sb.append(Global.format("You almost lose it right there as Airi penetrates you. Her mimicked {self:body-part:cock} pierces your {other:body-part:pussy} with ease, ",
                                    character, opponent));
                } else {
                    sb.append(Global.format("Airi furrows her faux-eyebrows in a look of intense concentration. A split second later, an huge slime-dong erupts from her crotch. "
                                    + "Grinning with a barely concealed malicious glee, Airi pulls your legs apart and penetrates your soaked depths with her turgid penis. "
                                    + "Her new cock pierces your {other:body-part:pussy} with ease, ", character, opponent));
                }
                sb.append(Global.format("and immediately <i>expands</i> inside your drenched love canal. You gasp as her transformed cock completely fills your pussy, "
                                + "grinding against every bit of you. You beg her to stop, but the smug asian girl just shakes her head and replies <i>\"No... this is your... punishment.\"</i>"
                                + "Slowly at first, but with increasing tempo, Airi fucks you with her oversized penis. It's rather painful, but you can't deny that it's turning you on immensely. "
                                + "However, every time you are just about to reach your climax, her cock seems to disappear inside you like a punctured balloon, leaving you with an acute sense of loss. "
                                + "Soon you grow desperate at Airi's inhumane teasing and beg for her to let you cum. Luckily for you, that seems to be what she was waiting for. "
                                + "<i>\"Say it {other:name}... Who is better... me or {self:name}..?\"</i> At this point, you're deliriously aroused and cannot not but help screaming Airi's name "
                                + "into the night, hoping she'll take mercy on your poor tormented cunt. The gelatinous girl seems happy with your answer. "
                                + "She starts moving her hips again and finally fucks you in earnest. The piston action makes you cum almost instantly and continuously, shuddering on almost every return stroke. "
                                + "After a few dozen good pumps, Airi shudders as well and floods your {other:body-part:pussy} with her thick pseudo-sperm. "
                                + "Her scalding cum triggers a final climax from you and your arms desperately try to find a good place to grip as you cum hard around her pole yet again. "
                                + "After she confirms that you came, she nods happily and leans close to give you a peck on the lips, <i>\"Good... And don't forget it...\"</i>.", character, opponent));
            }
            return sb.toString();
        }
        if (character.has(Trait.slime)) {
            return "Airi crawls over to you at an agonizing pace. Her slime rapidly flows on top of your penis and covers it in a sticky bulb. <i>\"Time… for you to cum…\"</i><br/><br/>"
                            + "Her previously still slime suddenly starts to frantically squeeze and knead your cock, pulsating in waves of sticky goo on top of you. Startled by the sudden stimulation, you barely manage to hold on. Unfortunately--or perhaps fortunately--for you, Airi is not finished. She also covers your chest with her own sticky breasts and engulfs your nipples inside hers. Although it’s just slime, you feel as if her lips are on your nipples, sucking them and rolling the tips around inside her mouth.<br/><br/>"
                            + "As you’re being overloaded with sensations, Airi brings her face close to yours and whispers in your ear.<i>\"Cum… cum… cum…\"</i> With a groan of agonising pleasure, you come hard, firing ropes of your seed inside her translucent depths.<br/><br/>"
                            + "Panting with exertion from the aftershocks of your orgasm, you see your cum floating around in her body quickly getting absorbed and disappearing into nothing. Sensing danger, you glance at Airi's face <i>\"...Not enough... I need more food...\"</i><br/><br/>"
                            + "This time Airi engulfs your whole body, leaving only your face outside, facing the sky. Try as you might, you can't even move your neck to see what's happening below. Feeling frightened at what she might do, you tense up your body to attempt to resist. <i>\"Are you... ready..? I'll begin...\"</i> Whatever you expected, it was not this. Her whole body begins to churn around your own, both massaging and licking every square inch of you. You feel a tendril of slime enter your ass and press against your prostate. At the same time two tendrils of slime enter your ears and attach themselves to something deep inside your head. In seconds, you feel Airi literally inside your head.<br/><br/>"
                            + "<i>\"Cum...\"</i> An orgasm like nothing you felt before tears through your body, searing your head until your vision turns white.<br/><br/>"
                            + "<i>\"Cum...\"</i> Another climax wracks you, suspending all your thoughts.<br/><br/>"
                            + "<i>\"Cum...\"</i> Your cum turns thin, flowing out like water.<br/><br/>"
                            + "<i>\"Give me... everything...\"</i> One final orgasm leaves you out cold. When you come to, you see Airi has left, taking your boxers like that. "
                            + "Wow, you're not sure how many more of these you can endure.<br/><br/>";
        }

        String message = "";
        if (opponent.hasDick()) {
            message = "Airi looks triumphant as you spill your seed onto her small hands. ";
        } else {
            message = "Airi looks triumphant as your pussy convulses around her slim fingers. ";
        }
        return message + Global.format("<i>\"You know, before college I haven't even held another {other:boy}'s hand. "
                        + "My parents were really strict with me. Once I moved out and started school here, I jumped at joining the games when offered the chance. I lost a lot at first, "
                        + "but look at me now!\"</i> She gives your shrinking dick a little squeeze making you yelp. Smiling with satisfaction, Airi pulls your wrist with both hands and places it between her legs. "
                        + "<i>\"Now please... a little help here?\"</i>"
                        + "<br/><br/>"
                        + "You weren't about to refuse the winner's orders, so you dutifully play with her soaked cunt until she shudders quietly and cums as well. "
                        + "You're a bit peeved that you didn't even make Airi transform this time, but you can only try to do better when you meet her again.", character, opponent);
    }

    @Override
    public String defeat(Combat c, Result flag) {
        return "Fighting Airi is not easy. Her stickiness makes it"
                        + " quite difficult for you to accomplish much of anything. Still, "
                        + "considering her incoherent babbling she's probably not got much fight left in her. "
                        + "In a desperate attempt, she launces herself at you, knocking you both to the ground."
                        + " You react quickly, rolling over and getting on top before she can stick you to the "
                        + "floor. Her slime crawls up your sides, seeking to engulf and immobilize you, but you "
                        + "raise yourself up slightly, out of her reach. You reach down with one hand and deftly"
                        + " get to work on the area of slime shaped like a pussy. It's very different from a "
                        + "regular girl's, considering that your fingers actually <i>sink in</i> rather than graze"
                        + " over, and that she is sticky enough to prevent them from moving very fast. Still, "
                        + "it's clearly having the desired effect. You redouble your efforts, even leaning in to "
                        + "kiss her, not caring about the effects her slime has when ingested. Before long, Airi's "
                        + "mumbling reaches a crescendo, and she wraps her gooey limbs around you. Her body loses"
                        + " shapes as she cums, as if it's melting around you. Within a few seconds you are lying "
                        + "half-embedded in an amorphous heap of slime. Worried for Airi's safety, you jump up and call her name."
                        + " Fortunately, it is not long before she reassumes her human form with a look of ecstasy still"
                        + " on her face. <i>\"Wonderful... Reward...\"</i> she mutters, even breathier than usual. "
                        + "She gently pulls you back down onto her. Since you've already won, you do not resist as Airi "
                        + "lets you sink in to her body slightly. Nor when every bit of her body starts vibrating against "
                        + "your skin. The full-body massage is wonderful, especially when it starts focusing more and more "
                        + "on your dick. It's somehow ended up in her pussy, which is milking you greedily. Seeing no reason"
                        + " to stop her, you just relax in her embrace and let her slowly drive you closer. Right before you "
                        + "cum, she kisses you again, pushing you over the edge. There is no frantic milking this time, just "
                        + "a great orgasm. When you have both recovered, you get up and she hands you a small bit of slime "
                        + "that she separated from her body. <i>\"Trophy... No clothes... Will grow back...\"</i> You take "
                        + "the slime from her, examining it before stashing it away. It tingles in your hand, and you "
                        + "wonder just how much control she still has over it as she glides away.";
    }

    @Override
    public String draw(Combat c, Result flag) {
        return "[Placeholder] You make each other cum at the same time. Sorry.";

    }

    @Override
    public boolean fightFlight(Character opponent) {
        return !character.mostlyNude() || opponent.mostlyNude();
    }

    @Override
    public boolean attack(Character opponent) {
        return true;
    }

    @Override
    public String victory3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return "Airi crawls over to you at an agonizing pace. Her slime rapidly flows on top of your penis and covers it in a sticky bulb. <i>\"Time… for you to cum…\"</i><br/><br/>"
                            + "Her previously still slime suddenly starts to frantically squeeze and knead your cock, pulsating in waves of sticky goo on top of you. Startled by the sudden stimulation, you cum in seconds, spilling the proof of your defeat inside her tendril.<br/><br/>";
        } else {
            return "Airi flows over to " + target.getName()
                            + ". Her slime pools into a long and flexible appendage and worms itself inside "
                            + target.possessiveAdjective()
                            + " depths. The appendage starts to twist and squirm inside her poor victim and almost instantly causes her victim to scream out in pleasure.<br/><br/>";
        }
    }

    @Override
    public String intervene3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return Global.format(
                            "Your fight with {other:name} seemed to have ran into a stalemate. Neither of you is willing to get close enough to each other for anything substantial to happen. You just continue to trade taunts whilst waiting for an opportunity.<br/><br/>"
                                            + "Suddenly, you feel something grasp your ankles and pull you off balance. You brace yourself for the fall, but after a second, you only feel softness on your back. It’s Airi. She somehow snuck up on you and tripped you into falling on top of her. She quickly engulfs your hands and legs in her slime and presents your helpless body to {other:name}’s ministrations.",
                            character, assist);
        } else {
            return Global.format(
                            "Your fight with {other:name} seemed to have ran into a stalemate. Neither of you is willing to get close enough to each other for anything substantial to happen. You just continue to trade taunts whilst waiting for an opportunity.<br/><br/>"
                                            + "Suddenly, a blue blob appears in your line of sight. It’s Airi! More swiftly than you would expect, Airi moves to {other:name}’s side and engulfs her body in her own. After dissolving her clothing with her slime, Airi surfaces only {other:name-possessive} torso and sex, presenting her to you. Well, presented with a gift on a silver platter, you’re not going to refuse!",
                            character, target);
        }
    }

    @Override
    public boolean fit() {
        return !character.mostlyNude() && character.getStamina().percent() >= 50
                        || character.getArousal().percent() > 50;
    }

    public void advance() {

    }

    @Override
    public boolean checkMood(Combat c, Emotion mood, int value) {
        switch (mood) {
            case confident:
                return value >= 50;
            case desperate:
                return value >= 150;
            default:
                return value >= 100;
        }
    }

    @Override
    public String image(Combat c) {
        if (character.has(Trait.slime)) {
            SlimeMimicry mimicry = (SlimeMimicry) character.getStatus(Stsflag.mimicry);
            if (mimicry != null) {
                return "airi_" + mimicry.getMimickedName() + "_slime.jpg";
            } else {
                return super.image(c);
            }
        } else if (character.getWillpower().percent() > 90) {
            return "airi_human.jpg";
        } else if (character.getWillpower().percent() > 75) {
            return "airi_mostly_human.jpg";
        } else {
            return "airi_mostly_slime.jpg";
        }
    }
}
