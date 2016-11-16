package nightgames.characters;

import java.util.HashMap;
import java.util.Map;

import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.status.Lethargic;
import nightgames.status.Resistance;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public enum Trait {
    // Physical
    vaginaltongue("Vaginal Tongue", "Have a second tongue", (b, c, t) -> {
        if (c.crotchAvailable()) {
            b.append("Occasionally, a pink tongue slides out of her pussy and licks her lower lips.");
        }
    }),

    sadist("Sadist", "Skilled at providing pleasure alongside pain",
                    (b, c, t) -> b.append(Global.capitalizeFirstLetter(
                                    String.format("%s sneers in an unsettling way.", c.subject())))),
    bitingwords("Biting Words", "Knows how to rub salt in the wound."),
    smqueen("SM Queen", "A natural dom."),

    // Perks
    ticklemonster("Tickle Monster", "Skilled at tickling in unconventional areas"), // Mara Sex perk,
                                                                                    // increases
                                                                                    // pleasure from
                                                                                    // tickling if
                                                                                    // target is nude
    heeldrop("Heeldrop", "A wrestling move feared by men and women alike"), // Mara
                                                                            // Sparring
                                                                            // perk,
                                                                            // increases
                                                                            // damage
                                                                            // from
                                                                            // stomp
    spider("Spider", "Elaborate rope traps come naturally"), // Mara Gaming
                                                             // perk,
                                                             // Spiderweb
    experttongue("Expert Tonguework", "Can charm with oral pleasure"), // Angel
                                                                       // Sex
                                                                       // perk,
                                                                       // kiss
                                                                       // has
                                                                       // chance
                                                                       // to
                                                                       // inflict
                                                                       // Charm
    disciplinarian("Disciplinarian", "Frighteningly skilled at spanking"), // Angel
                                                                           // Sparring
                                                                           // perk,
                                                                           // spank
                                                                           // has
                                                                           // a
                                                                           // chance
                                                                           // to
                                                                           // inflict
                                                                           // Shame
    pokerface("Poker Face", "Bluff like a champion"), // Angel Gaming perk,
                                                      // Bluff
    silvertongue("Silvertongue", "Terrific tongue talent"), // Cassie Sex perk,
                                                            // increases
                                                            // pleasure from
                                                            // oral attacks
    judonovice("Judo Novice", "Basic understanding of judo"), // Cassie Sparring
                                                              // perk, Hip
                                                              // Throw
    misdirection("Misdirection", "They look left, you go right"), // Cassie
                                                                  // Gaming
                                                                  // perk,
                                                                  // Diversion
    dirtyfighter("Dirty Fighter", "Down, but not out"), // Jewel Sparring perk,
                                                        // kick can be used from
                                                        // prone
    spiral("Spiral", "Who the hell do you think I am?"), // Jewel Sex perk,
                                                         // Spiral Thrust
    fearless("Fearless", "Leeroy Jenkins"), // Jewel Gaming perk, Bravado
    clairvoyance("Clairvoyance", "Good with predicting and poker"), // Reyka
                                                                    // Sparring
                                                                    // perk
                                                                    // evasion
                                                                    // bonus
    locator("Locator", "Like a bloodhound"), // Reyka Gaming perk out of combat
                                             // action
    desensitized("Desensitized", "Sex is old hat now"), // Reyka Sex perk slight
                                                        // pleasure reduction
    desensitized2("Desensitized 2", "Only the strongest stimulation gets you off"),
    RawSexuality("Raw Sexuality", "Constant lust boost for you and your opponent in battle", (b, c, t) -> {
        if (c.human()) {
            b.append("You exude");
        } else {
            b.append(c.name() + " exudes");
        }
        b.append(" an aura of pure eros, making both of you flush with excitement.");
    }), // Eve
    affectionate("Affectionate", "Increased affection gain from draws"), // Kat
                                                                         // Sex
                                                                         // perk
    aikidoNovice("Aikido Novice", "Improved counterattack rate"), // Kat
                                                                  // Sparring
                                                                  // perk
    analFanatic("Anal Fanatic", "Lives for fucking ass"),

    // Passive Skills
    exhibitionist("Exhibitionist", "More effective without any clothes"), // Passively
                                                                          // builds
                                                                          // mojo
                                                                          // while
                                                                          // nude
    pheromones("Pheromones", "Scent can drive people wild", (b, c, t) -> {
        b.append("A primal musk surrounds ");
        if (c.human()) {
            b.append("your");
        } else {
            b.append(c.name() + "'s");
        }
        b.append(" body.");
    }), // causes horny in opponents if aroused
    augmentedPheromones("Augmented Pheromones", "Artificially enhanced pheromones", null, pheromones),

    magicEyeArousal("Magic Eyes: Arouse", "Eyes have a chance to arouse"),
    magicEyeEnthrall("Magic Eyes: Enthrall", "Eyes have a chance to enthrall"),
    magicEyeTrance("Magic Eyes: Trance", "Eyes have a chance to entrance"),
    magicEyeFrenzy("Magic Eyes: Frenzy", "Eyes have a chance to cause frenzy"),
    enchantingVoice("Enchanting Voice", "Willbending voice, can occasionally force a command."),
    soulsucker("Soulsucker", "Soul sucking lips"),
    entrallingjuices("Enthralling cum", "Enthralling juices"),
    frenzyingjuices("Frenzying juices", "Frenzying juices"),
    lacedjuices("Laced Juices", "Intoxicating bodily fluids"), // opponents take temptation when using oral skills
    addictivefluids("Addictive Fluids", "Addictive bodily fluids"), // opponents can only use oral skills if available
    temptingtits("Tempting Tits", "Perfectly shaped and oh-so-tempting."),
    beguilingbreasts("Beguiling Breasts", "Glamourous breasts can put you in trance"), // the first time in a fight that you see bare breasts you are entranced
    lactating("Lactating", "Breasts produces milk", new TraitDescription() {
        public void describe(StringBuilder b, Character c, Trait t) {
            if (!c.human()) {
                if (c.breastsAvailable()) {
                    b.append("You occasionally see milk dribbling down her breasts. Is she lactating?");
                } else {
                    b.append("You notice a damp spot on her " + c.getOutfit().getTopOfSlot(ClothingSlot.top).getName()
                                    + ".");
                }
            } else {
                b.append("Your nipples ache from the milk building up in your mammaries.");
            }
        }
    }),
    sedativecream("Sedative Cream", "Lactate that weakens the drinker"), // the first time in a fight that you see bare breasts you are entranced

    defthands("Deft hands", "They know where to go"), // hands damage upgrade
    nimbletoes("Nimble toes", "Good both in the street and in the bed."), // feet damage upgrade
    polecontrol("Pole Control", "Always hit the right spots"), // Dick damage upgrade
    hypnoticsemen("Hypnotic Semen", "Cum drains willpower"), // Semen willpower damage trait
    sweetlips("Sweet lips", "Enticing lips makes kissing dangerous"), // more kickback damage from kiss
    testosterone("Testosterone", "More powerful muscles"), // Having a cock gives + to power
    pussyhandler("Pussy Handler", "Expert at pleasing the pussy"), // Bonus damage to pussies
    dickhandler("Dick Handler", "Expert at pleasing cocks"), // Bonus damage to cocks
    asshandler("Ass Handler", "Adept at giving anal pleasure"), // Bonus damage to asses
    sexualmomentum("Sexual Momentum", "Causing an orgasm with penetration goes straight to your ego"), // You regain some willpower when you cause a climax while fucking someone
    anatomyknowledge("Anatomical Knowledge", "Advanced medical knowledge; find all the good spots"), // Increased damage when using finger / fuck skills
    druglacedprecum("Drug-laced Precum", "Drugs in your precum are perfect for increasing an enemies sensitivity"), // Anybody part that comes into contact with your precum becomes increasingly sensitive for x turns.
    magicmilk("Magicked Milk", "Magically augmented milk. It's a a strong addictive aphrodisiac, as well as a subtle hypnotic."), // .
    zealinspiring("Zeal Inspiring", "Instills true belief in people, inspiring them to follow her"),
    corrupting("Corrupting Influence", "Corrupts to the very core."),
    breeder("Breeder", "Particularly inviting"),
    mindcontroller("Mind Controller", "Can take control of others' minds. Inventive, yes?"),
    darkpromises("Dark Promises", "Can enthrall with the right words"), // whisper upgrade, can enthrall
    dominatrix("Dominatrix", "Relishes in hurting and humiliating partners."),
    
    energydrain("Energy Drain", "Drains energy during intercourse"),
    objectOfWorship("Object Of Worship", "Opponents is periodically forced to worship your body.",
                    (b, c, t) -> b.append("A divine aura surrounds " + c.nameDirectObject() + ".")),
    spiritphage("Semenphage", "Feeds on semen"),
    erophage("Erophage", "Feeds on sexuality"),
    tight("Tight", "Powerful musculature and exquisite tightness makes for quick orgasms."),
    holecontrol("Pussy Control", "Dexterous internal muscle control."),
    oiledass("Oiled Ass", "Natural oils makes her ass always ready."),
    autonomousAss("Autonomous Ass", "Asshole instinctively forces anything inside of it to cum."),
    fetishTrainer("Fetish Trainer", "Capable of developing others' fetishes."),
    insertion("Insertion Master", "More pleasure on insertion"), // more damage on insertion.
    hawkeye("Hawk Eye", "More accurate"), // 5% additional accuracy
    proheels("Heels Pro", "Pro at walking around in heels"), // no speed penalty from heels
    masterheels("Heels Master", "Master at moving in heels, resists knockdowns"), // graceful when wearing heels
    naturalgrowth("Natural Growth", "Always keeps up on levels"), // levels up to highest level + 2 after each night
    unnaturalgrowth("Unnatural Growth", "Always keeps up on levels"), // levels up to highest level + 5 after each night
    devoteeFervor("Devotee Fervor", "Pets can sometimes act twice"),
    congregation("Congregation", "Doubles the amount of allowed pets"),

    // training perks
    analTraining1("Anal Training 1", "Refined ass control."),
    analTraining2("Anal Training 2", "Perfected ass control."),
    analTraining3("Anal Training 3", "Godly ass control."),
    sexTraining1("Sex Training 1", "Refined sex techniques."),
    sexTraining2("Sex Training 2", "Perfected sex techniques."),
    sexTraining3("Sex Training 3", "Godly sex techniques."),
    tongueTraining1("Tongue Training 1", "Refined tongue control."),
    tongueTraining2("Tongue Training 2", "Perfected tongue control."),
    tongueTraining3("Tongue Training 3", "Godly tongue control."),
    limbTraining1("Hands&Feet Training 1", "Refined hands and feet control."),
    limbTraining2("Hands&Feet Training 2", "Perfected hands and feet control."),
    limbTraining3("Hands&Feet Training 3", "Godly hands and feet control."),

    // resistances
    freeSpirit("Free Spirit", "Better at escaping pins and resisting mind control"),
    calm("Calm", "Chance at resisting arousal over time"),
    skeptical("Skeptical", "Chance at resisting mental statuses"),
    graceful("Graceful", "Chance at resisting knockdowns"),
    steady("Steady", "Cannot be knocked down"),
    shameless("Shameless", "Impossible to embarrass"), // Eve

    cute("Innocent Appearance", "Reduction to opponent's power"),

    autonomousPussy("Autonomous Pussy", "Her pussy instinctively forces anything inside of it to cum."),
    // AI traits
    submissive("Submissive", "Enjoys being the sub."),
    naturalTop("Natural Top", "Being the dom comes easy."),
    obsequiousAppeal("Obsequious Appeal", "So tempting when on the bottom."),
    catstongue("Cat's Tongue", "Rough but sensual."),
    opportunist("Opportunist", "Always ready to stuff someone's backside."),
    carnalvirtuoso("Carnal Virtuoso", "Opponents cums twice"),
    toymaster("Toymaster", "Expert at using toys."),
    apostles("Apostles", "Endless followers"),
    sacrosanct("Sacrosanct", "Too sacred to hurt."),
    mandateOfHeaven("Mandate of Heaven", "Temptation demands worship."),
    piety("Piety", "If worshipping when dominating at sex, switch positions."),
    // Weaknesses
    ticklish("Ticklish", "Can be easily tickled into submission"), // more weaken damage and arousal from tickle
    insatiable("Insatiable", "One orgasm is never enough"), // arousal doesn't completely clear at end of match
    lickable("Lickable", "Weak against oral sex"), // more arousal from oral attacks
    imagination("Active Imagination", "More easily swayed by pillow talk"), // more temptation damage from indirect skills
    achilles("Achilles Jewels", "Delicate parts are somehow even more delicate"), // more pain from groin attacks
    naive("Naive", "Chance to not get cynical after mindgames"), // Chance to not get cynical after recovering from mindgames
    footfetishist("Foot Fetishist", "Loves those feet"), // Starts off each match with a foot fetish
    immobile("Immobile", "Unable to move"), // Cannot move
    lethargic("Lethargic", "Very low mojo gain from normal methods.", new Lethargic(null, 999, .75)), // 25% mojo gain
    hairtrigger("Hair Trigger", "Very quick to shoot. Not for beginners."),
    obedient("Obedient", "Easy to order around."),
    cursed("Cursed", "Restricts some skills. The name is probably a plot point. The suspense is killing me."),

    // Restrictions
    softheart("Soft Hearted", "Incapable of being mean"), // restricts slap, stomp, flick
    petite("Petite", "Small body, small breasts"), // restricts carry, tackle, paizuri
    undisciplined("Undisciplined", "Lover, not a fighter"), // restricts manuever, focus, armbar
    direct("Direct", "Patience is overrated"), // restricts whisper, dissolving trap, aphrodisiac trap, decoy, strip tease

    shy("Shy", "Seldom prone to shameless displays", new TraitDescription() {
        public void describe(StringBuilder b, Character c, Trait t) {
            if (c.human())
                b.append("You shy away from your opponent's gaze.");
            else
                b.append(c.name + " quickly avoids your gaze.");
        }
    }), // restricts striptease, flick, facesit, taunt, squeeze

    // Class
    madscientist("Mad Scientist", "May have gone overboard with her projects"),
    witch("Witch", "Learned to wield traditional arcane magic"),
    succubus("Succubus", "Embraced the dark powers that feed on mortal lust"),
    demigoddess("Demigoddess", "Blessed by a deity of sexual pleasure, and on the road to ascension herself."),
    fighter("Fighter", "A combination of martial arts and ki"),
    slime("Slime", "An accident in the biology labs made the body a bit more... malleable."),
    dryad("Dryad", "Part girl, part tree."),
    temptress("Temptress", "Well versed in the carnal arts."),
    ninja("Ninja", "A shadowy servant."),
    
    slimification("Slimification", "May fall into slime form on orgasm"),

    // Class subtrait
    divinity("Divinity", "Has aspects of divinity."),
    leveldrainer("Level Drainer", "Natrually adept at draining levels."),

    // Strength
    dexterous("Dexterous", "Dexterous limbs and fingers. Underwear is not an obstacle."),
                                                                                // digital
                                                                                // stimulation
                                                                                // through
                                                                                // underwear
    romantic("Romantic", "Every kiss is as good as the first"), // bonus to kiss
    experienced("Experienced Lover", "Skilled at pacing yourself when thrusting"), // reduced recoil from
                                                                                   // penetration
    wrassler("Wrassler", "A talent for fighting dirty"), // squeeze, knee, kick
                                                         // reduce arousal
                                                         // less
    pimphand("Pimp Hand", "A devastating slap and a bonus to hands"),
    stableform("Stable Form", "Cannot be affected by unexpected transformations"), // ignores
                                                                                   // thrown
                                                                                   // transformative
                                                                                   // drafts
                                                                                   // from the
                                                                                   // opponent
    brassballs("Brass Balls", "Can take a kick"),

    Clingy("Clingy", "Harder to escape"),
    fakeout("Fakeout", "Easier to counter"),
    repressed("Repressed", "Sexually represssed, lower seduction"),
    // Feats
    sprinter("Sprinter", "Better at escaping combat"),
    QuickRecovery("Quick Recovery", "Regain stamina rapidly out of combat"),
    Sneaky("Sneaky", "Easier time hiding and ambushing competitors"),
    PersonalInertia("Personal Inertia", "Status effects (positive and negative) last 50% longer"),
    Confident("Confident", "Mojo decays slower out of combat"),
    SexualGroove("Sexual Groove", "Passive mojo gain every turn in combat"),
    BoundlessEnergy("Boundless Energy", "Increased passive stamina gain in battle"),
    Unflappable("Unflappable", "Not distracted by being fucked from behind"),
    resourceful("Resourceful", "Chance to not consume an item on use"),
    treasureSeeker("Treasure Seeker", "Improved chance of opening item caches"),
    sympathetic("Sympathetic", "Intervening opponents are more likely to side with you"),
    fastLearner("Fast Learner", "Improved experience gain"),
    leadership("Leadership", "Summoned pets are more powerful"),
    tactician("Tactician", "Summoned pets have higher evasion"),
    faefriend("Fae Friend", "Less effort to summon Faeries"),
    fitnessNut("Fitness Nut", "More efficient at exercising"),
    expertGoogler("Expert Googler", "More efficient at finding porn"),
    mojoMaster("Mojo Master", "Max Mojo increases faster"),
    powerfulhips("Powerful Hips", "Can grind from submissive positions"),
    strongwilled("Strong Willed", "Lowers willpower loss from orgasms"),
    nymphomania("Nymphomania", "Restores willpower upon orgasm"),
    alwaysready("Always Ready", "Always ready for penetration", (b, c, t) -> {
        if (!c.hasDick() && c.crotchAvailable()) {
            b.append("Juices constainly drool from ");
            if (c.human()) {
                b.append("your slobbering pussy.");
            } else {
                b.append(c.name + "'s slobbering pussy.");
            }
        }
    }),
    revered("Revered", "Higher chance of worship"),
    cautious("Cautious", "Better chance of avoiding traps"),
    responsive("Responsive", "Return more pleasure when being fucked"),
    assmaster("Ass Master", "Who needs lube? Also boosts pleasure to both parties when assfucking"),
    
    // Jewel's unique traits
    powerfulcheeks("Powerful Cheeks", "As in asscheeks. Makes pulling out more difficult."),
    temptingass("Tempting Ass", "Opponent's can't help butt fuck it"), // ... sorry
    disablingblows("Disabling Blows", "Painful attacks may reduce Power"),
    takedown("Takedown", "Expert at tackling weary opponents"),
    indomitable("Indomitable", "Plainly refuses to be dominated"),
    confidentdom("Confident Dom", "Attributes rise while dominant"),
    drainingass("Draining Ass", "Taking it in the ass drains stamina an possibly Power"),
    edger("Edger", "Can keep oppoenents right at the edge"),
    commandingvoice("Commanding Voice", "Does not take 'no' for an answer"),
    mentalfortress("Mental Fortress", "Confident enough to have a chance to resist mind control"),
    bewitchingbottom("Bewitching Bottom", "Makes opponents go wild for ass"),
    unquestionable("Unquestionable", "Does not tolerate resistance when on top"),
    grappler("Grapller", "Bonus to hold strength"),
    
    // Item
    strapped("Strapped", "Penis envy", (b, c, t) -> {
        if (c.human()) {
            b.append("A large black strap-on dildo adorns your waist.");
        } else {
            b.append("A large black strap-on dildo adorns " + c.name() + "'s waists.");
        }
    }), // currently wearing a strapon

    event("event", "special character"),
    mindcontrolresistance("", "temporary resistance to mind games - hidden"),
    none("", "");
    private String desc;
    private TraitDescription longDesc;
    private String name;
    public Trait parent;
    public Status status;

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return name;
    }

    private Trait(String name, String description) {
        this.name = name;
        desc = description;
    }

    private Trait(String name, String description, TraitDescription longDesc) {
        this.name = name;
        desc = description;
        this.longDesc = longDesc;
    }

    private Trait(String name, String description, TraitDescription longDesc, Trait parent) {
        this.name = name;
        desc = description;
        this.longDesc = longDesc;
        this.parent = parent;
    }

    private Trait(String name, String description, Status status) {
        this.name = name;
        desc = description;
        this.status = status;
    }

    public boolean isFeat() {
        return compareTo(sprinter) >= 0 && compareTo(strapped) < 0;
    }

    public void describe(Character c, StringBuilder b) {
        if (longDesc != null) {
            longDesc.describe(b, c, this);
            b.append(' ');
        }
    }

    public static Map<Trait, Resistance> resistances;
    public static Resistance nullResistance;

    static {
        nullResistance = (c, s) -> "";
        resistances = new HashMap<>();
        resistances.put(shameless, (c, s) -> {
            if (s.flags().contains(Stsflag.shamed) || s.flags().contains(Stsflag.distracted)) {
                return "Shameless";
            }
            return "";
        });
        resistances.put(Trait.freeSpirit, (c, s) -> {
            // 30% to resist enthrall and bound
            if ((s.flags().contains(Stsflag.enthralled) || s.flags().contains(Stsflag.bound))
                            && Global.random(100) < 30) {
                return "Free Spirit";
            }
            return "";
        });
        resistances.put(Trait.calm, (c, s) -> {
            // 50% to resist horny and hypersensitive
            if ((s.flags().contains(Stsflag.horny) || s.flags().contains(Stsflag.hypersensitive))
                            && Global.random(100) < 50) {
                return "Calm";
            }
            return "";
        });
        resistances.put(Trait.skeptical, (c, s) -> {
            // 30% to resist mindgames
            if (s.mindgames() && Global.random(100) < 30) {
                return "Skeptical";
            }
            return "";
        });
        resistances.put(Trait.masterheels, (c, s) -> {
            // 33% to resist falling wearing heels
            if (c.has(ClothingTrait.heels) && s.flags().contains(Stsflag.falling) && Global.random(100) < 33) {
                return "Heels Master";
            }
            return "";
        });
        resistances.put(Trait.graceful, (c, s) -> {
            // 25% to resist falling
            if (s.flags().contains(Stsflag.falling) && Global.random(100) < 25) {
                return "Graceful";
            }
            return "";
        });
        resistances.put(Trait.steady, (c, s) -> {
            // 100% to resist falling
            if (s.flags().contains(Stsflag.falling)) {
                return "Steady";
            }
            return "";
        });
        resistances.put(Trait.naive, (c, s) -> {
            // 50% to resist Cynical
            if (s.flags().contains(Stsflag.cynical) && Global.random(100) < 50) {
                return "Naive";
            }
            return "";
        });
        resistances.put(Trait.mindcontrolresistance, (c, s) -> {
            // TODO: We should not be getting combat information from the gui; the gui should be focused on display and interaction.
           if (s.mindgames() && !Global.gui().combat.getOpponent(c).has(Trait.mindcontroller)) {
               float magnitude =
                               Global.getPlayer().getAddiction(AddictionType.MIND_CONTROL).map(Addiction::getMagnitude)
                                               .orElse(0f);
               float threshold = 40 * magnitude;
               if (Global.random(100) < threshold) {
                   return "Mara's Control";
               }
           }
           return "";
        });
        resistances.put(Trait.mentalfortress, (c, s) -> {
           if (s.mindgames() && (c.getStamina().percent()*3 / 4) > Global.random(100)) {
               return "Mental Fortress";
           }
           return "";
        });
    }

    public static Resistance getResistance(Trait t) {
        if (resistances.containsKey(t)) {
            return resistances.get(t);
        } else {
            return nullResistance;
        }
    }
}
